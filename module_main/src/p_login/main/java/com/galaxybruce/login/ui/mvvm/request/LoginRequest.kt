package com.galaxybruce.login.ui.mvvm.request

import android.annotation.SuppressLint
import com.galaxybruce.base.manager.AppSessionManager
import com.galaxybruce.base.model.AppUserInfo
import com.galaxybruce.component.app.BaseApplication
import com.galaxybruce.component.net.AppServiceGenerator
import com.galaxybruce.component.net.model.AppGenericBean
import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.component.ui.jetpack.SuccessCallback
import com.galaxybruce.login.network.http.LoginApi
import com.galaxybruce.login.network.http.LoginServerUrl
import com.galaxybruce.login.ui.mvvm.viewmodel.LoginViewModel
import com.galaxybruce.main.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginRequest(viewModel: LoginViewModel) : JPBaseRequest(viewModel) {

    private val mApi: LoginApi = AppServiceGenerator.createService(LoginApi::class.java)

    @SuppressLint("CheckResult")
    fun loginRequest(account: String?, pwd: String?, successCallback: SuccessCallback<Boolean>) {
        showLoadingProgress()

        val requestUrl: String = LoginServerUrl.URL_LOGIN
        mApi.login(requestUrl)
            .compose(this.handleEverythingResult<AppGenericBean<Any>>(false))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getUserInfoRequest(account, pwd, successCallback)
            }, {
                hideLoadingProgress()
                showToast(it.message)
            })
    }

    @SuppressLint("CheckResult")
    fun getUserInfoRequest(account: String?, pwd: String?, successCallback: SuccessCallback<Boolean>) {
        val requestUrl: String = LoginServerUrl.URL_USER_INFO
        mApi.getUserInfo(requestUrl)
            .compose(this.handleEverythingResult<AppGenericBean<AppUserInfo>>(false))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                hideLoadingProgress()
            }
            .subscribe({
                if(it.data == null) {
                    throw Exception(BaseApplication.instance.resources.getString(R.string.login_user_info_empty_hint))
                }
                AppSessionManager.getInstance().userInfo = it.data.apply {
                    this.account = account
                    this.pwd = pwd
                }
                successCallback(true)
            }, {
                showToast(it.message)
                // todo demo中没有接口，直接返回登录成功
                successCallback(true)
            })
    }
}