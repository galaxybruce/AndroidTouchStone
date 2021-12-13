package com.galaxybruce.component.util.imageloader.scroll;

import android.app.Activity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.lang.ref.WeakReference;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 *  @描述 滚动时 控制图片加载器的  加载情况
 *  @作者
 *  @创建时间  2017/11/15 11:47
 *
**/
public class AppPauseGlideOnRecyclerScrollListener extends RecyclerView.OnScrollListener {

    public static AppPauseGlideOnRecyclerScrollListener createPauseOnFling(Activity context) {
        return new AppPauseGlideOnRecyclerScrollListener(Glide.with(context),
                false, true);
    }

    public static AppPauseGlideOnRecyclerScrollListener createPauseOnFling(Fragment fragment) {
        return new AppPauseGlideOnRecyclerScrollListener(Glide.with(fragment),
                false, true);
    }

    public static AppPauseGlideOnRecyclerScrollListener createPauseOnOnScrollAndFling(Activity context) {
        return new AppPauseGlideOnRecyclerScrollListener(Glide.with(context),
                true, true);
    }

    public static AppPauseGlideOnRecyclerScrollListener createPauseOnOnScrollAndFling(Fragment fragment) {
        return new AppPauseGlideOnRecyclerScrollListener(Glide.with(fragment),
                true, true);
    }

    private WeakReference<RequestManager> requestManager;
    private final boolean pauseOnScroll;
    private final boolean pauseOnFling;
    private final RecyclerView.OnScrollListener externalListener;

    public AppPauseGlideOnRecyclerScrollListener(RequestManager requestManager,
                                                 boolean pauseOnScroll, boolean pauseOnFling) {
        this(requestManager, pauseOnScroll, pauseOnFling, null);
    }

    public AppPauseGlideOnRecyclerScrollListener(RequestManager requestManager,
                                                 boolean pauseOnScroll, boolean pauseOnFling,
                                                 RecyclerView.OnScrollListener customListener) {
        this.requestManager = new WeakReference<>(requestManager);
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        this.externalListener = customListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState){
        switch(newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                if (requestManager.get() != null){
                    this.requestManager.get().resumeRequests();
                }
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                if(this.pauseOnScroll && requestManager.get() != null) {
                    requestManager.get().pauseRequests();
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                if(this.pauseOnFling && requestManager.get() != null) {
                    this.requestManager.get().pauseRequests();
                }
                break;
            default:
                break;
        }

        if(this.externalListener != null) {
            this.externalListener.onScrollStateChanged(recyclerView, newState);
        }
    }

    /**
     * Callback method to be invoked when the RecyclerView has been scrolled. This will be
     * called after the scroll has completed.
     * <p>
     * This callback will also be called if visible item range changes after a layout
     * calculation. In that case, dx and dy will be 0.
     *
     * @param recyclerView The RecyclerView which scrolled.
     * @param dx The amount of horizontal scroll.
     * @param dy The amount of vertical scroll.
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy){
        if(this.externalListener != null) {
            this.externalListener.onScrolled(recyclerView, dx, dy);
        }
    }

}
