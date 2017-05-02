package com.hexandria.mechanics.events.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.mechanics.events.Payload;
import com.hexandria.websocket.Message;

/**
 * Created by root on 25.04.17.
 */
public class Move extends Message {

    private int playerIndex;
    private int squadIndex;
    private Coordinates moveTo;

    @JsonCreator
    public Move(@JsonProperty("playerIndex") int playerIndex, @JsonProperty("squadIdnex") int squadIndex){
        System.out.println("Move creator with " + playerIndex + " squadIndex : " + squadIndex);
        this.playerIndex = playerIndex;
        this.squadIndex = squadIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getSquadIndex() {
        return squadIndex;
    }

    public void setSquadIndex(int squadIndex) {
        this.squadIndex = squadIndex;
    }

    public Coordinates getMoveTo() {
        return moveTo;
    }

    public void setMoveTo(Coordinates moveTo) {
        this.moveTo = moveTo;
    }

    //    private Coordinates newCoordinates;
//    private Coordinates oldCoordinates;
//
//    @JsonCreator
//    public Move(Coordinates newCoordinates, Coordinates oldCoordinates){
//        this.oldCoordinates = oldCoordinates;
//        this.newCoordinates = newCoordinates;
//    }
//
//    public Coordinates getNewCoordinates() {
//        return newCoordinates;
//    }
//
//    public Coordinates getOldCordinates() {return oldCoordinates;}
//
//    public void setOldCordinates(Coordinates oldCordinates) {this.oldCoordinates = oldCordinates;}
//
//    public void setNewCoordinates(Coordinates newCoordinates) {
//        this.newCoordinates = newCoordinates;
//    }
}
