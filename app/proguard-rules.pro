# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\ide\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript listener
# class:
#-keepclassmembers class fqcn.of.javascript.listener.for.webview {
#   public *;
#}
#代码迭代优化次数  指定代码的压缩级别
-optimizationpasses 7
#混淆时是否记录日志
-verbose
#保护给定的可选属性，例如LineNumberTable, LocalVariableTable, SourceFile, Deprecated, Synthetic, Signature, InnerClasses.
-keepattributes Exceptions,InnerClasses,Signature,SourceFile,LineNumberTable,*Annotation*
#指定不去忽略非公共的库类。
-dontskipnonpubliclibraryclasses
#指定不去忽略包可见的库类的成员。
-dontskipnonpubliclibraryclassmembers
# 忽略警告，避免打包时某些警告出现
-ignorewarnings
# adding this in to preserve line numbers so that the stack traces
# can be remapped
#设置源文件中给定的字符串常量
-renamesourcefileattribute SourceFile
#保护指定类的成员，如果此类受到保护他们会保护的更好
#-keepclassmembers {modifier} {class_specification}
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keep class android.webkit.JavascriptInterface {*;}
-keepclassmembers class * extends android.webkit.WebChromeClient {
   public void openFileChooser(...);
}
#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#不压缩输入的类文件
-dontshrink
#混淆时是否做预校验  【不预校验】
-dontpreverify
#不优化输入的类文件  使用gson包解析数据时，出现missing type parameter异常添加此行代码
-dontoptimize
#混淆时不会产生形形色色的类名  是否使用大小写混合  【不使用】
-dontusemixedcaseclassnames
#重新包装所有重命名的包并放在给定的单一包中 flattenpackagehierarchy {package_name}
-flattenpackagehierarchy
#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

-printmapping map.txt

#-keep 指定的类和类成员被保留作为入口 保护指定的类文件和类的成员
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends java.lang.Throwable {*;}
-keep public class * extends java.lang.Exception {*;}
-keep public class * extends android.preference.Preference
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep class android.support.** { *; }
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-keep public class **.R$*{
   public static final int *;
}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
-dontwarn android.support.**

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

#保护指定的类和类的成员的名称，如果所有指定的类成员出席（在压缩步骤之后）
# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保护指定类的成员，如果此类受到保护他们会保护的更好
#保持类成员
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep public class java.util.HashMap {
	public <methods>;
}
-keep public class java.lang.String {
	public <methods>;
}
-keep public class java.util.List {
	public <methods>;
}

-keep public class android.net.http.SslError
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn Android.webkit.WebViewClient


-keep class * implements com.galaxybruce.component.proguard.IProguardKeeper {*;}


-keepclassmembers class * {
    public void set*(***);
    public boolean is*();
    public *** get*();
}

-keepclassmembers class * {
    public boolean onShowFileChooser(***);
    public void openFileChooser(***);
}

-keepclassmembers class * {
    public void onSuccess(***);
    public void onSuccess*(***);
}


#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class com.tencent.bugly.** {*; }

#fastjson
-dontwarn com.alibaba.fastjson.**
-keep public class com.alibaba.fastjson.**{*;}


#netty
-dontwarn io.netty.**
-keep class io.netty.** { *; }

#imageloader
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }

-dontwarn com.getkeepsafe.relinker.**
-keep class com.getkeepsafe.relinker.** { *; }

#httpclient
-dontwarn org.apache.http.**
-keep public class org.apache.http.**{*;}
-dontwarn org.apache.commons.logging.**
-keep public class org.apache.commons.logging.**{*;}

#async-httpclient
-dontwarn com.loopj.android.http.**
-keep class com.loopj.android.http.** { *; }

#RxJava
-dontwarn rx.**
-keep class rx.** { *; }
-dontwarn io.reactivex.**
-keep class io.reactivex.** { *; }

#gson
-dontwarn com.google.gson.**
-keep class com.google.gson.** { *; }

#Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keep class retrofit2.converter.gson.**{ *; }

#OKHTTP
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.squareup.okhttp.**

# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# flutter
-keep class io.flutter.app.** { *; }
-keep class io.flutter.plugin.**  { *; }
-keep class io.flutter.util.**  { *; }
-keep class io.flutter.view.**  { *; }
-keep class io.flutter.**  { *; }
-keep class io.flutter.plugins.**  { *; }


# 内部WebView混淆过滤
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
# 网络监听库
-dontwarn com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
-dontwarn io.reactivex.functions.Function
-dontwarn rx.internal.util.**
-dontwarn sun.misc.Unsafe

