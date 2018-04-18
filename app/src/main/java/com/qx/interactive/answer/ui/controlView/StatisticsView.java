package com.qx.interactive.answer.ui.controlView;

/**
 * Created by HeYingXin on 2017/2/22.
 */
public interface StatisticsView extends MvpView {

    void changeStartTime(int year, int mouth, int day);

    void changeEndTime(int year, int mouth, int day);
}
