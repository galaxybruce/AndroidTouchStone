package com.galaxybruce.component.internal;

public interface IAuthAccount {

    String getUid();

    /**
     * 用户手机号
     */
    String getPhone();

    /**
     * 用户名称
     */
    String getName();

    /**
     * 用户头像
     */
    String getAvatar();

}
