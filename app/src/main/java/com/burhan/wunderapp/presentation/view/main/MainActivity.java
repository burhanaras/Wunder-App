package com.burhan.wunderapp.presentation.view.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;

import com.burhan.wunderapp.R;
import com.burhan.wunderapp.common.maps.MapsUtil;
import com.burhan.wunderapp.common.mvp.MvpActivity;
import com.burhan.wunderapp.common.mvp.MvpFragment;
import com.burhan.wunderapp.presentation.view.main.home.HomeFragment;
import com.burhan.wunderapp.presentation.view.main.list.ListFragment;
import com.burhan.wunderapp.presentation.view.main.map.DetailsFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView, OnMapReadyCallback, LocationListener, ListFragment.OnListFragmentInteractionListener {

    public static final String TAG = MainActivity.class.getName();
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    SupportMapFragment mapFragment;
    private LatLngBounds mapLatLngBounds;

    protected static final int MIN_TIME = 1000;
    protected static final int MIN_DISTANCE = 1;

    private Fragment detailsFragment;
    private Fragment listFragment;

    private SparseIntArray mErrorString;
    protected LocationManager locationManager;
    public static Location gpsLocation;


    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenterImpl();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.provideMapLatLngBounds();

        detailsFragment = DetailsFragment.newInstance(this);
        listFragment = ListFragment.newInstance(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, listFragment, ListFragment.TAG)
                .addToBackStack(ListFragment.TAG)
                .commit();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);



        mErrorString = new SparseIntArray();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermissions();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            triggerFragmentBackPress(getSupportFragmentManager().getBackStackEntryCount());

        } else {
            finish();
        }
    }

    @Override
    public void setMapLatLngBounds(final LatLngBounds latLngBounds) {
        mapLatLngBounds = latLngBounds;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                mapLatLngBounds,
                MapsUtil.calculateWidth(getWindowManager()),
                MapsUtil.calculateHeight(getWindowManager(), getResources().getDimensionPixelSize(R.dimen.map_margin_bottom)), 150));
        googleMap.setOnMapLoadedCallback(() -> googleMap.snapshot(presenter::saveBitmap));
    }

    private void triggerFragmentBackPress(final int count) {
        ((MvpFragment) getSupportFragmentManager().findFragmentByTag(getSupportFragmentManager().getBackStackEntryAt(count - 1).getName())).onBackPressed();
    }

    public void superOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return true;
        }
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Log.d(TAG, "requestPermissions() called");
        Set<String> missingPermissions = new HashSet<>();
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            missingPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            missingPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (missingPermissions.isEmpty()) {
            // permission = true;
            openGPS();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(missingPermissions.toArray(new String[missingPermissions.size()]),
                        PERMISSIONS_REQUEST_LOCATION);
            }
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                openGPS();
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.open_gps_message))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, PERMISSIONS_REQUEST_LOCATION);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void openGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, MainActivity.this);
        gpsLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (gpsLocation != null) {
            Log.d(TAG, "Initial GPS location is : " + gpsLocation.toString());
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged() called with: location = [" + location + "]");
        gpsLocation = location;
        ((DetailsFragment) detailsFragment).updateGpsLocation(gpsLocation);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d(TAG, "onStatusChanged() called with: s = [" + s + "], i = [" + i + "], bundle = [" + bundle + "]");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d(TAG, "onProviderEnabled() called with: s = [" + s + "]");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d(TAG, "onProviderDisabled() called with: s = [" + s + "]");
    }

    @Override
    public void onListIemClicked() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.container, DetailsFragment.newInstance(this), DetailsFragment.TAG)
                .addToBackStack(DetailsFragment.TAG)
                .commit();
    }
}
