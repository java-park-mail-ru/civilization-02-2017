package com.hexandria.websocket;

import org.jetbrains.annotations.NotNull;
/**
 * Created by root on 20.04.17.
 */
public class Message {
    @NotNull
    private String type;
    @NotNull
    private String content;

    @NotNull
    public String getType() {
        return type;
    }

    @NotNull
    public String getContent() {

        return content;
    }

    public Message(){

    }

    public Message (@NotNull String type, @NotNull String content){
        this.content = content;
        this.type = type;
    }

    public Message(@NotNull Class clazz, @NotNull String content){
        this(clazz.getName(), content);
    }

}
