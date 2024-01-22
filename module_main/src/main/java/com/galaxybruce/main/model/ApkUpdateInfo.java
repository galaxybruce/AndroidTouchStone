package com.galaxybruce.main.model;

import com.galaxybruce.component.proguard.IProguardKeeper;

import java.util.List;

/**
 * @author bruce.zhang
 * @date 2023/6/18 18:01
 * @description
 * <p>
 * modification history:
 */
public class ApkUpdateInfo {

   private List<UpdateInfo> androidupdateinfo;

   public List<UpdateInfo> getAndroidupdateinfo() {
      return androidupdateinfo;
   }

   public void setAndroidupdateinfo(List<UpdateInfo> androidupdateinfo) {
      this.androidupdateinfo = androidupdateinfo;
   }

   public static class UpdateInfo implements IProguardKeeper {

      /***
       * 最近一次强更版本号
       */
      private String vcforce;
      /**
       * 1强制 0非强制
       * */
      private String forceupdate;
      private String desc;
      private String downlink;
      private String downlink_32;
      private int versioncode;
      private String versionname;

      // 创纪云用
      private String appname;
      private String appid;
      //渠道
      private String channel;

      public String getVcforce() {
         return vcforce;
      }

      public void setVcforce(String vcforce) {
         this.vcforce = vcforce;
      }

      public String getForceupdate() {
         return forceupdate;
      }

      public void setForceupdate(String forceupdate) {
         this.forceupdate = forceupdate;
      }

      public String getDesc() {
         return desc;
      }

      public void setDesc(String desc) {
         this.desc = desc;
      }

      public String getDownlink() {
         return downlink;
      }

      public void setDownlink(String downlink) {
         this.downlink = downlink;
      }

      public String getDownlink_32() {
         return downlink_32;
      }

      public void setDownlink_32(String downlink_32) {
         this.downlink_32 = downlink_32;
      }

      public int getVersioncode() {
         return versioncode;
      }

      public void setVersioncode(int versioncode) {
         this.versioncode = versioncode;
      }

      public String getVersionname() {
         return versionname;
      }

      public void setVersionname(String versionname) {
         this.versionname = versionname;
      }

      public String getAppname() {
         return appname;
      }

      public void setAppname(String appname) {
         this.appname = appname;
      }

      public String getAppid() {
         return appid;
      }

      public void setAppid(String appid) {
         this.appid = appid;
      }

      public String getChannel() {
         return channel;
      }

      public void setChannel(String channel) {
         this.channel = channel;
      }
   }
}
