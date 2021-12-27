package com.galaxybruce.component.ui.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.ToastUtils
import com.galaxybruce.component.app.BaseApplication
import com.galaxybruce.component.ui.ILogin
import com.galaxybruce.component.ui.IUiDataProvider
import com.galaxybruce.component.ui.IUiInit
import com.galaxybruce.component.ui.IUiView
import com.galaxybruce.component.ui.dialog.AppDialogFragment
import com.galaxybruce.component.ui.dialog.AppLoadingDialog
import com.galaxybruce.component.ui.fragment.BaseFragment
import java.lang.Exception
import java.util.*

/**
 * @date 2019-06-21 17:29
 * @author bruce.zhang
 * @description
 *
 * modification history:
 */
abstract class BaseActivity : AppCompatActivity(), IUiInit, IUiView, ILogin, IUiDataProvider {

    protected lateinit var mActivity: Activity
    protected var mContentView: View? = null
    protected var mPreviousDialog: AppDialogFragment? = null

    /** 和页面相关的缓存，比如生命周期相关的代理 */
    private val mCache: Map<String, Any> by lazy {
        HashMap<String, Any>(4)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mActivity = this
        addWindowFeatures()
        applyStyle2ActivityTheme()

        super.onCreate(savedInstanceState)

        setRootLayout(bindLayoutId())
        initData(intent.extras, savedInstanceState)
        initView(mContentView)
        initStatusBar()
        if(checkLogin()) {
            bindData(savedInstanceState)
        }
    }

    open fun setRootLayout(layoutId: Int) {
        if (layoutId <= 0) return
        mContentView = LayoutInflater.from(this).inflate(layoutId, null)
        setContentView(mContentView)
    }

    /**
     * 添加window窗口特征
     * 如设置全屏：window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)//remove notification bar  即全屏
     *
     * @return boolean 是否有标题栏
     */
    open fun addWindowFeatures() {
//        BBSWindowUtil.setScreenPortrait(this);
    }

    open fun applyStyle2ActivityTheme() {
//        AppAttrResolveUtil.applyStyle2ActivityTheme(this, false)
    }

    open fun initStatusBar() {

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

    /**
     * 如果是tab activity，这里返回当前选中的fragment
     * @return
     */
    open fun getCurrentFragment(): Fragment? {
        return null
    }

    override fun showLoadingProgress(message: String?) {
        showLoadingDialog(AppLoadingDialog.getInstance(message))
    }

    override fun showToast(message: String?) {
        runOnUiThread {
            ToastUtils.showShort(message)
        }
    }

    override fun hideLoadingProgress() {
        if (mPreviousDialog != null) {
            mPreviousDialog!!.dismissAllowingStateLoss()
            mPreviousDialog = null
        }
    }

    private fun showLoadingDialog(dialog: AppDialogFragment) {
        try {
            if (mPreviousDialog != null) {
                mPreviousDialog!!.dismissAllowingStateLoss()
                mPreviousDialog = null
            }
            if (!(dialog.isAdded || isFinishing)) {
                dialog.show(supportFragmentManager, null)
                mPreviousDialog = dialog
            }
        } catch (e: Exception) {
        }
    }

    override fun login() {

    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
    }

    override fun initView(view: View?) {
    }

    override fun bindData(savedInstanceState: Bundle?) {
    }

    override fun finishAllActivity() {
        BaseApplication.instance.finishAllActivity()
    }
}