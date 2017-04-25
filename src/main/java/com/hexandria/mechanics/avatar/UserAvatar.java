package com.hexandria.mechanics.avatar;

import com.hexandria.mechanics.base.Capital;
import com.hexandria.mechanics.base.Squad;
import com.hexandria.mechanics.base.Town;
import org.eclipse.jetty.server.Authentication;

import java.util.List;


/**
 * Created by root on 23.04.17.
 */
public class UserAvatar {
    private Long id;
    private String name;
    private String color;
    private Capital capitalCity;
    private List<Town> towns;
    private List<Squad> squads;

    public UserAvatar(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Capital getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(Capital capitalCity) {
        this.capitalCity = capitalCity;
    }

    public List<Town> getTowns() {
        return towns;
    }

    public void setTowns(List<Town> towns) {
        this.towns = towns;
    }

    public List<Squad> getSquads() {
        return squads;
    }

    public void setSquads(List<Squad> squads) {
        this.squads = squads;
    }
}
