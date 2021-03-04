package xpx.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import intersky.apputils.AppUtils;
import intersky.apputils.OSHelper;
import intersky.apputils.RomUtil;


public class BluetoothReceiver extends BroadcastReceiver {

    public Handler handler;

    public BluetoothReceiver(Handler handler)
    {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {

            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Message message = new Message();
            message.what = BlueToothHandler.EVENT_FIND_DEVICE;
            message.obj = device;
            handler.sendMessage(message);
        }
        else if (intent.getAction().equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
            BluetoothDevice mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            try {
                //(三星)4.3版本测试手机还是会弹出用户交互页面(闪一下)，如果不注释掉下面这句页面不会取消但可以配对成功。(中兴，魅族4(Flyme 6))5.1版本手机两中情况下都正常

                {
                    //ClsUtils.setPairingConfirmation(mBluetoothDevice.getClass(), mBluetoothDevice, true);
                    if(RomUtil.isEmui() == false)
                    abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                }
               // mBluetoothDevice.setPin(BluetoothSetManager.bluetoothSetManager.ping.getBytes());
                //3.调用setPin方法进行配对...
                boolean ret = ClsUtils.setPin(mBluetoothDevice.getClass(), mBluetoothDevice, BluetoothSetManager.bluetoothSetManager.ping);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Message msg = new Message();
            msg.obj = device;

            switch (device.getBondState()) {
                case BluetoothDevice.BOND_NONE:
                    if(BluetoothSetManager.bluetoothSetManager.blueToothHandler != null)
                    {
                        msg.what = BlueToothHandler.EVENT_REBOND;
                        BluetoothSetManager.bluetoothSetManager.blueToothHandler
                                .sendMessage(msg);
                    }

                    break;
                case BluetoothDevice.BOND_BONDING:
                    break;
                case BluetoothDevice.BOND_BONDED:
                    if(BluetoothSetManager.bluetoothSetManager.blueToothHandler != null)
                    {
                        msg.what = BlueToothHandler.EVENT_CONCTACT_DEVICE;
                        BluetoothSetManager.bluetoothSetManager.blueToothHandler
                                .sendMessage(msg);
                    }

                    break;
            }
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_GATT_SERVICES_DISCOVERED))
        {
            handler.sendEmptyMessage(BlueToothHandler.EVENT_DISCOVER_SERVEICE);
        }
        if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_DISCONNECT))
        {
            handler.sendEmptyMessage(BlueToothHandler.EVENT_GETT_DISCONNECT);
        }
    }

}
