package com.hexandria.mechanics.events;

import com.hexandria.mechanics.base.Coordinates;

/**
 * Created by root on 25.04.17.
 */
public class Update extends Logic {
    private Integer newMorale;

    private Integer newCount;

    public Update(Coordinates coordinates, Integer newMorale, Integer newCount){
        super(coordinates);
        this.newMorale = newMorale;
        this.newCount = newCount;
    }

    public Integer getNewMorale() {
        return newMorale;
    }

    public void setNewMorale(Integer newMorale) {
        this.newMorale = newMorale;
    }

    public Integer getNewCount() {
        return newCount;
    }

    public void setNewCount(Integer newCount) {
        this.newCount = newCount;
    }
}
