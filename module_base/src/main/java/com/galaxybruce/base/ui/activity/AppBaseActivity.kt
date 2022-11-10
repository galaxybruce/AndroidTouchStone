package com.galaxybruce.base.ui.activity


import android.content.res.Resources
import android.view.MenuItem
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.AdaptScreenUtils
import com.galaxybruce.component.ui.jetpack.JPBaseActivityV2
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel

/**
 * @date 2021/5/23 20:39
 * @author bruce.zhang
 * @description app activity基类
 * <p>
 * modification history:
 */
abstract class AppBaseActivity<B : ViewDataBinding, VM : JPBaseViewModel> : JPBaseActivityV2<B, VM>() {

    /**
     * 以pt为单位适配，这里的宽度已设计稿的标准像素尺寸为准
     */
    override fun getResources(): Resources {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 750)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}