package com.qx.interactive.answer.presenter;

import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.CPUpdateDownloadCallback;
import com.qx.interactive.answer.app.BaseApp;
import com.qx.interactive.answer.ui.activity.AnswerStatisticsActivity;
import com.qx.interactive.answer.ui.activity.SeatSettingActivity;
import com.qx.interactive.answer.ui.controlView.MainView;
import com.qx.interactive.answer.ui.selfView.InputPassWordDialog;
import com.qx.interactive.answer.ui.selfView.LockView;
import com.qx.interactive.answer.utils.LogUtils;

import java.util.List;

/**
 * Created by HeYingXin on 2017/2/16.
 */
public class MainPresenter extends BasePresenter<MainView>{

    private UsbManager myUsbManager;
    private Intent mIntent;

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void attachView(MainView mvpView) {
        super.attachView(mvpView);
    }

    //弹出密码页面，输出完成后，返回验证
    public void popupPassword() {
        try {
            final InputPassWordDialog dialog = new InputPassWordDialog(getMvpView().getContext());
            dialog.create();
            dialog.setCallBack(new LockView.OnDrawFinishedListener() {
                @Override
                public void onDrawFinished(List<Integer> passPositions) {
                    LogUtils.e("已点击的密码",passPositions.toString());
                    if(passPositions.toString().equals("[1, 1, 1, 1, 1, 1]")) {
                        dialog.dismiss();
                        Intent intent = new Intent(getMvpView().getContext(), SeatSettingActivity.class);
                        getMvpView().getContext().startActivity(intent);
                    }else if(passPositions.size()>=6){
                        dialog.dismiss();
                        LogUtils.e("密码错误");
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            getMvpView().showError("InputPassWordDialog对话框报错："+e.toString(),null);
        }
    }

    //无论怎样都要进一次权限判断，先弹出权限选框再说
    public void enterSystem() {
        //先初始化答题宝
        BaseApp.getInstance().binder.checkConncet();
        BaseApp.getInstance().binder.stopBd();
        BaseApp.getInstance().binder.startBd();
        BaseApp.getInstance().binder.finishAnswer();
        BaseApp.getInstance().binder.startAnswer();
        BaseApp.getInstance().binder.cleanIdList();
    }

    public void enterAnserStatistics(){
        mIntent = new Intent(getMvpView().getContext(), AnswerStatisticsActivity.class);
        getMvpView().getContext().startActivity(mIntent);
    }

    public void clickUpdate(){
        BDAutoUpdateSDK.cpUpdateCheck(getMvpView().getActivity(), new MyCPCheckUpdateCallback());
    }

    private class MyCPCheckUpdateCallback implements CPCheckUpdateCallback {

        @Override
        public void onCheckUpdateCallback(AppUpdateInfo info, AppUpdateInfoForInstall infoForInstall) {
            if (infoForInstall != null && !TextUtils.isEmpty(infoForInstall.getInstallPath())) {
                BDAutoUpdateSDK.cpUpdateInstall(BaseApp.getInstance().getApplicationContext(), infoForInstall.getInstallPath());
            } else if (info != null) {
                BDAutoUpdateSDK.cpUpdateDownload(getMvpView().getActivity(), info, new UpdateDownloadCallback());
            } else {
                Toast.makeText(getMvpView().getActivity(),"当前已是最新版本",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateDownloadCallback implements CPUpdateDownloadCallback {

        @Override
        public void onDownloadComplete(String apkPath) {
            BDAutoUpdateSDK.cpUpdateInstall(BaseApp.getInstance().getApplicationContext(), apkPath);
        }

        @Override
        public void onStart() {
            //setText(txtLog.getText() + "\n Download onStart");
        }

        @Override
        public void onPercent(int percent, long rcvLen, long fileSize) {
            //setText(txtLog.getText() + "\n Download onPercent: " + percent + "%");
        }

        @Override
        public void onFail(Throwable error, String content) {
            //setText(txtLog.getText() + "\n Download onFail: " + content);
        }

        @Override
        public void onStop() {
            //setText(txtLog.getText() + "\n Download onStop");
        }

    }

}
