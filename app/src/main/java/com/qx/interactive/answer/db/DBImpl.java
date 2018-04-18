package com.qx.interactive.answer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.qx.interactive.answer.model.Subject;
import com.qx.interactive.gen.DaoMaster;
import com.qx.interactive.gen.DaoSession;
import com.qx.interactive.gen.SubjectDao;

import java.util.List;

/**
 * Created by HeYingXin on 2017/2/24.
 * 数据库操作实例
 */
public class DBImpl implements DbApption{

    private static DaoSession daoSession;

    public DBImpl(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "interactive.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    @Override
    public void insertSubject(Subject subject) {
        daoSession.getSubjectDao().insertOrReplace(subject);
    }

    @Override
    public List<Subject> queryRangeSubject(long startTime, long endTime) {
        Log.e("TAG",startTime +"" +endTime);
        return daoSession.getSubjectDao().queryBuilder().where(SubjectDao.Properties.Time.between(startTime,endTime)).list();
    }

    @Override
    public Subject queryFirstRecord() {
        return daoSession.getSubjectDao().queryBuilder().where(SubjectDao.Properties.Id.eq(1)).unique();
    }
}
