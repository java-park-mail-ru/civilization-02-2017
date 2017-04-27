package com.hexandria.mechanics.events.logic;

import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.mechanics.events.Payload;

/**
 * Created by root on 25.04.17.
 */
public class Update extends Payload {
    private Integer newMorale;
    private Coordinates squatCoordinates;
    private Integer newCount;

    public Update(Integer newMorale, Coordinates squatCoordinates, Integer newCount) {
        this.newMorale = newMorale;
        this.squatCoordinates = squatCoordinates;
        this.newCount = newCount;
    }

    public Integer getNewMorale() {
        return newMorale;
    }

    public void setNewMorale(Integer newMorale) {
        this.newMorale = newMorale;
    }

    public Coordinates getSquatCoordinates() {
        return squatCoordinates;
    }

    public void setSquatCoordinates(Coordinates squatCoordinates) {
        this.squatCoordinates = squatCoordinates;
    }

    public Integer getNewCount() {
        return newCount;
    }

    public void setNewCount(Integer newCount) {
        this.newCount = newCount;
    }
}
