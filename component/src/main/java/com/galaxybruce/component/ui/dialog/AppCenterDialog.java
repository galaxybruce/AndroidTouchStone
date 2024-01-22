package com.galaxybruce.component.ui.dialog;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.galaxybruce.component.R;
import com.galaxybruce.component.ui.jetpack.JPBaseDialogFragment;
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

/**
 * @date 2021/12/13 17:02
 * @author bruce.zhang
 * @description 显示在屏幕中间的dialog
 * <p>
 * modification history:
 */
public class AppCenterDialog<VM extends JPBaseViewModel, B extends ViewDataBinding>
        extends JPBaseDialogFragment<VM, B> {

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Dialog_NoFrame);
    }

    protected void setDialogShowStyle(@NonNull Dialog dialog, @NonNull Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = Resources.getSystem().getDisplayMetrics().widthPixels * 86 / 100;
        window.setLayout(lp.width, lp.height);
    }
}
