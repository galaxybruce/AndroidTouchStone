package com.galaxybruce.component.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.galaxybruce.component.R;


/**
 * Created by bruce.zhang on 2016/9/12.
 */
public class AppListFooterView extends LinearLayout {

    public ProgressBar progress;
    public  TextView text;

    private int _loadMoreText;
    private int _loadFinishText;
    private int _noDateText;

    public AppListFooterView(Context context) {
        this(context, null);
    }

    public AppListFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppListFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        _loadMoreText = R.string.loading_more;
        _loadFinishText = R.string.loading_no_more;
        _noDateText = R.string.loading_no_data;

        final View rootView = LayoutInflater.from(context).inflate(R.layout.app_list_footer_loadmore, this);
        progress = (ProgressBar) rootView.findViewById(R.id.progressbar);
        text = (TextView) rootView.findViewById(R.id.text);

    }

    public void setState(final int state, final Drawable loadMoreBg) {
        setBackground(loadMoreBg);

        switch (state) {
            case AppListAdapterLoadDataState.STATE_LOAD_MORE:
            case AppListAdapterLoadDataState.STATE_FORCE_LOAD_MORE:
                setFooterViewLoading();
                break;
            case AppListAdapterLoadDataState.STATE_NO_MORE:
                setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
                SpanUtils.with(text)
                        .appendImage(R.drawable.icon_nore_more_left, SpanUtils.ALIGN_CENTER)
                        .appendSpace(SizeUtils.dp2px(4))
                        .append(getContext().getString(_loadFinishText))
                        .appendSpace(SizeUtils.dp2px(4))
                        .appendImage(R.drawable.icon_nore_more_right, SpanUtils.ALIGN_CENTER)
                        .create();
                break;
            case AppListAdapterLoadDataState.STATE_EMPTY_ITEM:
                setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
                text.setText(_noDateText);
                break;
            case AppListAdapterLoadDataState.STATE_NETWORK_ERROR:
                setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
                if (hasInternet()) {
                    text.setText("加载出错了");
                } else {
                    text.setText("没有可用的网络");
                }
                break;
            default:
                setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
                break;
        }
    }

    public void setFooterViewLoading() {
        setFooterViewLoading("");
    }

    public void setFooterViewLoading(String loadMsg) {
        setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(loadMsg)) {
            text.setText(_loadMoreText);
        } else {
            text.setText(loadMsg);
        }
    }

    public void setFooterViewText(String msg) {
        setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        text.setVisibility(View.VISIBLE);
        text.setText(msg);
    }

    public void setLoadMoreText(int loadMoreText) {
        _loadMoreText = loadMoreText;
    }

    public void setLoadFinishText(int loadFinishText) {
        _loadFinishText = loadFinishText;
    }

    public void setNoDataText(int noDataText) {
        _noDateText = noDataText;
    }

    public boolean hasInternet() {
        boolean flag;
        if (((ConnectivityManager) getContext().getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }
}
