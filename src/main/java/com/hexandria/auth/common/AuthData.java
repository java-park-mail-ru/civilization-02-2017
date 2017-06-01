package com.hexandria.auth.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthData {
    private final String login;
    private final String password;
    private final String email;
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