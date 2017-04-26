package com.hexandria.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexandria.auth.common.user.UserEntity;
import com.hexandria.auth.common.user.UserManager;
import com.hexandria.mechanics.Game;
import com.hexandria.mechanics.avatar.UserAvatar;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.xml.soap.Text;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 09.04.17.
 */

@Service
public class RemotePointService {
    @NotNull
    private final UserManager manager;
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger LOGGER = LoggerFactory.getLogger(RemotePointService.class);

    private final List<Long> waiters = new ArrayList<>();
    private final List<Game> games = new ArrayList<>();
    private final Map<Long, Game> gameMap = new ConcurrentHashMap<>();

    public RemotePointService(@NotNull UserManager manager){
        this.manager = manager;
    }

    public void handleGameMessage(Message message){
        System.out.println("Event: " + message.getEvent() + "\nPayload: " + message.getPayload());
    }

    public void registerUser(Long userId, @NotNull WebSocketSession webSocketSession) throws IOException {
        LOGGER.warn("User with " + userId + " connected");
        sessions.put(userId, webSocketSession);
        waiters.add(userId);
        if(waiters.size() >= 2){
            
            sessions.get(waiters.get(0))
                    .sendMessage(new TextMessage("Game created, connecting to game server"));
            sessions.get(waiters.get(1))
                    .sendMessage(new TextMessage("Game created, connecting to game server"));

            UserEntity firstUser = manager.getUserById(waiters.get(0).intValue());
            UserEntity secondUser = manager.getUserById(waiters.get(1).intValue());

            List<UserAvatar> avatars = new ArrayList<>();
            avatars.add(new UserAvatar(new Long(firstUser.getId()), firstUser.getLogin()));
            avatars.add(new UserAvatar(new Long(secondUser.getId()), secondUser.getLogin()));

            Game newGame = new Game(new ArrayList<>(avatars));
            games.add(newGame);
            gameMap.put(new Long(firstUser.getId()), newGame);
            gameMap.put(new Long(secondUser.getId()), newGame);

            waiters.remove(0);
            waiters.remove(1);
        }
        else{
            webSocketSession.sendMessage(new TextMessage("Wait for new users connected"));
        }
    }

    public boolean isConnected(Long userId) {
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
    }

    public void removeUser(Long userId)
    {
        sessions.remove(userId);
    }

    public void cutDownConnection(Long userId, @NotNull CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException ignore) {
            }
        }
    }

    public void sendMessageToUser(Long userId, @NotNull Message message) throws IOException {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession == null) {
            throw new IOException("No game websocket for user " + userId);
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("Session is closed or not exsists");
        }
        try {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (JsonProcessingException | WebSocketException e) {
            throw new IOException("Unable to send message", e);
        }
    }
}
