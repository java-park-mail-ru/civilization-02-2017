package com.hexandria.mechanics.events.logic;

import com.hexandria.mechanics.avatar.UserAvatar;
import com.hexandria.mechanics.base.Squad;
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
        public final Squad squad;
        public final UserAvatar owner;
        public Payload(Town town) {
            this.squad = new Squad(town.getTroopsGenerated(), town.getMoraleGenerated(), town.getOwner());
            this.owner = town.getOwner();
        }
    }
}
