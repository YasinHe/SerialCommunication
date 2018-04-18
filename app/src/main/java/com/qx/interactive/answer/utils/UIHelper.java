package com.qx.interactive.answer.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.Window;

/**
 */
public class UIHelper {

    private static ProgressDialog progressDialog;

    public static void hideTitle(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public static void showProgressDialog(Context context, String title,
                                          String msg) {
        showProgressDialog(context, title, msg, true, null);
    }

    public static void showProgressDialog(Context context, String msg) {
        showProgressDialog(context, null, msg, true, null);
    }

    public static void showProgressDialog(Context context, String msg,
                                          boolean cancelable) {
        showProgressDialog(context, null, msg, cancelable, null);
    }

    public static void showProgressDialog(Context context, String title, String msg,
                                          boolean cancelable, OnCancelListener onCancle) {
        if (progressDialog != null && progressDialog.isShowing() == true) {
            return;
        }
        if (context != null) {
            progressDialog = ProgressDialog.show(context, title, msg, true,
                    cancelable);
            progressDialog.setCanceledOnTouchOutside(false);
            if (onCancle != null)
                progressDialog.setOnCancelListener(onCancle);
        }
    }

    public static void showProgressDialog(Context context, String msg,
                                          OnCancelListener onCancle) {
        showProgressDialog(context, null, msg, true, onCancle);
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
