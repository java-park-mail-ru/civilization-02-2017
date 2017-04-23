package com.hexandria.websocket;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 20.04.17.
 */
@Service
public class GameMessageHandlerContainer implements MessageHandlerContainer {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMessageHandlerContainer.class);
    final Map<Class<?>, MessageHandler<?>> handlerMap = new HashMap<>();

    @Override
    public void handle(@NotNull Message message, @NotNull Long forUser) throws HandleException {
        final Class clazz;
        try{
            clazz = Class.forName(message.getType());
        } catch (ClassNotFoundException e) {
            throw new HandleException("Cant handle message of " + message.getType() + "type", e);
        }
        final MessageHandler<?> handler = handlerMap.get(clazz);

        if(handler == null){
            throw new HandleException("No handler for " + message.getType());
        }
        handler.handleMessage(message, forUser);
        LOGGER.debug("message handled: type =[" + message.getType() + "], content=[" + message.getContent() + ']');
    }

    @Override
    public <T> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler){
        handlerMap.put(clazz, handler);
    }
}
