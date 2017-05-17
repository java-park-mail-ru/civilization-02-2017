package com.hexandria.mechanics.events.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hexandria.websocket.Message;

/**
 * Created by frozenfoot on 02.05.17.
 */
public class Ping extends Message {

    private String username;
    private String event;

    @JsonCreator
    public Ping(@JsonProperty("payload") String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
