package com.burhan.wunderapp.presentation.view.main.map;


import com.burhan.wunderapp.common.mvp.MvpPresenter;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface DetailsFragmentPresenter extends MvpPresenter<DetailsFragmentView> {

    void search();

    void search(String term);

    void search(LatLng latLng);

    void getRoutePoints(LatLng first, final LatLng secondLatLng);

    void onBackPressedWithScene(List<Placemark> placemarks);

    void moveMapAndAddMarker(List<Placemark> placemarks);

}
