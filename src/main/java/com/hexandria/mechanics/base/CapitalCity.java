package com.hexandria.mechanics.base;

import com.hexandria.mechanics.avatar.UserAvatar;

/**
 * Created by root on 23.04.17.
 */
public class CapitalCity extends City {

    public CapitalCity(int coordX, int coordY, UserAvatar owner){
        super(coordX, coordY);
        this.owner = owner;
        this.troops = new Troops(50, 30, this.owner);
    }
}
