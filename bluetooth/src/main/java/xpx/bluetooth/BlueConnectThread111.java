package xpx.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Message;
import android.os.ParcelUuid;

public class BlueConnectThread111 extends Thread{

    public BluetoothSetManager bluetoothSetManager;
    public boolean stop = false;
    public BluetoothDevice device;
    public int persent = 1;
    public long TIMEOUT = 20;
    public long currentlast = 0;
    public BlueConnectThread111(BluetoothSetManager bluetoothSetManager,BluetoothDevice device)
    {
        this.bluetoothSetManager = bluetoothSetManager;
        this.device = device;
    }

    @Override
    public void run() {
        super.run();
        currentlast = System.currentTimeMillis();
//        if(device.getBondState() != BluetoothDevice.BOND_BONDED)
//        {
//            persent = 30;
//            device.createBond();
//            Message message = new Message();
//            message.obj = String.valueOf(persent)+"%";
//            message.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
//            bluetoothSetManager.blueToothHandler.sendMessage(message);
//            while (device.getBondState() != BluetoothDevice.BOND_BONDED && stop == false)
//            {
//                if(persent < 60)
//                {
//                    persent++;
//                }
//                Message msg = new Message();
//                msg.obj = String.valueOf(persent)+"%";
//                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
//                bluetoothSetManager.blueToothHandler.sendMessage(msg);
//                try {
//                    sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            currentlast = System.currentTimeMillis();
//            while (bluetoothSetManager.mBluetoothGatt == null && stop == false)
//            {
//                checkTimeout();
//                if(persent < 70)
//                {
//                    persent++;
//                }
//                Message msg = new Message();
//                msg.obj = String.valueOf(persent)+"%";
//                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
//                bluetoothSetManager.blueToothHandler.sendMessage(msg);
//                try {
//                    sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            currentlast = System.currentTimeMillis();
//            while (bluetoothSetManager.mConnectionState != BluetoothSetManager.STATE_CONNECTED && stop == false)
//            {
//                checkTimeout();
//                if(persent < 80)
//                {
//                    persent++;
//                }
//                Message msg = new Message();
//                msg.obj = String.valueOf(persent)+"%";
//                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
//                bluetoothSetManager.blueToothHandler.sendMessage(msg);
//                try {
//                    sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            currentlast = System.currentTimeMillis();
//            while (bluetoothSetManager.hasgetservice == false&& stop == false)
//            {
//                checkTimeout();
//                if(persent < 99)
//                {
//                    persent++;
//                }
//                Message msg = new Message();
//                msg.obj = String.valueOf(persent)+"%";
//                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
//                bluetoothSetManager.blueToothHandler.sendMessage(msg);
//                try {
//                    sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        else
//        {
//            persent = 30;
//            Message message = new Message();
//            message.obj = String.valueOf(persent)+"%";
//            message.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
//            bluetoothSetManager.blueToothHandler.sendMessage(message);
//            boolean first = true;
//            while (device.getBondState() == BluetoothDevice.BOND_BONDED && stop == false)
//            {
//                checkTimeout();
//                if(first)
//                {
//                    first = false;
//                    bluetoothSetManager.unpairDevice(device);
//                }
//                if(persent < 45)
//                {
//                    persent++;
//                }
//                Message msg = new Message();
//                msg.obj = String.valueOf(persent)+"%";
//                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
//                bluetoothSetManager.blueToothHandler.sendMessage(msg);
////                bluetoothSetManager.unpairDevice(device);
//                try {
//                    sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            currentlast = System.currentTimeMillis();
//            while (device.getBondState() != BluetoothDevice.BOND_BONDED && stop == false)
//            {
//                device.createBond();
//                checkTimeout();
//                if(persent < 60)
//                {
//                    persent++;
//                }
//                Message msg = new Message();
//                msg.obj = String.valueOf(persent)+"%";
//                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
//                bluetoothSetManager.blueToothHandler.sendMessage(msg);
//                try {
//                    sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            currentlast = System.currentTimeMillis();
//            while (bluetoothSetManager.mBluetoothGatt == null)
//            {
//                checkTimeout();
//                if(persent < 70)
//                {
//                    persent++;
//                }
//                Message msg = new Message();
//                msg.obj = String.valueOf(persent)+"%";
//                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
//                bluetoothSetManager.blueToothHandler.sendMessage(msg);
//                try {
//                    sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            currentlast = System.currentTimeMillis();
//            while (bluetoothSetManager.mConnectionState != BluetoothSetManager.STATE_CONNECTED && stop == false)
//            {
//                checkTimeout();
//                if(persent < 80)
//                {
//                    persent++;
//                }
//                Message msg = new Message();
//                msg.obj = String.valueOf(persent)+"%";
//                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
//                bluetoothSetManager.blueToothHandler.sendMessage(msg);
//                try {
//                    sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            currentlast = System.currentTimeMillis();
//            while (bluetoothSetManager.hasgetservice == false && stop == false)
//            {
//                checkTimeout();
//                if(persent < 99)
//                {
//                    persent++;
//                }
//                Message msg = new Message();
//                msg.obj = String.valueOf(persent)+"%";
//                msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH;
//                bluetoothSetManager.blueToothHandler.sendMessage(msg);
//                try {
//                    sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        if(stop == true)
//        {
//            persent = 100;
//            Message msg = new Message();
//            msg.obj = String.valueOf(persent)+"%";
//            msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH_FAIL;
//            bluetoothSetManager.blueToothHandler.sendMessage(msg);
//        }
//        else
//        {
//            persent = 100;
//            Message msg = new Message();
//            msg.obj = String.valueOf(persent)+"%";
//            msg.what = BlueToothHandler.EVENT_UPDATA_BLUETOOTH_FINISH;
//            bluetoothSetManager.blueToothHandler.sendMessage(msg);
//        }

    }


    public void checkTimeout()
    {
        if(System.currentTimeMillis() - currentlast > TIMEOUT*1000)
        {
           stop = true;
        }

    }

}
