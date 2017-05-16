package com.hexandria.mechanics.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hexandria.mechanics.avatar.UserAvatar;

/**
 * Created by root on 23.04.17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Town extends Ceil {

    private static final int troopsGenerated = 15;
    private static final int moraleGenerated = 10;
    private String name;

    public UserAvatar owner;

    public Town(Coordinates position, String name){
        super(position);
        this.owner = null;
        this.name = name;
    }

    @JsonIgnore
    public int getTroopsGenerated() {
        return troopsGenerated;
    }

    @JsonIgnore
    public int getMoraleGenerated() {
        return moraleGenerated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserAvatar getOwner() {
        return owner;
    }

    public void setOwner(UserAvatar owner) {
        this.owner = owner;
    }

    public void generateSquads(){
        if(this.owner == null){return;}

        if (squad == null){
            squad = new Squad(troopsGenerated, moraleGenerated, owner);
        }
        else{
            squad.setCount(squad.getCount() + troopsGenerated);
            squad.setMorale(squad.getMorale() + moraleGenerated);
        }
    }

}
