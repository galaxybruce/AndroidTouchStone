package com.galaxybruce.component.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.galaxybruce.component.R;
import com.galaxybruce.component.util.AppMaxTextLengthFilter;
import com.galaxybruce.component.util.ToastUtils;

import org.jetbrains.annotations.Nullable;

import androidx.databinding.ViewDataBinding;

/**
 * @date 2021/3/30 14:53
 * @author bruce.zhang
 *
 * @description 支持输入框并且自动弹出的键盘的dialog。参考子类{@link AppSimpleInputDialog}
 *
 * 使用方式：
 * new AppSimpleInputDialog.Builder()
 *      .tag("")
 *      .title("哈哈哈")
 *      .inputType4Integer()
 *      .build().show(getSupportFragmentManager(), "AppSimpleInputDialog");
 *
 * <p>
 * modification history:
 */
public abstract class AppBaseInputDialog<B extends ViewDataBinding> extends AppCustomConfirmDialog<B> {

    @Override
    protected boolean supportMVVM() {
        return false;
    }

    /**
     * 不能放在onCreateDialog方法中，不然不会生效，需要另外在onCreateView中设置布局宽度为屏幕宽度
     * v.setMinimumWidth(getResources().getDisplayMetrics().widthPixels);
     */
    @Override
    public void resizeDialogFragment() {
        Dialog dialog = getDialog();
        if(dialog != null) {
            Window win = dialog.getWindow();
            if (win != null) {
                WindowManager.LayoutParams params = win.getAttributes();
                params.gravity = Gravity.CENTER;
                params.width = Resources.getSystem().getDisplayMetrics().widthPixels * 80 / 100;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                win.setAttributes(params);
//            dialog.onWindowAttributesChanged(params);
                dialog.setCanceledOnTouchOutside(true);

                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    // android 11不用这个会闪动
                    win.getDecorView().post(new Runnable() {
                        @Override
                        public void run() {
                            showKeyboard(win, mEditText);
                        }
                    });
                } else {
                    // android 11以下，放在post中键盘无法自动显示
                    showKeyboard(win, mEditText);
                }
            }
        }
    }

    private void showKeyboard(Window win, EditText editText) {
        // 修复bug : setSpan (2 ... 2) ends beyond length 0
        if(editText.getText().length() > 0) {
            editText.setSelection(editText.getText().length());
            Selection.selectAll(editText.getText());
        }
        editText.requestFocus();

        // 不设置这个，键盘不自动弹出
        win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Context context = getContext();
        if(context != null) {
            editText.setFilters(new InputFilter[]{new AppMaxTextLengthFilter(context,
                    mMaxLength, "最多只能输入" + mMaxLength + "个字符")});

            // 弹出dialog逻辑必须放在post中，不然会出现闪动。基本思路是确保先定位在底部，然后再被键盘顶出。
            InputMethodManager manager = ((InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE));
            if (manager != null) {
                manager.showSoftInput(editText, 0);
                manager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        }
    }


    String mTag;
    String mTitle;
    String mContent;
    String mInputHint;
    protected int mInputType;
    protected int mMaxLength;
    protected int mMinLength;
    private int mLines;
    protected double mMaxValue;
    protected double mMinValue;

    protected EditText mEditText;

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

        /**
         * tag，用于区分一个页面多个输入框的场景
         * @return
         */
        public T tag(String var) {
            bundle.putString("tag", var);
            return (T)this;
        }

        public T title(String val) {
            bundle.putString("title", val);
            return (T)this;
        }

        public T content(String val) {
            bundle.putString("content", val);
            return (T)this;
        }

        public T maxLength(int val) {
            bundle.putInt("maxLength", val);
            return (T)this;
        }

        public T minLength(int val) {
            bundle.putInt("minLength", val);
            return (T)this;
        }

        public T maxValue(double val) {
            bundle.putDouble("maxValue", val);
            return (T)this;
        }

        public T minValue(double val) {
            bundle.putDouble("minValue", val);
            return (T)this;
        }

        public T inputHint(String val) {
            bundle.putString("inputHint", val);
            return (T)this;
        }

        /**
         * 文本键盘，文本时可以允许多行
         * @return
         */
        public T inputType4Text(int lines) {
            bundle.putInt("inputType", 1);
            bundle.putInt("lines", lines);
            return (T)this;
        }

        /**
         * 整数键盘
         * @return
         */
        public T inputType4Integer() {
            bundle.putInt("inputType", 2);
            return (T)this;
        }

        /**
         * 小数键盘
         * @return
         */
        public T inputType4Decimal() {
            bundle.putInt("inputType", 3);
            return (T)this;
        }

    }

    @Override
    public void initData(@Nullable Bundle bundle, Bundle savedInstanceState) {
        super.initData(bundle, savedInstanceState);

        Bundle arguments = getArguments();
        if(arguments != null) {
            mTag = arguments.getString("tag");
            mTitle = arguments.getString("title");
            mContent = arguments.getString("content");
            mInputType = arguments.getInt("inputType", 1);
            mMaxLength = arguments.getInt("maxLength", Integer.MAX_VALUE);
            mMinLength = arguments.getInt("minLength", 0);
            mLines = arguments.getInt("lines", 1);
            mMaxValue = arguments.getDouble("maxValue", Double.MAX_VALUE);
            mMinValue = arguments.getDouble("minValue", Double.NEGATIVE_INFINITY);
            mInputHint = arguments.getString("inputHint");
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ((TextView)view.findViewById(R.id.tv_title)).setText(TextUtils.isEmpty(mTitle) ? "输入" : mTitle);
        mEditText = view.findViewById(R.id.et_content);
        mEditText.setText(mContent);
        if(mInputHint != null) {
            mEditText.setHint(mInputHint);
        }

        if (mInputType == 2) {
            mEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        } else if (mInputType == 3) {
            mEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            if(mLines > 1) {
                mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                // 必须设置成false，否则不会换行
                mEditText.setSingleLine(false);
                mEditText.setGravity(Gravity.START | Gravity.TOP);
            } else {
                mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                mEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            }
        }
        // 必须放在setInputType后面，否则setMaxLines没有效果
        mEditText.setLines(mLines);
        mEditText.setMaxLines(mLines);
    }

    /**
     * 检查数据有效性默认逻辑
     * @return
     */
    protected boolean checkData() {
        String content = mEditText.getText().toString().trim();
        if(mMinLength > 0 && content.trim().length() < mMinLength) {
            ToastUtils.showToast(getContext(), "最少输入" + mMinLength + "个字符");
            return false;
        }
        try {
            double contentValue = Double.parseDouble(content);
            if(contentValue > mMaxValue) {
                ToastUtils.showToast(getContext(), "超过最大值: " + mMaxValue);
                return false;
            } else if(contentValue < mMinValue) {
                ToastUtils.showToast(getContext(), "必须大于最小值: " + mMinValue);
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
