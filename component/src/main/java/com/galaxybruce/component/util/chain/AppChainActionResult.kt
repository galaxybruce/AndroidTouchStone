package com.galaxybruce.component.util.chain

/**
 * @date 2023/6/23 08:57
 * @author bruce.zhang
 * @description 链式任务返回结果
 *
 * modification history:
 */
data class AppChainActionResult<R>(
    var success: Boolean = true,
    var errorMsg: String? = null,
    var data: R? = null
)