package com.galaxybruce.component.util;


import android.content.Context;
import android.net.ConnectivityManager;

/**
 * @author bruce.zhang
 * @date 2019/3/20 14:13
 * @description 检测网络的一个工具包
 * <p>
 * modification history:
 */
public class NetworkUtil {

    public static boolean hasInternet(Context context) {
        return ((ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}