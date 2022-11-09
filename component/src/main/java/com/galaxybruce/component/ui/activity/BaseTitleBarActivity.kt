package com.galaxybruce.component.ui.activity

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.galaxybruce.component.R
import com.galaxybruce.component.ui.jetpack.JPBaseActivityV2
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel

/**
 * @date 2022/11/9 22:41
 * @author bruce.zhang
 * @description 具有标题栏的Activity
 * <p>
 * modification history:
 */
abstract class BaseTitleBarActivity<B : ViewDataBinding, VM : JPBaseViewModel> : JPBaseActivityV2<B, VM>() {

    override fun setRootLayout(layoutId: Int) {
        val inflater = LayoutInflater.from(this)
        mContentView = inflater.inflate(bindTitleLayoutId(), null)
        setContentView(mContentView)
        mDataBinding = mJPPageDelegate.setRootLayout(layoutId, inflater,
            findViewById(R.id.content_layout), true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    open fun bindTitleLayoutId(): Int {
        return if(isTitleOverLabMode()) R.layout.activity_title_overlap else R.layout.activity_title
    }

    override fun initView(view: View?) {
        super.initView(view)

        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.colorPrimaryDark))
        BarUtils.addMarginTopEqualStatusBarHeight(findViewById(R.id.root_layout))
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            this.setDisplayHomeAsUpEnabled(true)
            this.title = bindTitle()
        }
    }

    open fun isTitleOverLabMode(): Boolean {
        return false
    }

    abstract fun bindTitle(): CharSequence
}