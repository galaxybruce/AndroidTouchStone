package com.galaxybruce.component.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

/**
 * @date 2019-06-24 19:55
 * @author bruce.zhang
 * @description
 * <p>
 * modification history:
 */
public class AppFileProvider extends FileProvider {

   public static final String PROVIDER = ".galaxybruce.provider";

   private static String getFileProviderAuthority(Context context) {
      return context.getPackageName() + AppFileProvider.PROVIDER;
   }

   public static Uri getUriForFile(@NonNull Context context, @NonNull File file) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
         return FileProvider.getUriForFile(context,
                 getFileProviderAuthority(context),
                 file);
      } else {
         return Uri.fromFile(file);
      }
   }

   public static String parsePath(Context context, Uri uri) {
      String path = "";
      if (uri == null) {
         return path;
      }
      try {
         if (TextUtils.equals(uri.getAuthority(), getFileProviderAuthority(context))) {
            String tempPath = uri.getPath();
            if(!TextUtils.isEmpty(tempPath)) {
               if(tempPath.startsWith("/root")) {
                  path = new File(tempPath.replace("root/", "")).getAbsolutePath();
               } else if(tempPath.startsWith("/external_path")) {
                  path = new File(Environment.getExternalStorageDirectory(), tempPath.replace("/external_path", "")).getAbsolutePath();
               } else if(tempPath.startsWith("/external_files_path")) {
                  path = new File(context.getExternalFilesDir(""), tempPath.replace("/external_files_path", "")).getAbsolutePath();
               } else if(tempPath.startsWith("/external_cache_path")) {
                  path = new File(context.getExternalCacheDir(), tempPath.replace("/external_cache_path", "")).getAbsolutePath();
               } else if(tempPath.startsWith("/files_path")) {
                  path = new File(context.getFilesDir(), tempPath.replace("/files_path", "")).getAbsolutePath();
               } else if(tempPath.startsWith("/cache_path")) {
                  path = new File(context.getCacheDir(), tempPath.replace("/cache_path", "")).getAbsolutePath();
               } else {
                  path = uri.getEncodedPath();
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return path;
   }
}
