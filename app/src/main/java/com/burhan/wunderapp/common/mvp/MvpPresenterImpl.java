package com.burhan.wunderapp.common.mvp;

import java.lang.ref.SoftReference;

public class MvpPresenterImpl<V extends MvpView> implements MvpPresenter<V> {

    private SoftReference<V> viewReference;

    @Override
    public void attachView(V view) {
        viewReference = new SoftReference<>(view);
        onViewAttached();
    }

    public V getView() {
        return viewReference == null ? null : viewReference.get();
    }

    @Override
    public boolean isViewAttached() {
        return viewReference != null && viewReference.get() != null;
    }

    @Override
    public void detachView() {
        if (viewReference != null) {
            viewReference.clear();
            viewReference = null;
        }
    }

    public void onViewAttached() {

    }
    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" + " requesting data to the Presenter");
        }
    }
}
