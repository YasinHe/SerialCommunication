package com.qx.interactive.answer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qx.interactive.answer.R;
import com.qx.interactive.answer.app.BaseConstants;
import com.qx.interactive.answer.interfaces.IconnectFailureCallback;
import com.qx.interactive.answer.interfaces.IsubjectCallBack;
import com.qx.interactive.answer.model.SeatPerson;
import com.qx.interactive.answer.presenter.SubjectPresenter;
import com.qx.interactive.answer.service.OtgService;
import com.qx.interactive.answer.ui.BaseActivity;
import com.qx.interactive.answer.ui.adapter.SubjectAdapter;
import com.qx.interactive.answer.ui.controlView.SubjectView;
import com.qx.interactive.answer.ui.selfView.SpacesItemDecoration;
import com.qx.interactive.answer.utils.LogUtils;
import com.qx.interactive.answer.utils.OtgUtils;
import com.qx.interactive.answer.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HeYingXin on 2017/2/23.
 */
public class SubjectActivity extends BaseActivity implements SubjectView, ViewTreeObserver.OnGlobalLayoutListener,
        IsubjectCallBack, IconnectFailureCallback {

    //正确答案  红框
    @Bind(R.id.iv_Right_right)
    ImageView mIvRightRight;
    @Bind(R.id.iv_Wrong_right)
    ImageView mIvWrongRight;
    @Bind(R.id.iv_A_right)
    ImageView mIvARight;
    @Bind(R.id.iv_B_right)
    ImageView mIvBRight;
    @Bind(R.id.iv_C_right)
    ImageView mIvCRight;
    @Bind(R.id.iv_D_right)
    ImageView mIvDRight;
    @Bind(R.id.iv_E_right)
    ImageView mIvERight;
    @Bind(R.id.iv_F_right)
    ImageView mIvFRight;
    @Bind(R.id.v_line)
    View mVLine;
    @Bind(R.id.iv_icon_right)
    TextView mIvIconRight;
    @Bind(R.id.iv_icon_Wrong)
    ImageView mIvIconWrong;
    @Bind(R.id.tv_icon_A)
    TextView mTvIconA;
    @Bind(R.id.tv_icon_B)
    TextView mTvIconB;
    @Bind(R.id.tv_icon_C)
    TextView mTvIconC;
    @Bind(R.id.tv_icon_D)
    TextView mTvIconD;
    @Bind(R.id.tv_icon_E)
    TextView mTvIconE;
    @Bind(R.id.tv_icon_F)
    TextView mTvIconF;
    @Bind(R.id.lly_funcation)
    LinearLayout mLlyFuncation;
    @Bind(R.id.v_line2)
    View mVLine2;
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_exit)
    TextView mTvExit;
    @Bind(R.id.ral_left_part)
    RelativeLayout mRalLeftPart;
    @Bind(R.id.iv_unuse)
    ImageView mIvUnuse;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.lly_Right_part)
    LinearLayout mLlyRightPart;
    //正确率
    @Bind(R.id.tv_accuracy)
    TextView mTvAccuracy;
    //参与人数
    @Bind(R.id.tv_take_part)
    TextView mTvTakePart;
    @Bind(R.id.iv_Right_line)
    ImageView mIvRightLine;
    @Bind(R.id.tv_Right_precent)
    TextView mTvRightPrecent;
    @Bind(R.id.tv_Right_count)
    TextView mTvRightCount;
    @Bind(R.id.ral_part_Right)
    RelativeLayout mRalPartRight;
    @Bind(R.id.iv_Wrong_line)
    ImageView mIvWrongLine;
    @Bind(R.id.tv_Wrong_precent)
    TextView mTvWrongPrecent;
    @Bind(R.id.tv_Wrong_count)
    TextView mTvWrongCount;
    @Bind(R.id.ral_part_Wrong)
    RelativeLayout mRalPartWrong;
    @Bind(R.id.iv_A_line)
    ImageView mIvALine;
    @Bind(R.id.tv_A_precent)
    TextView mTvAPrecent;
    @Bind(R.id.tv_A_count)
    TextView mTvACount;
    @Bind(R.id.ral_part_A)
    RelativeLayout mRalPartA;
    @Bind(R.id.iv_B_line)
    ImageView mIvBLine;
    @Bind(R.id.tv_B_precent)
    TextView mTvBPrecent;
    @Bind(R.id.tv_B_count)
    TextView mTvBCount;
    @Bind(R.id.ral_part_B)
    RelativeLayout mRalPartB;
    @Bind(R.id.iv_C_line)
    ImageView mIvCLine;
    @Bind(R.id.tv_C_precent)
    TextView mTvCPrecent;
    @Bind(R.id.tv_C_count)
    TextView mTvCCount;
    @Bind(R.id.ral_part_C)
    RelativeLayout mRalPartC;
    @Bind(R.id.iv_D_line)
    ImageView mIvDLine;
    @Bind(R.id.tv_D_precent)
    TextView mTvDPrecent;
    @Bind(R.id.tv_D_count)
    TextView mTvDCount;
    @Bind(R.id.ral_part_D)
    RelativeLayout mRalPartD;
    @Bind(R.id.iv_E_line)
    ImageView mIvELine;
    @Bind(R.id.tv_E_precent)
    TextView mTvEPrecent;
    @Bind(R.id.tv_E_count)
    TextView mTvECount;
    @Bind(R.id.ral_part_E)
    RelativeLayout mRalPartE;
    @Bind(R.id.iv_F_line)
    ImageView mIvFLine;
    @Bind(R.id.tv_F_precent)
    TextView mTvFPrecent;
    @Bind(R.id.tv_F_count)
    TextView mTvFCount;
    @Bind(R.id.ral_part_F)
    RelativeLayout mRalPartF;
    @Bind(R.id.tv_start_choose)
    TextView mTvStartChoose;
    @Bind(R.id.tv_start_judge)
    TextView mTvStartJudge;
    @Bind(R.id.ral_exit)
    RelativeLayout mRalExit;
    @Bind(R.id.rcy_seats)
    RecyclerView mRcySeats;
    @Bind(R.id.ral_show_judge)
    RelativeLayout mRalShowJudge;//展示正确错误
    @Bind(R.id.iv_Right)
    ImageView mIvRight;
    @Bind(R.id.iv_Wrong)
    ImageView mIvWrong;
    @Bind(R.id.iv_A)
    ImageView mIvA;
    @Bind(R.id.iv_B)
    ImageView mIvB;
    @Bind(R.id.iv_C)
    ImageView mIvC;
    @Bind(R.id.iv_D)
    ImageView mIvD;
    @Bind(R.id.iv_E)
    ImageView mIvE;
    @Bind(R.id.iv_F)
    ImageView mIvF;

    SubjectPresenter mSubjectPresenter;
    SubjectAdapter mSubjectAdapter;
    List<SeatPerson> mPersons = new ArrayList<>();
    volatile int oldColumu = -1;
    private volatile int screenRcyHeigh = 0, screenRcyWidth = 0;
    private int allCount, mTarkPartCount;
    private boolean chooise;
    float selectanceA, selectanceB, selectanceC, selectanceD, selectanceE, selectanceF, selectanceR, selectanceW;
    String sSelectanceA, sSelectanceB, sSelectanceC, sSelectanceD, sSelectanceE, sSelectanceF, sSelectanceR, sSelectanceW;
    private String techRightAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        mSubjectPresenter = new SubjectPresenter();
        mSubjectPresenter.attachView(this);
        mRcySeats.setNestedScrollingEnabled(false);
        mRcySeats.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(R.dimen.W_DIMEN_6PX)));
        mRcySeats.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void initData() {
        OtgService.setCallBackListener(this);
        mSubjectPresenter.initIntent(getIntent());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        mSubjectPresenter.reFresh(0);
        UIHelper.dismissProgressDialog();
        OtgService.unSetCallBackListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubjectPresenter.detachView();
    }

    public static void EntrySubject(Context mContext, boolean choice) {
        Intent intent = new Intent(mContext, SubjectActivity.class);
        intent.putExtra(BaseConstants.SUBJECTACTIVITY_CHOOISE, choice);
        mContext.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        OtgService.setCallBackListener(this);
        techRightAnswer = "";
        mSubjectPresenter.initIntent(intent);
        backAnswerCount(0);
        super.onNewIntent(intent);
    }

    @OnClick({R.id.ral_part_Right, R.id.ral_part_Wrong,
            R.id.ral_part_A, R.id.ral_part_B, R.id.ral_part_C,
            R.id.ral_part_D, R.id.ral_part_E, R.id.ral_part_F,
            R.id.tv_start_choose, R.id.tv_start_judge, R.id.ral_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ral_part_Right:
                techRightAnswer = mSubjectPresenter.clickCorrentResponse("正确", mIvRight);
                mSubjectAdapter.setRightAnswer(techRightAnswer);
                mIvWrong.setBackgroundResource(R.drawable.ic_null_choose);
                break;
            case R.id.ral_part_Wrong:
                techRightAnswer = mSubjectPresenter.clickCorrentResponse("错误", mIvWrong);
                mSubjectAdapter.setRightAnswer(techRightAnswer);
                mIvRight.setBackgroundResource(R.drawable.ic_null_choose);
                break;
            case R.id.ral_part_A:
                String resultA = mSubjectPresenter.clickCorrentResponse("A", mIvA);
                techRightAnswer = resultA;
                mSubjectAdapter.setRightAnswer(resultA);
                break;
            case R.id.ral_part_B:
                String resultB = mSubjectPresenter.clickCorrentResponse("B", mIvB);
                techRightAnswer = resultB;
                mSubjectAdapter.setRightAnswer(resultB);
                break;
            case R.id.ral_part_C:
                String resultC = mSubjectPresenter.clickCorrentResponse("C", mIvC);
                techRightAnswer = resultC;
                mSubjectAdapter.setRightAnswer(resultC);
                break;
            case R.id.ral_part_D:
                String resultD = mSubjectPresenter.clickCorrentResponse("D", mIvD);
                techRightAnswer = resultD;
                mSubjectAdapter.setRightAnswer(resultD);
                break;
            case R.id.ral_part_E:
                String resultE = mSubjectPresenter.clickCorrentResponse("E", mIvE);
                techRightAnswer = resultE;
                mSubjectAdapter.setRightAnswer(resultE);
                break;
            case R.id.ral_part_F:
                String resultF = mSubjectPresenter.clickCorrentResponse("F", mIvF);
                techRightAnswer = resultF;
                mSubjectAdapter.setRightAnswer(resultF);
                break;
            case R.id.tv_start_choose:
                mSubjectPresenter.reFresh(1);
                break;
            case R.id.tv_start_judge:
                mSubjectPresenter.reFresh(2);
                break;
            case R.id.ral_exit:
                SubjectActivity.this.finish();
                break;
        }
    }

    @Override
    public void reFreshData(List<SeatPerson> list, final int columu, final int nullSeat) {
        mPersons.clear();
        mPersons.addAll(list);
        // judge for first execute
        mRcySeats.post(new Runnable() {
            @Override
            public void run() {
                allCount = mPersons.size() - nullSeat;
                if (oldColumu != columu) {
                    oldColumu = columu;
                    mRcySeats.setLayoutManager(new GridLayoutManager(mContext, columu));
                    mSubjectAdapter = new SubjectAdapter(mContext, mPersons, screenRcyHeigh, screenRcyWidth, columu,
                            SubjectActivity.this, techRightAnswer);
                    mRcySeats.setAdapter(mSubjectAdapter);
                    String result = "参与人数：<font color='red'>%d</font>";
                    result = String.format(result, 0) + "/" + allCount;
                    mTvTakePart.setText(Html.fromHtml(result));
                } else {
                    mSubjectAdapter.reFresh(mPersons, screenRcyHeigh, screenRcyWidth, columu, techRightAnswer);
                }
            }
        });
    }

    @Override
    public void showUiByType(boolean isChooise) {
        chooise = isChooise;
        if (isChooise) {
            mRalPartA.setVisibility(View.VISIBLE);
            mRalPartB.setVisibility(View.VISIBLE);
            mRalPartC.setVisibility(View.VISIBLE);
            mRalPartD.setVisibility(View.VISIBLE);
            mRalPartE.setVisibility(View.VISIBLE);
            mRalPartF.setVisibility(View.VISIBLE);
            mRalPartRight.setVisibility(View.GONE);
            mRalPartWrong.setVisibility(View.GONE);
        } else {
            mRalPartRight.setVisibility(View.VISIBLE);
            mRalPartWrong.setVisibility(View.VISIBLE);
            mRalPartA.setVisibility(View.GONE);
            mRalPartB.setVisibility(View.GONE);
            mRalPartC.setVisibility(View.GONE);
            mRalPartD.setVisibility(View.GONE);
            mRalPartE.setVisibility(View.GONE);
            mRalPartF.setVisibility(View.GONE);
        }
    }

    @Override
    public SubjectActivity getActivity() {
        return this;
    }

    @Override
    public void reFreshStatistics(final int tarkPartCount, final int a,
                                  final int b, final int c, final int d, final int e,
                                  final int f, final int right, final int wrong) {
        mTarkPartCount = tarkPartCount;
        String result = "参与人数：<font color='red'>%d</font>";
        result = String.format(result, tarkPartCount) + "/" + allCount;
        final String finalResult = result;
        OtgUtils.myPercent(1, 1);
        if (a != 0 && tarkPartCount != 0) {
            selectanceA = (float) a / tarkPartCount;
            sSelectanceA = OtgUtils.myPercent(a, tarkPartCount);
        } else {
            selectanceA = 0;
            sSelectanceA = "0%";
        }
        if (b != 0 && tarkPartCount != 0) {
            selectanceB = (float) b / tarkPartCount;
            sSelectanceB = OtgUtils.myPercent(b, tarkPartCount);
        } else {
            selectanceB = 0;
            sSelectanceB = "0%";
        }
        if (c != 0 && tarkPartCount != 0) {
            selectanceC = (float) c / tarkPartCount;
            sSelectanceC = OtgUtils.myPercent(c, tarkPartCount);
        } else {
            selectanceC = 0;
            sSelectanceC = "0%";
        }
        if (d != 0 && tarkPartCount != 0) {
            selectanceD = (float) d / tarkPartCount;
            sSelectanceD = OtgUtils.myPercent(d, tarkPartCount);
        } else {
            selectanceD = 0;
            sSelectanceD = "0%";
        }
        if (e != 0 && tarkPartCount != 0) {
            selectanceE = (float) e / tarkPartCount;
            sSelectanceE = OtgUtils.myPercent(e, tarkPartCount);
        } else {
            selectanceE = 0;
            sSelectanceE = "0%";
        }
        if (f != 0 && tarkPartCount != 0) {
            selectanceF = (float) f / tarkPartCount;
            sSelectanceF = OtgUtils.myPercent(f, tarkPartCount);
        } else {
            selectanceF = 0;
            sSelectanceF = "0%";
        }
        if (right != 0 && tarkPartCount != 0) {
            selectanceR = (float) right / tarkPartCount;
            sSelectanceR = OtgUtils.myPercent(right, tarkPartCount);
        } else {
            selectanceR = 0;
            sSelectanceR = "0%";
        }
        if (wrong != 0 && tarkPartCount != 0) {
            selectanceW = (float) wrong / tarkPartCount;
            sSelectanceW = OtgUtils.myPercent(wrong, tarkPartCount);
        } else {
            selectanceW = 0;
            sSelectanceW = "0%";
        }
        mRcySeats.post(new Runnable() {
            @Override
            public void run() {
                //参与率改变
                mTvTakePart.setText(Html.fromHtml(finalResult));
                float maxWidth = getResources().getDimension(R.dimen.W_DIMEN_200PX);
                if (chooise) {
                    //改变A
                    RelativeLayout.LayoutParams paramsa = (RelativeLayout.LayoutParams) mIvALine.getLayoutParams();
                    if (selectanceA != 0) {
                        paramsa.width = (int) (maxWidth * selectanceA);
                    } else {
                        paramsa.width = 0;
                    }
                    mIvALine.setLayoutParams(paramsa);
                    mTvAPrecent.setText(sSelectanceA);
                    mTvACount.setText(a + "人");
                    //改变B
                    RelativeLayout.LayoutParams paramsb = (RelativeLayout.LayoutParams) mIvBLine.getLayoutParams();
                    if (selectanceB != 0) {
                        paramsb.width = (int) (maxWidth * selectanceB);
                    } else {
                        paramsb.width = 0;
                    }
                    mIvBLine.setLayoutParams(paramsb);
                    mTvBPrecent.setText(sSelectanceB);
                    mTvBCount.setText(b + "人");
                    //改变C
                    RelativeLayout.LayoutParams paramsc = (RelativeLayout.LayoutParams) mIvCLine.getLayoutParams();
                    if (selectanceC != 0) {
                        paramsc.width = (int) (maxWidth * selectanceC);
                    } else {
                        paramsc.width = 0;
                    }
                    mIvCLine.setLayoutParams(paramsc);
                    mTvCPrecent.setText(sSelectanceC);
                    mTvCCount.setText(c + "人");
                    //改变D
                    RelativeLayout.LayoutParams paramsd = (RelativeLayout.LayoutParams) mIvDLine.getLayoutParams();
                    if (selectanceD != 0) {
                        paramsd.width = (int) (maxWidth * selectanceD);
                    } else {
                        paramsd.width = 0;
                    }
                    mIvDLine.setLayoutParams(paramsd);
                    mTvDPrecent.setText(sSelectanceD);
                    mTvDCount.setText(d + "人");
                    //改变E
                    RelativeLayout.LayoutParams paramse = (RelativeLayout.LayoutParams) mIvELine.getLayoutParams();
                    if (selectanceE != 0) {
                        paramse.width = (int) (maxWidth * selectanceE);
                    } else {
                        paramse.width = 0;
                    }
                    mIvELine.setLayoutParams(paramse);
                    mTvEPrecent.setText(sSelectanceE);
                    mTvECount.setText(e + "人");
                    //改变F
                    RelativeLayout.LayoutParams paramsf = (RelativeLayout.LayoutParams) mIvFLine.getLayoutParams();
                    if (selectanceF != 0) {
                        paramsf.width = (int) (maxWidth * selectanceF);
                    } else {
                        paramsf.width = 0;
                    }
                    mIvFLine.setLayoutParams(paramsf);
                    mTvFPrecent.setText(sSelectanceF);
                    mTvFCount.setText(f + "人");
                } else {
                    //改变Right
                    RelativeLayout.LayoutParams paramsr = (RelativeLayout.LayoutParams) mIvRightLine.getLayoutParams();
                    if (selectanceR != 0) {
                        paramsr.width = (int) (maxWidth * selectanceR);
                    } else {
                        paramsr.width = 0;
                    }
                    mIvRightLine.setLayoutParams(paramsr);
                    mTvRightPrecent.setText(sSelectanceR);
                    mTvRightCount.setText(right + "人");
                    //改变Wrong
                    RelativeLayout.LayoutParams paramsw = (RelativeLayout.LayoutParams) mIvWrongLine.getLayoutParams();
                    if (selectanceW != 0) {
                        paramsw.width = (int) (maxWidth * selectanceW);
                    } else {
                        paramsw.width = 0;
                    }
                    mIvWrongLine.setLayoutParams(paramsw);
                    mTvWrongPrecent.setText(sSelectanceW);
                    mTvWrongCount.setText(wrong + "人");
                }
            }
        });
    }

    @Override
    public void chooseChangeUi(String result) {
        mIvRightLine.setBackgroundResource(R.drawable.bg_r);
        mIvWrongLine.setBackgroundResource(R.drawable.bg_w);
        mIvALine.setBackgroundResource(R.drawable.bg_a);
        mTvIconA.setTextColor(Color.parseColor("#39a3fb"));
        mIvBLine.setBackgroundResource(R.drawable.bg_b);
        mTvIconB.setTextColor(Color.parseColor("#39a3fb"));
        mIvCLine.setBackgroundResource(R.drawable.bg_c);
        mTvIconC.setTextColor(Color.parseColor("#39a3fb"));
        mIvDLine.setBackgroundResource(R.drawable.bg_d);
        mTvIconD.setTextColor(Color.parseColor("#39a3fb"));
        mIvELine.setBackgroundResource(R.drawable.bg_e);
        mTvIconE.setTextColor(Color.parseColor("#39a3fb"));
        mIvFLine.setBackgroundResource(R.drawable.bg_f);
        mTvIconF.setTextColor(Color.parseColor("#39a3fb"));
        if (TextUtils.isEmpty(result)) {
            mIvRight.setBackgroundResource(R.drawable.ic_null_choose);
            mIvWrong.setBackgroundResource(R.drawable.ic_null_choose);
            mIvA.setBackgroundResource(R.drawable.ic_null_choose);
            mIvB.setBackgroundResource(R.drawable.ic_null_choose);
            mIvC.setBackgroundResource(R.drawable.ic_null_choose);
            mIvD.setBackgroundResource(R.drawable.ic_null_choose);
            mIvE.setBackgroundResource(R.drawable.ic_null_choose);
            mIvF.setBackgroundResource(R.drawable.ic_null_choose);
        }
        if (!result.contains("正确") && !TextUtils.isEmpty(result)) {
            mIvRightLine.setBackgroundResource(R.drawable.bg_wrong_answer);
            mIvRightRight.setVisibility(View.GONE);
        }
        if (!result.contains("错误") && !TextUtils.isEmpty(result)) {
            mIvWrongLine.setBackgroundResource(R.drawable.bg_wrong_answer);
            mIvWrongRight.setVisibility(View.GONE);
        }
        if (!result.contains("A") && !TextUtils.isEmpty(result)) {
            mIvALine.setBackgroundResource(R.drawable.bg_wrong_answer);
            mTvIconA.setTextColor(Color.parseColor("#fd3737"));
            mIvARight.setVisibility(View.GONE);
        }
        if (!result.contains("B") && !TextUtils.isEmpty(result)) {
            mIvBLine.setBackgroundResource(R.drawable.bg_wrong_answer);
            mTvIconB.setTextColor(Color.parseColor("#fd3737"));
            mIvBRight.setVisibility(View.GONE);
        }
        if (!result.contains("C") && !TextUtils.isEmpty(result)) {
            mIvCLine.setBackgroundResource(R.drawable.bg_wrong_answer);
            mTvIconC.setTextColor(Color.parseColor("#fd3737"));
            mIvCRight.setVisibility(View.GONE);
        }
        if (!result.contains("D") && !TextUtils.isEmpty(result)) {
            mIvDLine.setBackgroundResource(R.drawable.bg_wrong_answer);
            mTvIconD.setTextColor(Color.parseColor("#fd3737"));
            mIvDRight.setVisibility(View.GONE);
        }
        if (!result.contains("E") && !TextUtils.isEmpty(result)) {
            mIvELine.setBackgroundResource(R.drawable.bg_wrong_answer);
            mTvIconE.setTextColor(Color.parseColor("#fd3737"));
            mIvERight.setVisibility(View.GONE);
        }
        if (!result.contains("F") && !TextUtils.isEmpty(result)) {
            mIvFLine.setBackgroundResource(R.drawable.bg_wrong_answer);
            mTvIconF.setTextColor(Color.parseColor("#fd3737"));
            mIvFRight.setVisibility(View.GONE);
        }

    }

    @Override
    public void onGlobalLayout() {
        mRcySeats.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (screenRcyHeigh == 0 || screenRcyWidth == 0) {
                    screenRcyHeigh = mRcySeats.getHeight() - 180;
                    screenRcyWidth = mRcySeats.getWidth() - 50;
                    if (mSubjectAdapter != null && mPersons != null) {
                        mSubjectAdapter.reFresh(mPersons, screenRcyHeigh, screenRcyWidth, oldColumu, techRightAnswer);
                    }
                }
            }
        }, 300);
    }

    @Override
    public void backAnswerCount(int count) {
        if (count != 0) {
            String temp = OtgUtils.myPercent(count, mTarkPartCount);
            String result = "正确率：<font color='red'>%s</font>";
            result = String.format(result, temp);
            mTvAccuracy.setText(Html.fromHtml(result));
            mSubjectPresenter.addSubjectData(temp);
        } else {
            if (mTarkPartCount > 0) {
                String result = "正确率：<font color='red'>%s</font>";
                result = String.format(result, "0%");
                mTvAccuracy.setText(Html.fromHtml(result));
                mSubjectPresenter.addSubjectData("0%");
            } else {
                mTvAccuracy.setText("正确率：");
            }
        }
    }

    PopupWindow popupWindow;

    //被长按，意思要弹出popuWindos
    @Override
    public void longClickItem(View v, SeatPerson person) {
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.view_popu_subject, null);
        TextView tv_seat_id = (TextView) contentView.findViewById(R.id.tv_seat_id);
        TextView tv_card_id = (TextView) contentView.findViewById(R.id.tv_card_id);
        TextView tv_result = (TextView) contentView.findViewById(R.id.tv_result);
        tv_seat_id.setText("座位号："+(Integer.parseInt(person.seatId)+1));
        tv_card_id.setText("卡号："+person.cardId);
        tv_result.setText("已选："+person.chooseResult);
        popupWindow = new PopupWindow(contentView, getResources().getDimensionPixelOffset(R.dimen.W_DIMEN_124PX),
                getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_90PX), true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        LogUtils.e("TAG","当前控件的坐标位置:"+location[0]+"----:"+location[1]);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - (popupWindow.getWidth() / 2),
                location[1] - popupWindow.getHeight());
    }


    @UiThread
    @Override
    public void rePeat() {
        mRcySeats.postDelayed(new Runnable() {
            @Override
            public void run() {
                UIHelper.showProgressDialog(SubjectActivity.this, "正在初始化连接设备...");
                mSubjectPresenter.initOtg();
            }
        }, 1000);
    }

    @Override
    public void success() {
        UIHelper.dismissProgressDialog();
    }

    @Override
    public void nullDevice() {
        UIHelper.dismissProgressDialog();
        Toast.makeText(mContext, "未检测到连接设备，请保持答题宝设备连接", Toast.LENGTH_LONG).show();
    }
}
