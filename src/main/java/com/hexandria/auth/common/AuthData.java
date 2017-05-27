package com.hexandria.auth.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class AuthData {
    protected final String login;
    protected final String password;
    protected final String email;
    @JsonCreator
    public AuthData(@JsonProperty("login") String login, @JsonProperty("password") String password, @JsonProperty("email") String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}