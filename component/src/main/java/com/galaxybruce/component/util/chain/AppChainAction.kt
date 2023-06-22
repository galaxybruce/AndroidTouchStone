package com.galaxybruce.component.util.chain

import com.galaxybruce.component.R

/**
 * @date 2023/6/22 23:05
 * @author bruce.zhang
 * @description 一系列链表行为
 * <p>
 * modification history:
 */
abstract class AppChainAction<T, R> {

    var next: AppChainAction<T, R>? = null

    /**
     * @param request  一些列行为依赖的共同的参数对象，如果不需要参数，传Unit
     */
    abstract fun action(request: T, callback: (R) -> Unit)
}