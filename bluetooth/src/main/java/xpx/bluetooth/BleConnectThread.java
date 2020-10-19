package xpx.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;

public class BleConnectThread extends Thread {
    public Context context;

    public BleConnectThread(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        super.run();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          BluetoothSetManager.bluetoothSetManager.mBluetoothGatt = BluetoothSetManager.bluetoothSetManager.netBluetoothDevice.connectGatt(context, true, BluetoothSetManager.bluetoothSetManager.mGattCallback,
                  BluetoothDevice.TRANSPORT_LE);
        }
        else
        {
            BluetoothSetManager.bluetoothSetManager.mBluetoothGatt = BluetoothSetManager.bluetoothSetManager.netBluetoothDevice.connectGatt(context, false, BluetoothSetManager.bluetoothSetManager.mGattCallback);
        }
    }

}
