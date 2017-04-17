package com.burhan.wunderapp.common.maps;

import android.util.Log;
import android.view.View;

/**
 * Created by BURHAN on 4/13/2017.
 */
public class MarkerClickListener implements View.OnClickListener {
    public static final String TAG = MarkerClickListener.class.getName();
    private PulseOverlayLayout mapOverLayLayout;
    private int position;

    public MarkerClickListener(PulseOverlayLayout mapOverLayLayout, int position) {

        this.mapOverLayLayout = mapOverLayLayout;
        this.position = position;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick() called with: view = [" + view + "]");
        mapOverLayLayout.showMarker(position);
    }
}
