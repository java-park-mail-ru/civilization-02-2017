package com.hexandria.mechanics.base;

import com.hexandria.mechanics.avatar.UserAvatar;
import org.eclipse.jetty.server.Authentication;

/**
 * Created by root on 23.04.17.
 */
class Troops {
    private int amount;
    private int morale;
    private UserAvatar owner;

    public Troops(int amount, int morale, UserAvatar owner){
        this.amount = amount;
        this.morale = morale;
        this.owner = owner;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMorale() {
        return morale;
    }

    public void setMorale(int morale) {
        this.morale = morale;
    }
}
