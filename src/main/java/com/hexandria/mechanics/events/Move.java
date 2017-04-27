package com.hexandria.mechanics.events;

import com.hexandria.mechanics.base.Coordinates;

/**
 * Created by root on 25.04.17.
 */
public class Move extends Payload {
    private Coordinates newCoordinates;
    private Coordinates oldCoordinates;

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
