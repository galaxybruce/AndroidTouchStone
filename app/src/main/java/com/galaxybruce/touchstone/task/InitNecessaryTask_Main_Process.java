package com.galaxybruce.touchstone.task;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.effective.android.anchors.Process;
import com.effective.android.anchors.Task;
import com.galaxybruce.component.interal.AppInternal;
import com.galaxybruce.component.interal.IAppInternal;
import com.galaxybruce.component.interal.IAuthAccount;
import com.galaxybruce.component.proguard.IProguardKeeper;
import com.galaxybruce.touchstone.BuildConfig;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @date 2020/9/7 09:44
 * @author bruce.zhang
 * @description
 *  小的同步任务组合，在Application.onCreate()必须初始化完成的task！！！
 *  该任务只在 Main 进程中运行！！！
 *  该任务只在 Main 进程中运行！！！
 *  该任务只在 Main 进程中运行！！！
 * <p>
 * modification history:
 */
//@TaskAnchor
public class InitNecessaryTask_Main_Process extends Task implements IProguardKeeper {

    public InitNecessaryTask_Main_Process() {
        super(false, Process.MAIN);
        setPriority(10);
    }

    @Override
    protected void run(String name, Application application) {
        initBugly(application);
        initRxJavaExceptionHandler();
        initInternal(application);
        initRouter(application);
    }

    private void initBugly(Application application) {

    }

    private void initRxJavaExceptionHandler() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
//                CrashReport.postCatchedException(throwable);
            }
        });
    }

    private void initInternal(Application application) {
        AppInternal.getInstance().setAppInternal(new IAppInternal() {
            @Override
            public Application getApplication() {
                return application;
            }

            @Override
            public boolean isDebugMode() {
                return BuildConfig.DEBUG_ABLE;
            }

            @Override
            public IAuthAccount getAuthAccount() {
                return new IAuthAccount() {
                    @Override
                    public String getUid() {
                        return null;
                    }

                    @Override
                    public String getPhone() {
                        return null;
                    }

                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override
                    public String getAvatar() {
                        return null;
                    }
                };
            }
        });
    }

    private void initRouter(Application application) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);
    }

}
