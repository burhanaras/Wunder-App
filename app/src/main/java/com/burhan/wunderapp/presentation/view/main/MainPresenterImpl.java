package com.burhan.wunderapp.presentation.view.main;

import android.graphics.Bitmap;
import com.burhan.wunderapp.common.maps.MapBitmapCache;
import com.burhan.wunderapp.common.maps.MapsUtil;
import com.burhan.wunderapp.common.mvp.MvpPresenterImpl;

public class MainPresenterImpl extends MvpPresenterImpl<MainView> implements MainPresenter {
    @Override
    public void saveBitmap(final Bitmap bitmap) {
        MapBitmapCache.instance().putBitmap(bitmap);
    }

    @Override
    public void provideMapLatLngBounds() {
        getView().setMapLatLngBounds(MapsUtil.provideLatLngBoundsForAllPlaces(null));
    }
}
