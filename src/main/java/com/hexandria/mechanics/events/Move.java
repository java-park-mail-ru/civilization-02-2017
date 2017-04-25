package com.hexandria.mechanics.events;

import com.hexandria.mechanics.base.Coordinates;

import java.security.PublicKey;

/**
 * Created by root on 25.04.17.
 */
public class Move extends Logic {
    private Coordinates newCoordinates;

    public Move(Coordinates coordinates, Coordinates newCoordinates){
        super(coordinates);
        this.newCoordinates = newCoordinates;
    }

    public Coordinates getNewCoordinates() {
        return newCoordinates;
    }

    public void setNewCoordinates(Coordinates newCoordinates) {
        this.newCoordinates = newCoordinates;
    }
}
