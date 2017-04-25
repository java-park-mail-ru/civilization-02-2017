package com.hexandria.mechanics.base;

/**
 * Created by root on 23.04.17.
 */
public class Ceil {
    private Coordinates coordinates;
    protected Squad squad;

    public Ceil(Coordinates coordinates){
        this.coordinates = coordinates;
        this.squad = null;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    public void setCoordinates(Coordinates coordinates){
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }
}
