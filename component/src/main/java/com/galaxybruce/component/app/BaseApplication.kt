package com.galaxybruce.component.app

import android.app.Activity
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDexApplication
import com.bumptech.glide.Glide
import com.galaxybruce.component.app.crash.CrashHandler
import com.galaxybruce.component.app.privacy.PrivacyUtil


abstract class BaseApplication : MultiDexApplication(), ViewModelStoreOwner {

    companion object {
        lateinit var instance: BaseApplication
        // 是否是degug版本
        var DEBUG: Boolean = false

        // 是否是可以debug
        var DEBUG_ABLE: Boolean = false

        fun isDebug(): Boolean {
            return DEBUG
        }

        fun debugAble(): Boolean {
            return DEBUG_ABLE
        }
    }

    private val appLifecycle = AppActivityLifecycle()

    private val mAppViewModelStore : ViewModelStore by lazy {
        ViewModelStore()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        registerActivityLifecycleCallbacks(appLifecycle)

        val isMainProcess = isMainProcess()
        if (isMainProcess) {
            initCrash()
        }

        if (!checkPrivacyPolicy()) {
            return
        }
        _onCreate(isMainProcess)
    }


    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(instance).clearMemory()
        }
        Glide.get(instance).trimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(instance).clearMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    private fun initCrash() {
        CrashHandler.init(this, true)
    }

    protected fun isMainProcess(): Boolean {
        val packageName = packageName
        val processName = PrivacyUtil.getCurProcessName(this)
        return packageName == processName
    }

    fun finishAllActivity() {
        appLifecycle.finishActivities()
    }

    fun getTopActivity(): Activity? {
        return appLifecycle.topActivity
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    open fun getAppViewModelProvider(): ViewModelProvider {
        val factory: ViewModelProvider.Factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        return ViewModelProvider(this, factory)
    }

    /**
     * 更新登录事件
     */
    open fun updateLoginEvent(login : Boolean) {
        // 增加LiveData通知事件
        val appViewModel = instance
            .getAppViewModelProvider().get(AppViewModel::class.java)
        appViewModel.loginEvent.value = login
    }

    protected open fun _onCreate(isMainProcess: Boolean) {

    }

    /**
     * 隐私政策check
     * privacy_policy_agreed_type: 0: 默认状态 1-同意; 2-拒绝
     *
     * 其他解决方案：[通过拦截 Activity的创建 实现APP的隐私政策改造](https://juejin.cn/post/6990643611130363917)
     */
    protected open fun checkPrivacyPolicy(): Boolean {
        return true
    }

}