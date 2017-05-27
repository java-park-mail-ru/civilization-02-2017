package com.hexandria.mechanics;


//TODO Remove hardcode from map generation

import com.hexandria.mechanics.base.*;
import com.hexandria.mechanics.events.game.GameResult;
import com.hexandria.mechanics.events.game.Turn;
import com.hexandria.mechanics.events.logic.*;
import com.hexandria.mechanics.player.GamePlayer;
import com.hexandria.websocket.Message;
import org.jetbrains.annotations.Nullable;

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
    private long latestTurnStart;
    private int currentPlayerId;

    public synchronized long getLatestTurnStart() {
        return latestTurnStart;
    }

    public synchronized List<Message> finishTurn() {
        currentPlayerId = (currentPlayerId + 1) % players.size() ;
        latestTurnStart = System.currentTimeMillis();
        final List<Message> newTurnMessages = new LinkedList<>();
        newTurnMessages.add(new Turn(new Turn.Payload()));
        for(int i = 0; i < sizeX; ++i){
            for(int j = 0; j < sizeY; ++j){
                if(map[i][j].getClass() == Town.class && ((Town) map[i][j]).getOwner() != null){
                    if(map[i][j].getSquad() != null){
                        ((Town) map[i][j]).generateSquads();
                        newTurnMessages.add(new Update((Town)map[i][j]));
                    }
                    else{
                        ((Town) map[i][j]).generateSquads();
                        newTurnMessages.add(new Create((Town) map[i][j]));
                    }
                }
                else if(map[i][j].getClass() == Capital.class){
                    if(map[i][j].getSquad() != null){
                        ((Capital) map[i][j]).generateSquads();
                        newTurnMessages.add(new Update((Capital)map[i][j]));
                    }
                    else{
                        ((Capital) map[i][j]).generateSquads();
                        newTurnMessages.add(new Create((Capital) map[i][j]));
                    }
                }

                if(map[i][j].getSquad() != null){
                    map[i][j].getSquad().setMoved(false);
                }
            }
        }
        return newTurnMessages;
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
        System.out.println(players.get(0).getName());
        map[2][3] = new Town(new Coordinates(2, 3), "Town1");
        map[7][8] = new Town(new Coordinates(7, 8), "Town2");
        this.players = players;
        latestTurnStart = System.currentTimeMillis();
        currentPlayerId = 0;
    }

    @Nullable
    public synchronized List<Message> interact(Message message, Long userID) {
        if (message.getClass() == Move.class && validate(userID, (Move) message)) {
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
            } else if ((toCell.getClass() == Capital.class) && !Objects.equals(((Capital) toCell).getOwner(), fromCell.getSquad().getOwner())) {
                final List<Message> events = new LinkedList<>();
                events.add(new GameResult(fromCell.getSquad().getOwner(), toCell.getSquad().getOwner(), "Capital captured"));
                return events;
            } else {
                return null;
            }
        }
        else if(message.getClass() == Turn.class && Objects.equals(userID, players.get(currentPlayerId).getId())) {
            return finishTurn();
        }
        else{
            return new LinkedList<>();
        }
    }

    public List<Message> captureEmptyTown(Cell fromCell, Cell toCell) {
        final Town town = (Town) toCell;
        town.setOwner(fromCell.getSquad().getOwner());
        town.setSquad(fromCell.getSquad());
        fromCell.setSquad(null);
        final List<Message> messages = new LinkedList<>();
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
        events.add(new Update(toCell.getPosition(),
                null,
                toCell.getSquad().getCount(),
                toCell.getSquad().getMorale()));
        events.add(new Delete(fromCell.getPosition()));
        return events;
    }

    public List<Message> move(Cell fromCell, Cell toCell) {
        toCell.setSquad(fromCell.getSquad());
        fromCell.setSquad(null);
        final List<Message> events = new LinkedList<>();
        events.add(new Update(
                fromCell.getPosition(),
                toCell.getPosition(),
                null,
                null));
        toCell.getSquad().setMoved(true);
        return events;
    }

    public boolean validate(Long userID, Move move) {
        final Coordinates from = move.getFrom();
        final Coordinates to = move.getTo();
        //noinspection OverlyComplexBooleanExpression
        if(from.getX() < 0 || from.getY() < 0 || to.getX() < 0 || to.getY() < 0){
            return false;
        }
        //noinspection OverlyComplexBooleanExpression
        if(from.getX() >= sizeX || from.getY() >= sizeY || to.getX() >= sizeX || from.getY() >= sizeY){
            return false;
        }
        if(Math.abs(to.getX() - from.getX()) > 1 || (Math.abs(to.getY() - from.getY()) > 1)){
            return false;
        }
        if(!(Objects.equals(userID, players.get(currentPlayerId).getId()))){
            return false;
        }
        return !(map[from.getX()][from.getY()].getSquad() == null
                || map[from.getX()][from.getY()].getSquad().getMoved());
    }
}
