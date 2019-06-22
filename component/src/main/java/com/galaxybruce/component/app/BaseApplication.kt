package com.galaxybruce.component.app

import android.support.multidex.MultiDexApplication
import com.galaxybruce.component.BuildConfig
import com.squareup.leakcanary.LeakCanary


abstract class BaseApplication : MultiDexApplication() {

    companion object {
        lateinit var instance: BaseApplication

        fun isDebug(): Boolean {
            return BuildConfig.DEBUG
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        initLeakCanary()
        initCrash()
    }

    private fun initLeakCanary() {// 内存泄露检查工具
        if (isDebug()) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return
            }
            LeakCanary.install(this)
        }
    }

    private fun initCrash() {

    }

}
