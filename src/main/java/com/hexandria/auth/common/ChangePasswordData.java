package com.hexandria.auth.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordData extends AuthData {
    private final String newPassword;

    public ChangePasswordData(@JsonProperty("login") String login, @JsonProperty("password") String password,
                              @JsonProperty("email") String email, @JsonProperty("newPassword") String newPassword) {
        super(login, password, email);
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
