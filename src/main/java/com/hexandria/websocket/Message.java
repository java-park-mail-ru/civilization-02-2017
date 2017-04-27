package com.hexandria.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hexandria.mechanics.events.logic.Move;
import com.hexandria.mechanics.events.Payload;

/**
 * Created by root on 09.04.17.
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "event")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Move.class, name = "EVENTS.LOGIC.MOVE")
})
public abstract class Message {
    public String event;
}
