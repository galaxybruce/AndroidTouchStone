package com.galaxybruce.component.util

import android.view.MotionEvent
import android.view.View
import com.blankj.utilcode.util.ScreenUtils

/**
 * @date 2023/7/10 17:43
 * @author bruce.zhang
 * @description
 *
 * modification history:
 */
object AppViewUtils {

    /**
     * 检查触摸事件是否在view范围内
     */
    fun checkViewBound(view: View, event: MotionEvent): Boolean {
        val location = intArrayOf(0, 0)
        view.getLocationInWindow(location)
        val left = location[0]
        val top = location[1]
        val bottom = top + view.height
        val right = left + view.width
        return event.x > left && event.x < right && event.y > top && event.y < bottom
    }

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