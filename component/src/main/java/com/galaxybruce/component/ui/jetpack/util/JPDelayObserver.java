package com.galaxybruce.component.ui.jetpack.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;


/**
 * @author bruce.zhang
 * @date 2020/9/29 19:04
 * @description LiveData延迟触发订阅Observer
 * <p>
 * modification history:
 */
public abstract class JPDelayObserver<T> implements Observer<T>, DefaultLifecycleObserver {

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private long delayMillis;

    public JPDelayObserver(@NonNull Lifecycle lifecycle, long delayMillis) {
        this.delayMillis = delayMillis;
        lifecycle.addObserver(this);
    }

    @Override
    public final void onChanged(T o) {
        if(delayMillis <= 0) {
            _onChanged(o);
            return;
        }
        if(mainHandler != null) {
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _onChanged(o);
                }
            }, delayMillis);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        if(mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }
    }

    protected abstract void _onChanged(T o);
}
