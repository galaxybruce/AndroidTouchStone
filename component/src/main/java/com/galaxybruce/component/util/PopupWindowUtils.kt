package com.galaxybruce.component.util

import android.view.View
import com.blankj.utilcode.util.ScreenUtils

/**
 * @date 2024/4/16 16:59
 * @author bruce.zhang
 * @description (亲，我是做什么的)
 *
 * modification history:
 */
object PopupWindowUtils {

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * @param anchorView 呼出window的view
     * @param contentView  window的内容布局
     * @param alwaysShowUp 是否总是显示在anchorView上面
     * @return window显示的左上角的xOff,yOff坐标
     */
    fun calculatePopWindowPos(anchorView: View, contentView: View, alwaysShowUp: Boolean): IntArray {
        val windowPos = IntArray(2)
        val anchorLoc = IntArray(2)
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc)
        val anchorHeight = anchorView.height
        // 获取屏幕的高宽
        val screenWidth = ScreenUtils.getAppScreenWidth()
        val screenHeight = ScreenUtils.getAppScreenHeight()
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        // 计算contentView的高宽
        val windowHeight = contentView.measuredHeight
        val windowWidth = contentView.measuredWidth
        // 判断需要向上弹出还是向下弹出显示
        val isNeedShowUp = screenHeight - anchorLoc[1] - anchorHeight < windowHeight
        val contentViewLeft = Integer.min(
            Integer.max(0, anchorLoc[0] + (anchorView.width - windowWidth) / 2),
            screenWidth - windowWidth)
        windowPos[0] = contentViewLeft
        if (isNeedShowUp || alwaysShowUp) {
            windowPos[1] = anchorLoc[1] - windowHeight
        } else {
            windowPos[1] = anchorLoc[1] + anchorHeight
        }
        return windowPos
    }
}