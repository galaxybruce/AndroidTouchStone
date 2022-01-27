package com.galaxybruce.component.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ClickUtils;
import com.galaxybruce.component.R;
import com.galaxybruce.component.util.RxLifecycleUtils;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class AppLoadingDialog extends AppCenterDialog {

    @Override
    protected boolean supportMVVM() {
        return false;
    }

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

    @Override
    public int bindLayoutId() {
        return R.layout.app_dialog_loading;
    }

    @Override
    public void initView(@Nullable View view) {
        super.initView(view);

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

        final View closeView  = view.findViewById(R.id.img_close);
        if(closeView != null) {
            closeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissAllowingStateLoss();
                }
            });
        }

        View rootView = getView();
        if(rootView != null && closeView != null) {
            Observable.timer(20, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxLifecycleUtils.bindLifecycle(this))
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            closeView.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    @Override
    public void bindData(@Nullable Bundle savedInstanceState) {
        super.bindData(savedInstanceState);
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
