package com.hexandria.mechanics;

import com.hexandria.mechanics.avatar.UserAvatar;
import com.hexandria.mechanics.base.Capital;
import com.hexandria.mechanics.base.Ceil;
import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.mechanics.base.Town;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 25.04.17.
 */
public class Game {
    private List<UserAvatar> players;
    private String name;
    private ArrayList<UserAvatar> users;
    private int sizeX;
    private int sizeY;
    private Ceil map [][];

    public Game(String name, int sizeX, int sizeY, List<UserAvatar> avatars){
        this.name = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.players = avatars;
        this.map = new Ceil[sizeX][sizeY];

        for(int i = 0; i < sizeY; ++i){
            for(int j = 0; j < sizeX; ++j){
                Coordinates coordinates = new Coordinates(j, i);
                if(i == j){
                    this.map[j][i] = new Town(coordinates, "Town" + (i * sizeX + j));
                }
                else{
                    this.map[j][i] = new Ceil(coordinates);
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
        this.map = new Ceil[10][15];
        for(int i = 0; i < 10; ++i){
            for(int j = 0; j < 14; ++j){
                map[i][j] = new Ceil(new Coordinates(i, j));
            }
        }
        map[0][0] = new Capital(new Coordinates(0, 0), "capital1", players.get(0));
        map[9][14] = new Capital(new Coordinates(9, 14), "capital2", players.get(1));
        map[2][3] = new Town(new Coordinates(2, 3), "Town1");
        map[7][8] = new Town(new Coordinates(7, 8), "Town2");
        this.players = players;
    }

    public void changeGameMap(Logic event) {
        System.out.println("In game map: " + event.toString());
    }
}
