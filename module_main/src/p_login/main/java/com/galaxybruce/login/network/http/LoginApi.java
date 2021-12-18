package com.galaxybruce.login.network.http;

import com.galaxybruce.base.model.AppUserInfo;
import com.galaxybruce.component.net.model.AppGenericBean;

import io.reactivex.Observable;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @date 2021/4/1 14:34
 * @author
 * @description
 * <p>
 * modification history:
 */
public interface LoginApi {

    /**
     * 登录
     * @param url
     * @return
     */
    @POST
    @Headers("Content-Type: application/json")
    Observable<AppGenericBean> login(@Url String url);

    /**
     * 获取用户信息
     * @param url
     * @return
     */
    @POST
    @Headers("Content-Type: application/json")
    Observable<AppGenericBean<AppUserInfo>> getUserInfo(@Url String url);

}
