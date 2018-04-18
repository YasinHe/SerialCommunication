package com.qx.interactive.answer.interfaces;

import android.view.View;

import com.qx.interactive.answer.model.SeatPerson;

/**
 * Created by HeYingXin on 2017/2/24.
 */
public interface IsubjectCallBack {
    void backAnswerCount(int count);
    void longClickItem(View view, SeatPerson person);
}
