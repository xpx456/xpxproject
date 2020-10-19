package xpx.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
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
                bluetoothSetManager.reBond((BluetoothDevice) msg.obj);
                break;
            case PERMISSION_REQUEST_WRITE_SECURE_SETTINGS:
                bluetoothSetManager.doDiscovery();
                break;
            case EVENT_DISCOVER_SERVEICE:
                BluetoothSetManager.bluetoothSetManager.blueToothApis.measureService();
                break;
            case EVENT_CONCTACT_DEVICE:
                BluetoothDevice device = (BluetoothDevice) msg.obj;
                if(bluetoothSetManager.bluetoothDevice.getAddress().equals(device.getAddress()))
                {
                    BluetoothSetManager.bluetoothSetManager.connectDevice(BluetoothSetManager.bluetoothSetManager.bluetoothDevice
                            ,bluetoothSetManager.context);
                }
                break;
            case EVENT_UPDATA_BLUETOOTH:
                BluetoothSetManager.bluetoothSetManager.updataHit((String) msg.obj);
                break;
            case EVENT_UPDATA_BLUETOOTH_FINISH:
                BluetoothSetManager.bluetoothSetManager.updataFinish((String) msg.obj);
                break;
            case EVENT_GETT_DISCONNECT:
                BluetoothSetManager.bluetoothSetManager.stopAuto();
                break;
            case EVENT_UN_FIND_DEVICE:
                bluetoothSetManager.unfindDevice();
                break;
            case EVENT_STOP_BLE_SCAN_DEVICE:
                bluetoothSetManager.stopLeScan();
                Message message = new Message();
                message.what = BlueToothHandler.EVENT_UN_FIND_DEVICE;
                bluetoothSetManager.blueToothHandler.sendMessage(message);
                break;


        }
    }
}
