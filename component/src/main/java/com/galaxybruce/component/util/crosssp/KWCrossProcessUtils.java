package com.galaxybruce.component.util.crosssp;


import android.content.Context;
import android.net.Uri;

/**
 * @date 2017/12/18 17:34
 * @author bruce.zhang
 * @description 跨进程保存数据utils
 * <p>
 * modification history:
 */
public class KWCrossProcessUtils {

    public static final String CONTENT="content://";
    public static final String AUTHORITY_SUFIX = ".sphelper";
    public static final String SEPARATOR= "/";
    public static final String TYPE_STRING_SET="string_set";
    public static final String TYPE_STRING="string";
    public static final String TYPE_INT="int";
    public static final String TYPE_LONG="long";
    public static final String TYPE_FLOAT="float";
    public static final String TYPE_BOOLEAN="boolean";
    public static final String VALUE= "value";

    public static final String NULL_STRING= "null";
    public static final String TYPE_CONTAIN="contain";
    public static final String TYPE_CLEAN="clean";
    public static final String TYPE_GET_ALL="get_all";

    public static final String CURSOR_COLUMN_NAME = "cursor_name";
    public static final String CURSOR_COLUMN_TYPE = "cursor_type";
    public static final String CURSOR_COLUMN_VALUE = "cursor_value";

    /**
     * 构建uri
     * @param type 保存值的数据类型
     * @param name 保存值的key
     */
    public static Uri buildUri(Context context, String type, String name) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CONTENT)
                .append(KWCrossProcessUtils.getAuthority(context))
                .append(SEPARATOR)
                .append(type)
                .append(SEPARATOR)
                .append(name);

        return Uri.parse(stringBuilder.toString());
    }

    public static String getAuthority(Context context) {
        return context.getPackageName() + AUTHORITY_SUFIX;
    }
}
