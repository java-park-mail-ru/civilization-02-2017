package sample.auth;


public enum ERRORSTATE {
    FORBIDDEN(403), NOT_FOUND(404), BAD_REQUEST(400), CONFLICT(409);
    private int value;

    ERRORSTATE(int value) {
        this.value = value;
    }

    public int getCode() {
        return value;
    }
}
