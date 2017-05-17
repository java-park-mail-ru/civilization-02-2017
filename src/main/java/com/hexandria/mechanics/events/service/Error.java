package com.hexandria.mechanics.events.service;


import com.hexandria.websocket.Message;

/**
 * Created by frozenfoot on 02.05.17.
 */
public class Error extends Message {
    public Error(String error){
        this.error = error;
    }

    private final String error;
}
