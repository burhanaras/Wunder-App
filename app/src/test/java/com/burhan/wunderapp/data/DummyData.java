package com.burhan.wunderapp.data;


import com.burhan.wunderapp.data.remote.model.Placemark;
import com.burhan.wunderapp.data.remote.model.PlacemarksList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BURHAN on 4/9/2017.
 */

public class DummyData {

    public static PlacemarksList getDummyPlacemarks() {
        List<Placemark> placemarks = new ArrayList<>();
        placemarks.add(dummyPlaceMark1());
        placemarks.add(dummyPlaceMark2());

        PlacemarksList list = new PlacemarksList();
        list.setItems(placemarks);
        return list;
    }

    public static Placemark dummyPlaceMark1() {
        Placemark placemark = new Placemark();
        placemark.setAddress("Flughafen HH (Mietwagenzentrum Ebene 1 oder 5) Hamburg");
        placemark.setCoordinates(new double[]{10.00708, 53.62914});
        placemark.setEngineType("CE");
        placemark.setExterior("GOOD");
        placemark.setInterior("GOOD");
        placemark.setFuel(60);
        placemark.setName("HH-GO8005");
        placemark.setVin("WME4513341K412733");
        return placemark;
    }

    public static Placemark dummyPlaceMark2() {
        Placemark placemark = new Placemark();
        placemark.setAddress("Schnackenburgallee 51, 22525 Hamburg");
        placemark.setCoordinates(new double[]{9.91765, 53.58102});
        placemark.setEngineType("CE");
        placemark.setExterior("GOOD");
        placemark.setInterior("GOOD");
        placemark.setFuel(55);
        placemark.setName("HH-GO8487");
        placemark.setVin("WME4513341K412743");
        return placemark;
    }
}
