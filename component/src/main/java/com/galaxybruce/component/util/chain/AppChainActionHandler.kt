package com.galaxybruce.component.util.chain

/**
 * @date 2023/6/22 23:05
 * @author bruce.zhang
 * @description 一系列链表行为处理
 *
 * 使用场景：
 * 1. 一些列有依赖关系的行为
 * 2. 一个行为依赖N个没有关系的行为，比如初始化购物车前是否录入会员、是否录入营业员、是否输入备用金
 *
 * 使用案例：
 * AppChainActionHandler<Unit, Unit>().apply {
 *     this.add(Action1(this@MainActivity) { action1Result ->
 *         ToastUtils.showToast(this@MainActivity, "action1 ${action1Result}")
 *     })
 *     this.add(Action2())
 *     this.add(Action3())
 *     this.add(Action4())
 * }.action(Unit, AppChainActionResult<Unit>()) { result ->
 *     AppLogUtils.i("执行目标任务")
 * }
 *
 * <p>
 * modification history:
 */
class AppChainActionHandler<T,R> {
    private var _first: AppChainAction<T, R>? = null
    private var _last: AppChainAction<T, R>? = null

    fun add(chain: AppChainAction<T, R>) {
        if (_first == null) {
            _first = chain
            _last = chain
            return
        }
        _last!!.next = chain
        _last = chain
    }

    /**
     * 开始执行
     * @param request 一些列行为依赖的共同的参数对象，如果不需要参数，传Unit
     * @param result 链式任务返回结果，如果不需要返回接口，传AppChainActionResult<Unit>()
     * @param callback 所有行为执行完成后的回调。一般来说，这个callback就是等所以依赖的行为
     * 执行完毕后再执行；也可以把callback中要执行的内容封装成一个ChainAction，这样callback中
     * 就不需要再做任何东西。
     *
     * action中可以做各种形式的行为，比如显示dialog、发送网络请求等，任务执行完成后调用
     * next?.action(request, result, callback) ?: callback.invoke(result)
     */
    fun action(request: T,
               result: AppChainActionResult<R> = AppChainActionResult(),
               callback: (AppChainActionResult<R>) -> Unit) {
        _first?.action(request, result, callback)
    }
}