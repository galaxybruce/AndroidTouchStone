package com.galaxybruce.touchstone

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.galaxybruce.component.app.BaseApplication
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
            Toast.makeText(this, "I am a image", Toast.LENGTH_LONG).show()
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
    }

    override fun getTitleOverLabMode(): Boolean {
        return true
    }

}
