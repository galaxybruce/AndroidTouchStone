package com.galaxybruce.component.app.crash;

import android.app.Application;
import android.content.Intent;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final Application mApplication;
    private final Thread.UncaughtExceptionHandler mDefaultHandler;
    private final boolean isRestartApp;

    public static void init(Application application, boolean isRestartApp) {
        Thread.UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (exceptionHandler instanceof CrashHandler) {
            return;
        }
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(application, exceptionHandler, isRestartApp));
    }

    private CrashHandler(Application application, Thread.UncaughtExceptionHandler exceptionHandler, boolean isRestartApp) {
        mApplication = application;
        this.isRestartApp = isRestartApp;
        this.mDefaultHandler = exceptionHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();

        int maxStackTraceSize = 131071;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTraceString = sw.toString();

        if (stackTraceString.length() > maxStackTraceSize) {
            String disclaimer = " [stack trace too large]";
            stackTraceString = stackTraceString.substring(0, maxStackTraceSize - disclaimer.length()) + disclaimer;
        }

        Intent intent = new Intent(mApplication, ExceptionActivity.class);
        intent.putExtra("message", stackTraceString);
        intent.putExtra("isRestartApp", isRestartApp);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mApplication.startActivity(intent);

        // todo 上报系统

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 系统的ExceptionHandler: com.android.internal.os.RuntimeInit$KillApplicationHandler
        if (mDefaultHandler != null && !mDefaultHandler.getClass().getName().startsWith("com.android.internal.os.RuntimeInit")) {
            mDefaultHandler.uncaughtException(thread, ex);
        }

//        //关闭所有页面并杀掉进程
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(10);
    }

}
