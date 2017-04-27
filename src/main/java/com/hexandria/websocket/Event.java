package com.hexandria.websocket;

/**
 * Created by root on 27.04.17.
 */
public class Event {
    String eventType;

    public Event (){
        eventType = "";
    }

    public Event(String eventType){
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
