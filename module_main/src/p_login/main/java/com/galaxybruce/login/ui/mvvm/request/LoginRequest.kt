package com.galaxybruce.login.ui.mvvm.request

import android.annotation.SuppressLint
import com.galaxybruce.base.manager.AppUserInfoManager
import com.galaxybruce.base.model.AppUserInfo
import com.galaxybruce.component.net.AppServiceGenerator
import com.galaxybruce.component.net.model.AppGenericBean
import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.login.network.http.LoginApi
import com.galaxybruce.login.network.http.LoginServerUrl
import com.galaxybruce.login.ui.mvvm.viewmodel.LoginViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginRequest(private val viewModel: LoginViewModel) : JPBaseRequest() {

    private val mApi: LoginApi = AppServiceGenerator.createService(LoginApi::class.java)

    @SuppressLint("CheckResult")
    fun loginRequest(name: String?, password: String?) {
        showLoadingProgress()

        val requestUrl: String = LoginServerUrl.URL_LOGIN
        mApi.login(requestUrl)
            .compose(this.handleEverythingResult<AppGenericBean<Any>>(false))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getUserInfoRequest()
            }, {
                hideLoadingProgress()
                showToast(it.message)

                // todo demo中没有接口，直接返回登录成功
                viewModel.loginSuccess.value = true
            })
    }

    @SuppressLint("CheckResult")
    fun getUserInfoRequest() {
        val requestUrl: String = LoginServerUrl.URL_USER_INFO
        mApi.getUserInfo(requestUrl)
            .compose(this.handleEverythingResult<AppGenericBean<AppUserInfo>>(false))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                hideLoadingProgress()
            }
            .subscribe({
                AppUserInfoManager.getInstance().userInfo = it.data
                viewModel.loginSuccess.value = true
            }, {
                showToast(it.message)
            })
    }
}