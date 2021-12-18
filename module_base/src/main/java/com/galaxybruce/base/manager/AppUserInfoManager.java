package com.galaxybruce.base.manager;

import android.text.TextUtils;

import com.galaxybruce.base.model.AppUserInfo;
import com.galaxybruce.component.app.BaseApplication;
import com.galaxybruce.component.util.cache.AppFileCacheManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * @date 2021/4/22 23:44
 * @author 
 * @description 用户信息管理
 * <p>
 * modification history:
 */
public class AppUserInfoManager {

    private static final String KEY_FILE_NAME = "FILE_USER_INFO";

    /**
     * app退出方式：0表示没执行退出操作，1表示正常退出，2表示切换账号
     */
    interface AppExitType {
        int EXIT_TYPE_NONE = 0;
        int EXIT_TYPE_NORMAL = 1;
        int EXIT_TYPE_SWITCH_USER = 2;
    }

    @IntDef({AppExitType.EXIT_TYPE_NONE, AppExitType.EXIT_TYPE_NORMAL, AppExitType.EXIT_TYPE_SWITCH_USER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExitTypeInter {
    }

    private AppUserInfo userInfo;

    private AppUserInfoManager() {

    }

    private static class InternalHolder {
        static AppUserInfoManager INSTANCE = new AppUserInfoManager();
    }

    public static AppUserInfoManager getInstance() {
        return InternalHolder.INSTANCE;
    }

    public void initUserInfo() {
        exitType = AppExitType.EXIT_TYPE_NONE;
        if (userInfo == null) {
            userInfo = AppFileCacheManager.loadCacheObject(
                    BaseApplication.instance.getApplicationContext(),
                    KEY_FILE_NAME, AppUserInfo.class, false);
        }
    }

    public AppUserInfo getUserInfo() {
        initUserInfo();
        if (userInfo == null) {
            userInfo = new AppUserInfo();
        }
        return userInfo;
    }

    public void setUserInfo(AppUserInfo userInfo) {
        this.userInfo = userInfo;
        try {
            AppFileCacheManager.saveCacheObject(
                    BaseApplication.instance.getApplicationContext(),
                    KEY_FILE_NAME, userInfo, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isLogin() {
        initUserInfo();
        if (userInfo == null) {
            return false;
        }
        return !TextUtils.isEmpty(userInfo.getUid()) && getExitType() == AppExitType.EXIT_TYPE_NONE;
    }

    public int getExitType() {
        return exitType;
    }

    public void clear() {
        userInfo = null;
        AppFileCacheManager.removeAsync(BaseApplication.instance.getApplicationContext(),
                KEY_FILE_NAME, false);
    }

    @ExitTypeInter
    public int exitType = AppExitType.EXIT_TYPE_NONE;

}
