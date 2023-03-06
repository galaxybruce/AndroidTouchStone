package com.galaxybruce.component.ui.activity

import com.galaxybruce.component.util.AppConstants
import com.galaxybruce.component.util.AppConstants.TitleMode

/**
 * @date 2023/3/6 16:45
 * @author bruce.zhang
 * @description 标题栏信息
 *
 * modification history:
 */
class AppTitleInfo constructor(
    @TitleMode
    var titleMode: Int,
    var title: String?,
    var showHomeAsUp: Boolean,
){

    companion object {
        @JvmStatic
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        @TitleMode
        var titleMode: Int = AppConstants.TITLE_MODE_LINEAR

        var title: String? = null

        var showHomeAsUp: Boolean = true

        fun build() = AppTitleInfo(titleMode, title, showHomeAsUp)
    }
}