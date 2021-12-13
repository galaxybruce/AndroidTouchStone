package com.galaxybruce.component.util.exception;

/**
 * @date 2019/3/20 11:11
 * @author bruce.zhang
 * @description
 * <p>
 * modification history:
 */
public class AppException extends IllegalArgumentException {

    public enum ErrorType {
        NET, PARSE
    }

    private ErrorType errorType;
    private int errorCode;

    public AppException() {
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AppException(String message, ErrorType error) {
        super(message);
        this.errorType = error;
    }

    public AppException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AppException(String message, ErrorType error, int errorCode) {
        super(message);
        this.errorType = error;
        this.errorCode = errorCode;
    }

    public AppException(Throwable throwable) {
        super(throwable);
    }

    public boolean isNetError() {
        return errorType == ErrorType.NET
                || errorType == ErrorType.PARSE;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
