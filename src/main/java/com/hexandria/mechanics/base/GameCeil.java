package com.hexandria.mechanics.base;

/**
 * Created by root on 23.04.17.
 */
class GameCeil implements IGameCeil {
    private int coordX;
    private int coordY;
    protected Troops troops;

    public GameCeil(int coordX, int coordY){
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public Troops getTroops() {
        return troops;
    }

    public void setTroops(Troops troops) {
        this.troops = troops;
    }

    public int getCoordX(){
        return coordX;
    }

    public int getCoordY(){
        return coordY;
    }
}
