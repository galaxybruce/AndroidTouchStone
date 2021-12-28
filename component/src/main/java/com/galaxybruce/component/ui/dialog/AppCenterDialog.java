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

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

/**
 * @date 2021/12/13 17:02
 * @author bruce.zhang
 * @description 显示在屏幕中间的dialog
 * <p>
 * modification history:
 */
public class AppCenterDialog<B extends ViewDataBinding> extends JPBaseDialogFragment<B> {

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Dialog_NoFrame);
    }

    /**
     * 不能放在onCreateDialog方法中，不然不会生效，需要另外在onCreateView中设置布局宽度为屏幕宽度
     * v.setMinimumWidth(getResources().getDisplayMetrics().widthPixels);
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
}
