package com.galaxybruce.component.ui.jetpack.util

import java.lang.reflect.ParameterizedType

/**
 * @date 2023/6/18 16:19
 * @author bruce.zhang
 * @description ViewMode相关工具类
 *
 * modification history:
 */

/**
 * 获取当前UI(activity|fragment|dialogFragment)对应的ViewMode类
 */
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}