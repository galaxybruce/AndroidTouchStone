package com.galaxybruce.component.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * @author bruce.zhang
 * @date 2021/12/13 11:01
 * @description 常用常量
 * <p>
 * modification history:
 */
public class AppConstants {

    /**
     * 设计稿的尺寸-宽度，需在Application中初始化
     */
    public static int DESIGN_UI_WIDTH = 750;

    /**
     * 空字符串
     */
    public final static String EMPTY_STR = "";

    /**
     * 空json字符串
     */
    public final static String EMPTY_JSON = "{}";

    public interface IntentKeys {
        /**
         * 登录成功后跳转路由KEY
         */
        String KEY_LOGIN_SUCCESS_ROUTER = "KEY_LOGIN_SUCCESS_ROUTER";
    }

    /**
     * 标题栏类型-无标题栏
     */
    public final static int TITLE_MODE_NONE = 0;
    /**
     * 标题栏类型-垂直排列
     */
    public final static int TITLE_MODE_LINEAR = 1;
    /**
     * 标题栏类型-悬浮在布局上面
     */
    public final static int TITLE_MODE_FLOAT = 2;
    /**
     * 标题栏类型-自定义标题栏
     */
    public final static int TITLE_MODE_CUSTOM = 3;
    @IntDef({TITLE_MODE_NONE, TITLE_MODE_LINEAR, TITLE_MODE_FLOAT, TITLE_MODE_CUSTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TitleMode {
    }

}
