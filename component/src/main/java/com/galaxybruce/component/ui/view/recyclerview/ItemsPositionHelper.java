package com.galaxybruce.component.ui.view.recyclerview;

import android.view.View;
import android.widget.ListView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Date: 2017/2/28 13:54
 * Author: bruce.zhang
 * Description: RecyclerView和ListView 的position帮助类
 * <p>
 * Modification  History:
 */
public class ItemsPositionHelper {

    public static int getLastVisiblePosition(RecyclerView recyclerView) {
        int position;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastPositions = staggeredGridLayoutManager.findLastVisibleItemPositions(new int[staggeredGridLayoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = layoutManager.getItemCount() - 1;
        }
        return position;
    }

    public int getFirstVisiblePosition(RecyclerView recyclerView) {
        int position;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            position = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastPositions = staggeredGridLayoutManager.findFirstVisibleItemPositions(new int[staggeredGridLayoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    public static int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    public int getMinPositions(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            minPosition = Math.min(minPosition, positions[i]);
        }
        return minPosition;
    }

    /**
     * 根据position获取listView中的view
     * @param pos
     * @param listView
     * @return
     */
    public static View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
