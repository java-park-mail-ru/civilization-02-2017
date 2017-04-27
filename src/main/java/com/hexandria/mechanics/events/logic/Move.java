package com.hexandria.mechanics.events.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.mechanics.events.Payload;

/**
 * Created by root on 25.04.17.
 */
public class Move extends Payload {
    private Coordinates newCoordinates;
    private Coordinates oldCoordinates;

    @JsonCreator
    public Move(Coordinates newCoordinates, Coordinates oldCoordinates){
        this.oldCoordinates = oldCoordinates;
        this.newCoordinates = newCoordinates;
    }

    public Coordinates getNewCoordinates() {
        return newCoordinates;
    }

    public Coordinates getOldCordinates() {return oldCoordinates;}

    public void setOldCordinates(Coordinates oldCordinates) {this.oldCoordinates = oldCordinates;}

    public void setNewCoordinates(Coordinates newCoordinates) {
        this.newCoordinates = newCoordinates;
    }
}
