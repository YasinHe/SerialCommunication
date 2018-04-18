package com.qx.interactive.answer.db;

import com.qx.interactive.answer.model.Subject;

import java.util.List;

/**
 * Created by HeYingXin on 2017/2/24.
 * 数据库操作定义接口
 */
public interface DbApption {
    /**
     * 插入一条题目
     * @param subject
     */
    void insertSubject(Subject subject);

    /**
     * 查询时间范围内的所有题目
     */
    List<Subject> queryRangeSubject(long startTime,long endTime);

    Subject queryFirstRecord();

}
