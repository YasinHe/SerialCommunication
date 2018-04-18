package com.qx.interactive.answer.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.qx.interactive.answer.R;
import com.qx.interactive.answer.presenter.AnserStatisticsPresenter;
import com.qx.interactive.answer.ui.BaseActivity;
import com.qx.interactive.answer.ui.controlView.StatisticsView;
import com.qx.interactive.answer.utils.TimeUtils;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HeYingXin on 2017/2/22.
 * 答题统计
 */
public class AnswerStatisticsActivity extends BaseActivity implements StatisticsView {

    AnserStatisticsPresenter mStatisticsPresenter;
    Calendar now = Calendar.getInstance();
    String seven,today,thirty,thirtytoday;

    @Bind(R.id.chart)
    LineChart mChart;
    @Bind(R.id.tv_quick_statistics)
    TextView mTvQuickStatistics;
    @Bind(R.id.tv_seven_day)
    TextView mTvSevenDay;
    @Bind(R.id.tv_thirty_day)
    TextView mTvThirtyDay;
    @Bind(R.id.ral_left_funcation)
    RelativeLayout mRalLeftFuncation;
    @Bind(R.id.tv_exit)
    TextView mTvExit;
    @Bind(R.id.tv_year2)
    TextView mTvYear2;
    @Bind(R.id.tv_mouth2)
    TextView mTvMouth2;
    @Bind(R.id.tv_day2)
    TextView mTvDay2;
    @Bind(R.id.tv_year1)
    TextView mTvYear1;
    @Bind(R.id.tv_mouth1)
    TextView mTvMouth1;
    @Bind(R.id.tv_day1)
    TextView mTvDay1;
    @Bind(R.id.ral_start_time)
    RelativeLayout mRalStartTime;
    @Bind(R.id.ral_end_time)
    RelativeLayout mRalEndTime;
    @Bind(R.id.ral_exit)
    RelativeLayout mRalExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_statistics);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        mStatisticsPresenter = new AnserStatisticsPresenter();
        mStatisticsPresenter.attachView(this);
    }

    @Override
    protected void initData() {
        mStatisticsPresenter.initData(mChart);
        mTvYear2.setText(now.get(Calendar.YEAR)+"");
        mTvMouth2.setText(now.get(Calendar.MONTH)+1+"");
        mTvDay2.setText(now.get(Calendar.DAY_OF_MONTH)+"");
        String lastDate = TimeUtils.getStatetime(7);
        mTvYear1.setText(lastDate.substring(0,lastDate.indexOf('/')));
        mTvMouth1.setText(lastDate.substring(lastDate.indexOf('/')+1,lastDate.lastIndexOf('/')));
        mTvDay1.setText(lastDate.substring(lastDate.lastIndexOf('/')+1,lastDate.length()));
        seven = TimeUtils.getStatetime(6);
        today = TimeUtils.getStatetime(0);
        thirty = TimeUtils.getStatetime(30);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStatisticsPresenter.detachView();
    }

    @Override
    public void changeStartTime(int year, int mouth, int day) {
        mTvYear1.setText(year + "");
        mTvMouth1.setText(mouth + "");
        mTvDay1.setText(day + "");
    }

    @Override
    public void changeEndTime(int year, int mouth, int day) {
        mTvYear2.setText(year + "");
        mTvMouth2.setText(mouth + "");
        mTvDay2.setText(day + "");
    }

    @OnClick({R.id.tv_seven_day, R.id.tv_thirty_day, R.id.ral_start_time, R.id.ral_end_time,R.id.ral_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_seven_day:
                mStatisticsPresenter.quickStat(1,mChart);
                mTvSevenDay.setBackgroundResource(R.drawable.ic_day_choose);
                mTvSevenDay.setTextColor(Color.parseColor("#ffffff"));
                mTvThirtyDay.setBackgroundResource(R.drawable.ic_day_no_choose);
                mTvThirtyDay.setTextColor(Color.parseColor("#54b4fc"));
                mTvYear1.setText(seven.substring(0,seven.indexOf('/')));
                mTvMouth1.setText(seven.substring(seven.indexOf('/')+1,seven.lastIndexOf('/')));
                mTvDay1.setText(seven.substring(seven.lastIndexOf('/')+1,seven.length()));

                mTvYear2.setText(today.substring(0,today.indexOf('/')));
                mTvMouth2.setText(today.substring(today.indexOf('/')+1,today.lastIndexOf('/')));
                mTvDay2.setText(today.substring(today.lastIndexOf('/')+1,today.length()));
                break;
            case R.id.tv_thirty_day:
                mStatisticsPresenter.quickStat(2,mChart);
                mTvSevenDay.setBackgroundResource(R.drawable.ic_day_no_choose);
                mTvSevenDay.setTextColor(Color.parseColor("#54b4fc"));
                mTvThirtyDay.setBackgroundResource(R.drawable.ic_day_choose);
                mTvThirtyDay.setTextColor(Color.parseColor("#ffffff"));
                mTvYear1.setText(thirty.substring(0,thirty.indexOf('/')));
                mTvMouth1.setText(thirty.substring(thirty.indexOf('/')+1,thirty.lastIndexOf('/')));
                mTvDay1.setText(thirty.substring(thirty.lastIndexOf('/')+1,thirty.length()));

                mTvYear2.setText(today.substring(0,today.indexOf('/')));
                mTvMouth2.setText(today.substring(today.indexOf('/')+1,today.lastIndexOf('/')));
                mTvDay2.setText(today.substring(today.lastIndexOf('/')+1,today.length()));
                break;
            case R.id.ral_exit:
                AnswerStatisticsActivity.this.finish();
                break;
            case R.id.ral_start_time:
                mStatisticsPresenter.chooseSystemTime(true,mChart);
                break;
            case R.id.ral_end_time:
                mStatisticsPresenter.chooseSystemTime(false,mChart);
                break;
        }
    }
}
