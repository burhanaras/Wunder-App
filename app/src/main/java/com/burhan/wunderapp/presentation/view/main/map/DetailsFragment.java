package com.burhan.wunderapp.presentation.view.main.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Scene;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.BindView;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.burhan.wunderapp.R;
import com.burhan.wunderapp.common.maps.MapBitmapCache;
import com.burhan.wunderapp.common.maps.PulseOverlayLayout;
import com.burhan.wunderapp.common.mvp.MvpFragment;
import com.burhan.wunderapp.common.transitions.ScaleDownImageTransition;
import com.burhan.wunderapp.common.transitions.TransitionUtils;
import com.burhan.wunderapp.common.views.HorizontalRecyclerViewScrollListener;
import com.burhan.wunderapp.common.views.TranslateItemAnimator;
import com.burhan.wunderapp.data.WunderPlacemarksInjection;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.burhan.wunderapp.data.repository.WunderRepository;
import com.burhan.wunderapp.presentation.view.main.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends MvpFragment<DetailsFragmentView, DetailsFragmentPresenter>
        implements DetailsFragmentView, OnMapReadyCallback, PlacesAdapter.OnPlaceClickListener, HorizontalRecyclerViewScrollListener.OnItemCoverListener {
    public static final String TAG = DetailsFragment.class.getSimpleName();

    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.container) FrameLayout containerLayout;
    @BindView(R.id.mapPlaceholder) ImageView mapPlaceholder;
    @BindView(R.id.mapOverlayLayout) PulseOverlayLayout mapOverlayLayout;
    private ProgressDialog progressDialog;

    private List<Placemark> placemarks;
    private PlacesAdapter placesAdapter;
    private String currentTransitionName;
    private Scene detailsScene;

    private LatLng hamburg = new LatLng(53.5534361,9.9885464);

    public static Fragment newInstance(final Context ctx) {
        DetailsFragment fragment = new DetailsFragment();
        ScaleDownImageTransition transition = new ScaleDownImageTransition(ctx, MapBitmapCache.instance().getBitmap());
        transition.addTarget(ctx.getString(R.string.mapPlaceholderTransition));
        transition.setDuration(600);
        fragment.setEnterTransition(transition);
        return fragment;
    }

    @Override
    protected DetailsFragmentPresenter createPresenter() {
        WunderRepository wunderRepository = WunderPlacemarksInjection.provideUserRepo();
        Scheduler mainScheduler = AndroidSchedulers.mainThread();
        Scheduler ioScheduler = Schedulers.io();
        DetailsFragmentPresenter presenter =  new DetailsFragmentPresenterImpl(wunderRepository, mainScheduler, ioScheduler);
        return  presenter;
    }

    @Override
    public void onBackPressed() {
        if (detailsScene != null) {
            presenter.onBackPressedWithScene(placemarks);
        } else {
            ((MainActivity) getActivity()).superOnBackPressed();
        }
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_details;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog= new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        setupMapData();
        setupMapFragment();
        setupRecyclerView();
    }

    private void setupMapData() {
        if(MainActivity.gpsLocation !=null){
           presenter.search(new LatLng( MainActivity.gpsLocation.getLatitude(),  MainActivity.gpsLocation.getLongitude()));
        }else{
            presenter.search(hamburg);
        }

    }

    private void setupMapFragment() {
        ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapFragment1)).getMapAsync(this);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        placesAdapter = new PlacesAdapter(this, getActivity());
        recyclerView.setAdapter(placesAdapter);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mapOverlayLayout.setupMap(googleMap);

    }

    private void setupGoogleMap() {
        presenter.moveMapAndAddMarker(placemarks);
    }

    private void addDataToRecyclerView() {
        recyclerView.setItemAnimator(new TranslateItemAnimator());
        recyclerView.setAdapter(placesAdapter);
        placesAdapter.setPlacesMarks(placemarks);
        recyclerView.addOnScrollListener(new HorizontalRecyclerViewScrollListener(this));
    }

    @Override
    public void onPlaceClicked(final View sharedImageView, final String transitionName, final int position) {
        currentTransitionName = transitionName;
        detailsScene = DetailsLayout.showScene(getActivity(), containerLayout, sharedImageView, currentTransitionName, placemarks.get(position));
        getRoutePointsAndAnimateMap(position);
        animateMap();
    }

    private void getRoutePointsAndAnimateMap(final int position) {
        LatLng currentLatLng = (MainActivity.gpsLocation == null )? mapOverlayLayout.getCurrentLatLng(): new LatLng(MainActivity.gpsLocation.getLatitude(), MainActivity.gpsLocation.getLongitude());
        presenter.getRoutePoints(currentLatLng, placemarks.get(position).getLatLng());
    }

    private void animateMap() {
        mapOverlayLayout.setOnCameraIdleListener(null);
        mapOverlayLayout.hideAllMarkers();
    }

    @Override
    public void showSearchResults(List<Placemark> placemarkList) {
        Log.d(TAG, "showSearchResults() called with: placemarkList = [" + placemarkList + "]");
        placemarks = placemarkList;
        setupGoogleMap();
        addDataToRecyclerView();
    }

    @Override
    public void showError(String message) {
        Log.e(TAG, message);
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading() called");
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hideLoading() called");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void drawPolylinesOnMap(final ArrayList<LatLng> polylines) {
        getActivity().runOnUiThread(() -> mapOverlayLayout.addPolyline(polylines));
    }

    @Override
    public void onBackPressedWithScene(final LatLngBounds latLngBounds) {
        int childPosition = TransitionUtils.getItemPositionFromTransition(currentTransitionName);
        DetailsLayout.hideScene(getActivity(), containerLayout, getSharedViewByPosition(childPosition), currentTransitionName);
        notifyLayoutAfterBackPress(childPosition);
        mapOverlayLayout.onBackPressed(latLngBounds);
        detailsScene = null;
    }


    private View getSharedViewByPosition(final int childPosition) {
        Log.d(TAG, "getSharedViewByPosition() called with: childPosition = [" + childPosition + "]");
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            if (childPosition == recyclerView.getChildAdapterPosition(recyclerView.getChildAt(i))) {
                return recyclerView.getChildAt(i);
            }
        }
        return null;
    }
    private void notifyLayoutAfterBackPress(final int childPosition) {
        containerLayout.removeAllViews();
        containerLayout.addView(recyclerView);
        recyclerView.requestLayout();
        placesAdapter.notifyItemChanged(childPosition);
    }

    @Override
    public void moveMapAndAddMaker(final LatLngBounds latLngBounds) {
        mapOverlayLayout.moveCamera(latLngBounds);
        mapOverlayLayout.createAndShowGPSMarker(hamburg);
        mapOverlayLayout.setOnCameraIdleListener(() -> {
            for (int i = 0; i < placemarks.size(); i++) {
                mapOverlayLayout.createAndShowMarker(i, placemarks.get(i).getLatLng());
            }
            mapOverlayLayout.setOnCameraIdleListener(null);
        });
        mapOverlayLayout.setOnCameraMoveListener(mapOverlayLayout::refresh);


    }

    @Override
    public void updateMapZoomAndRegion(final LatLng northeastLatLng, final LatLng southwestLatLng) {
        getActivity().runOnUiThread(() -> {
            mapOverlayLayout.animateCamera(new LatLngBounds(southwestLatLng, northeastLatLng));
            mapOverlayLayout.setOnCameraIdleListener(() -> mapOverlayLayout.drawStartAndFinishMarker());
        });
    }

    @Override
    public void onItemCover(final int position)
    {
        mapOverlayLayout.showMarker(position);
    }

    public void updateGpsLocation(Location gpsLocation) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment f =   getFragmentManager()
                .findFragmentById(R.id.container);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }
}
