package com.burhan.wunderapp.presentation;

import com.burhan.wunderapp.common.mvp.MvpPresenter;
import com.burhan.wunderapp.common.mvp.MvpPresenterImpl;
import com.burhan.wunderapp.data.DummyData;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.burhan.wunderapp.data.remote.model.PlacemarksList;
import com.burhan.wunderapp.data.repository.WunderRepository;
import com.burhan.wunderapp.presentation.view.main.list.ListFragmentPresenter;
import com.burhan.wunderapp.presentation.view.main.list.ListFragmentPresenterImpl;
import com.burhan.wunderapp.presentation.view.main.list.ListFragmentView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by BURHAN on 4/18/2017.
 */

public class ListPresenterUnitTest {

    @Mock
    WunderRepository wunderRepository;
    @Mock
    ListFragmentView view;

    ListFragmentPresenter presenter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        presenter =new ListFragmentPresenterImpl(wunderRepository, Schedulers.immediate(), Schedulers.immediate());
        presenter.attachView(view);
    }

    @Test
    public void search_ValidSearchTerm_ReturnsResults() {
        PlacemarksList list = DummyData.getDummyPlacemarks();
        when(wunderRepository.searchPlacemarks()).thenReturn(Observable.<List<Placemark>>just(list.getItems()));

        presenter.search();

        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view).onPlacemarksLoaded(list.getItems());
        verify(view, never()).showError(anyString());
    }

    @Test
    public void searchWithText_ValidSearchTerm_ReturnsResults() {
        PlacemarksList list = DummyData.getDummyPlacemarks();
        when(wunderRepository.searchPlacemarks(anyString())).thenReturn(Observable.<List<Placemark>>just(list.getItems()));

        presenter.search("Hamburg");

        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view).onPlacemarksLoaded(list.getItems());
        verify(view, never()).showError(anyString());
    }


    @Test
    public void search_WunderRepositoryError_ErrorMsg() {
        String errorMsg = "No internet";
        when(wunderRepository.searchPlacemarks(anyString())).thenReturn(Observable.error(new IOException(errorMsg)));

        presenter.search();

        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view, never()).onPlacemarksLoaded(anyList());
        verify(view).showError(errorMsg);
    }

    @Test
    public void search_with_text_WunderRepositoryError_ErrorMsg() {
        String errorMsg = "No internet";
        when(wunderRepository.searchPlacemarks(anyString())).thenReturn(Observable.error(new IOException(errorMsg)));

        presenter.search("ohh yeah!!!");

        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view, never()).onPlacemarksLoaded(anyList());
        verify(view).showError(errorMsg);
    }

    @Test(expected = MvpPresenterImpl.MvpViewNotAttachedException.class)
    public void search_NotAttached_ThrowsMvpException() {
        presenter.detachView();

        presenter.search("test");

        verify(view, never()).showLoading();
        verify(view, never()).onPlacemarksLoaded(anyList());
    }
}
