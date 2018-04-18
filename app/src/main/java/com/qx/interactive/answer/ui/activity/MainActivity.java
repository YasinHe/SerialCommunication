package com.qx.interactive.answer.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qx.interactive.answer.R;
import com.qx.interactive.answer.interfaces.IconnectFailureCallback;
import com.qx.interactive.answer.presenter.MainPresenter;
import com.qx.interactive.answer.service.OtgService;
import com.qx.interactive.answer.ui.BaseActivity;
import com.qx.interactive.answer.ui.controlView.MainView;
import com.qx.interactive.answer.utils.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HeYingXin on 2017/2/16.
 */
public class MainActivity extends BaseActivity implements MainView,IconnectFailureCallback {


    MainPresenter mMainPresenter;
    @Bind(R.id.iv_icon)
    ImageView mIvIcon;
    @Bind(R.id.tv_start_judge)
    TextView mTvStartJudge;
    @Bind(R.id.tv_start_choose)
    TextView mTvStartChoose;
    @Bind(R.id.iv_answer_stat)
    ImageView mIvAnswerStat;
    @Bind(R.id.iv_software_update)
    ImageView mIvSoftwareUpdate;
    volatile int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        mHelperUtils.getLly_function_right().setVisibility(View.VISIBLE);
        mHelperUtils.getLly_text_right().setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        mMainPresenter = new MainPresenter();
        mMainPresenter.attachView(this);
        OtgService.setCallBackListener(this);
        UIHelper.showProgressDialog(MainActivity.this, "正在初始化连接设备...");
        rePeat();
    }

    @Override
    protected void onStop() {
        OtgService.unSetCallBackListener(this);
        UIHelper.dismissProgressDialog();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    @OnClick({R.id.iv_icon,R.id.tv_start_judge, R.id.tv_start_choose, R.id.iv_answer_stat, R.id.iv_software_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_icon:
                ++count;
                if(count==8) {
                    mMainPresenter.popupPassword();
                }
                mIvIcon.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count = 0;
                    }
                },4000);
                break;
            case R.id.tv_start_judge:
                SubjectActivity.EntrySubject(mContext,false);
                break;
            case R.id.tv_start_choose:
                SubjectActivity.EntrySubject(mContext,true);
                break;
            case R.id.iv_answer_stat:
                mMainPresenter.enterAnserStatistics();
                break;
            case R.id.iv_software_update:
                mMainPresenter.clickUpdate();
                break;
        }
    }

    @Override
    public MainActivity getActivity() {
        return this;
    }

    @Override
    public void rePeat() {
        mIvIcon.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMainPresenter.enterSystem();
            }
        }, 2000);
    }

    @Override
    public void success() {
        UIHelper.dismissProgressDialog();
    }

    @Override
    public void nullDevice() {
        Toast.makeText(mContext, "未检测到连接设备，请保持答题宝设备连接", Toast.LENGTH_LONG).show();
        UIHelper.dismissProgressDialog();
    }
}
