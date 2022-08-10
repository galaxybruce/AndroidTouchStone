package com.galaxybruce.component.util.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @date 2022/2/18 23:17
 * @author bruce.zhang
 * @description rxjava 重试
 *
 * .compose(this.<LiveEntity<String>>handleEverythingResult(false))
 * .subscribeOn(Schedulers.io())
 * .observeOn(Schedulers.io())
 * .retryWhen(new RxRetryDelay(3, 1000))
 * .subscribe(...)
 *
 * <p>
 * modification history:
 */
public class RxRetryDelay implements Function<Observable<? extends Throwable>, Observable<?>> {

    private int maxRetries = 3;//最大出错重试次数
    private int retryDelayMillis = 1000;//重试间隔时间
    private int retryCount = 0;//当前出错重试次数

    public RxRetryDelay(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
                .flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
                    if (++retryCount <= maxRetries) {
                        return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS, Schedulers.trampoline());
                    }
                    return Observable.error(throwable);
                });
    }
}
