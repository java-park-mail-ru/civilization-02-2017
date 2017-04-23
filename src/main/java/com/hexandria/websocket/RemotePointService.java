package com.hexandria.websocket;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by root on 20.04.17.
 */
public class RemotePointService {

    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void registerUser(@NotNull Long userId, @NotNull WebSocketSession session){
        sessions.put(userId, session);
    }

    public boolean isConnected(Long userId){
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
    }

    public void removeUser(Long userId){
        sessions.remove(userId);
    }

    public void cutDownConnection(Long userId, @NotNull CloseStatus closeStatus){

        final WebSocketSession webSocketSession = sessions.get(userId);
        if(webSocketSession != null && webSocketSession.isOpen()){
            try{
                webSocketSession.close();
            }
            catch (IOException e) {

            }
        }
    }

    public void sendMessage(Long userId, @NotNull Message message) throws IOException {

        final WebSocketSession webSocketSession = sessions.get(userId);

        if(webSocketSession == null){
            throw new IOException("No game socket for " + userId);
        }

        if(!webSocketSession.isOpen()){
            throw new IOException("Session closed for user" + userId);
        }

        try{
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        }
        catch (JsonProcessingException | WebSocketException e){
            throw new IOException("Unable to send message", e);
        }
    }

}
