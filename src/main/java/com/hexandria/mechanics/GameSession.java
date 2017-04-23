package com.hexandria.mechanics;

import com.hexandria.auth.common.user.UserEntity;
import com.hexandria.mechanics.base.GameMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by root on 23.04.17.
 */
public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    @NotNull
    private final long sessionId;
    @NotNull
    private final ArrayList<UserEntity> players;
    @NotNull
    private GameMap map;

    @NotNull
    public ArrayList<UserEntity> getPlayers() {
        return players;
    }

    @NotNull
    public GameMap getMap() {
        return map;
    }

    public void setMap(@NotNull GameMap map) {
        this.map = map;
    }

    public GameSession(@NotNull ArrayList<UserEntity> players){
        this.players = players;
        this.sessionId = ID_GENERATOR.getAndIncrement();
        this.map = new GameMap("Map" + this.sessionId, 10, 10);
    }
}
