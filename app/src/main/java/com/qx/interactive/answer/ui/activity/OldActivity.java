package com.qx.interactive.answer.ui.activity;

import android.app.Activity;
import android.app.PendingIntent;
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
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.qx.interactive.answer.R;
import com.qx.interactive.answer.interfaces.IwriteDateFailureCallBack;
import com.qx.interactive.answer.utils.InPutOutPutUtils;
import com.qx.interactive.answer.utils.LogUtils;
import com.qx.interactive.answer.utils.OtgUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/2/13.
 */
public class OldActivity extends Activity implements InPutOutPutUtils.Listener,IwriteDateFailureCallBack {
    private static final String TAG = "USB_HOST";

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
    private TextView info;
    private UsbEndpoint epOut;
    private UsbEndpoint epIn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old);
        info = (TextView) findViewById(R.id.info);
        // 获取UsbManager
        myUsbManager = (UsbManager) getSystemService(USB_SERVICE);
        tryGetUsbPermission();
        enumerateDevice();
        findInterface();
        openDevice();
        assignEndpoint();
        openFunction();
    }

    /**
     * 分配端点，IN | OUT，即输入输出；此处我直接用1为OUT端点，0为IN，当然你也可以通过判断
     */

    //USB_ENDPOINT_XFER_BULK
     /*
     #define USB_ENDPOINT_XFER_CONTROL 0 --控制传输
     #define USB_ENDPOINT_XFER_ISOC 1 --等时传输
     #define USB_ENDPOINT_XFER_BULK 2 --块传输
     #define USB_ENDPOINT_XFER_INT 3 --中断传输
     * */
    private void assignEndpoint() {
        if (myInterface != null) { //这一句不加的话 很容易报错  导致很多人在各大论坛问:为什么报错呀
            //这里的代码替换了一下 按自己硬件属性判断吧
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

    /**
     * 打开设备
     */
    private void openDevice() {
        if (myInterface != null) {
            UsbDeviceConnection conn = null;
            // 在open前判断是否有连接权限；对于连接权限可以静态分配，也可以动态分配权限，可以查阅相关资料
            if (myUsbManager.hasPermission(myUsbDevice)) {
                conn = myUsbManager.openDevice(myUsbDevice);
            }

            if (conn == null) {
                return;
            }

            if (conn.claimInterface(myInterface, true)) {
                myDeviceConnection = conn; // 到此你的android设备已经连上HID设备
                Log.d(TAG, "打开设备成功");
            } else {
                conn.close();
            }
        }
    }

    /**
     * 找设备接口
     */
    private void findInterface() {
        if (myUsbDevice != null) {
            LogUtils.d(TAG, "interfaceCounts : " + myUsbDevice.getInterfaceCount());
            for (int i = 0; i < myUsbDevice.getInterfaceCount(); i++) {
                UsbInterface intf = myUsbDevice.getInterface(i);
                // 根据手上的设备做一些判断，其实这些信息都可以在枚举到设备时打印出来
                if (intf.getInterfaceClass() == 3
                        && intf.getInterfaceSubclass() == 0
                        && intf.getInterfaceProtocol() == 0) {
                    myInterface = intf;
                    Log.d(TAG, "找到我的设备接口");
                }
                break;
            }
        }
    }

    /**
     * 枚举设备
     */
    private void enumerateDevice() {
        if (myUsbManager == null)
            return;

        HashMap<String, UsbDevice> deviceList = myUsbManager.getDeviceList();
        if (!deviceList.isEmpty()) { // deviceList不为空
            StringBuffer sb = new StringBuffer();
            for (UsbDevice device : deviceList.values()) {
                sb.append(device.toString());
                sb.append("\n");
                info.setText(sb);
                // 输出设备信息
                LogUtils.d(TAG, "DeviceInfo: " + device.getVendorId() + " , "
                        + device.getProductId());

                // 枚举到设备
//                if (device.getVendorId() == VendorID
//                        && device.getProductId() == ProductID) {
                myUsbDevice = device;
                LogUtils.d(TAG, "枚举设备成功");
//                }
            }
        }
    }

    private void openFunction(){
        if(myDeviceConnection!=null&&epIn!=null&&epOut!=null&&mUtils==null)
        mUtils = new InPutOutPutUtils(myDeviceConnection,this,epIn,epOut,this);

        if(mUtils!=null){
            mExecutor.submit(mUtils);
            //第一步 停止广播f
            byte[] step1 = new byte[]{BaseStationId,0x00,0x00,0x00, (byte) 0xf3,0x00,0x00,0x00,0x00};
            mUtils.writeAsync(step1);
            //第二步 清空ID列表
            byte[] step2 = new byte[]{BaseStationId,0x00,0x00,0x00, (byte) 0xf5,0x00,0x01,0x00,0x00};
            mUtils.writeAsync(step2);
            //第三步 开始广播
            byte[] step3 = new byte[]{BaseStationId,0x00,0x00,0x00, (byte) 0xf3,0x01,0x00,0x00,0x00};
            mUtils.writeAsync(step3);
            //第四步 结束答题
            byte[] step4 = new byte[]{BaseStationId,0x00,0x01,0x00, (byte) 0xf2,0x00,0x00,0x00,0x00};
            mUtils.writeAsync(step4);
            //第五步 握手
//            byte[] step5 = new byte[]{(byte) 0xf4,0x01,0x00,0x00,0x00};
//            byte[] cardId = OtgUtils.cardIdToHex(998350795);
//            mUtils.writeAsync(OtgUtils.byteMerger(cardId,step5));
            //第六步 开始答题
//            byte[] step6 = new byte[]{BaseStationId,0x01,0x00,0x00, (byte) 0xf2,0x00,0x00,0x00,0x00};
//            mUtils.writeAsync(step6);
            //第七步 结束答题
//            byte[] step7 = new byte[]{BaseStationId,0x00,0x01,0x00, (byte) 0xf2,0x00,0x00,0x00,0x00};
//            mUtils.writeAsync(step7);
        }
    }

    @Override
    public void onNewData(byte[] data) {
        String result = OtgUtils.bytes2hex03(data);
        Log.e(TAG,"接受消息："+result);
        if(result.substring(8,10).equals("a6")) {
            LogUtils.e(TAG, "消息发送人为:" + result.substring(0, 8) + "他的物理卡号为:"
                    + new BigInteger(result.substring(0, 8), 16).toString());
            LogUtils.e(TAG, "消息的识别为：" + result.substring(8, 10));
            LogUtils.e(TAG, "学生单选题结果为：" + result.substring(10, 12) + "------->"
                    + OtgUtils.studentOneChooseResult(result.substring(10, 12)));
            LogUtils.e(TAG, "学生对错勾选题结果为：" + result.substring(12, 14) + "------->"
                    + OtgUtils.studentRightOrWrongChooseResult(result.substring(12, 14)));
            LogUtils.e(TAG, "消息的答题号为：" + result.substring(14, 16));
            LogUtils.e(TAG, "消息的答题结果标示为：" + result.substring(16, 18) + "-----答题结果是否有效--->"
                    + ((Integer.parseInt(result.substring(16, 18),10) & 0x80) == 0));
        }
    }

    @Override
    public void onRunError(Exception e) {
        Log.e(TAG,"消息队列报错"+e);
    }

    //-------------------------------------------------权限部分-----------------------------------------------------
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private void tryGetUsbPermission() {
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbPermissionActionReceiver, filter);

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);

        //here do emulation to ask all connected usb device for permission
        for (final UsbDevice usbDevice : myUsbManager.getDeviceList().values()) {
            //add some conditional check if necessary
            //if(isWeCaredUsbDevice(usbDevice)){
            if (myUsbManager.hasPermission(usbDevice)) {
                //if has already got permission, just goto connect it
                //that means: user has choose yes for your previously popup window asking for grant perssion for this usb device
                //and also choose option: not ask again
                afterGetUsbPermission(usbDevice);
            } else {
                //this line will let android popup window, ask user whether to allow this app to have permission to operate this usb device
                myUsbManager.requestPermission(usbDevice, mPermissionIntent);
            }
            //}
        }
    }

    private void afterGetUsbPermission(UsbDevice usbDevice) {
        //call method to set up device communication
        Toast.makeText(this, String.valueOf("Got permission for usb device: " + usbDevice), Toast.LENGTH_LONG).show();
        Toast.makeText(this, String.valueOf("Found USB device: VID=" + usbDevice.getVendorId() + " PID=" + usbDevice.getProductId()), Toast.LENGTH_LONG).show();
        doYourOpenUsbDevice(usbDevice);
    }

    private void doYourOpenUsbDevice(UsbDevice usbDevice) {
        //now follow line will NOT show: User has not given permission to device UsbDevice
        UsbDeviceConnection connection = myUsbManager.openDevice(usbDevice);
        //add your operation code here
    }

    private final BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //user choose YES for your previously popup window asking for grant perssion for this usb device
                        if (null != usbDevice) {
                            afterGetUsbPermission(usbDevice);
                        }
                    } else {
                        //user choose NO for your previously popup window asking for grant perssion for this usb device
                        Toast.makeText(context, String.valueOf("Permission denied for device" + usbDevice), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    @Override
    public void reWrite() {

    }

    @Override
    public void success() {

    }
}

