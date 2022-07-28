package com.galaxybruce.component.router

import android.app.Activity
import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @date 2019-11-04 11:05
 * @author bruce.zhang
 * @description 构造路由url
 *
 * modification history:
 */
class AppRouterUrlBuilder {

    companion object {
        fun instance(url: String): AppRouterUrlBuilder {
            val instance = AppRouterUrlBuilder()
            instance.mUrl = ARouter.getInstance().build(url)
            return instance
        }
    }

    lateinit var mUrl: Postcard

    fun addParam(key: String, value: String): AppRouterUrlBuilder {
        mUrl.withString(key, value)
        return this
    }

    fun go(context: Context) {
        mUrl.navigation(context)
    }

    fun go(context: Activity, requestCode: Int) {
        mUrl.navigation(context, requestCode)
    }

}