package com.galaxybruce.component.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.blankj.utilcode.util.Utils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @date 2022/8/21 09:32
 * @author bruce.zhang
 * @description 透明activity，用来处理需要借助activity才能完成的任务，比如：
 * 1. 处理ActivityResult
 * 2. 处理权限请求
 * 3. 其他一些需要activity context的任务
 *
 * <p>
 * modification history:
 */
public class AppUtilsTransActivity extends AppCompatActivity {

    private static final String CALLBACK_ID_FORMAT = "CB_%d_%d";
    private static final String EXTRA_DELEGATE = "extra_delegate";
    private static long uniqueId = 0;

    private static final Map<String, TransActivityDelegate> CALLBACK_MAP = new HashMap<>();

    private TransActivityDelegate delegate;

    public static void start(final TransActivityDelegate delegate) {
        start(null, null, delegate, AppUtilsTransActivity.class);
    }

    public static void start(final Utils.Consumer<Intent> consumer,
                             final TransActivityDelegate delegate) {
        start(null, consumer, delegate, AppUtilsTransActivity.class);
    }

    public static void start(final Activity activity,
                             final TransActivityDelegate delegate) {
        start(activity, null, delegate, AppUtilsTransActivity.class);
    }

    public static void start(final Activity activity,
                             final Utils.Consumer<Intent> consumer,
                             final TransActivityDelegate delegate) {
        start(activity, consumer, delegate, AppUtilsTransActivity.class);
    }

    protected static void start(final Activity activity,
                                final Utils.Consumer<Intent> consumer,
                                final TransActivityDelegate delegate,
                                final Class<?> cls) {
        if (delegate == null) return;
        Intent starter = new Intent(Utils.getApp(), cls);
        String callbackStr = String.format(CALLBACK_ID_FORMAT, ++uniqueId, SystemClock.currentThreadTimeMillis());
        CALLBACK_MAP.put(callbackStr, delegate);
        starter.putExtra(EXTRA_DELEGATE, callbackStr);
        if (consumer != null) {
            consumer.accept(starter);
        }
        if (activity == null) {
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Utils.getApp().startActivity(starter);
        } else {
            activity.startActivity(starter);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        try {
            String callbackId = getIntent().getStringExtra(EXTRA_DELEGATE);
            delegate = TextUtils.isEmpty(callbackId) ? null : CALLBACK_MAP.remove(callbackId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (delegate == null) {
            super.onCreate(savedInstanceState);
            finish();
            return;
        }
        delegate.onCreateBefore(this, savedInstanceState);
        super.onCreate(savedInstanceState);
        delegate.onCreated(this, savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TransActivityDelegate callback = delegate;
        if (callback == null) return;
        callback.onStarted(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TransActivityDelegate callback = delegate;
        if (callback == null) return;
        callback.onResumed(this);
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
        TransActivityDelegate callback = delegate;
        if (callback == null) return;
        callback.onPaused(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        TransActivityDelegate callback = delegate;
        if (callback == null) return;
        callback.onStopped(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TransActivityDelegate callback = delegate;
        if (callback == null) return;
        callback.onSaveInstanceState(this, outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String callbackId = getIntent().getStringExtra(EXTRA_DELEGATE);
        if(!TextUtils.isEmpty(callbackId)) {
            CALLBACK_MAP.remove(callbackId);
        }
        TransActivityDelegate callback = delegate;
        if (callback == null) return;
        callback.onDestroy(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TransActivityDelegate callback = delegate;
        if (callback == null) return;
        callback.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TransActivityDelegate callback = delegate;
        if (callback == null) return;
        callback.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        TransActivityDelegate callback = delegate;
        if (callback == null) return super.dispatchTouchEvent(ev);
        if (callback.dispatchTouchEvent(this, ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public abstract static class TransActivityDelegate {
        public void onCreateBefore(@NonNull AppUtilsTransActivity activity, @Nullable Bundle savedInstanceState) {/**/}

        public void onCreated(@NonNull AppUtilsTransActivity activity, @Nullable Bundle savedInstanceState) {/**/}

        public void onStarted(@NonNull AppUtilsTransActivity activity) {/**/}

        public void onDestroy(@NonNull AppUtilsTransActivity activity) {/**/}

        public void onResumed(@NonNull AppUtilsTransActivity activity) {/**/}

        public void onPaused(@NonNull AppUtilsTransActivity activity) {/**/}

        public void onStopped(@NonNull AppUtilsTransActivity activity) {/**/}

        public void onSaveInstanceState(@NonNull AppUtilsTransActivity activity, Bundle outState) {/**/}

        public void onRequestPermissionsResult(@NonNull AppUtilsTransActivity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {/**/}

        public void onActivityResult(@NonNull AppUtilsTransActivity activity, int requestCode, int resultCode, Intent data) {/**/}

        public boolean dispatchTouchEvent(@NonNull AppUtilsTransActivity activity, MotionEvent ev) {
            return false;
        }
    }
}
