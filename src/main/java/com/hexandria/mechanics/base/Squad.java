package com.hexandria.mechanics.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexandria.mechanics.player.GamePlayer;

/**
 * Created by root on 23.04.17.
 */
public class Squad {
    private int count;
    private int morale;
    private final GamePlayer owner;
    private boolean isMoved;

    public Squad(int count, int morale, GamePlayer owner){
        this.count = count;
        this.morale = morale;
        this.owner = owner;
        this.isMoved = false;
    }

    public void mergeSquads(Squad squad){
        count = count + squad.count;
    }

    @JsonIgnore
    public GamePlayer getOwner(){
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

    @JsonIgnore
    public void setMoved(boolean isMoved){
        this.isMoved = isMoved;
    }

    @JsonIgnore
    public boolean getMoved(){
        return isMoved;
    }
}
