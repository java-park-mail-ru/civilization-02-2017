package com.hexandria.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hexandria.mechanics.events.game.GameResult;
import com.hexandria.mechanics.events.game.Start;
import com.hexandria.mechanics.events.logic.*;
import com.hexandria.mechanics.events.service.Connect;
import com.hexandria.mechanics.events.service.Ping;

/**''
 * Created by root on 09.04.17.
 */


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "event")
@JsonSubTypes({

        @JsonSubTypes.Type(value = Connect.class, name = "EVENTS.SERVICE.CONNECT"),
        @JsonSubTypes.Type(value = Ping.class, name = "EVENTS.SERVICE.PING"),

        @JsonSubTypes.Type(value = Start.class, name = "EVENTS.GAME.START"),
        @JsonSubTypes.Type(value = GameResult.class, name = "EVENTS.GAME.RESULT"),

        @JsonSubTypes.Type(value = Move.class, name = "EVENTS.LOGIC.MOVE"),
        @JsonSubTypes.Type(value = Update.class, name = "EVENTS.LOGIC.UPDATE"),
        @JsonSubTypes.Type(value = Delete.class, name = "EVENTS.LOGIC.DELETE"),
        @JsonSubTypes.Type(value = Create.class, name = "EVENTS.LOGIC.CREATE"),
        @JsonSubTypes.Type(value = AttackTown.class, name = "EVENTS.LOGIC.ATTACK_TOWN"),
        @JsonSubTypes.Type(value = Turn.class, name = "EVENTS.LOGIC.TURN"),

})
public abstract class Message {
}
