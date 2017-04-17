package com.burhan.wunderapp.common.mvp;

public interface MvpView {
    void showError(String message);

    void showLoading();

    void hideLoading();
}
