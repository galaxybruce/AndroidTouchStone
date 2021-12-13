package com.galaxybruce.touchstone

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.galaxybruce.component.ui.activity.BaseTitleBarActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseTitleBarActivity() {

    override fun bindTitle(): CharSequence {
        return "MainActivity"
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
    }

    override fun bindLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(view: View?) {
        image.setOnClickListener {
            ARouter.getInstance().build("/test/TestActivity").navigation()
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
    }

    override fun getTitleOverLabMode(): Boolean {
        return true
    }

}
