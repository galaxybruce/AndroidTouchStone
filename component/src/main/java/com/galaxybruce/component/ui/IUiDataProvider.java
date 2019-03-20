package com.galaxybruce.component.ui;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Map;

/**
 * @author bruce.zhang
 * @date 2018/11/13 16:46
 * @description
 *
 * <p>
 * modification history:
 */
public interface IUiDataProvider {

    /**
     * 提供在Activity或者Fragment生命周期内的缓存容器
     * 此缓存容器和 Activity或者Fragment的生命周期绑定，屏幕旋转的情况下回情况，需要另外处理
     * @return
     */
    @NonNull
    Map<String, Object> provideCache();

    /**
     * 提供页面Context
     * @return
     */
    Context provideContext();

    /**
     * 提供页面唯一识别符
     * @return
     */
    int provideIdentifier();
}
