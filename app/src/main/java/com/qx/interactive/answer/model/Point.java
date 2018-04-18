package com.qx.interactive.answer.model;

/**
 * Created by HeYingXin on 2017/2/16.
 */
public class Point {
    public static final int STATE_NORMAL = 1;
    public static final int STATE_PRESS = 2;

    public float x;
    public float y;
    public int state = STATE_NORMAL;

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }
    /**
     * 计算两点间的距离
     * @param a 另外一个点
     * @return
     */
    public float getInstance(Point a){
        return (float) Math.sqrt((x-a.x)*(x-a.x) + (y-a.y)*(y-a.y));
    }
}
