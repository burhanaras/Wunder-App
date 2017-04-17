package com.burhan.wunderapp.presentation.view.main.list;

import com.burhan.wunderapp.common.mvp.MvpView;
import com.burhan.wunderapp.data.remote.model.Placemark;

import java.util.List;

public interface ListFragmentView extends MvpView {

    void onPlacemarksLoaded(List<Placemark> placemarks);
}
