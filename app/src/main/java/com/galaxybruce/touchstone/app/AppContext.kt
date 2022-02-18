package com.galaxybruce.touchstone.app

import com.effective.android.anchors.AnchorsManager
import com.effective.android.anchors.Project
import com.galaxybruce.component.app.BaseApplication
import com.galaxybruce.touchstone.BuildConfig
import com.galaxybruce.touchstone.task.InitTaskFactory

/**
 * @date 2019-06-22 10:41
 * @author bruce.zhang
 * @description
 *
 * modification history:
 */

class AppContext : BaseApplication() {

    override fun onPreCreate() {
        BaseApplication.DEBUG = BuildConfig.DEBUG
        BaseApplication.DEBUG_ABLE = BuildConfig.DEBUG_ABLE
    }

    override fun initTask(isMainProcess: Boolean) {
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

    override fun checkPrivacyPolicy(): Boolean {
        //        return PrivacyUtil.checkPrivacyPolicy(this)
        return true
    }
}