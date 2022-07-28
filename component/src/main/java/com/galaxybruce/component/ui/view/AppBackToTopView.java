package com.galaxybruce.component.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.Objects;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by bruce.zhang
 */
public class AppBackToTopView extends AppCompatImageView {
    private View mScrollView;
    private int mVisiblePosition;
    private boolean mAutoHide = true;

    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    setVisibility(View.GONE);
                    setAlpha(1.0f);
                }
            }).start();
        }
    };

    public int getVisiblePosition() {
        return mVisiblePosition;
    }

    public void setAutoHide(boolean autoHide) {
        mAutoHide = autoHide;
    }

    public AppBackToTopView(Context context) {
        this(context, null);
    }

    public AppBackToTopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScrollView instanceof ListView) {
                    ((ListView) mScrollView).setSelection(0);

                } else if (mScrollView instanceof RecyclerView) {
                    RecyclerView recyclerView = ((RecyclerView) mScrollView);
                    //recyclerView.scrollToPosition(0);
                    Objects.requireNonNull(recyclerView.getLayoutManager()).smoothScrollToPosition(recyclerView, null, 0);
                }
                setVisibility(View.GONE);
            }
        });
    }

    /**
     * @param recyclerView
     * @param visiblePosition 到哪个位置显示回到顶部按钮
     */
    public void setRecyclerView(RecyclerView recyclerView, int visiblePosition) {
        mVisiblePosition = visiblePosition;
        mScrollView = recyclerView;
        recyclerView.addOnScrollListener(onRecyclerViewScrollListener);
    }

    /**
     * @param listView
     * @param visiblePosition  到哪个位置显示回到顶部按钮
     * @param onScrollListener listView外面设置的OnScrollListener
     */
    public void setListView(AbsListView listView, int visiblePosition, AbsListView.OnScrollListener onScrollListener) {
        mVisiblePosition = visiblePosition;
        mScrollView = listView;
        listView.setOnScrollListener(new BackToTopScrollListener(listView, onScrollListener, AppBackToTopView.this));
    }

    private void show() {
        removeCallbacks(mHideRunnable);
        setVisibility(View.VISIBLE);
    }

    private void autoHide() {
        if (mAutoHide) {
            removeCallbacks(mHideRunnable);
            postDelayed(mHideRunnable, 3000);
        }
    }

    private RecyclerView.OnScrollListener onRecyclerViewScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem >= mVisiblePosition) {
                    show();
                    autoHide();
                } else {
                    setVisibility(View.GONE);
                }
            }
        }
    };

    private static class BackToTopScrollListener implements AbsListView.OnScrollListener {
        private final AbsListView.OnScrollListener externalListener;
        private final AbsListView mListView;
        private final WeakReference<AppBackToTopView> mBackToTopView;

        public BackToTopScrollListener(AbsListView interceptListView, AbsListView.OnScrollListener externalListener, AppBackToTopView backToTopView) {
            this.mListView = interceptListView;
            this.externalListener = externalListener;
            this.mBackToTopView = new WeakReference<>(backToTopView);
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (this.externalListener != null) {
                this.externalListener.onScrollStateChanged(view, scrollState);
            }

            if (scrollState == SCROLL_STATE_IDLE) {
                int firstVisible = mListView.getFirstVisiblePosition();
                AppBackToTopView backToTopView = mBackToTopView.get();
                if (backToTopView != null) {
                    if (firstVisible > backToTopView.getVisiblePosition()) {
                        backToTopView.show();
                        backToTopView.autoHide();
                    } else {
                        backToTopView.setVisibility(View.GONE);
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (this.externalListener != null) {
                this.externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
    }
}
