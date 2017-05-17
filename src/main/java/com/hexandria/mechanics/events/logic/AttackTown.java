package com.hexandria.mechanics.events.logic;

import com.hexandria.mechanics.avatar.UserAvatar;
import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.websocket.Message;

/**
 * Created by frozenfoot on 16.05.17.
 */
public class AttackTown extends Message {
    public final Payload payload;

    public AttackTown(Coordinates position, UserAvatar owner){
        this.payload = new Payload(position, owner);
    }

    public static class Payload{
        public final Coordinates position;
        public final UserAvatar newOwner;

        public Payload(Coordinates position, UserAvatar newOwner){
            this.newOwner = newOwner;
            this.position = position;
        }
    }
}
