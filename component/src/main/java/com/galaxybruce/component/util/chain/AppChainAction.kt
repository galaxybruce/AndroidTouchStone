package com.galaxybruce.component.util.chain

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
     * @param result 链式任务返回结果，如果不需要返回接口，传AppChainActionResult<Unit>()
     */
    abstract fun action(request: T,
                        result: AppChainActionResult<R>,
                        callback: (AppChainActionResult<R>) -> Unit)
}