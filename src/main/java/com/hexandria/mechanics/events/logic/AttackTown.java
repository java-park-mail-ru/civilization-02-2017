package com.hexandria.mechanics.events.logic;

import com.hexandria.mechanics.avatar.GameAvatar;
import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.websocket.Message;

/**
 * Created by frozenfoot on 16.05.17.
 */
public class AttackTown extends Message {
    public final Payload payload;

    public AttackTown(Coordinates position, GameAvatar owner){
        this.payload = new Payload(position, owner);
    }

    public static class Payload{
        public final Coordinates position;
        public final GameAvatar newOwner;

        public Payload(Coordinates position, GameAvatar newOwner){
            this.newOwner = newOwner;
            this.position = position;
        }
    }
}
