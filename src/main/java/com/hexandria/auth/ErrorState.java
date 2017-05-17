package com.hexandria.auth;


public enum ErrorState {
    FORBIDDEN(403), NOT_FOUND(404), BAD_REQUEST(400), CONFLICT(409);
    private final int value;

    ErrorState(int value) {
        this.value = value;
    }

    public int getCode() {
        return value;
    }
}
