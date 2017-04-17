package com.burhan.wunderapp.presentation.view.main.list;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.burhan.wunderapp.R;
import com.burhan.wunderapp.common.maps.MapBitmapCache;
import com.burhan.wunderapp.common.mvp.MvpFragment;
import com.burhan.wunderapp.common.transitions.ScaleDownImageTransition;
import com.burhan.wunderapp.data.WunderPlacemarksInjection;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.burhan.wunderapp.data.repository.WunderRepository;
import com.burhan.wunderapp.presentation.view.main.MainActivity;
import com.burhan.wunderapp.presentation.view.main.list.adapter.PlacemarkListAdapter;
import com.burhan.wunderapp.presentation.view.main.map.DetailsFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

/**
 * Created by BURHAN on 4/16/2017.
 */

public class ListFragment extends MvpFragment<ListFragmentView, ListFragmentPresenter> implements ListFragmentView {
    public static final String TAG = ListFragment.class.getName();
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private PlacemarkListAdapter mAdapter;
    private RecyclerTouchListener onTouchListener;
    private SearchView searchView;
    private OnListFragmentInteractionListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog= new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        setupRecyclerView(view);
        presenter.search();
    }

    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new PlacemarkListAdapter(getActivity(), new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        onTouchListener = new RecyclerTouchListener(getActivity(), mRecyclerView);
        onTouchListener
                .setIndependentViews(R.id.rowButton)
                .setViewsToFade(R.id.rowButton)
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Log.d(TAG, "onRowClicked() called with: position = [" + position + "]");
                        if(listener != null) listener.onListIemClicked();
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        Log.d(TAG, "onIndependentViewClicked() called with: independentViewID = [" + independentViewID + "], position = [" + position + "]");
                        if(listener != null) listener.onListIemClicked();
                    }
                })
                .setLongClickable(true, new RecyclerTouchListener.OnRowLongClickListener() {
                    @Override
                    public void onRowLongClicked(int position) {
                        Log.d(TAG, "onRowLongClicked() called with: position = [" + position + "]");
                    }
                })
                .setSwipeOptionViews(R.id.add, R.id.edit, R.id.change)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        if(listener != null) listener.onListIemClicked();
                        String message = "";
                        if (viewID == R.id.add) {
                            message += "Add";
                        } else if (viewID == R.id.edit) {
                            message += "Edit";
                        } else if (viewID == R.id.change) {
                            message += "Change";
                        }
                        message += " clicked for row " + (position + 1);
                        Log.d(TAG, "onSwipeOptionClicked() called with:"+message);
                    }
                });
    }

    @Override
    protected ListFragmentPresenter createPresenter() {
        WunderRepository wunderRepository = WunderPlacemarksInjection.provideUserRepo();
        Scheduler mainScheduler = AndroidSchedulers.mainThread();
        Scheduler ioScheduler = Schedulers.io();
        ListFragmentPresenter presenter = new ListFragmentPresenterImpl(wunderRepository, mainScheduler, ioScheduler);
        return presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.addOnItemTouchListener(onTouchListener); }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerView.removeOnItemTouchListener(onTouchListener);
    }

    @Override
    public void onBackPressed() {
        ((MainActivity) getActivity()).finish();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_list;
    }

    @Override
    public void onPlacemarksLoaded(List<Placemark> placemarks) {
        Log.d(TAG, "onPlacemarksLoaded() called with: placemarks = [" + placemarks + "]");
        mAdapter.setData(placemarks);
    }

    @Override
    public void showError(String message) {
        Log.d(TAG, "showError() called with: message = [" + message + "]");
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
   //     super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchActionMenuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                presenter.search(query);
                searchActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchActionMenuItem.expandActionView();
    }

    public interface OnListFragmentInteractionListener{
        void onListIemClicked();
    }

    public static Fragment newInstance(final Context ctx) {
        ListFragment fragment = new ListFragment();
        ScaleDownImageTransition transition = new ScaleDownImageTransition(ctx, MapBitmapCache.instance().getBitmap());
        transition.addTarget(ctx.getString(R.string.mapPlaceholderTransition));
        transition.setDuration(600);
        fragment.setEnterTransition(transition);
        return fragment;
    }

}
