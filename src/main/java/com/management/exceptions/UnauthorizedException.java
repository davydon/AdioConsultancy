package com.management.exceptions;

/**
 * Created by Lukman.Arogundade on 11/1/2016.
 */
public class UnauthorizedException extends AbstractException {

    public UnauthorizedException(String code, String message) {
        super(code, message);
    }
}