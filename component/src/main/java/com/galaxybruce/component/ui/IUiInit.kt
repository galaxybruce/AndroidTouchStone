package com.galaxybruce.component.ui

import android.os.Bundle
import android.view.View

/**
 * @date 2019/3/20 11:25
 * @author bruce.zhang
 * @description 所有的UI都可以实现这个接口 Activity Fragment CustomView
 *
 *
 * modification history:
 */
interface IUiInit {

    /** 在initView之前初始化数据  */
    fun initData(bundle: Bundle?, savedInstanceState: Bundle? = null): Boolean

    /** 返回布局id  */
    fun bindLayoutId(): Int

    /** find view and bind listener */
    fun initView(view: View?)

    /** 绑定数据  */
    fun bindData(savedInstanceState: Bundle? = null)

}
