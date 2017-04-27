package com.hexandria.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexandria.auth.common.user.UserManager;
import jdk.nashorn.internal.parser.JSONParser;
import net.minidev.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * Created by root on 25.04.17.
 */
public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebSocketHandler.class);
    @NotNull
    private final UserManager userManager;
    @NotNull
    private final RemotePointService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public MyWebSocketHandler(@NotNull UserManager userManager, @NotNull RemotePointService service){
        this.service = service;
        this.userManager = userManager;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.warn("Disconnected user with id  " + session.getAttributes().get("userId"));
        service.removeUser(new Long(session.getAttributes().get("userId").toString()));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        service.registerUser(new Long(session.getAttributes().get("userId").toString()), session);
        LOGGER.warn("Connected user with id  " + session.getAttributes().get("userId"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
        Long userId = new Long((Integer) session.getAttributes().get("userId"));
        final Message message;
        try {
            message = objectMapper.readValue(jsonTextMessage.getPayload(), Message.class);
        } catch (IOException ex) {
            LOGGER.error("wrong json format at ping response", ex);
            return;
        }
        service.handleGameMessage(message, userId);
    }
}
