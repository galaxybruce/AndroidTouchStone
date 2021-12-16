package com.galaxybruce.component.net.interceptor;

import android.text.TextUtils;

import com.galaxybruce.component.interal.AppInternal;
import com.galaxybruce.component.interal.IAuthAccount;
import com.galaxybruce.component.net.cookie.AppCookieManager;
import com.galaxybruce.component.net.exception.AppLoginExpiresException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CookieInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request source = chain.request();

        Map<String, String> params = new HashMap<>(16);
        // 对get/post请求的url中的参数进行cookie封装，验签
        if (source.url() != null
                && !TextUtils.isEmpty(source.url().encodedQuery())) {
            HttpUrl httpUrl = source.url();
            Set<String> keys = httpUrl.queryParameterNames();
            for (String key : keys) {
                params.put(key, httpUrl.queryParameter(key));
            }
        }

        RequestBody requestBody = source.body();
        if (requestBody instanceof FormBody) {
            FormBody formBody = (FormBody) source.body();
            int size = formBody == null ? 0 : formBody.size();
            for (int i = 0; i < size; i++) {
                params.put(formBody.name(i), formBody.value(i));
            }
        }

        Request.Builder builder = source.newBuilder();
        Map<String, String> headers = AppCookieManager.getHeaders(params);
        if(headers != null && !headers.isEmpty()) {
            Set<Map.Entry<String, String>> set = headers.entrySet();
            for (Map.Entry<String, String> entry : set) {
                if (!TextUtils.isEmpty(entry.getKey())
                        && !TextUtils.isEmpty(entry.getValue())) {
                    builder.header(entry.getKey(), entry.getValue());
                }
            }
        }

        // 登录态检测，需要再header中增加checkLogin:
        // @Headers({"Content-type:application/json;charset=UTF-8;", "checkLogin:true"})
        String checkLogin = source.header("checkLogin");
        if(TextUtils.equals(Boolean.toString(true), checkLogin)) {
            IAuthAccount authAccount = AppInternal.getInstance().getAuthAccount();
            if (authAccount != null && TextUtils.isEmpty(authAccount.getUid())) {
                throw new AppLoginExpiresException();
            }
        }

        //  todo 这里可以进行灰度处理，请求url替换

        return chain.proceed(builder.build());
    }


}
