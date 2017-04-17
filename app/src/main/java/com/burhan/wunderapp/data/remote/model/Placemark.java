package com.burhan.wunderapp.data.remote.model;

/**
 * Created by BURHAN on 4/9/2017.
 */

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * {
 "address": "Lesserstraße 170, 22049 Hamburg",
 "coordinates": [10.07526, 53.59301, 0],
 "engineType": "CE",
 "exterior": "UNACCEPTABLE",
 "distance": 42,
 "interior": "UNACCEPTABLE",
 "name": "HH-GO8522",
 "vin": "WME4513341K565439"
 }
 */
public class Placemark {
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("coordinates")
    @Expose
    private double[] coordinates;
    @SerializedName("engineType")
    @Expose
    private String engineType;
    @SerializedName("exterior")
    @Expose
    private String exterior;
    @SerializedName("fuel")
    @Expose
    private int fuel;
    @SerializedName("interior")
    @Expose
    private String interior;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("vin")
    @Expose
    private String vin;

    private int profilePhoto;

    public int getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(int profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getExterior() {
        return exterior;
    }

    public void setExterior(String exterior) {
        this.exterior = exterior;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public String toString() {
        return "Placemark{" +
                "address='" + address + '\'' +
                ", coordinates=" + Arrays.toString(coordinates) +
                ", engineType='" + engineType + '\'' +
                ", exterior='" + exterior + '\'' +
                ", fuel=" + fuel +
                ", interior='" + interior + '\'' +
                ", name='" + name + '\'' +
                ", vin='" + vin + '\'' +
                '}';
    }

    public LatLng getLatLng() {
            LatLng latLng= new LatLng(coordinates[1], coordinates[0]);
        return latLng;
    }
}
