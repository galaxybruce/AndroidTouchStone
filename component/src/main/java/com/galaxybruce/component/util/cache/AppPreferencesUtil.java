package com.galaxybruce.component.util.cache;

import android.content.Context;

import com.galaxybruce.component.app.BaseApplication;


/**
 * @date 2020/4/4 11:29
 * @author bruce.zhang
 * @description 简单字段类型的数据保存，如果是json串保存，请使用{@link AppBigDataCacheManager}
 * <p>
 * modification history:
 */
public class AppPreferencesUtil {

    public static Context getContext() {
        return BaseApplication.instance;
    }


    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String defValue) {
        return AppSharedPreferencesCompat.get(getContext(),
                null, key, defValue);
    }

    public static String getString(String sharedPrefsName, String key, String defValue) {
        return AppSharedPreferencesCompat.get(getContext(),
                sharedPrefsName, key, defValue);
    }

    public static void putString(String key, String value) {
        AppSharedPreferencesCompat.put(getContext(),
                null, key, value);
    }

    public static void putString(String sharedPrefsName, String key, String value) {
        AppSharedPreferencesCompat.put(getContext(),
                sharedPrefsName, key, value);
    }

    public static int getInt(String key, int defValue) {
        return AppSharedPreferencesCompat.get(getContext(),
                null, key, defValue);
    }

    public static void putInt(String key, int value) {
        AppSharedPreferencesCompat.put(getContext(),
                null, key, value);
    }

    public static int getInt(String sharedPrefsName, String key, int defValue) {
        return AppSharedPreferencesCompat.get(getContext(),
                sharedPrefsName, key, defValue);
    }

    public static void putInt(String sharedPrefsName, String key, int value) {
        AppSharedPreferencesCompat.put(getContext(),
                sharedPrefsName, key, value);
    }

    public static long getLong(String key) {
        return getLong(key, 0);
    }

    public static long getLong(String key, long defValue) {
        return AppSharedPreferencesCompat.get(getContext(),
                null, key, defValue);
    }

    public static void putLong(String key, long value) {
        AppSharedPreferencesCompat.put(getContext(),
                null, key, value);
    }

    public static void putLong(String sharedPrefsName, String key, long value) {
        AppSharedPreferencesCompat.put(getContext(),
                sharedPrefsName, key, value);
    }

    public static long getLong(String sharedPrefsName, String key, long defValue) {
        return AppSharedPreferencesCompat.get(getContext(),
                sharedPrefsName, key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return AppSharedPreferencesCompat.get(getContext(),
                null, key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        AppSharedPreferencesCompat.put(getContext(),
                null, key, value);
    }

    public static boolean getBoolean(String sharedPrefsName, String key, boolean defValue) {
        return AppSharedPreferencesCompat.get(getContext(),
                sharedPrefsName, key, defValue);
    }

    public static void putBoolean(String sharedPrefsName, String key, boolean value) {
        AppSharedPreferencesCompat.put(getContext(),
                sharedPrefsName, key, value);
    }

    public static void remove(String key) {
        AppSharedPreferencesCompat.remove(getContext(),
                null, key);
    }

    public static boolean contains(String key) {
        return AppSharedPreferencesCompat.contains(getContext(),
                null, key);
    }

    public static void remove(String sharedPrefsName, String key) {
        AppSharedPreferencesCompat.remove(getContext(),
                sharedPrefsName, key);
    }

    public static boolean contains(String sharedPrefsName, String key) {
        return AppSharedPreferencesCompat.contains(getContext(),
                sharedPrefsName, key);
    }
}
