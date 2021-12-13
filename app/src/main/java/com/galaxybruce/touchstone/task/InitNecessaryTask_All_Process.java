package com.galaxybruce.touchstone.task;

import android.app.Application;

import com.effective.android.anchors.Process;
import com.effective.android.anchors.Task;
import com.galaxybruce.component.proguard.IProguardKeeper;
import com.galaxybruce.component.util.log.AppLogUtils;
import com.galaxybruce.touchstone.BuildConfig;

/**
 * @date 2020/9/7 09:44
 * @author bruce.zhang
 * @description
 *  小的同步任务组合，在Application.onCreate()必须初始化完成的task！！！
 *  该任务只在 所有 进程中运行！！！
 *  该任务只在 所有 进程中运行！！！
 *  该任务只在 所有 进程中运行！！！
 * <p>
 * modification history:
 */
//@TaskAnchor
public class InitNecessaryTask_All_Process extends Task implements IProguardKeeper {

    public InitNecessaryTask_All_Process() {
        super(false, Process.ALL);
        setPriority(10);
    }

    @Override
    protected void run(String name, Application application) {
        initLog();
    }

    private void initLog() {
        AppLogUtils.init(BuildConfig.DEBUG, null);
    }

}
