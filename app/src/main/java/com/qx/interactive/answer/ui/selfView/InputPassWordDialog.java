package com.qx.interactive.answer.ui.selfView;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.qx.interactive.answer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HeYingXin on 2017/2/17.
 */
public class InputPassWordDialog extends SelfDialog{
    @Bind(R.id.mlv_lock)
    LockView mMlvLock;
    private Context mContext;
    private Dialog mDialog;

    public InputPassWordDialog(Context context) {
        mContext = context;
        super.Dialog(context);
    }

    //链式调用顺序  1
    public InputPassWordDialog create() {
        View view = initView(mContext, R.layout.dialog_input_password);
        builder.setView(view);
        ButterKnife.bind(this,view);
        mDialog = initDialog(builder);
        Window window = initWindow();
        mDialog.setCanceledOnTouchOutside(true);
        WindowManager m = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = d.getHeight(); // 铺满
        p.width = d.getWidth(); // 铺满
        p.alpha = 0.8f; //设置对话框的透明度
        window.setAttributes(p);//
        return this;
    }

    //2
    public InputPassWordDialog setCallBack(LockView.OnDrawFinishedListener linstener) {
        if(linstener!=null){
            mMlvLock.setOnDrawFinishedListener(linstener);
        }
        return this;
    }

    //3
    public InputPassWordDialog show() throws Exception {
        mDialog.show();
        return this;
    }

    public InputPassWordDialog dismiss(){
        if(mDialog!=null) {
            mDialog.dismiss();
        }
        return this;
    }

    @Override
    protected View initView(@NonNull Context context, int id) {
        LayoutInflater inflate = LayoutInflater.from(context);
        return inflate.inflate(id, null);
    }

    @Override
    protected Dialog initDialog(AlertDialog.Builder builder) {
        return builder.show();
    }

    @Override
    protected Window initWindow() {
        return mDialog.getWindow();
    }

}
