package com.galaxybruce.component.util.permission

import android.Manifest
import android.content.Context
import android.os.Build
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.StringUtils
import com.galaxybruce.component.R
import com.galaxybruce.component.ui.dialog.AppConfirmDialog
import com.galaxybruce.component.util.AndroidSDKVersionUtils

/**
 * @date 2022/8/21 22:09
 * @author bruce.zhang
 * @description 权限处理帮助类，封装 [com.blankj.utilcode.util.PermissionUtils] 最常用的场景
 * <p>
 * modification history:
 */
object AppPermissionHelper {

    /**
     * 申请一类权限
     * @param permissions PermissionConstants.PermissionGroup 自定义的常量
     */
    @JvmStatic
    fun requestGroup(
        context: Context,
        callback: PermissionUtils.SimpleCallback,
        @PermissionConstants.PermissionGroup vararg permissions: String
    ) {
        request(context, callback, *permissions)
    }

    /**
     * 申请具体某个权限
     * @param permissions Manifest.permission.xxxx
     */
    @JvmStatic
    fun request(
        context: Context,
        callback: PermissionUtils.SimpleCallback,
        vararg permissions: String
    ) {
        PermissionUtils.permission(*permissions)
            .rationale { activity, shouldRequest -> showRationaleDialog(activity, shouldRequest) }
            .callback(object : PermissionUtils.SingleCallback {
                override fun callback(
                    isAllGranted: Boolean,
                    granted: MutableList<String>,
                    deniedForever: MutableList<String>,
                    denied: MutableList<String>
                ) {
                    if (isAllGranted) {
                        callback.onGranted()
                        return
                    }
                    if (deniedForever.isNotEmpty()) {
                        showOpenAppSettingDialog(context, callback::onDenied, null)
                        return
                    }
                    val activity = ActivityUtils.getActivityByContext(context)
                    if (activity != null) {
                        SnackbarUtils.with(activity.findViewById(android.R.id.content))
                            .setMessage(activity.getString(R.string.permission_denied_list,
                                permissions2String(denied)
                            ))
                            .showError(false)
                    }
                    callback.onDenied()
                }

                fun permissions2String(permissions: MutableList<String>): String {
                    if (permissions.isEmpty()) return "[]"
                    val sb: StringBuilder = StringBuilder()
                    for (permission in permissions) {
                        sb.append(", " + permission.substring(permission.lastIndexOf('.') + 1))
                    }
                    return "[${sb.substring(2)}]"
                }
            })
            .request()
    }

    @JvmStatic
    fun showRationaleDialog(
        context: Context,
        shouldRequest: PermissionUtils.OnRationaleListener.ShouldRequest
    ) {
        AppConfirmDialog.Builder()
            .setTitle(StringUtils.getString(R.string.dialog_text_alert_title))
            .setMessage(StringUtils.getString(R.string.permission_rationale_message))
            .setCallback(object: AppConfirmDialog.AppConfirmDialogCallback {
                override fun onConfirm() {
                    shouldRequest.again(true)
                }

                override fun onCancel() {
                    shouldRequest.again(false)
                }
            })
            .build()
            .show(context, null)
    }

    @JvmStatic
    fun showExplainDialog(
        context: Context,
        denied: List<String>,
        shouldRequest: PermissionUtils.OnExplainListener.ShouldRequest
    ) {
        AppConfirmDialog.Builder()
            .setTitle(StringUtils.getString(R.string.dialog_text_alert_title))
            .setMessage(context.getString(R.string.permission_explain_message, "$denied"))
            .setCallback(object: AppConfirmDialog.AppConfirmDialogCallback {
                override fun onConfirm() {
                    shouldRequest.start(true)
                }

                override fun onCancel() {
                    shouldRequest.start(false)
                }
            })
            .build()
            .show(context, null)
    }

    @JvmStatic
    fun showOpenAppSettingDialog(context: Context) {
        showOpenAppSettingDialog(context, object: () -> Unit {
            override fun invoke() {
            }
        }, null)
    }

    /**
     * showOpenAppSettingDialog重载方法，而不是给error指定默认值是空，
     * 是为了兼容以前java对showOpenAppSettingDialog(context: Context)方法的调用
     */
    @JvmStatic
    fun showOpenAppSettingDialog(context: Context, error: () -> Unit, onStartSetting: (() -> Unit)? = null) {
        AppConfirmDialog.Builder()
            .setTitle(StringUtils.getString(R.string.dialog_text_alert_title))
            .setMessage(StringUtils.getString(R.string.permission_denied_forever_message))
            .setCallback(object: AppConfirmDialog.AppConfirmDialogCallback {
                override fun onConfirm() {
                    PermissionUtils.launchAppDetailsSettings()
                    onStartSetting?.invoke()
                }

                override fun onCancel() {
                    error.invoke()
                }
            })
            .build()
            .show(context, null)
    }

    /**
     * 保存图片权限
     * android 10以及以上，使用MediaStore API保存图片，不需要申请权限。
     * android 10以下，是通过手动扫描的方式，先保存到sdcard，需要申请权限
     *
     * [Android存储权限适配与读写媒体文件](https://medium.com/@wanxiao1994/android%E5%AD%98%E5%82%A8%E6%9D%83%E9%99%90%E9%80%82%E9%85%8D%E4%B8%8E%E8%AF%BB%E5%86%99%E5%AA%92%E4%BD%93%E6%96%87%E4%BB%B6-5c2004a62dfa)
     */
    @JvmStatic
    fun requestSaveMedia(
        context: Context,
        callback: PermissionUtils.SimpleCallback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            request(context, callback, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            callback.onGranted()
        }
    }

    /**
     * 运行时通知权限 android 13及以上有
     * 目前在登录页 和 首页统一申请
     */
    @JvmStatic
    fun requestNotification(context: Context,
                            callback: PermissionUtils.SimpleCallback? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(AndroidSDKVersionUtils.hasTiramisu()) {
                if(callback == null) {
                    PermissionUtils.permission(Manifest.permission.POST_NOTIFICATIONS)
                        .request()
                } else {
                    request(context, callback, Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                callback?.onGranted()
            }
        } else {
            callback?.onGranted()
        }
    }
}