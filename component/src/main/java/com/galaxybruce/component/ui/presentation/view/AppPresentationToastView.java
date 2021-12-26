package com.galaxybruce.component.ui.presentation.view;

import android.content.Context;
import android.widget.TextView;

import com.galaxybruce.component.R;


/**
 * @author bruce.zhang
 * @date 2021/6/15 19:47
 * @description Presentation上显示的toast
 * <p>
 * modification history:
 */
public class AppPresentationToastView extends AppPresentationWindowManager implements IAppPresentationView<String> {

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    public AppPresentationToastView(Context outerContext) {
        super(outerContext);
    }

    @Override
    public void show(String data) {
        addView(R.layout.app_presention_toast_view);
        TextView textView = view.findViewById(R.id.tv_message);
        textView.setText(data);

        if(mRunnable != null) {
            view.removeCallbacks(mRunnable);
        }
        view.postDelayed(mRunnable, 3000);
    }

    @Override
    public void hide() {
        view.removeCallbacks(mRunnable);
        removeView();
    }

}
