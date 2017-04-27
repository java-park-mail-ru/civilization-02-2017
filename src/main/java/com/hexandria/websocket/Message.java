package com.hexandria.websocket;

import com.hexandria.mechanics.events.Payload;

/**
 * Created by root on 09.04.17.
 */

public class Message {
    public Event event;
    public Payload payload;

    public Message(Event event, Payload payload){
        this.event = event;
        this.payload = payload;
    }

    public Message(){
        this.event = new Event();
        this.payload = new Payload();
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
