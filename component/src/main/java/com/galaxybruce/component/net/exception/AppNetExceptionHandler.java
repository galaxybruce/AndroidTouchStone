package com.galaxybruce.component.net.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

import static com.galaxybruce.component.net.exception.AppRetrofitConstants.Code.*;
import static com.galaxybruce.component.net.exception.AppRetrofitConstants.Message.*;


/**
 * @date 2021/12/13 18:09
 * @author bruce.zhang
 * @description 统一处理异常信息
 * <p>
 * modification history:
 */
public class AppNetExceptionHandler {

    /**
     * 异常处理
     *
     * @param e 异常对象
     * @return KRetrofitException
     */
    public static Exception handleException(Throwable e) {
        Exception httpException;
        if (e instanceof HttpException) {
            httpException = new AppRetrofitException(MESSAGE_BAD_NETWORK, ((HttpException) e).code());
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            httpException = new AppRetrofitException(MESSAGE_CONNECT, ERROR_CONNECT);
        } else if (e instanceof InterruptedIOException) {
            httpException = new AppRetrofitException(MESSAGE_CONNECT_TIMEOUT, ERROR_CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            httpException = new AppRetrofitException(MESSAGE_PARSE, ERROR_PARSE);
        } else if (e instanceof AppNetException || e instanceof AppLoginExpiresException) {
            httpException = (Exception) e;
        } else {
            httpException = new AppRetrofitException(e.getMessage(), ERROR_UNKNOWN);
        }
        return httpException;
    }
}
