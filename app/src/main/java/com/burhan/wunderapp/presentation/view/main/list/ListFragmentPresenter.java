package com.burhan.wunderapp.presentation.view.main.list;

import com.burhan.wunderapp.common.mvp.MvpPresenter;
import com.burhan.wunderapp.presentation.view.main.home.HomeView;

public interface ListFragmentPresenter extends MvpPresenter<ListFragmentView> {

    void search();

    void search(String term);
}
