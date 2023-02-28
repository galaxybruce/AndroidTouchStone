package com.galaxybruce.component.util.helper

import android.content.Context
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.StringUtils
import com.galaxybruce.component.R
import com.galaxybruce.component.ui.dialog.AppConfirmDialog

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
                        showOpenAppSettingDialog(context)
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
        AppConfirmDialog.Builder()
            .setTitle(StringUtils.getString(R.string.dialog_text_alert_title))
            .setMessage(StringUtils.getString(R.string.permission_denied_forever_message))
            .setCallback(object: AppConfirmDialog.AppConfirmDialogCallback {
                override fun onConfirm() {
                    PermissionUtils.launchAppDetailsSettings()
                }

                override fun onCancel() {

                }
            })
            .build()
            .show(context, null)
    }
}