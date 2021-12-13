package com.galaxybruce.component.net.cookie;

import android.text.TextUtils;

import com.galaxybruce.component.interal.AppInternal;
import com.galaxybruce.component.interal.IHttpRequestOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AppCookieManager {

    private Map<String, String> externalCookies = new HashMap<>();
    private Map<String, String> manualCookies = new HashMap<>();

    private AppCookieManager() {
    }

    private static class AppCookieManagerHolder {
        static AppCookieManager INSTANCE = new AppCookieManager();
    }

    public static AppCookieManager getInstance() {
        return AppCookieManagerHolder.INSTANCE;
    }

    private Map<String, String> generateCommonCookie() {
        Map<String, String> params = new HashMap<>();
        try {
            putNonNullValue(params, "source", "android");
            handleManualCookies(manualCookies, params);
        } catch (Exception ex) {

        }
        return params;
    }

    private void handleManualCookies(Map<String, String> manualCookies, Map<String, String> params){
        if (manualCookies == null || manualCookies.isEmpty()){
            return;
        }
        for (String key : manualCookies.keySet()) {
            if (TextUtils.isEmpty(manualCookies.get(key))){
                params.remove(key);
            }else{
                params.put(key, manualCookies.get(key));
            }
        }
    }

    public void updateManualCookie(String key, String value) {
        manualCookies.put(key, value);
    }

    private static void putNonNullValue(Map<String, String> params, String key, String value) {
        params.put(key, TextUtils.isEmpty(value) ? "" : value);
    }

    public static Map<String, String> getHeaders(Map<String, String> requestParams) {
        Map<String, String> headersMap = new HashMap<>();
        IHttpRequestOptions httpRequestOptions = AppInternal.getInstance().getHttpRequestOptions();
        if (httpRequestOptions != null) {
            Map<String, String> headers = httpRequestOptions.generateHeaders(requestParams);
            if (headers != null && !headers.isEmpty()) {
                headersMap.putAll(headers);
            }
            Map<String, String> cookies = httpRequestOptions.generateCookies(requestParams);
            if (cookies != null && !cookies.isEmpty()) {
                headersMap.put("Cookie", formatCookie(cookies));
            }
        }
        return headersMap;
    }

    private static String formatCookie(Map<String, String> cookies) {
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String, String>> set = cookies.entrySet();
        for (Map.Entry<String, String> entry : set) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.append(key).append("=").append(value).append(";");
        }
        return builder.toString();
    }
}
