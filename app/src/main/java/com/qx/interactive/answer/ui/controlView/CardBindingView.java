package com.qx.interactive.answer.ui.controlView;

import com.qx.interactive.answer.model.SeatPerson;

import java.util.List;

/**
 * Created by HeYingXin on 2017/2/21.
 */
public interface CardBindingView extends MvpView{
    void reFreshData(List<SeatPerson> list,int columu);

    void ToastMsg(int type,String msg);
}
