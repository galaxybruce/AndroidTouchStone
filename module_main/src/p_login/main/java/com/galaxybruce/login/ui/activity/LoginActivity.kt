package com.galaxybruce.login.ui.activity


import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.galaxybruce.base.ui.activity.AppBaseActivity
import com.galaxybruce.component.app.BaseApplication
import com.galaxybruce.component.router.RouterUrlBuilder
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel
import com.galaxybruce.component.ui.jetpack.JPDataBindingConfig
import com.galaxybruce.component.util.AppConstants
import com.galaxybruce.login.ui.mvvm.viewmodel.LoginViewModel
import com.galaxybruce.main.BR
import com.galaxybruce.main.R
import com.galaxybruce.main.databinding.LoginLayoutBinding

/**
 * @date 2021/4/29 10:49
 * @author
 * @description 登录页面
 *
 * 逻辑梳理：
 * 进入登录页面的场景有三种：
 * 1. 刚启动app，缓存中没有登录信息进入登录页
 *      这种情况最简单，登录后获取用户信息，再进入主页面
 *
 * 2. 用户进入设置页，主动退出登录
 *      退出登录后，用户可能是不用了，也可能切换账号，所以把所有资源清理掉。
 *      登录成功后，再用新的账号信息获取资源。
 *
 * 3. 用户请求接口，登录态过期
 *      这种情况，直接进入登录页，不可以关闭其他页面，登录成功后，关闭登录页就行，
 *      并发送登录成功的消息，其他页面收到登录成功的消息做对应的业务处理。
 *
 *
 * 重点：
 * 1. 点返回时，关闭所以页面finishAllActivity()
 * 2. 登录页有可能是切换账号，所以登录成功后，要在登录页拉取个人信息相关的接口。
 *
 * <p>
 * modification history:
 */
@Route(path = "/app/login")
class LoginActivity : AppBaseActivity<LoginLayoutBinding>() {

    private lateinit var mPageViewModel: LoginViewModel

    override fun initViewModel(): JPBaseViewModel {
        mPageViewModel = getActivityViewModel(LoginViewModel::class.java)
        return mPageViewModel
    }

    override fun initDataBindConfig(): JPDataBindingConfig {
        return JPDataBindingConfig(bindLayoutId())
            .addBindingParam(BR.vm, mPageViewModel)
            .addBindingParam(BR.click, ClickProxy())
    }

    override fun bindLayoutId(): Int {
        return R.layout.login_layout
    }

    override fun bindData(savedInstanceState: Bundle?) {
        super.bindData(savedInstanceState)

        // RouterUrlBuilder.instance("/app/login")
        //      .addParam(AppConstants.Login.KEY_LOGIN_SUCCESS_ROUTER, "/app/startphone")
        //      .go(mActivity)
        setLiveDataObserver(mPageViewModel.loginSuccess) { loginSuccess ->
            loginSuccess?.takeIf { it }?.let {
                BaseApplication.instance.updateLoginEvent(true)

                val routePath = intent.getStringExtra(AppConstants.Login.KEY_LOGIN_SUCCESS_ROUTER)
                if(routePath.isNullOrBlank()) {
                    finish()
                } else {
                    RouterUrlBuilder.instance(routePath).go(this)
                    finishAllActivity()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAllActivity()
    }

    /**
     * 页面事件类，可以把所有事件都写在这里
     */
    inner class ClickProxy {
        fun login() {
            mPageViewModel.request.loginRequest(mPageViewModel.email.get(),
                mPageViewModel.password.get())
        }
    }
}