package com.qx.interactive.answer.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by HeYingXin on 2017/2/22.
 */
public class SeatFileUtils {

    private static String SDPATH = Environment.getExternalStorageDirectory()
            + "/Ita/";

    //存所有文件
    private static String APP_SD_PATH_File = SDPATH + "file"
            + File.separator;

    //初始化
    public static void createAppDir(){
        File dirAPP_SD_PATH_USER = new File(SDPATH);//创建库
        File dirAPP_SD_PATH_USERFILE = new File(APP_SD_PATH_File);//创建库
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dirAPP_SD_PATH_USER.mkdir();
            dirAPP_SD_PATH_USERFILE.mkdir();
        }
    }

    // 把数据写入SD卡
    public static String writeSDcard(String str,String fileName) throws Exception{
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(APP_SD_PATH_File,fileName);
            FileOutputStream fileW = new FileOutputStream(file);
            fileW.write(str.getBytes());
            fileW.close();
            return file.getCanonicalPath();
        }else{
            throw new Exception("SD卡不存在！！");
        }
    }
    // 从SD卡中读取数据
    public static String readSDcard(String fileName) throws Exception{
        StringBuffer str = new StringBuffer();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(APP_SD_PATH_File,fileName);
            if (file.exists()) {
                FileInputStream fileR = new FileInputStream(file);
                BufferedReader reads = new BufferedReader(
                        new InputStreamReader(fileR));
                String st = null;
                while ((st =reads.readLine())!=null ) {
                    str.append(st);
                }
                fileR.close();
            }
        }else{
            throw new Exception("SD卡不存在！！");
        }
        return str.toString();
    }
}
