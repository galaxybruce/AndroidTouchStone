package com.galaxybruce.component.ui.jetpack

import androidx.lifecycle.MutableLiveData

/**
 * @date 2020/8/10  5:34 PM
 * @author
 * @description
 * <p>
 * modification history:
 */
interface IJPViewModelAction {
    fun showLoadingProgress(message: String? = null)

    fun hideLoadingProgress()

    fun showToast(message: String?)

    fun reLogin()

    fun finish()

    fun getActionLiveData(): MutableLiveData<JPPageActionEvent>
}