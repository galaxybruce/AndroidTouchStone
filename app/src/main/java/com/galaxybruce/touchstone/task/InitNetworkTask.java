package com.galaxybruce.touchstone.task;

import android.app.Application;

import com.effective.android.anchors.Process;
import com.effective.android.anchors.Task;
import com.galaxybruce.component.net.AppServiceGenerator;
import com.galaxybruce.component.proguard.IProguardKeeper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * @date 2020/9/7 09:44
 * @author bruce.zhang
 * @description 网络请求初始化
 * <p>
 * modification history:
 */
//@TaskAnchor
public class InitNetworkTask extends Task implements IProguardKeeper {

    public InitNetworkTask() {
        super(false, Process.ALL);
        setPriority(10);
    }

    @Override
    protected void run(String name, Application application) {
        // 添加自定义的interceptor
        List<Interceptor> list = new ArrayList<>(1);
//        list.add(new RequestReportInterceptor());
        AppServiceGenerator.addExternalInterceptors(list);
    }


}
