package com.management.exceptions;


/**
 * Created by Segun.Oladapo on 4/15/2016.
 */
public class FailedRequestException extends AbstractException {

    public FailedRequestException(String code, String message) {
        super(code, message);

    }
}
