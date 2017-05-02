package com.hexandria.mechanics.base;

import com.hexandria.mechanics.avatar.UserAvatar;

/**
 * Created by root on 23.04.17.
 */
public class Squad {
    private int count;
    private int morale;
    private UserAvatar owner;

    public Squad(int amount, int morale, UserAvatar owner){
        this.count = amount;
        this.morale = morale;
        this.owner = owner;
    }

    public UserAvatar getOwner(){
        return owner;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int amount) {
        this.count = amount;
    }

    public int getMorale() {
        return morale;
    }

    public void setMorale(int morale) {
        this.morale = morale;
    }
}
