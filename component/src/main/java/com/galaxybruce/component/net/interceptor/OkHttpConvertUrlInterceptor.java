package com.galaxybruce.component.net.interceptor;

import com.galaxybruce.component.internal.AppInternal;
import com.galaxybruce.component.internal.IHttpRequestOptions;

import java.io.IOException;

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
public class OkHttpConvertUrlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
//        if (request.isHttps() || RegexUtils.isIP(request.url().host())) {
//            return chain.proceed(request);
//        }

        IHttpRequestOptions httpRequestOptions = AppInternal.getInstance().getHttpRequestOptions();
        if(httpRequestOptions != null) {
            String url = request.url().toString();
            url = httpRequestOptions.convertUrl(url);
//            HttpUrl httpUrl = request.url().newBuilder()
//                    .scheme("https")
//                    .port(443)
//                    .build();
            request = request.newBuilder()
                    .url(url)
                    .build();
        }
        return chain.proceed(request);
    }
}
