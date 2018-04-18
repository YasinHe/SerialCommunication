package com.qx.interactive.answer.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.qx.interactive.answer.interfaces.IconnectFailureCallback;
import com.qx.interactive.answer.interfaces.IotgFunction;
import com.qx.interactive.answer.interfaces.IotgServerCallBack;
import com.qx.interactive.answer.interfaces.IwriteDateFailureCallBack;
import com.qx.interactive.answer.utils.InPutOutPutUtils;
import com.qx.interactive.answer.utils.OtgUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by HeYingXin on 2017/2/21.
 */
public class OtgService extends Service implements InPutOutPutUtils.Listener,IwriteDateFailureCallBack{
    private static final String TAG = "OtgService";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private UsbManager myUsbManager;
    private UsbDevice myUsbDevice;
    private UsbInterface myInterface;
    private UsbDeviceConnection myDeviceConnection;
    private InPutOutPutUtils mUtils;
    //线程执行
    private final ExecutorService mExecutor = Executors.newFixedThreadPool(1);
    private final int VendorID = 1035;    //这里要改成自己的硬件ID
    private final int ProductID = 2097;
    private final int BaseStationId = 0x01;//基站id
    private UsbEndpoint epOut;
    private UsbEndpoint epIn;
    //回调
    static IotgServerCallBack mIotgServerCallBack;
    private static List<IconnectFailureCallback> mCallbacks = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createConncet();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public synchronized void reWrite() {
        if(mCallbacks!=null&&mCallbacks.size()>0){
            for(IconnectFailureCallback callback:mCallbacks){
                callback.rePeat();
            }
        }
    }

    @Override
    public void success() {
        if(mCallbacks!=null&&mCallbacks.size()>0){
            for(IconnectFailureCallback callback:mCallbacks){
                callback.success();
            }
        }
    }

    class MyBinder extends Binder implements IotgFunction{

        @Override
        public void stopBd() {
            byte[] step1 = new byte[]{BaseStationId,0x00,0x00,0x00, (byte) 0xf3,0x00,0x00,0x00,0x00};
            if(mUtils!=null)
            mUtils.writeAsync(step1);
        }

        @Override
        public void cleanIdList() {
            byte[] step2 = new byte[]{BaseStationId,0x00,0x00,0x00, (byte) 0xf5,0x00,0x01,0x00,0x00};
            if(mUtils!=null)
            mUtils.writeAsync(step2);
        }

        @Override
        public void startBd() {
            byte[] step3 = new byte[]{BaseStationId,0x00,0x00,0x00, (byte) 0xf3,0x01,0x00,0x00,0x00};
            if(mUtils!=null)
            mUtils.writeAsync(step3);
        }

        @Override
        public void finishAnswer() {
            byte[] step4 = new byte[]{BaseStationId,0x00,0x01,0x00, (byte) 0xf2,0x00,0x00,0x00,0x00};
            if(mUtils!=null)
            mUtils.writeAsync(step4);
        }

        @Override
        public void handShake(int currentCardCount,long cardId) {
            byte byteCardCount[] = OtgUtils.unsignedShortToByte1(currentCardCount);
            byte[] step5 = new byte[]{(byte) 0xf4};
            byte[] center = OtgUtils.byteMerger(step5,byteCardCount);
            byte[] last = new byte[]{0x00,0x00,0x00};
            byte[] result = OtgUtils.byteMerger(center,last);
            byte[] byteCardId = OtgUtils.cardIdToHex(cardId);// 998350795
            if(mUtils!=null)
            mUtils.writeAsync(OtgUtils.byteMerger(byteCardId,result));
        }

        @Override
        public void startAnswer() {
            byte[] step6 = new byte[]{BaseStationId,0x01,0x00,0x00, (byte) 0xf2,0x00,0x00,0x00,0x00};
            if(mUtils!=null)
            mUtils.writeAsync(step6);
        }

        @Override
        public void checkConncet() {
            if(myUsbDevice==null||mUtils==null||(mUtils!=null&&mUtils.isBad())){
                createConncet();
            }
        }
    }

    @Override
    public void onNewData(byte[] data) {
        String result = OtgUtils.bytes2hex03(data);
        if(mIotgServerCallBack!=null){
            mIotgServerCallBack.callBack(result);
        }
//        Log.e(TAG,"接受消息："+result);
//        if(result.substring(8,10).equals("a6")) {
//            LogUtils.e(TAG, "消息发送人为:" + result.substring(0, 8) + "他的物理卡号为:"
//                    + new BigInteger(result.substring(0, 8), 16).toString());
//            LogUtils.e(TAG, "消息的识别为：" + result.substring(8, 10));
//            LogUtils.e(TAG, "学生单选题结果为：" + result.substring(10, 12) + "------->"
//                    + OtgUtils.studentOneChooseResult(result.substring(10, 12)));
//            LogUtils.e(TAG, "学生对错勾选题结果为：" + result.substring(12, 14) + "------->"
//                    + OtgUtils.studentRightOrWrongChooseResult(result.substring(12, 14)));
//            LogUtils.e(TAG, "消息的答题号为：" + result.substring(14, 16));
//            LogUtils.e(TAG, "消息的答题结果标示为：" + result.substring(16, 18) + "-----答题结果是否有效--->"
//                    + ((Integer.parseInt(result.substring(16, 18),10) & 0x80) == 0));
//        }else{
//            LogUtils.e(TAG, "有人按下了答题键:" + result.substring(0, 8) + "他的物理卡号为:"
//                    + new BigInteger(result.substring(0, 8), 16).toString());
//        }
    }

