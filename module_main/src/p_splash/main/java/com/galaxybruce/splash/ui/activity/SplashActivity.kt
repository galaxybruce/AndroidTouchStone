package com.galaxybruce.splash.ui.activity

import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Bundle
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.billy.cc.core.component.CC
import com.galaxybruce.base.manager.AppSessionManager
import com.galaxybruce.base.ui.activity.AppBaseActivity
import com.galaxybruce.component.app.privacy.AppPrivacyUtil
import com.galaxybruce.component.internal.AppInternal
import com.galaxybruce.component.ui.activity.AppTitleInfo
import com.galaxybruce.component.ui.jetpack.JPDataBindingConfig
import com.galaxybruce.component.util.AppActivityUtil
import com.galaxybruce.component.util.AppConstants
import com.galaxybruce.component.util.extensions.remove
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
class SplashActivity : AppBaseActivity<SplashViewModel, SplashLayoutBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!AppPrivacyUtil.checkPrivacyInLaunchActivity(this)) {
            return
        }
        // installSplashScreen必须调用，原因是install函数会获取postSplashScreenTheme 配置的主题，并在检查通过后setTheme 给Activity。
        val compatSplashScreen = installSplashScreen()
        // 这段代码是用来让启动画面继续显示，用来在页面显示之前异步记载数据。根据实际需求决定要不要以及时间长短，或者通过其他条件动态决定
        compatSplashScreen.setKeepOnScreenCondition {
            // 这里可以不用等数据加载完成，直接返回false即可
            !(mPageViewModel.dataLoaded.value ?: false)
        }
        // 退出动画
        compatSplashScreen.setOnExitAnimationListener { provider ->
            ObjectAnimator.ofFloat(provider.iconView, View.SCALE_X, View.SCALE_Y, Path().apply {
                moveTo(1f, 1f)
                lineTo(0f, 0f)
            }).apply {
                doOnEnd { provider.view.remove() }
                start()
            }
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

        setLiveDataObserver(mPageViewModel.dataLoaded) {
            if(it) {
                // 数据加载完后，在启动页停留2秒，观察下启动动画消失后的情况
                window.decorView.postDelayed({
                    if(AppInternal.getInstance().mustLogin() && !AppSessionManager.getInstance().isLogin) {
                        CC.obtainBuilder("MainComponent")
                            .setActionName("openLoginActivity")
                            .addParam(AppConstants.IntentKeys.KEY_LOGIN_SUCCESS_ROUTER, "openMainActivity")
                            .build().call()
                    } else {
                        CC.obtainBuilder("MainComponent")
                            .setActionName("openMainActivity")
                            .build().call()
                    }
                    finish()
                }, 2000)

            }
        }

        mPageViewModel.request.requestAdData {
            mPageViewModel.dataLoaded.value = true
        }
    }
}