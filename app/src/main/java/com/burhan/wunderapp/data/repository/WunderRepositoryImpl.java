package com.burhan.wunderapp.data.repository;


import com.burhan.wunderapp.data.remote.WunderPlacemarksRestService;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.burhan.wunderapp.domain.DistanceCalculator;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class WunderRepositoryImpl implements WunderRepository {

   private WunderPlacemarksRestService  wunderPlacemarksRestService;

    public WunderRepositoryImpl(WunderPlacemarksRestService _wunderPlacemarksRestService) {
        this.wunderPlacemarksRestService = _wunderPlacemarksRestService;
    }

    @Override
    public Observable<List<Placemark>> searchPlacemarks() {
        return Observable.defer(() -> wunderPlacemarksRestService.searchPlacemarks().concatMap(
                placemarksList -> Observable.from(placemarksList.getItems())
                        .filter(new Func1<Placemark, Boolean>() {
                            @Override
                            public Boolean call(Placemark o) {
                                return o.getInterior().equals("GOOD")
                                        && o.getExterior().equals("GOOD"); //filtering
                            }
                        })
                        .toList()))
                .retryWhen(observable -> observable.flatMap(o -> {
                    if (o instanceof IOException) {
                        return Observable.just(null);
                    }
                    return Observable.error(o);
                }));
    }

    @Override
    public Observable<List<Placemark>> searchPlacemarks(String searchText) {
        return Observable.defer(() -> wunderPlacemarksRestService.searchPlacemarks().concatMap(
                placemarksList -> Observable.from(placemarksList.getItems())
                        .filter(new Func1<Placemark, Boolean>() {
                            @Override
                            public Boolean call(Placemark o) {
                                return o.getInterior().equals("GOOD")
                                        && o.getExterior().equals("GOOD")
                                        && o.getAddress().contains(searchText); //filtering
                            }
                        })
                        .toList()))
                .retryWhen(observable -> observable.flatMap(o -> {
                    if (o instanceof IOException) {
                        return Observable.just(null);
                    }
                    return Observable.error(o);
                }));
    }

    @Override
    public Observable<List<Placemark>> searchPlacemarks(LatLng latLng) {
        return Observable.defer(() -> wunderPlacemarksRestService.searchPlacemarks().concatMap(
                placemarksList -> Observable.from(placemarksList.getItems())
                        .filter(new Func1<Placemark, Boolean>() {
                            @Override
                            public Boolean call(Placemark o) {
                                return o.getInterior().equals("GOOD")
                                        && o.getExterior().equals("GOOD")
                                        && new DistanceCalculator(o.getLatLng()).rangeTo(latLng) == DistanceCalculator.NEAR; //filtering
                            }
                        })
                        .toList()))
                .retryWhen(observable -> observable.flatMap(o -> {
                    if (o instanceof IOException) {
                        return Observable.just(null);
                    }
                    return Observable.error(o);
                }));
    }
}
