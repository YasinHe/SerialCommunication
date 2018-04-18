package com.qx.interactive.answer.utils;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by HeYingXin on 2017/2/17.
 */
public class LogUtils {

    private static final boolean isDebug = true;
    private static final String TAG = "iA-TAG";

    public static void d(@NonNull String info){
        d(TAG,info);
    }

    public static void d(String TAG,@NonNull String info){
        if(isDebug){
            Log.d(TAG,info);
        }
    }

    public static void i(@NonNull String info){
        i(TAG,info);
    }

    public static void i(String TAG,@NonNull String info){
        if(isDebug){
            Log.i(TAG,info);
        }
    }

    public static void e(@NonNull String info){
        e(TAG,info);
    }

    public static void e(String TAG,@NonNull String info){
        if(isDebug){
            Log.e(TAG,info);
        }
    }
}
