package com.hexandria.mechanics.events;

import com.hexandria.mechanics.base.Coordinates;

/**
 * Created by root on 25.04.17.
 */
public abstract class Logic {
    public Coordinates coordinates;

    public Logic(Coordinates coordinates){
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
