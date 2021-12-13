package com.galaxybruce.component.net.transformer;

import com.galaxybruce.component.net.exception.AppNetExceptionHandler;
import com.galaxybruce.component.net.exception.AppNetException;
import com.galaxybruce.component.net.model.IAppBean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * @date 2021/12/13 18:12
 * @author bruce.zhang
 * @description 异常的统一处理
 * <p>
 * modification history:
 */
public class AppNetResponseTransformer {

    public static <T extends IAppBean> ObservableTransformer<T, T> handleResult() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .onErrorResumeNext(new ErrorResumeFunction<T>())
                        .flatMap(new ResponseFunction<T>());
            }
        };
    }

    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     */
    public static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<T>> {

        @Override
        public ObservableSource<T> apply(Throwable throwable) throws Exception {
            return Observable.error(AppNetExceptionHandler.handleException(throwable));
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    public static class ResponseFunction<T extends IAppBean> implements Function<T, ObservableSource<T>> {

        @Override
        public ObservableSource<T> apply(T entity) throws Exception {
            if (entity.isSuccessful()) {
                return Observable.just(entity);
            } else {
                return Observable.error(new AppNetException(entity.getCode(), entity.getMessage()));
            }
        }
    }
}
