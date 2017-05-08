package com.hexandria.mechanics;

import com.hexandria.mechanics.avatar.UserAvatar;
import com.hexandria.mechanics.base.*;
import com.hexandria.mechanics.events.logic.Move;
import com.hexandria.mechanics.events.service.Error;
import com.hexandria.websocket.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 25.04.17.
 */
@SuppressWarnings("Duplicates")
public class Game {
    private List<UserAvatar> players;
    private String name;
    private ArrayList<UserAvatar> users;
    private int sizeX;
    private int sizeY;
    private Ceil map [][];

    public List<UserAvatar> getPlayers() {
        return players;
    }

    public Game(String name, int sizeX, int sizeY, List<UserAvatar> avatars){
        this.name = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.players = avatars;
        this.map = new Ceil[sizeX][sizeY];

        for(int i = 0; i < sizeX; ++i){
            for(int j = 0; j < sizeY; ++j){
                Coordinates coordinates = new Coordinates(i, j);
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

    public Message changeGameMap(Message message) {
        if(message.getClass() == Move.class && validate()) {
            Move move = ((Move) message);
            int fromX = move.getFrom().getX();
            int fromY = move.getFrom().getY();
            int toX = move.getTo().getX();
            int toY = move.getTo().getY();
            Squad moveableSquad = map[fromX][fromY].getSquad();
            map[fromX][fromY].setSquad(null);
            map[toX][toY].setSquad(moveableSquad);
            return message;
        }
        else{
            return new Error("Something went wrong in changeGameMap");
        }
    }

    public void printMap(){
        for(int i = 0; i < sizeX; ++i){
            for(int j = 0; j < sizeY; ++j){
                if(map[i][j].getSquad() == null){
                    System.out.print("0");
                }
                else{
                    System.out.print("1");
                }
            }
            System.out.println();
        }
    }

    public boolean validate(){
        return true;
    }
}
