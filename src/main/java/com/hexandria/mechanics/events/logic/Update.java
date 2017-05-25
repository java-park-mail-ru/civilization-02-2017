package com.hexandria.mechanics.events.logic;

import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.websocket.Message;
import org.jetbrains.annotations.Nullable;

/**
 * Created by root on 25.04.17.
 */

public class Update extends Message {
    public final Payload payload;

    public Update(Coordinates position, @Nullable Coordinates newPosition, @Nullable Integer newCount, @Nullable Integer newMorale){
        this.payload = new Payload(position, newPosition, newCount, newMorale);
    }

    public static class Payload{
        public final Coordinates position;
        public final Coordinates newPosition;
        public final Integer newCount;
        public final Integer newMorale;

        public Payload(Coordinates position, Coordinates newPosition, Integer newCount, Integer newMorale){
            this.position = position;
            this.newPosition = newPosition;
            this.newCount = newCount;
            this.newMorale = newMorale;
        }
    }
}
