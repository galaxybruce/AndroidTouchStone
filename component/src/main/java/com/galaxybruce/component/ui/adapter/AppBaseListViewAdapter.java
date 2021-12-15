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
public abstract class AppBaseListViewAdapter<T> extends BaseAdapter implements IListAdapter {
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected Resources mResources;
    protected ArrayList<T> mDatas = new ArrayList<T>();

    public AppBaseListViewAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResources = context.getResources();
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        if (mDatas.size() > position) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public int getDataSize() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public ArrayList<T> getData() {
        return mDatas == null ? (mDatas = new ArrayList<T>()) : mDatas;
    }

    public void setData(ArrayList<T> data) {
        mDatas = data;
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        addData(data, true);
    }

    public void addData(List<T> data, boolean refresh) {
        if (data == null) return;

        if (mDatas != null) {
            mDatas.addAll(data);
        }
        if (refresh) {
            notifyDataSetChanged();
        }
    }

    public void addData(T data) {
        if (mDatas != null) {
            mDatas.add(data);
        }
        notifyDataSetChanged();
    }

    public void addData(int index, T data) {
        if (mDatas != null) {
            mDatas.add(index, data);
        }
        notifyDataSetChanged();
    }

    public void removeData(Object obj) {
        if (mDatas != null) {
            mDatas.remove(obj);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        clear(true);
    }

    public void clear(boolean refresh) {
        if (mDatas != null) {
            mDatas.clear();
        }
        if (refresh) {
            notifyDataSetChanged();
        }
    }

}
