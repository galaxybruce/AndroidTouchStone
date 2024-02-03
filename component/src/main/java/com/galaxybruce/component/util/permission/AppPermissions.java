package com.galaxybruce.component.util.permission;

import android.Manifest;
import android.os.Build;

import com.galaxybruce.component.util.AndroidSDKVersionUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

public final class AppPermissions {

    public static final String CALENDAR             = "CALENDAR";
    public static final String CAMERA               = "CAMERA";
    public static final String CONTACTS             = "CONTACTS";
    public static final String LOCATION             = "LOCATION";
    public static final String MICROPHONE           = "MICROPHONE";
    public static final String PHONE                = "PHONE";
    public static final String SENSORS              = "SENSORS";
    public static final String SMS                  = "SMS";
    public static final String STORAGE              = "STORAGE";
    public static final String ACTIVITY_RECOGNITION = "ACTIVITY_RECOGNITION";

    private static final String[] GROUP_CALENDAR             = {
            Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
    };
    private static final String[] GROUP_CAMERA               = {
            Manifest.permission.CAMERA
    };
    private static final String[] GROUP_CONTACTS             = {
            Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS
    };
    private static final String[] GROUP_LOCATION             = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };
    private static final String[] GROUP_MICROPHONE           = {
            Manifest.permission.RECORD_AUDIO
    };
    private static final String[] GROUP_PHONE                = {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.ANSWER_PHONE_CALLS
    };
    private static final String[] GROUP_PHONE_BELOW_O        = {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS
    };
    private static final String[] GROUP_SENSORS              = {
            Manifest.permission.BODY_SENSORS
    };
    private static final String[] GROUP_SMS                  = {
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission.RECEIVE_MMS,
    };
    private static final String[] GROUP_STORAGE              = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final String[] GROUP_ACTIVITY_RECOGNITION = {
            Manifest.permission.ACTIVITY_RECOGNITION,
    };

    @StringDef({CALENDAR, CAMERA, CONTACTS, LOCATION, MICROPHONE, PHONE, SENSORS, SMS, STORAGE,})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionGroup {
    }

    public static String[] getPermissions(@com.blankj.utilcode.constant.PermissionConstants.PermissionGroup final String permission) {
        if (permission == null) return new String[0];
        switch (permission) {
            case CALENDAR:
                return GROUP_CALENDAR;
            case CAMERA:
                return GROUP_CAMERA;
            case CONTACTS:
                return GROUP_CONTACTS;
            case LOCATION:
                return GROUP_LOCATION;
            case MICROPHONE:
                return GROUP_MICROPHONE;
            case PHONE:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    return GROUP_PHONE_BELOW_O;
                } else {
                    return GROUP_PHONE;
                }
            case SENSORS:
                return GROUP_SENSORS;
            case SMS:
                return GROUP_SMS;
            case STORAGE:
                return GROUP_STORAGE;
            case ACTIVITY_RECOGNITION:
                return GROUP_ACTIVITY_RECOGNITION;
        }
        return new String[]{permission};
    }

    /**
     * 获取图片和视频权限
     * android 13上对媒体权限做了细分，但是图片和视频应该是在同一个组里，申请读取图片权限，视频权限也会自动获取
     * 
     * 在android 10上，写多媒体文件，一般是通过 MediaStore API 写，不需要额外申请权限。
     * 读写其他文件建议使用文件选择器SAF，SAF不需要动态权限，而且获取某一个目录后，还可以保存该目录的权限，不用每次都申请
     * 参考：[SAF（Storage Access Framework）使用攻略](https://juejin.cn/post/6844904058743078919)。
     *
     * @return
     */
    public static String[] getPictureVideoStoragePermission() {
        if(AndroidSDKVersionUtils.hasTiramisu()){
            return new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO};
        } else {
            return GROUP_STORAGE;
        }
    }

    /**
     * 获取音频权限
     * android 13上对媒体权限做了细分
     * @return
     */
    public static String[] getAudioStoragePermission() {
        if(AndroidSDKVersionUtils.hasTiramisu()){
            return new String[]{Manifest.permission.READ_MEDIA_AUDIO};
        } else {
            return GROUP_STORAGE;
        }
    }
}
