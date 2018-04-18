package com.qx.interactive.answer.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qx.interactive.answer.R;
import com.qx.interactive.answer.interfaces.IsettingSeatCallBack;
import com.qx.interactive.answer.model.SeatPerson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HeYingXin on 2017/2/20.
 */
public class SettingSeatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    @Bind(R.id.tv_name)
    TextView mTvName;
    private Context mContext;
    private List<SeatPerson> mList = new ArrayList<>();
    private int mHeight, mWidth, mColumu, mRow;
    private IsettingSeatCallBack mBackClick;

    public SettingSeatAdapter(Context context, List<SeatPerson> list, int height, int width, int columu, IsettingSeatCallBack click) {
        mContext = context;
        mList = list;
        mColumu = columu;
        mRow = mList.size() / mColumu;
        mHeight = height / mRow;
        mWidth = width / mColumu;
        mBackClick = click;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_setting_seat, parent, false);
            VHItem ch = new VHItem(view);
            return ch;
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SeatPerson seatPerson = mList.get(position);
        VHItem vhItem = (VHItem) holder;
        vhItem.mTvName.setHeight(mHeight);
        vhItem.mTvName.setWidth(mWidth);
        if(seatPerson.emptySeat){
            vhItem.mTvName.setBackgroundResource(R.drawable.bg_empty_seat);
            vhItem.mTvName.setText("空");
            vhItem.mTvName.setTextColor(Color.parseColor("#b1b0b0"));
        }else if(!TextUtils.isEmpty(seatPerson.cardId)){
            vhItem.mTvName.setBackgroundResource(R.drawable.bg_seat_icon);
            vhItem.mTvName.setText("已绑定");
            vhItem.mTvName.setTextColor(Color.parseColor("#4baeff"));
        }else{
            vhItem.mTvName.setBackgroundResource(R.drawable.bg_seat_icon);
            vhItem.mTvName.setText(Integer.parseInt(seatPerson.seatId)+1+"");
            vhItem.mTvName.setTextColor(Color.parseColor("#4baeff"));
        }
        vhItem.mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackClick.doubleClick(seatPerson);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @OnClick(R.id.tv_name)
    public void onClick() {
    }

    class VHItem extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_name)
        TextView mTvName;

        public VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void reFresh(List<SeatPerson> list, int height, int width, int columu) {
        mList = list;
        mColumu = columu;
        mRow = mList.size() / mColumu;
        mHeight = height / mRow;
        mWidth = width / mColumu;
        notifyDataSetChanged();
    }

}

