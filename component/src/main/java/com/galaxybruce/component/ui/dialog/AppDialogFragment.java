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

import com.galaxybruce.component.ui.IUiInit;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public abstract class AppDialogFragment extends DialogFragment implements IUiInit {
    
    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            // add by bruce.zhang  在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commitAllowingStateLoss();
            super.show(manager, tag);
        } catch (Exception e) {
            if (manager == null) {
                return;
            }
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        resizeDialogFragment();

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
    protected void resizeDialogFragment() {
        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = Resources.getSystem().getDisplayMetrics().widthPixels * 86 / 100;
        window.setLayout(lp.width, lp.height);
    }

    /**
     * 禁用软键盘
     * @return
     */
    protected boolean forbiddenSoftKeyboard() {
        return false;
    }
}
