package com.xpxbluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;

public class BlueToothHandler extends Handler {



    public static final int  CONNECT_DEVICE_UPDATA = 300000;
    public static final int  CONNECT_DEVICE_FINISH = 300001;
    public static final int  CONNECT_DEVICE_STOP = 300002;
    public static final int  CONNECT_DEVICE_DISMISS = 300003;
    public BluetoothSetManager bluetoothSetManager;

    public BlueToothHandler(BluetoothSetManager bluetoothSetManager) {
        this.bluetoothSetManager = bluetoothSetManager;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {

            case CONNECT_DEVICE_UPDATA:
                bluetoothSetManager.connectUpdata((XpxConnect) msg.obj);
                break;
            case CONNECT_DEVICE_FINISH:
                bluetoothSetManager.connectFinish((XpxConnect) msg.obj);
                break;
            case CONNECT_DEVICE_STOP:
                bluetoothSetManager.connectStop((XpxConnect) msg.obj);
                break;
            case CONNECT_DEVICE_DISMISS:
                bluetoothSetManager.dismiss((XpxConnect) msg.obj);
                break;
        }
    }
}
