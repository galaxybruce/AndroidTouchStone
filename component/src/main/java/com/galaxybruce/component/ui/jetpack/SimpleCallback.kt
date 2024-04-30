package com.galaxybruce.component.ui.jetpack

/**
 * @date 2024/3/28 10:06
 * @author bruce.zhang
 * @description 简单的接口回调
 *
 * modification history:
 */
typealias SuccessCallback<T> = (T) -> Unit

typealias ErrorCallback<T> = (T) -> Unit