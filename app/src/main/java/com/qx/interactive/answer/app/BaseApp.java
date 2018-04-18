package com.qx.interactive.answer.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.qx.interactive.answer.db.DBImpl;
import com.qx.interactive.answer.interfaces.IotgFunction;
import com.qx.interactive.answer.utils.LogUtils;
import com.qx.interactive.answer.utils.SeatFileUtils;
import com.qx.interactive.answer.utils.SystemUtils;

/**
 * Created by HeYingXin on 2017/2/16.
 */
public class BaseApp extends Application {

    private static BaseApp mBaseApp;
    public IotgFunction binder;
    private Intent in;
    //数据库
    public DBImpl mDB;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApp = this;
        String processName = SystemUtils.getProcessName(this, android.os.Process.myPid());
        if(processName != null){
            boolean defaultProcess = processName.equals("com.qx.interactive.answer");
            if(defaultProcess){
                initCloudChannel(this);
                //配置数据库
                mDB = new DBImpl(this);
                //配置基本文件
                SeatFileUtils.createAppDir();
                bindServer();
            }
            if(processName.equals("com.qx.interactive.answer:channel")){
                //初始化阿里的幽灵服务
                initCloudChannel(this);
            }
        }
    }

    private void bindServer(){
        in = new Intent();
        in.setAction("com.qx.interactive.answer.service.OtgService");
        in.setPackage(getPackageName());
        startService(in);
        bindService(in, conn, Context.BIND_AUTO_CREATE);
    }

    public void unBindService(){
        try {
            if (binder != null) {
                unbindService(conn);
                stopService(in);
            }
            bindServer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ServiceConnection conn = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            Log.e("TAG", "服务停止" + name);
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (IotgFunction) service;
        }
    };

    public static BaseApp getInstance(){
        return mBaseApp;
    }

    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtils.e("TAG", "成功------->"+response);
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                LogUtils.e("TAG", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }
}
