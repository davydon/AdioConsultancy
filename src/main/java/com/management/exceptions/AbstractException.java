package com.management.exceptions;

/**
 * Created by Segun.Oladapo on 10/28/2015.
 */
public class AbstractException extends RuntimeException {

    public AbstractException(String code, String message) {

        super(message);
        this.setCode(code);
    }


    private String code;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
