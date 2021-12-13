package com.galaxybruce.component.net.exception;


/**
 * description：异常的统一处理
 * <p>
 * author： DS.Hu
 * <p>
 * time： 2018/7/23 18:58
 * <p>
 */

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
