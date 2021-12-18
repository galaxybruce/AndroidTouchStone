package com.galaxybruce.component.ui.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.galaxybruce.component.ui.ILogin
import com.galaxybruce.component.ui.IUiDataProvider
import com.galaxybruce.component.ui.IUiInit
import com.galaxybruce.component.ui.IUiView
import com.galaxybruce.component.ui.activity.BaseActivity
import java.util.*

/**
 * @date 2019-06-21 19:22
 * @author bruce.zhang
 * @description
 *
 * modification history:
 */
abstract class BaseFragment : Fragment(), IUiInit, IUiView, ILogin, IUiDataProvider {

    protected lateinit var mActivity: Activity
    protected lateinit var mInflater: LayoutInflater

    /** 和页面相关的缓存，比如生命周期相关的代理 */
    private lateinit var mCache: Map<String, Any>

    /** view是否创建  */
    protected var mViewInited: Boolean = false
    protected var mDataLoaded: Boolean = false
    /**
     * fragment.mHidden状态是在FragmentManager.hideFragment方法中改变的
     * 被嵌套的fragment是无法通过传递修改的，所以需要另外维护一个
     * 嵌套fragment的可见性参考：https://juejin.cn/post/6844904022923706381
     */
    protected var mHidden: Boolean = false
    protected var isOnResume: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mInflater = inflater
        if (bindLayoutId() <= 0) {
            return super.onCreateView(inflater, container, savedInstanceState)
        }
        var rootView = view
        if (rootView == null) {
            rootView = setRootLayout(bindLayoutId(), inflater, container)
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
        mViewInited = true
        bindData(savedInstanceState)
    }

    override fun onDestroyView() {
        (view?.parent as? ViewGroup)?.removeView(view)
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        // 若当前是对于用户可见，且未onresume
        if (userVisibleHint && !isHidden && !isFragmentHidden() && !isOnResume) {
            isOnResume = true
        }

        onVisibleToUserChanged(isOnResume)
    }

    override fun onPause() {
        super.onPause()
        if (isOnResume) {
            isOnResume = false
        }

        onVisibleToUserChanged(isOnResume)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewInited = false
    }

    /**
     * 嵌套fragment的可见性参考：https://juejin.cn/post/6844904022923706381
     * @param isVisibleToUser
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) { //若要对用户可见
            if (!isOnResume) { //当前状态是是没有onresume
                isOnResume = true
            }
        } else { //若要对用户不可见
            if (isOnResume) { //当前状态是可见
                isOnResume = false
            }
        }
        onVisibleToUserChanged(isVisibleToUser)
    }

    /**
     * 嵌套fragment的可见性参考：https://juejin.cn/post/6844904022923706381
     * @param hidden
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mHidden = hidden
        if (!hidden) { //若要对用户可见
            if (!isOnResume) { //当前状态是是没有onresume
                isOnResume = true
            }
        } else { //若要对用户不可见
            if (isOnResume) { //当前状态是可见
                isOnResume = false
            }
        }
        onVisibleToUserChanged(!hidden)
    }

    /**
     * fragment.mHidden状态是在FragmentManager.hideFragment方法中改变的
     * 被嵌套的fragment是无法通过传递修改的，所以需要另外维护一个
     *
     * @return
     */
    open fun isFragmentHidden(): Boolean {
        return mHidden
    }

    open fun setRootLayout(layoutId: Int, inflater: LayoutInflater, container: ViewGroup?): View {
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

    /**
     * 设置沉浸式状态栏
     * 如果子类不需要沉浸式或者自己实现沉浸式，需要覆盖这个方法
     * activity如果嵌套fragment，在fragment设置就行，这个方法不实现
     *
     * BBSWindowUtil.setTranslucentStatusBar(this, Color.parseColor("#ffffff"));
     */
    protected open fun setTranslucentStatusBar() {}

    protected open fun onVisibleToUserChanged(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            if (mViewInited) {
                setTranslucentStatusBar()
            }
        }
        if (shouldDoLazyRequest(isVisibleToUser, mViewInited)) {
            mDataLoaded = true
            doLazyRequest()
        }
    }

    /**
     * 判断是否需要发送请求。
     * 默认只第一次可见时自动发送请求，如果业务上需要每次可见时都发送请求，请重新实现该方法
     *
     * return isVisibleToUser && mViewInited
     *
     * @param isVisibleToUser
     * @return
     */
    protected open fun shouldDoLazyRequest(isVisibleToUser: Boolean, mViewInited: Boolean): Boolean {
        return isVisibleToUser && mViewInited && !mDataLoaded
    }

    /**
     * 延迟发送请求，或者处理其他逻辑。
     * 如果业务上需要每次可见时都发送请求，请重新实现[BaseFragment.shouldDoLazyRequest]方法，
     * 在该方法中发送请求。
     */
    protected open fun doLazyRequest() {}

    override fun showToast(message: String?) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showToast(message)
        }
    }

    override fun showLoadingProgress(message: String?) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showLoadingProgress(message)
        }
    }

    override fun hideLoadingProgress() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).hideLoadingProgress()
        }
    }

    override fun login() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).login()
        }
    }

    override fun finishAllActivity() {
        activity.takeIf { it != null && !it.isFinishing && it is IUiView }?.let {
            (it as IUiView).finishAllActivity()
        }
    }

}