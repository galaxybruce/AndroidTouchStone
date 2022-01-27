package com.galaxybruce.component.util;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

/**
 * Date: 2017/2/21 19:47
 * Author: bruce.zhang
 * Description: EditText最大长度限制
 * <p>
 * Modification  History:
 */
public class AppMaxTextLengthFilter implements InputFilter
    {
        private int mMaxLength;

        private Context mContext;
        private String mStrToast;

        public AppMaxTextLengthFilter(Context context, int maxLength, String toast)
        {
            super();

            mContext = context.getApplicationContext();
            mMaxLength = maxLength;
            mStrToast = toast;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
        {
            int keep = mMaxLength - (dest.length() - (dend - dstart));

            if (keep <= 0)
            {
                ToastUtils.showToast(mContext, mStrToast);
                return "";
            }
            else if (keep >= end - start)
            {
                return null; // keep original
            }
            else
            {
                return source.subSequence(start, start + keep);
            }
        }

    public int getMaxLength()
    {
        return mMaxLength;
    }

}
