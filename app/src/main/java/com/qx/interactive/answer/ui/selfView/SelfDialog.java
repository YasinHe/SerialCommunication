package com.qx.interactive.answer.ui.selfView;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.qx.interactive.answer.R;

/**
 * Created by HeYingXin on 2017/2/17.
 */
public abstract class SelfDialog {
    public android.support.v7.app.AlertDialog.Builder builder;

    protected android.support.v7.app.AlertDialog.Builder Dialog(Context context){
        builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.MyDialog);
        return builder;
    }

    /**
     *
     * @param context 必须是Activity的
     * @param id
     * @return
     */
    protected abstract View initView(@NonNull Context context,@LayoutRes int id);

    protected abstract android.app.Dialog initDialog(android.support.v7.app.AlertDialog.Builder builder);

    protected abstract Window initWindow();
}
