package com.qx.interactive.answer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HeYingXin on 2017/2/20.
 */
public class SeatPerson implements Parcelable{
    public String seatId;//座位号   行数加列数组成
    public String cardId;//卡号
    public String chooseResult;
    public boolean emptySeat;
    public boolean toBeBinding;
    public int mColumu,mRow;

    @Override
    public String toString() {
        return "SeatPerson{" +
                "seatId='" + seatId + '\'' +
                ", cardId='" + cardId + '\'' +
                ", chooseResult='" + chooseResult + '\'' +
                ", emptySeat=" + emptySeat +
                ", toBeBinding=" + toBeBinding +
                ", mColumu=" + mColumu +
                ", mRow=" + mRow +
                '}';
    }


    public SeatPerson() {
    }

    public SeatPerson(String seatId,String cardId,String chooseResult,boolean empty,int columu,int row,boolean beBinding) {
        this.seatId = seatId;
        this.cardId = cardId;
        this.chooseResult = chooseResult;
        this.emptySeat = empty;
        this.mColumu = columu;
        this.mRow = row;
        this.toBeBinding = beBinding;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.seatId);
        dest.writeString(this.cardId);
        dest.writeString(this.chooseResult);
        dest.writeByte(this.emptySeat ? (byte) 1 : (byte) 0);
        dest.writeByte(this.toBeBinding ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mColumu);
        dest.writeInt(this.mRow);
    }

    protected SeatPerson(Parcel in) {
        this.seatId = in.readString();
        this.cardId = in.readString();
        this.chooseResult = in.readString();
        this.emptySeat = in.readByte() != 0;
        this.toBeBinding = in.readByte() != 0;
        this.mColumu = in.readInt();
        this.mRow = in.readInt();
    }

    public static final Creator<SeatPerson> CREATOR = new Creator<SeatPerson>() {
        @Override
        public SeatPerson createFromParcel(Parcel source) {
            return new SeatPerson(source);
        }

        @Override
        public SeatPerson[] newArray(int size) {
            return new SeatPerson[size];
        }
    };
}
