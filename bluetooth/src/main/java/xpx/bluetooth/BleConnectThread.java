package xpx.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.Build;

public class BleConnectThread extends Thread {
    public Context context;
    public BluetoothDevice bluetoothDevice;
    public BluetoothGatt bluetoothGatt;
    public BleConnectThread(Context context, BluetoothDevice device) {
        this.context = context;
        this.bluetoothDevice = device;
    }

    @Override
    public void run() {
        super.run();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bluetoothDevice.connectGatt(context, true, BluetoothSetManager.bluetoothSetManager.mGattCallback,
                  BluetoothDevice.TRANSPORT_LE);
        }
        else
        {
            bluetoothDevice.connectGatt(context, false, BluetoothSetManager.bluetoothSetManager.mGattCallback);
        }
    }

}
