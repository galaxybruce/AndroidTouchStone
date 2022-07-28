package com.galaxybruce.component.internal;

import android.app.Application;

/**
 * @author bruce.zhang
 * @date 2021/12/13 11:48
 * @description 内部调用api，Application中初始化
 * <p>
 * modification history:
 */
public interface IAppInternal {

    Application getApplication();

    boolean isDebugMode();

    IAuthAccount getAuthAccount();

    IHttpRequestOptions getHttpRequestOptions();

    /**
     * app是否必须登录才能使用
     * @return
     */
    boolean mustLogin();
}
