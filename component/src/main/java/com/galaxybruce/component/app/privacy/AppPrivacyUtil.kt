package com.galaxybruce.component.app.privacy

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Process
import android.text.TextUtils
import com.blankj.utilcode.util.ProcessUtils
import com.galaxybruce.component.app.BaseApplication
import com.galaxybruce.component.ui.activity.BaseActivity
import com.galaxybruce.component.util.crosssp.AppProcessSPHelper
import kotlin.system.exitProcess

/**
 * @date 2021/9/27 10:05
 * @author bruce.zhang
 * @description 隐私政策相关处理
 *
 * modification history:
 */
object AppPrivacyUtil {

    const val PRIVACY_STATUS_KEY = "privacy_policy_agreed_type"

    /**
     * 是否需要检查隐私政策，有的私有化部署app不用上应用市场，无需检查。
     */
    var NEED_CHECK_PRIVACY: Boolean = true

    fun getCurProcessName(context: Context): String? {
        val pid = android.os.Process.myPid()
        val activityManager: ActivityManager? = context
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcessInfos = activityManager?.runningAppProcesses
        if (runningAppProcessInfos != null) {
            for (appProcess in runningAppProcessInfos) {
                if (appProcess.pid == pid) {
                    return appProcess.processName
                }
            }
        }
        return null
    }

    /**
     * 隐私政策check
     * privacy_policy_agreed_type: 0: 默认状态 1-同意; 2-拒绝
     *
     * 其他解决方案：[通过拦截 Activity的创建 实现APP的隐私政策改造](https://juejin.cn/post/6990643611130363917)
     */
    fun checkPrivacyPolicy(application: Application,
                           privacyPolicyActivity: Class<*> = AppPrivacyPolicyActivity::class.java,
                           alertMsg: String? = null): Boolean {
        // 如果没有同意过隐私政策，则启动隐私政策进程，其他进程等待用户操作
        if (NEED_CHECK_PRIVACY &&
            AppProcessSPHelper.getInt(application, PRIVACY_STATUS_KEY, 0) == 0) {
            val currentProcess = ProcessUtils.getCurrentProcessName()
            if (TextUtils.equals(currentProcess, "${application.packageName}:privacyProcess")) {
                // 当前进程是隐私政策进程，直接返回，不执行任何初始化
                return false
            } else {
                // 当前进程不是隐私政策进程，启动隐私政策进程
                val intent = Intent()
                intent.setClass(application, privacyPolicyActivity)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("alertMsg", alertMsg)
                application.startActivity(intent)

                // 当前进程不是隐私政策进程，都得等待用户同意进程才能继续执行
                while (true) {
                    val privacyAgreed = AppProcessSPHelper.getInt(application, PRIVACY_STATUS_KEY, 0)
                    if (privacyAgreed == 1) { // 用户同意
                        break
                    } else if (privacyAgreed == 2) { // 用户拒绝
                        AppProcessSPHelper.save(application, PRIVACY_STATUS_KEY, 0)
                        // 这段代码在鸿蒙系统上有问题，已经移到SplashActivity.onCreate中
//                        registerActivityLifecycleCallbacks(object : KWSimpleActivityLifecycle() {
//                            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
//                                unregisterActivityLifecycleCallbacks(this)
//                                activity.finish()
//                                Process.killProcess(Process.myPid())
//                                exitProcess(0)
//                            }
//                        })
                        return false
                    } else { // 用户没操作
                        try {
                            Thread.sleep(200)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
        return true
    }

    /**
     * 在app启动的Activity.onCreate方法中执行
     */
    fun checkPrivacyInLaunchActivity(activity: BaseActivity): Boolean {
        // 没有同意隐私政策时，杀死进程
        if (NEED_CHECK_PRIVACY &&
            AppProcessSPHelper.getInt(activity, PRIVACY_STATUS_KEY, 0) != 1) {
            // 先回到桌面，在杀进程，因为不回到桌面，某些机型可能出现黑屏的情况
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(homeIntent)

            activity.finish()
            BaseApplication.instance.finishAllActivity()
            Process.killProcess(Process.myPid())
            exitProcess(0)
            return false
        }
        return true
    }
}