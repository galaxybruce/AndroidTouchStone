package com.galaxybruce.component.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;


/**
 * @author bruce.zhang
 * @date 2018/5/31 17:14
 * @description 自定义样式属性解析
 * <p>
 * modification history:
 */
public class AppAttrResolveUtil {

    public static final String APP_THEME_NAME = "App_Theme";

    /**
     * 把自定义的样式插入到当前activity的Theme
     * 随便在styles.xml文件中定义一个样式，如custom_theme，没有必要在styles把改样式设置到全局Theme中
     *
     * 该方法在Activity.setContentView之前调用
     * @param context
     */
    public static void applyStyle2ActivityTheme(Context context, String styleName, boolean force){
        int resId = context.getResources().getIdentifier(styleName, "style",
                context.getPackageName());
        context.getTheme().applyStyle(resId, force);
    }

    /**
     * 获取application中设置的全局的Theme
     * @param context
     * @return
     */
    public static Resources.Theme getApplicationTheme(Context context) {
        try {
            Resources resources = context.getApplicationContext().getResources();
            Resources.Theme newTheme = resources.newTheme();
            int resId = resources.getIdentifier(APP_THEME_NAME, "style", context.getPackageName());
            newTheme.applyStyle(resId, true);
            return newTheme;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析颜色值
     * @param context
     * @param attrRes
     * @return
     */
    public static int resolveAttrColor(Context context, int attrRes) {
        try {
            TypedValue typedValue = new TypedValue();

            // I used getActivity() as if you were calling from a fragment.
            // You just want to call getTheme() on the current activity, however you can get it
            context.getTheme().resolveAttribute(attrRes, typedValue, true);

            // it's probably a good idea to check if the color wasn't specified as a resource
            if (typedValue.resourceId != 0) {
                return context.getResources().getColor(typedValue.resourceId);
            } else if(typedValue.data != 0){
                // this should work whether there was a resource id or not
                return typedValue.data;
            } else {
                // 当前activity theme获取不到，从application中获取
                Resources.Theme theme = getApplicationTheme(context);
                theme.resolveAttribute(attrRes, typedValue, true);

                if (typedValue.resourceId != 0) {
                    return context.getResources().getColor(typedValue.resourceId);
                } else if(typedValue.data != 0){
                    // this should work whether there was a resource id or not
                    return typedValue.data;
                }
            }
        } catch (Exception e) {

        }
        return 0;
    }

    /**
     * 解析背景属性值
     * @param context
     * @param view
     * @param attrRes
     */
    public static void resolveAttrBackgroundRes(Context context, View view, int attrRes) {
        try {
            TypedValue typedValue = new TypedValue();

            // I used getActivity() as if you were calling from a fragment.
            // You just want to call getTheme() on the current activity, however you can get it
            context.getTheme().resolveAttribute(attrRes, typedValue, true);

            // it's probably a good idea to check if the color wasn't specified as a resource
            if (typedValue.resourceId != 0) {
                view.setBackgroundResource(typedValue.resourceId);
            } else if(typedValue.data != 0){
                // this should work whether there was a resource id or not
                view.setBackgroundColor(typedValue.data);
            } else {
                // 当前activity theme获取不到，从application中获取
                Resources.Theme theme = getApplicationTheme(context);
                theme.resolveAttribute(attrRes, typedValue, true);

                if (typedValue.resourceId != 0) {
                    view.setBackgroundResource(typedValue.resourceId);
                } else if(typedValue.data != 0){
                    // this should work whether there was a resource id or not
                    view.setBackgroundColor(typedValue.data);
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 解析文本颜色属性值
     * @param context
     * @param view
     * @param attrRes
     */
    public static void resolveAttrTextColorRes(Context context, TextView view, int attrRes) {
        try {
            TypedValue typedValue = new TypedValue();

            // I used getActivity() as if you were calling from a fragment.
            // You just want to call getTheme() on the current activity, however you can get it
            context.getTheme().resolveAttribute(attrRes, typedValue, true);

            // it's probably a good idea to check if the color wasn't specified as a resource
            if (typedValue.resourceId != 0) {
                view.setTextColor(context.getResources().getColor(typedValue.resourceId));
            } else if(typedValue.data != 0){
                // this should work whether there was a resource id or not
                view.setTextColor(typedValue.data);
            } else {
                // 当前activity theme获取不到，从application中获取
                Resources.Theme theme = getApplicationTheme(context);
                theme.resolveAttribute(attrRes, typedValue, true);

                if (typedValue.resourceId != 0) {
                    view.setTextColor(context.getResources().getColor(typedValue.resourceId));
                } else if(typedValue.data != 0){
                    // this should work whether there was a resource id or not
                    view.setTextColor(typedValue.data);
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 解析文本字体大小属性值
     * @param context
     * @param view
     * @param attrRes
     */
    public static void resolveAttrTextSizeRes(Context context, TextView view, int attrRes) {
        try {
            TypedValue typedValue = new TypedValue();

            // I used getActivity() as if you were calling from a fragment.
            // You just want to call getTheme() on the current activity, however you can get it
            context.getTheme().resolveAttribute(attrRes, typedValue, true);

            // it's probably a good idea to check if the color wasn't specified as a resource
            if (typedValue.resourceId != 0) {
                view.setTextSize(context.getResources().getDimensionPixelSize(typedValue.resourceId));
            } else if(typedValue.data != 0){
                // this should work whether there was a resource id or not
                TypedArray typedArray = context.obtainStyledAttributes(typedValue.data, new int[] { attrRes });
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX,typedArray.getDimensionPixelSize(0, -1));
                typedArray.recycle();
            } else {
                // 当前activity theme获取不到，从application中获取
                Resources.Theme theme = getApplicationTheme(context);
                theme.resolveAttribute(attrRes, typedValue, true);

                if (typedValue.resourceId != 0) {
                    view.setTextSize(context.getResources().getDimensionPixelSize(typedValue.resourceId));
                } else if(typedValue.data != 0){
                    // this should work whether there was a resource id or not
                    TypedArray typedArray = context.obtainStyledAttributes(typedValue.data, new int[] { attrRes });
                    view.setTextSize(TypedValue.COMPLEX_UNIT_PX,typedArray.getDimensionPixelSize(0, -1));
                    typedArray.recycle();
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * SwipeRefreshLayout转圈颜色
     * @param context
     * @param view
     * @param attrRes
     */
    public static void resolveAttrSwipeRefreshLayoutRes(Context context, SwipeRefreshLayout view, int attrRes) {
        try {
            TypedValue typedValue = new TypedValue();

            // I used getActivity() as if you were calling from a fragment.
            // You just want to call getTheme() on the current activity, however you can get it
            context.getTheme().resolveAttribute(attrRes, typedValue, true);

            // it's probably a good idea to check if the color wasn't specified as a resource
            if (typedValue.resourceId != 0) {
                view.setColorSchemeResources(typedValue.resourceId);
            } else if(typedValue.data != 0){
                // this should work whether there was a resource id or not
                view.setColorSchemeColors(typedValue.data);
            } else {
                // 当前activity theme获取不到，从application中获取
                Resources.Theme theme = getApplicationTheme(context);
                theme.resolveAttribute(attrRes, typedValue, true);

                if (typedValue.resourceId != 0) {
                    view.setColorSchemeResources(typedValue.resourceId);
                } else if(typedValue.data != 0){
                    // this should work whether there was a resource id or not
                    view.setColorSchemeColors(typedValue.data);
                }
            }
        } catch (Exception e) {
//            view.setColorSchemeResources(R.color.bbs_swiperefresh_color1);
        }
    }
}
