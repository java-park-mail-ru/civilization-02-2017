package com.hexandria.mechanics.events.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.websocket.Message;

/**
 * Created by root on 25.04.17.
 */
public class Move extends Message {

	private final Payload payload;

	@JsonCreator
	public Move(@JsonProperty("payload") Payload payload) {
		this.payload = payload;
	}

	public int getPlayerIndex() {
		return payload.playerIndex;
	}

	public int getSquadIndex() {
		return payload.squadIndex;
	}

	public Coordinates getMoveTo() {
		return payload.moveTo;
	}
	
	public static class Payload {
		private final int playerIndex;
		private final int squadIndex;
		private final Coordinates moveTo;

		@JsonCreator
		public Payload(@JsonProperty("playerIndex") int playerIndex, @JsonProperty("squadIndex") int squadIndex,
				@JsonProperty("moveTo") Coordinates moveTo) {
			this.playerIndex = playerIndex;
			this.squadIndex = squadIndex;
			this.moveTo = moveTo;
		}
	}

}
