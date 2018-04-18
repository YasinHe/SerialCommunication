package com.qx.interactive.answer.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
 * Created by HeYingXin on 2017/2/21.
 */
public class BindingCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Bind(R.id.tv_name)
    TextView mTvName;
    private Context mContext;
    private List<SeatPerson> mList = new ArrayList<>();
    private int mHeight, mWidth, mColumu, mRow;
    private IsettingSeatCallBack mBackClick;

    public BindingCardAdapter(Context context, List<SeatPerson> list, int height, int width, int columu, IsettingSeatCallBack click) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_binding_seat, parent, false);
        VHItem ch = new VHItem(view);
        return ch;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SeatPerson seatPerson = mList.get(position);
        VHItem vhItem = (VHItem) holder;
        //未绑定
        if(TextUtils.isEmpty(seatPerson.cardId)&&!seatPerson.toBeBinding){
            vhItem.mRalBinding.setVisibility(View.GONE);
            vhItem.mTvName.setVisibility(View.VISIBLE);
            vhItem.mTvName.setHeight(mHeight);
            vhItem.mTvName.setWidth(mWidth);
            if (seatPerson.emptySeat) {
                vhItem.mTvName.setBackgroundResource(R.drawable.bg_empty_seat);
                vhItem.mTvName.setText("空");
                vhItem.mTvName.setTextColor(Color.parseColor("#b1b0b0"));
            } else {
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
        }else if(seatPerson.toBeBinding){//正在绑定
            vhItem.mRalBinding.setVisibility(View.GONE);
            vhItem.mTvName.setVisibility(View.VISIBLE);
            vhItem.mTvName.setHeight(mHeight);
            vhItem.mTvName.setWidth(mWidth);
            vhItem.mTvName.setBackgroundResource(R.drawable.bg_binding);
            vhItem.mTvName.setText("请按答题键绑定");
            vhItem.mTvName.setTextColor(Color.parseColor("#0df0e8"));
        } else {//已绑定
            vhItem.mRalBinding.setVisibility(View.VISIBLE);
            vhItem.mTvName.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vhItem.mRalBinding.getLayoutParams();
            params.height = mHeight;
            params.width = mWidth;
            vhItem.mRalBinding.setLayoutParams(params);
            vhItem.mTvCard.setText("卡号："+seatPerson.cardId);
            vhItem.mTvPosition.setText(Integer.parseInt(seatPerson.seatId)+1+"");
            vhItem.mRalBinding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBackClick.doubleClick(seatPerson);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @OnClick({R.id.tv_name,R.id.ral_binding})
    public void onClick() {
    }

    class VHItem extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_position)
        TextView mTvPosition;
        @Bind(R.id.tv_card)
        TextView mTvCard;
        @Bind(R.id.ral_binding)
        RelativeLayout mRalBinding;

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

