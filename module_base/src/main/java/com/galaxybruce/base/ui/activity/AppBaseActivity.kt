package com.galaxybruce.base.ui.activity


import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.ClickUtils
import com.galaxybruce.base.util.debug.AppDebugLogDialog
import com.galaxybruce.component.ui.jetpack.JPBaseActivity

/**
 * @date 2021/5/23 20:39
 * @author bruce.zhang
 * @description app activity基类
 * <p>
 * modification history:
 */
abstract class AppBaseActivity<B: ViewDataBinding> : JPBaseActivity<B>() {

    override fun applyStyle2ActivityTheme() {
        super.applyStyle2ActivityTheme()
        // 全屏透明状态栏基类
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)//remove notification bar  即全屏
    }

//    override fun initStatusBar() {
//        BBSStatusBarUtil.setTranslucentForImageView(this, 0,
//            android.R.color.transparent, null)
//    }

    /**
     * 以pt为单位适配，这里的宽度已设计稿的标准像素尺寸为准
     */
    override fun getResources(): Resources {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 750)
    }

    override fun initView(view: View?) {
        super.initView(view)

        // 调试日志面板
        findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
            .setOnClickListener(object: ClickUtils.OnMultiClickListener(5) {
                override fun onTriggerClick(v: View?) {
                    AppDebugLogDialog.Builder()
                        .build().show(supportFragmentManager, "AppDebugLogDialog")
                }

                override fun onBeforeTriggerClick(v: View?, count: Int) {
                }
            })
    }
}