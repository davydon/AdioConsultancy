package com.management.exceptions;

/**
 * Created by Lukman.Arogundade on 4/30/2015.
 */
public class UserLoginException extends AbstractException {

    public UserLoginException(String code, String message) {
        super(code, message);
    }
}
