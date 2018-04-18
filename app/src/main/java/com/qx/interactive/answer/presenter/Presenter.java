package com.qx.interactive.answer.presenter;

import com.qx.interactive.answer.ui.controlView.MvpView;

/**
 * Created by HeYingXin on 2017/2/16.
 */
public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}
