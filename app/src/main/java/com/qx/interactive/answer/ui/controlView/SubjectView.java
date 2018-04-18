package com.qx.interactive.answer.ui.controlView;

import com.qx.interactive.answer.model.SeatPerson;
import com.qx.interactive.answer.ui.activity.SubjectActivity;

import java.util.List;

/**
 * Created by HeYingXin on 2017/2/23.
 */
public interface SubjectView extends MvpView{
    void reFreshData(List<SeatPerson> list, int columu,int nullSeat);
    void showUiByType(boolean isChooise);
    SubjectActivity getActivity();
    void reFreshStatistics(int tarkPartCount,int a,int b,int c,int d,int e,int f,int right,int wrong);
    void chooseChangeUi(String result);
}
