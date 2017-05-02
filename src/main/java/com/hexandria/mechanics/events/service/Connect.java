package com.hexandria.mechanics.events.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hexandria.websocket.Message;

/**
 * Created by frozenfoot on 02.05.17.
 */
public class Connect extends Message {
    private String username;

    @JsonCreator
    public Connect(@JsonProperty("payload") String payload){
        this.username = payload;
    }

    public String getPayload() {
        return username;
    }

    public void setPayload(String payload) {
        this.username = payload;
    }
}
