package com.qx.interactive.answer.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qx.interactive.answer.R;
import com.qx.interactive.answer.interfaces.IsubjectCallBack;
import com.qx.interactive.answer.model.SeatPerson;
import com.qx.interactive.answer.utils.OtgUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

/**
 * Created by HeYingXin on 2017/2/24.
 */
public class SubjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SeatPerson> mList = new ArrayList<>();
    private int mHeight, mWidth, mColumu, mRow,answerRightCount;
    private String result;
    private IsubjectCallBack mIsubjectCallBack;

    public SubjectAdapter(Context context, List<SeatPerson> list, int height, int width, int columu,
                          IsubjectCallBack callBack,String answer) {
        result = answer;//正确答案只有一个,包括多选  多选  A和B   这里结果就是AB
        if(!TextUtils.isEmpty(answer)&&answer.length()>0){
            if(!answer.contains("正确")&&!answer.contains("错误"))
                result = OtgUtils.sort(answer);
        }
        answerRightCount = 0;
        mContext = context;
        mList = list;
        mColumu = columu;
        mRow = mList.size() / mColumu;
        mHeight = height / mRow;
        mWidth = width / mColumu;
        mIsubjectCallBack = callBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_subject_view, parent, false);
        VHItem ch = new VHItem(view);
        return ch;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SeatPerson seatPerson = mList.get(position);
        VHItem vhItem = (VHItem) holder;
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) vhItem.mRalSubject.getLayoutParams();
        params.height = mHeight;
        params.width = mWidth;
        vhItem.mRalSubject.setLayoutParams(params);

        vhItem.mTvPosition.setTextColor(Color.parseColor("#8f8f8f"));
        vhItem.mTvAnwser.setTextColor(Color.parseColor("#8f8f8f"));
        vhItem.mRalSubject.setBackgroundResource(R.drawable.bg_empty_seat);
        vhItem.mTvPosition.setText(Integer.parseInt(seatPerson.seatId)+1+"");
        vhItem.mRalSubject.setVisibility(View.VISIBLE);
        if (seatPerson.emptySeat) {
            vhItem.mRalSubject.setBackgroundResource(R.drawable.bg_null_bind);
            vhItem.mTvAnwser.setText("空位");
            vhItem.mRalSubject.setVisibility(View.GONE);
        } else if((TextUtils.isEmpty(seatPerson.cardId))){
            vhItem.mRalSubject.setBackgroundResource(R.drawable.bg_null_bind);
            vhItem.mTvAnwser.setText("未绑定");
        } else {
            if (!TextUtils.isEmpty(seatPerson.chooseResult)||seatPerson.chooseResult.equals("null")) {//已答题
                if(TextUtils.isEmpty(result)||result.equals(seatPerson.chooseResult)) {
                    vhItem.mRalSubject.setBackgroundResource(R.drawable.bg_seat_icon);
                    vhItem.mTvPosition.setTextColor(Color.parseColor("#54b1fd"));
                    vhItem.mTvAnwser.setTextColor(Color.parseColor("#4baeff"));
                    //正确人数
                    if(!TextUtils.isEmpty(result)&&result.equals(seatPerson.chooseResult)){
                        ++answerRightCount;
                    }
                    vhItem.mTvAnwser.setText(seatPerson.chooseResult);
                }else{
                    vhItem.mRalSubject.setBackgroundResource(R.drawable.bg_answer_wrong);
                    vhItem.mTvPosition.setTextColor(Color.parseColor("#fb373f"));
                    vhItem.mTvAnwser.setTextColor(Color.parseColor("#fb373f"));
                    vhItem.mTvAnwser.setText(seatPerson.chooseResult);
                }
                vhItem.mRalSubject.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mIsubjectCallBack.longClickItem(v,seatPerson);
                        return false;
                    }
                });
            }else{
                vhItem.mRalSubject.setBackgroundResource(R.drawable.bg_null_answer);
                vhItem.mTvAnwser.setTextColor(Color.parseColor("#4baeff"));
                vhItem.mTvPosition.setTextColor(Color.parseColor("#4baeff"));
                vhItem.mTvAnwser.setText("未答");
            }
            mIsubjectCallBack.backAnswerCount(answerRightCount);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @OnLongClick(R.id.ral_subject)
    public boolean onLongClick(){
        return false;
    }

    class VHItem extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_position)
        TextView mTvPosition;
        @Bind(R.id.tv_anwser)
        TextView mTvAnwser;
        @Bind(R.id.ral_subject)
        RelativeLayout mRalSubject;

        public VHItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void reFresh(List<SeatPerson> list, int height, int width, int columu,String answer) {
        result = answer;
        if(!TextUtils.isEmpty(answer)&&answer.length()>0){
            if(!answer.contains("正确")&&!answer.contains("错误"))
            result = OtgUtils.sort(answer);
        }
        answerRightCount = 0;
        mList = list;
        mColumu = columu;
        mRow = mList.size() / mColumu;
        mHeight = height / mRow;
        mWidth = width / mColumu;
        flush();
    }

    public void setRightAnswer(String answer){
        result = answer;//正确答案只有一个,包括多选  多选  A和B   这里结果就是AB
        if(!TextUtils.isEmpty(answer)&&answer.length()>0){
            if(!answer.contains("正确")&&!answer.contains("错误"))
            result = OtgUtils.sort(answer);
        }
        answerRightCount = 0;
        flush();
    }

    private synchronized void flush(){
        notifyDataSetChanged();
    }

}
