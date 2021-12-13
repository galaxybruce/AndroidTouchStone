package com.galaxybruce.touchstone.demo.http;


import com.galaxybruce.component.net.model.AppGenericBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * @date 2021/4/1 14:34
 * @author
 * @description
 * <p>
 * modification history:
 */
public interface DemoApi {

    @GET
    Observable<AppGenericBean<GroupSocketHost>> getData(@Url String url);

}
