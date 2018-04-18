package com.qx.interactive.answer.utils;

import android.support.annotation.Nullable;

import java.text.DecimalFormat;

/**
 * Created by HeYingXin on 2017/2/13.
 */
public class OtgUtils {
    final static String HEX = "0123456789abcdef";

    public static String bytes2hex03(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes)
        {
            sb.append(HEX.charAt((b >> 4) & 0x0f));
            sb.append(HEX.charAt(b & 0x0f));
        }
        return sb.toString();
    }

    //把卡号转为16进制数组
    public static byte[] cardIdToHex(long cardId){
        return hexStringToBytes(Long.toHexString(cardId));
    }

    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    //合并两个byte数组
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static byte[] unsignedShortToByte1(int s) {
        byte[] targets = new byte[1];
        targets[0] = (byte) (s & 0xFF);
        return targets;
    }

    public static byte[] unsignedShortToByte2(int s) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (s >> 8 & 0xFF);
        targets[1] = (byte) (s & 0xFF);
        return targets;
    }

    //判断学生选择答案结果
    @Nullable
    public static String studentOneChooseResult(String hexString){
        String s = Integer.toBinaryString(Integer.valueOf(hexString,16));
        int result = Integer.parseInt(s);
        LogUtils.e("TAG","转二进制答案结果:---"+result);
        StringBuilder back = new StringBuilder();
        int length = s.length();
        int tempLength = length;
        for(int i=0;i<length;i++){
            if(s.charAt(i)=='1'){
                if(tempLength==6){
                    back.append("F");
                }
                if(tempLength==5){
                    back.append("E");
                }
                if(tempLength==4){
                    back.append("D");
                }
                if(tempLength==3){
                    back.append("C");
                }
                if(tempLength==2){
                    back.append("B");
                }
                if(tempLength==1){
                    back.append("A");
                }
            }
            --tempLength;
        }
        if(back.toString().length()>0) {
            return sort(back.toString());
        }else{
            return "未答";
        }
    }

    public static String sort(String back){
        String resultBack = "";
        if(back.toString().contains("A")){
            resultBack = "A";
        }
        if(back.toString().contains("B")){
            resultBack = resultBack+"B";
        }
        if(back.toString().contains("C")){
            resultBack = resultBack+"C";
        }
        if(back.toString().contains("D")){
            resultBack = resultBack+"D";
        }
        if(back.toString().contains("E")){
            resultBack = resultBack+"E";
        }
        if(back.toString().contains("F")){
            resultBack = resultBack+"F";
        }
        return resultBack.toString();
    }

    //判断学生对错选择答案结果
    @Nullable
    public static String studentRightOrWrongChooseResult(String s){
        switch (s){
            case "91":
                return "正确";
            case "92":
                return "错误";
        }
        return "未答";
    }

    //百分比工具
    public static String myPercent(int y, int z) {
        String baifenbi = "";// 接受百分比的值
        double baiy = y * 1.0;
        double baiz = z * 1.0;
        double fen = baiy / baiz;
        DecimalFormat df1 = new DecimalFormat("##%");
        baifenbi = df1.format(fen);
        return baifenbi;
    }

}
