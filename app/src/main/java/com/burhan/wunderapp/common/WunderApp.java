package com.burhan.wunderapp.common;

import android.app.Application;

import com.burhan.wunderapp.R;
import com.burhan.wunderapp.common.model.MapsApiManager;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class WunderApp extends Application {

    private static WunderApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        MapsApiManager.instance().initialize();
        initCalligraphy();
    }

    private void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static WunderApp getInstance() {
        return sInstance;
    }
}
