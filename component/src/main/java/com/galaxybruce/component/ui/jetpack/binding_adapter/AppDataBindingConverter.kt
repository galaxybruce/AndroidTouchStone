package com.galaxybruce.component.ui.jetpack.binding_adapter

import android.widget.EditText
import androidx.databinding.InverseMethod

/**
 * @date 2022/3/4 15:08
 * @author bruce.zhang
 * @description 双向绑定 数据转换
 *
 *  使用方法:
 *   <data>
 *      <import type="com.galaxybruce.base.util.AppDataBindingConverter" />
 *   </data>
 *
 *  android:text="@={AppDataBindingConverter.floatToString(edtWeight, vm.adjustResult.weight)}"
 *
 * modification history:
 */
object AppDataBindingConverter {

    @InverseMethod("stringToFloat")
    @JvmStatic
    fun floatToString(view: EditText, value: Float): String {
        return value.toString()
    }


    @JvmStatic
    fun stringToFloat(view: EditText, value: String): Float {
        return value.toFloatOrNull() ?: 0.0f
    }

    @InverseMethod("stringToInt")
    @JvmStatic
    fun intToString(view: EditText, value: Int): String {
        return value.toString()
    }


    @JvmStatic
    fun stringToInt(view: EditText, value: String): Int {
        return value.toIntOrNull() ?: 0
    }
}