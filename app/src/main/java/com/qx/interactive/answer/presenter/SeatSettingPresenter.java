package com.qx.interactive.answer.presenter;

import android.support.v4.app.FragmentManager;

import com.qx.interactive.answer.app.BaseConstants;
import com.qx.interactive.answer.model.SeatPerson;
import com.qx.interactive.answer.ui.BaseFragment;
import com.qx.interactive.answer.ui.adapter.MyFragmentAdatper;
import com.qx.interactive.answer.ui.controlView.SeatSetView;
import com.qx.interactive.answer.ui.fragment.BindingCardFragment;
import com.qx.interactive.answer.ui.fragment.SettingSeatFragment;
import com.qx.interactive.answer.utils.SaveSeatPersonUtils;

import java.util.ArrayList;

/**
 * Created by HeYingXin on 2017/2/20.
 */
public class SeatSettingPresenter extends BasePresenter<SeatSetView>{

    protected ArrayList<BaseFragment> mFragments;
    protected MyFragmentAdatper mFragmentAdatper;

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void attachView(SeatSetView mvpView) {
        super.attachView(mvpView);
    }

    public void initFragment(){
        mFragments = new ArrayList<>();
        mFragments.add(SettingSeatFragment.newInstance());
        mFragments.add(BindingCardFragment.newInstance());
        FragmentManager fm = getMvpView().getActivity().getSupportFragmentManager();
        mFragmentAdatper  = new MyFragmentAdatper(fm, mFragments);
        getMvpView().setFragment(mFragmentAdatper);
    }

    //将f1的数据同步到f2
    public void syncData(){
        mFragments.get(1).setData(mFragments.get(0).getData(),mFragments.get(0).getColumu());
    }

    //将f2的数据同步到f1
    public void syncData2(){
        mFragments.get(0).setData(mFragments.get(1).getData(),mFragments.get(1).getColumu());
    }

    public void exitSaveBind(){
        ArrayList<SeatPerson> list = mFragments.get(1).getData();
        if(list==null||list.size()<=0){
            //如果页面2没有  那就从页面1保存
            list = mFragments.get(0).getData();
        }
        if(list!=null) {
            try {
                SaveSeatPersonUtils.getInstance().save(list, BaseConstants.BIND_SEAT);
            } catch (Exception e) {
                getMvpView().showError("保存绑定座位出错:"+e.toString(),null);
            }
        }
    }

}
