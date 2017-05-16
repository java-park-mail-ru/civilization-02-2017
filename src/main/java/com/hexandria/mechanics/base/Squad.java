package com.hexandria.mechanics.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexandria.mechanics.avatar.UserAvatar;

/**
 * Created by root on 23.04.17.
 */
public class Squad {
    private int count;
    private int morale;
    private final UserAvatar owner;

    public Squad(int count, int morale, UserAvatar owner){
        this.count = count;
        this.morale = morale;
        this.owner = owner;
    }

    public void mergeSquads(Squad squad){
        count = count + squad.count;
    }

    @JsonIgnore
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
