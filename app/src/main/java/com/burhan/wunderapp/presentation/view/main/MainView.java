package com.burhan.wunderapp.presentation.view.main;

import com.burhan.wunderapp.common.mvp.MvpView;
import com.google.android.gms.maps.model.LatLngBounds;

public interface MainView extends MvpView {
    void setMapLatLngBounds(final LatLngBounds latLngBounds);
}
