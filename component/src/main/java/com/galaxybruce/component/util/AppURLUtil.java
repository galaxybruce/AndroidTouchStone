package com.galaxybruce.component.util;


import android.net.Uri;
import android.text.TextUtils;

import com.blankj.utilcode.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.galaxybruce.component.util.AppConstants.EMPTY_STR;

/**
 * url处理
 * @author Administrator
 */
public class AppURLUtil {

    /**
     * 获取uri参数值
     *
     * @param url
     * @param key
     * @return
     */
    public static String getUriParamValue(String url, String key) {
        String value = null;
        if (TextUtils.isEmpty(url)
                || TextUtils.isEmpty(key)) {
            return EMPTY_STR;
        }

        //最好以这种昂方式获取，正则表达式对片段#处理不正确
        Uri uri = Uri.parse(url);
        String parameter = uri.getQueryParameter(key);
        if (TextUtils.isEmpty(parameter)) {
            if (url.contains("#")) {
                url = url.replace("#","");
                return getUriParamValue(url, key);
            }
        }
        return StringUtils.null2Length0(parameter);

//        Pattern pattern = Pattern.compile("(" + name +"=([^&#]*))", Pattern.CASE_INSENSITIVE);
//        Matcher m = pattern.matcher(string);
//        if (m.find())
//        {
////            String text0 = m.group(0);
////            String text1 = m.group(1);
//            String value = m.group(2);
//            try {
//                return URLDecoder.decode(value, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                return value;
//            }
//        }
//        return "";
    }

    /**
     * 获取不规则url中的参数
     * 这个url可能是不完整的，比如url仅仅只有cmd: cmd=aaaa&a=1
     * @param url
     * @param key
     * @return
     */
    public static String getInvalidUriParamValue(String url, String key) {
        String value = "";
        if (TextUtils.isEmpty(url)
                || TextUtils.isEmpty(key)) {
            return value;
        }

        Pattern pattern = Pattern.compile("(" + key +"=([^&#]*))", Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(url);
        if (m.find()) {
            try {
                value = m.group(2);
                return URLDecoder.decode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return value;
            }
        }
        return "";
    }

    /**
     * 替换url参数
     *
     * @param url
     * @param name
     * @param value
     * @return
     */
    public static String replaceUriParamValue(String url, String name, String value) {
        return url.replaceAll("(" + name + "=([^&#]*))", name + "=" + value);
        //		return url.replaceAll("(([?|&])" + name +"=[^&]*)", "$2" + name + "=" + accessToken);
    }

    /**
     * 给url添加参数
     *
     * @param url
     * @param key   参数key
     * @param value 参数value
     * @return
     */
    public static String addUrlParam(String url, String key, String value) {
        try {
            URL currentURL = new URL(url);
            if (TextUtils.isEmpty(currentURL.getQuery())) {
                url += "?";
            } else {
                url += "&";
            }
            url += key + "=" + value;
        } catch (MalformedURLException e) {
        }

        return url;
    }

    /**
     * 删除url中的所有参数以及fragment
     * @param url
     * @return
     */
    public static String deleteAllParams(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        int queryIndex = url.indexOf("?");
        if(queryIndex < 0) {
            queryIndex = url.indexOf("#");
        }
        if(queryIndex <= 0) {
            return url;
        }
        return url.substring(0, queryIndex);
    }
}
