package com.galaxybruce.component.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.galaxybruce.component.R;
import com.galaxybruce.component.util.NetworkUtil;


/**
 * created by bruce.zhang
 */
public class AppEmptyLayout extends FrameLayout {

    public static final int NETWORK_ERROR = 1;
    public static final int NETWORK_LOADING = 2;
    public static final int NO_DATA = 3;
    public static final int HIDE_LAYOUT = 4;

    private final Context context;
    private LinearLayout llContent;
    private ImageView img;
    private TextView tvMsg;
    private ProgressBar animProgress;
    private OnClickListener listener;
    private OnClickListener tvButtonListener;

    private int mErrorState = HIDE_LAYOUT;
    private String strNoDataContent = "";
    private int noDataImage;

    public AppEmptyLayout(Context context) {
        this(context, null);
    }

    public AppEmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        setClickable(true);
        setBackgroundResource(R.color.windowBackground);

        View view = View.inflate(context, R.layout.app_view_error_layout, this);
        llContent = (LinearLayout) view.findViewById(R.id.ll_content);
        img = (ImageView) view.findViewById(R.id.img_error_layout);
        tvMsg = (TextView) view.findViewById(R.id.tv_error_layout);
        animProgress = (ProgressBar) view.findViewById(R.id.animProgress);

        llContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });

    }

    public void dismiss() {
        setErrorType(HIDE_LAYOUT);
    }

    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == NETWORK_LOADING;
    }

    public boolean isHide() {
        return mErrorState == HIDE_LAYOUT;
    }

    public void setErrorType(int state) {
        mErrorState = state;
        animate().cancel();

        switch (state) {
            case NETWORK_ERROR:
                if (NetworkUtil.hasInternet(getContext())) {
                    tvMsg.setText(R.string.app_error_view_load_error_click_to_refresh);
                } else {
                    tvMsg.setText(R.string.app_error_view_network_error_click_to_refresh);
                }
//            BBSAttrResolveUtil.resolveAttrBackgroundRes(getContext(), img, R.attr.bbs_load_icon_failed);
                img.setBackgroundResource(R.drawable.app_data_error_icon);

                setVisibility(View.VISIBLE);
                tvMsg.setVisibility(View.VISIBLE);
                img.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                break;

            case NETWORK_LOADING:
                setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
                tvMsg.setVisibility(View.GONE);
                break;

            case NO_DATA:
                setVisibility(View.VISIBLE);
                img.setVisibility(View.VISIBLE);
                tvMsg.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                setNoDataImage();
                setTvNoDataContent();
                break;

            case HIDE_LAYOUT:
//            setVisibility(View.GONE);
                if (getVisibility() != View.GONE) {
                    animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            setVisibility(View.GONE);
                            setAlpha(1.0f);
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }

    public void setMessage(String msg) {
        tvMsg.setText(msg);
    }

    public void setImage(int imgResource) {
        img.setBackgroundResource(imgResource);
    }

    public void setNoDataContent(String noDataContent) {
        strNoDataContent = noDataContent;
    }

    public void setNoDataTextSize(float sp){
        if (tvMsg != null) {
            tvMsg.setTextSize(sp);
        }
    }

    public void setNoDataImage(int image) {
        noDataImage = image;
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setOnButtonClickListener(OnClickListener tvButtonListener) {
        this.tvButtonListener = tvButtonListener;
    }

    public void setTvNoDataContent() {
        if (!strNoDataContent.equals("")) {
            tvMsg.setText(strNoDataContent);
        } else {
            tvMsg.setText(R.string.app_error_view_no_data);
        }
    }

    public void setNoDataImage() {
        if (noDataImage > 0) {
            img.setBackgroundResource(noDataImage);
        } else {
//            BBSAttrResolveUtil.resolveAttrBackgroundRes(getContext(), img, R.attr.bbs_load_icon_noData);
            img.setBackgroundResource(R.drawable.app_data_empty_icon);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            mErrorState = HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }

    public void setContentGravity(int gravity) {
        if (gravity == Gravity.TOP) {
            LayoutParams params = (LayoutParams) llContent.getLayoutParams();
            params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            params.topMargin = dip2px(context, 100);
        } else {
            LayoutParams params = (LayoutParams) llContent.getLayoutParams();
            params.gravity = Gravity.CENTER;
            params.topMargin = 0;
        }
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void setContentClickable(boolean clickable) {
        llContent.setClickable(clickable);
    }

}
