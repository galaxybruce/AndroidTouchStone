package com.galaxybruce.component.ui.jetpack.binding_adapter

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.ClickUtils
import com.galaxybruce.component.ui.jetpack.JPListDataModel


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


//    @BindingAdapter(value = ["binding_image_original_target", "binding_displayOriginalImage", "binding_placeholder"], requireAll = false)
//    @JvmStatic
//    fun displayOriginalImage(view: ImageView, target: Any?, url: String?, placeholder: Int = 0) {
//        view.displayOriginalImage(url, placeholder,null)
//    }
//
//    @BindingAdapter(value = ["binding_image_screen_size_target", "binding_displayScreenSizeImage", "binding_placeholder"], requireAll = false)
//    @JvmStatic
//    fun displayScreenSizeImage(view: ImageView, target: Any?, url: String?, placeholder: Int = 0) {
//        view.displayScreenSizeImage(url)
//    }
//
//    @BindingAdapter(value = ["binding_image_small_target", "binding_displaySmallImage", "binding_placeholder"], requireAll = false)
//    @JvmStatic
//    fun displaySmallImage(view: ImageView, target: Any?, url: String?, placeholder: Int = 0) {
//        view.displaySmallImage(url, placeholder)
//    }
//
//    @BindingAdapter(value = ["binding_image_target", "binding_displayMiddleImage", "binding_placeholder"], requireAll = false)
//    @JvmStatic
//    fun displayMiddleImage(view: ImageView, target: Any?, url: String?, placeholder: Int = 0) {
//        BBSImageLoaderUtilWrapper.displayMiddleImage(target, url, view, placeholder)
//    }
//
//    @BindingAdapter(value = ["binding_selected"])
//    @JvmStatic
//    fun setViewSelected(view: View, isSelected: Boolean = false) {
//        view.setSelected { isSelected }
//    }
//
//    @BindingAdapter(value = ["binding_enabled"])
//    @JvmStatic
//    fun setViewEnabled(view: View, isEnabled: Boolean = false) {
//        view.isEnabled = isEnabled
//    }
//
//    @BindingAdapter(value = ["binding_visible", "binding_notGone"], requireAll = false)
//    @JvmStatic
//    fun visible(view: View, visible: Boolean, notGone: Boolean = false) {
//        view.visibility = if (visible) View.VISIBLE else if (notGone) View.INVISIBLE else View.GONE
//    }
//
//    @BindingAdapter(value = ["binding_enable"])
//    @JvmStatic
//    fun visible(view: View, isEnabled: Boolean) {
//        view.isEnabled = isEnabled
//    }
//
//    @BindingAdapter(value = ["binding_textNull", "binding_defaultText"], requireAll = false)
//    @JvmStatic
//    fun setTextNull(view: TextView, text: String?, defaultText: String?) {
//        view.textNull(text, defaultText)
//    }
//
//    @BindingAdapter(value = ["binding_onClickWithDebouncing"], requireAll = false)
//    @JvmStatic
//    fun onClickWithDebouncing(view: View?, clickListener: View.OnClickListener) {
//        ClickUtils.applySingleDebouncing(view, 500, clickListener)
//    }
//
//    @BindingAdapter(value = ["binding_bbsRecyclerViewListData"], requireAll = false)
//    @JvmStatic
//    fun updateBBSRecyclerViewData(recyclerview: BBSRecyclerView2<Any>, listData: JPListDataModel?){
//        listData?.takeIf { it.isAvailable }?.let {
//            if(it.isError) {
//                recyclerview.bbsExecuteListener.executeOnLoadDataError(null)
//            } else {
//                recyclerview.bbsExecuteListener.executeOnLoadDataSuccess(it.list)
//            }
//            recyclerview.bbsExecuteListener.executeOnLoadFinish()
//        }
//
//    }
//
//    @BindingAdapter(value = ["binding_notifyBBSRecyclerViewListChanged"], requireAll = false)
//    @JvmStatic
//    fun notifyBBSRecyclerViewListChanged(recyclerView: BBSRecyclerView2<Any>, notify: Boolean) {
//        if (notify) {
//            recyclerView.notifyAdapterDataSetChanged()
//        }
//    }

}