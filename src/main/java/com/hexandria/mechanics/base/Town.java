package com.hexandria.mechanics.base;

import com.hexandria.mechanics.avatar.UserAvatar;

/**
 * Created by root on 23.04.17.
 */
public class Town extends Ceil {

    private static final int troopsGenerated = 15;
    private static final int moraleGenerated = 10;
    private String name;
    protected UserAvatar owner;

    public Town(Coordinates coordinates, String name){
        super(coordinates);
        this.owner = null;
        this.name = name;
    }

    public static int getTroopsGenerated() {
        return troopsGenerated;
    }

    public static int getMoraleGenerated() {
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
