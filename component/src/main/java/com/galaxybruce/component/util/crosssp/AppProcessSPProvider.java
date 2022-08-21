package com.galaxybruce.component.util.crosssp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.util.Map;
import java.util.Set;

import androidx.annotation.Nullable;

import static com.galaxybruce.component.util.crosssp.AppProcessUtils.CURSOR_COLUMN_NAME;
import static com.galaxybruce.component.util.crosssp.AppProcessUtils.CURSOR_COLUMN_TYPE;
import static com.galaxybruce.component.util.crosssp.AppProcessUtils.CURSOR_COLUMN_VALUE;
import static com.galaxybruce.component.util.crosssp.AppProcessUtils.SEPARATOR;
import static com.galaxybruce.component.util.crosssp.AppProcessUtils.TYPE_BOOLEAN;
import static com.galaxybruce.component.util.crosssp.AppProcessUtils.TYPE_CLEAN;
import static com.galaxybruce.component.util.crosssp.AppProcessUtils.TYPE_CONTAIN;
import static com.galaxybruce.component.util.crosssp.AppProcessUtils.TYPE_FLOAT;
import static com.galaxybruce.component.util.crosssp.AppProcessUtils.TYPE_GET_ALL;
import static com.galaxybruce.component.util.crosssp.AppProcessUtils.TYPE_INT;
import static com.galaxybruce.component.util.crosssp.AppProcessUtils.TYPE_LONG;
import static com.galaxybruce.component.util.crosssp.AppProcessUtils.TYPE_STRING;
import static com.galaxybruce.component.util.crosssp.AppProcessUtils.VALUE;

/**
 * @date 2017/12/18 17:34
 * @author bruce.zhang
 * @description 跨进程保存数据 ContentProvider
 * 注意authority一定要是${applicationId}.sphelper
 * <p>
 * modification history:
 */
public class AppProcessSPProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String[] path = uri.getPath().split(SEPARATOR);
        String type = path[1];
        if (type.equals(TYPE_GET_ALL)) {
            Map<String, ?> all = AppProcessSPHelperImpl.getAll(getContext());
            if (all == null) {
                return null;
            }
            MatrixCursor cursor = new MatrixCursor(new String[]{CURSOR_COLUMN_NAME, CURSOR_COLUMN_TYPE, CURSOR_COLUMN_VALUE});
            Set<String> keySet = all.keySet();
            for (String key : keySet) {
                Object[] rows = new Object[3];
                rows[0] = key;
                rows[2] = all.get(key);
                if (rows[2] instanceof Boolean) {
                    rows[1] = TYPE_BOOLEAN;
                } else if (rows[2] instanceof String) {
                    rows[1] = TYPE_STRING;
                } else if (rows[2] instanceof Integer) {
                    rows[1] = TYPE_INT;
                } else if (rows[2] instanceof Long) {
                    rows[1] = TYPE_LONG;
                } else if (rows[2] instanceof Float) {
                    rows[1] = TYPE_FLOAT;
                }
                cursor.addRow(rows);
            }
            return cursor;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // 用这个来取数值
        String[] path = uri.getPath().split(SEPARATOR);
        String type = path[1];
        String key = path[2];
        if (type.equals(TYPE_CONTAIN)) {
            return AppProcessSPHelperImpl.contains(getContext(), key) + "";
        }
        return AppProcessSPHelperImpl.get(getContext(), key, type) + "";
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String[] path = uri.getPath().split(SEPARATOR);
        String type = path[1];
        String key = path[2];
        Object obj = (Object) values.get(VALUE);
        if (obj != null) {
            AppProcessSPHelperImpl.save(getContext(), key, obj);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String[] path = uri.getPath().split(SEPARATOR);
        String type = path[1];
        if (type.equals(TYPE_CLEAN)) {
            AppProcessSPHelperImpl.clear(getContext());
            return 0;
        }
        String key = path[2];
        if (AppProcessSPHelperImpl.contains(getContext(), key)) {
            AppProcessSPHelperImpl.remove(getContext(), key);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        insert(uri, values);
        return 0;
    }
}