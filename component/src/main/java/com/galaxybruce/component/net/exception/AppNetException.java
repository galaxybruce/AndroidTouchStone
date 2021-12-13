package com.galaxybruce.component.net.exception;


public class AppNetException extends Exception {
    private String code;

    public AppNetException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
