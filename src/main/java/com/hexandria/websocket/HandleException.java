package com.hexandria.websocket;

import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * Created by root on 20.04.17.
 */
public class HandleException extends Exception {

    public HandleException (String message, Throwable clause){
        super(message, clause);
    }

    public HandleException(String message){
        super(message);
    }
}
