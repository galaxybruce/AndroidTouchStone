package com.galaxybruce.component.util.bitmap;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import com.galaxybruce.component.file.AppFilePathManager;
import com.galaxybruce.component.file.AppFileUtils;
import com.galaxybruce.component.file.AppUriFileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class AppBitmapUtils {

    public static void scanMedia(Context context, String path) {
        AppMediaScanner.scanMedia(context, path);
    }

    public static String saveBitmapAndScan2Media(Context context, Bitmap bitmap) {
        return saveBitmapAndScan2Media(context, bitmap, ".jpg");
    }

    public static String saveGifAndScan2Media(Context context, Bitmap bitmap) {
        return saveBitmapAndScan2Media(context, bitmap, ".gif");
    }

    /**
     * 保存图片并扫描进相册
     * @param context
     * @param bitmap
     * @param suffix
     * @return
     */
    private static String saveBitmapAndScan2Media(Context context, Bitmap bitmap, String suffix) {
        if (bitmap == null) {
            return null;
        }

        String mimeType = ".gif".equals(suffix) ? "image/gif" : "image/jpeg";

        // 参考文章 https://blog.csdn.net/guanyingcao/article/details/103634339
        String filePath = null;
        Uri outPutUri = null;
        boolean success;
        // Android 10创建uri的方式和使用uri加载图片的方式在10以下的手机是同样适用的
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android10及以上，如果你只需要保存媒体文件到媒体库，不需要申请权限。
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            int count = 0;
            // 某些机型 第一次获取时 返回null
            while (count++ < 10 && outPutUri == null) {
                outPutUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }
            try{
                filePath = AppUriFileUtils.getPath(context, outPutUri);
            }catch (Exception e){}

            success = saveBitmap(context, bitmap, outPutUri);
        } else {
            filePath = AppFilePathManager.getPictureFilePath(context, null, suffix);
            outPutUri = Uri.fromFile(new File(filePath));

            success = saveBitmap(context, bitmap, outPutUri);
            scanMedia(context, filePath);
        }

        return success ? filePath : null;
    }

    public static String saveBitmapAndScan2Media(Context context, File src) {
        return saveBitmapAndScan2Media(context, src, ".jpg");
    }

    public static String saveGifAndScan2Media(Context context, File src) {
        return saveBitmapAndScan2Media(context, src, ".gif");
    }

    /**
     * 保存图片并扫描进相册
     * @param context
     * @param src
     * @return
     */
    private static String saveBitmapAndScan2Media(Context context, File src, String suffix) {
        if (src == null) {
            return null;
        }

        String mimeType = ".gif".equals(suffix) ? "image/gif" : "image/jpeg";

        // 参考文章 https://blog.csdn.net/guanyingcao/article/details/103634339
        String filePath = null;
        Uri outPutUri = null;
        // Android 10创建uri的方式和使用uri加载图片的方式在10以下的手机是同样适用的
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            int count = 0;
            // 某些机型 第一次获取时 返回null
            while (count++ < 10 && outPutUri == null) {
                outPutUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }

            try {
                filePath = AppUriFileUtils.getPath(context, outPutUri);
                AppFileUtils.copyFile(context, src, outPutUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            filePath = AppFilePathManager.getPictureFilePath(context, null, suffix);
            outPutUri = Uri.fromFile(new File(filePath));

            try {
                AppFileUtils.copyFile(context, src, outPutUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            scanMedia(context, filePath);
        }

        return filePath;
    }

    /**
     * 保存视频并扫描进相册
     * @param context
     * @param src
     * @return
     */
    public static String saveVideoAndScan2Media(Context context, File src) {
        if (src == null) {
            return null;
        }

        // 参考文章 https://blog.csdn.net/guanyingcao/article/details/103634339
        String filePath = null;
        Uri outPutUri = null;
        // Android 10创建uri的方式和使用uri加载图片的方式在10以下的手机是同样适用的
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            values.put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES);
            int count = 0;
            // 某些机型 第一次获取时 返回null
            while (count++ < 10 && outPutUri == null) {
                outPutUri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            }

            try {
                filePath = AppUriFileUtils.getPath(context, outPutUri);
                AppFileUtils.copyFile(context, src, outPutUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            filePath = AppFilePathManager.getVideoFilePath(context, null, ".mp4");
            outPutUri = Uri.fromFile(new File(filePath));

            try {
                AppFileUtils.copyFile(context, src, outPutUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            scanMedia(context, filePath);
        }

        return filePath;
    }

    /**
     * 保存bitmap到私有目录
     * @param context
     * @param bitmap
     * @return
     */
    public static String saveBitmapToAppDir(Context context, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        String filePath = AppFilePathManager.getAppPictureFilePath(context, null, ".jpg");
        Uri outPutUri = Uri.fromFile(new File(filePath));
        saveBitmap(context, bitmap, outPutUri);
        return filePath;
    }


    /**
     * 图片保存到公有目录
     *
     * 参考文章：Android10填坑适配指南，实际经验代码，拒绝翻译  https://www.jianshu.com/p/d79c2ee86b2a
     * @param context
     * @param bitmap
     * @param outPutUri
     */
    public static boolean saveBitmap(Context context, Bitmap bitmap, Uri outPutUri) {
        boolean success = false;
        if(outPutUri != null) {
            OutputStream outputStream = null;
            try {
                // 这里不能使用new FileOutputStream(filePath);否则出现没有权限的错误
                outputStream = context.getContentResolver().openOutputStream(outPutUri);
                success = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (null != outputStream) {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return success;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
