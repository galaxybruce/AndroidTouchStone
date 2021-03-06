package com.galaxybruce.component.ui.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import com.galaxybruce.component.ui.IUiDataProvider
import com.galaxybruce.component.ui.IUiInit
import com.galaxybruce.component.ui.IUiRequest
import com.galaxybruce.component.ui.fragment.BaseFragment
import java.util.HashMap

/**
 * @date 2019-06-21 17:29
 * @author bruce.zhang
 * @description
 *
 * modification history:
 */

abstract class BaseActivity : AppCompatActivity(), IUiInit, IUiRequest, IUiDataProvider {

    protected lateinit var mActivity: Activity
    protected lateinit var mContentView: View

    /** 和页面相关的缓存，比如生命周期相关的代理 */
    private val mCache: Map<String, Any> by lazy {
        HashMap<String, Any>(4)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mActivity = this
        addWindowFeatures()
        super.onCreate(savedInstanceState)
        setRootLayout(bindLayoutId())
        initData(intent.extras, savedInstanceState)
        initView(mContentView)
        if(checkLogin()) bindData(savedInstanceState)
    }

    open fun setRootLayout(layoutId: Int) {
        if (layoutId <= 0) return
        mContentView = LayoutInflater.from(this).inflate(layoutId, null)
        setContentView(mContentView)
    }

    /**
     * 添加window窗口特征
     *
     * @return boolean 是否有标题栏
     */
    open fun addWindowFeatures() {
//        BBSWindowUtil.setScreenPortrait(this);
    }

    /**
     * 检测是否登录
     */
    open fun checkLogin(): Boolean {
        // todo 添加是否登录逻辑
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val manager: FragmentManager = supportFragmentManager
        val fragments: List<Fragment>? = manager.fragments
        if (fragments?.isEmpty() == false) {
            for (fragment in fragments) {
                if (fragment is BaseFragment && fragment.onKeyDown(keyCode, event)) {
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun provideCache(): Map<String, Any> {
        return mCache
    }

    override fun provideContext(): Context {
        return this
    }

    override fun provideIdentifier(): Int {
        return hashCode()
    }
}