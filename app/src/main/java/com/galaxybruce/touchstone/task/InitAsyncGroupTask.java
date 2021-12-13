package com.galaxybruce.touchstone.task;

import android.app.Application;

import com.effective.android.anchors.Process;
import com.effective.android.anchors.Task;
import com.galaxybruce.component.proguard.IProguardKeeper;

/**
 * @author wujian
 * @date 2020/9/9  3:49 PM
 * @description 异步任务组 初始化，可以把一些没有被其他任务以来的异步任务放在这里
 *
 *  todo tip: 最好不要放在这里，这种方式是不可靠的，因为不清楚被依赖的业务执行时机
 * <p>
 * modification history:
 */
public class InitAsyncGroupTask extends Task implements IProguardKeeper {

    public InitAsyncGroupTask() {
        super(true, Process.MAIN);
        setPriority(5);
    }

    @Override
    protected void run(String name, Application application) {
    }

}
