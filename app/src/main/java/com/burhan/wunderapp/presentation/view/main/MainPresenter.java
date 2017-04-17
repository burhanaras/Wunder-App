package com.burhan.wunderapp.presentation.view.main;

import android.graphics.Bitmap;
import com.burhan.wunderapp.common.mvp.MvpPresenter;

public interface MainPresenter extends MvpPresenter<MainView> {
    void saveBitmap(Bitmap googleMap);

    void provideMapLatLngBounds();
}