    @Override
    public void onRunError(Exception e) {
        Log.e(TAG,"消息队列报错"+e);
    }

    public static void registCallBack(IotgServerCallBack callBack){
        mIotgServerCallBack = callBack;
    }

    /**
     * 連接建立
     */
    private void createConncet(){
        myUsbManager = (UsbManager) getSystemService(USB_SERVICE);
        tryGetUsbPermission();
    }

    //请求权限
    private void tryGetUsbPermission() {
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbPermissionActionReceiver, filter);
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        if(myUsbManager.getDeviceList().size()<=0){
            if(mCallbacks!=null&&mCallbacks.size()>0){
                for(IconnectFailureCallback callback:mCallbacks){
                    callback.nullDevice();
                }
            }
            return;
        }
        for (final UsbDevice usbDevice : myUsbManager.getDeviceList().values()) {
            if (myUsbManager.hasPermission(usbDevice)) {
                afterGetUsbPermission(usbDevice,false);
            } else {
                myUsbManager.requestPermission(usbDevice, mPermissionIntent);
            }
        }
    }

    private void afterGetUsbPermission(UsbDevice usbDevice,boolean isReConnect) {
        doYourOpenUsbDevice(usbDevice,isReConnect);
    }

    private void doYourOpenUsbDevice(UsbDevice usbDevice,boolean isReConnect) {
        UsbDeviceConnection connection = myUsbManager.openDevice(usbDevice);
        //如果说权限框弹出来允许了，那就重新再来一次建立稳定连接
        if(isReConnect){
            reWrite();
        }else{
            //权限获取成功
            enumerateDevice();
            findInterface();
            openDevice();
            assignEndpoint();
            openFunction();
        }
    }

    private final BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (null != usbDevice) {
                            afterGetUsbPermission(usbDevice, true);
                        }
                    } else {
                        Toast.makeText(context, "您拒绝开放权限，这样会导致功能无法使用！", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    //枚举设备
    private void enumerateDevice() {
        if (myUsbManager == null)
            return;
        HashMap<String, UsbDevice> deviceList = myUsbManager.getDeviceList();
        if (!deviceList.isEmpty()) {
            for (UsbDevice device : deviceList.values()) {
                myUsbDevice = device;
            }
        }
    }

    //找到接口
    private void findInterface() {
        if (myUsbDevice != null) {
            for (int i = 0; i < myUsbDevice.getInterfaceCount(); i++) {
                UsbInterface intf = myUsbDevice.getInterface(i);
                if (intf.getInterfaceClass() == 3
                        && intf.getInterfaceSubclass() == 0
                        && intf.getInterfaceProtocol() == 0) {
                    myInterface = intf;
                }
                break;
            }
        }
    }

    //打开设备
    private void openDevice() {
        if (myInterface != null) {
            UsbDeviceConnection conn = null;
            if (myUsbManager.hasPermission(myUsbDevice)) {
                conn = myUsbManager.openDevice(myUsbDevice);
            }
            if (conn == null) {
                return;
            }
            if (conn.claimInterface(myInterface, true)) {
                myDeviceConnection = conn;
            } else {
                conn.close();
            }
        }
    }

    private void assignEndpoint() {
        if (myInterface != null) {
            for (int i = 0; i < myInterface.getEndpointCount(); i++) {
                UsbEndpoint ep = myInterface.getEndpoint(i);
                if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_INT) {
                    if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                        epOut = ep;
                    } else {
                        epIn = ep;
                    }
                }
            }
        }
    }

    private void openFunction() {
        boolean isBad = true;
        if(mUtils!=null){
            isBad = mUtils.isBad();
        }
        if (myDeviceConnection != null && epIn != null && epOut != null && isBad==true)
            mUtils = new InPutOutPutUtils(myDeviceConnection, this, epIn, epOut,this);

        if (mUtils != null) {
            mExecutor.submit(mUtils);
        }
    }

    public static void setCallBackListener(IconnectFailureCallback callBackListener){
        if(mCallbacks!=null&&mCallbacks.size()>0) {
            mCallbacks.remove(callBackListener);
            mCallbacks.add(callBackListener);
        }else{
            mCallbacks = new ArrayList<>();
            mCallbacks.add(callBackListener);
        }
    }

    public static void unSetCallBackListener(IconnectFailureCallback callBackListener){
        if(mCallbacks!=null&&mCallbacks.size()>0) {
            mCallbacks.remove(callBackListener);
        }
    }
}
