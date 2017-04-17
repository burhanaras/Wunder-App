package com.burhan.wunderapp.data.repository;


import com.burhan.wunderapp.data.remote.model.Placemark;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import rx.Observable;

public interface WunderRepository {

    Observable<List<Placemark>> searchPlacemarks();
    Observable<List<Placemark>> searchPlacemarks(String searchText);
    Observable<List<Placemark>> searchPlacemarks(LatLng latLng);
}
