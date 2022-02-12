package com.galaxybruce.main.ui.activity

import android.content.res.Resources
import android.os.Bundle
import android.os.Parcel
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.AdaptScreenUtils
import com.galaxybruce.component.router.RouterUrlBuilder
import com.galaxybruce.component.ui.activity.BaseTitleBarActivity
import com.galaxybruce.component.ui.dialog.AppConfirmDialog
import com.galaxybruce.component.ui.dialog.AppCustomConfirmDialog
import com.galaxybruce.component.ui.dialog.AppCustomConfirmDialog.AppConfirmDialogCallback
import com.galaxybruce.main.R
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = "/app/main")
class MainActivity : BaseTitleBarActivity() {

    override fun bindTitle(): CharSequence {
        return "MainActivity"
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
    }

    override fun bindLayoutId(): Int {
        return R.layout.activity_main
    }

    companion object {
        var n: Int = 0
    }

    override fun initView(view: View?) {
        image.setOnClickListener {
//            RouterUrlBuilder.instance("/test/TestActivity")
//                .addParam("a", "a11").go(this)

            AppConfirmDialog.getInstance("提示", "哈哈哈哈", false,
                object : AppConfirmDialogCallback() {
                    override fun onCancel() {
                        showToast("cancel== ${++n}")
                    }

                    override fun onConfirm() {
                        showToast("ok== ${++n}")                    }
                })
                .show(supportFragmentManager, "aaa")
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
    }

    override fun getTitleOverLabMode(): Boolean {
        return true
    }

    /**
     * 以pt为单位适配，这里的宽度已设计稿的标准像素尺寸为准
     */
    override fun getResources(): Resources {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 750)
    }

}
