package com.burhan.wunderapp.presentation.view.main.map;

import com.burhan.wunderapp.common.mvp.MvpView;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.ArrayList;
import java.util.List;

public interface DetailsFragmentView extends MvpView {

    void showSearchResults(List<Placemark> placemarkList);

    void drawPolylinesOnMap(ArrayList<LatLng> decode);

    void onBackPressedWithScene(LatLngBounds latLngBounds);

    void moveMapAndAddMaker(LatLngBounds latLngBounds);

    void updateMapZoomAndRegion(LatLng northeastLatLng, LatLng southwestLatLng);
}
