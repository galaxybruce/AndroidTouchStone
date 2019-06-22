package com.galaxybruce.touchstone

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.galaxybruce.component.app.BaseApplication
import com.galaxybruce.component.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
    }

    override fun bindLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(view: View?) {
        image.setOnClickListener { Toast.makeText(this, "I am a image", Toast.LENGTH_LONG).show() }
        Log.i("com.kidswant.ss-app", "isDebug: ${BaseApplication.isDebug()}" )

    }

    override fun bindData(savedInstanceState: Bundle?) {
    }

}
