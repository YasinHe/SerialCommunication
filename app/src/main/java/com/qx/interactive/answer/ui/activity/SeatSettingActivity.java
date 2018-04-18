package com.qx.interactive.answer.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qx.interactive.answer.R;
import com.qx.interactive.answer.presenter.SeatSettingPresenter;
import com.qx.interactive.answer.ui.BaseActivity;
import com.qx.interactive.answer.ui.adapter.MyFragmentAdatper;
import com.qx.interactive.answer.ui.controlView.SeatSetView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HeYingXin on 2017/2/17.
 */
public class SeatSettingActivity extends BaseActivity implements SeatSetView, ViewPager.OnPageChangeListener {

    @Bind(R.id.iv_seat_setting)
    ImageView mIvSeatSetting;
    @Bind(R.id.iv_binding_card)
    ImageView mIvBindingCard;
    @Bind(R.id.ral_left_funcation)
    RelativeLayout mRalLeftFuncation;
    @Bind(R.id.vp_fragment)
    ViewPager mVpFragment;
    @Bind(R.id.tv_exit)
    TextView mTvExit;

    SeatSettingPresenter mSettingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_setting);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        mSettingPresenter = new SeatSettingPresenter();
        mSettingPresenter.attachView(this);
    }

    @Override
    protected void initData() {
        mSettingPresenter.initFragment();
    }

    @Override
    protected void onStop() {
        mSettingPresenter.exitSaveBind();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mSettingPresenter.detachView();
        super.onDestroy();
    }

    @OnClick({R.id.iv_seat_setting, R.id.iv_binding_card,R.id.tv_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_seat_setting:
                mVpFragment.setCurrentItem(0, false);
                break;
            case R.id.iv_binding_card:
                mVpFragment.setCurrentItem(1, false);
                break;
            case R.id.tv_exit:
                SeatSettingActivity.this.finish();
                break;
        }
    }

    @Override
    public SeatSettingActivity getActivity() {
        return this;
    }

    @Override
    public void setFragment(MyFragmentAdatper myFragmentAdatper) {
        if (myFragmentAdatper != null && mVpFragment != null) {
            mVpFragment.setAdapter(myFragmentAdatper);
            mVpFragment.setCurrentItem(0, false);
            mVpFragment.setOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mSettingPresenter.syncData2();
                mIvSeatSetting.setBackgroundResource(R.drawable.ic_seat_setting_big);
                mIvBindingCard.setBackgroundResource(R.drawable.ic_binding_small);
                break;
            case 1:
                mSettingPresenter.syncData();
                mIvSeatSetting.setBackgroundResource(R.drawable.ic_seat_setting_small);
                mIvBindingCard.setBackgroundResource(R.drawable.ic_binding_big);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
