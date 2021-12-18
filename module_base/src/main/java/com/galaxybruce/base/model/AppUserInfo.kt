package com.galaxybruce.base.model

import com.galaxybruce.component.proguard.IProguardKeeper

/**
 * @date 2021/4/22 22:24
 * @author
 * @description 用户信息
 *
 * modification history:
 */

data class AppUserInfo(
    var uid: String? = null,
    var name: String? = null,
    var avatar: String? = null,
    var sex: String? = null,
): IProguardKeeper {

}
