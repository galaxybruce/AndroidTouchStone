package com.galaxybruce.component.interal;

import android.app.Application;

/**
 * @author bruce.zhang
 * @date 2021/12/13 11:48
 * @description 内部调用api，Application中初始化
 * <p>
 * modification history:
 */
public class AppInternal implements IAppInternal {

    private AppInternal() {
    }

    private static class InternalHolder {
        static AppInternal instance = new AppInternal();
    }

    public static AppInternal getInstance() {
        return InternalHolder.instance;
    }

    private IAppInternal appInternal;
    private IAuthAccount authAccount;
    private IHttpRequestOptions httpRequestOptions;

    public void setAppInternal(IAppInternal var) {
        appInternal = var;
    }

    @Override
    public Application getApplication() {
        if(appInternal != null) {
            return appInternal.getApplication();
        }
        return null;
    }

    @Override
    public boolean isDebugMode() {
        if(appInternal != null) {
            return appInternal.isDebugMode();
        }
        return false;
    }

    @Override
    public IAuthAccount getAuthAccount() {
        if(appInternal != null & authAccount == null) {
            IAuthAccount temp = appInternal.getAuthAccount();
            if(temp != null) {
                authAccount = temp;
            }
        }
        return authAccount;
    }

    @Override
    public IHttpRequestOptions getHttpRequestOptions() {
        if(appInternal != null & httpRequestOptions == null) {
            IHttpRequestOptions temp = appInternal.getHttpRequestOptions();
            if(temp != null) {
                httpRequestOptions = temp;
            }
        }
        return httpRequestOptions;
    }
}
