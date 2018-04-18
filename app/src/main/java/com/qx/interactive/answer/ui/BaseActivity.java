package com.qx.interactive.answer.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qx.interactive.answer.ui.controlView.MvpView;
import com.qx.interactive.answer.utils.HeadHelperUtils;
import com.qx.interactive.answer.utils.LogUtils;

import butterknife.ButterKnife;

/**
 * Created by HeYingXin on 2017/2/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements MvpView {

    protected HeadHelperUtils mHelperUtils;
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    public void setContentView(@LayoutRes int resourceId){
        View view = getLayoutInflater().inflate(resourceId, null, false);
        super.setContentView(view);
        mHelperUtils = new HeadHelperUtils(view);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 抽象出骨架
     */
    protected abstract void initView();
    protected abstract void initData();

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
        return mContext;
    }
}

