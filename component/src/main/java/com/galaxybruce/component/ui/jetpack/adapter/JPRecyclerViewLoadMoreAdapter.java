package com.galaxybruce.component.ui.jetpack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.galaxybruce.component.ui.adapter.AppRecyclerLoadMoreAdapter;
import com.galaxybruce.component.ui.adapter.RecyclerViewHolder;

/**
 * @date 2020/8/14  4:54 PM
 * @author
 * @description 刷新  加载 adapter 配合 bbsrecyclerview 使用
 *
 *  只需实现  getLayoutId   onBindItem 方法
 *
 * <p>
 * modification history:
 */
public abstract class JPRecyclerViewLoadMoreAdapter<T> extends AppRecyclerLoadMoreAdapter<T> {

    public JPRecyclerViewLoadMoreAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateRealViewHolder(ViewGroup viewGroup, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                getLayoutId(viewType), viewGroup, false);
        return new RecyclerViewHolder(mContext, binding.getRoot());
    }

    @Override
    public void onBindRealViewHolder(RecyclerView.ViewHolder holder, int dataPosition) {
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        if (binding != null) {
            onBindItem(binding, dataPosition);
            binding.executePendingBindings();
        }
    }

    /**
     * 布局
     * @param viewType
     * @return
     */
    protected @LayoutRes int getLayoutId(int viewType){
        return viewType;
    }

    /**
     * 数据绑定
     * @param binding  子类可通过判断 binding 类型强转来绑定数据
     * @param dataPosition
     */
    protected abstract void onBindItem(ViewDataBinding binding, int dataPosition);

}
