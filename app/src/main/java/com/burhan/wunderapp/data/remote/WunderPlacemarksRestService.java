package com.burhan.wunderapp.data.remote;



import com.burhan.wunderapp.data.remote.model.PlacemarksList;

import retrofit2.http.GET;
import rx.Observable;

public interface WunderPlacemarksRestService {

    @GET("/wunderbucket/locations.json")
    Observable<PlacemarksList> searchPlacemarks();

}
