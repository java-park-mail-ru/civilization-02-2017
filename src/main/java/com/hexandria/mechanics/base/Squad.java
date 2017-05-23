package com.hexandria.mechanics.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexandria.mechanics.avatar.GameAvatar;

/**
 * Created by root on 23.04.17.
 */
public class Squad {
    private int count;
    private int morale;
    private final GameAvatar owner;

    public Squad(int count, int morale, GameAvatar owner){
        this.count = count;
        this.morale = morale;
        this.owner = owner;
    }

    public void mergeSquads(Squad squad){
        count = count + squad.count;
    }

    @JsonIgnore
    public GameAvatar getOwner(){
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
