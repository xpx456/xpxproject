package com.dk.dkphone;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.dk.dkphone.database.DBHelper;
import com.dk.dkphone.entity.Optation;
import com.dk.dkphone.entity.TestItem;
import com.dk.dkphone.entity.User;
import com.dk.dkphone.view.DkPadApplication;
import com.dk.dkphone.view.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import intersky.apputils.AppUtils;
import xpx.bluetooth.BluetoothSetManager;
import xpx.usb.XpxUsbManager;

public class TestManager {
    public static final int  EVENT_CONNECT_TYPE = 300013;
    public static final  String PING = "123456";
    public static UUID MY_UUID_DK_READ = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    public static UUID MY_UUID_DK_WRITE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    public static UUID MY_UUID_DK_IMF = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public static final int SEND_DATA = 1000;
    public static final int GET_DATA = 1001;
    public static TestManager testManager;
    public TestHandler testHandler = new TestHandler();

    public ArrayList<SendData> sendData = new ArrayList<SendData>();
    public TestItem test;
    public User user;
    public XpxUsbManager xpxUsbManager;
    public BluetoothSetManager bluetoothSetManager;
    public BluetoothGattCharacteristic mRead;
    public BluetoothGattCharacteristic mWrite;
    public int type = 0;
    public boolean blueconnect = false;

    public XpxUsbManager.GetData getData = new XpxUsbManager.GetData() {
        @Override
        public void receiveData(String[] data) {
            Message msg = new Message();
            msg.obj = data;
            msg.what = GET_DATA;
            if(testHandler != null)
            testHandler.sendMessage(msg);
        }
    };

    public void checkConnectType() {
        type = 0;
        if(bluetoothSetManager.bluetoothDevice != null)
        {
            if( bluetoothSetManager.bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED
                    && DkPadApplication.mApp.testManager.bluetoothSetManager.mConnectionState == BluetoothSetManager.STATE_CONNECTED)
            {
                type = 1;
                blueconnect = true;
                Intent intent = new Intent(MainActivity.BLUE_STATE);
                intent.putExtra("blue",true);
                DkPadApplication.mApp.sendBroadcast(intent);
            }
            else
            {
                blueconnect = false;
                Intent intent = new Intent(MainActivity.BLUE_STATE);
                intent.putExtra("blue",false);
                DkPadApplication.mApp.sendBroadcast(intent);
            }
        }


        if(xpxUsbManager.connect)
        {
            type = 2;
        }

        testHandler.sendEmptyMessageDelayed(EVENT_CONNECT_TYPE,100);
    }


    public void getData(String[] data)
    {

        data[1] = String.valueOf(0);
        if(data[2].equals("0"))
        {
            data[2] = String.valueOf(test.currentLeavel);
            setData(data);
        }
        else
        {
            data[2] = String.valueOf(test.currentLeavel);
            setErrorData(data);
        }

    }

    public static TestManager init() {
        if (testManager == null) {
            testManager = new TestManager();
            testManager.xpxUsbManager = XpxUsbManager.init(DkPadApplication.mApp);
            testManager.xpxUsbManager.stopCheck();
            testManager.xpxUsbManager.getData = testManager.getData;
            testManager.bluetoothSetManager = BluetoothSetManager.init(DkPadApplication.mApp, testManager.blueToothApis,PING);
            testManager.checkConnectType();
        }
        return testManager;
    }

    public void setNewTest(Optation optation, User user) {
        if (this.test != null) {
            //DBHelper.getInstance(DkPadApplication.mApp).saveRecord(test);
        }
        this.test = praseOpData(optation);
        this.test.oid = optation.oid;
        this.user = user;
        this.test.uid = this.user.uid;
    }

