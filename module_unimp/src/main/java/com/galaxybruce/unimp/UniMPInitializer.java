package com.galaxybruce.unimp;

import android.content.Context;
import android.util.Log;

import com.galaxybruce.unimp.plugin.component.TestText;
import com.galaxybruce.unimp.plugin.module.TestModule;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.common.util.RuningAcitvityUtil;
import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IDCUniMPPreInitCallback;
import io.dcloud.feature.sdk.MenuActionSheetItem;

public class UniMPInitializer {

    /**
     * 判断是否是小程序进程
     * 为了防止其他三方SDK可能影响小程序的运行 请禁止在小程序进程初始化其他三方SDK！！！
     *
     * @param context
     * @return
     */
    public static boolean isUniMPProcess(Context context) {
        return RuningAcitvityUtil.getAppName(context).contains("unimp");
    }

    /**
     * 初始化原生扩展能力
     * 注意：原生扩展代码运行在小程序进程中，而且uni规定在小程序进程中禁止调用DCUniMPSDK的API。所以只需小程序进程初始化。
     *
     * 参考文章：
     * <a href="https://nativesupport.dcloud.net.cn/UniMPDocs/Extension/android.html">native support</a>
     *
     * @param context
     */
    public static void initUniMPProcess(Context context) {
        try {
            WXSDKEngine.registerModule("TestModule", TestModule.class);
            WXSDKEngine.registerComponent("myText", TestText.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

    /**
     * 宿主进程初始化uni小程序sdk
     * 注意：DCUniMPSDK的API只可以在宿主进程调用。否则会触发Not initialized异常。
     *
     * @param context
     */
    public static void initHostProcess(Context context) {
        //初始化 uni小程序SDK ----start----------
        MenuActionSheetItem item = new MenuActionSheetItem("关于", "gy");

        MenuActionSheetItem item1 = new MenuActionSheetItem("获取当前页面url", "hqdqym");
        MenuActionSheetItem item2 = new MenuActionSheetItem("跳转到宿主原生测试页面", "gotoTestPage");
        List<MenuActionSheetItem> sheetItems = new ArrayList<>();
        sheetItems.add(item);
        sheetItems.add(item1);
        sheetItems.add(item2);
        Log.i("unimp","onCreate----");
        DCSDKInitConfig config = new DCSDKInitConfig.Builder()
                .setCapsule(true)
                .setMenuDefFontSize("16px")
                .setMenuDefFontColor("#ff00ff")
                .setMenuDefFontWeight("normal")
                .setMenuActionSheetItems(sheetItems)
                .setEnableBackground(true)//开启后台运行
                .setUniMPFromRecents(true)
                .build();
        DCUniMPSDK.getInstance().initialize(context, config, new IDCUniMPPreInitCallback() {
            @Override
            public void onInitFinished(boolean isSuccess) {
                Log.i("unimp","onInitFinished----" + isSuccess);
            }
        });
        //初始化 uni小程序SDK ----end----------
    }

}
