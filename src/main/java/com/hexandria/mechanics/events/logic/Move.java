package com.hexandria.mechanics.events.logic;

import com.fasterxml.jackson.annotation.*;
import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.websocket.Message;

/**
 * Created by root on 25.04.17.
 */
public class Move extends Message {
    public final Payload payload;
    @JsonCreator
    public Move(@JsonProperty("payload") Payload payload) {
        this.payload = payload;
    }

    @JsonIgnore
    public Coordinates getTo() {
        return payload.to;
    }

    @JsonIgnore
    public Coordinates getFrom() {
        return payload.from;
    }

    public static class Payload {
        public final Coordinates from;
        public final Coordinates to;

        @JsonCreator
        public Payload(@JsonProperty("from") Coordinates from,
                @JsonProperty("to") Coordinates to) {
            this.from = from;
            this.to = to;
        }
    }
}
