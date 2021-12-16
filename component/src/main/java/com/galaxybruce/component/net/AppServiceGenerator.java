package com.galaxybruce.component.net;

import com.galaxybruce.component.net.factory.DateToLongDeserializer;
import com.galaxybruce.component.net.factory.KeepConverterFactory;
import com.galaxybruce.component.net.interceptor.CookieInterceptor;
import com.galaxybruce.component.net.interceptor.EncryptInterceptor;
import com.galaxybruce.component.net.interceptor.HttpsInterceptor;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @date 2021/12/16 14:38
 * @author bruce.zhang
 * @description retrofit封装
 *
 * 参考文章：
 * 1. [Android搭建应用框架系列之Retrofit封装](https://www.jianshu.com/p/26b2cfc3cbda)
 * <p>
 * modification history:
 */
public class AppServiceGenerator {

    /**
     * 外部自定义的interceptor
     */
    private static List<Interceptor> sExternalInterceptors;

    private AppServiceGenerator() {
    }

    private static Retrofit mRetrofit;

    public static void init(AppRetrofitConfig config) {
        if (mRetrofit == null) {
            createRetrofit(config);
        }
    }

    private static void createRetrofit(AppRetrofitConfig httpConfig) {
        if (httpConfig == null) {
            throw new ExceptionInInitializerError("AppRetrofitConfig must not be null!");
        }
        OkHttpClient.Builder builder = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                .connectTimeout(httpConfig.getConnectTimeout(), TimeUnit.SECONDS)
                .writeTimeout(httpConfig.getWriteTimeout(), TimeUnit.SECONDS)
                .readTimeout(httpConfig.getReadTimeout(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(httpConfig.isRetryOnConnectionFailure());
        for (Interceptor interceptor : httpConfig.getInterceptors()) {
            builder.addInterceptor(interceptor);
        }

        mRetrofit = new Retrofit.Builder()
                .baseUrl(httpConfig.getBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.create())
                .addConverterFactory(KeepConverterFactory.create())
                .addConverterFactory(new StringConverterFactory())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(Long.class, new DateToLongDeserializer())
                        .registerTypeAdapter(long.class, new DateToLongDeserializer())
                        .setLenient().create()))
                .client(builder.build())
                .build();
    }

    /**
     * 创建请求对象
     *
     * @param serviceClass class
     * @param <S>          class对象
     * @return class对象
     */
    public static <S> S createService(Class<S> serviceClass) {
        if (mRetrofit == null) {
            initDefaultRetrofit();
        }
        return mRetrofit.create(serviceClass);
    }

    private static synchronized void initDefaultRetrofit() {
        AppRetrofitConfig.Builder config = new AppRetrofitConfig.Builder()
                .baseUrl("https://www.google.com/")
                .setConnectTimeout(30)
                .setReadTimeout(30)
                .setWriteTimeout(30)
                .addInterceptor(new CookieInterceptor())
                .addInterceptor(new EncryptInterceptor())
                .addInterceptor(new HttpsInterceptor());

        if(sExternalInterceptors != null && !sExternalInterceptors.isEmpty()) {
            for (Interceptor externalInterceptor : sExternalInterceptors) {
                config.addInterceptor(externalInterceptor);
            }
            sExternalInterceptors.clear();
        }

        if (mRetrofit == null) {
            AppServiceGenerator.init(config.build());
        }
    }

    /**
     * 设置自定义interceptor
     * @param externalInterceptors
     */
    public static void addExternalInterceptors(List<Interceptor> externalInterceptors) {
        sExternalInterceptors = new ArrayList<>(externalInterceptors);
    }

}
