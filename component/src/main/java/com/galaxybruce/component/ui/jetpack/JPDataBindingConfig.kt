package com.galaxybruce.component.ui.jetpack

import android.util.SparseArray

/**
 * @date 2020/8/11  10:14 AM
 * @author
 * @description databind 配置
 * <p>
 * modification history:
 */
data class JPDataBindingConfig(val layout: Int) {
    var bindingParams = SparseArray<Any>()

    fun addBindingParam(variableId: Int, obj: Any): JPDataBindingConfig {
        if(bindingParams.get(variableId) == null) {
            bindingParams.put(variableId, obj)
        }
        return this
    }
}