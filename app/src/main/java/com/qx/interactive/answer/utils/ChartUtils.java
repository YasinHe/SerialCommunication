package com.qx.interactive.answer.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * 图表工具
 * Created by yangle on 2016/11/29.
 * MainTain by HeYingXin on 2017/2/22.
 *
 */
public class ChartUtils {

    public static int weekValue = 1;
    public static int monthValue = 2;

    /**
     * 初始化图表
     *
     * @param chart 原始图表
     * @return 初始化后的图表
     */
    public static LineChart initChart(LineChart chart) {
        // 不显示数据描述
        chart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("暂无数据");
        // 不显示表格颜色
        chart.setDrawGridBackground(false);
        // 不可以缩放
        chart.setScaleEnabled(false);
        // 不显示y轴右边的值
        chart.getAxisRight().setEnabled(false);
        // 不显示图例
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        // 向左偏移15dp，抵消y轴向右偏移的30dp
        chart.setExtraLeftOffset(-15);
        XAxis xAxis = chart.getXAxis();
        // 不显示x轴
        xAxis.setDrawAxisLine(false);
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.parseColor("#50697a"));
        xAxis.setTextSize(12);
        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
        // 设置x轴数据偏移量
        xAxis.setYOffset(-12);

        YAxis yAxis = chart.getAxisLeft();
        // 不显示y轴
        yAxis.setDrawAxisLine(false);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 不从y轴发出横向直线
        yAxis.setDrawGridLines(false);
        yAxis.setTextColor(Color.parseColor("#50697a"));
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
        yAxis.setXOffset(30);
        yAxis.setYOffset(-3);
        yAxis.setAxisMinimum(0);
        chart.invalidate();
        return chart;
    }

    /**
     * 设置图表数据
     *
     * @param chart  图表
     * @param values 数据
     */
    public static void setChartData(LineChart chart, List<Entry> values) {
        LineDataSet [] lineDataSet1 = new LineDataSet[2];
        LineDataSet lineDataSet;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            lineDataSet = new LineDataSet(values, "");
            // 设置曲线颜色
            lineDataSet.setColor(Color.parseColor("#6cd3ef"));
            // 设置平滑曲线
            lineDataSet.setMode(LineDataSet.Mode.LINEAR);
            // 不显示坐标点的小圆点
            lineDataSet.setDrawCircles(false);
            // 不显示坐标点的数据
            lineDataSet.setDrawValues(false);
            // 不显示定位线
            lineDataSet.setHighlightEnabled(false);

            LineData data = new LineData(lineDataSet);
            chart.setData(data);
            chart.invalidate();
        }
    }

    static int[] mChartColors = {Color.parseColor("#6cd3ef"),Color.parseColor("#ff4c79")};
    public static void setChartData2(LineChart chart, List<Entry>[] mEntries) {
        LineDataSet [] lineDataSet = new LineDataSet[2];

        if (chart.getData() != null && chart.getData().getDataSetCount()==2) {
            for(int index = 0, len = mEntries.length; index < len; index ++) {
                List<Entry> list = mEntries[index];
                lineDataSet[index] = (LineDataSet) chart.getData().getDataSetByIndex(index);
                lineDataSet[index].setValues(list);
            }
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            for (int index = 0, len = mEntries.length; index < len; index ++) {
                lineDataSet[index] = new LineDataSet(mEntries[index], "");
                lineDataSet[index].setColor(mChartColors[index]);
                lineDataSet[index].setMode(LineDataSet.Mode.LINEAR);
                lineDataSet[index].setDrawCircles(false);
                lineDataSet[index].setDrawValues(false);
                lineDataSet[index].setHighlightEnabled(false);

            }
            LineData data = new LineData(lineDataSet);
            chart.setData(data);
            chart.invalidate();
        }
    }

    /**
     * 更新图表
     *
     * @param chart     图表
     * @param values    数据
     * @param valueType 数据类型
     */
    public static void notifyDataSetChanged(LineChart chart, List<Entry> values,
                                            final int valueType) {
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValuesProcess(valueType)[(int) value];
            }
        });
        chart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value+"%";
            }
        });
        chart.invalidate();
        setChartData(chart, values);
    }

    public static void notifyDataSetChanged2(LineChart chart, List<Entry>[] values,
                                            final int valueType) {
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValuesProcess(valueType)[(int) value];
            }
        });
        chart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value+"%";
            }
        });
        chart.invalidate();
        setChartData2(chart, values);
    }

    public static void notifyDataSetChanged3(LineChart chart, List<Entry>[] values,
                                             final String [] date) {
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return date[(int) value];
            }
        });
        chart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value+"%";
            }
        });
        chart.invalidate();
        setChartData2(chart, values);
    }

    /**
     * x轴数据处理
     *
     * @param valueType 数据类型
     * @return x轴数据
     */
    private static String[] xValuesProcess(int valueType) {
         if (valueType == weekValue) { // 最近一个星期
            String[] weekValues = new String[7];
            for(int i=0;i<7;i++){
                weekValues[i] = TimeUtils.getStatetime2(7-(i+1));
            }
            return weekValues;

        } else if (valueType == monthValue) { // 最近三十天
            String[] monthValues = new String[7];
             for(int i=0;i<7;i++){
                 monthValues[i] = TimeUtils.getStatetime2(30-i*5);
             }
            return monthValues;
        }
        return new String[]{};
    }
}
