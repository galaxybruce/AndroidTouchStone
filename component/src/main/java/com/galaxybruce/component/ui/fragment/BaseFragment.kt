package com.galaxybruce.component.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.galaxybruce.component.ui.IUiDataProvider
import com.galaxybruce.component.ui.IUiInit
import com.galaxybruce.component.ui.IUiRequest
import java.util.HashMap

/**
 * @date 2019-06-21 19:22
 * @author bruce.zhang
 * @description
 *
 * modification history:
 */
abstract class BaseFragment : Fragment(), IUiInit, IUiRequest, IUiDataProvider {

    protected lateinit var mActivity: Activity
    protected lateinit var mInflater: LayoutInflater

    /** 和页面相关的缓存，比如生命周期相关的代理 */
    private lateinit var mCache: Map<String, Any>

    /** view是否创建  */
    protected var mViewInit: Boolean = false
    /** 数据第一次加载过  */
    protected var mFirstLoad = true

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mInflater = inflater
        var rootView = view
        if (rootView == null) {
            rootView = setRootLayout(bindLayoutId())
        } else {
            val parent = rootView.parent
            if (parent is ViewGroup) {
                parent.removeView(rootView)
            }
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(arguments, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView(view)
        mViewInit = true
        bindData(savedInstanceState)
    }

    override fun onDestroyView() {
        (view?.parent as? ViewGroup)?.removeView(view)
        super.onDestroyView()
    }

    open fun setRootLayout(layoutId: Int): View {
        return mInflater.inflate(layoutId, null)
    }

    fun <T : View> findViewById(@IdRes id: Int): T? {
        return view?.findViewById(id)
    }

    fun runOnUiThread(action: Runnable, delay: Long = 0) {
        activity?.window?.decorView?.postDelayed(action, delay)
//        activity?.window?.decorView?.postDelayed(action, delay) ?: return
//        activity?.window?.decorView?.let { it.postDelayed(action, delay) }
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val manager: FragmentManager = childFragmentManager
        val fragments: List<Fragment>? = manager.fragments
        if (fragments?.isEmpty() == false) {
            for (fragment in fragments) {
                if (fragment is BaseFragment && fragment.onKeyDown(keyCode, event)) {
                    return true
                }
            }
        }
        return false
    }

    override fun provideCache(): Map<String, Any> {
        if (!this::mCache.isInitialized) {
            mCache = HashMap(4)
        }
        return mCache
    }

    override fun provideContext(): Context {
        return mActivity
    }

    override fun provideIdentifier(): Int {
        return hashCode()
    }
}