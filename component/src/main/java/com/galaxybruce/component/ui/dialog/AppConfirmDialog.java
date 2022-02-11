package com.galaxybruce.component.ui.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.galaxybruce.component.R;

import androidx.fragment.app.FragmentManager;

/**
 * @date 2021/3/30 14:53
 * @author bruce.zhang
 *
 * @description 确认|取消dialog
 *
 * 使用方式：
 *
 * <p>
 * modification history:
 */
public class AppConfirmDialog extends AppCustomConfirmDialog {

    @Override
    protected boolean supportMVVM() {
        return false;
    }

    public static class Builder {
        private String title;
        private String message;
        private boolean isHtml;
        private boolean includeUrl;
        private boolean isVisibleCancel = true;
        private boolean isVisibleConfirm = true;
        private String cancelText;
        private String confirmText;
        private boolean cancelable = true;
        private int gravity = Gravity.CENTER;
        private AppConfirmDialogCallback callback;


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessageGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setMessage(String message, boolean isHtml, boolean includeUrl) {
            this.message = message;
            this.isHtml = isHtml;
            this.includeUrl = includeUrl;
            return this;
        }

        public Builder setVisibleCancel(boolean visibleCancel) {
            isVisibleCancel = visibleCancel;
            return this;
        }

        public Builder setVisibleConfirm(boolean visibleConfirm) {
            isVisibleConfirm = visibleConfirm;
            return this;
        }

        public Builder setCancelText(String cancelText) {
            this.cancelText = cancelText;
            return this;
        }

        public Builder setConfirmText(String confirmText) {
            this.confirmText = confirmText;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setListener(AppConfirmDialogCallback callback) {
            this.callback = callback;
            return this;
        }

        public AppConfirmDialog create() {
            AppConfirmDialog dialog = new AppConfirmDialog();
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putInt("gravity", gravity);
            bundle.putString("message", message);
            bundle.putBoolean("isHtml", isHtml);
            bundle.putBoolean("includeUrl", includeUrl);
            bundle.putBoolean("cancelable", cancelable);
            bundle.putBoolean("isVisibleCancel", isVisibleCancel);
            bundle.putBoolean("isVisibleConfirm", isVisibleConfirm);
            bundle.putString("cancelText", cancelText);
            bundle.putString("confirmText", confirmText);
            dialog.setArguments(bundle);
            dialog.callback = callback;
            return dialog;
        }

        public void show(FragmentManager manager, String tag) {
            AppConfirmDialog dialog = create();
            dialog.show(manager, tag);
        }
    }

    public static AppConfirmDialog getInstance(String title, String message, boolean cancelable, AppConfirmDialogCallback callback) {
        return new AppConfirmDialog.Builder()
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setListener(callback)
                .create();
    }

    private TextView tvTitle;
    private TextView tvMessage;

    @Override
    public int bindContentLayoutId() {
        return R.layout.app_confirm_dialog;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        tvTitle = view.findViewById(R.id.tv_title);
        tvMessage = view.findViewById(R.id.tv_message);
    }

    @Override
    public void bindData(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.bindData(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        String title = bundle.getString("title");
        String message = bundle.getString("message");
        boolean isHtml = bundle.getBoolean("isHtml");
        boolean includeUrl = bundle.getBoolean("includeUrl");
        int gravity = bundle.getInt("gravity", -1);

        if(tvTitle != null) {
            if (!TextUtils.isEmpty(title)) {
                tvTitle.setText(title);
                tvTitle.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
            }
        }
        if (!TextUtils.isEmpty(message)) {
            tvMessage.setText(isHtml ? Html.fromHtml(message) : message);
        }
        if (isHtml && includeUrl) {
            tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
            clickLink();
        }
        if (gravity != -1) {
            tvMessage.setGravity(gravity);
        }
    }

    private void clickLink() {
        CharSequence str = tvMessage.getText();
        if (str instanceof Spannable) {
            int end = str.length();
            Spannable sp = (Spannable) str;
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);  //找出text中的a标签
            SpannableStringBuilder style = new SpannableStringBuilder(str);
            style.clearSpans();
            for (URLSpan url : urls) {
                style.setSpan(new TextViewClickSpan(url), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new NoUnderLineSpan(), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                style.setSpan(new ForegroundColorSpan(Color.parseColor("#CC0000")), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                style.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tvMessage.setText(style);
        }
    }

    private class TextViewClickSpan extends ClickableSpan {
        URLSpan urlSpan;

        public TextViewClickSpan(URLSpan urlSpan) {
            this.urlSpan = urlSpan;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
        }

        @Override
        public void onClick(View widget) {
            if (urlSpan != null && !TextUtils.isEmpty(urlSpan.getURL())) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(urlSpan.getURL());
                intent.setData(content_url);
                startActivity(intent);
            }
        }
    }

    private static class NoUnderLineSpan extends UnderlineSpan {
        public NoUnderLineSpan() {
        }

        public NoUnderLineSpan(Parcel src) {
            super(src);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

}
