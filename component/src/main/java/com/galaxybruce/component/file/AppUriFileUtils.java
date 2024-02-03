/*
 * Copyright (C) 2007-2008 OpenIntents.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.galaxybruce.component.file;

import android.app.Activity;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;

import com.blankj.utilcode.util.UriUtils;
import com.blankj.utilcode.util.Utils;
import com.blankj.utilcode.util.UtilsTransActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @version 2009-07-03
 * @author Peli
 * @version 2013-12-11
 * @author paulburke (ipaulpro)
 */
public class AppUriFileUtils {
    private AppUriFileUtils() {} //private constructor to enforce Singleton pattern

    /** TAG for log messages. */
    static final String TAG = "FileUtils";
    private static final boolean DEBUG = false; // Set to true to enable logging

    public static final String MIME_TYPE_AUDIO = "audio/*";
    public static final String MIME_TYPE_TEXT = "text/*";
    public static final String MIME_TYPE_IMAGE = "image/*";
    public static final String MIME_TYPE_VIDEO = "video/*";
    public static final String MIME_TYPE_APP = "application/*";

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     *         null if uri was null.
     */
    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    /**
     * @return Whether the URI is a local one.
     */
    public static boolean isLocal(String url) {
        if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
            return true;
        }
        return false;
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author paulburke
     */
    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * Convert File into Uri.
     *
     * @param file
     * @return uri
     */
    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    /**
     * @return The MIME type for the given file.
     */
    public static String getMimeType(File file) {

        String extension = getExtension(file.getName());

        if (extension.length() > 0)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));

        return "application/octet-stream";
    }

    /**
     * @return The MIME type for the give Uri.
     */
    public static String getMimeType(Context context, Uri uri) {
        String filePath = getPath(context, uri);
        if(TextUtils.isEmpty(filePath)) return null;

        File file = new File(filePath);
        return getMimeType(file);
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG)
                    DatabaseUtils.dumpCursor(cursor);

                final int column_index = cursor.getColumnIndex(column);
                if(column_index > -1) {
                    return cursor.getString(column_index);
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @see #isLocal(String)
     * @see #getFile(Context, Uri)
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {
        File file = UriUtils.uri2File(uri);
        if(file != null) {
            return file.getAbsolutePath();
        }
        return null;
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     *         Uri is unsupported or pointed to a remote resource.
     * @see #getPath(Context, Uri)
     * @author paulburke
     */
    public static File getFile(Context context, Uri uri) {
        return UriUtils.uri2File(uri);
    }

    /**
     * 判断公有目中文件是否存在：Android10填坑适配指南，实际经验代码，拒绝翻译 https://www.jianshu.com/p/d79c2ee86b2a
     * @param context
     * @param uri
     * @return
     */
    public static boolean isContentUriExists(Context context, Uri uri) {
        if (null == context || uri == null) {
            return false;
        }
        ContentResolver cr = context.getContentResolver();
        try {
            AssetFileDescriptor afd = cr.openAssetFileDescriptor(uri, "r");
            if (null == afd || afd.getLength() == 0) {
                return false;
            } else {
                try {
                    afd.close();
                } catch (IOException e) {
                }
                return true;
            }
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void deleteContentUri(Activity activity, Uri uri, DeleteMediaActivityImpl.DeleteMediaCallback deleteMediaCallback) {
        AppUriFileUtils.DeleteMediaActivityImpl.deleteMedia(activity, uri, deleteMediaCallback);
    }

    /**
     * 删除文件
     * @param activity
     * @param uri
     * @param
     * @return
     */
    private static boolean deleteContentUri(Activity activity, Uri uri, int permissionRequestCode) {
        int result = 0;
        if(uri != null) {
            try {
                result = activity.getContentResolver().delete(uri, null, null);
            } catch (Exception e) {
                try {
                    if(permissionRequestCode > 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            activity.startIntentSenderForResult(
                                    ((RecoverableSecurityException)e).getUserAction().getActionIntent().getIntentSender(),
                                    permissionRequestCode, null, 0, 0, 0);
                        }
                    }
                } catch (IntentSender.SendIntentException e2) {
//                    LogUtil.log("startIntentSender fail");
                }
            }
        }
        return result > 0;
    }

    /**
     * 根据图片路径反查id
     * @param context
     * @param path 图片绝对路径
     * @return
     */
    public static Uri getImageContentUri(Context context, String path) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                    new String[] { path }, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(MediaStore.MediaColumns._ID);
                if(index >= 0) {
                    int id = cursor.getInt(index);
                    return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
                }
            } else {
                // 如果图片不在手机的共享图片数据库，就先把它插入。
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    if (new File(path).exists()) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.DATA, path);
                        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 根据视频路径反查id
     * @param context
     * @param path 图片绝对路径
     * @return
     */
    public static Uri getVideoContentUri(Context context, String path) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[] { MediaStore.Video.Media._ID }, MediaStore.Video.Media.DATA + "=? ",
                    new String[] { path }, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(MediaStore.MediaColumns._ID);
                if(index >= 0) {
                    int id = cursor.getInt(index);
                    return Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
                }
            } else {
                // 如果图片不在手机的共享图片数据库，就先把它插入。
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    if (new File(path).exists()) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Video.Media.DATA, path);
                        return context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 通过uri获取公有目录下的bitmap
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        FileDescriptor fileDescriptor = null;
        Bitmap bitmap = null;
        try {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            if (parcelFileDescriptor != null && parcelFileDescriptor.getFileDescriptor() != null) {
                fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                //转换uri为bitmap类型
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * Attempt to retrieve the thumbnail of given File from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param file
     * @return
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, File file) {
        return getThumbnail(context, getUri(file), getMimeType(file));
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @return
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, Uri uri) {
        return getThumbnail(context, uri, getMimeType(context, uri));
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @param mimeType
     * @return
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, Uri uri, String mimeType) {
        if (DEBUG)
            Log.d(TAG, "Attempting to get thumbnail");

//        if (!isMediaUri(uri)) {
//            Log.e(TAG, "You can only retrieve thumbnails for images and videos.");
//            return null;
//        }

        Bitmap bm = null;
        if (uri != null && !TextUtils.isEmpty(mimeType)) {
            final ContentResolver resolver = context.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    final int id = cursor.getInt(0);
                    if (DEBUG)
                        Log.d(TAG, "Got thumb ID: " + id);

                    if (mimeType.contains("video")) {
                        bm = MediaStore.Video.Thumbnails.getThumbnail(
                                resolver,
                                id,
                                MediaStore.Video.Thumbnails.MINI_KIND,
                                null);
                    }
                    else if (mimeType.contains("image")) {
                        bm = MediaStore.Images.Thumbnails.getThumbnail(
                                resolver,
                                id,
                                MediaStore.Images.Thumbnails.MINI_KIND,
                                null);
                    }
                }
            } catch (Exception e) {
                if (DEBUG)
                    Log.e(TAG, "getThumbnail", e);
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return bm;
    }

    public static String getThumbnailPath(Context context, Uri uri) {
        return getThumbnailPath(context, uri, getMimeType(context, uri));
    }

    public static String getThumbnailPath(Context context, Uri uri, String mimeType) {
        if (DEBUG)
            Log.d(TAG, "Attempting to get thumbnail");

        String path = null;
        if (uri != null && !TextUtils.isEmpty(mimeType)) {
            final ContentResolver resolver = context.getContentResolver();
            Cursor cursor = null;
            Cursor thumbnailCursor = null;
            try {
                cursor = resolver.query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    final int id = cursor.getInt(0);
                    if (mimeType.contains("video")) {
                        thumbnailCursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(
                                resolver, id, MediaStore.Video.Thumbnails.MINI_KIND, null);
                        if(thumbnailCursor.moveToFirst()) {
                            int index = thumbnailCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA);
                            if(index >= 0) {
                                path = thumbnailCursor.getString(index);
                            }
                        }
                    }
                    else if (mimeType.contains("image")) {
                        thumbnailCursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(
                                resolver, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                        if(thumbnailCursor.moveToFirst()) {
                            int index = thumbnailCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                            if(index >= 0) {
                                path = thumbnailCursor.getString(index);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                if (DEBUG)
                    Log.e(TAG, "getThumbnail", e);
            } finally {
                if (cursor != null)
                    cursor.close();
                if (thumbnailCursor != null)
                    thumbnailCursor.close();
            }
        }
        return path;
    }

    public static int[] getBitmapPixel(Context context, Uri uri) {

        int[] pixel = new int[2];

        if (uri == null || !isMediaUri(uri)) {
            return pixel;
        }

        final ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, new String[]{MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                pixel[0] = cursor.getInt(0);
                pixel[1] = cursor.getInt(1);
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return pixel;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static final class DeleteMediaActivityImpl extends UtilsTransActivity.TransActivityDelegate {

        public static interface DeleteMediaCallback {
            void onMediaDeleted(boolean success);
        }
        private static DeleteMediaCallback mediaCallback;
        private static final String MEDIA_URI                = "MEDIA_URI";

        public static void deleteMedia(final Activity activity, final Uri uri, final DeleteMediaCallback mediaCallback) {
            DeleteMediaActivityImpl.mediaCallback = mediaCallback;

            UtilsTransActivity.start(activity, new Utils.Consumer<Intent>() {
                @Override
                public void accept(Intent data) {
                    data.putExtra(MEDIA_URI, uri);
                }
            }, new DeleteMediaActivityImpl());
        }

        @Override
        public void onCreated(final UtilsTransActivity activity, @Nullable Bundle savedInstanceState) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            Uri mediaUri = activity.getIntent().getParcelableExtra(MEDIA_URI);
            AppUriFileUtils.deleteContentUri(activity, mediaUri, 100);
        }

        @Override
        public void onActivityResult(@NotNull UtilsTransActivity activity, int requestCode, int resultCode, Intent data) {
            boolean success = false;
            if(requestCode == 100 && resultCode == UtilsTransActivity.RESULT_OK) {
                Uri mediaUri = activity.getIntent().getParcelableExtra(MEDIA_URI);
                success = AppUriFileUtils.deleteContentUri(activity, mediaUri, 0);
            }
            if(DeleteMediaActivityImpl.mediaCallback != null) {
                DeleteMediaActivityImpl.mediaCallback.onMediaDeleted(success);
                DeleteMediaActivityImpl.mediaCallback = null;
            }
            activity.finish();
        }
    }
}
