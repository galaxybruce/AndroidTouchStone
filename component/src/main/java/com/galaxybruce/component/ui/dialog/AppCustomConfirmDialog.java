package com.galaxybruce.component.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.galaxybruce.component.R;
import com.galaxybruce.component.ui.jetpack.JPBaseDialogFragment;
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel;

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
public abstract class AppCustomConfirmDialog<VM extends JPBaseViewModel, B extends ViewDataBinding>
        extends JPBaseDialogFragment<VM, B>
        implements DialogInterface.OnKeyListener, DialogInterface.OnShowListener{

    public static class Builder<T extends Builder> {
        protected Bundle bundle;

        public Builder() {
            bundle = new Bundle();
        }

        /**
         * 设置callback，可以是实现了Parcelable接口的任何类
         * @param callback
         * @return
         */
        public T setCallback(Parcelable callback) {
            bundle.putParcelable("callback", callback);
            return (T)this;
        }

        public T setVisibleCancel(boolean visibleCancel) {
            bundle.putBoolean("isVisibleCancel", visibleCancel);
            return (T)this;
        }

        public T setVisibleConfirm(boolean visibleConfirm) {
            bundle.putBoolean("isVisibleConfirm", visibleConfirm);
            return (T)this;
        }

        public T setCancelText(String cancelText) {
            bundle.putString("cancelText", cancelText);
            return (T)this;
        }

        public T setConfirmText(String confirmText) {
            bundle.putString("confirmText", confirmText);
            return (T)this;
        }

        public T setCancelable(boolean cancelable) {
            bundle.putBoolean("cancelable", cancelable);
            return (T)this;
        }

    }

    protected View vLine;
    protected TextView btnCancel;
    protected TextView btnConfirm;

    private boolean cancelable;

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Dialog_NoFrame);
    }

    @Override
    protected View setRootLayout(int layoutId, @NonNull LayoutInflater inflater, ViewGroup container) {
        View contentView = super.setRootLayout(layoutId, inflater, null);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.app_custom_confirm_dialog, container, false);
        FrameLayout contentLayout = rootView.findViewById(R.id.dialog_content);
        contentLayout.addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return rootView;
    }

    @Override
    public void initData(@androidx.annotation.Nullable Bundle bundle, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.initData(bundle, savedInstanceState);

        Bundle arguments = getArguments();
        if(arguments != null) {
            cancelable = arguments.getBoolean("cancelable", true);
        }
    }

    @Override
    public void initView(@Nullable View view) {
        super.initView(view);

        vLine = view.findViewById(R.id.line);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnConfirm = view.findViewById(R.id.btnConfirm);

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

    @Override
    public void bindData(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.bindData(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String cancelText = bundle.getString("cancelText");
            String confirmText = bundle.getString("confirmText");
            boolean isVisibleCancel = bundle.getBoolean("isVisibleCancel", true);
            boolean isVisibleConfirm = bundle.getBoolean("isVisibleConfirm", true);

            if(!TextUtils.isEmpty(cancelText)) {
                btnCancel.setText(cancelText);
            }
            if(!TextUtils.isEmpty(confirmText)) {
                btnConfirm.setText(confirmText);
            }

            if (!isVisibleCancel) {
                btnCancel.setVisibility(View.GONE);
                if(vLine != null) {
                    vLine.setVisibility(View.GONE);
                }
            } else {
                btnCancel.setVisibility(View.VISIBLE);
            }
            if (!isVisibleConfirm) {
                btnConfirm.setVisibility(View.GONE);
                if(vLine != null) {
                    vLine.setVisibility(View.GONE);
                }
            } else {
                btnConfirm.setVisibility(View.VISIBLE);
            }
            if (isVisibleCancel && isVisibleConfirm) {
                if(vLine != null) {
                    vLine.setVisibility(View.VISIBLE);
                }
            }

            setCancelable(cancelable);
        }
    }

    protected void onCancelClick() {
        dismiss();
    }

    protected void onConfirmClick() {
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnKeyListener(this);
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (!cancelable
                && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE)) {
            return true;
        }
        return false;
    }

    @Override
    public void onShow(DialogInterface dialog) {

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
