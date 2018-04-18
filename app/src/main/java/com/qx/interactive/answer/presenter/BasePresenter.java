package com.qx.interactive.answer.presenter;

import com.qx.interactive.answer.ui.controlView.MvpView;

/**
 * Created by HeYingXin on 2017/2/16.
 */
public class BasePresenter<T extends MvpView> implements Presenter<T> {

    private T mMvpView;

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    //获取页面Context实例
    public T getMvpView() {
        return mMvpView;
    }

    //获取页面存活情况
    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("页面错误");
        }
    }

}
