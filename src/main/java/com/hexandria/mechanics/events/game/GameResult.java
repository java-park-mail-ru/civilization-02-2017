package com.hexandria.mechanics.events.game;

import com.hexandria.mechanics.player.GamePlayer;
import com.hexandria.websocket.Message;

/**
 * Created by frozenfoot on 18.05.17.
 */
public class GameResult extends Message {
    public final Payload payload;

    public GameResult(GamePlayer winner, GamePlayer loser, String reason){
        this.payload = new Payload(winner, loser, reason);
    }

    public static class Payload{
        public GamePlayer winner;
        public GamePlayer loser;
        public String reason;

        public Payload(GamePlayer winner, GamePlayer loser, String reason){
            this.loser = loser;
            this.winner = winner;
            this.reason = reason;
        }
    }
}
