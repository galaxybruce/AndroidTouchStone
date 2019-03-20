package com.galaxybruce.component.file;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Date: 2017/10/27 16:06
 * Author: bruce.zhang
 * Description: 文件、目录管理
 *
 * 文件按生命周期分为三种：
 * 1.app独立的文件，app卸载了也不会删除的，主要是拍照的图片，保存的图片，拍摄的视频，
 * 音乐等多媒体文件。存放在SDCard/Pictures  /SDCard/Movies/  /SDCard/Music/目录下，
 * 参考方法getFilePath(Context context, String type, String suffix)
 *          getPictureFilePath(Context context, String suffix)
 *          getVideoFilePath(Context context, String suffix)
 *          getMusicFilePath(Context context, String suffix)
 *
 * 2.app专属的文件，随着app卸载自动就删除了，存放在SDCard/Android/data/{packageName}/files
 * 或者/data/data/{packageName}/files目录下
 * 参考方法getAppFilePath(Context context, String businessFolderName, String subFolderName, String suffix)
 *          getAppPictureFilePath(Context context, String subFolderName, String suffix)
 *          getAppVideoFilePath(Context context, String subFolderName, String suffix)
 *          getAppMusicFilePath(Context context, String subFolderName, String suffix)
 *
 * 3.app运行过程中产生的临时文件，任务结束要手动删除了
 * 参考方法getAppCachePath(Context context, String businessFolderName, String subFolderName, String suffix)
 *
 * 异常情况：
 * 1、存储空间不够检测时机：应该在app启动或者app进入前台或者开始执行某项占用空间非常大的任务前
 *    不应该在调用具体生成文件的方式时检测，因为有可能某个任务是批量生成文件
 *    检测到存储空间不够时处理方法：停止当前任务，给出提示，让用户去清理数据
 * 2、是否有sdcard检测时机同1 处理方法：停止当前任务，给出提示
 * 3、是否有权限检测时机同1 处理方法：停止当前任务，给出提示
 * <p>
 * Modification  History:
 */
public class AppFilePathManager {

    //常用目录名
    public static final String BUSINESS_PICTURE_FOLDER = "picture";
    public static final String BUSINESS_VIDEO_FOLDER = "video";
    public static final String BUSINESS_MUSIC_FOLDER = "music";

    public static String getPictureFilePath(Context context, String suffix) {
        return AppFilePathManager.getPictureFilePath(context, null, suffix);
    }

    /**
     * 获取app独立的图片路径，app卸载了，图片依然存在 sdcard/Pictures
     * @param suffix 文件后缀名，如.jpg  .gif; 默认值.jpg
     * @return
     */
    public static String getPictureFilePath(Context context, String subBusinessFolderName, String suffix) {
        if(TextUtils.isEmpty(suffix)) {
            suffix = ".jpg";
        }

        return AppFilePathManager.getFilePath(context, Environment.DIRECTORY_PICTURES, subBusinessFolderName, suffix);
    }

    public static String getVideoFilePath(Context context, String suffix) {
        return AppFilePathManager.getVideoFilePath(context, null, suffix);
    }

    /**
     * 获取app独立的视频路径，app卸载了，视频依然存在 sdcard/Movies
     *  @param suffix 文件后缀名，如.mp4  .flv; 默认值.mp4
     * @return
     */
    public static String getVideoFilePath(Context context, String subBusinessFolderName, String suffix) {
        if(TextUtils.isEmpty(suffix)) {
            suffix = ".mp4";
        }

        return AppFilePathManager.getFilePath(context, Environment.DIRECTORY_MOVIES, subBusinessFolderName, suffix);
    }

    public static String getMusicFilePath(Context context, String suffix) {
        return AppFilePathManager.getMusicFilePath(context, null, suffix);
    }

    /**
     * 获取app独立的音频路径，app卸载了，音乐依然存在 sdcard/Music
     * @param suffix 文件后缀名，如.mp3 ; 默认值.mp3
     * @return
     */
    public static String getMusicFilePath(Context context, String subBusinessFolderName, String suffix) {
        if(TextUtils.isEmpty(suffix)) {
            suffix = ".mp3";
        }

        return AppFilePathManager.getFilePath(context, Environment.DIRECTORY_MUSIC, subBusinessFolderName, suffix);
    }

    /**
     * 获取app独立的路径 sdcard/Pictures, sdcard/Movies, sdcard/Music
     * @param context
     * @param type      Environment类中的常量，如Environment.DIRECTORY_PICTURES等，不能自己随便写
     * @param subBusinessFolderName 子业务文件夹
     * @param suffix    文件后缀名如.mp3
     * @return
     */
    private static String getFilePath(Context context, String type, String subBusinessFolderName, String suffix) {
        File dir = AppFilePathManager.getFileDir(context, type, subBusinessFolderName);
        String path = dir.getAbsolutePath() + File.separator + FileUtils.createUniqueFileName(suffix);
        return path;
    }

