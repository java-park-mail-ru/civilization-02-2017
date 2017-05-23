package com.hexandria.mechanics.events.game;

import com.hexandria.mechanics.avatar.GameAvatar;
import com.hexandria.websocket.Message;

/**
 * Created by frozenfoot on 18.05.17.
 */
public class GameResult extends Message {
    public final Payload payload;

    public GameResult(GameAvatar winner, GameAvatar loser, String reason){
        this.payload = new Payload(winner, loser, reason);
    }

    public static class Payload{
        public GameAvatar winner;
        public GameAvatar loser;
        public String reason;

        public Payload(GameAvatar winner, GameAvatar loser, String reason){
            this.loser = loser;
            this.winner = winner;
            this.reason = reason;
        }
    }
}
