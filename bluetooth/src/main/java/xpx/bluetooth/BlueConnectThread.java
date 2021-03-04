package xpx.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Message;

public class BlueConnectThread extends Thread{

    public BluetoothSetManager bluetoothSetManager;
    public boolean stop = false;
    public BluetoothDevice device;
    public int persent = 1;
    public long TIMEOUT = 20;
    public long currentlast = 0;
    public BlueConnectThread(BluetoothSetManager bluetoothSetManager, BluetoothDevice device)
    {
        this.bluetoothSetManager = bluetoothSetManager;
        this.device = device;
    }

    @Override
    public void run() {
        super.run();
        currentlast = System.currentTimeMillis();
        Message msg1 = new Message();
        msg1.obj = device;
        msg1.what = BlueToothHandler.EVENT_CONCTACT_DEVICE;
        BluetoothSetManager.bluetoothSetManager.blueToothHandler
                .sendMessage(msg1);
        currentlast = System.currentTimeMillis();
        while (bluetoothSetManager.hashConnect.get(device.getAddress()) == null && stop == false)
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
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        currentlast = System.currentTimeMillis();
        while (bluetoothSetManager.getDeviceConnect(device.getAddress()) == false && stop == false)
        {
            checkTimeout();
            if(persent < 80)
            {
                persent++;
            }
            Message msg = new Message();
            msg.obj = String.valueOf(persent)+"%";
            msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
            bluetoothSetManager.blueToothHandler.sendMessage(msg);
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(stop == true)
        {
            persent = 100;
            Message msg = new Message();
            msg.obj = device;
            msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH_FAIL;
            bluetoothSetManager.blueToothHandler.sendMessage(msg);
        }
        else
        {
            persent = 100;
            Message msg = new Message();
            msg.obj = device;
            msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH_FINISH;
            bluetoothSetManager.blueToothHandler.sendMessage(msg);
        }

    }


    public void checkTimeout()
    {
        if(System.currentTimeMillis() - currentlast > TIMEOUT*1000)
        {
           stop = true;
        }

    }

}
