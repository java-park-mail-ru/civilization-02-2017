package com.hexandria.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexandria.auth.common.user.UserEntity;
import com.hexandria.auth.common.user.UserManager;
import com.hexandria.mechanics.Game;
import com.hexandria.mechanics.player.GamePlayer;
import com.hexandria.mechanics.events.game.GameResult;
import com.hexandria.mechanics.events.game.Start;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.util.Collections.singletonMap;

/**
 * Created by root on 09.04.17.
 */

@SuppressWarnings("OverlyBroadThrowsClause")
@Service
public class RemotePointService {
    @NotNull
    private final UserManager manager;
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(RemotePointService.class);

    private final Queue<Long> waiters = new ConcurrentLinkedDeque<>();
    private final List<Game> games = new ArrayList<>();
    private final Map<Long, Game> gameMap = new ConcurrentHashMap<>();
    public final static int TURN_DURATION_MILLIS = 60 * 1000;

    public RemotePointService(@NotNull UserManager manager, @NotNull ObjectMapper objectMapper) {
        this.manager = manager;
        this.objectMapper = objectMapper;
    }
    private class GameDispatcher implements Runnable {
        @Override
        public void run() {
            for (Game game: games){
                if (game.getLatestTurnStart().getTime() + TURN_DURATION_MILLIS > System.currentTimeMillis()){
                    game.finishTurn();
                }
            }
        }
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    public void handleGameMessage(Message message, Long userID) throws IOException {
        final Game game = gameMap.get(userID);
        final List<Message> responces = game.interact(message);
        for(Message responce : responces) {
            for (Map.Entry<Long, Game> entry : gameMap.entrySet()) {
                if(responce.getClass() == GameResult.class){
                    finishGame(game, (GameResult) responce);
                    return;
                }
                if (Objects.equals(entry.getValue(), game) && isConnected(entry.getKey())) {
                    sendMessageToUser(entry.getKey(), responce);
                }
            }
        }
    }

    public void finishGame(Game game, GameResult gameResult) throws IOException {
        final Long winnerId = gameResult.payload.winner.getId();
        final Long loserId = gameResult.payload.loser.getId();
        sendMessageToUser(winnerId, gameResult);
        sendMessageToUser(loserId, gameResult);
        sessions.get(winnerId).close();
        sessions.get(loserId).close();
        sessions.remove(winnerId);
        sessions.remove(loserId);
        games.remove(game);
        gameMap.remove(loserId);
        gameMap.remove(winnerId);
    }

    public void disconnectedHandler(Long userId, @NotNull CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(userId);

        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                Game userGame = gameMap.get(userId);
                for(Map.Entry<Long, Game> entry : gameMap.entrySet()){
                    if(entry.getValue() == userGame){
                        GamePlayer winner;
                        GamePlayer loser;

                        if(userGame.getPlayers().get(0).getId() == userId){
                            winner = userGame.getPlayers().get(1);
                            loser = userGame.getPlayers().get(0);
                        }
                        else{
                            winner = userGame.getPlayers().get(0);
                            loser = userGame.getPlayers().get(1);
                        }

                        sendMessageToUser(entry.getKey(), new GameResult(
                                winner, loser, "Your opponent disconnected"
                        ));
                        sessions.get(entry.getKey()).close();
                        sessions.remove(userId);
                        gameMap.remove(winner.getId());
                        gameMap.remove(loser.getId());
                        break;
                    }
                }
                games.remove(userGame);
            } catch (IOException ignore) {
            }
        }
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    public void registerUser(Long userId, @NotNull WebSocketSession webSocketSession) throws IOException {

        LOGGER.info("User with " + userId + " connected");
        sessions.put(userId, webSocketSession);
        waiters.add(userId);

        if (waiters.size() >= 2) {
            final Long firstUserId = waiters.poll();
            final Long secondUserId = waiters.poll();
            final TextMessage message = new TextMessage(
                    objectMapper.writeValueAsString(
                            singletonMap("message", "Game created, connecting to game")
                    )
            );
            sessions.get(firstUserId).sendMessage(message);
            sessions.get(secondUserId).sendMessage(message);

            final UserEntity firstUser = manager.getUserById(firstUserId.intValue());
            final UserEntity secondUser = manager.getUserById(secondUserId.intValue());

            final List<GamePlayer> avatars = new ArrayList<>();
            avatars.add(new GamePlayer((long) firstUser.getId(), firstUser.getLogin()));
            avatars.add(new GamePlayer((long) secondUser.getId(), secondUser.getLogin()));

            final Game newGame = new Game(new ArrayList<>(avatars));
            sendMessageToUser(firstUserId, new Start(newGame));
            sendMessageToUser(secondUserId, new Start(newGame));

            games.add(newGame);
            gameMap.put((long) firstUser.getId(), newGame);
            gameMap.put((long) secondUser.getId(), newGame);

        }
        else {
            webSocketSession.sendMessage(
                    new TextMessage(objectMapper.writeValueAsString(
                            singletonMap("message", "waiting for new users")
                    ))
            );
        }
    }

    public boolean isConnected(Long userId) {
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
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
