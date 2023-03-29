package com.galaxybruce.touchstone.app

import com.effective.android.anchors.AnchorsManager
import com.effective.android.anchors.Project
import com.galaxybruce.component.app.BaseApplication
import com.galaxybruce.component.util.AppConstants
import com.galaxybruce.touchstone.BuildConfig
import com.galaxybruce.touchstone.task.InitTaskFactory
import com.galaxybruce.unimp.UniMPInitializer

/**
 * @date 2019-06-22 10:41
 * @author bruce.zhang
 * @description
 *
 * modification history:
 */

class AppContext : BaseApplication() {

    override fun onPreCreate() {
        DEBUG = BuildConfig.DEBUG
        DEBUG_ABLE = BuildConfig.DEBUG_ABLE
        AppConstants.DESIGN_UI_WIDTH = 750
    }

    override fun initTask(isMainProcess: Boolean) {
        // uni小程序进程只需初始化小程序sdk，其他sdk不要初始化
        if(UniMPInitializer.isUniMPProcess(this)) {
            UniMPInitializer.initUniMPProcess(this.applicationContext)
            return
        }
        // 其他进程根据实际需要来决定是否要初始化小程序sdk，主进程肯定是需要的
        if(isMainProcess) {
            UniMPInitializer.initHostProcess(this.applicationContext)
        }

        // 启动任务初始化，建议以后都采用这种方式
        val initTaskFactory = InitTaskFactory(null)
        val builder = Project.Builder(InitTaskFactory.PROJECT, initTaskFactory)
        builder.add(InitTaskFactory.TASK_NETWORK)
        builder.add(InitTaskFactory.TASK_NECESSARY_ALL_PROCESS).dependOn(InitTaskFactory.TASK_NETWORK)
        builder.add(InitTaskFactory.TASK_NECESSARY_MAIN_PROCESS).dependOn(InitTaskFactory.TASK_NECESSARY_ALL_PROCESS)
        builder.add(InitTaskFactory.TASK_ASYNC_GROUP).dependOn(InitTaskFactory.TASK_NECESSARY_MAIN_PROCESS)
        val project = builder.build()

        AnchorsManager.instance()
            .setApplication(this)
            .debuggable(BuildConfig.DEBUG)
            .addAnchors(
                InitTaskFactory.TASK_NECESSARY_MAIN_PROCESS
            )
            .start(initTaskFactory, project)
    }

//    override fun checkPrivacyPolicy(): Boolean {
//        AppPrivacyUtil.NEED_CHECK_PRIVACY = false
//        return true
//    }
}