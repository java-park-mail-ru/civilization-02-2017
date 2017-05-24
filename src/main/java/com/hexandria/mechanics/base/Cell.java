package com.hexandria.mechanics.base;

/**
 * Created by root on 23.04.17.
 */
public class Cell {
    private Coordinates position;
    public Squad squad;

    public Cell(Coordinates position){
        this.position = position;
        this.squad = null;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    public void setPosition(Coordinates coordinates){
        this.position = coordinates;
    }

    public Coordinates getPosition(){
        return position;
    }
}
