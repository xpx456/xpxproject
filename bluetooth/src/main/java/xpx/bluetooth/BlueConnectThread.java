package xpx.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Message;
import android.os.ParcelUuid;

public class BlueConnectThread extends Thread{

    public BluetoothSetManager bluetoothSetManager;
    public boolean stop = false;
    public BluetoothDevice device;
    public int persent = 1;
    public long TIMEOUT = 20;
    public long currentlast = 0;
    public BlueConnectThread(BluetoothSetManager bluetoothSetManager,BluetoothDevice device)
    {
        this.bluetoothSetManager = bluetoothSetManager;
        this.device = device;
    }

    @Override
    public void run() {
        super.run();
        currentlast = System.currentTimeMillis();
        if(device.getBondState() != BluetoothDevice.BOND_BONDED)
        {
            persent = 20;
            device.createBond();
            Message message = new Message();
            message.obj = String.valueOf(persent)+"%";
            message.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
            bluetoothSetManager.blueToothHandler.sendMessage(message);
            while (device.getBondState() != BluetoothDevice.BOND_BONDED && stop == false)
            {
                if(persent < 50)
                {
                    persent++;
                }
                Message msg = new Message();
                msg.obj = String.valueOf(persent)+"%";
                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
                bluetoothSetManager.blueToothHandler.sendMessage(msg);
            }
            while (bluetoothSetManager.mBluetoothGatt == null )
            {
                checkTimeout();
            }
            while (bluetoothSetManager.mConnectionState != BluetoothSetManager.STATE_CONNECTED && stop == false)
            {
                checkTimeout();
                if(persent < 70)
                {
                    persent++;
                }
                Message msg = new Message();
                msg.obj = String.valueOf(persent)+"%";
                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
                bluetoothSetManager.blueToothHandler.sendMessage(msg);
            }
            while (bluetoothSetManager.hasgetservice == false&& stop == false)
            {
                checkTimeout();
                if(persent < 99)
                {
                    persent++;
                }
                Message msg = new Message();
                msg.obj = String.valueOf(persent)+"%";
                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
                bluetoothSetManager.blueToothHandler.sendMessage(msg);
            }

        }
        else
        {
            persent = 5;
            Message message = new Message();
            message.obj = String.valueOf(persent)+"%";
            message.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
            bluetoothSetManager.blueToothHandler.sendMessage(message);
            boolean first = true;
            while (device.getBondState() == BluetoothDevice.BOND_BONDED)
            {
                checkTimeout();
                if(first)
                {
                    first = false;
                    bluetoothSetManager.unpairDevice(device);
                }
                if(persent < 20)
                {
                    persent++;
                }
                Message msg = new Message();
                msg.obj = String.valueOf(persent)+"%";
                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
                bluetoothSetManager.blueToothHandler.sendMessage(msg);
                bluetoothSetManager.unpairDevice(device);
            }
            while (device.getBondState() != BluetoothDevice.BOND_BONDED && stop == false)
            {
                checkTimeout();
                if(persent < 50)
                {
                    persent++;
                }
                Message msg = new Message();
                msg.obj = String.valueOf(persent)+"%";
                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
                bluetoothSetManager.blueToothHandler.sendMessage(msg);
            }
            while (bluetoothSetManager.mBluetoothGatt == null)
            {
                checkTimeout();
            }
            while (bluetoothSetManager.mConnectionState != BluetoothSetManager.STATE_CONNECTED && stop == false)
            {
                checkTimeout();
                if(persent < 70)
                {
                    persent++;
                }
                Message msg = new Message();
                msg.obj = String.valueOf(persent)+"%";
                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
                bluetoothSetManager.blueToothHandler.sendMessage(msg);
            }
            while (bluetoothSetManager.hasgetservice == false && stop == false)
            {
                checkTimeout();
                if(persent < 99)
                {
                    persent++;
                }
                Message msg = new Message();
                msg.obj = String.valueOf(persent)+"%";
                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
                bluetoothSetManager.blueToothHandler.sendMessage(msg);
            }
        }

        persent = 100;
        Message msg = new Message();
        msg.obj = String.valueOf(persent)+"%";
        msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH_FINISH;
        bluetoothSetManager.blueToothHandler.sendMessage(msg);
    }


    public void checkTimeout()
    {
        if(System.currentTimeMillis() - currentlast > TIMEOUT*1000)
        {
           stop = false;
        }
        currentlast = System.currentTimeMillis();
    }
}
