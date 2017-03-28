package sample.auth.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordCredentials extends AuthorizationCredentials {
    private final String newPassword;

    public ChangePasswordCredentials(@JsonProperty("login") String login, @JsonProperty("password") String password,
                                     @JsonProperty("email") String email, @JsonProperty("newPassword") String newPassword) {
        super(login, password, email);
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
