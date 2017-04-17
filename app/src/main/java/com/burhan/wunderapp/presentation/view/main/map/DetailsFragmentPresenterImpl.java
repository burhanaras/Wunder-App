package com.burhan.wunderapp.presentation.view.main.map;

import android.util.Log;

import com.burhan.wunderapp.common.maps.MapsUtil;
import com.burhan.wunderapp.common.model.Bounds;
import com.burhan.wunderapp.common.model.DirectionsResponse;
import com.burhan.wunderapp.common.model.MapsApiManager;
import com.burhan.wunderapp.common.model.Route;
import com.burhan.wunderapp.common.mvp.MvpPresenterImpl;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.burhan.wunderapp.data.repository.WunderRepository;
import com.burhan.wunderapp.presentation.model.dummy.DummyImageProvider;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Scheduler;
import rx.Subscriber;

public class DetailsFragmentPresenterImpl extends MvpPresenterImpl<DetailsFragmentView> implements DetailsFragmentPresenter {
    public static final String TAG = DetailsFragmentPresenterImpl.class.getName();

    private int VISIBLE_ITEM_COUNT_ON_SCREEN= 10;
    private MapsApiManager mapsApiManager = MapsApiManager.instance();

    private WunderRepository wunderRepository;
    private Scheduler mainScheduler;
    private Scheduler ioScheduler;

    public DetailsFragmentPresenterImpl(WunderRepository wunderRepository, Scheduler mainScheduler, Scheduler ioScheduler) {

        this.wunderRepository = wunderRepository;
        this.mainScheduler = mainScheduler;
        this.ioScheduler = ioScheduler;
    }

    @Override
    public void search() {
        checkViewAttached();
        getView().showLoading();
        wunderRepository.searchPlacemarks().subscribeOn(ioScheduler).observeOn(mainScheduler)
                .subscribe(new Subscriber<List<Placemark>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Placemark> placemarkList) {
                        getView().hideLoading();
                        DummyImageProvider.populateItemsWithDummyImages(placemarkList);
                        getView().showSearchResults(limit(placemarkList, VISIBLE_ITEM_COUNT_ON_SCREEN));
                    }
                });


    }

    @Override
    public void search(String term) {
        checkViewAttached();
        getView().showLoading();
        wunderRepository.searchPlacemarks(term).subscribeOn(ioScheduler).observeOn(mainScheduler)
                .subscribe(new Subscriber<List<Placemark>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Placemark> placemarkList) {
                        getView().hideLoading();
                        DummyImageProvider.populateItemsWithDummyImages(placemarkList);
                        getView().showSearchResults(limit(placemarkList, VISIBLE_ITEM_COUNT_ON_SCREEN));
                    }
                });

    }

    @Override
    public void search(LatLng location) {
        checkViewAttached();
        getView().showLoading();
        if(location == null){
            getView().showError("Location can't be null.");
            return;
        }

        wunderRepository.searchPlacemarks(location).subscribeOn(ioScheduler).observeOn(mainScheduler)
                .subscribe(new Subscriber<List<Placemark>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Placemark> placemarkList) {
                        getView().hideLoading();
                        DummyImageProvider.populateItemsWithDummyImages(placemarkList);
                        getView().showSearchResults(limit(placemarkList, VISIBLE_ITEM_COUNT_ON_SCREEN));
                    }
                });

    }

    private List<Placemark> limit(List<Placemark> list, int limit) {
        int treshold = (list.size() > limit) ? limit : list.size();
        return list.subList(0,treshold);
    }

    @Override
    public void getRoutePoints(final LatLng first, final LatLng secondLatLng) {
        Log.d(TAG, "getRoutePoints() called with: first = [" + first + "], secondLatLng = [" + secondLatLng + "]");
        mapsApiManager.getRoute(first, secondLatLng, new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                Route route = new Gson().fromJson(response.body().charStream(), DirectionsResponse.class).getRoutes().get(0);
                providePolylineToDraw(route.getOverviewPolyline().getPoints());
                updateMapZoomAndRegion(route.getBounds());
            }
        });
    }

    @Override
    public void onBackPressedWithScene(List<Placemark> placemarks) {
        getView().onBackPressedWithScene(MapsUtil.provideLatLngBoundsForAllPlaces(placemarks));
    }

    @Override
    public void moveMapAndAddMarker(List<Placemark> placemarks) {
        getView().moveMapAndAddMaker(MapsUtil.provideLatLngBoundsForAllPlaces(placemarks));
    }

    private void updateMapZoomAndRegion(final Bounds bounds) {
        bounds.getSouthwest().setLat(MapsUtil.increaseLatitude(bounds));
        getView().updateMapZoomAndRegion(bounds.getNortheastLatLng(), bounds.getSouthwestLatLng());
    }

    private void providePolylineToDraw(final String points) {
        getView().drawPolylinesOnMap(new ArrayList<>(PolyUtil.decode(points)));
    }
}