-keepattributes *Annotation*
-keep class kotlin.** { *; }
-keep class org.jetbrains.** { *; }


#freso
-keep class com.facebook.** {*; }
-keep class com.facebook.imagepipeline.** {*; }
-keep class com.facebook.animated.gif.** {*; }
-keep class com.facebook.drawee.** {*; }
-keep class com.facebook.drawee.backends.pipeline.** {*; }
-keep class com.facebook.imagepipeline.** {*; }
-keep class bolts.** {*; }
-keep class me.relex.photodraweeview.** {*; }


-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**


#greendao
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {     public static java.lang.String TABLENAME; }
-keep class **$Properties{*;}


#arouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
# If you use the byType method to obtain Service, add the following rules to protect the interface:
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
# If single-type injection is used, that is, no interface is defined to implement IProvider, the following rules need to be added to protect the implementation
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider






##################################### uni小程序 start #####################################
-keep public class * extends io.dcloud.common.DHInterface.IPlugin
-keep public class * extends io.dcloud.common.DHInterface.IFeature
-keep public class * extends io.dcloud.common.DHInterface.IBoot
-keep public class * extends io.dcloud.common.DHInterface.IReflectAble

-keep class io.dcloud.** {*;}
-dontwarn io.dcloud.**
-dontwarn com.alibaba.**

-keep class vi.com.gdi.** {*;}

-keepclasseswithmembers class io.dcloud.appstream.StreamAppManager {
    public protected <methods>;
}

-keep public class * extends io.dcloud.common.DHInterface.IReflectAble{
  public protected <methods>;
  public protected *;
}
-keep class **.R
-keep class **.R$* {
    public static <fields>;
}
-keep public class * extends io.dcloud.common.DHInterface.IJsInterface{
  public protected <methods>;
  public protected *;
}

-keepclasseswithmembers class io.dcloud.EntryProxy {
    <methods>;
}

-keep class * implements android.os.IInterface {
  <methods>;
}

-keepclasseswithmembers class *{
  public static java.lang.String getJsContent();
}

-keepclasseswithmembers class *{
  public static io.dcloud.share.AbsWebviewClient getWebviewClient(io.dcloud.share.ShareAuthorizeView);
}

-keepattributes Exceptions,InnerClasses,Signature,Deprecated, SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class dc.** {*;}
-keep class okio.**{*;}
-keep class org.apache.** {*;}
-keep class org.json.** {*;}
-keep class net.ossrs.** {*;}
-keep class android.** {*;}
-keep class com.facebook.**{*;}
-keep class com.bumptech.glide.**{*;}
-keep class com.alibaba.fastjson.**{*;}
-keep class com.sina.**{*;}
-keep class com.weibo.ssosdk.**{*;}
-keep class com.asus.**{*;}
-keep class com.bun.**{*;}
-keep class com.heytap.**{*;}
-keep class com.huawei.**{*;}
-keep class com.meizu.**{*;}
-keep class com.samsung.**{*;}
-keep class com.zui.**{*;}
-keep class com.amap.**{*;}
-keep class com.loc.**{*;}
-keep class com.autonavi.**{*;}
-keep class pl.droidsonroids.gif.**{*;}
-keep class com.tencent.**{*;}
-keep class com.baidu.**{*;}
-keep class com.iflytek.**{*;}
-keep class com.umeng.**{*;}
-keep class tv.**{*;}
-keep class master.**{*;}
-keep class uk.co.**{*;}
-keep class com.dmcbig.**{*;}
-dontwarn android.**
-dontwarn com.tencent.**

-keep class * implements com.taobao.weex.IWXObject{*;}
-keep public class * extends com.taobao.weex.common.WXModule{*;}
-keep public class * extends com.taobao.weex.ui.component.WXComponent{*;}
-keep public class * extends io.dcloud.weex.AppHookProxy{*;}
-keep public class * extends io.dcloud.feature.uniapp.UniAppHookProxy{*;}
-keep public class * extends io.dcloud.feature.uniapp.common.UniModule{*;}
-keep public class * extends io.dcloud.feature.uniapp.ui.component.UniComponent{*;}


-keepattributes Signature

-dontwarn org.codehaus.mojo.**
-dontwarn org.apache.commons.**
-dontwarn com.amap.**
-dontwarn com.sina.weibo.sdk.**
-dontwarn com.alipay.**
-dontwarn com.lucan.ajtools.**
-dontwarn pl.droidsonroids.gif.**

