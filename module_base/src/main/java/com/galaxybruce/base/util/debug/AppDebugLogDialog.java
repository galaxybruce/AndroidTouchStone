package com.galaxybruce.base.util.debug;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.galaxybruce.base.BR;
import com.galaxybruce.base.R;
import com.galaxybruce.base.databinding.AppDebugLogDialogBinding;
import com.galaxybruce.base.databinding.AppDebugLogDialogItemLayoutBinding;
import com.galaxybruce.component.ui.dialog.AppBottomDialog;
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel;
import com.galaxybruce.component.ui.jetpack.JPDataBindingConfig;
import com.galaxybruce.component.ui.jetpack.JPListDataModel;
import com.galaxybruce.component.ui.jetpack.adapter.JPRecyclerViewLoadMoreAdapter;
import com.galaxybruce.component.ui.view.recyclerview.AppRecyclerView2;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * @date
 * @author
 * @description 显示日志dialog
 *
 * 使用方式：
 * new AppDebugLogDialog.Builder()
 *      .build().show(getSupportFragmentManager(), "AppDebugLogDialog");
 *
 * <p>
 * modification history:
 */
public class AppDebugLogDialog extends AppBottomDialog<AppDebugLogDialogBinding> {

    AppDebugLogDialogViewModel mPageViewModel;

    public static final class Builder {
        Bundle bundle;

        public Builder() {
            bundle = new Bundle();
        }

        public AppDebugLogDialog build() {
            AppDebugLogDialog dialog = new AppDebugLogDialog();
            dialog.setArguments(bundle);
            return dialog;
        }
    }

    @Override
    public void resizeDialogFragment() {
        super.resizeDialogFragment();
        Dialog dialog = getDialog();
        if(dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    @Override
    protected boolean supportMVVM() {
        return true;
    }

    @Override
    public JPBaseViewModel initViewModel() {
        mPageViewModel = getFragmentViewModel(AppDebugLogDialogViewModel.class);
        return mPageViewModel;
    }

    @Override
    public JPDataBindingConfig initDataBindConfig() {
        return new JPDataBindingConfig(bindLayoutId())
                .addBindingParam(BR.vm, mPageViewModel)
                .addBindingParam(BR.click, new ClickProxy());
    }

    @Override
    public int bindLayoutId() {
        return R.layout.app_debug_log_dialog;
    }

    @Override
    public void initData(@Nullable Bundle bundle, Bundle savedInstanceState) {
        setLiveDataObserver(AppDebugLogManager.INSTANCE.getNewLog(),
                new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        JPListDataModel dataModel = mPageViewModel.listData.getValue();
                        List<Object> list = dataModel.getList();
                        if(list == null) {
                            list = new ArrayList<Object>();
                        }
                        list.add(s);
                        mPageViewModel.listData.setValue(new JPListDataModel(list, true));
                        getBinding().bbsRecyclerView.getRecyclerView().scrollToPosition(list.size() - 1);
                    }
                });
    }

    @Override
    public void initView(@Nullable View view) {
        super.initView(view);

        AppDebugLogDialogBinding realBinding = getBinding();

        // 根据实际需要动态设置RecyclerView高度
        ViewGroup.LayoutParams layoutParams = realBinding.bbsRecyclerView.getLayoutParams();
        layoutParams.height = (int)(ScreenUtils.getScreenHeight() * 0.5f);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setReverseLayout(true);
        AppRecyclerView2<String> bbsRecyclerView = realBinding.bbsRecyclerView;
        bbsRecyclerView.setBbsAdapter(new InnerAdapter(getContext()))
                .setPullRefreshEnable(false)
                .setRequestDataIfViewCreated(true)
                .setInitPage(1)
                .setLayoutManager(layoutManager)
                .setBbsRequestListener(new AppRecyclerView2.AppRequestListener() {
                    @Override
                    public void sendRequestData(boolean refresh, int page) {
                        performRequest();
                    }

                    @Override
                    public void sendRequestLoadMoreData(int page) {
                    }
                }).create();

        realBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
    }

    @Override
    public void bindData(Bundle savedInstanceState) {
        // 页面重建时，判断是否有数据，没有数据才发送请求
        if (mPageViewModel.listData.getValue() == null
                || mPageViewModel.listData.getValue().getList() == null
                || mPageViewModel.listData.getValue().getList().isEmpty()) {
            getBinding().bbsRecyclerView.requestDataWithoutLoading();
        }
    }

    private void performRequest() {
        this.mPageViewModel.listData.setValue(new JPListDataModel(new ArrayList<>(), true));
    }

    /**
     * 页面事件类，可以把所有事件都写在这里
     */
    public class ClickProxy {
        public void switchGravityEvent() {
            Dialog dialog = getDialog();
            if (dialog == null) {
                return;
            }
            Window window = dialog.getWindow();
            if (window == null) {
                return;
            }

            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = params.gravity == Gravity.BOTTOM ? Gravity.TOP : Gravity.BOTTOM;
            window.setAttributes(params);
        }
    }

    private class InnerAdapter extends JPRecyclerViewLoadMoreAdapter<String> {

        private static final int VIEW_TYPE_XXX = 0x2001f;

        public InnerAdapter(Context context) {
            super(context);
        }

        @Override
        public boolean needLoadMore() {
            return false;
        }

        @Override
        protected boolean showFooterViewOfHint() {
            return false;
        }

        @Override
        protected int getRealItemViewType(int position) {
            return VIEW_TYPE_XXX;
        }

        @Override
        protected int getLayoutId(int viewType) {
            if(viewType == VIEW_TYPE_XXX) {
                return R.layout.app_debug_log_dialog_item_layout;
            }
            return super.getLayoutId(viewType);
        }

        @Override
        protected void onBindItem(ViewDataBinding binding, int dataPosition) {
            if(binding instanceof AppDebugLogDialogItemLayoutBinding) {
                String itemInfo = getData().get(dataPosition);
                AppDebugLogDialogItemLayoutBinding itemLayoutBinding = ((AppDebugLogDialogItemLayoutBinding) binding);
                itemLayoutBinding.setVm(itemInfo);
            }
        }
    }
}
