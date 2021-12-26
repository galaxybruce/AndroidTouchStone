package com.galaxybruce.component.ui.presentation.view;

import android.view.WindowManager;

/**
 * @author bruce.zhang
 * @date 2021/6/16 14:40
 * @description Presentation View
 * <p>
 * modification history:
 */
interface IAppPresentationView<T> {
    void show(T data);

    void hide();

    void resizeView(WindowManager.LayoutParams layoutParams);

}
