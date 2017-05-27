package com.hexandria.mechanics.events.logic;

import com.hexandria.mechanics.player.GamePlayer;
import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.websocket.Message;

/**
 * Created by frozenfoot on 16.05.17.
 */
public class AttackTown extends Message {
    public final Payload payload;



    public AttackTown(Coordinates position, GamePlayer owner){
        this.payload = new Payload(position, owner);
    }

    public static class Payload{
        public final Coordinates position;
        public final String newOwner;

        public Payload(Coordinates position, GamePlayer newOwner){
            this.newOwner = newOwner.getName();
            this.position = position;
        }
    }
}
