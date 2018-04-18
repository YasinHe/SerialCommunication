package com.qx.interactive.answer.utils;


import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;

import com.qx.interactive.answer.interfaces.IwriteDateFailureCallBack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by HeYingXin on 2017/2/13.
 */
public class InPutOutPutUtils extends Thread {

    private static final String TAG = InPutOutPutUtils.class.getSimpleName();
    private IwriteDateFailureCallBack mDateFailureCallBack;

    //超时时间
    private static final int READ_WAIT_MILLIS = 2000;
    private static final int BUFSIZ = 9;//根据文档一般是9个字节为一个命令
    public static final int DEFAULT_READ_BUFFER_SIZE = 1024;//最大缓冲
    public static final int DEFAULT_WRITE_BUFFER_SIZE = 1024;
    private boolean isCanUse = true;

    private final UsbDeviceConnection mDriver;
    private final UsbEndpoint mWriteEndpoint;
    private final UsbEndpoint mReadEndpoint;

    private final ByteBuffer mReadBuffer = ByteBuffer.allocate(BUFSIZ);
    private ArrayList<ByteBuffer> mWriteBuffers = new ArrayList<>();

    private final Object mReadBufferLock = new Object();
    private final Object mWriteBufferLock = new Object();

    private byte[] mReadBufferByte;
    private byte[] mWriteBufferByte;

    private enum States {
        STOPPED,
        RUNNING,
        STOPPING
    }

    private States mState = States.STOPPED;
    private Listener mListener;
    public interface Listener {
        public void onNewData(byte[] data);
        public void onRunError(Exception e);
    }

    public InPutOutPutUtils(UsbDeviceConnection driver, Listener listener,UsbEndpoint in,UsbEndpoint out,IwriteDateFailureCallBack callBack) {
        mDriver = driver;
        mListener = listener;
        mWriteEndpoint = out;
        mReadEndpoint = in;
        mReadBufferByte = new byte[DEFAULT_READ_BUFFER_SIZE];
        mWriteBufferByte = new byte[DEFAULT_WRITE_BUFFER_SIZE];
        mDateFailureCallBack = callBack;
    }

    public synchronized void setListener(Listener listener) {
        mListener = listener;
    }

    public synchronized Listener getListener() {
        return mListener;
    }

    public void writeAsync(byte[] data) {
        synchronized (mWriteBuffers) {
            ByteBuffer temp = ByteBuffer.allocate(BUFSIZ);
            temp.put(data);
            mWriteBuffers.add(temp);
        }
    }

    public synchronized boolean isBad() {
          return !isCanUse;
    }

    private synchronized States getStates() {
        return mState;
    }

    @Override
    public void run() {
        synchronized (this) {
            if (getStates() != States.STOPPED) {
                throw new IllegalStateException("Already running.");
            }
            mState = States.RUNNING;
        }
        try {
            while (true) {
                if (getStates() != States.RUNNING) {
                    break;
                }
                step();
            }
        } catch (Exception e) {
            final Listener listener = getListener();
            if (listener != null) {
                listener.onRunError(e);
            }
        } finally {
            synchronized (this) {
                mState = States.STOPPED;
            }
        }
    }

    private void step() throws IOException {
        int len = read(mReadBuffer.array(), READ_WAIT_MILLIS);
        if (len > 0) {
            final Listener listener = getListener();
            if (listener != null) {
                final byte[] data = new byte[len];
                mReadBuffer.get(data, 0, len);
                listener.onNewData(data);
            }
            mReadBuffer.clear();
        }
        byte[] outBuff = null;
        synchronized (mWriteBuffers) {
            if(mWriteBuffers!=null&&mWriteBuffers.size()>0){
                ByteBuffer buffer = mWriteBuffers.get(0);
                if (buffer.position() > 0) {
                    len = buffer.position();
                    outBuff = new byte[len];
                    buffer.rewind();
                    buffer.get(outBuff, 0, len);
                    mWriteBuffers.remove(buffer);
                    buffer.clear();
                }
            }
        }
        if (outBuff != null) {
            write(outBuff, READ_WAIT_MILLIS);
        }
    }

    private int read(byte[] dest, int timeoutMillis){
        synchronized (mReadBufferLock) {
            int readAmt = Math.min(dest.length, mReadBufferByte.length);
            int numBytesRead = mDriver.bulkTransfer(mReadEndpoint, mReadBufferByte,
                    readAmt, timeoutMillis);
            if (numBytesRead < 0) {
                return 0;
            }
            System.arraycopy(mReadBufferByte, 0, dest, 0, numBytesRead);
            return numBytesRead;
        }
    }

    private int write(byte[] src, int timeoutMillis) throws IOException {
        int offset = 0;
        while (offset < src.length) {
            final int writeLength;
            final int amtWritten;
            synchronized (mWriteBufferLock) {
                final byte[] writeBuffer;
                writeLength = Math.min(src.length - offset, mWriteBufferByte.length);
                if (offset == 0) {
                    writeBuffer = src;
                } else {
                    System.arraycopy(src, offset, mWriteBufferByte, 0, writeLength);
                    writeBuffer = mWriteBufferByte;
                }
                amtWritten = mDriver.bulkTransfer(mWriteEndpoint,
                                writeBuffer, writeLength, timeoutMillis);
            }
            if (amtWritten <= 0) {
                isCanUse = false;
                mDateFailureCallBack.reWrite();
                LogUtils.e(TAG,"写入答题宝失败，数据写入报错结果："+amtWritten+"--writeLength:"+writeLength+"--offset:"+offset
                +"--src:"+src);
                throw new IOException("Error writing " + writeLength
                        + " bytes at offset " + offset + " length="
                        + src.length);
            }else{
                isCanUse = true;
                mDateFailureCallBack.success();
            }
            offset += amtWritten;
        }
        return offset;
    }

}
