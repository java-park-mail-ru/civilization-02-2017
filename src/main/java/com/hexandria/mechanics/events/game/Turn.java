package com.hexandria.mechanics.events.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hexandria.websocket.Message;

/**
 * Created by frozenfoot on 25.05.17.
 */
public class Turn extends Message {
    public final Payload payload;
    @JsonCreator
    public Turn(@JsonProperty("payload") Payload payload) {this.payload = payload;}

    public static class Payload{
        public final String name;

        @JsonCreator
        public Payload(){
            this.name = "test";
        }
    }
}
