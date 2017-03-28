package sample.auth.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessResponseMessage {
    @JsonProperty("description")
    private final String description;

    public SuccessResponseMessage(@JsonProperty String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
