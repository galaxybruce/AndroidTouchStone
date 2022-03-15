package com.galaxybruce.component.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView base adapter
 * created by bruce.zhang
 */
public abstract class AppBaseListViewAdapter<T> extends BaseAdapter
        implements IListAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected Resources mResources;
    protected List<T> mDataList = new ArrayList<T>();

    public AppBaseListViewAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResources = context.getResources();
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public T getItem(int position) {
        if (mDataList.size() > position) {
            return mDataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getDataSize() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public List<T> getData() {
        return mDataList == null ? (mDataList = new ArrayList<T>()) : mDataList;
    }

    public void setData(ArrayList<T> data) {
        mDataList = data;
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        addData(data, true);
    }

    public void addData(List<T> data, boolean refresh) {
        if (data == null) return;

        if (mDataList != null) {
            mDataList.addAll(data);
        }
        if (refresh) {
            notifyDataSetChanged();
        }
    }

    public void addData(T data) {
        if (mDataList != null) {
            mDataList.add(data);
        }
        notifyDataSetChanged();
    }

    public void addData(int index, T data) {
        if (mDataList != null) {
            mDataList.add(index, data);
        }
        notifyDataSetChanged();
    }

    public void removeData(Object obj) {
        if (mDataList != null) {
            mDataList.remove(obj);
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

}
