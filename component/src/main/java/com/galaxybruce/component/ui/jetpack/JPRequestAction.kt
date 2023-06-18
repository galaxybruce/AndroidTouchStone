package com.galaxybruce.component.ui.jetpack

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ViewUtils
import com.galaxybruce.component.ui.jetpack.livedata.UnStickyMutableLiveData

/**
 * @date 2023/6/18 17:07
 * @author bruce.zhang
 * @description request中常用行为
 *
 * modification history:
 */
class JPRequestAction: IJPViewModelAction {

    // 这里为什么使用UnPeekLiveData，而不是普通的LiveData？是为了解决数据倒灌的问题。
    // 页面弹窗、加载框、登录的不像普通的数据重复设置不影响
    private val actionEvent: MutableLiveData<JPPageActionEvent> = UnStickyMutableLiveData<JPPageActionEvent>()

    override fun showLoadingProgress(message: String?) {
        ViewUtils.runOnUiThread {
            actionEvent.value = JPPageActionEvent(JPPageActionEvent.SHOW_LOADING_DIALOG, message)
        }
    }

    override fun hideLoadingProgress() {
        ViewUtils.runOnUiThread {
            actionEvent.value = JPPageActionEvent(JPPageActionEvent.DISMISS_LOADING_DIALOG)
        }
    }

    override fun showToast(message: String?) {
        ViewUtils.runOnUiThread {
            actionEvent.value = JPPageActionEvent(JPPageActionEvent.SHOW_TOAST, message)
        }
    }

    override fun reLogin() {
        ViewUtils.runOnUiThread {
            actionEvent.value = JPPageActionEvent(JPPageActionEvent.LOGIN)
        }
    }

    override fun finish() {
        ViewUtils.runOnUiThread {
            actionEvent.value = JPPageActionEvent(JPPageActionEvent.FINISH)
        }
    }

    override fun getActionLiveData(): MutableLiveData<JPPageActionEvent> {
        return actionEvent
    }

}