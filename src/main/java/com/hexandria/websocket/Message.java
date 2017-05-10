package com.hexandria.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hexandria.mechanics.events.service.Connect;
import com.hexandria.mechanics.events.logic.Move;
import com.hexandria.mechanics.events.service.Ping;

/**''
 * Created by root on 09.04.17.
 */


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "event")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Move.class, name = "EVENTS.LOGIC.MOVE"),
        @JsonSubTypes.Type(value = Connect.class, name = "EVENTS.SERVICE.CONNECT"),
        @JsonSubTypes.Type(value = Ping.class, name = "EVENTS.SERVICE.PING")
})
public abstract class Message {
    public Message(){}
}
