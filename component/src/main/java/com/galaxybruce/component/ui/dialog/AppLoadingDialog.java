package com.galaxybruce.component.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ViewUtils;
import com.galaxybruce.component.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AppLoadingDialog extends AppCenterDialogFragment {

    public static AppLoadingDialog getInstance() {
        return new AppLoadingDialog();
    }

    public static AppLoadingDialog getInstance(String message) {
        AppLoadingDialog dialog = new AppLoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        dialog.setArguments(bundle);
        return dialog;
    }

    TextView tvMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog_NoFrame);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.app_dialog_loading, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvMessage = view.findViewById(R.id.tv_message);

        // 防止某些情况，loading框不消失，手动点击3下消失
        final View loadingContainer = view.findViewById(R.id.loading_container);
        if(loadingContainer != null) {
            loadingContainer.setOnClickListener(new ClickUtils.OnMultiClickListener(3) {
                @Override
                public void onTriggerClick(View v) {
                    dismissAllowingStateLoss();
                }

                @Override
                public void onBeforeTriggerClick(View v, int count) {

                }
            });
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null && getView() != null) {
            String message = getArguments().getString("message");
            if (!TextUtils.isEmpty(message)) {
                TextView textView = getView().findViewById(R.id.tv_message);
                textView.setVisibility(View.VISIBLE);
                textView.setText(message);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 更新信息
     * @param msg
     */
    public void updateMessage(String msg) {
        if(tvMessage != null) {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(msg);
        }
    }
}