    /**
     * 获取app独立的路径 sdcard/Pictures, sdcard/Movies, sdcard/Music
     * @param context
     * @param type      Environment类中的常量，如Environment.DIRECTORY_PICTURES等，不能自己随便写
     * @param subBusinessFolderName 子业务文件夹
     * @return
     */
    public static File getFileDir(Context context, String type, String subBusinessFolderName) {
        if(!FileUtils.hasSDCard()) {
            return null;
        }

        if(!FileUtils.hasExternalStoragePermission(context)) {
            return null;
        }

        //TODO 包名简化策略，可以考虑用配置的方式，从外面传进来
        File dir = FileUtils.getExternalPublicDir(type);
        String subFolderName = context.getPackageName();
        dir = new File(dir,  subFolderName);
        if(!TextUtils.isEmpty(subBusinessFolderName)) {
            dir = new File(dir, subBusinessFolderName);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }


    /**
     * 获取app专属的图片路径，直到app卸载才删除
     * @param context
     * @return
     */
    public static String getAppPictureFilePath(Context context, String subFolderName, String suffix) {
        return AppFilePathManager.getAppFilePath(context, BUSINESS_PICTURE_FOLDER, subFolderName, suffix);
    }

    /**
     * 获取app专属的视频路径，直到app卸载才删除
     * @param context
     * @return
     */
    public static String getAppVideoFilePath(Context context, String subFolderName, String suffix) {
        return AppFilePathManager.getAppFilePath(context, BUSINESS_VIDEO_FOLDER, subFolderName, suffix);
    }

    /**
     * 获取app专属的音频路径，直到app卸载才删除
     * @param context
     * @return
     */
    public static String getAppMusicFilePath(Context context, String subFolderName, String suffix) {
        return AppFilePathManager.getAppFilePath(context, BUSINESS_MUSIC_FOLDER, subFolderName, suffix);
    }

    /**
     * 获取app专属的文件路径，直到app卸载才删除；存放在SDCard/Android/data/{packageName}/files或者/data/data/{packageName}/files目录下
     * 策略：先从sdcard获取，失败后从内部空间获取
     * @param context
     * @param businessFolderName 业务目录，如picture，video，music以及其他业务
     * @param subFolderName 某个任务的子目录，建议随机生成KWFileUtils.createUniqueDirName();，避免和其他任务重复，方便任务结束时删除该目录中的文件
     * @param suffix 文件后缀名
     * @return
     */
    public static String getAppFilePath(Context context, String businessFolderName,
                                        String subFolderName, String suffix) {
        File dir = AppFilePathManager.getAppFileDir(context, businessFolderName, subFolderName);

        String path = dir.getAbsolutePath() + File.separator + FileUtils.createUniqueFileName(suffix);
        return path;
    }

    /**
     * 获取app专属的文件路径，直到app卸载才删除；存放在SDCard/Android/data/{packageName}/files或者/data/data/{packageName}/files目录下
     * 策略：先从sdcard获取，失败后从内部空间获取
     * @param context
     * @param businessFolderName 业务目录，如picture，video，music以及其他业务
     * @param subFolderName 某个任务的子目录，建议随机生成KWFileUtils.createUniqueDirName();，避免和其他任务重复，方便任务结束时删除该目录中的文件
     * @return
     */
    public static File getAppFileDir(Context context, String businessFolderName, String subFolderName) {
        File dir = FileUtils.getExternalFilesDir(context);
        if(dir == null) {
            dir = FileUtils.getInternalFilesDir(context);
        }

        if(!TextUtils.isEmpty(businessFolderName)) {
            dir = new File(dir, businessFolderName);
        }
        if(!TextUtils.isEmpty(subFolderName)) {
            dir = new File(dir, subFolderName);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    /**
     * 获取app专属的缓存路径，存放在SDCard/Android/data/{packageName}/cache或者/data/data/{packageName}/cache目录下
     * 策略：先从sdcard获取，失败后从内部空间获取
     * @param context
     * @param businessFolderName 业务目录，如picture，video，music以及其他业务
     * @param subFolderName 某个任务的子目录，建议随机生成KWFileUtils.createUniqueDirName()，避免和其他任务重复，方便任务结束时删除该目录中的文件
     * @param suffix 文件后缀名
     * @return
     */
    public static String getAppCacheFilePath(Context context, String businessFolderName,
                                             String subFolderName, String suffix) {
        File dir = AppFilePathManager.getAppCacheFileDir(context, businessFolderName, subFolderName);

        String path = dir.getAbsolutePath() + File.separator + FileUtils.createUniqueFileName(suffix);
        return path;
    }

    /**
     * 获取app专属的缓存路径，存放在SDCard/Android/data/{packageName}/cache或者/data/data/{packageName}/cache目录下
     * 策略：先从sdcard获取，失败后从内部空间获取
     * @param context
     * @param businessFolderName 业务目录，如picture，video，music以及其他业务
     * @param subFolderName 某个任务的子目录，建议随机生成KWFileUtils.createUniqueDirName()，避免和其他任务重复，方便任务结束时删除该目录中的文件
     * @return
     */
    public static File getAppCacheFileDir(Context context, String businessFolderName, String subFolderName) {
        File dir = FileUtils.getExternalCacheDir(context);
        if(dir == null) {
            dir = FileUtils.getInternalCacheDir(context);
        }

        if(!TextUtils.isEmpty(businessFolderName)) {
            dir = new File(dir, businessFolderName);
        }
        if(!TextUtils.isEmpty(subFolderName)) {
            dir = new File(dir, subFolderName);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    /**
     * 随机生成文件路径
     * @param parent 调用KWAppPathManager.getAppCacheFileDir或者KWAppPathManager.getAppFileDir生成的目录
     * @param suffix 文件后缀名 可以是null
     * @return
     */
    public static String getFilePathInParent(File parent, String suffix) {
        if(parent == null) {
            throw new IllegalArgumentException("KWAppPathManager getFilePath parent is null");
        }
        String path = parent.getAbsolutePath() + File.separator + FileUtils.createUniqueFileName(suffix);
        return path;
    }

}