    public void setTest(TestItem test) {
        this.test = test;
        this.user = DkPadApplication.mApp.hashMap.get(test.uid);
        try {
            this.test.currentLeavel = this.test.hashopLeave.getInt(this.test.current / DkPadApplication.OPTATION_ITEM_PER_SECOND);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startTest() {
        int a = 100/DkPadApplication.mApp.maxSelect;
        int b = test.currentLeavel/a;
        if(b > DkPadApplication.mApp.maxSelect)
        {
            b = DkPadApplication.mApp.maxSelect;
        }
        if(type == 2)
        {
            testManager.xpxUsbManager.sendData(xpxUsbManager.setLeave(b));
            sendData();
        }
        else if(type == 1)
        {

            mWrite.setValue(xpxUsbManager.setLeave(b));
            bluetoothSetManager.mBluetoothGatt.writeCharacteristic(mWrite);
            sendData();
        }

    }

    public void stopTest() {
        if(type == 2)
        {
            testManager.xpxUsbManager.sendData(xpxUsbManager.stop);
            testHandler.removeMessages(SEND_DATA);
        }
        else if(type == 1)
        {
            mWrite.setValue(xpxUsbManager.stop);
            bluetoothSetManager.mBluetoothGatt.writeCharacteristic(mWrite);
            testHandler.removeMessages(SEND_DATA);
        }


    }

    public void setLeavel(int leavel) {
        int a = 100/DkPadApplication.mApp.maxSelect;
        int b = leavel/a;
        if(b > DkPadApplication.mApp.maxSelect)
        {
            b = DkPadApplication.mApp.maxSelect;
        }
        if(type == 2)
        {

            testManager.xpxUsbManager.sendData(xpxUsbManager.setLeave(b));
        }
        else if(type == 1)
        {
            mWrite.setValue(xpxUsbManager.setLeave(b));
            bluetoothSetManager.mBluetoothGatt.writeCharacteristic(mWrite);
        }

    }

    public void addLeave() {
        if (test.currentLeavel <= 100) {
            test.currentLeavel++;
        }
        setLeavel(test.currentLeavel);

    }

    public void setLeavestete(int leave)
    {
        if(leave <= 100)
        {
            test.currentLeavel = leave;
        }
        else
        {
            test.currentLeavel = 100;
        }
        setLeavel(test.currentLeavel);
    }

    public void desLeave() {
        if (test.currentLeavel > 1) {
            test.currentLeavel--;
        }
        setLeavel(test.currentLeavel);
    }

    public void sendData() {
        if (test.isstart) {
            if(type == 2)
            {
                testManager.xpxUsbManager.sendData(xpxUsbManager.check);
                testHandler.removeMessages(SEND_DATA);
                testHandler.sendEmptyMessageDelayed(SEND_DATA, 1000);
            }
            else if(type == 1)
            {
                mWrite.setValue(xpxUsbManager.check);
                bluetoothSetManager.mBluetoothGatt.writeCharacteristic(mWrite);
                testHandler.removeMessages(SEND_DATA);
                testHandler.sendEmptyMessageDelayed(SEND_DATA, 1000);
            }

        } else {

        }
    }

    public void setData(String[] data) {
        if(test.isstart == true)
        {
            test.updateLeave();
            if (test.finish) {
                for (int i = 0; i < sendData.size(); i++) {
                    sendData.get(i).sendDataFinish(data);

                }
                test.recordData(data);
                DBHelper.getInstance(DkPadApplication.mApp).saveRecord(test);

            } else {
                for (int i = 0; i < sendData.size(); i++) {
                    sendData.get(i).sendData(data);

                }
                test.recordData(data);
                DBHelper.getInstance(DkPadApplication.mApp).saveRecord(test);

            }
        }

    }

    public void setErrorData(String[] data) {
        for (int i = 0; i < sendData.size(); i++) {
            sendData.get(i).error(data);

        }

    }

    public static TestItem praseOpData(Optation optation) {
        TestItem testItem = new TestItem();
        try {


            JSONArray ja = new JSONArray(optation.data);
            testItem.hashopLeave = ja;
            if (testItem.hashopLeave.length() > 0) {
                testItem.currentLeavel = testItem.hashopLeave.getInt(0);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return testItem;
    }

    public static void praseOpData(TestItem testItem, String optation) {
        try {


            JSONArray ja = new JSONArray(optation);
            testItem.hashopLeave = ja;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void praseStringData(TestItem testItem, String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            testItem.leavels = jsonObject.getJSONArray("leavel");
            testItem.herts = jsonObject.getJSONArray("hert");
            testItem.speeds = jsonObject.getJSONArray("speed");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String initStringData(TestItem testItem) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("leavel", testItem.leavels);
            jsonObject.put("hert", testItem.herts);
            jsonObject.put("speed", testItem.speeds);
            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    public class TestHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SEND_DATA:
                    sendData();
                    break;
                case GET_DATA:
                    getData((String[]) msg.obj);
                    break;
                case EVENT_CONNECT_TYPE:
                    checkConnectType();
                    break;
            }

        }
    }

    public interface SendData {
        void sendData(String[] data);

        void sendDataFinish(String[] data);

        void error(String[] data);
    }

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
            byte[] data = characteristic.getValue();
            if(data.length > 0)
            {
                if(data[0] == 0x55)
                {
                    if(data.length >= 9)
                    {
                        if(data[8] == 0x3f)
                        {
                            if(data[1] == 0x15 || data[1] == 0x25) {
                                String v1 = String.format("%02x", data[3]);
                                String v2 = String.format("%02x", data[2]);

                                String l1 = String.format("%02x", data[5]);
                                String l2 = String.format("%02x", data[4]);

                                String s = String.format("%02x", data[6]);
                                int d = Integer.valueOf(l1+l2,16);
                                int sate = Integer.valueOf(s,16);
                                int v = Integer.valueOf(v1+v2,16);
                                String satue = Integer.toBinaryString(sate);
                                double rv = 0;
                                if(v > 0 )
                                {
                                    rv = XpxUsbManager.DIRECT*XpxUsbManager.pie*5*50/v;
                                }

                                String[] dataget = new String[3];
                                DecimalFormat df = new DecimalFormat("#.00");
                                dataget[0] = String.valueOf(df.format(rv));
                                dataget[1] = "0";
                                dataget[2] = "0";
                                if(satue.startsWith("1"))
                                {
                                    if(bluetoothSetManager.change == false)
                                    {
                                        bluetoothSetManager.change = true;
                                        bluetoothSetManager.changecurrent = System.currentTimeMillis();
                                    }
                                }
                                else
                                {
                                    bluetoothSetManager.change = false;
                                }

                                if((System.currentTimeMillis() - bluetoothSetManager.changecurrent) > XpxUsbManager.CHANGE_TIME && bluetoothSetManager.change == true)
                                {
                                    dataget[2] = "1";
                                    if(bluetoothSetManager.deviceerror = false)
                                        bluetoothSetManager.deviceerror = true;
                                }

                                if(getData != null)
                                {
                                    getData.receiveData(dataget);
                                }
                            }
                        }
                    }
                }
            }
        }
    };
}
