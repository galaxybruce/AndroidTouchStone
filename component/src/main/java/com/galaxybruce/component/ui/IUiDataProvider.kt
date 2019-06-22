package com.galaxybruce.component.ui

import android.content.Context

/**
 * @author bruce.zhang
 * @date 2018/11/13 16:46
 * @description
 *
 *
 *
 * modification history:
 */
interface IUiDataProvider {

    /**
     * 提供在Activity或者Fragment生命周期内的缓存容器
     * 此缓存容器和 Activity或者Fragment的生命周期绑定，屏幕旋转的情况下回情况，需要另外处理
     * @return
     */
    fun provideCache(): Map<String, Any>

    /**
     * 提供页面Context
     * @return
     */
    fun provideContext(): Context

    /**
     * 提供页面唯一识别符
     * @return
     */
    fun provideIdentifier(): Int
}
