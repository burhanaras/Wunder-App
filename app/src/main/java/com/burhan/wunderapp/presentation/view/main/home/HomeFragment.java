package com.burhan.wunderapp.presentation.view.main.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import com.burhan.wunderapp.R;
import com.burhan.wunderapp.common.mvp.MvpFragment;
import com.burhan.wunderapp.presentation.view.main.MainActivity;

public class HomeFragment extends MvpFragment<HomeView, HomePresenter> implements HomeView {
    public static final String TAG = HomeFragment.class.getSimpleName();

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenterImpl();
    }

    @Override
    public void onBackPressed() {
        ((MainActivity)getActivity()).superOnBackPressed();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showError(String message) {
        Log.d(TAG, "showError() called with: message = [" + message + "]");
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading() called");
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hideLoading() called");
    }
}
