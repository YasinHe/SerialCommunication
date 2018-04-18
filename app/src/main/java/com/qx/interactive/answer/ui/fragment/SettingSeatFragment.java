package com.qx.interactive.answer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.qx.interactive.answer.R;
import com.qx.interactive.answer.interfaces.IsettingSeatCallBack;
import com.qx.interactive.answer.model.SeatPerson;
import com.qx.interactive.answer.presenter.SettingSeatPresenter;
import com.qx.interactive.answer.ui.BaseFragment;
import com.qx.interactive.answer.ui.adapter.SettingSeatAdapter;
import com.qx.interactive.answer.ui.controlView.SettingSeatView;
import com.qx.interactive.answer.ui.selfView.SpacesItemDecoration;
import com.qx.interactive.answer.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HeYingXin on 2017/2/20.
 */
public class SettingSeatFragment extends BaseFragment implements SettingSeatView, IsettingSeatCallBack
        , ViewTreeObserver.OnGlobalLayoutListener {

    SettingSeatPresenter mSeatPresenter;
    SettingSeatAdapter mSeatAdapter;
    ArrayList<SeatPerson> mPersons = new ArrayList<>();
    volatile int oldColumu = -1;
    private volatile int screenRcyHeigh = 0, screenRcyWidth = 0;

    @Bind(R.id.tv_reset)
    TextView mTvReset;
    @Bind(R.id.rcy_seats)
    RecyclerView mRcySeats;
    @Bind(R.id.tv_columu)
    TextView mTvColumu;
    @Bind(R.id.iv_columu_subtract)
    ImageView mIvColumuSubtract;
    @Bind(R.id.tv_columu_count)
    TextView mTvColumuCount;
    @Bind(R.id.iv_columu_add)
    ImageView mIvColumuAdd;
    @Bind(R.id.tv_row)
    TextView mTvRow;
    @Bind(R.id.iv_row_subtract)
    ImageView mIvRowSubtract;
    @Bind(R.id.tv_row_count)
    TextView mTvRowCount;
    @Bind(R.id.iv_row_add)
    ImageView mIvRowAdd;
    @Bind(R.id.tv_input)
    TextView mTvInput;
    @Bind(R.id.tv_output)
    TextView mTvOutput;
    @Bind(R.id.tv_save)
    TextView mTvSave;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seat_setting, container, false);
        onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {
        mSeatPresenter = new SettingSeatPresenter();
        mSeatPresenter.attachView(this);
        mRcySeats.setNestedScrollingEnabled(false);
        mRcySeats.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(R.dimen.W_DIMEN_6PX)));
        mRcySeats.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void initData() {
        mSeatPresenter.initFragment();
    }

    @Override
    protected void lazyLoad() {
        LogUtils.d("开启懒加载");
    }

    public static SettingSeatFragment newInstance() {
        return new SettingSeatFragment();
    }

    @Override
    public ArrayList<SeatPerson> getData() {
        return mPersons;
    }

    @Override
    public void setData(ArrayList<SeatPerson> seatPersons,int columu) {
        if(seatPersons!=null){
            mSeatPresenter.syncData(seatPersons,columu);
        }
    }

    @Override
    public int getColumu() {
        return oldColumu;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_columu_subtract, R.id.iv_columu_add, R.id.iv_row_subtract,
            R.id.iv_row_add, R.id.tv_input, R.id.tv_output, R.id.tv_save,R.id.tv_reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_columu_subtract:
                mSeatPresenter.changeColumuRow(2, false);
                break;
            case R.id.iv_columu_add:
                mSeatPresenter.changeColumuRow(2, true);
                break;
            case R.id.iv_row_subtract:
                mSeatPresenter.changeColumuRow(1, false);
                break;
            case R.id.iv_row_add:
                mSeatPresenter.changeColumuRow(1, true);
                break;
            case R.id.tv_input:
                mSeatPresenter.inPut();
                break;
            case R.id.tv_output:
                mSeatPresenter.outPut();
                break;
            case R.id.tv_save:
                mSeatPresenter.saveCurrentSeatSetting();
                break;
            case R.id.tv_reset:
                mSeatPresenter.reSet();
                break;
        }
    }

    @Override
    public void reFreshData(final List<SeatPerson> list, final int columu) {
        mTvColumuCount.setText("" + columu);
        mTvRowCount.setText("" + list.size() / columu);
        mPersons.clear();
        mPersons.addAll(list);
        if (oldColumu != columu) {
            oldColumu = columu;
            mRcySeats.setLayoutManager(new GridLayoutManager(getContext(), columu));
            mSeatAdapter = new SettingSeatAdapter(getContext(), mPersons, screenRcyHeigh, screenRcyWidth, columu, this);//851, 1644
            mRcySeats.setAdapter(mSeatAdapter);
        } else {
            mSeatAdapter.reFresh(mPersons, screenRcyHeigh, screenRcyWidth, columu);
        }
    }

    @Override
    public void doubleClick(SeatPerson seatPerson) {
         mSeatPresenter.doubleCilckEmpty(seatPerson);
    }

    @Override
    public void onGlobalLayout() {
        mRcySeats.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (screenRcyHeigh == 0 || screenRcyWidth == 0) {
                    screenRcyHeigh = mRcySeats.getHeight() - 130;
                    screenRcyWidth = mRcySeats.getWidth() - 50;
                    if (mSeatAdapter != null && mPersons != null) {
                        mSeatAdapter.reFresh(mPersons, screenRcyHeigh, screenRcyWidth, oldColumu);
                    }
                }
            }
        }, 300);
    }
}
