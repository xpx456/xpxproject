package xpx.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Message;

public class NewBlueConnectThread extends Thread{

    public BluetoothSetManager bluetoothSetManager;
    public boolean stop = false;
    public BluetoothDevice device;
    public long TIMEOUT = 20;
    public long currentlast = 0;
    public boolean timedout = false;
    public String action;
    public NewBlueConnectThread(BluetoothSetManager bluetoothSetManager, BluetoothDevice device,String action)
    {
        this.bluetoothSetManager = bluetoothSetManager;
        this.device = device;
        this.action = action;
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
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(stop == false)
        {
            if(timedout == true)
            {
                Message msg = new Message();
                msg.obj = device;
                msg.what = BlueToothHandler.EVENT_UPDATA_NEW_BLUETOOTH_FAIL;
                bluetoothSetManager.blueToothHandler.sendMessage(msg);
            }
            else
            {
                Message msg = new Message();
                msg.obj = device;
                msg.what = BlueToothHandler.EVENT_UPDATA_NEW_BLUETOOTH_FINISH;
                bluetoothSetManager.blueToothHandler.sendMessage(msg);
            }
        }

    }


    public void checkTimeout()
    {
        if(System.currentTimeMillis() - currentlast > TIMEOUT*1000)
        {
            timedout = true;
        }

    }

}
