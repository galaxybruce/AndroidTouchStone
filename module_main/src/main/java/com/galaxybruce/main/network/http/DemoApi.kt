package com.galaxybruce.main.network.http

import com.galaxybruce.main.model.ApkUpdateInfo
import com.galaxybruce.main.model.AppBean4Cms
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @date 2021/4/1 14:34
 * @author
 * @description
 *
 *
 * modification history:
 */
interface DemoApi {

    /**
     * RxJava方式
     */
    @GET
    fun getData(@Url url: String?): Observable<AppBean4Cms<ApkUpdateInfo>>

    /**
     * 协程方式
     */
    @GET
    suspend fun getDataSuspend(@Url url: String?): AppBean4Cms<ApkUpdateInfo>
}