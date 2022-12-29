package com.galaxybruce.component.util.imageloader;

import android.text.TextUtils;

/**
 * @author bruce.zhang
 * @date 2018/5/26 10:51
 * @description 图片url处理 ，目前是腾讯云的
 * <p>
 * modification history:
 */
public class AppImageUrlFormatter {

    static final String NETWORK_IMAGE_NORMAL = "?imageView2/2/w/%d/h/%d/q/%d";
    static final String NETWORK_IMAGE_WEBP_QUALITY_SIZE = "?imageView2/2/w/%d/h/%d/format/webp/q/%d";
    static final String NETWORK_IMAGE_WEBP_QUALITY = "?imageMogr2/format/webp/quality/%d";
    ///    public static final String NETWORK_IMAGE_GIF_FORMAT1 = "?imageView2/2/w/%d/h/%d";
///    public static final String NETWORK_IMAGE_GIF_FORMAT = "?imageMogr2/format/gif/quality/60";

    public static String formatImage(String url, int width, int height, int quality) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        if (!(url.startsWith("http") || url.startsWith("HTTP"))) {
            return url;
        }
        url = delUrlParam(url);
        StringBuilder stringBuilder = new StringBuilder(url);
        //微信用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像）
        if (url.startsWith("http://wx.qlogo.cn/")) {
            return stringBuilder.replace(stringBuilder.length() - 1,
                    stringBuilder.length(),
                    String.valueOf(width)).toString();
        }
        return stringBuilder.append(String.format(NETWORK_IMAGE_WEBP_QUALITY_SIZE, width, height, quality)).toString();
    }

    public static String formatImage(String url) {
        return formatImage(url, 90);
    }

    public static String formatImage(String url, int quality) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        if (!(url.startsWith("http") || url.startsWith("HTTP"))) {
            return url;
        }
        url = delUrlParam(url);
        return url.concat(String.format(NETWORK_IMAGE_WEBP_QUALITY, quality));
    }

    /**
     * 通用格式，所有平台都适用
     *
     * @param url
     * @param width
     * @param height
     * @param quality
     * @return
     */
    public static String formatImageNormal(String url, int width, int height, int quality) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        url = delUrlParam(url);
        StringBuilder stringBuilder = new StringBuilder(url);
        return stringBuilder.append(String.format(NETWORK_IMAGE_NORMAL, width, height, quality)).toString();
    }

    private static String delUrlParam(String url) {
        int index = url.lastIndexOf("?");
        if (index > 0) {
            url = url.substring(0, index);
        }
        return url;
    }
}
