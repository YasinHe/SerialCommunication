package com.qx.interactive.answer.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by HeYingXin on 2017/2/17.
 */
public class BitmapUtils {

    public static Bitmap zoomImg(Bitmap bm, int newWidth , int newHeight){
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
}
