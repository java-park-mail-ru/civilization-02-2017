package com.hexandria.mechanics.base;

import com.hexandria.mechanics.avatar.UserAvatar;

/**
 * Created by root on 23.04.17.
 */
public class Capital extends Town {

    public Capital(Coordinates coordinates, String name, UserAvatar owner){
        super(coordinates, name);
        this.owner = owner;
        this.squad = new Squad(50, 30, this.owner);
    }
}
