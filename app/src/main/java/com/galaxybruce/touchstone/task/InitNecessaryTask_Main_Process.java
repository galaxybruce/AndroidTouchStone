package com.galaxybruce.touchstone.task;

import android.app.Application;

import com.effective.android.anchors.Process;
import com.effective.android.anchors.Task;
import com.galaxybruce.base.manager.AppSessionManager;
import com.galaxybruce.component.internal.AppInternal;
import com.galaxybruce.component.internal.IAppInternal;
import com.galaxybruce.component.internal.IAuthAccount;
import com.galaxybruce.component.internal.IHttpRequestOptions;
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
        initRouter(application);
        initInternal(application);
        initUserInfo();
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
                        return AppSessionManager.getInstance().getUserInfo().getUid();
                    }

                    @Override
                    public String getPhone() {
                        return AppSessionManager.getInstance().getUserInfo().getPhone();
                    }

                    @Override
                    public String getName() {
                        return AppSessionManager.getInstance().getUserInfo().getName();
                    }

                    @Override
                    public String getAvatar() {
                        return AppSessionManager.getInstance().getUserInfo().getAvatar();
                    }
                };
            }

            @Override
            public IHttpRequestOptions getHttpRequestOptions() {
                return null;
            }

            @Override
            public boolean mustLogin() {
                return false;
            }
        });
    }

    private void initRouter(Application application) {
//        if (BuildConfig.DEBUG) {
//            ARouter.openLog();
//            ARouter.openDebug();
//        }
//        ARouter.init(application);
    }

    private void initUserInfo() {
        // 如果用户是登录状态，就获取最新用户信息
        if(AppSessionManager.getInstance().isLogin()) {
            // todo 调接口获取用户最新信息
        }
    }

}
