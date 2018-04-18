package com.qx.interactive.answer.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by HeYingXin on 2017/2/23.
 */
@Entity
public class Subject implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "type")
    public int type;//1是选择题  2是判断题
    @Property(nameInDb = "time")
    public long time;//提问时间
    @Property(nameInDb = "partRale")
    public String partRale;//参与率
    @Property(nameInDb = "accuraty")
    public String accuraty;//正确率
    @Transient
    public String correctResponse;//正确答案

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCorrectResponse() {
        return correctResponse;
    }

    public void setCorrectResponse(String correctResponse) {
        this.correctResponse = correctResponse;
    }

    public String getAccuraty() {
        return accuraty;
    }

    public void setAccuraty(String accuraty) {
        this.accuraty = accuraty;
    }

    public String getPartRale() {
        return partRale;
    }

    public void setPartRale(String partRale) {
        this.partRale = partRale;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "type=" + type +
                ", time=" + time +
                ", partRale='" + partRale + '\'' +
                ", accuraty='" + accuraty + '\'' +
                ", correctResponse='" + correctResponse + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeLong(this.time);
        dest.writeString(this.partRale);
        dest.writeString(this.accuraty);
        dest.writeString(this.correctResponse);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject() {
    }

    protected Subject(Parcel in) {
        this.type = in.readInt();
        this.time = in.readLong();
        this.partRale = in.readString();
        this.accuraty = in.readString();
        this.correctResponse = in.readString();
    }

    @Generated(hash = 2030039568)
    public Subject(Long id, int type, long time, String partRale, String accuraty) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.partRale = partRale;
        this.accuraty = accuraty;
    }

    public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel source) {
            return new Subject(source);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };
}
