package com.dk.dktest.view;

import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.SharedPreferences;

import com.dk.dktest.entity.TestRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import intersky.appbase.AppActivityManager;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetUtils;
import xpx.bluetooth.BluetoothSetManager;
import xpx.bluetooth.BluetoothUtils;


public class DkTestApplication extends Application {

    public static UUID MY_UUID_DK_READ = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    public static UUID MY_UUID_DK_WRITE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    public static UUID MY_UUID_DK_IMF = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public static final  String PING = "123456";
    public AppActivityManager appActivityManager;
    public NetUtils netUtils;
    public static DkTestApplication mApp;
    public String clidenid;
    public BluetoothSetManager bluetoothSetManager;
    public BluetoothGattCharacteristic mRead;
    public BluetoothGattCharacteristic mWrite;
    public ArrayList<GetData> getDatas = new ArrayList<GetData>();
    public ArrayList<TestRecord> testRecords = new ArrayList<>();
    public ArrayList<TestRecord> testSRecords = new ArrayList<>();
    public String buff = "";
    public void onCreate() {
        mApp = this;
        appActivityManager = AppActivityManager.getAppActivityManager(mApp);
        netUtils = NetUtils.init(mApp);
        bluetoothSetManager = BluetoothSetManager.init(mApp, blueToothApis,PING);
        getClientid();
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
            int a=0;
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor characteristic) {
            int a=0;
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] array = characteristic.getValue();
            String b = new String(array);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] array = characteristic.getValue();
            String b = new String(array);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] array = characteristic.getValue();
            String b = new String(array);
            buff += b;
            if(buff.contains("\r\n"))
            {
                String c = buff.substring(0,buff.indexOf("\r\n")+2);
                for(int i = 0 ; i < getDatas.size(); i++)
                {
                    getDatas.get(i).getData(praseData(c));
                }
                buff = buff.substring(buff.indexOf("\r\n")+2,buff.length());
                String d = buff;

            }
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
                    bluetoothSetManager.setCharacteristicNotification(characteristic,true);
                }
                else if(characteristic.getUuid().toString().equals(MY_UUID_DK_WRITE.toString()))
                {
                    mWrite = characteristic;
                    bluetoothSetManager.setCharacteristicNotification(characteristic,true);
                }
                else if(characteristic.getUuid().toString().equals(MY_UUID_DK_IMF.toString()))
                {
                    bluetoothSetManager.setCharacteristicNotification(characteristic,true);

                }

            }
        }
    }

    public boolean wridtCmd(String data)
    {
        if(mWrite != null)
        {
            mWrite.setValue(data.getBytes());
            boolean start = bluetoothSetManager.mBluetoothGatt.writeCharacteristic(mWrite);
            return start;
        }
        return false;
    }

    public ArrayList<String[]> praseData(String data)
    {
        String[] value = data.split(";");
        ArrayList<String[]> items = new ArrayList<String[]>();
        for(int i = 0 ; i < value.length-1;i++)
        {

            String[] item = value[i].split(",");
            items.add(item);
        }
        return items;
    }

    public interface GetData
    {
        public void getData(ArrayList<String[]> data);
    }
}
