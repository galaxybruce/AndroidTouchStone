package com.galaxybruce.component.ui.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.galaxybruce.component.R;
import com.galaxybruce.component.ui.jetpack.JPBaseDialogFragment;

import org.jetbrains.annotations.Nullable;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

/**
 * @date 2020/7/17 19:18
 * @author bruce.zhang
 * @description  带"确定"和"取消"按钮的自定义dialog
 * <p>
 * modification history:
 */
public abstract class AppCustomConfirmDialog<B extends ViewDataBinding> extends JPBaseDialogFragment<B> {

    /**
     * 确定和取消按钮的文本通过bundle传进来
     */
    protected static final String KEY_CANCEL_TEXT = "KEY_CANCEL_TEXT";
    protected static final String KEY_CONFIRM_TEXT = "KEY_CONFIRM_TEXT";

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Dialog_NoFrame);
    }

    @Override
    protected View setRootLayout(int layoutId, @NonNull LayoutInflater inflater, ViewGroup container) {
        if(supportMVVM()) {
            View contentView = super.setRootLayout(bindContentLayoutId(), inflater, null);
            View rootView = LayoutInflater.from(getContext()).inflate(layoutId, container, false);
            FrameLayout contentLayout = rootView.findViewById(R.id.dialog_content);
            contentLayout.addView(contentView);
            return rootView;
        } else {
            return super.setRootLayout(layoutId, inflater, container);
        }
    }

    @Override
    public final int bindLayoutId() {
        return R.layout.app_custom_confirm_dialog;
    }

    public abstract int bindContentLayoutId();

    @Override
    public void initView(@Nullable View view) {
        super.initView(view);

        if(!supportMVVM()) {
            FrameLayout contentLayout = view.findViewById(R.id.dialog_content);
            LayoutInflater.from(getContext()).inflate(bindContentLayoutId(), contentLayout, true);
        }

        final TextView btnCancel = view.findViewById(R.id.btnCancel);
        final TextView btnConfirm = view.findViewById(R.id.btnConfirm);
        if(getArguments() != null && !TextUtils.isEmpty(getArguments().getString(KEY_CANCEL_TEXT))) {
            btnCancel.setText(getArguments().getString(KEY_CANCEL_TEXT));
        }
        if(getArguments() != null && !TextUtils.isEmpty(getArguments().getString(KEY_CONFIRM_TEXT))) {
            btnConfirm.setText(getArguments().getString(KEY_CONFIRM_TEXT));
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmClick();
            }
        });
    }

    protected void onCancelClick() {
        dismiss();
    }

    protected void onConfirmClick() {
        dismiss();
    }

}
