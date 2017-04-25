package com.hexandria.websocket;

import org.jetbrains.annotations.NotNull;

/**
 * Created by root on 09.04.17.
 */

public interface MessageHandlerContainer {

    void handle(@NotNull KMessage message, long forUser) throws HandleException;

    <T> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler);
}
