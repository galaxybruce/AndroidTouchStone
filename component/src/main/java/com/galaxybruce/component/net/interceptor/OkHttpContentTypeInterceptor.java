package com.galaxybruce.component.net.interceptor;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @date 2022/8/30 19:05
 * @author bruce.zhang
 * @description 修改Content-Type
 * <p>
 * modification history:
 */
public class OkHttpContentTypeInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();

        // post请求统一补全Content-Type中的charset；get请求不需要Content-Type，不用处理
        if(requestBody != null) {
            MediaType mediaType = requestBody.contentType();
            // 文件上传不需要加charset，否在上传的图片大小是整个请求体的大小
            if (mediaType != null && !TextUtils.equals(mediaType.type(), "multipart") && mediaType.charset() == null) {
                Request.Builder builder = request.newBuilder();
                mediaType = MediaType.Companion.parse(mediaType.toString() + ";charset=utf-8");
                request = builder
//                        .header("Content-Type", mediaType.toString()) // 这句代码没什么用，必须修改RequestBody中的contentType
                        .method(request.method(), RequestBody.Companion.create(bodyToString(requestBody), mediaType))
                        .build();
            }
        }

        return chain.proceed(request);
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null) {
                request.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return e.getMessage();
        }
    }

}
