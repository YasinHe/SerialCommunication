package com.qx.interactive.answer.interfaces;

/**
 * Created by HeYingXin on 2017/2/21.
 */
public interface IotgFunction {
    //停止廣播
    void stopBd();
    //清空ID列表(这个不能乱发)
    void cleanIdList();
    //开始广播
    void startBd();
    //结束答题
    void finishAnswer();
    //握手
    void handShake(int currentCardCount,long cardId);
    //开始答题
    void startAnswer();

    //检测连接可用性
    void checkConncet();
}
