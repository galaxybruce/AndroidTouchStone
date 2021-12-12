package com.galaxybruce.component.util.crosssp;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.CURSOR_COLUMN_NAME;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.CURSOR_COLUMN_TYPE;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.CURSOR_COLUMN_VALUE;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.NULL_STRING;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.SEPARATOR;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.TYPE_BOOLEAN;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.TYPE_CLEAN;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.TYPE_CONTAIN;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.TYPE_FLOAT;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.TYPE_GET_ALL;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.TYPE_INT;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.TYPE_LONG;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.TYPE_STRING;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.TYPE_STRING_SET;
import static com.galaxybruce.component.util.crosssp.KWCrossProcessUtils.VALUE;

/**
 * @date 2017/12/18 17:34
 * @author bruce.zhang
 * @description 跨进程保存数据 sp
 * <p>
 * modification history:
 */
public class KWCrossProcessSPHelper {
    public static final String COMMA_REPLACEMENT = "__COMMA__";

    public static void checkContext(Context context) {
        if (context == null) {
            throw new IllegalStateException("context has not been initialed");
        }
    }

    public synchronized static void save(Context context, String name, Boolean t) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_BOOLEAN, name);
        ContentValues cv = new ContentValues();
        cv.put(VALUE, t);
        cr.update(uri, cv, null, null);
    }

    public synchronized static void save(Context context, String name, String t) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_STRING, name);
        ContentValues cv = new ContentValues();
        cv.put(VALUE, t);
        cr.update(uri, cv, null, null);
    }

    public synchronized static void save(Context context, String name, Integer t) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_INT, name);
        ContentValues cv = new ContentValues();
        cv.put(VALUE, t);
        cr.update(uri, cv, null, null);
    }

    public synchronized static void save(Context context, String name, Long t) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_LONG, name);
        ContentValues cv = new ContentValues();
        cv.put(VALUE, t);
        cr.update(uri, cv, null, null);
    }

    public synchronized static void save(Context context, String name, Float t) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_BOOLEAN, name);
        ContentValues cv = new ContentValues();
        cv.put(VALUE, t);
        cr.update(uri, cv, null, null);
    }


    public synchronized static void save(Context context, String name, Set<String> t) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_STRING_SET, name);
        ContentValues cv = new ContentValues();
        Set<String> convert = new HashSet<>();
        for (String string : t) {
            convert.add(string.replace(",", COMMA_REPLACEMENT));
        }
        cv.put(VALUE, convert.toString());
        cr.update(uri, cv, null, null);
    }

    public static String getString(Context context, String name, String defaultValue) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_STRING, name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return defaultValue;
        }
        return rtn;
    }

    public static int getInt(Context context, String name, int defaultValue) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_INT, name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return defaultValue;
        }
        return Integer.parseInt(rtn);
    }

    public static float getFloat(Context context, String name, float defaultValue) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_FLOAT, name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return defaultValue;
        }
        return Float.parseFloat(rtn);
    }

    public static boolean getBoolean(Context context, String name, boolean defaultValue) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_BOOLEAN, name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(rtn);
    }

    public static long getLong(Context context, String name, long defaultValue) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_LONG, name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return defaultValue;
        }
        return Long.parseLong(rtn);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getStringSet(Context context, String name, Set<String> defaultValue) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_STRING_SET, name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return defaultValue;
        }
        if (!rtn.matches("\\[.*\\]")) {
            return defaultValue;
        }
        String sub = rtn.substring(1, rtn.length() - 1);
        String[] spl = sub.split(", ");
        Set<String> returns = new HashSet<>();
        for (String t : spl) {
            returns.add(t.replace(COMMA_REPLACEMENT, ", "));
        }
        return returns;
    }

    public static boolean contains(Context context, String name) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_CONTAIN, name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return false;
        } else {
            return Boolean.parseBoolean(rtn);
        }
    }

    public static void remove(Context context, String name) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = KWCrossProcessUtils.buildUri(context, TYPE_LONG, name);
        cr.delete(uri, null, null);
    }

    public static void clear(Context context) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(KWCrossProcessUtils.CONTENT + KWCrossProcessUtils.getAuthority(context) + SEPARATOR + TYPE_CLEAN);
        cr.delete(uri, null, null);
    }

    public static Map<String, ?> getAll(Context context) {
        checkContext(context);
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(KWCrossProcessUtils.CONTENT + KWCrossProcessUtils.getAuthority(context) + SEPARATOR + TYPE_GET_ALL);
        Cursor cursor = cr.query(uri, null, null, null, null);
        HashMap resultMap = new HashMap();
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(CURSOR_COLUMN_NAME);
            int typeIndex = cursor.getColumnIndex(CURSOR_COLUMN_TYPE);
            int valueIndex = cursor.getColumnIndex(CURSOR_COLUMN_VALUE);
            do {
                String key = cursor.getString(nameIndex);
                String type = cursor.getString(typeIndex);
                Object value = null;
                if (type.equalsIgnoreCase(TYPE_STRING)) {
                    value = cursor.getString(valueIndex);
                    if (((String) value).contains(COMMA_REPLACEMENT)) {
                        String str = (String) value;
                        if (str.matches("\\[.*\\]")) {
                            String sub = str.substring(1, str.length() - 1);
                            String[] spl = sub.split(", ");
                            Set<String> returns = new HashSet<>();
                            for (String t : spl) {
                                returns.add(t.replace(COMMA_REPLACEMENT, ", "));
                            }
                            value = returns;
                        }
                    }
                } else if (type.equalsIgnoreCase(TYPE_BOOLEAN)) {
                    value = cursor.getString(valueIndex);
                } else if (type.equalsIgnoreCase(TYPE_INT)) {
                    value = cursor.getInt(valueIndex);
                } else if (type.equalsIgnoreCase(TYPE_LONG)) {
                    value = cursor.getLong(valueIndex);
                } else if (type.equalsIgnoreCase(TYPE_FLOAT)) {
                    value = cursor.getFloat(valueIndex);
                } else if (type.equalsIgnoreCase(TYPE_STRING_SET)) {
                    value = cursor.getString(valueIndex);
                }
                resultMap.put(key, value);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return resultMap;
    }


}