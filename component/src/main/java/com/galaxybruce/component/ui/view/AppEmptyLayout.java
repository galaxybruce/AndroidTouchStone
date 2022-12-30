package com.galaxybruce.component.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.galaxybruce.component.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;


/**
 * created by bruce.zhang
 */
public class AppEmptyLayout extends FrameLayout {

    public static final int NETWORK_ERROR = 1;
    public static final int NETWORK_LOADING = 2;
    public static final int NO_DATA = 3;
    public static final int HIDE_LAYOUT = 4;

    @IntDef({NETWORK_ERROR, NETWORK_LOADING, NO_DATA, HIDE_LAYOUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AppEmptyLayoutState {
    }

    private LinearLayout contentLayout;
    private ImageView imgError;
    private TextView tvError;
    private ProgressBar animProgress;
    private OnClickListener retryListener;

    private int errorState = NETWORK_LOADING;
    private String strNoDataContent = "";
    private int noDataImage;

    public AppEmptyLayout(Context context) {
        this(context, null);
    }

    public AppEmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setClickable(true);
        setBackgroundResource(R.color.windowBackground);

        View view = View.inflate(context, R.layout.app_view_error_layout, this);
        contentLayout = (LinearLayout) view.findViewById(R.id.content_layout);
        imgError = (ImageView) view.findViewById(R.id.img_error);
        tvError = (TextView) view.findViewById(R.id.tv_error);
        animProgress = (ProgressBar) view.findViewById(R.id.anim_progress);

        contentLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (retryListener != null && errorState != NETWORK_LOADING) {
                    retryListener.onClick(v);
                }
            }
        });
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            errorState = HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }

    public void dismiss() {
        setErrorType(HIDE_LAYOUT);
    }

    public int getErrorState() {
        return errorState;
    }

    public boolean isLoadError() {
        return errorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return errorState == NETWORK_LOADING;
    }

    public boolean isHide() {
        return errorState == HIDE_LAYOUT;
    }

    public void setErrorType(@AppEmptyLayoutState int state) {
        errorState = state;
        animate().cancel();

        switch (state) {
            case NETWORK_ERROR:
                if (NetworkUtils.isConnected()) {
                    tvError.setText(R.string.app_error_view_load_error_click_to_refresh);
                } else {
                    tvError.setText(R.string.app_error_view_network_error_click_to_refresh);
                }
//            BBSAttrResolveUtil.resolveAttrBackgroundRes(getContext(), img, R.attr.bbs_load_icon_failed);
                imgError.setBackgroundResource(R.drawable.app_data_error_icon);

                setVisibility(View.VISIBLE);
                tvError.setVisibility(View.VISIBLE);
                imgError.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                break;

            case NETWORK_LOADING:
                setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.VISIBLE);
                imgError.setVisibility(View.GONE);
                tvError.setVisibility(View.GONE);
                break;

            case NO_DATA:
                setVisibility(View.VISIBLE);
                imgError.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                updateNoDataImage();
                updateNoDataContent();
                break;

            case HIDE_LAYOUT:
                if (getVisibility() != View.GONE) {
                    animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(errorState == HIDE_LAYOUT) {
                                setVisibility(View.GONE);
                                setAlpha(1.0f);
                            }
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }

    public void setNoDataContent(String noDataContent) {
        strNoDataContent = noDataContent;
    }

    public void setNoDataTextSize(float sp){
        if (tvError != null) {
            tvError.setTextSize(sp);
        }
    }

    public void setNoDataImage(int image) {
        noDataImage = image;
    }

    public void updateNoDataContent(String msg) {
        tvError.setText(msg);
    }

    public void updateNoDataContent() {
        if (!TextUtils.isEmpty(strNoDataContent)) {
            tvError.setText(strNoDataContent);
        } else {
            tvError.setText(R.string.app_error_view_no_data);
        }
    }

    public void updateNoDataImage(int imgResource) {
        imgError.setBackgroundResource(imgResource);
    }

    public void updateNoDataImage() {
        if (noDataImage > 0) {
            imgError.setBackgroundResource(noDataImage);
        } else {
//            BBSAttrResolveUtil.resolveAttrBackgroundRes(getContext(), img, R.attr.bbs_load_icon_noData);
            imgError.setBackgroundResource(R.drawable.app_data_empty_icon);
        }
    }

    /**
     * 设置内容距离顶部大小
     * @param topMargin
     */
    public void setContentTopMargin(int topMargin) {
        LayoutParams params = (LayoutParams) contentLayout.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.topMargin = topMargin;
    }

    /**
     * 设置内容居中
     */
    public void setContentCenter() {
        LayoutParams params = (LayoutParams) contentLayout.getLayoutParams();
        params.gravity = Gravity.CENTER;
    }

    public void setRetryClickListener(OnClickListener listener) {
        this.retryListener = listener;
    }

}
