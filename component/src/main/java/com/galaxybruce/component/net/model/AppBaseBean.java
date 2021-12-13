package com.galaxybruce.component.net.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by bruce.zhang on 2015/10/10.
 **/
public class AppBaseBean implements Serializable, IAppBean {

    /** 成功 */
    public static final String CODE_SUCCESS_1001 = "1001";
    public static final String CODE_SUCCESS_0 = "0";
    /** 未登录 */
    public static final String CODE_NOT_LOGIN = "1024";

    /** 状态码 */
    public String errorCode;
    public String errCode;
    protected String code = "-1";
    /** 状态码 */
    protected String errno = "-1";


    /** 状态码对应的错误信息 */
    protected String msg;
    protected String message;



    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        if (!TextUtils.isEmpty(message)) {
            return message;
        }
        return msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    @Override
    public boolean isSuccessful() {
        return CODE_SUCCESS_1001.equals(code);
    }

    @Override
    public boolean isExpireLogin() {
        return CODE_NOT_LOGIN.equals(getCode());
    }
}
