package com.galaxybruce.component.util.rx;

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
 * 使用方式：
 * 案例1：
 * Observable.just(true)
 *    .subscribeOn(Schedulers.io())
 *    .observeOn(AndroidSchedulers.mainThread())
 *    .`as`(RxLifecycleUtils.bindLifecycle(this))
 *    .subscribe {
 *    }
 *
 * 案例2：
 * Observable.interval(0, 10, TimeUnit.MILLISECONDS)
 *    .take(10) // 执行10次
 *    .subscribeOn(Schedulers.io())
 *    .observeOn(AndroidSchedulers.mainThread())
 *    .`as`(RxLifecycleUtils.bindLifecycle(lifecycleOwner))
 *    .subscribe(object : Observer<Long> {
 *        override fun onSubscribe(d: Disposable) {
 *        }
 *
 *        override fun onNext(value: Long) {
 *
 *        }
 *
 *        override fun onError(e: Throwable) {}
 *
 *        override fun onComplete() {
 *        }
 *   })
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