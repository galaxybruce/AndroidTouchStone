package com.galaxybruce.component.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;

import java.io.Serializable;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author bruce.zhang
 * @date 2022/8/18 23:28
 * @description
 *
 * {@link Activity#startActivityForResult}的简洁写法
 *
 * 1. {@link #openActivity}方法可以通过自定义Intent打开任何页面
 * 2. 其他方法是封装的具体场景，比如打开SAF，打开相机、打开相册等
 * 2.1 SAF打开文档：openDocument
 * 2.2 SAF创建文档：createDocument
 *
 * 参考文章：
 * [获取 activity 的结果](https://developer.android.com/training/basics/intents/result?hl=zh-cn)
 * [优雅地封装 Activity Result API，完美地替代 startActivityForResult()](https://juejin.cn/post/6987575150283587592)
 *
 * <p>
 * modification history:
 */
public class AppActivityResultUtil {

   /**
    * 打开 activity 通用回调方法，适用场景：
    * 1. 由调用方自定义打开activity的Intent
    * 2. 返回值是 {@link ActivityResult}，调用方从中Intent中解析数据
    *
    * 使用方式：
    * AppActivityResultUtil.openActivity(mActivity,
    *     object: AppActivityResultUtil.AppActivityResultCallbackInputIntent {
    *     override fun onActivityResult(result: ActivityResult?) {
    *         result?.let {
    *             AppLogUtils.i("uri: " + result.toString())
    *         }
    *     }
    *     override fun createIntent(activity: Activity): Intent {
    *         return Intent(Intent.ACTION_OPEN_DOCUMENT)
    *             .putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf"))
    *             .setType("*\/*")
    *     }
    * })
    *
    * @param activity
    * @param callback {@link AppActivityResultCallbackInputIntent}
    */
   public static void openActivity(Activity activity,
                                   @NonNull final AppActivityResultCallbackInputIntent callback) {
      AppUtilsTransActivity.start(activity, new CallbackActivityImpl() {
         @Override
         public void onCreated(@NonNull AppUtilsTransActivity activity, @Nullable Bundle savedInstanceState) {
            super.onCreated(activity, savedInstanceState);
            ActivityResultLauncher<Intent> launcher =
                    activity.registerForActivityResult(
                            new ActivityResultContracts.StartActivityForResult(),
                            new ActivityResultCallback<ActivityResult>() {
                               @Override
                               public void onActivityResult(ActivityResult result) {
                                  activity.finish();
                                  callback.onActivityResult(result);
                               }
                            });
            Intent intent = callback.createIntent(activity);
            launcher.launch(intent);
         }
      });
   }

   /**
    * 打开 activity 通用回调方法，适用场景：
    * 1. ActivityResultContract由调用方传入（一般是系统预定义的：new ActivityResultContracts.xxx()）
    * 2. 返回值由泛型决定
    *
    * @param activity
    * @param input
    * @param activityResultContract
    * @param activityResultCallback
    * @param <I>
    * @param <O>
    */
   public static <I, O> void openActivity(Activity activity, I input,
                                          @NonNull final ActivityResultContract<I, O> activityResultContract,
                                          @NonNull final ActivityResultCallback<O> activityResultCallback) {
      AppUtilsTransActivity.start(activity, new CallbackActivityImpl() {
         @Override
         public void onCreated(@NonNull AppUtilsTransActivity transActivity, @Nullable Bundle savedInstanceState) {
            super.onCreated(transActivity, savedInstanceState);
            ActivityResultLauncher<I> launcher =
                    transActivity.registerForActivityResult(
                            activityResultContract,
                            new AppActivityResultCallbackWrapper<O>(activityResultCallback) {
                               @Override
                               public void onActivityResult(O result) {
                                  transActivity.finish();
                                  super.onActivityResult(result);
                               }
                            });
            launcher.launch(input);
         }
      });
   }

   /**
    * 通过SAF打开文件
    * [从共享存储空间访问文档和其他文件](https://developer.android.com/training/data-storage/shared/documents-files?hl=zh-cn#create-file)
    * 使用方式：
    * AppActivityResultUtil.openDocument(mActivity, arrayOf("application/pdf")) { uri ->
    *     uri?.let {
    *         AppLogUtils.i("uri: " + uri.toString())
    *     }
    * }
    *
    * 获取到文件的Uri后，可以查询文件相关信息，并且可以读取文件内容
    * if (uri != null) {
    *   Cursor cursor = getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
    *   if (cursor.moveToFirst()) {
    *     String name = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
    *     toast(name);
    *   }
    *   cursor.close();
    * }
    *
    * @param activity
    * @param mineTypes new String[]{"application/*"}
    * @param callback
    */
   public static void openDocument(Activity activity,
                                   String[] mineTypes,
                                   @NonNull final ActivityResultCallback<Uri> callback) {
      openActivity(activity, mineTypes, new ActivityResultContracts.OpenDocument(), callback);
   }

   /**
    * 通过SAF在sdcard上创建文件
    * [从共享存储空间访问文档和其他文件](https://developer.android.com/training/data-storage/shared/documents-files?hl=zh-cn#create-file)
    *
    * 使用方式
    * AppActivityResultUtil.createDocument(mActivity, "test.pdf") { uri ->
    *    // 获取到文件的Uri后，可以根据Uri查询文件相关信息，并且可以向文件写数据
    *     uri?.let {
    *         AppLogUtils.i("uri: " + uri.toString())
    *         val cursor: Cursor? = mActivity.contentResolver.query(uri,
    *             null, null, null, null);
    *         if (cursor?.moveToFirst() == true) {
    *             val name: String = cursor?.getString(cursor.getColumnIndexOrThrow(
    *                 OpenableColumns.DISPLAY_NAME))
    *         }
    *         cursor?.close();
    *     }
    * }
    *
    * @param activity
    * @param fileName 文件名称，如"invoice.pdf"
    * @param callback
    */
   public static void createDocument(Activity activity,
                                     String fileName,
                                     @NonNull final ActivityResultCallback<Uri> callback) {
      openActivity(activity, fileName, new ActivityResultContracts.CreateDocument(), callback);
   }

   public static class AppActivityResultCallbackWrapper<O> implements ActivityResultCallback<O>, Serializable {
      private final ActivityResultCallback<O> activityResultCallback;

      AppActivityResultCallbackWrapper(ActivityResultCallback<O> activityResultCallback) {
         this.activityResultCallback = activityResultCallback;
      }

      @Override
      public void onActivityResult(O result) {
         this.activityResultCallback.onActivityResult(result);
      }
   }

   /**
    * 自定义打开页面的Intent
    * 是 {@link #openActivity(Activity, AppActivityResultCallbackInputIntent)} 方法的参数
    */
   public interface AppActivityResultCallbackInputIntent extends ActivityResultCallback<ActivityResult> {

      @NonNull Intent createIntent(@NonNull Activity activity);

   }

   private static class CallbackActivityImpl extends AppUtilsTransActivity.TransActivityDelegate {
      @Override
      public boolean dispatchTouchEvent(@NonNull AppUtilsTransActivity activity, MotionEvent ev) {
         activity.finish();
         return true;
      }

      @Override
      public void onActivityResult(@NonNull AppUtilsTransActivity activity, int requestCode, int resultCode, Intent data) {
         // 这里不能finish,否则AppActivityResultCallback不会执行
//         activity.finish();
      }
   }
}
