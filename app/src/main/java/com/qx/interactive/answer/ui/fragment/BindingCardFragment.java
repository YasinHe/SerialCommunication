package com.qx.interactive.answer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.qx.interactive.answer.R;
import com.qx.interactive.answer.interfaces.IsettingSeatCallBack;
import com.qx.interactive.answer.model.SeatPerson;
import com.qx.interactive.answer.presenter.BindingCardPresenter;
import com.qx.interactive.answer.ui.BaseFragment;
import com.qx.interactive.answer.ui.adapter.BindingCardAdapter;
import com.qx.interactive.answer.ui.controlView.CardBindingView;
import com.qx.interactive.answer.ui.selfView.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by HeYingXin on 2017/2/20.
 */
public class BindingCardFragment extends BaseFragment implements CardBindingView,IsettingSeatCallBack
                                                                     ,ViewTreeObserver.OnGlobalLayoutListener{

    BindingCardPresenter mBindingCardPresenter;
    ArrayList<SeatPerson> mPersons;
    BindingCardAdapter mBindingCardAdapter;
    volatile int oldColumu = -1;
    private volatile int screenRcyHeigh = 0,screenRcyWidth = 0;

    @Bind(R.id.rcy_seats)
    RecyclerView mRcySeats;
    @Bind(R.id.tv_name)
    TextView mTvName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_binding_card, container, false);
        onViewCreated(view, savedInstanceState);
        return view;
    }

    @Override
    protected void initView() {
        mBindingCardPresenter = new BindingCardPresenter();
        mBindingCardPresenter.attachView(this);
        mRcySeats.setNestedScrollingEnabled(false);
        mRcySeats.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(R.dimen.W_DIMEN_6PX)));
        mRcySeats.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void initData() {
        mPersons = new ArrayList<>();
    }

    @Override
    protected void lazyLoad() {

    }

    public static BindingCardFragment newInstance() {
        return new BindingCardFragment();
    }

    @Override
    public ArrayList<SeatPerson> getData() {
        return mPersons;
    }

    @Override
    public int getColumu() {
        return oldColumu;
    }

    @Override
    public void setData(ArrayList<SeatPerson> seatPersons,int columu) {
        if(seatPersons!=null){
            mBindingCardPresenter.initData(seatPersons,columu);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void reFreshData(List<SeatPerson> list, final int columu) {
        mPersons.clear();
        mPersons.addAll(list);
        // judge for first execute
        mRcySeats.post(new Runnable() {
            @Override
            public void run() {
                if (oldColumu != columu) {
                    oldColumu = columu;
                    mRcySeats.setLayoutManager(new GridLayoutManager(getContext(), columu));
                    mBindingCardAdapter = new BindingCardAdapter(getContext(), mPersons, screenRcyHeigh,
                            screenRcyWidth, columu, BindingCardFragment.this);
                    mRcySeats.setAdapter(mBindingCardAdapter);
                } else {
                    mBindingCardAdapter.reFresh(mPersons, screenRcyHeigh, screenRcyWidth, columu);
                }
            }
        });
    }

    @Override
    public void ToastMsg(final int type, final String msg) {
        mRcySeats.post(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case 1:
                        Toast.makeText(getContext(), "答题宝绑定卡号已达上限", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getContext(), "绑定成功！卡号为：" + msg, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getContext(), "该卡号已被绑定过！", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void doubleClick(SeatPerson seatPerson) {
        if(!seatPerson.emptySeat)
        mBindingCardPresenter.requestBind(seatPerson);
    }

    @Override
    public void onGlobalLayout() {
        mRcySeats.postDelayed(new Runnable() {
            @Override
            public void run() {
                String msg1 = "iv1' width:" + mRcySeats.getWidth() + "" +
                        " height:" + mRcySeats.getHeight() + "  measuredWidth:" + mRcySeats.getMeasuredWidth()
                        + "measuredHeight:" + mRcySeats.getMeasuredHeight();
                if(screenRcyHeigh==0||screenRcyWidth==0) {
                    screenRcyHeigh = mRcySeats.getHeight() - 130;
                    screenRcyWidth = mRcySeats.getWidth() - 50;
                    if(mBindingCardAdapter!=null&&mPersons!=null) {
                        mBindingCardAdapter.reFresh(mPersons, screenRcyHeigh, screenRcyWidth, oldColumu);
                    }
                }
            }
        }, 300);
    }

}
