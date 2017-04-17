package com.burhan.wunderapp.presentation.view.main.list;

import com.burhan.wunderapp.common.mvp.MvpPresenterImpl;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.burhan.wunderapp.data.repository.WunderRepository;
import com.burhan.wunderapp.presentation.model.dummy.DummyImageProvider;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

public class ListFragmentPresenterImpl extends MvpPresenterImpl<ListFragmentView> implements ListFragmentPresenter {

    private WunderRepository wunderRepository;
    private Scheduler mainScheduler;
    private Scheduler ioScheduler;

    public ListFragmentPresenterImpl(WunderRepository wunderRepository, Scheduler mainScheduler, Scheduler ioScheduler) {

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
                        getView().onPlacemarksLoaded(placemarkList);
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
                        getView().onPlacemarksLoaded(placemarkList);
                    }
                });
    }
}
