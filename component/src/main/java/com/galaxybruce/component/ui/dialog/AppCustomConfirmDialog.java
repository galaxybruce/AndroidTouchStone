package com.galaxybruce.component.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
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
public abstract class AppCustomConfirmDialog<B extends ViewDataBinding> extends JPBaseDialogFragment<B>
        implements DialogInterface.OnKeyListener, DialogInterface.OnShowListener{

    public abstract static class AppConfirmDialogCallback extends AppDialogCallback {

        public abstract void onCancel();

        public abstract void onConfirm();
    }

    public static class Builder<T extends Builder> {
        protected Bundle bundle;

        public Builder() {
            bundle = new Bundle();
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

        public T setCallback(AppConfirmDialogCallback callback) {
            bundle.putParcelable("callback", callback);
            return (T)this;
        }
    }

    protected AppConfirmDialogCallback callback;

    protected View vLine;
    protected TextView btnCancel;
    protected TextView btnConfirm;

    private boolean isVisibleCancel;
    private boolean isVisibleConfirm;
    private boolean cancelable;

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
    public void initData(@androidx.annotation.Nullable Bundle bundle, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.initData(bundle, savedInstanceState);

        Bundle arguments = getArguments();
        if(arguments != null) {
            cancelable = arguments.getBoolean("cancelable", true);
            isVisibleCancel = arguments.getBoolean("isVisibleCancel", true);
            isVisibleConfirm = arguments.getBoolean("isVisibleConfirm", true);
            callback = arguments.getParcelable("callback");
        }
    }

    @Override
    public void initView(@Nullable View view) {
        super.initView(view);

        if(!supportMVVM()) {
            FrameLayout contentLayout = view.findViewById(R.id.dialog_content);
            LayoutInflater.from(getContext()).inflate(bindContentLayoutId(), contentLayout, true);
        }

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

        AppConfirmDialogCallback confirmDialogCallback = getDialogListener(this, AppConfirmDialogCallback.class);
        if(confirmDialogCallback != null) {
            this.callback = confirmDialogCallback;
        }
    }

    @Override
    public void bindData(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.bindData(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String cancelText = bundle.getString("cancelText");
            String confirmText = bundle.getString("confirmText");
            cancelable = bundle.getBoolean("cancelable", true);
            isVisibleCancel = bundle.getBoolean("isVisibleCancel", true);
            isVisibleConfirm = bundle.getBoolean("isVisibleConfirm", true);

            if(!TextUtils.isEmpty(cancelText)) {
                btnCancel.setText(cancelText);
            }
            if(!TextUtils.isEmpty(confirmText)) {
                btnConfirm.setText(confirmText);
            }

            if (!isVisibleCancel) {
                btnCancel.setVisibility(View.GONE);
                vLine.setVisibility(View.GONE);
            } else {
                btnCancel.setVisibility(View.VISIBLE);
            }
            if (!isVisibleConfirm) {
                btnConfirm.setVisibility(View.GONE);
                vLine.setVisibility(View.GONE);
            } else {
                btnConfirm.setVisibility(View.VISIBLE);
            }
            if (isVisibleCancel && isVisibleConfirm) {
                vLine.setVisibility(View.VISIBLE);
            }

            setCancelable(cancelable);
        }
    }

    protected void onCancelClick() {
        if(callback != null) {
            callback.onCancel();
        }
        dismiss();
    }

    protected void onConfirmClick() {
        if(callback != null) {
            callback.onConfirm();
        }
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
