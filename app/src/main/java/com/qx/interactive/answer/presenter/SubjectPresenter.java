package com.qx.interactive.answer.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.qx.interactive.answer.R;
import com.qx.interactive.answer.app.BaseApp;
import com.qx.interactive.answer.app.BaseConstants;
import com.qx.interactive.answer.interfaces.IotgServerCallBack;
import com.qx.interactive.answer.model.SeatPerson;
import com.qx.interactive.answer.model.Subject;
import com.qx.interactive.answer.service.OtgService;
import com.qx.interactive.answer.ui.activity.SubjectActivity;
import com.qx.interactive.answer.ui.controlView.SubjectView;
import com.qx.interactive.answer.utils.LogUtils;
import com.qx.interactive.answer.utils.OtgUtils;
import com.qx.interactive.answer.utils.SaveSeatPersonUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HeYingXin on 2017/2/23.
 */
public class SubjectPresenter extends BasePresenter<SubjectView> implements IotgServerCallBack {

    private boolean isChooise;//是否是选择题
    private Subject mSubject;
    private String correctResponse = "";
    private List<SeatPerson> mPersons;
    //列数，参与人数，真实人数，全部位置数量，选择答案  ABCDEF...的人数
    private int nullSeat,columu,row,tarkPartCount,reallyBind,allCount,ChooseA,ChooseB,ChooseC,ChooseD,ChooseE,ChooseF,ChooseRight,ChooseWrong;

