package com.galaxybruce.component.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.galaxybruce.component.R;
import com.galaxybruce.component.ui.jetpack.JPBaseDialogFragment;
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

/**
 * @author bruce.zhang
 * @date 2019/1/3 14:50
 * @description 底部弹出dialog
 * 参考BottomSheetDialogFragment，可以直接用。因为我们项目中都是继承KidDialogFragment，
 * 所以需要重写一下
 * <p>
 * dialog listener处理参考:
 * [Android DialogFragment 在页面销毁下的使用方式](https://www.jianshu.com/p/5ba9f36a4a90)
 * <p>
 * modification history:
 *
 */
public abstract class AppBottomDialog<VM extends JPBaseViewModel, B extends ViewDataBinding>
        extends JPBaseDialogFragment<VM, B> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Dialog_Anim_Up_Down);
    }

    @Override
    protected void setDialogShowStyle(@NonNull Dialog dialog, @NonNull Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
//            dialog.onWindowAttributesChanged(params);
        dialog.setCanceledOnTouchOutside(true);
    }

}
