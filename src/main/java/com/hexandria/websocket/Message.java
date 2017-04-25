package com.hexandria.websocket;

import org.jetbrains.annotations.NotNull;

/**
 * Created by root on 09.04.17.
 */

@SuppressWarnings("NullableProblems")
public class Message {
    public String event;
    public String payload;

    public Message(String event, String payload){
        this.event = event;
        this.payload = payload;
    }

    public Message(){
        this.event = "";
        this.payload = "";
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
