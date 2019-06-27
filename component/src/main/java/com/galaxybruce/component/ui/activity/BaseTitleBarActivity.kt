package com.galaxybruce.component.ui.activity

import android.view.LayoutInflater
import android.view.MenuItem
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.galaxybruce.component.R
import kotlinx.android.synthetic.main.activity_title.*


abstract class BaseTitleBarActivity : BaseActivity() {

    override fun setRootLayout(layoutId: Int) {
        super.setRootLayout(bindTitleLayoutId())
        if (layoutId > 0) {
            LayoutInflater.from(this).inflate(layoutId, baseContentLayout)
        }
        setSupportActionBar(baseToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.colorPrimaryDark))
        BarUtils.addMarginTopEqualStatusBarHeight(baseRootLayout)

        supportActionBar?.title = bindTitle()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    open fun bindTitleLayoutId(): Int {
        return if(getTitleOverLabMode()) R.layout.activity_title_overlap else R.layout.activity_title
    }

    open fun getTitleOverLabMode(): Boolean {
        return false
    }

    abstract fun bindTitle(): CharSequence
}