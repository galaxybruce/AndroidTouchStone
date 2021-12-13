package com.galaxybruce.component.net.exception;

/**
 * @date 2021/12/13 18:10
 * @author bruce.zhang
 * @description 异常对象
 * <p>
 * modification history:
 */
public class AppRetrofitException extends Exception {

    private int code;

    public AppRetrofitException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
