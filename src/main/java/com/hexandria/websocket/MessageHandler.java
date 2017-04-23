package com.hexandria.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by root on 20.04.17.
 */
public abstract class MessageHandler<T> {
    @NotNull
    private final Class<T> clazz;

    public MessageHandler(@NotNull Class<T> clazz){
        this.clazz = clazz;
    }

    public void handleMessage(@NotNull Message message, @NotNull Long forUser) throws HandleException {
        try{
            final Object data = new ObjectMapper().readValue(message.getContent(), clazz);
            handle(clazz.cast(data), forUser);
        }

        catch (IOException | ClassCastException e){
            throw new HandleException("Can't read incoming message of type " + message.getType() + " with content: " + message.getContent(), e);
        }
    }

    public abstract void handle(@NotNull T message, @NotNull Long forUser) throws HandleException;
}
