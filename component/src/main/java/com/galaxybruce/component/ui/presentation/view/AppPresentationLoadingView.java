package com.galaxybruce.component.ui.presentation.view;

import android.content.Context;
import android.widget.TextView;

import com.galaxybruce.component.R;


/**
 * @author bruce.zhang
 * @date 2021/6/15 19:47
 * @description Presentation上显示的loading view
 * <p>
 * modification history:
 */
public class AppPresentationLoadingView extends AppPresentationWindowManager implements IAppPresentationView<String> {

    public AppPresentationLoadingView(Context outerContext) {
        super(outerContext);
    }

    @Override
    public void show(String data) {
        addView(R.layout.app_dialog_loading);
        TextView textView = view.findViewById(R.id.tv_message);
        textView.setText(data);
    }

    @Override
    public void hide() {
        removeView();
    }

}
