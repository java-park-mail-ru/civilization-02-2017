package com.hexandria.mechanics.base;

import com.hexandria.mechanics.avatar.UserAvatar;

import java.util.ArrayList;

/**
 * Created by root on 23.04.17.
 */
public class GameMap {
    private String name;
    private ArrayList<UserAvatar> users;
    private int sizeX;
    private int sizeY;
    private IGameCeil map [][];

    public GameMap(String name, int sizeX, int sizeY){
        this.name = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.map = new IGameCeil[sizeX][sizeY];

        for(int i = 0; i < sizeY; ++i){
            for(int j = 0; j < sizeX; ++j){
                if(i == j){
                    this.map[j][i] = new City(j, i);
                }
                else{
                    this.map[j][i] = new GameCeil(j, i);
                }
            }
        }
        this.map[0][0] = new CapitalCity(0, 0, users.get(0));
        this.map[sizeX - 1][sizeY - 1] = new CapitalCity(sizeX - 1, sizeY - 1, users.get(1));
    }
}
