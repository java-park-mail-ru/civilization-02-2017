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
    }
}
