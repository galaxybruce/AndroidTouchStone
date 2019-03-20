package com.galaxybruce.component.mvp;

/**
 * created by bruce.zhang
 * @param <V>
 */
public interface MvpPresenter<V extends MvpView> {

  void attachView(V view);

  void detachView();

}
