package com.hexandria.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexandria.auth.common.user.UserEntity;
import com.hexandria.auth.common.user.UserManager;
import com.hexandria.mechanics.Game;
import com.hexandria.mechanics.avatar.UserAvatar;
import net.minidev.json.JSONObject;
import org.eclipse.jetty.util.ConcurrentArrayQueue;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 09.04.17.
 */

@Service
public class RemotePointService {
    @NotNull
    private final UserManager manager;
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(RemotePointService.class);

    private final Queue<Long> waiters = new ConcurrentArrayQueue<>();
    private final List<Game> games = new ArrayList<>();
    private final Map<Long, Game> gameMap = new ConcurrentHashMap<>();

    public RemotePointService(@NotNull UserManager manager, @NotNull ObjectMapper objectMapper){
        this.manager = manager;
        this.objectMapper = objectMapper;
    }

    public void handleGameMessage(Message message, Long userID) throws IOException {
        Game game = gameMap.get(userID);
        Message confirmMessage = game.changeGameMap(message);
        String jsonResponce = objectMapper.writeValueAsString(confirmMessage);
        WebSocketMessage webMessage = new TextMessage(jsonResponce);
        for(Map.Entry<Long, Game> entry : gameMap.entrySet()){
            if(entry.getValue() == game){
                sessions.get(entry.getKey()).sendMessage(webMessage);
            }
        }
    }

    public void registerUser(Long userId, @NotNull WebSocketSession webSocketSession) throws IOException {

        LOGGER.info("User with " + userId + " connected");
        sessions.put(userId, webSocketSession);
        waiters.add(userId);

        if(waiters.size() >= 2){

            JSONObject json = new JSONObject();
            json.put("message", "Game created, connecting to game");
            Long firstUserId = waiters.poll();
            Long secondUserId = waiters.poll();
            sessions.get(firstUserId)
                    .sendMessage(new TextMessage(json.toString()));
            sessions.get(secondUserId)
                    .sendMessage(new TextMessage(json.toString()));

            UserEntity firstUser = manager.getUserById(firstUserId.intValue());
            UserEntity secondUser = manager.getUserById(secondUserId.intValue());

            List<UserAvatar> avatars = new ArrayList<>();
            avatars.add(new UserAvatar((long) firstUser.getId(), firstUser.getLogin()));
            avatars.add(new UserAvatar((long) secondUser.getId(), secondUser.getLogin()));

            Game newGame = new Game(new ArrayList<>(avatars));
            games.add(newGame);
            gameMap.put((long) firstUser.getId(), newGame);
            gameMap.put((long) secondUser.getId(), newGame);

            waiters.remove(1);
            waiters.remove(0);
        }

        else{
            JSONObject json = new JSONObject();
            json.put("message", "waiting for new users");
            webSocketSession.sendMessage(new TextMessage(json.toString()));
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
