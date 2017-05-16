package com.hexandria.mechanics.base;

import com.hexandria.mechanics.avatar.UserAvatar;

/**
 * Created by root on 23.04.17.
 */
public class Capital extends Town {

    private static final int DEFAULT_COUNT = 50;
    private static final int DEFAULT_MORALE = 30;

    public Capital(Coordinates coordinates, String name, UserAvatar owner){
        super(coordinates, name);
        this.owner = owner;
        this.squad = new Squad(DEFAULT_COUNT, DEFAULT_MORALE, this.owner);
    }
}
