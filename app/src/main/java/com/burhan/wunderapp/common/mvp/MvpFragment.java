package com.burhan.wunderapp.common.mvp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class MvpFragment<V extends MvpView, P extends MvpPresenter> extends Fragment implements MvpView {

    protected P presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("mvp", "onCreateView: "+getLayout());
        View view = inflater.inflate(getLayout(), container, false);
        if (view != null) {
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(presenter == null)
            presenter = createPresenter();
        //noinspection unchecked
        presenter.attachView(this);
    }

    protected abstract P createPresenter();

    public abstract void onBackPressed();

    @LayoutRes
    public abstract int getLayout();

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }
}
