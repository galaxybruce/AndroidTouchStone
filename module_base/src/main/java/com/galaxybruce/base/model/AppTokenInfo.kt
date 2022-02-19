package com.galaxybruce.base.model

import com.galaxybruce.component.proguard.IProguardKeeper

/**
 * @date 2021/4/22 22:24
 * @author
 * @description token信息
 *
 * modification history:
 */

data class AppTokenInfo(
    var token: String? = null,        // token
): IProguardKeeper {

}