-keep class com.taobao.weex.** { *; }
-keep class com.taobao.gcanvas.**{*;}
-dontwarn com.taobao.weex.**
-dontwarn com.taobao.gcanvas.**

#个推
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }

-keep class android.support.v4.app.NotificationCompat { *; }
-keep class android.support.v4.app.NotificationCompat$Builder { *; }
#魅族
-keep class com.meizu.** { *; }
-dontwarn com.meizu.**
#小米
-keep class com.xiaomi.** { *; }
-dontwarn com.xiaomi.push.**
-keep class org.apache.thrift.** { *; }
#华为
-dontwarn com.huawei.hms.**
-keep class com.huawei.hms.** { *; }

-keep class com.huawei.android.** { *; }
-dontwarn com.huawei.android.**

-keep class com.hianalytics.android.** { *; }
-dontwarn com.hianalytics.android.**

-keep class com.huawei.updatesdk.** { *; }
-dontwarn com.huawei.updatesdk.**
#OPPO
-keep class com.coloros.mcssdk.** { *; }
-dontwarn com.coloros.mcssdk.**


#高德定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.loc.**{*;}

#腾讯X5--------------start-----------------------
-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**
#-overloadaggressively
# ------------------ Keep LineNumbers and properties ---------------- #
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
# --------------------------------------------------------------------------
# Addidional for x5.sdk classes for apps
-keep class com.tencent.smtt.export.external.**{*;}
-keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {*;}
-keep class com.tencent.smtt.sdk.CacheManager {public *;}
-keep class com.tencent.smtt.sdk.CookieManager {public *;}
-keep class com.tencent.smtt.sdk.WebHistoryItem {public *;}
-keep class com.tencent.smtt.sdk.WebViewDatabase {public *;}
-keep class com.tencent.smtt.sdk.WebBackForwardList {public *;}
-keep public class com.tencent.smtt.sdk.WebView {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.WebView$HitTestResult {public static final <fields>;public java.lang.String getExtra();public int getType();}
-keep public class com.tencent.smtt.sdk.WebView$WebViewTransport {public <methods>;}
-keep public class com.tencent.smtt.sdk.WebView$PictureListener {public <fields>;public <methods>;}
-keepattributes InnerClasses
-keep public enum com.tencent.smtt.sdk.WebSettings$** {*;}
-keep public enum com.tencent.smtt.sdk.QbSdk$** {*;}
-keep public class com.tencent.smtt.sdk.WebSettings {public *;}

-keepattributes Signature
-keep public class com.tencent.smtt.sdk.ValueCallback {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.WebViewClient {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.DownloadListener {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.WebChromeClient {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.WebChromeClient$FileChooserParams {public <fields>;public <methods>;}
-keep class com.tencent.smtt.sdk.SystemWebChromeClient{public *;}
# 1. extension interfaces should be apparent
-keep public class com.tencent.smtt.export.external.extension.interfaces.* {public protected *;}

# 2. interfaces should be apparent
-keep public class com.tencent.smtt.export.external.interfaces.* {public protected *;}
-keep public class com.tencent.smtt.sdk.WebViewCallbackClient {public protected *;}
-keep public class com.tencent.smtt.sdk.WebStorage$QuotaUpdater {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.WebIconDatabase {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.WebStorage {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.DownloadListener {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.QbSdk {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.QbSdk$PreInitCallback {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.CookieSyncManager {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.Tbs* {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.utils.LogFileUtils {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.utils.TbsLog {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.utils.TbsLogClient {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.CookieSyncManager {public <fields>;public <methods>;}
# Added for game demos
-keep public class com.tencent.smtt.sdk.TBSGamePlayer {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.TBSGamePlayerClient* {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.TBSGamePlayerClientExtension {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.TBSGamePlayerService* {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.utils.Apn {public <fields>;public <methods>;}
-keep class com.tencent.smtt.** {*;}
# end
-keep public class com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension {public <fields>;public <methods>;}
-keep class MTT.ThirdAppInfoNew {*;}
-keep class com.tencent.mtt.MttTraceEvent {*;}
# Game related
-keep public class com.tencent.smtt.gamesdk.* {public protected *;}
-keep public class com.tencent.smtt.sdk.TBSGameBooter {public <fields>;public <methods>;}
-keep public class com.tencent.smtt.sdk.TBSGameBaseActivity {public protected *;}
-keep public class com.tencent.smtt.sdk.TBSGameBaseActivityProxy {public protected *;}
-keep public class com.tencent.smtt.gamesdk.internal.TBSGameServiceClient {public *;}
#腾讯X5--------------end-----------------------

##################################### uni小程序 end #####################################


