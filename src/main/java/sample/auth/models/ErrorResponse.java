package sample.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sample.auth.ErrorState;

public class ErrorResponse {
    @JsonProperty("error")
    private final String errorText;
    @JsonIgnore
    private final ErrorState errorStatus;

    public String getErrorText() {
        return errorText;
    }

    public ErrorResponse(String errorText, ErrorState errorStatus) {
        this.errorText = errorText;
        this.errorStatus = errorStatus;
    }

    public ErrorState getErrorStatus() {
        return errorStatus;
    }
}
