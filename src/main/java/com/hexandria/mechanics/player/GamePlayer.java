package com.hexandria.mechanics.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hexandria.mechanics.base.Capital;


/**
 * Created by root on 23.04.17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GamePlayer {
    @JsonIgnore
    private Long id;
    private String name;
    @JsonIgnore
    private Capital capitalCity;

    public GamePlayer(Long id, String name){
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

    public Capital getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(Capital capitalCity) {
        this.capitalCity = capitalCity;
    }
}
