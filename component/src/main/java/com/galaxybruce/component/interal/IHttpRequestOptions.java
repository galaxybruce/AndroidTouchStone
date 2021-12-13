package com.galaxybruce.component.interal;

import java.util.Map;

/**
 * @author bruce.zhang
 * @date 2021/6/5 20:49
 * @description http请求额外的cookie和header
 * <p>
 * modification history:
 */
public interface IHttpRequestOptions {

    void addCookies(Map<String, String> params, Map<String, String> cookies);

    Map<String, String> addHeaders(Map<String, String> params);
}
