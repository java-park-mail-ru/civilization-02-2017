package com.hexandria.mechanics.base;

/**
 * Created by root on 23.04.17.
 */
class GameCeil implements IGameCeil {
    private int coordX;
    private int coordY;

    public GameCeil(int coordX, int coordY){
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public int getCoordX(){
        return coordX;
    }

    public int getCoordY(){
        return coordY;
    }
}
