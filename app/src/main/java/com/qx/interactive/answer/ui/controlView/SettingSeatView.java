package com.qx.interactive.answer.ui.controlView;

import com.qx.interactive.answer.model.SeatPerson;

import java.util.List;

/**
 * Created by HeYingXin on 2017/2/20.
 */
public interface SettingSeatView extends MvpView{
     void reFreshData(List<SeatPerson> list,int columu);
}
