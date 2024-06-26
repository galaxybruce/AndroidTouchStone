package com.galaxybruce.component.ui.dialog;

import android.os.Bundle;
import android.view.View;

import com.galaxybruce.component.R;

import org.jetbrains.annotations.Nullable;

/**
 * @date 2021/3/30 14:53
 * @author bruce.zhang
 * @description 单纯输入dialog
 *
 * 使用方式：
 * new AppSimpleInputDialog.Builder()
 *      .tag("")
 *      .title("哈哈哈")
 *      .inputType4Integer()
 *      .setCallback(ISimpleInputDialogCallback)
 *      .build()
 *      .show(getSupportFragmentManager(), "AppSimpleInputDialog");
 *
 * <p>
 * modification history:
 */
public class AppSimpleInputDialog extends AppBaseInputDialog {

    public interface ISimpleInputDialogCallback extends AppDialogCallback {
        void onInputDialogCallback(String tag, String content);
    }

    ISimpleInputDialogCallback mCallback;

    public static final class Builder extends AppBaseInputDialog.Builder<Builder> {

        public Builder() {
            super();
        }

        public AppSimpleInputDialog build() {
            AppSimpleInputDialog dialog = new AppSimpleInputDialog();
            // 自定义确定按钮文本
//          bundle.putString(KEY_CONFIRM_TEXT, "发送到直播间");
            dialog.setArguments(bundle);
            return dialog;
        }
    }

    @Override
    public int bindLayoutId() {
        return R.layout.app_simple_input_dialog_layout;
    }

    @Override
    public void initData(@Nullable Bundle bundle, Bundle savedInstanceState) {
        super.initData(bundle, savedInstanceState);
        // 获取listener
        mCallback = getDialogListener(this, ISimpleInputDialogCallback.class);
        if(bundle != null) {
            ISimpleInputDialogCallback tCallback = bundle.getParcelable("callback");
            if(tCallback != null) {
                mCallback = tCallback;
            }
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);
    }

    @Override
    protected void onConfirmClick() {
        if(mCallback != null) {
            mCallback.onInputDialogCallback(mTag == null ? "" : mTag,
                    mEditText.getText().toString().trim());
        }
        super.onConfirmClick();
    }

}
