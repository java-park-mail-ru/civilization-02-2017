package com.hexandria.mechanics.events.game;

import com.hexandria.mechanics.Game;
import com.hexandria.mechanics.base.Capital;
import com.hexandria.mechanics.base.Ceil;
import com.hexandria.mechanics.base.Town;
import com.hexandria.websocket.Message;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by frozenfoot on 16.05.17.
 */
public class Start extends Message {
    public final Payload payload;

    public Start(Game game){
        this.payload = new Payload(game);
    }

    public static class Payload{
        public final int sizeX;
        public final int sizeY;
        public final List<Town> towns;
        public final List<Capital> capitals;
//        public List<Squad> squads;

        public Payload(Game game){
            this.towns = new LinkedList<>();
            this.capitals = new LinkedList<>();
//            this.squads = new LinkedList<>();
            this.sizeX = game.getSizeX();
            this.sizeY = game.getSizeY();
            final Ceil[][] map = game.getMap();
            for(int i = 0; i < sizeX; ++ i){
                for(int j = 0; j < sizeY; ++j){

                    if(map[i][j].getClass() == Town.class){
                        towns.add((Town)map[i][j]);
                    }
                    else if(map[i][j].getClass() == Capital.class){
                        capitals.add((Capital)map[i][j]);
                    }
//                    if(map[i][j].getSquad() != null){
//                        squads.add(map[i][j].getSquad());
//                    }
                }
            }
        }
    }
}
