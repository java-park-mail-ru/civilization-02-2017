package com.hexandria.mechanics.events.logic;

import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.mechanics.base.Town;
import com.hexandria.websocket.Message;

/**
 * Created by frozenfoot on 16.05.17.
 */
public class Create extends Message {
    public final Payload payload;

    public Create(Town town) {
        payload = new Payload(town);
    }

    public static class Payload{
        public final Integer count;
        public final Integer morale;
        public final String owner;
        public final Coordinates position;

        public Payload(Town town) {
            this.count = town.getSquad().getCount();
            this.morale = town.getSquad().getMorale();
            this.owner = town.getOwner().getName();
            this.position = town.getPosition();
        }
    }
}
