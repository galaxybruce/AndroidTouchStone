package com.galaxybruce.component.util.imageloader.scroll;

import android.content.Context;
import android.widget.AbsListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.lang.ref.WeakReference;

import androidx.fragment.app.Fragment;

/**
 *
 *  @描述 滚动时 控制图片加载器的  加载情况
 *  @作者 bruce.zhang
 *  @创建时间  2017/11/15 11:47
 *
**/
public class AppPauseGlideOnListViewScrollListener implements AbsListView.OnScrollListener {

    public static AppPauseGlideOnListViewScrollListener createPauseOnFling(Context context) {
        return new AppPauseGlideOnListViewScrollListener(Glide.with(context), false, true);
    }

    public static AppPauseGlideOnListViewScrollListener createPauseOnFling(Fragment fragment) {
        return new AppPauseGlideOnListViewScrollListener(Glide.with(fragment), false, true);
    }

    public static AppPauseGlideOnListViewScrollListener createPauseOnScrollAndFling(Context context) {
        return new AppPauseGlideOnListViewScrollListener(Glide.with(context), true, true);
    }

    public static AppPauseGlideOnListViewScrollListener createPauseOnScrollAndFling(Fragment fragment) {
        return new AppPauseGlideOnListViewScrollListener(Glide.with(fragment), true, true);
    }

    public static AppPauseGlideOnListViewScrollListener createPauseOnScrollAndFling(Context context,
                                                                                      AbsListView.OnScrollListener customListener) {
        return new AppPauseGlideOnListViewScrollListener(Glide.with(context), true,
                true, customListener);
    }

    public static AppPauseGlideOnListViewScrollListener createPauseOnOnScrollAndFling(Fragment fragment,
                                                                                      AbsListView.OnScrollListener customListener) {
        return new AppPauseGlideOnListViewScrollListener(Glide.with(fragment), true,
                true, customListener);
    }

    private WeakReference<RequestManager> requestManager;
    private final boolean pauseOnScroll;
    private final boolean pauseOnFling;
    private final AbsListView.OnScrollListener externalListener;

    public AppPauseGlideOnListViewScrollListener(RequestManager requestManager, boolean pauseOnScroll, boolean pauseOnFling) {
        this(requestManager, pauseOnScroll, pauseOnFling, null);
    }

    public AppPauseGlideOnListViewScrollListener(RequestManager requestManager, boolean pauseOnScroll, boolean pauseOnFling,
                                                 AbsListView.OnScrollListener customListener) {
        this.requestManager = new WeakReference<>(requestManager);
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        this.externalListener = customListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int newState) {
        switch(newState) {
            case SCROLL_STATE_IDLE:
                if (requestManager.get() != null){
                    this.requestManager.get().resumeRequests();
                }
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                if(this.pauseOnScroll && requestManager.get() != null) {
                    requestManager.get().pauseRequests();
                }
                break;
            case SCROLL_STATE_FLING:
                if(this.pauseOnFling && requestManager.get() != null) {
                    this.requestManager.get().pauseRequests();
                }
                break;
            default:
                break;
        }

        if(this.externalListener != null) {
            this.externalListener.onScrollStateChanged(absListView, newState);
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(this.externalListener != null) {
            this.externalListener.onScroll(absListView, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
