package com.hexandria.websocket;


import org.jetbrains.annotations.NotNull;

/**
 * Created by root on 20.04.17.
 */
public interface MessageHandlerContainer {

    void handle(@NotNull Message message, @NotNull Long forUser) throws HandleException;

    <T> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler);
}
