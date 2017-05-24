package com.hexandria.mechanics;


//TODO Remove hardcode from map generation

import com.hexandria.mechanics.player.GamePlayer;
import com.hexandria.mechanics.base.*;
import com.hexandria.mechanics.events.game.GameResult;
import com.hexandria.mechanics.events.logic.AttackTown;
import com.hexandria.mechanics.events.logic.Delete;
import com.hexandria.mechanics.events.logic.Move;
import com.hexandria.mechanics.events.logic.Update;
import com.hexandria.websocket.Message;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by root on 25.04.17.
 */
@SuppressWarnings({"MagicNumber"})
public class Game {
    private final List<GamePlayer> players;
    private int sizeX;
    private int sizeY;
    private final Cell[][] map;
    private final Date latestTurnStart;
    private int currentPlayerId;

    public Date getLatestTurnStart() {
        return latestTurnStart;
    }

    public void finishTurn() {
        currentPlayerId += 1 % (players.size() - 1);
        latestTurnStart.setTime(System.currentTimeMillis());
    }

    public Cell[][] getMap() {
        return map;
    }

    public int getSizeX() {

        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public Game(List<GamePlayer> players) {
        this.sizeX = 10;
        this.sizeY = 15;
        this.map = new Cell[sizeX][sizeY];
        for (int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                map[i][j] = new Cell(new Coordinates(i, j));
            }
        }
        map[0][0] = new Capital(new Coordinates(0, 0), "capital1", players.get(0));
        map[8][13] = new Capital(new Coordinates(8, 13), "capital2", players.get(1));
        map[2][3] = new Town(new Coordinates(2, 3), "Town1");
        map[7][8] = new Town(new Coordinates(7, 8), "Town2");
        this.players = players;
        latestTurnStart = new Date();
        currentPlayerId = 0;
    }

    public List<Message> interact(Message message) {
        if (message.getClass() == Move.class && validate()) {
            final Move move = ((Move) message);
            final Cell toCell = map[move.getTo().getX()][move.getTo().getY()];
            final Cell fromCell = map[move.getFrom().getX()][move.getFrom().getY()];

            if (toCell.getSquad() == null && toCell.getClass() == Cell.class) {
                return move(fromCell, toCell);
            } else if (toCell.getSquad() == null && toCell.getClass() == Town.class) {
                return captureEmptyTown(fromCell, toCell);
            } else if (Objects.equals(toCell.getSquad().getOwner(), fromCell.getSquad().getOwner())) {
                return mergeSquads(fromCell, toCell);
            } else if (!Objects.equals(toCell.getSquad().getOwner(), fromCell.getSquad().getOwner())) {
                return fight(fromCell, toCell);
            } else if (toCell.getClass() == Capital.class && ((Capital) toCell).getOwner() != fromCell.getSquad().getOwner()) {
                List<Message> events = new LinkedList<>();
                events.add(new GameResult(fromCell.getSquad().getOwner(), toCell.getSquad().getOwner(), "Capital captured"));
                return events;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<Message> captureEmptyTown(Cell fromCell, Cell toCell) {
        Town town = (Town) toCell;
        town.setOwner(fromCell.getSquad().getOwner());
        town.setSquad(fromCell.getSquad());
        fromCell.setSquad(null);
        List<Message> messages = new LinkedList<>();
        messages.add(new AttackTown(toCell.getPosition(), toCell.getSquad().getOwner()));
        messages.add(new Update(fromCell.getPosition(), toCell.getPosition(), null, null));
        toCell.getSquad().setMoved(true);
        return messages;
    }

    public List<Message> fight(Cell fromCell, Cell toCell) {
        final List<Message> events = new LinkedList<>();
        //Attacker lost
        if (fromCell.getSquad().getCount() < toCell.getSquad().getCount()) {
            toCell.getSquad().setCount(toCell.getSquad().getCount() - fromCell.getSquad().getCount());
            fromCell.setSquad(null);
            events.add(new Delete(fromCell.getPosition()));
            events.add(new Update(
                            toCell.getPosition(),
                            null,
                            toCell.getSquad().getCount(),
                            toCell.getSquad().getMorale()
                    )
            );
        }
        //Attacker won
        else if (fromCell.getSquad().getCount() > toCell.getSquad().getCount()) {
            final Squad toSquad = toCell.getSquad();
            final Squad fromSquad = fromCell.getSquad();
            fromCell.setSquad(null);
            toCell.setSquad(fromSquad);
            toCell.getSquad().setCount(fromSquad.getCount() - toSquad.getCount());
            toCell.getSquad().setMoved(true);
            if (toCell.getClass() == Capital.class) {
                events.add(new GameResult(fromSquad.getOwner(), toSquad.getOwner(), "Capital captured"));
                return events;
            }
            events.add(new Delete(toCell.getPosition()));
            events.add(new Update(
                    fromCell.getPosition(),
                    toCell.getPosition(),
                    toCell.getSquad().getCount(),
                    toCell.getSquad().getMorale()));
            //If attacker attacked town - set new owner of town;
            if (toCell.getClass() == Town.class) {
                final Town capturedTown = (Town) toCell;
                capturedTown.setOwner(fromSquad.getOwner());
                events.add(new AttackTown(capturedTown.getPosition(), fromSquad.getOwner()));
            }
        }
        //Draw
        else {
            fromCell.setSquad(null);
            toCell.setSquad(null);
            events.add(new Delete(fromCell.getPosition()));
            events.add(new Delete(toCell.getPosition()));
        }
        return events;
    }

    public List<Message> mergeSquads(Cell fromCell, Cell toCell) {
        final Squad fromSquad = fromCell.getSquad();
        fromCell.setSquad(null);
        toCell.getSquad().mergeSquads(fromSquad);
        toCell.getSquad().setMoved(true);
        final List<Message> events = new LinkedList<>();
        events.add(new Update(fromCell.getPosition(),
                toCell.getPosition(),
                toCell.getSquad().getCount(),
                toCell.getSquad().getMorale()));
        events.add(new Delete(fromCell.getPosition()));
        return events;
    }

    public List<Message> move(Cell fromCell, Cell toCell) {
        final Squad moveableSquad = fromCell.getSquad();
        toCell.setSquad(moveableSquad);
        final List<Message> events = new LinkedList<>();
        events.add(new Update(
                fromCell.getPosition(),
                toCell.getPosition(),
                null,
                null));
        events.add(new Delete(fromCell.getPosition()));
        fromCell.setSquad(null);
        toCell.getSquad().setMoved(true);
        return events;
    }

    public boolean validate() {
        return true;
    }
}
