package xpx.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;


import intersky.apputils.AppUtils;

public class BlueToothHandler extends Handler {

    public static final int PERMISSION_REQUEST_COARSE_LOCATION = 40001;
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 40002;
    public static final int PERMISSION_REQUEST_WRITE_SETTINGS = 40003;
    public static final int PERMISSION_REQUEST_WRITE_SECURE_SETTINGS = 40004;
    public static final int EVENT_FIND_DEVICE = 300004;
    public static final int EVENT_DISCOVER_SERVEICE = 300005;
    public static final int EVENT_CONCTACT_DEVICE = 300006;
    public static final int EVENT_REBOND = 300007;

    public static final int  EVENT_UPDATA_BLUETOOTH = 300010;
    public static final int  EVENT_UPDATA_BLUETOOTH_FINISH = 300011;
    public static final int  EVENT_GETT_DISCONNECT = 300012;
    public static final int EVENT_UN_FIND_DEVICE = 300013;
    public static final int EVENT_STOP_BLE_SCAN_DEVICE = 300014;
    public static final int  EVENT_UPDATA_BLUETOOTH_FAIL = 300015;
    public static final int  EVENT_NEW_APP_CONNECT_FAIL = 300016;
    public static final int  EVENT_NEW_APP_CONNECT_SUCCESS = 300017;
    public static final int EVENT_NEW_UN_FIND_DEVICE = 300018;
    public static final int  EVENT_UPDATA_NEW_BLUETOOTH_FAIL = 300019;
    public static final int  EVENT_UPDATA_NEW_BLUETOOTH_FINISH = 300020;
    public BluetoothSetManager bluetoothSetManager;

    public BlueToothHandler(BluetoothSetManager bluetoothSetManager) {
        this.bluetoothSetManager = bluetoothSetManager;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case EVENT_FIND_DEVICE:
                bluetoothSetManager.addDevice((BluetoothDevice) msg.obj);
                break;
            case EVENT_REBOND:
                //bluetoothSetManager.reBond((BluetoothDevice) msg.obj);
                break;
            case PERMISSION_REQUEST_WRITE_SECURE_SETTINGS:
                bluetoothSetManager.doDiscovery();
                break;
            case EVENT_DISCOVER_SERVEICE:
                BluetoothSetManager.bluetoothSetManager.blueToothApis.measureService();
                break;
            case EVENT_CONCTACT_DEVICE:
                BluetoothDevice device = (BluetoothDevice) msg.obj;
                BluetoothSetManager.bluetoothSetManager.connectDevice(device
                        ,bluetoothSetManager.context);
                break;
            case EVENT_UPDATA_BLUETOOTH:
                BluetoothSetManager.bluetoothSetManager.updataHit((String) msg.obj);
                break;
            case EVENT_UPDATA_BLUETOOTH_FINISH:
                BluetoothSetManager.bluetoothSetManager.blueConnectThread = null;
                BluetoothSetManager.bluetoothSetManager.updataFinish((BluetoothDevice) msg.obj);
                break;
            case EVENT_GETT_DISCONNECT:
                BluetoothSetManager.bluetoothSetManager.stopAuto();
                break;
            case EVENT_UN_FIND_DEVICE:
            case EVENT_STOP_BLE_SCAN_DEVICE:
                bluetoothSetManager.stopLeScan();
                if(bluetoothSetManager.findaddress.length() > 0)
                {
                    if(bluetoothSetManager.devices.get(bluetoothSetManager.findaddress) == null)
                    {
                        bluetoothSetManager.unfindDevice();
                    }
                }

                break;
            case EVENT_UPDATA_BLUETOOTH_FAIL:
                BluetoothSetManager.bluetoothSetManager.blueConnectThread = null;
                BluetoothSetManager.bluetoothSetManager.connectFail();
                break;
            case EVENT_NEW_APP_CONNECT_SUCCESS:
                Intent intent = new Intent((String) msg.obj);
                intent.putExtra("connect",true);
                if(BluetoothSetManager.bluetoothSetManager.context != null)
                BluetoothSetManager.bluetoothSetManager.context.sendBroadcast(intent);
                break;
            case EVENT_NEW_APP_CONNECT_FAIL:
                Intent intent1 = new Intent((String) msg.obj);
                intent1.putExtra("connect",false);
                if(BluetoothSetManager.bluetoothSetManager.context != null)
                    BluetoothSetManager.bluetoothSetManager.context.sendBroadcast(intent1);
                break;
            case EVENT_UPDATA_NEW_BLUETOOTH_FINISH:
                BluetoothSetManager.bluetoothSetManager.blueNewConnectThread = null;
                BluetoothSetManager.bluetoothSetManager.updatamewFinish((BluetoothDevice) msg.obj);
                break;
            case EVENT_UPDATA_NEW_BLUETOOTH_FAIL:
                BluetoothSetManager.bluetoothSetManager.blueNewConnectThread = null;
                BluetoothSetManager.bluetoothSetManager.connectFail();
                break;

        }
    }
}
