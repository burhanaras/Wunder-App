package com.burhan.wunderapp.presentation.model.dummy;

import com.burhan.wunderapp.R;
import com.burhan.wunderapp.data.remote.model.Placemark;

import java.util.List;

/**
 * Created by BURHAN on 4/15/2017.
 */

public class DummyImageProvider {

    public static final String TAG = DummyImageProvider.class.getName();

    private static int[] images = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,
            R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight, R.drawable.nine, R.drawable.ten};
    public static int profileImage(int i){
        int result =  images[i % images.length];
        return result;
    }

    public static void populateItemsWithDummyImages(List<Placemark> placemarkList) {
        if(placemarkList== null) return;
        for(int i=0;i<placemarkList.size();i++) {
            placemarkList.get(i).setProfilePhoto(profileImage(i));
        }
    }
}
