package com.galaxybruce.component.net.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import retrofit2.HttpException;



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
        if (e instanceof AppNetException || e instanceof AppLoginExpiresException) {
            httpException = (Exception) e;
        } else if (e instanceof HttpException) {
            httpException = new AppNetException(AppNetError.ERROR_BAD_NETWORK, e);
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            httpException = new AppNetException(AppNetError.ERROR_CONNECT, e);
        } else if (e instanceof ConnectTimeoutException || e instanceof SocketTimeoutException) {
            httpException = new AppNetException(AppNetError.ERROR_CONNECT_TIMEOUT, e);
        } else if (e instanceof JsonParseException || e instanceof JSONException ||
                e instanceof ParseException || e instanceof MalformedJsonException) {
            httpException = new AppNetException(AppNetError.ERROR_PARSE, e);
        } else if (e instanceof SSLException) {
            httpException = new AppNetException(AppNetError.ERROR_SSL, e);
        } else {
            httpException = new AppNetException(AppNetError.ERROR_UNKNOWN, e);
        }
        return httpException;
    }
}
