package com.hexandria.mechanics.events.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.hexandria.websocket.Message;

/**
 * Created by frozenfoot on 25.05.17.
 */
public class Turn extends Message {

    public Payload payload;
    public static class Payload{
        @JsonCreator
        public Payload(){
        }
    }

    @JsonCreator
    public Turn(Payload payload){
        this.payload = payload;
    }
}
