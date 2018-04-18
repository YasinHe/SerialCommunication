package com.qx.interactive.answer.ui.controlView;

import com.qx.interactive.answer.ui.activity.SeatSettingActivity;
import com.qx.interactive.answer.ui.adapter.MyFragmentAdatper;

/**
 * Created by HeYingXin on 2017/2/17.
 */
public interface SeatSetView extends MvpView{
    SeatSettingActivity getActivity();
    void setFragment(MyFragmentAdatper myFragmentAdatper);
}
