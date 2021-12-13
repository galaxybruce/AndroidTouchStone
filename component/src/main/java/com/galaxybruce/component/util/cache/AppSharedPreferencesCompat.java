package com.galaxybruce.component.util.cache;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.Map;
import java.util.Set;

/**
 * @date 2018/5/25 17:49 
 * @author bruce.zhang
 * @description 内部实现可以考虑改成MMKV
 * <p>
 * modification history:
 */
public class AppSharedPreferencesCompat {

    private AppSharedPreferencesCompat() {
        throw new SharedPreferencesException("Stub!");
    }

    private static SharedPreferences getSharedPrefs(Context context, String sharedPrefsName) {
        checkNotNull(context, "Context can not be null!");
        if(TextUtils.isEmpty(sharedPrefsName)) {
            return PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            return context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE);
        }
    }

    public static void put(Context context, String sharedPrefsName, String key, String value) {
        checkNotEmpty(key, "SharedPreferences key can not be empty!");
        if(value == null) {
            value = "";
        }
        SharedPreferences.Editor editor = getSharedPrefs(context, sharedPrefsName).edit();
        editor.putString(key, value);
        SharedPreferencesEditorCompat.apply(editor);
    }

    public static String get(Context context, String sharedPrefsName, String key, String defValue) {
        checkNotEmpty(key, "SharedPreferences key can not be empty!");
        return getSharedPrefs(context, sharedPrefsName).getString(key, defValue);
    }

    public static void put(Context context, String sharedPrefsName, String key, int value) {
        checkNotEmpty(key, "SharedPreferences key can not be empty!");
        SharedPreferences.Editor editor = getSharedPrefs(context, sharedPrefsName).edit();
        editor.putInt(key, value);
        SharedPreferencesEditorCompat.apply(editor);
    }

    public static int get(Context context, String sharedPrefsName, String key, int defValue) {
        checkNotEmpty(key, "SharedPreferences key can not be empty!");
        return getSharedPrefs(context, sharedPrefsName).getInt(key, defValue);
    }

    public static void put(Context context, String sharedPrefsName, String key, long value) {
        checkNotEmpty(key, "SharedPreferences key can not be empty!");
        SharedPreferences.Editor editor = getSharedPrefs(context, sharedPrefsName).edit();
        editor.putLong(key, value);
        SharedPreferencesEditorCompat.apply(editor);
    }

    public static long get(Context context, String sharedPrefsName, String key, long defValue) {
        checkNotEmpty(key, "SharedPreferences key can not be empty!");
        return getSharedPrefs(context, sharedPrefsName).getLong(key, defValue);
    }

    public static void put(Context context, String sharedPrefsName, String key, boolean value) {
        checkNotEmpty(key, "SharedPreferences key can not be empty!");
        SharedPreferences.Editor editor = getSharedPrefs(context, sharedPrefsName).edit();
        editor.putBoolean(key, value);
        SharedPreferencesEditorCompat.apply(editor);
    }

    public static boolean get(Context context, String sharedPrefsName, String key, boolean defValue) {
        checkNotEmpty(key, "SharedPreferences key can not be empty!");
        return getSharedPrefs(context, sharedPrefsName).getBoolean(key, defValue);
    }

    public static void clear(Context context, String sharedPrefsName) {
        SharedPreferences.Editor editor = getSharedPrefs(context, sharedPrefsName).edit();
        editor.clear();
        SharedPreferencesEditorCompat.apply(editor);
    }

    public static void remove(Context context, String sharedPrefsName, String key) {
        checkNotEmpty(key, "SharedPreferences key can not be empty!");
        SharedPreferences.Editor editor = getSharedPrefs(context, sharedPrefsName).edit();
        editor.remove(key);
        SharedPreferencesEditorCompat.apply(editor);
    }

    public static Map<String, ?> getAll(Context context, String sharedPrefsName) {
        checkNotNull(context, "Context can not be null!");
        return getSharedPrefs(context, sharedPrefsName).getAll();
    }

    public static boolean contains(Context context, String sharedPrefsName, String key) {
        checkNotEmpty(key, "SharedPreferences key can not be empty!");
        return getSharedPrefs(context, sharedPrefsName).contains(key);
    }

    public static Set<String> getStringSet(Context context, String sharedPrefsName, String key, Set<String> defValues) {
        checkNotEmpty(key, "SharedPreferences key can not be empty!");
        return getSharedPrefs(context, sharedPrefsName).getStringSet(key, defValues);
    }

    public static Builder newBuilder(Context context, String sharedPrefsName) {
        return new Builder(context, sharedPrefsName);
    }

    public static final class Builder {

        private SharedPreferences.Editor mEditor;

        private Builder(Context context, String sharedPrefsName) {
            mEditor = getSharedPrefs(context, sharedPrefsName).edit();
        }

        public Builder put(String key, String value) {
            checkNotEmpty(key, "SharedPreferences key can not be empty!");
            if(value == null) {
                value = "";
            }
            mEditor.putString(key, value);
            return this;
        }

        public Builder put(String key, int value) {
            checkNotEmpty(key, "SharedPreferences key can not be empty!");
            mEditor.putInt(key, value);
            return this;
        }

        public Builder put(String key, long value) {
            checkNotEmpty(key, "SharedPreferences key can not be empty!");
            mEditor.putLong(key, value);
            return this;
        }

        public Builder put(String key, boolean value) {
            checkNotEmpty(key, "SharedPreferences key can not be empty!");
            mEditor.putBoolean(key, value);
            return this;
        }

        public Builder put(String key, float value) {
            checkNotEmpty(key, "SharedPreferences key can not be empty!");
            mEditor.putFloat(key, value);
            return this;
        }

        public Builder remove(String key) {
            checkNotEmpty(key, "SharedPreferences key can not be empty!");
            mEditor.remove(key);
            return this;
        }

        public void apply() {
            if (mEditor != null){
                SharedPreferencesEditorCompat.apply(mEditor);
            }
        }

        public void commit() {
            if (mEditor != null) {
                SharedPreferencesEditorCompat.commit(mEditor);
            }
        }
    }

    private static <T> void checkNotNull(T t, String message) {
        if (t == null) {
            throw new SharedPreferencesException(String.valueOf(message));
        }
    }

    private static void checkNotEmpty(String t, String message) {
        if (TextUtils.isEmpty(t)) {
            throw new SharedPreferencesException(String.valueOf(message));
        }
    }

    private static final class SharedPreferencesException extends RuntimeException {
        public SharedPreferencesException(String message, Throwable cause) {
            super(message, cause);
        }

        public SharedPreferencesException(String message) {
            super(message);
        }
    }

    private static final class SharedPreferencesEditorCompat {

        static void apply(final SharedPreferences.Editor editor) {
            try {
                editor.apply();
            } catch (Throwable e) {
                AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        editor.commit();
                    }
                });
            }
        }

        static void commit(SharedPreferences.Editor editor) {
            editor.commit();
        }
    }
}