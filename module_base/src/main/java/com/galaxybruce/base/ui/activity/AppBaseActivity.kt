package com.galaxybruce.base.ui.activity


import androidx.databinding.ViewDataBinding
import com.galaxybruce.component.ui.jetpack.JPBaseActivityV2
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel

/**
 * @date 2021/5/23 20:39
 * @author bruce.zhang
 * @description app activity基类
 * <p>
 * modification history:
 */
abstract class AppBaseActivity<VM : JPBaseViewModel, B : ViewDataBinding> : JPBaseActivityV2<VM, B>() {



}