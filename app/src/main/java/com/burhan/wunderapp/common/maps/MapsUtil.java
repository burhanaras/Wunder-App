package com.burhan.wunderapp.common.maps;

import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.burhan.wunderapp.common.model.Bounds;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

public class MapsUtil {
    public static final String MAP_BITMAP_KEY = "map_bitmap_key";
    public static final int DEFAULT_ZOOM = 150;
    private static final double LATITUDE_INCREASE_FACTOR = 1.5;
    public static int DEFAULT_MAP_PADDING = 30;
    public static final LatLng hamburg = new LatLng(53.5534361,9.9885464);

    public static String increaseLatitude(final Bounds bounds) {
        double southwestLat = bounds.getSouthwest().getLatD();
        double northeastLat = bounds.getNortheast().getLatD();
        double updatedLat = LATITUDE_INCREASE_FACTOR * Math.abs(northeastLat - southwestLat);
        return String.valueOf(southwestLat - updatedLat);
    }

    public static int calculateWidth(final WindowManager windowManager) {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int calculateHeight(final WindowManager windowManager, final int paddingBottom) {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels - paddingBottom;
    }

    public static LatLngBounds provideLatLngBoundsForAllPlaces(List<Placemark> placemarks) {
        if (placemarks == null){
            placemarks = new ArrayList<>();
            Placemark placemark=new Placemark();
            placemark.setCoordinates(new double[]{hamburg.latitude, hamburg.longitude});
            placemarks.add(placemark);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(Placemark place : placemarks) {
            builder.include(place.getLatLng());
        }
        return builder.build();
    }


}
