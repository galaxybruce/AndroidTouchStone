package com.galaxybruce.component.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

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

   public static Uri getUriForFile(@NonNull Context context, @NonNull File file) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
         return FileProvider.getUriForFile(context,
                 context.getPackageName() + AppFileProvider.PROVIDER,
                 file);
      } else {
         return Uri.fromFile(file);
      }
   }
}
