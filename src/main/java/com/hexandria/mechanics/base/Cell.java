package com.hexandria.mechanics.base;

import org.jetbrains.annotations.Nullable;

/**
 * Created by root on 23.04.17.
 */
public class Cell {
    private final Coordinates position;
    protected Squad squad;

    public Cell(Coordinates position){
        this.position = position;
        this.squad = null;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(@Nullable Squad squad) {
        this.squad = squad;
    }

    public Coordinates getPosition(){
        return position;
    }
}
