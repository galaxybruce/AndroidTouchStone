package com.galaxybruce.component.util.debug

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.ScreenUtils
import com.galaxybruce.component.BR
import com.galaxybruce.component.R
import com.galaxybruce.component.app.crash.AppCrashHandler
import com.galaxybruce.component.databinding.AppDebugLogDialogBinding
import com.galaxybruce.component.databinding.AppDebugLogDialogItemLayoutBinding
import com.galaxybruce.component.ui.activity.BaseActivity
import com.galaxybruce.component.ui.jetpack.JPBaseFragment
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel
import com.galaxybruce.component.ui.jetpack.JPDataBindingConfig
import com.galaxybruce.component.ui.jetpack.adapter.JPRecyclerViewLoadMoreAdapter
import com.galaxybruce.component.ui.view.recyclerview.ItemsPositionHelper
import com.galaxybruce.component.util.cache.AppBigDataCacheManager

/**
 * @date
 * @author
 * @description 显示日志dialog
 *
 * 使用方式：
 * new AppDebugLogDialog.Builder()
 * .build().show(getSupportFragmentManager(), "AppDebugLogDialog");
 *
 * modification history:
 */
class AppDebugLogDialog : JPBaseFragment<AppDebugLogDialogBinding>() {

    companion object {
        fun show(activity: BaseActivity, clickView: View, show: () -> Boolean) {
            clickView.setOnClickListener(object: ClickUtils.OnMultiClickListener(5) {
                override fun onTriggerClick(v: View?) {
                    if (show()) {
                        val fragmentManager = activity.supportFragmentManager
                        val content = activity.findViewById<ViewGroup>(android.R.id.content)
                        content.addView(
                            FrameLayout(activity).apply {
                                this.id = R.id.app_fragment_container
                            },
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        )
                        fragmentManager.beginTransaction().replace(
                            R.id.app_fragment_container, AppDebugLogDialog(), "debug_log_fragment"
                        )
                            .commitAllowingStateLoss()
                    }
                }

                override fun onBeforeTriggerClick(v: View?, count: Int) {
                }
            })
        }

        fun hide(activity: BaseActivity) {
            val fragmentManager = activity.supportFragmentManager
            val fragment = fragmentManager.findFragmentByTag("debug_log_fragment")
            fragment?.let {
                fragmentManager.beginTransaction().remove(fragment)
                    .commitAllowingStateLoss()
            }
            activity.findViewById<ViewGroup>(android.R.id.content).let {
                it.removeView(it.findViewById(R.id.app_fragment_container))
            }
        }
    }

    private lateinit var mPageViewModel: AppDebugLogDialogViewModel

    override fun initViewModel(): JPBaseViewModel {
        mPageViewModel = getFragmentViewModel(AppDebugLogDialogViewModel::class.java)
        return mPageViewModel
    }

    override fun initDataBindConfig(): JPDataBindingConfig {
        return JPDataBindingConfig(bindLayoutId())
            .addBindingParam(BR.vm, mPageViewModel)
            .addBindingParam(BR.click, ClickProxy())
    }

    override fun bindLayoutId(): Int {
        return R.layout.app_debug_log_dialog
    }

    override fun initView(view: View?) {
        super.initView(view)

        binding.recyclerView.layoutManager = LinearLayoutManager(mActivity)
        binding.recyclerView.adapter = InnerAdapter(mActivity).apply {
            this.data = mPageViewModel.listData
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
        setLiveDataObserver(AppDebugLogManager.newLog) { s ->
            s?.let {
                val listData = mPageViewModel.listData
                listData.add(s)
                binding.recyclerView.adapter!!.notifyItemInserted(listData.size - 1)
                val lastVisibleItem = ItemsPositionHelper.getLastVisiblePosition(binding.recyclerView)
                if (binding.recyclerView.adapter!!.itemCount - 1 == lastVisibleItem) {
                    binding.recyclerView.scrollToPosition(listData.size - 1)
                }
            }
        }

        mPageViewModel.listData.addAll(AppDebugLogManager.logList)
        binding.recyclerView.adapter!!.notifyDataSetChanged()
    }

    /**
     * 页面事件类，可以把所有事件都写在这里
     */
    inner class ClickProxy {

        fun expandCollapseEvent() {
            if(binding.tvExpandCollapse.text == "expand") {
                binding.tvExpandCollapse.text = "collapse"
                binding.menuLayout.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.VISIBLE
            } else {
                binding.tvExpandCollapse.text = "expand"
                binding.menuLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
            }
        }

        fun showDeviceInfoEvent() {
            val screenWidth = ScreenUtils.getAppScreenWidth()
            val screenHeight = ScreenUtils.getAppScreenHeight()
            val density = ScreenUtils.getScreenDensity()
            val densityDpi = ScreenUtils.getScreenDensityDpi()
            AppDebugLogManager.pushLog("屏幕分辨率：$screenWidth x $screenHeight")
            AppDebugLogManager.pushLog("屏幕密度：$density - $densityDpi")
        }

        @SuppressLint("CheckResult")
        fun showCrashLogEvent() {
            AppBigDataCacheManager.loadCacheStringAsync(null, AppCrashHandler.APP_CRASH_LOG_KEY, false)
                .subscribe({
                    AppDebugLogManager.pushLog("crash日志：\n\n $it")
                }, {

                })
        }

        fun closeDebugEvent() {
            hide(context as BaseActivity)
        }

        fun clearLogEvent() {
            mPageViewModel.listData.clear()
            binding.recyclerView.adapter?.notifyDataSetChanged()
            AppDebugLogManager.clear()
        }
    }

    private inner class InnerAdapter(context: Context) : JPRecyclerViewLoadMoreAdapter<String>(context) {
        private val VIEW_TYPE_XXX = 0x2001f

        override fun getRealItemViewType(position: Int): Int {
            return VIEW_TYPE_XXX
        }

        override fun getLayoutId(viewType: Int): Int {
            return if (viewType == VIEW_TYPE_XXX) {
                R.layout.app_debug_log_dialog_item_layout
            } else super.getLayoutId(
                viewType
            )
        }

        override fun onBindItem(binding: ViewDataBinding, dataPosition: Int) {
            if (binding is AppDebugLogDialogItemLayoutBinding) {
                val itemInfo = data[dataPosition]
                binding.vm = itemInfo
            }
        }
    }
}