package sample.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sample.auth.ERRORSTATE;

public class ErrorResponse {
    @JsonProperty("error")
    private final String errorText;
    @JsonIgnore
    private final ERRORSTATE errorStatus;

    public String getErrorText() {
        return errorText;
    }

    public ErrorResponse(String errorText, ERRORSTATE errorStatus) {
        this.errorText = errorText;
        this.errorStatus = errorStatus;
    }

    public ERRORSTATE getErrorStatus() {
        return errorStatus;
    }
}
