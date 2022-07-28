package com.galaxybruce.component.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView base adapter
 * created by bruce.zhang
 */
public abstract class AppBaseRecyclerAdapter<T>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements IAppListAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected Resources mResources;
    protected List<T> mDataList = new ArrayList<T>();

    public AppBaseRecyclerAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResources = context.getResources();
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getHeaderViewCount() {
        return 0;
    }

    public void setData(List<T> data) {
        mDataList = data;
        notifyDataSetChanged();
    }

    public void addData(T data) {
        int position = getDataSize();
        if (mDataList != null) {
            mDataList.add(data);
        }
        notifyItemInserted(position + getHeaderViewCount());
    }

    public void addData(int index, T data) {
        if (mDataList != null) {
            mDataList.add(index, data);
        }
        notifyItemInserted(index + getHeaderViewCount());
    }

    public void addData(List<T> data) {
        addData(data, true);
    }

    public void addData(List<T> data, boolean refresh) {
        addData(-1, data, refresh);
    }

    public void addData(int position, List<T> data, boolean refresh) {
        if (data == null) return;

        try {
            int count = data.size();
            if (mDataList != null) {
                if (position >= 0) {
                    mDataList.addAll(position, data);
                } else {
                    mDataList.addAll(data);
                }
                if (refresh) {
//						notifyItemRangeInserted(position, count);
                    notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeData(T data) {
        if (mDataList != null) {
            mDataList.remove(data);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        clear(true);
    }

    public void clear(boolean refresh) {
        if (mDataList != null) {
            mDataList.clear();
        }
        if (refresh) {
            notifyDataSetChanged();
        }
    }

    public int getDataSize() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public List<T> getData() {
        return mDataList == null ? (mDataList = new ArrayList<T>()) : mDataList;
    }

}
