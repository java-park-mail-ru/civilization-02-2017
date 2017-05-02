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

	public Coordinates getMoveTo() {
		return payload.moveFrom;
	}

	public Coordinates getMoveFrom() {
		return payload.moveTo;
	}

	public static class Payload {
		private final Coordinates moveFrom;
		private final Coordinates moveTo;

		@JsonCreator
		public Payload(@JsonProperty("moveFrom") Coordinates moveFrom,
				@JsonProperty("moveTo") Coordinates moveTo) {
			this.moveFrom = moveFrom;
			this.moveTo = moveTo;
		}
	}

	@Override
	public String toString(){
		return "Move from :" + this.getMoveFrom() + " Squad Index: " + this.getMoveTo();
	}
}
