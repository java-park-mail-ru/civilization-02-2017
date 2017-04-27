package com.hexandria.mechanics.events.logic;

import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.mechanics.events.Payload;

/**
 * Created by root on 25.04.17.
 */
public class Delete extends Payload {
    Coordinates coordinates;

    public Delete(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
