package com.galaxybruce.component.ui.jetpack

/**
 * @date 2020/8/11  10:15 AM
 * @author
 * @description mvvm 页面事件动作
 * <p>
 * modification history:
 */
data class JPPageActionEvent(val action: Int, val message: String? = null) {

    companion object {
        const val SHOW_LOADING_DIALOG = 1
        const val DISMISS_LOADING_DIALOG = 2
        const val SHOW_TOAST = 3
        const val LOGIN = 4
        const val FINISH = 5
    }
}