package com.burhan.wunderapp.data.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BURHAN on 4/9/2017.
 */

public class PlacemarksList {

    @SerializedName("placemarks")
    @Expose
    private List<Placemark> items = new ArrayList<Placemark>();

    public List<Placemark> getItems() {
        return items;
    }

    public void setItems(List<Placemark> items) {
        this.items = items;
    }
}
