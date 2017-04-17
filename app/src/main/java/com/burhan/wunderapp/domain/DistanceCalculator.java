package com.burhan.wunderapp.domain;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by BURHAN on 4/15/2017.
 */

public class DistanceCalculator {
    public static final double NEAR = 0.03; //3 km
    public static final double MEDIUM = 0.1; //10 km
    public static final double FAR = 1; // 100 km
    private  LatLng from;

    public DistanceCalculator(LatLng latLng) {
        from = latLng;
    }

    public double rangeTo(LatLng to) {
        if (to != null && from != null) {
            double distance = distance(to);
            if(distance<=NEAR){
                return NEAR;
            } else if (distance <= MEDIUM) {
                return MEDIUM;
            } else {
                return FAR;
            }
        }
        return FAR;
    }

    public double distance(LatLng to) {

        double total = (from.latitude - to.latitude) * (from.latitude - to.latitude) + (from.longitude - to.longitude) * (from.longitude - to.longitude);
        return Math.sqrt(total);
    }
    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

}
