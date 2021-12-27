package com.galaxybruce.component.ui.dialog;

import android.app.Dialog;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * @date 2021/12/13 17:02
 * @author bruce.zhang
 * @description 显示在屏幕中间的dialog
 * <p>
 * modification history:
 */
public class AppCenterDialogFragment extends AppDialogFragment {

    /**
     * 不能放在onCreateDialog方法中，不然不会生效，需要另外在onCreateView中设置布局宽度为屏幕宽度
     * v.setMinimumWidth(getResources().getDisplayMetrics().widthPixels);
     */
    public void resizeDialogFragment() {
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
