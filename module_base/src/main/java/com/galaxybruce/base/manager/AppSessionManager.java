package com.galaxybruce.base.manager;

import android.text.TextUtils;

import com.galaxybruce.base.model.AppTokenInfo;
import com.galaxybruce.base.model.AppUserInfo;
import com.galaxybruce.component.app.BaseApplication;
import com.galaxybruce.component.util.cache.AppFileCacheManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.lifecycle.MutableLiveData;

/**
 * @date 2021/4/22 23:44
 * @author 
 * @description 用户会话信息管理
 * <p>
 * modification history:
 */
public class AppSessionManager {

    private static final String KEY_FILE_NAME_USER = "FILE_USER_INFO";
    private static final String KEY_FILE_NAME_TOKEN = "FILE_TOKEN_INFO";

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

    @ExitTypeInter
    public int exitType = AppExitType.EXIT_TYPE_NONE;

    private AppUserInfo userInfo;
    private AppTokenInfo tokenInfo;

    /**
     * 是否重新登录或者切换账号
     * 业务方根据需要监听该事件：
     * AppSessionManager.getInstance().loginEvent.observeForever(loginObserver);
     * AppSessionManager.getInstance().loginEvent.removeObserver(loginObserver);
     */
    public MutableLiveData<Boolean> loginEvent = new MutableLiveData<Boolean>();

    private AppSessionManager() {
        loginEvent.setValue(false);
    }

    private static class InternalHolder {
        static AppSessionManager INSTANCE = new AppSessionManager();
    }

    public static AppSessionManager getInstance() {
        return InternalHolder.INSTANCE;
    }

    public void initSessionInfo() {
        exitType = AppExitType.EXIT_TYPE_NONE;
        if (userInfo == null) {
            userInfo = AppFileCacheManager.loadCacheObject(
                    BaseApplication.instance.getApplicationContext(),
                    KEY_FILE_NAME_USER, AppUserInfo.class, false);
        }
        if (tokenInfo == null) {
            tokenInfo = AppFileCacheManager.loadCacheObject(
                    BaseApplication.instance.getApplicationContext(),
                    KEY_FILE_NAME_TOKEN, AppTokenInfo.class, false);
        }
    }

    public AppUserInfo getUserInfo() {
        initSessionInfo();
        if (userInfo == null) {
            userInfo = new AppUserInfo();
        }
        return userInfo;
    }

    public void setUserInfo(AppUserInfo userInfo) {
        this.userInfo = userInfo;
        AppFileCacheManager.saveCacheObjectAsync(
                BaseApplication.instance.getApplicationContext(),
                KEY_FILE_NAME_USER, userInfo, false);
    }

    public AppTokenInfo getTokenInfo() {
        initSessionInfo();
        if (tokenInfo == null) {
            tokenInfo = new AppTokenInfo();
        }
        return tokenInfo;
    }

    public void setTokenInfo(AppTokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
        AppFileCacheManager.saveCacheObjectAsync(
                BaseApplication.instance.getApplicationContext(),
                KEY_FILE_NAME_TOKEN, tokenInfo, false);
    }

    public boolean isLogin() {
        initSessionInfo();
        if (userInfo == null) {
            return false;
        }
        return !TextUtils.isEmpty(userInfo.getUid()) && exitType == AppExitType.EXIT_TYPE_NONE;
    }

    public void clear() {
        userInfo = null;
        AppFileCacheManager.removeAsync(BaseApplication.instance.getApplicationContext(),
                KEY_FILE_NAME_USER, false);
        AppFileCacheManager.removeAsync(BaseApplication.instance.getApplicationContext(),
                KEY_FILE_NAME_TOKEN, false);
    }

    /**
     * 更新登录事件
     */
    public void updateLoginEvent(boolean login) {
        loginEvent.postValue(login);
    }

}
