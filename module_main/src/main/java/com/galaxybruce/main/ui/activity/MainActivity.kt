package com.galaxybruce.main.ui.activity

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.galaxybruce.component.router.RouterUrlBuilder
import com.galaxybruce.component.ui.activity.BaseTitleBarActivity
import com.galaxybruce.main.R
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = "main")
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
            RouterUrlBuilder.instance("/test/TestActivity")
                .addParam("a", "a11").go(this)
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
    }

    override fun getTitleOverLabMode(): Boolean {
        return true
    }

}
