package com.galaxybruce.component.util.log;

import android.text.TextUtils;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.galaxybruce.component.util.debug.AppDebugLogDialog;
import com.galaxybruce.component.util.debug.AppDebugLogManager;

public class AppLogUtils {
    public static String customTagPrefix = "";

    private AppLogUtils() {
    }

    private static boolean sEnableLog = false;

    public static CustomLogger sCustomLogger;

    public static void init(boolean enableLog, CustomLogger customLogger){
        sEnableLog = enableLog;

        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(sEnableLog ? LogLevel.ALL : LogLevel.NONE)
                .enableThreadInfo()                                    // Enable thread info, disabled by default
//                .enableStackTrace(2)                                   // Enable stack trace info with depth 2, disabled by default
                .enableBorder()                                        // Enable border, disabled by default
                .build();
        XLog.init(config);

        if(customLogger != null) {
            sCustomLogger = customLogger;
            return;
        }
        sCustomLogger = new CustomLogger() {
            @Override
            public void d(String tag, String content) {
                XLog.d("%s: %s", tag, content);
            }

            @Override
            public void d(String tag, String content, Throwable tr) {
                XLog.d(String.format("%s: %s", tag, content), tr);
            }

            @Override
            public void e(String tag, String content) {
                XLog.e("%s: %s", tag, content);
            }

            @Override
            public void e(String tag, String content, Throwable tr) {
                XLog.e(String.format("%s: %s", tag, content), tr);
            }

            @Override
            public void i(String tag, String content) {
                XLog.i("%s: %s", tag, content);
            }

            @Override
            public void i(String tag, String content, Throwable tr) {
                XLog.i(String.format("%s: %s", tag, content), tr);
            }

            @Override
            public void v(String tag, String content) {
                XLog.v("%s: %s", tag, content);
            }

            @Override
            public void v(String tag, String content, Throwable tr) {
                XLog.v(String.format("%s: %s", tag, content), tr);
            }

            @Override
            public void w(String tag, String content) {
                XLog.w("%s: %s", tag, content);
            }

            @Override
            public void w(String tag, String content, Throwable tr) {
                XLog.w(String.format("%s: %s", tag, content), tr);
            }

            @Override
            public void w(String tag, Throwable tr) {

            }

            @Override
            public void wtf(String tag, String content) {

            }

            @Override
            public void wtf(String tag, String content, Throwable tr) {

            }

            @Override
            public void wtf(String tag, Throwable tr) {

            }
        };
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public interface CustomLogger {
        void d(String tag, String content);

        void d(String tag, String content, Throwable tr);

        void e(String tag, String content);

        void e(String tag, String content, Throwable tr);

        void i(String tag, String content);

        void i(String tag, String content, Throwable tr);

        void v(String tag, String content);

        void v(String tag, String content, Throwable tr);

        void w(String tag, String content);

        void w(String tag, String content, Throwable tr);

        void w(String tag, Throwable tr);

        void wtf(String tag, String content);

        void wtf(String tag, String content, Throwable tr);

        void wtf(String tag, Throwable tr);
    }

    public static void d(String content) {
        if (!sEnableLog) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (sCustomLogger != null) {
            sCustomLogger.d(tag, content);
        }
        showOnUi(tag + "\n" + content);
    }

    public static void d(String content, Throwable tr) {
        if (!sEnableLog) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (sCustomLogger != null) {
            sCustomLogger.d(tag, content, tr);
        }
        showOnUi(tag + "\n" + content);
    }

    public static void e(String content) {
        if (!sEnableLog) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (sCustomLogger != null) {
            sCustomLogger.e(tag, content);
        }
        showOnUi(tag + "\n" + content);
    }

    public static void e(String content, Throwable tr) {
        if (!sEnableLog) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (sCustomLogger != null) {
            sCustomLogger.e(tag, content, tr);
        }
        showOnUi(tag + "\n" + content + "\n" + tr.getMessage());
    }

    public static void i(String content) {
        if (!sEnableLog) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (sCustomLogger != null) {
            sCustomLogger.i(tag, content);
        }
        showOnUi(tag + "\n" + content);
    }

    public static void i(String content, Throwable tr) {
        if (!sEnableLog) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (sCustomLogger != null) {
            sCustomLogger.i(tag, content, tr);
        }
        showOnUi(tag + "\n" + content + "\n" + tr.getMessage());
    }

    public static void v(String content) {
        if (!sEnableLog) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (sCustomLogger != null) {
            sCustomLogger.v(tag, content);
        }
        showOnUi(tag + "\n" + content);
    }

    public static void v(String content, Throwable tr) {
        if (!sEnableLog) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (sCustomLogger != null) {
            sCustomLogger.v(tag, content, tr);
        }
        showOnUi(tag + "\n" + content + "\n" + tr.getMessage());
    }

    public static void w(String content) {
        if (!sEnableLog) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (sCustomLogger != null) {
            sCustomLogger.w(tag, content);
        }
        showOnUi(tag + "\n" + content);
    }

    public static void w(String content, Throwable tr) {
        if (!sEnableLog) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (sCustomLogger != null) {
            sCustomLogger.w(tag, content, tr);
        }
        showOnUi(tag + "\n" + content + "\n" + tr.getMessage());
    }

    public static void w(Throwable tr) {
        if (!sEnableLog) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (sCustomLogger != null) {
            sCustomLogger.w(tag, tr);
        }
        showOnUi(tag + "\n" + tr.getMessage());
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static void showOnUi(String log) {
        AppDebugLogManager.INSTANCE.pushLog(log);
    }
}
