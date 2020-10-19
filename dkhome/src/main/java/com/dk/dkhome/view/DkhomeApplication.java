package com.dk.dkhome.view;

import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.SharedPreferences;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import intersky.appbase.AppActivityManager;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetUtils;
import xpx.bluetooth.BluetoothSetManager;

public class DkhomeApplication extends Application {

    public static UUID MY_UUID_DK_READ = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    public static UUID MY_UUID_DK_WRITE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    public static UUID MY_UUID_DK_IMF = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public static final  String PING = "123456";
    public AppActivityManager appActivityManager;
    public NetUtils netUtils;
    public static DkhomeApplication mApp;
    public String clidenid;
    public BluetoothSetManager bluetoothSetManager;
    public BluetoothGattCharacteristic mRead;
    public BluetoothGattCharacteristic mWrite;
    public void onCreate() {
        mApp = this;
        appActivityManager = AppActivityManager.getAppActivityManager(mApp);
        netUtils = NetUtils.init(mApp);
        getClientid();
        bluetoothSetManager = BluetoothSetManager.init(mApp, blueToothApis,PING);
        super.onCreate();
    }

    public void getClientid() {
        SharedPreferences sharedPre = mApp.getSharedPreferences("AppDate", 0);
        clidenid = sharedPre.getString("clidenid", AppUtils.getguid());
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString("clidenid",clidenid);
        e.apply();
    }


    public BluetoothSetManager.BlueToothApis blueToothApis = new BluetoothSetManager.BlueToothApis() {
        @Override
        public void measureService() {
            prepareServiceData();
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor characteristic) {

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if(MY_UUID_DK_READ.toString().equals(characteristic.getUuid().toString()))
            {

            }
            else if(MY_UUID_DK_READ.toString().equals(characteristic.getUuid().toString()))
            {

            }
            byte[] array = characteristic.getValue();
            String b = new String(array);
        }
    };

    public void prepareServiceData() {

        List<BluetoothGattService> gattServices = bluetoothSetManager.mBluetoothGatt.getServices();
        if (gattServices == null)
            return;
        for (BluetoothGattService gattService : gattServices) {
            ArrayList<BluetoothGattCharacteristic> clist = new ArrayList<BluetoothGattCharacteristic>();
            clist.addAll(gattService.getCharacteristics());
            for (int j = 0; j < clist.size(); j++)
            {

                BluetoothGattCharacteristic characteristic = clist.get(j);
                if (characteristic.getUuid().toString().equals(MY_UUID_DK_READ.toString())) {
                    mRead = characteristic;
                }
                else if(characteristic.getUuid().toString().equals(MY_UUID_DK_WRITE.toString()))
                {
                    mWrite = characteristic;

                }
                bluetoothSetManager.setCharacteristicNotification(characteristic,true);
            }
        }

    }
}
