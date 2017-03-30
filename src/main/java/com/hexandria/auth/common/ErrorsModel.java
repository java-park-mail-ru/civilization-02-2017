package sample.auth.models;

import java.util.List;

/**
 * Created by frozenfoot on 28.03.17.
 */
public class ErrorsModel {
    private final List<ErrorResponse> errors;

    public ErrorsModel(List<ErrorResponse> errors){
        this.errors = errors;
    }
}
