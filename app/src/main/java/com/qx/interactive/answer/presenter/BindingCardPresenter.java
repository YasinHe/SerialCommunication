package com.qx.interactive.answer.presenter;

import android.text.TextUtils;

import com.qx.interactive.answer.app.BaseApp;
import com.qx.interactive.answer.interfaces.IotgServerCallBack;
import com.qx.interactive.answer.model.SeatPerson;
import com.qx.interactive.answer.service.OtgService;
import com.qx.interactive.answer.ui.controlView.CardBindingView;
import com.qx.interactive.answer.utils.LogUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HeYingXin on 2017/2/21.
 */
public class BindingCardPresenter extends BasePresenter<CardBindingView> implements IotgServerCallBack{

    protected int columu;
    protected long oldCardId;
    protected ArrayList<SeatPerson> mPersons;
    protected SeatPerson currentSeat;//当前等待绑定的座位对象
    protected int currentCardCount = 1;//行云答题宝支持200张卡，答题卡绑定不能为0,50,100,150,200这几个数
    protected List<String> boundId = new ArrayList<>();//已绑定过的卡号
    private volatile boolean isWainBinding = false;
    private volatile boolean isHandShakeing = false;
    @Override
    public void attachView(CardBindingView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void initData(List<SeatPerson> list, int col){
        mPersons = new ArrayList<>();
        mPersons.clear();
        mPersons.addAll(list);
        columu = col;
        getMvpView().reFreshData(mPersons,columu);
        OtgService.registCallBack(this);
        //先初始化答题宝
        BaseApp.getInstance().binder.stopBd();
        BaseApp.getInstance().binder.cleanIdList();
        BaseApp.getInstance().binder.startBd();
        BaseApp.getInstance().binder.finishAnswer();
        SeatPerson temp = new SeatPerson();
        for(SeatPerson person:mPersons){
            if(person.toBeBinding){
                temp = person;
            }
        }
        requestBind(temp);
    }

    public void requestBind(SeatPerson seatPerson){
        //每次绑定前都确定一遍连接可用性
        BaseApp.getInstance().binder.checkConncet();
        boundId.clear();
        currentSeat = seatPerson;
        for(SeatPerson person:mPersons){
            person.toBeBinding = false;
            if(person.seatId.equals(currentSeat.seatId)){
                person.toBeBinding = true;
            }
            if(!TextUtils.isEmpty(person.cardId)){
                boundId.add(person.cardId);
            }
        }
        getMvpView().reFreshData(mPersons,columu);
        isWainBinding = true;
    }

    //异步
    @Override
    public void callBack(String result) {
        LogUtils.e("有人按下了答题键：" + result + "他的物理卡号为："
                + new BigInteger(result.substring(0, 8), 16).toString());
        String tag = result.substring(8,10);
        //握手成功(白名单已注入)
        if(currentSeat != null &&isWainBinding) {//&&TextUtils.isEmpty(currentSeat.cardId)
            synchronized (currentSeat) {
                if (tag.equals("f4")) {
                    if (currentCardCount > 200) {
                        getMvpView().ToastMsg(1,null);
                    } else {
                        long cardId = new BigInteger(result.substring(0, 8), 16).longValue();
                        if (boundId.indexOf("" + cardId) != -1 || boundId.indexOf("0" + cardId) != -1) {
                            //说明这张卡号已经绑定过了
                            getMvpView().ToastMsg(3,null);
                            return;
                        }
                        for (SeatPerson person : mPersons) {
                            person.toBeBinding = false;
                            if (person.seatId.equals(currentSeat.seatId)) {
                                //重新绑定那个座位
                                if(!TextUtils.isEmpty(currentSeat.cardId)){
                                    boundId.remove(currentSeat.cardId);
                                }
                                if (cardId < 1000000000) {
                                    person.cardId = "0" + cardId;
                                    boundId.add("0" + cardId);
                                } else {
                                    person.cardId = cardId + "";
                                    boundId.add("0" + cardId);
                                }
                            }
                        }
                        //握手绑定成功
                        ++currentCardCount;
                        getMvpView().reFreshData(mPersons, columu);
                        isWainBinding = false;
                        isHandShakeing = false;
                        getMvpView().ToastMsg(2,boundId.get(boundId.size()-1));
                    }
                } else if (tag.equals("a0")&&!isHandShakeing) {
                    if (oldCardId == new BigInteger(result.substring(0, 8), 16).longValue()) {
                        //第一次按答题的机器,会重复多次发消息,这里起到过滤作用
                    } else {
                        oldCardId = new BigInteger(result.substring(0, 8), 16).longValue();
                        if (boundId.indexOf("" + oldCardId) != -1 || boundId.indexOf("0" + oldCardId) != -1) {
                            //说明这张卡号已经绑定过了
                            getMvpView().ToastMsg(3,null);
                            return;
                        }
                        if (currentCardCount == 50 || currentCardCount == 100 || currentCardCount == 150
                                || currentCardCount == 200) {
                            ++currentCardCount;
                        }
                        if (currentCardCount > 200) {
                            getMvpView().ToastMsg(1,null);
                        } else {
                            BaseApp.getInstance().binder.handShake(currentCardCount, oldCardId);//握手
                            isHandShakeing = true;
                        }
                    }
                } else if (tag.equals("a6")) {

                }
            }
        }
    }
}
