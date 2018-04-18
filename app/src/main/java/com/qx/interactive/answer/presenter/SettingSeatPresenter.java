package com.qx.interactive.answer.presenter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.qx.interactive.answer.app.BaseConstants;
import com.qx.interactive.answer.model.SeatPerson;
import com.qx.interactive.answer.ui.controlView.SettingSeatView;
import com.qx.interactive.answer.utils.SaveSeatPersonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HeYingXin on 2017/2/20.
 */
public class SettingSeatPresenter extends BasePresenter<SettingSeatView>{

    protected int columu,row;
    protected ArrayList<SeatPerson> mPersons;

    @Override
    public void attachView(SettingSeatView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    //初始化 6*10的碎片布局
    public void initFragment(){
        mPersons = new ArrayList<>();
        mPersons.clear();
        try {
            List<SeatPerson> personList = SaveSeatPersonUtils.getInstance().inPut(BaseConstants.BIND_SEAT);
            if(personList!=null&&personList.size()>0){
                mPersons.clear();
                mPersons.addAll(personList);
                int tempColumu = mPersons.get(mPersons.size()-1).mColumu;
                columu = tempColumu+1;
                row = mPersons.size()/columu;
                getMvpView().reFreshData(mPersons, columu);
            }
        } catch (Exception e) {
            getMvpView().showError("导入座位错误-->"+e.toString(),null);
        }
        //如果没有本地的座位数据，那就设置原始数据
        if(mPersons!=null&&mPersons.size()>0){
            return;
        }else {
            columu = 6;
            row = 10;
            for (int j = row - 1; j >= 0; j--) {
                for (int i = 0; i < columu; i++) {
                    mPersons.add(new SeatPerson(i + "" + j, "", "", false, i, j, false));
                }
            }
            getMvpView().reFreshData(mPersons, columu);
        }
    }

    public void syncData(List<SeatPerson> list, int col){
        mPersons.clear();
        mPersons.addAll(list);
        columu = col;
        row = mPersons.size()/columu;
        getMvpView().reFreshData(mPersons,columu);
    }

    //重置座位
    public void reSet(){
        mPersons = new ArrayList<>();
        mPersons.clear();
        columu = 6;
        row = 10;
        for (int j = row - 1; j >= 0; j--) {
            for (int i = 0; i < columu; i++) {
                mPersons.add(new SeatPerson(i + "" + j, "", "", false, i, j, false));
            }
        }
        getMvpView().reFreshData(mPersons, columu);
        outPut();
    }

    //点击修改是空还是不为空
    public void doubleCilckEmpty(@Nullable SeatPerson seatPerson){
       int result =  mPersons.indexOf(seatPerson);
        if(result!=-1){
            mPersons.remove(seatPerson);
            if(!seatPerson.emptySeat) {
                seatPerson.emptySeat = true;
                seatPerson.cardId = "";
            }else{
                seatPerson.emptySeat = false;
            }
            mPersons.add(result,seatPerson);
        }
        getMvpView().reFreshData(mPersons,columu);
    }

    /**
     * 修改列数/行数
     * @param type 1 ->row  2->columu
     * @param isAdd  is  Add  or  subtract
     */
    public void changeColumuRow(@Nullable int type,@Nullable boolean isAdd){
        switch (type){
            case 1:
                changeRow(isAdd);
                break;
            case 2:
                changeColumu(isAdd);
                break;
            default:
                Toast.makeText(getMvpView().getContext(),"Type is wrong",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //列数改变
    private void changeColumu(boolean isAdd){
        if (columu >= 10 && isAdd) {
            Toast.makeText(getMvpView().getContext(), "列数已达上限，无法再增加", Toast.LENGTH_SHORT).show();
        } else if (columu <= 1 && !isAdd) {
            Toast.makeText(getMvpView().getContext(), "列数已达下限，无法再减少", Toast.LENGTH_SHORT).show();
        } else {
            if (isAdd) {
                int tempCount = columu;
                int multiple = 1;
                row = mPersons.size() / columu;
                ++columu;
                for (int j = row - 1; j >= 0; j--) {
                    for (int i = 0; i < columu; i++) {
                        if (i == tempCount) {
                            mPersons.add((i * multiple) + (multiple - 1), new SeatPerson(i + "" + j, "", "", false, i, j, false));
                            ++multiple;
                        }
                    }
                }
            } else {
                List<SeatPerson> tempList = new ArrayList<>();
                for (SeatPerson s : mPersons) {
                    if (s.mColumu == columu - 1) {
                        tempList.add(s);
                    }
                }
                mPersons.removeAll(tempList);
                --columu;
            }
            getMvpView().reFreshData(mPersons, columu);
        }
    }

    //行数改变
    private void changeRow(boolean isAdd) {
        if (row >= 10 && isAdd) {
            Toast.makeText(getMvpView().getContext(), "行数已达上限，无法再增加", Toast.LENGTH_SHORT).show();
        } else if (row <= 1 && !isAdd) {
            Toast.makeText(getMvpView().getContext(), "行数已达下限，无法再减少", Toast.LENGTH_SHORT).show();
        } else {
            if (isAdd) {
                row = mPersons.size() / columu;
                ++row;
                for (int i = columu - 1; i >= 0; i--) {
                    mPersons.add(0, new SeatPerson(i + "" + (row - 1), "", "", false, i, (row - 1), false));
                }
            } else {
                --row;
                for (int i = 0; i < columu; i++) {
                    mPersons.remove(0);
                }
            }
            getMvpView().reFreshData(mPersons, columu);
        }
    }

    //保存
    public void saveCurrentSeatSetting(){
        try {
            String result = SaveSeatPersonUtils.getInstance().outPut(mPersons, BaseConstants.BIND_SEAT);
            if(TextUtils.isEmpty(result)){
                Toast.makeText(getMvpView().getContext(),"保存座位设置失败",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getMvpView().getContext(),"保存成功！目录地址为："+result,Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            getMvpView().showError("导出座位错误-->"+e.toString(),null);
        }
    }

    //导入
    public void inPut(){
        try {
            List<SeatPerson> personList = SaveSeatPersonUtils.getInstance().inPut(BaseConstants.BIND_SEAT);
            if(personList!=null&&personList.size()>0){
                mPersons.clear();
                mPersons.addAll(personList);
                int tempColumu = mPersons.get(mPersons.size()-1).mColumu;
                columu = tempColumu+1;
                row = mPersons.size()/columu;
                getMvpView().reFreshData(mPersons, columu);
            }
        } catch (Exception e) {
            getMvpView().showError("导入座位错误-->"+e.toString(),null);
        }
    }

    //导出
    public void outPut(){
        try {
            SaveSeatPersonUtils.getInstance().save(mPersons,BaseConstants.BIND_SEAT);
        } catch (Exception e) {
            getMvpView().showError("导出座位设置错误-->"+e.toString(),null);
        }
    }
}
