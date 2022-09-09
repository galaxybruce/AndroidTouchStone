package com.galaxybruce.component.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AdaptScreenUtils;
import com.galaxybruce.component.ui.IUiInit;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @date 2022/2/12 00:38
 * @author bruce.zhang
 * @description
 *
 * 关于dialog的宽度：
 *  如果是以宽度为维度适配的话，如果高度写死的话，由于不同的设备宽高比不一致，导致dialog不能展示全或者高度没有充分利用。
 *  所以需要按照高度的比例来算：假如设计稿中屏幕高度是1080，dialog高度是900，那么实际应该返回： ScreenUtils.getScreenHeight() * (900 /1080f)
 * 关于dialog高度：
 * 如果是以宽度为维度适配的话，如果高度写死的话，由于不同的设备宽高比不一致，导致dialog不能展示全或者高度没有充分利用。
 * 所以需要按照高度的比例来算：假如设计稿中屏幕高度是1080，dialog高度是900，那么实际应该返回： ScreenUtils.getScreenHeight() * (900 /1080f)
 *
 * <p>
 * modification history:
 */
public abstract class AppDialogFragment extends DialogFragment implements IUiInit {

    public void show(Context context, String tag) {
        Activity activity = ActivityUtils.getActivityByContext(context);
        if (!(activity instanceof FragmentActivity)) {
            return;
        }
        FragmentManager fm = ((FragmentActivity)activity).getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag(tag);
        if (prev != null) {
            fm.beginTransaction().remove(prev);
        }
        show(fm, tag);
    }

    @Override
    public void show(@NonNull FragmentManager manager, String tag) {
        try {
            // add by bruce.zhang  在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commitAllowingStateLoss();
            super.show(manager, tag);
        } catch (Exception e) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onStart() {
        adapterScreen();
        super.onStart();
        setDialogShowStyle();

        // 禁用软键盘
        if(forbiddenSoftKeyboard()) {
            Dialog dialog = getDialog();
            if(dialog != null) {
                Window window = dialog.getWindow();
                if (window != null) {
                    // 可以随时开启和禁用 软键盘：getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                }
            }
        }
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(getArguments(), savedInstanceState);
    }

    protected LayoutInflater mInflater;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        if (bindLayoutId() <= 0) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        View rootView = getView();
        if (rootView == null) {
            rootView = setRootLayout(bindLayoutId(), inflater, container);
        } else {
            final ViewParent parent = rootView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView(getView());
        bindData(savedInstanceState);
    }

    protected View setRootLayout(int layoutId, @NonNull LayoutInflater inflater, ViewGroup container) {
        if(layoutId > 0) {
            return mInflater.inflate(layoutId, container, false);
        } else {
            return null;
        }
    }

    @Override
    public void initData(@Nullable Bundle bundle, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public int bindLayoutId() {
        return 0;
    }

    @Override
    public void initView(@Nullable View view) {

    }

    @Override
    public void bindData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void dismiss() {
        super.dismissAllowingStateLoss();
    }

    /**
     * 获取dialog listener实例
     * Activity或者Fragment实现dialog的接口
     *
     * @param dialogFragment
     * @param listenerInterface
     * @param <T>
     * @return
     */
    protected static <T> T getDialogListener(DialogFragment dialogFragment, Class<T> listenerInterface) {
        //用targetFragment是否为空来标识是fragment还是activity开启的这个DialogFragment
        final Fragment parentFragment = dialogFragment.getParentFragment();
        if (parentFragment != null && listenerInterface.isAssignableFrom(parentFragment.getClass())) {
            return (T) parentFragment;
        }
        final Activity activity = dialogFragment.getActivity();
        if (activity != null && listenerInterface.isAssignableFrom(activity.getClass())) {
            return ((T) activity);
        }
        return null;
    }

    /**
     * 不能放在onCreateDialog方法中，不然不会生效，需要另外在onCreateView中设置布局宽度为屏幕宽度
     * v.setMinimumWidth(getResources().getDisplayMetrics().widthPixels);
     *
     * 默认dialog显示在中间
     */
    private void setDialogShowStyle() {
        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        setDialogShowStyle(dialog, window);
    }

    /**
     * 这里可以设置高度、宽度、以及自动弹出键盘
     * @param dialog
     * @param window
     */
    protected void setDialogShowStyle(@NonNull Dialog dialog, @NonNull Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = Resources.getSystem().getDisplayMetrics().widthPixels * 86 / 100;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setLayout(lp.width, lp.height);
    }

    /**
     * 禁用软键盘
     * @return
     */
    protected boolean forbiddenSoftKeyboard() {
        return false;
    }

    /**
     * 屏幕灭屏再亮屏Application的DisplayMetrics被重置，为避免resizeDialogFragment方法获取的宽和高度适配错误
     * 需要在resizeDialogFragment前重新适配
     */
    protected void adapterScreen() {
        AdaptScreenUtils.adaptWidth(super.getResources(), 750);
    }
}
