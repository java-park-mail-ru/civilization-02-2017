package com.hexandria.mechanics;

import com.hexandria.mechanics.avatar.UserAvatar;
import com.hexandria.mechanics.base.*;
import com.hexandria.mechanics.events.logic.AttackTown;
import com.hexandria.mechanics.events.logic.Delete;
import com.hexandria.mechanics.events.logic.Move;
import com.hexandria.mechanics.events.logic.Update;
import com.hexandria.websocket.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by root on 25.04.17.
 */
@SuppressWarnings({"MagicNumber"})
public class Game {
    private final List<UserAvatar> players;
    private int sizeX;
    private int sizeY;
    private final Ceil[][] map;

    public Ceil[][] getMap() {
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

    public List<UserAvatar> getPlayers() {
        return players;
    }

    public Game(int sizeX, int sizeY, List<UserAvatar> avatars){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.players = avatars;
        this.map = new Ceil[sizeX][sizeY];

        for(int i = 0; i < sizeX; ++i){
            for(int j = 0; j < sizeY; ++j){
                final Coordinates coordinates = new Coordinates(i, j);
                if(i == j){
                    this.map[i][j] = new Town(coordinates, "Town" + (i * sizeX + j));
                }
                else{
                    this.map[i][j] = new Ceil(coordinates);
                }
            }
        }
        this.map[0][0] = new Capital(new Coordinates(0, 0), "Capital1", players.get(0));
        this.map[sizeX - 1][sizeY - 1] = new Capital(
                new Coordinates(sizeX - 1, sizeY - 1),
                "Capital2",
                players.get(2));
    }

    public Game(List<UserAvatar> players){
        this.sizeX = 10;
        this.sizeY = 15;
        this.map = new Ceil[sizeX][sizeY];
        for(int i = 0; i < sizeX; ++i){
            for(int j = 0; j < sizeY; ++j){
                map[i][j] = new Ceil(new Coordinates(i, j));
            }
        }
        map[0][0] = new Capital(new Coordinates(0, 0), "capital1", players.get(0));
        map[9][14] = new Capital(new Coordinates(9, 14), "capital2", players.get(1));
        map[2][3] = new Town(new Coordinates(2, 3), "Town1");
        map[7][8] = new Town(new Coordinates(7, 8), "Town2");
        map[2][1].setSquad(new Squad(10, 5, players.get(0)));
        map[1][1].setSquad(new Squad(20, 11, players.get(0)));
        map[3][4].setSquad(new Squad(17, 20, players.get(1)));
        map[4][4].setSquad(new Squad(50, 10, players.get(1)));
        map[3][4].setSquad(new Squad(17, 20, players.get(1)));
        map[5][5].setSquad(new Squad(41, 7, players.get(1)));
        this.players = players;
    }

    public List<Message> interact(Message message) {
        if(message.getClass() == Move.class && validate()) {
            final Move move = ((Move) message);
            final Ceil toCeil = map[move.getTo().getX()][move.getTo().getY()];
            final Ceil fromCeil = map[move.getFrom().getX()][move.getFrom().getY()];

            if(toCeil.getSquad() == null && toCeil.getClass() == Ceil.class){
                return move(move, fromCeil, toCeil);
            }

            else if(Objects.equals(toCeil.getSquad().getOwner(), fromCeil.getSquad().getOwner())){
                return mergeSquads(fromCeil, toCeil);
            }
            else if(!Objects.equals(toCeil.getSquad().getOwner(), fromCeil.getSquad().getOwner())){
                return fight(move, fromCeil, toCeil);
            }
            else
                return null;

        }
        else{
            return null;
        }
    }

    public List<Message> fight(Move move, Ceil fromCeil, Ceil toCeil){
        final List<Message> events = new LinkedList<>();
        //Attacker lost
        if(fromCeil.getSquad().getCount() < toCeil.getSquad().getCount()){
            toCeil.getSquad().setCount(toCeil.getSquad().getCount() - fromCeil.getSquad().getCount());
            fromCeil.setSquad(null);
            events.add(new Delete(fromCeil.getPosition()));
            events.add(new Update(
                    toCeil.getPosition(),
                    null,
                    toCeil.getSquad().getCount(),
                    toCeil.getSquad().getMorale()
                    )
            );
        }
        //Attacker won
        else if(fromCeil.getSquad().getCount() > toCeil.getSquad().getCount()){
            final Squad toSquad = toCeil.getSquad();
            final Squad fromSquad = fromCeil.getSquad();
            fromCeil.setSquad(null);
            toCeil.setSquad(fromSquad);
            toCeil.getSquad().setCount(fromCeil.getSquad().getCount() - toSquad.getCount());
            events.add(new Delete(toCeil.getPosition()));
            events.add(new Update(
                    fromCeil.getPosition(),
                    toCeil.getPosition(),
                    toCeil.getSquad().getCount(),
                    toCeil.getSquad().getMorale()));
            if(toCeil.getClass() == Town.class){
                final Town capturedTown = (Town) toCeil;
                capturedTown.setOwner(fromSquad.getOwner());
                events.add(new AttackTown(capturedTown.getPosition(), fromSquad.getOwner()));
            }
        }
        //Draw
        else {
            fromCeil.setSquad(null);
            toCeil.setSquad(null);
            events.add(new Delete(fromCeil.getPosition()));
            events.add(new Delete(toCeil.getPosition()));
        }
        return events;
    }

    public List<Message> mergeSquads(Ceil fromCeil, Ceil toCeil){
        final Squad fromSquad = fromCeil.getSquad();
        fromCeil.setSquad(null);
        toCeil.getSquad().mergeSquads(fromSquad);
        final List<Message> events = new LinkedList<>();
        events.add(new Update(fromCeil.getPosition(),
                toCeil.getPosition(),
                toCeil.getSquad().getCount(),
                toCeil.getSquad().getMorale()));
        events.add(new Delete(fromCeil.getPosition()));
        return events;
    }

    public List<Message> move(Move move, Ceil fromCeil, Ceil toCeil){
        final Squad moveableSquad = fromCeil.getSquad();
        fromCeil.setSquad(null);
        toCeil.setSquad(moveableSquad);
        final List<Message> events = new LinkedList<>();
        events.add(new Update(
                fromCeil.getPosition(),
                toCeil.getPosition(),
                null,
                null));
        events.add(new Delete(fromCeil.getPosition()));
        return events;
    }

    public boolean validate(){
        return true;
    }
}
