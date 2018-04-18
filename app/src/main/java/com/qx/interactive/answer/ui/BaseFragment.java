package com.qx.interactive.answer.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.qx.interactive.answer.model.SeatPerson;
import com.qx.interactive.answer.ui.controlView.MvpView;
import com.qx.interactive.answer.utils.LogUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by HeYingXin on 2017/2/20.
 */
public abstract class BaseFragment extends Fragment implements MvpView{

    //标志位，标志已经初始化完成
    protected boolean isPrepared;
    //Fragment当前状态是否可见
    protected boolean isVisible;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isPrepared = true;
        ButterKnife.bind(this, view);
        lazyLoad();
        initView();
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 抽象出骨架
     */
    protected abstract void initView();
    protected abstract void initData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
    }

    /**
     * 延迟加载
     */
    protected abstract void lazyLoad();

    /**
     * 获取列数
     */
    public int getColumu(){
        return 0;
    }

    /**
     * 获取数据
     */
    public ArrayList<SeatPerson> getData(){
        return null;
    }

    /**
     * 接收数据
     */
    public void setData(ArrayList<SeatPerson> seatPersons,int columu){
    }

    @Override
    public void showLoading(String msg) {
        // TODO (如果有需要可以在这写个通用加载中对话框)
    }

    @Override
    public void hideLoading() {
        // TODO
    }

    @Override
    public void showError(String msg, View.OnClickListener onClickListener) {
        LogUtils.e("TAG",msg);
    }

    @Override
    public void showEmpty(String msg, View.OnClickListener onClickListener) {
        LogUtils.e("TAG",msg);
    }

    @Override
    public void showEmpty(String msg, View.OnClickListener onClickListener, int imageId) {
        LogUtils.e("TAG",msg);
    }

    @Override
    public void showNetError(View.OnClickListener onClickListener) {

    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
