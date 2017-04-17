package com.burhan.wunderapp.common.mvp;

public interface MvpPresenter<V extends MvpView> {
    void attachView(V view);
    void detachView();
    boolean isViewAttached();
}
