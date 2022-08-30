package com.galaxybruce.component.internal;

import java.util.List;
import java.util.Map;

/**
 * @author bruce.zhang
 * @date 2021/6/5 20:49
 * @description http请求额外的cookie和header
 * <p>
 * modification history:
 */
public interface IHttpRequestOptions {

    /**
     * 请求cookie
     * 有两部分组成，CookieManager.generateCommonCookie() + 额外的业务cookie
     * @param requestParams 请求中参数生成的Map
     */
    Map<String, String> generateCookies(Map<String, String> requestParams);

    /**
     * WebView中的cookie
     * 有两部分组成，CookieManager.generateCommonCookie() + 额外的业务cookie
     * @return
     */
    Map<String, String> generateWebCookies();

    /**
     * 请求header中增加参数
     * @param requestParams 请求中参数生成的Map
     * @return
     */
    Map<String, String> generateHeaders(Map<String, String> requestParams);

    /**
     * 添加cookie的域名
     * 在app里挂了站外地址，域名需要在这里提供
     * */
    List<String> getCookieDomains();

    /**
     * url转换
     * @param url
     * @return
     */
    String convertUrl(String url);

}
