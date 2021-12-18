package com.galaxybruce.component.util;

import android.app.Activity;
import android.content.Intent;

/**
 * @author bruce.zhang
 * @date 2021/12/18 17:53
 * @description
 * <p>
 * modification history:
 */
public class ActivityUtil {

    /**
     * 处理启动Activity某些特殊情况
     * @param activity
     * @return true 外面直接return，不处理任何逻辑
     */
    public static boolean handleSplashActivity(Activity activity) {
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!activity.isTaskRoot()) {
            Intent intent = activity.getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    activity.finish();
                    return true;
                }
            }
        }

        //处理首次安装点击打开切到后台,点击桌面图标再回来重启的问题及通过应用宝唤起在特定条件下重走逻辑的问题
        if ((activity.getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            activity.finish();
            return true;
        }

        return false;
    }
}
