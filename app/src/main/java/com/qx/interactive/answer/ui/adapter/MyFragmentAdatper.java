package com.qx.interactive.answer.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qx.interactive.answer.ui.BaseFragment;

import java.util.ArrayList;

/**
 * Created by HeYingXin on 2017/2/20.
 */
public class MyFragmentAdatper extends FragmentStatePagerAdapter {

    private ArrayList<BaseFragment> fragments;

    public MyFragmentAdatper(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public int getCount() {
        if(fragments!=null&&fragments.size()>0)
        {
            return fragments.size();
        }else{
            return 0;
        }
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments!=null&&fragments.size()>0)
        {
            return fragments.get(position);
        }else{
            return null;
        }
    }
}
