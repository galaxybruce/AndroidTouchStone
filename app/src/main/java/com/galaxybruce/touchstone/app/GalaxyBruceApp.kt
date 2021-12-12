package com.galaxybruce.touchstone.app

import com.galaxybruce.component.app.BaseApplication
import com.galaxybruce.touchstone.BuildConfig

/**
 * @date 2019-06-22 10:41
 * @author bruce.zhang
 * @description
 *
 * modification history:
 */

class GalaxyBruceApp : BaseApplication() {

    override fun onCreate() {
        BaseApplication.DEBUG = BuildConfig.DEBUG
        BaseApplication.DEBUG_ABLE = BuildConfig.DEBUG_ABLE
        super.onCreate()
    }
}