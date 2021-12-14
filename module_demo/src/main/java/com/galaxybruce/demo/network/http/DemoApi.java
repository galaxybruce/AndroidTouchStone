package com.galaxybruce.demo.network.http;


import com.galaxybruce.demo.model.AppBean4Cms;
import com.galaxybruce.demo.model.GroupSocketHost;

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
    Observable<AppBean4Cms<GroupSocketHost>> getData(@Url String url);

}
