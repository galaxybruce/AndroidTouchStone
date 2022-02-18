package com.galaxybruce.component.ui.jetpack.binding_adapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.ClickUtils
import com.galaxybruce.component.ui.jetpack.JPListDataModel
import com.galaxybruce.component.ui.view.recyclerview.AppRecyclerView2
import com.galaxybruce.component.util.imageloader.AppImageLoaderUtilWrapper


/**
 * @date 2020/8/12  3:27 PM
 * @author
 * @description 自定义 BindingAdapter
 *
 *  转化位java 所以默认值无效， 非空参数会转化为 0， false null
 *
 *  可空参数，  null
 * <p>
 * modification history:
 */
object JPCommonBindingAdapter {

    /**
     * @param placeholder binding_placeholder="@{com.galaxybruce.main.R.drawable.app_transparent_drawable}"
     */
    @BindingAdapter(value = ["binding_image_original_target", "binding_displayOriginalImage", "binding_placeholder"], requireAll = false)
    @JvmStatic
    fun displayOriginalImage(view: ImageView, target: Any?, url: String?, placeholder: Int = 0) {
        AppImageLoaderUtilWrapper.displayOriginalImage(target, url, view, placeholder, null)
    }

    @BindingAdapter(value = ["binding_image_screen_size_target", "binding_displayScreenSizeImage", "binding_placeholder"], requireAll = false)
    @JvmStatic
    fun displayScreenSizeImage(view: ImageView, target: Any?, url: String?, placeholder: Int = 0) {
        AppImageLoaderUtilWrapper.displayScreenSizeImage(target, url, view, placeholder, null)
    }

    @BindingAdapter(value = ["binding_image_small_target", "binding_displaySmallImage", "binding_placeholder"], requireAll = false)
    @JvmStatic
    fun displaySmallImage(view: ImageView, target: Any?, url: String?, placeholder: Int = 0) {
        AppImageLoaderUtilWrapper.displaySmallImage(target, url, view, placeholder, null)
    }

    @BindingAdapter(value = ["binding_image_middle_target", "binding_displayMiddleImage", "binding_placeholder"], requireAll = false)
    @JvmStatic
    fun displayMiddleImage(view: ImageView, target: Any?, url: String?, placeholder: Int = 0) {
        AppImageLoaderUtilWrapper.displayMiddleImage(target, url, view, placeholder, null)
    }

    @BindingAdapter(value = ["binding_selected"])
    @JvmStatic
    fun setViewSelected(view: View, isSelected: Boolean = false) {
        view.isSelected = isSelected
    }

    @BindingAdapter(value = ["binding_enabled"])
    @JvmStatic
    fun setViewEnabled(view: View, isEnabled: Boolean = false) {
        view.isEnabled = isEnabled
    }

    @BindingAdapter(value = ["binding_onClickWithDebouncing"], requireAll = false)
    @JvmStatic
    fun onClickWithDebouncing(view: View?, clickListener: View.OnClickListener) {
        ClickUtils.applySingleDebouncing(view, 500, clickListener)
    }

    @BindingAdapter(value = ["binding_setRecyclerViewListData"], requireAll = false)
    @JvmStatic
    fun setAppRecyclerViewData(recyclerview: AppRecyclerView2<Any>, listData: JPListDataModel?){
        listData?.takeIf { it.isAvailable }?.let {
            if(it.isError) {
                recyclerview.bbsExecuteListener.executeOnLoadDataError(null)
            } else {
                recyclerview.bbsExecuteListener.executeOnLoadDataSuccess(it.list)
            }
            recyclerview.bbsExecuteListener.executeOnLoadFinish()
        }
    }

    @BindingAdapter(value = ["binding_notifyRecyclerViewListChanged"], requireAll = false)
    @JvmStatic
    fun notifyAppRecyclerViewListChanged(recyclerView: AppRecyclerView2<Any>, notify: Boolean) {
        if (notify) {
            recyclerView.notifyAdapterDataSetChanged()
        }
    }

}