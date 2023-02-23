package com.galaxybruce.touchstone.task;

import android.app.Application;

import com.billy.cc.core.component.CC;
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
        initCC();
    }

    private void initLog() {
        AppLogUtils.init(BuildConfig.DEBUG, null);
    }

    /**
     * 初始化组件间通信库
     */
    private void initCC() {
        ////////////////////////////////////////////////////////////////////////////////////////////
        // 半自动注册使用方法：
        //  1. 需要注册的组件的module/build.gradle中添加
        //        manifestPlaceholders = [
        //                "MODULE_NAME"                  : "${project.getName()}"
        //        ]
        //  2. 创建模块内注册类：
        //  如demo中的com.billy.cc.demo.register.DemoCCRegister
        //  3. 在module/AndroidManifest.xml中把第二步创建的类注册到meta-data中
        //  key都是一样的
        //  <meta-data
        //    android:name="com.galaxybruce.component.interface.${MODULE_NAME}"
        //    android:value="com.billy.cc.demo.register.DemoCCRegister"/>
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        CC.enableVerboseLog(BuildConfig.DEBUG);
        CC.enableDebug(BuildConfig.DEBUG);
        CC.enableRemoteCC(false);
    }

}
