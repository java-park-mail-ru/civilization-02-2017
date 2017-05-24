package com.hexandria.mechanics.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hexandria.mechanics.player.GamePlayer;

/**
 * Created by root on 23.04.17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Town extends Cell {

    @JsonIgnore
    public int TROOPS_GENERATED = 15;
    @JsonIgnore
    public int MORALE_GENERATED = 10;
    private String name;

    public GamePlayer owner;

    public Town(Coordinates position, String name){
        super(position);
        this.owner = null;
        this.name = name;
    }

    @JsonIgnore
    public int getTroopsGenerated() {
        return TROOPS_GENERATED;
    }

    @JsonIgnore
    public int getMoraleGenerated() {
        return MORALE_GENERATED;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GamePlayer getOwner() {
        return owner;
    }

    public void setOwner(GamePlayer owner) {
        this.owner = owner;
    }

    public void generateSquads(){//Will return message, create or update
        if(this.owner == null){return;}

        if (squad == null){
            squad = new Squad(getTroopsGenerated(), getMoraleGenerated(), owner);
        }
        else{
            squad.setCount(squad.getCount() + getTroopsGenerated());
            squad.setMorale(squad.getMorale() + getMoraleGenerated());
        }
    }

}
