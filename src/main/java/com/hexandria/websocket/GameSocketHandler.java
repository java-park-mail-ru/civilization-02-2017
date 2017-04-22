package com.hexandria.websocket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexandria.auth.common.user.UserEntity;
import com.hexandria.auth.common.user.UserManager;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.slf4j.Logger;

import javax.naming.AuthenticationException;
import java.io.IOException;

/**
 * Created by root on 20.04.17.
 */
public class GameSocketHandler extends TextWebSocketHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class);

    @NotNull
    private final UserManager userManager;
    @NotNull
    private final MessageHandlerContainer messageHandlerContainer;
    @NotNull
    private final RemotePointService remotePointService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameSocketHandler(@NotNull UserManager userManager,
                             @NotNull MessageHandlerContainer messageHandlerContainer,
                             @NotNull RemotePointService remotePointService) {
        this.userManager = userManager;
        this.messageHandlerContainer = messageHandlerContainer;
        this.remotePointService = remotePointService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        final Long userId = (Long) webSocketSession.getAttributes().get("userId");
        if(userId == null || userManager.getUserById(userId.intValue()) == null){
            throw new AuthenticationException("Only authenticated users can play this game");
        }
        remotePointService.registerUser(userId, webSocketSession);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws AuthenticationException {
        final Long userId = (Long) session.getAttributes().get("userId");
        final UserEntity user = userManager.getUserById(userId.intValue());
        if(userId == null || user == null){
            throw new AuthenticationException("Only authenticated users can play this game");
        }
        handleMessage(user, message);
    }

    private void handleMessage(UserEntity user, TextMessage text){
        final Message message;
        try{
            message = objectMapper.readValue(text.getPayload(), Message.class);
        }
        catch (IOException e){
            LOGGER.error("Wrong json format", e);
            return;
        }
        try{
            messageHandlerContainer.handle(message, new Integer(user.getId()).longValue());
        }
        catch (HandleException e){
            LOGGER.error("Unhandlable message");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        final Long userId = (Long) session.getAttributes().get("userId");
        if(userId == null){
            LOGGER.warn("Session not found" + status);
            return;
        }
        remotePointService.removeUser(userId);
    }

    @Override
    public boolean supportsPartialMessages(){
        return false;
    }
}
