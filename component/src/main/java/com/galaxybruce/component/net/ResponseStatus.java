package com.galaxybruce.component.net;


import com.galaxybruce.component.proguard.IProguardKeeper;

/**
 * @date 2019/3/20 10:59
 * @author bruce.zhang
 * @description 接口返回数据的状态
 * <p>
 * modification history:
 */
public interface ResponseStatus extends IProguardKeeper {

    boolean success();

    boolean reLogin();

    String getMessage();

    int getCode();

}
