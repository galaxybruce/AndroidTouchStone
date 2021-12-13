package com.galaxybruce.component.ui

/**
 * @date 2019/3/20 11:25
 * @author bruce.zhang
 * @description 所有的UI都可以实现这个接口 Activity Fragment CustomView
 *
 *
 * modification history:
 */
interface IUiView {

    /** 显示加载框  */
    fun showLoadingProgress(message: String?)

    /** 隐藏加载框  */
    fun hideLoadingProgress()

    fun showToast(message: String?)

}
