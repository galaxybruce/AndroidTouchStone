package com.galaxybruce.component.app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @date 2021/1/12 14:09
 * @author bruce.zhang
 * @description app级别ViewMode，用来存放app级数据
 *
 * 监听登录：
 * AppViewModel appViewModel = UVBaseApplication.Companion.getInstance()
 *         .getAppViewModelProvider().get(AppViewModel.class);
 * appViewModel.getLoginEvent().observe(activity/fragment.getViewLifecycleOwner(), new Observer<Boolean>() {
 *     @Override
 *     public void onChanged(Boolean aBoolean) {
 *       
 *     }
 * });
 *
 * modification history:
 */
class AppViewModel : ViewModel() {

    /**
     * 是否重新登录或者切换账号
     */
    val loginEvent = MutableLiveData<Boolean>()

    init {
        loginEvent.value = false
    }
}