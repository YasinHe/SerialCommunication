package com.qx.interactive.answer.ui.controlView;

import android.content.Context;
import android.view.View;

/**
 * Created by HeYingXin on 2017/2/16.
 * 顶层接口，设置了六个通用回调方便扩展
 */
public interface MvpView {

    void showLoading(String msg);

    void hideLoading();

    void showError(String msg, View.OnClickListener onClickListener);

    void showEmpty(String msg, View.OnClickListener onClickListener);

    void showEmpty(String msg, View.OnClickListener onClickListener, int imageId);

    void showNetError(View.OnClickListener onClickListener);

    Context getContext();

}
