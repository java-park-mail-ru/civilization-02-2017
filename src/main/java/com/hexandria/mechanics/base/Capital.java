package com.hexandria.mechanics.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexandria.mechanics.player.GamePlayer;

/**
 * Created by root on 23.04.17.
 */
public class Capital extends Town {

    protected static final int TROOPS_GENERATED = 50;
    protected static final int MORALE_GENERATED = 30;

    public Capital(Coordinates coordinates, String name, GamePlayer owner){
        super(coordinates, name);
        this.owner = owner;
        this.squad = new Squad(TROOPS_GENERATED, MORALE_GENERATED, this.owner);
    }

    @Override
    @JsonIgnore
    public int getTroopsGenerated() {
        return TROOPS_GENERATED;
    }

    @Override
    @JsonIgnore
    public int getMoraleGenerated() {
        return MORALE_GENERATED;
    }
}
