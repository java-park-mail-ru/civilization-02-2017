package com.hexandria.mechanics.events.logic;

import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.websocket.Message;

/**
 * Created by root on 25.04.17.
 */
public class Delete extends Message {
    public final Payload payload;

    public Delete(Coordinates position){
        this.payload = new Payload(position);
    }

    public static class Payload{
        public final Coordinates position;
        public Payload(Coordinates coordinates) {
            this.position = coordinates;
        }
    }
}
