package com.galaxybruce.splash.ui.activity

import android.os.Bundle
import android.view.View
import com.galaxybruce.base.manager.AppSessionManager
import com.galaxybruce.base.ui.activity.AppBaseActivity
import com.galaxybruce.component.app.privacy.AppPrivacyUtil
import com.galaxybruce.component.internal.AppInternal
import com.galaxybruce.component.router.AppRouterUrlBuilder
import com.galaxybruce.component.ui.activity.AppTitleInfo
import com.galaxybruce.component.ui.jetpack.JPDataBindingConfig
import com.galaxybruce.component.util.AppActivityUtil
import com.galaxybruce.component.util.AppConstants
import com.galaxybruce.main.BR
import com.galaxybruce.main.R
import com.galaxybruce.main.databinding.SplashLayoutBinding
import com.galaxybruce.splash.ui.mvvm.viewmodel.SplashViewModel

/**
 * @date 2021/4/23 22:30
 * @author
 * @description 启动页
 *
 * <p>
 * modification history:
 */
class SplashActivity : AppBaseActivity<SplashLayoutBinding, SplashViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!AppPrivacyUtil.checkPrivacyInLaunchActivity(this)) {
            return
        }
        super.onCreate(savedInstanceState)
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (AppActivityUtil.handleSplashActivity(this)) {
            return
        }
    }

    override fun initDataBindConfig(): JPDataBindingConfig {
        return JPDataBindingConfig(bindLayoutId())
            .addBindingParam(BR.vm, mPageViewModel)
    }

    override fun getTitleInfo(): AppTitleInfo {
        return AppTitleInfo.build {
            titleMode = AppConstants.TITLE_MODE_NONE
        }
    }

    override fun bindLayoutId(): Int {
        return R.layout.splash_layout
    }

    override fun initView(view: View?) {
        super.initView(view)
    }

    override fun bindData(savedInstanceState: Bundle?) {
        super.bindData(savedInstanceState)

        if(AppInternal.getInstance().mustLogin() && !AppSessionManager.getInstance().isLogin) {
            AppRouterUrlBuilder.instance("/app/login")
                .addParam(AppConstants.IntentKeys.KEY_LOGIN_SUCCESS_ROUTER, "/app/main").go(this)
        } else {
            AppRouterUrlBuilder.instance("/app/main").go(this)
        }
        finish()
    }
}