package com.hexandria.mechanics.base;

import com.hexandria.mechanics.avatar.UserAvatar;

/**
 * Created by root on 23.04.17.
 */
class City extends GameCeil {
    private static final int troopsGenerated = 15;
    private static final int moraleGenerated = 10;
    private int coordX;
    private int coordY;
    protected UserAvatar owner;

    public void generateTroops(){
        if (troops == null){
            troops = new Troops(troopsGenerated, moraleGenerated, owner);
        }
        else{
            troops.setAmount(troops.getAmount() + troopsGenerated);
            troops.setMorale(troops.getMorale() + moraleGenerated);
        }
    }

    public City(){
        super(0, 0);
        owner = null;
        this.troops = null;
    }

    public City(int coordX, int coordY){
        super(coordX, coordY);
        owner = null;
        troops = null;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public int getCoordX(){
        return coordX;
    }

    public int getCoordY(){
        return coordY;
    }

    public UserAvatar getOwner(){
        return this.owner;
    }

    public void setOwner(UserAvatar owner){
        this.owner = owner;
    }
}