    @Override
    public void attachView(SubjectView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void initIntent(Intent intent){
        isChooise = intent.getBooleanExtra(BaseConstants.SUBJECTACTIVITY_CHOOISE,true);
        getMvpView().showUiByType(isChooise);
        mSubject = new Subject();
        if(isChooise) {
            mSubject.setType(1);
        }else {
            mSubject.setType(2);
        }
        ChooseA = 0;
        ChooseB = 0;
        ChooseC = 0;
        ChooseD = 0;
        ChooseE = 0;
        ChooseF = 0;
        ChooseRight = 0;
        ChooseWrong = 0;
        tarkPartCount = 0;
        mSubject.setTime(System.currentTimeMillis());
        mPersons = new ArrayList<>();
        try {
            List<SeatPerson> personList = SaveSeatPersonUtils.getInstance().inPut(BaseConstants.BIND_SEAT);
            mPersons.clear();
            mPersons.addAll(personList);
            if(mPersons.size()>0) {
                int tempColumu = mPersons.get(mPersons.size() - 1).mColumu;
                columu = tempColumu + 1;
                nullSeat = 0;
                for(SeatPerson s:mPersons){
                    if(s.emptySeat){
                        ++nullSeat;
                    }
                }
                allCount = mPersons.size();
                getMvpView().reFreshData(mPersons,columu,nullSeat);
            }
        } catch (Exception e) {
            getMvpView().showError("导入绑定座位错误-->"+e.toString(),null);
        }
        //如果没有本地的座位数据，那就设置原始数据(这个数据仅用于造假，看看而已，不具有任何实际用途)
        if(mPersons==null||mPersons.size()<=0){
            columu = 6;
            row = 10;
            for (int j = row - 1; j >= 0; j--) {
                for (int i = 0; i < columu; i++) {
                    mPersons.add(new SeatPerson(i + "" + j, "", "", false, i, j, false));
                }
            }
            allCount = mPersons.size();
            nullSeat = 0;
            getMvpView().reFreshData(mPersons, columu,nullSeat);
        }
        initOtg();
        correctResponse = "";
        getMvpView().chooseChangeUi(correctResponse);
        getMvpView().reFreshStatistics(tarkPartCount,ChooseA,ChooseB,ChooseC,ChooseD,ChooseE,ChooseF,ChooseRight,ChooseWrong);
    }

    public void initOtg(){
        OtgService.registCallBack(this);
        //先初始化答题宝,开始一道新题目
        BaseApp.getInstance().binder.checkConncet();
        BaseApp.getInstance().binder.stopBd();
        BaseApp.getInstance().binder.startBd();
        BaseApp.getInstance().binder.finishAnswer();
        BaseApp.getInstance().binder.startAnswer();
        //答题宝有断电机制，开始新题就重新绑定一次
        BaseApp.getInstance().binder.cleanIdList();
        reBind();
    }

    private void reBind(){
        int bindCount = 0;
        reallyBind = 0;
        for(SeatPerson s:mPersons){
            if(!TextUtils.isEmpty(s.cardId)){
                ++reallyBind;
                ++bindCount;
                String sendCardId;
                if(s.cardId.charAt(0)=='0'){
                    sendCardId = s.cardId.substring(1,s.cardId.length());
                }else{
                    sendCardId = s.cardId;
                }
                if (bindCount == 50 || bindCount == 100 || bindCount == 150
                        || bindCount == 200) {
                    ++bindCount;
                }
                if (bindCount > 200) {
                    Toast.makeText(getMvpView().getContext(),"最多绑定200张卡",Toast.LENGTH_SHORT).show();
                } else {
                    BaseApp.getInstance().binder.handShake(bindCount, Long.parseLong(sendCardId));//握手
                }
            }
        }
    }

    //教师点击正确答案选项
    public String clickCorrentResponse(String s, View view){
        if(correctResponse.contains(s)){//反选
            correctResponse = correctResponse.replaceAll(s.toString(),"");
            view.setBackgroundResource(R.drawable.ic_null_choose);
            getMvpView().chooseChangeUi(correctResponse);
            return correctResponse;
        }else{//选择
            if(!isChooise){//选择题就一个答案
                correctResponse = s;
            }else {
                correctResponse = correctResponse + s.toString();
            }
            view.setBackgroundResource(R.drawable.iv_have_choose);
            getMvpView().chooseChangeUi(correctResponse);
            return correctResponse;
        }
    }

    //开启一道新题或者关闭界面  0关闭  1选择题  2判断题  3退出回调
    public void reFresh(int type){
        switch (type){
            case 0:
                saveCurrentSubject();
                break;
            case 1:
                saveCurrentSubject();
                SubjectActivity.EntrySubject(getMvpView().getContext(),true);
                break;
            case 2:
                saveCurrentSubject();
                SubjectActivity.EntrySubject(getMvpView().getContext(),false);
                break;
        }
    }

    private void saveCurrentSubject(){
        //如果确实是一道完整的题目记录，就把他保存下来
        if(mSubject!=null&& !TextUtils.isEmpty(mSubject.getAccuraty())
                &&!TextUtils.isEmpty(mSubject.getCorrectResponse())
                &&!TextUtils.isEmpty(mSubject.getPartRale())){
            BaseApp.getInstance().mDB.insertSubject(mSubject);
        }
    }

    public void addSubjectData(String accuracy){
        mSubject.setAccuraty(accuracy);
        mSubject.setPartRale(OtgUtils.myPercent(tarkPartCount,reallyBind));
        mSubject.setCorrectResponse(correctResponse);
    }

    @Override
    public void callBack(String result) {
        LogUtils.e("有人按下了答题键:" + result + "他的物理卡号为:"
                + new BigInteger(result.substring(0, 8), 16).toString());
        String tag = result.substring(8,10);
        //如果教师已经公布正确答案，那是不可以修改答案的
        if(tag.equals("a6")){
            String ansertPerson;//回答人员
            String answerResult;//回答结果
            String resultCode;//消息结果号
            boolean resultIsCalid = false;//该答案是否有效
            if(isChooise){
                answerResult = OtgUtils.studentOneChooseResult(result.substring(10, 12));
            }else{
                answerResult = OtgUtils.studentRightOrWrongChooseResult(result.substring(12, 14));
            }
            long cardId  = new BigInteger(result.substring(0, 8), 16).longValue();
            if (cardId < 1000000000) {
                ansertPerson = "0" + cardId;
            } else {
                ansertPerson = cardId + "";
            }
            resultCode = result.substring(14, 16);
            LogUtils.e("题号",resultCode);
            resultIsCalid = (Integer.parseInt(result.substring(16, 18),16) & 0x80) == 0;
            if(resultIsCalid&&!TextUtils.isEmpty(answerResult)&&!resultCode.equals("00")){
                for(SeatPerson s:mPersons){
                    if(s.cardId.equals(ansertPerson)){
                        String lastAnswer = null;
                        if(TextUtils.isEmpty(s.chooseResult)){//他确实是第一次作答
                            ++tarkPartCount;//参与人数加1
                        }else{
                            lastAnswer = s.chooseResult;//上次做答的答案，这里要准备做减量
                        }
                        s.chooseResult = answerResult;
                        if(isChooise){
                            if (answerResult.contains("A")){
                                ++ChooseA;
                            }
                            if(answerResult.contains("B")){
                                ++ChooseB;
                            }
                            if(answerResult.contains("C")){
                                ++ChooseC;
                            }
                            if(answerResult.contains("D")){
                                ++ChooseD;
                            }
                            if(answerResult.contains("E")){
                                ++ChooseE;
                            }
                            if(answerResult.contains("F")){
                                ++ChooseF;
                            }
                        }else {
                            if (answerResult.contains("正确")){
                                ++ChooseRight;
                            }
                            if(answerResult.contains("错误")){
                                ++ChooseWrong;
                            }
                        }
                        if(!TextUtils.isEmpty(lastAnswer)){
                            if (lastAnswer.contains("A")){
                                --ChooseA;
                            }
                            if(lastAnswer.contains("B")){
                                --ChooseB;
                            }
                            if(lastAnswer.contains("C")){
                                --ChooseC;
                            }
                            if(lastAnswer.contains("D")){
                                --ChooseD;
                            }
                            if(lastAnswer.contains("E")){
                                --ChooseE;
                            }
                            if(lastAnswer.contains("F")){
                                --ChooseF;
                            }
                            if (lastAnswer.contains("正确")){
                                --ChooseRight;
                            }
                            if(lastAnswer.contains("错误")){
                                --ChooseWrong;
                            }
                        }
                    }
                }
                getMvpView().reFreshData(mPersons,columu,nullSeat);
                getMvpView().reFreshStatistics(tarkPartCount,ChooseA,ChooseB,ChooseC,ChooseD,ChooseE,ChooseF,ChooseRight,ChooseWrong);
            }
        }
    }
}
