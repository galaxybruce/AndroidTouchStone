package com.galaxybruce.component.net.model;


import com.galaxybruce.component.proguard.IProguardKeeper;

/**
 * @date 2021/12/13 18:01
 * @author bruce.zhang
 * @description 网络响应解析基础类
 * <p>
 * modification history:
 */
public interface IAppBean extends IProguardKeeper {
    /**
     * 成功请求
     *
     * @return boolean
     */
    boolean isSuccessful();

    /**
     * 错误信息
     *
     * @return message
     */
    String getMessage();

    /**
     * 请求返回的Code
     *
     * @return code
     */
    String getCode();

    /**
     * 是否登录过期
     */
    boolean isExpireLogin();

    void setMessage(String message);

    void setCode(String code);
}
