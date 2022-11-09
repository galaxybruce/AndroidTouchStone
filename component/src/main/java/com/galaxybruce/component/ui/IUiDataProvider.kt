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
