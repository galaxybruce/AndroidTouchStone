package com.galaxybruce.component.net;

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

    public AppException(String detailMessage) {
        super(detailMessage);
    }

    public AppException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AppException(String detailMessage, ErrorType error) {
        super(detailMessage);
        this.errorType = error;
    }

    public AppException(String detailMessage, int errorCode) {
        super(detailMessage);
        this.errorCode = errorCode;
    }

    public AppException(String detailMessage, ErrorType error, int errorCode) {
        super(detailMessage);
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
