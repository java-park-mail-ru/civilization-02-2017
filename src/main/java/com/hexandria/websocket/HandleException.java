package com.hexandria.websocket;


/**
 * Created by root on 09.04.17.
 */

public class HandleException extends Exception {
    public HandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandleException(String message) {
        super(message);
    }
}
