package com.galaxybruce.component.net.interceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author bruce.zhang
 * @date 2020/11/4 10:20
 * @description (亲 ， 我是做什么的)
 * <p>
 * modification history:
 */
public class HttpsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request source = chain.request();
        if (source.isHttps()) {
            return chain.proceed(source);
        }

        HttpUrl httpUrl = source.url().newBuilder()
                .scheme("https")
                .port(443)
                .build();
        Request request = source.newBuilder()
                .url(httpUrl)
                .build();
        return chain.proceed(request);
    }
}
