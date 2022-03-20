package com.galaxybruce.component.util.rx;

import io.reactivex.FlowableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author bruce.zhang
 * @date 2022/3/20 14:53
 * @description Rx 线程调度
 *
 * 参考文章：[RxJava2.x 操作符之 compose](https://cloud.tencent.com/developer/article/1592795)
 *
 * 使用方式：
 * myObservable
 *     .compose(RxSchedulerUtils.observableToMain())
 *     .subscribe();
 *
 * <p>
 * modification history:
 */
public class RxSchedulerUtils {

   /**
    * Observable 切换到主线程
    */
   public static <T> ObservableTransformer<T, T> observableToMain() {
      return upstream -> upstream
              .subscribeOn(Schedulers.io())
              .unsubscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread());
   }

   /**
    * Flowable 切换到主线程
    */
   public static <T> FlowableTransformer<T, T> flowableToMain() {
      return upstream -> upstream
              .subscribeOn(Schedulers.io())
              .unsubscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread());
   }

   /**
    * Maybe 切换到主线程
    */
   public static <T> MaybeTransformer<T, T> maybeToMain() {
      return upstream -> upstream
              .subscribeOn(Schedulers.io())
              .unsubscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread());
   }

}
