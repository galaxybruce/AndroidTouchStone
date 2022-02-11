package com.galaxybruce.component.util;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

/**
 * @author bruce.zhang
 * @date 2022/1/27 16:30
 * @description rx生命周期相关
 *
 * 可以给view实现ViewLifecycleOwner：
 * [一个简单非侵入式的AutoDispose](https://juejin.cn/post/6872337518163918861)
 *
 * <p>
 * modification history:
 */
public class RxLifecycleUtils {

    public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner lifecycleOwner) {
        return AutoDispose.autoDisposable(
                AndroidLifecycleScopeProvider.from(lifecycleOwner)
        );
    }

    public static <T> AutoDisposeConverter<T> bindUntilEvent(LifecycleOwner lifecycleOwner
            , Lifecycle.Event untilEvent) {
        return AutoDispose.autoDisposable(
                AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent)
        );
    }
}