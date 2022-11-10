package com.galaxybruce.component.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.galaxybruce.component.app.BaseApplication;
import com.galaxybruce.component.util.log.AppLogUtils;

import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.List;

public class AppBuildConfigUtils {
    private static JSONObject jsonObject = null;

    public static JSONObject getAppMetaData() {
        if (jsonObject != null) {
            return jsonObject;
        }
        try {
            ApplicationInfo appInfo = BaseApplication.instance.getPackageManager().getApplicationInfo(
                    BaseApplication.instance.getPackageName(),
                    PackageManager.GET_META_DATA
            );
            String value = appInfo.metaData.getString("BUILD_CONFIG");
            value = URLDecoder.decode(value, "UTF-8");
            jsonObject = new JSONObject(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    public static String getAppMetaDataString(String key) {
        return getAppMetaDataString(key, "");
    }

    public static String getAppMetaDataString(String key, String defValue) {
        String value = getAppMetaData().optString(key);
        if(TextUtils.isEmpty(value)) {
            value = defValue;
        }
        AppLogUtils.i("BuildConfigUtils.getAppMetaDataString.key[" + key + "]=" + value);
        return value;
    }

    public static int getAppMetaDataInt(String key) {
        return getAppMetaData().optInt(key);
    }

    public static boolean getAppMetaDataBoolean(String key) {
        return getAppMetaData().optBoolean(key);
    }

    public static boolean getAppMetaDataBoolean(String key, boolean defValue) {
        return getAppMetaData().optBoolean(key, defValue);
    }

    public static List<String> getAppMetaDataList(String key) {
        return JSONArray.parseArray(getAppMetaData().optString(key), String.class);
    }
}
