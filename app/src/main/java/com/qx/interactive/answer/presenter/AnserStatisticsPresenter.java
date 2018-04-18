package com.qx.interactive.answer.presenter;

import android.app.DatePickerDialog;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.qx.interactive.answer.app.BaseApp;
import com.qx.interactive.answer.model.Subject;
import com.qx.interactive.answer.ui.controlView.StatisticsView;
import com.qx.interactive.answer.utils.ChartUtils;
import com.qx.interactive.answer.utils.LogUtils;
import com.qx.interactive.answer.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by HeYingXin on 2017/2/22.
 */
public class AnserStatisticsPresenter extends BasePresenter<StatisticsView>{

    final Calendar c = Calendar.getInstance();
    DatePickerDialog dialog;
    private long startTime,endTime,firstRecordTime;
    private String firstRecordDate;

    @Override
    public void attachView(StatisticsView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void initData(LineChart chart){
        ChartUtils.initChart(chart);
        //进来默认显示最近7天
        ChartUtils.notifyDataSetChanged2(chart, getDataSeven(), ChartUtils.weekValue);
        firstRecordTime = BaseApp.getInstance().mDB.queryFirstRecord()==null?0:BaseApp.getInstance().mDB.queryFirstRecord().getTime();
        firstRecordDate = TimeUtils.dateToString(firstRecordTime,"yyyy.MM.dd");
    }

    public void chooseSystemTime(final boolean isStartTime,final LineChart chart){
        dialog = new DatePickerDialog(getMvpView().getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                LogUtils.d("tag", DateFormat.format("yyy-MM-dd", c) + "");
                if(isStartTime){
                    startTime = TimeUtils.dataToLong(year+"/"+(monthOfYear+1) +"/"+dayOfMonth);
                    if(startTime<firstRecordTime){
                        Toast.makeText(getMvpView().getContext(),"当前最早记录日期为："+firstRecordDate,Toast.LENGTH_SHORT).show();
                        startTime = 0;
                    }else if(startTime>System.currentTimeMillis()) {
                        Toast.makeText(getMvpView().getContext(),"时间不能超过当前日期："+firstRecordDate,Toast.LENGTH_SHORT).show();
                        startTime = 0;
                    }else{
                        getMvpView().changeStartTime(year, (monthOfYear+1), dayOfMonth);
                    }
                }else{
                    endTime = TimeUtils.dataToLong(year+"/"+(monthOfYear+1) +"/"+dayOfMonth);
                    if(endTime>System.currentTimeMillis()){
                        Toast.makeText(getMvpView().getContext(),"时间不能超过当前日期",Toast.LENGTH_SHORT).show();
                        endTime = 0;
                    }else if(endTime<firstRecordTime) {
                        Toast.makeText(getMvpView().getContext(),"当前最早记录日期为："+firstRecordDate,Toast.LENGTH_SHORT).show();
                        endTime = 0;
                    }else{
                        getMvpView().changeEndTime(year, (monthOfYear+1), dayOfMonth);
                    }
                }
                if(startTime!=0&&endTime!=0){
                    if(startTime>endTime){
                        Toast.makeText(getMvpView().getContext(),"开始时间不能晚于结束时间",Toast.LENGTH_SHORT).show();
                    }else {
                        getCustomRecrod(chart, startTime, endTime);
                    }
                }
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    /**
     * 1表示7天  2表示30天
     * @param type
     */
    public void quickStat(int type,LineChart chart){
        if(type==1){
            ChartUtils.notifyDataSetChanged2(chart, getDataSeven(), ChartUtils.weekValue);
        }else{
            ChartUtils.notifyDataSetChanged2(chart, getDataMouth(), ChartUtils.monthValue);
        }
    }

    private List<Entry>[] getDataSeven() {
        List<Subject> list7 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(7)),
                TimeUtils.dataToLong(TimeUtils.getStatetime(6)));
        List<Subject> list6 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(6)),
                TimeUtils.dataToLong(TimeUtils.getStatetime(5)));
        List<Subject> list5 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(5)),
                TimeUtils.dataToLong(TimeUtils.getStatetime(4)));
        List<Subject> list4 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(4)),
                TimeUtils.dataToLong(TimeUtils.getStatetime(3)));
        List<Subject> list3 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(3)),
                TimeUtils.dataToLong(TimeUtils.getStatetime(2)));
        List<Subject> list2 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(2)),
                TimeUtils.dataToLong(TimeUtils.getStatetime(1)));
        List<Subject> list1 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(1)),
                System.currentTimeMillis());
        return getResult(list7,list6,list5,list4,list3,list2,list1);
    }

    private List<Entry>[] getDataMouth() {
        List<Subject> list7 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(30)),
                TimeUtils.dataToLong(TimeUtils.getStatetime(29)));
        List<Subject> list6 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(25)),
                TimeUtils.dataToLong(TimeUtils.getStatetime(24)));
        List<Subject> list5 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(20)),
                TimeUtils.dataToLong(TimeUtils.getStatetime(19)));
        List<Subject> list4 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(15)),
                TimeUtils.dataToLong(TimeUtils.getStatetime(14)));
        List<Subject> list3 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(10)),
                TimeUtils.dataToLong(TimeUtils.getStatetime(9)));
        List<Subject> list2 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(5)),
                TimeUtils.dataToLong(TimeUtils.getStatetime(4)));
        List<Subject> list1 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(1)),
                System.currentTimeMillis());
        return getResult(list7,list6,list5,list4,list3,list2,list1);
    }

    private void getCustomRecrod(final LineChart chart,long start,long end){
        Calendar setCal = Calendar.getInstance();
        setCal.setTimeInMillis(start);
        Calendar setCal2 = Calendar.getInstance();
        setCal2.setTimeInMillis(end);
        int days =  TimeUtils.betweenDays(setCal,setCal2);
        int split = days/7;

        Calendar cal = Calendar.getInstance();
        int days2 =  TimeUtils.betweenDays(setCal,cal);

        String[] customValues = new String[7];
        for(int i=0;i<7;i++){
            customValues[i] = TimeUtils.getStatetime2(days2-(split*i));
        }
        List<Subject> list7 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(0))),
                TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(0)-1)));
        List<Subject> list6 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(split))),
                TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(split)-1)));
        List<Subject> list5 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(split*2))),
                TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(split*2)-1)));
        List<Subject> list4 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(split*3))),
                TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(split*3)-1)));
        List<Subject> list3 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(split*4))),
                TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(split*4)-1)));
        List<Subject> list2 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(split*5))),
                TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(split*5)-1)));
        List<Subject> list1 = BaseApp.getInstance().mDB.queryRangeSubject(TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(split*6))),
                TimeUtils.dataToLong(TimeUtils.getStatetime(days2-(split*6)-1)));
        ChartUtils.notifyDataSetChanged3(chart, getResult(list7,list6,list5,list4,list3,list2,list1),
                customValues);
    }

    private List<Entry>[] getResult(List<Subject> list7, List<Subject> list6, List<Subject> list5, List<Subject> list4
                            , List<Subject> list3, List<Subject> list2, List<Subject> list1){

        int accuracy7 = 0,accuracy6 = 0,accuracy5 = 0,accuracy4 = 0,accuracy3 = 0,accuracy2 = 0,accuracy1 = 0;
        int takePart7 = 0,takePart6 = 0,takePart5 = 0,takePart4 = 0,takePart3 = 0,takePart2 = 0,takePart1 = 0;

        for(Subject subject:list7){
            String s = subject.getAccuraty();
            String s2 = subject.getPartRale();
            accuracy7 = accuracy7+Integer.parseInt(s.substring(0,s.indexOf('%')));
            takePart7 = takePart7+Integer.parseInt(s2.substring(0,s2.indexOf('%')));
        }
        if(list7!=null&&list7.size()>0){
            accuracy7 = accuracy7/list7.size();
            takePart7 = takePart7/list7.size();
        }

        for(Subject subject:list6){
            String s = subject.getAccuraty();
            String s2 = subject.getPartRale();
            accuracy6 = accuracy6+Integer.parseInt(s.substring(0,s.indexOf('%')));
            takePart6 = takePart6+Integer.parseInt(s2.substring(0,s2.indexOf('%')));
        }
        if(list6!=null&&list6.size()>0){
            accuracy6 = accuracy6/list6.size();
            takePart6 = takePart6/list6.size();
        }

        for(Subject subject:list5){
            String s = subject.getAccuraty();
            String s2 = subject.getPartRale();
            accuracy5 = accuracy5+Integer.parseInt(s.substring(0,s.indexOf('%')));
            takePart5 = takePart5+Integer.parseInt(s2.substring(0,s2.indexOf('%')));
        }
        if(list5!=null&&list5.size()>0){
            accuracy5 = accuracy5/list5.size();
            takePart5 = takePart5/list5.size();
        }
        for(Subject subject:list4){
            String s = subject.getAccuraty();
            String s2 = subject.getPartRale();
            accuracy4 = accuracy4+Integer.parseInt(s.substring(0,s.indexOf('%')));
            takePart4 = takePart4+Integer.parseInt(s2.substring(0,s2.indexOf('%')));
        }
        if(list4!=null&&list4.size()>0){
            accuracy4 = accuracy4/list4.size();
            takePart4 = takePart4/list4.size();
        }
        for(Subject subject:list3){
            String s = subject.getAccuraty();
            String s2 = subject.getPartRale();
            accuracy3 = accuracy3+Integer.parseInt(s.substring(0,s.indexOf('%')));
            takePart3 = takePart3+Integer.parseInt(s2.substring(0,s2.indexOf('%')));
        }
        if(list3!=null&&list3.size()>0){
            accuracy3 = accuracy3/list3.size();
            takePart3 = takePart3/list3.size();
        }
        for(Subject subject:list2){
            String s = subject.getAccuraty();
            String s2 = subject.getPartRale();
            accuracy2 = accuracy2+Integer.parseInt(s.substring(0,s.indexOf('%')));
            takePart2 = takePart2+Integer.parseInt(s2.substring(0,s2.indexOf('%')));
        }
        if(list2!=null&&list2.size()>0){
            accuracy2 = accuracy2/list3.size();
            takePart2 = takePart2/list3.size();
        }
        for(Subject subject:list1){
            String s = subject.getAccuraty();
            String s2 = subject.getPartRale();
            accuracy1 = accuracy1+Integer.parseInt(s.substring(0,s.indexOf('%')));
            takePart1 = takePart1+Integer.parseInt(s2.substring(0,s2.indexOf('%')));
        }
        if(list1!=null&&list1.size()>0){
            accuracy1 = accuracy1/list1.size();
            takePart1 = takePart1/list1.size();
        }
        //第一根线是正确率
        List<Entry>[] result = new List[2];
        List<Entry> values = new ArrayList<>();
        values.add(new Entry(0, accuracy7));
        values.add(new Entry(1, accuracy6));
        values.add(new Entry(2, accuracy5));
        values.add(new Entry(3, accuracy4));
        values.add(new Entry(4, accuracy3));
        values.add(new Entry(5, accuracy2));
        values.add(new Entry(6, accuracy1));
        result[0] = values;
        //第一根线是参与率
        List<Entry> values2 = new ArrayList<>();
        values2.add(new Entry(0, takePart7));
        values2.add(new Entry(1, takePart6));
        values2.add(new Entry(2, takePart5));
        values2.add(new Entry(3, takePart4));
        values2.add(new Entry(4, takePart3));
        values2.add(new Entry(5, takePart2));
        values2.add(new Entry(6, takePart1));
        result[1] = values2;

        return result;
    }

}
