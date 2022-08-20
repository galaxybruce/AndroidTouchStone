package com.galaxybruce.component.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;

import com.blankj.utilcode.util.UtilsTransActivity;

import java.io.Serializable;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
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
    * AppActivityCallbackUtil.openActivity(mActivity,
    *     object: AppActivityCallbackUtil.AppActivityResultCallbackWithIntent {
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
    * @param callback {@link AppActivityResultCallbackWithIntent}
    */
   public static void openActivity(Activity activity,
                                             @NonNull final AppActivityResultCallbackWithIntent callback) {
      UtilsTransActivity.start(activity, new CallbackActivityImpl() {
         @Override
         public void onCreated(@NonNull UtilsTransActivity transActivity, @Nullable Bundle savedInstanceState) {
            super.onCreated(transActivity, savedInstanceState);
            ActivityResultLauncher<Intent> launcher =
                    transActivity.registerForActivityResult(
                              new ActivityResultContracts.StartActivityForResult(),
                              new ActivityResultCallback<ActivityResult>() {
                                    @Override
                                    public void onActivityResult(ActivityResult result) {
                                       transActivity.finish();
                                       callback.onActivityResult(result);
                                    }
                             });
            Intent intent = callback.createIntent(transActivity);
            launcher.launch(intent);
         }
      });
   }

   /**
    * 通过SAF打开文件
    * [从共享存储空间访问文档和其他文件](https://developer.android.com/training/data-storage/shared/documents-files?hl=zh-cn#create-file)
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
                                    @NonNull final AppActivityResultCallback<Uri> callback) {
      UtilsTransActivity.start(activity, new CallbackActivityImpl() {
         @Override
         public void onCreated(@NonNull UtilsTransActivity transActivity, @Nullable Bundle savedInstanceState) {
            super.onCreated(transActivity, savedInstanceState);
            ActivityResultLauncher<String[]> launcher =
                    transActivity.registerForActivityResult(
                            new ActivityResultContracts.OpenDocument(),
                            new AppActivityResultCallbackWrapper<Uri>(callback) {
                               @Override
                               public void onActivityResult(Uri result) {
                                  transActivity.finish();
                                  super.onActivityResult(result);
                               }
                            });
            launcher.launch(mineTypes);
         }
      });
   }

   /**
    * 通过SAF在sdcard上创建文件
    * [从共享存储空间访问文档和其他文件](https://developer.android.com/training/data-storage/shared/documents-files?hl=zh-cn#create-file)
    *
    * 获取到文件的Uri后，可以根据Uri查询文件相关信息，并且可以向文件写数据
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
    * @param fileName 文件名称，如"invoice.pdf"
    * @param callback
    */
   public static void createDocument(Activity activity,
                                   String fileName,
                                   @NonNull final AppActivityResultCallback<Uri> callback) {
      UtilsTransActivity.start(activity, new CallbackActivityImpl() {
         @Override
         public void onCreated(@NonNull UtilsTransActivity transActivity, @Nullable Bundle savedInstanceState) {
            ActivityResultLauncher<String> launcher =
                    transActivity.registerForActivityResult(
                            new ActivityResultContracts.CreateDocument(),
                            new AppActivityResultCallbackWrapper<Uri>(callback) {
                               @Override
                               public void onActivityResult(Uri result) {
                                  transActivity.finish();
                                  super.onActivityResult(result);
                               }
                            });
            launcher.launch(fileName);
         }
      });
   }

   /**
    * 自定义继承ActivityResultCallback主要是为了实现Serializable，
    * 因为UtilsTransActivity中传递的CallbackActivityImpl是Serializable，
    * 要求内部包含的对象也要实现Serializable
    * @param <O>
    */
   public interface AppActivityResultCallback<O> extends ActivityResultCallback<O>, Serializable {

   }

   public static class AppActivityResultCallbackWrapper<O> implements AppActivityResultCallback<O>, Serializable {
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
    * 可自定义打开页面的Intent
    * 是 {@link #openActivity(Activity, AppActivityResultCallbackWithIntent)} 的参数
    */
   public interface AppActivityResultCallbackWithIntent extends AppActivityResultCallback<ActivityResult> {
      @NonNull Intent createIntent(@NonNull Activity activity);
   }

   private static class CallbackActivityImpl extends UtilsTransActivity.TransActivityDelegate {
      @Override
      public boolean dispatchTouchEvent(@NonNull UtilsTransActivity activity, MotionEvent ev) {
         activity.finish();
         return true;
      }

      @Override
      public void onActivityResult(@NonNull UtilsTransActivity activity, int requestCode, int resultCode, Intent data) {
         // 这里不能finish,否则AppActivityResultCallback不会执行
//         activity.finish();
      }
   }
}
