package com.galaxybruce.component.net.cookie;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.blankj.utilcode.util.StringUtils;
import com.galaxybruce.component.interal.AppInternal;
import com.galaxybruce.component.interal.IHttpRequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author bruce.zhang
 * @date 2020/10/27 15:18
 * @description h5 cookie 同步
 * <p>
 * modification history:
 */
public class AppH5CookieManager {

    public static void setWebCookie(Context context, android.webkit.WebView webView) {
        CookieSyncManager syncManager = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        cookieManager.setAcceptCookie(true);
        Map<String, String> cookies = generateWebCookies();
        Set<Map.Entry<String, String>> set = cookies.entrySet();
        for (Map.Entry<String, String> entry : set) {
            IHttpRequestOptions httpRequestOptions = AppInternal.getInstance().getHttpRequestOptions();
            if(httpRequestOptions != null) {
                List<String> domains = new ArrayList<>();
                List<String> cookieDomains = httpRequestOptions.getCookieDomains();
                if (cookieDomains != null && !cookieDomains.isEmpty()) {
                    domains.addAll(cookieDomains);
                }
                if (domains.isEmpty()) {
                    break;
                }
                for (String domain : domains) {
                    cookieManager.setCookie(domain, entry.getKey() + "=" + StringUtils.null2Length0(entry.getValue()) + ";domain=" + domain);
                }
            }
        }
        syncManager.sync();
    }

    public static Map<String, String> generateWebCookies() {
        Map<String, String> cookies = null;
        IHttpRequestOptions httpRequestOptions = AppInternal.getInstance().getHttpRequestOptions();
        if (httpRequestOptions != null) {
            cookies = httpRequestOptions.generateWebCookies();
        }
        if (cookies == null) {
            cookies = new HashMap<>();
        }
        return cookies;
    }

}
