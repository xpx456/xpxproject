package com.dk.dkphone;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.view.View;

import com.dk.dkphone.database.DBHelper;
import com.dk.dkphone.entity.TestItem;
import com.dk.dkphone.entity.User;
import com.dk.dkphone.entity.writecmd;
import com.dk.dkphone.view.DkPhoneApplication;
import com.dk.dkphone.view.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import xpx.bluetooth.BluetoothSetManager;
import xpx.usb.XpxUsbManager;

public class TestManager {


    public static final boolean TEST_MODE = true;
    public static final int  EVENT_CONNECT_TYPE = 300013;
    public static final int  EVENT_CODE_RECEIVE = 300014;
    public static final int  EVENT_CMD_SEND = 300015;
    public static final int  RECEIVE = 600;
    public static final int  SENDE = 1000;
    public static final  String PING = "123456";
    public static UUID MY_UUID_DK_READ = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    public static UUID MY_UUID_DK_WRITE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    public static UUID MY_UUID_DK_IMF = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public static final int SEND_DATA = 1000;
    public static final int GET_DATA = 1001;
    public static TestManager testManager;
    public TestHandler testHandler = new TestHandler();
    public int Testpersent = 30;
    public ArrayList<SendData> sendData = new ArrayList<SendData>();
    public TestItem test;
    public User user;
    public XpxUsbManager xpxUsbManager;
    public BluetoothSetManager bluetoothSetManager;
    public BluetoothGattCharacteristic mRead;
    public BluetoothGattCharacteristic mWrite;
    public int type = 0;
    public boolean leavelock = false;
    public int leavecode = 0;
    public String deviceMac;
    public ArrayList<String> datagetList = new ArrayList<>();
    public ArrayList<String> datagetList2 = new ArrayList<>();
    public ArrayList<byte[]> cmds = new ArrayList<byte[]>();
    public byte[] leavecmd = null;
    public int preset = -1;
    public int cmdsecond = 0;
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

        if(TestManager.TEST_MODE == false)
        {
            type = 0;
            if( checkDeviceConnect())
            {
                type = 1;
                Intent intent = new Intent(MainActivity.BLUE_STATE);
                intent.putExtra("blue",true);
                DkPhoneApplication.mApp.sendBroadcast(intent);
            }
            else
            {
                Intent intent = new Intent(MainActivity.BLUE_STATE);
                intent.putExtra("blue",false);
                DkPhoneApplication.mApp.sendBroadcast(intent);
            }
            if(xpxUsbManager.connect)
            {
                type = 2;
            }
            for (int i = 0; i < sendData.size(); i++) {
                if(type == 1 || type == 2)
                    sendData.get(i).sendDeviceConnect(true);
                else
                    sendData.get(i).sendDeviceConnect(false);
            }
        }
        testHandler.sendEmptyMessageDelayed(EVENT_CONNECT_TYPE,500);
    }

    public void codeReceive()
    {
//        if(leavetimetik > 0 )
//        {
//            leavetimetik--;
//        }
//        else
//        {
//            leavecode = 0;
//        }
        testHandler.sendEmptyMessageDelayed(EVENT_CODE_RECEIVE,RECEIVE);
    }

    public void sendCmds()
    {
//        if(cmds.size() > 0)
//        {
//            if(type == 2)
//            {
//                testManager.xpxUsbManager.sendData(cmds.get(0));
//                cmds.remove(0);
//            }
//            if(checkDeviceConnect())
//            {
//                if(mWrite != null)
//                {
//                    mWrite.setValue(cmds.get(0));
//                    if(bluetoothSetManager.hashConnect.get(deviceMac) != null)
//                    {
//                        bluetoothSetManager.hashConnect.get(deviceMac).writeCharacteristic(mWrite);
//                        cmds.remove(0);
//                    }
//
//                }
//            }
//        }


        if(type == 2)
        {
//            if(cmdsecond%2 == 0)
//            {
//                testManager.xpxUsbManager.sendData(xpxUsbManager.check);
//            }
//            else
//            {
//
//
//            }
            if(leavecmd != null)
            {
                testManager.xpxUsbManager.sendData(leavecmd);
                leavecmd = null;
            }
            else
            {
                testManager.xpxUsbManager.sendData(xpxUsbManager.check);
            }

        }
        else if(checkDeviceConnect())
        {
            if(mWrite != null)
            {

                if(bluetoothSetManager.hashConnect.get(deviceMac) != null)
                {
//                    if(cmdsecond%2 == 0)
//                    {
//                        mWrite.setValue(xpxUsbManager.check);
//                    }
//                    else
//                    {
//
//                    }
                    if(leavecmd != null)
                    {
                        mWrite.setValue(leavecmd);
                        leavecmd = null;
                    }
                    else
                    {
                        mWrite.setValue(xpxUsbManager.check);
                    }
                    bluetoothSetManager.hashConnect.get(deviceMac).writeCharacteristic(mWrite);
                }

            }
        }
        cmdsecond++;
        if(cmdsecond == 60*60*2*24)
        {
            cmdsecond = 0;
        }
        testHandler.sendEmptyMessageDelayed(EVENT_CMD_SEND,SENDE);
    }


    public void getData(String[] data)
    {

        data[1] = String.valueOf(0);
        if(testManager.xpxUsbManager.SPEED_MODE == 2)
        {
            if(data[5].equals("0"))
            {
                setData(data);
            }
            else
            {
                setErrorData(data);
            }

        }
        else{
            if(data[5].equals("0"))
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

    }

    public static TestManager init() {
        if (testManager == null) {
            testManager = new TestManager();
            testManager.xpxUsbManager = XpxUsbManager.init(DkPhoneApplication.mApp);
            testManager.xpxUsbManager.stopCheck();
            testManager.xpxUsbManager.getData = testManager.getData;
            testManager.bluetoothSetManager = BluetoothSetManager.init(DkPhoneApplication.mApp, testManager.blueToothApis,PING);
            testManager.deviceMac = testManager.bluetoothSetManager.getLast();
            testManager.checkConnectType();
            //testManager.codeReceive();
            testManager.sendCmds();
            //testManager.sendData();
        }
        return testManager;
    }

    public boolean checkDeviceConnect()
    {
        if(deviceMac.length() > 0)
        {
            return testManager.bluetoothSetManager.getDeviceConnect(deviceMac);
        }
        return false;
    }

    public void connectDevice(Context context, View view)
    {
        if(deviceMac.length() > 0)
        {
           testManager.bluetoothSetManager.autoConnectDevice(context,view,deviceMac);
        }
    }

    public void stopConnect() {
        testManager.bluetoothSetManager.connectStop(deviceMac);
    }

    public void connectDevice(Context context, View view,String mac)
    {
        if(mac.length() > 0)
        {
            deviceMac = mac;
            if(testManager.bluetoothSetManager.devices.containsKey(mac))
            testManager.bluetoothSetManager.autoConnectDevice(context,view,deviceMac);
            else
                testManager.bluetoothSetManager.autoFindConnectDevice(context,view,deviceMac);
        }
    }

//    public void setNewTest(Optation optation, User user) {
//        if (this.test != null) {
//            //DBHelper.getInstance(DkPadApplication.mApp).saveRecord(test);
//        }
//        this.test = praseOpData(optation);
//        this.test.oid = optation.oid;
//        this.user = user;
//        this.test.uid = this.user.uid;
//    }

    public void setNewTest(User user) {
        this.test = new TestItem();
        if (this.test != null) {
            DBHelper.getInstance(DkPhoneApplication.mApp).saveRecord(test);
        }
        this.user = user;
        this.test.uid = this.user.uid;
    }

//    public void setTest(TestItem test) {
//        this.test = test;
//        this.user = DkPadApplication.mApp.hashMap.get(test.uid);
//        try {
//            this.test.currentLeavel = this.test.hashopLeave.getInt(this.test.current / DkPadApplication.OPTATION_ITEM_PER_SECOND);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public void startTest() {
        int a = 100/ DkPhoneApplication.mApp.maxSelect;
        int b = test.currentLeavel/a;
        if(b > DkPhoneApplication.mApp.maxSelect)
        {
            b = DkPhoneApplication.mApp.maxSelect;
        }
        if(TEST_MODE == false)
        {

            if(type == 2)
            {
//                leavecode++;
//                testManager.xpxUsbManager.sendData(xpxUsbManager.setLeave(b));
            }
            else if(type == 1)
            {
//                leavecode++;
//                mWrite.setValue(xpxUsbManager.setLeave(b));
//                if(bluetoothSetManager.hashConnect.get(deviceMac) != null)
//                    bluetoothSetManager.hashConnect.get(deviceMac) .writeCharacteristic(mWrite);
            }
        }
        else
        {

        }

    }

    public void stopTest() {
        if(TEST_MODE == false)
        {
            if(type == 2)
            {
//                testManager.xpxUsbManager.sendData(xpxUsbManager.stop);
            }
            else if(type == 1)
            {
//                mWrite.setValue(xpxUsbManager.stop);
//                if(bluetoothSetManager.hashConnect.get(deviceMac) != null)
//                    bluetoothSetManager.hashConnect.get(deviceMac).writeCharacteristic(mWrite);
            }
        }



    }

    public void setLeavel(int leavel) {
        if(TEST_MODE == false)
        {
            if(testManager.xpxUsbManager.SPEED_MODE != 2)
            {
                int a = 100/ DkPhoneApplication.mApp.maxSelect;
                int b = leavel/a;
                if(b > DkPhoneApplication.mApp.maxSelect)
                {
                    b = DkPhoneApplication.mApp.maxSelect;
                }
                if(type == 2)
                {
//                    leavecode++;
//                    cmds.add(xpxUsbManager.setLeave(b));
                    leavecmd = xpxUsbManager.setLeave(b);
                    //testManager.xpxUsbManager.sendData(xpxUsbManager.setLeave(b));
                }
                else if(type == 1)
                {
//                    leavecode++;
//                    cmds.add(xpxUsbManager.setLeave(b));
                    leavecmd = xpxUsbManager.setLeave(b);
//                    mWrite.setValue(xpxUsbManager.setLeave(b));
//                    if(bluetoothSetManager.hashConnect.get(deviceMac) != null)
//                        bluetoothSetManager.hashConnect.get(deviceMac).writeCharacteristic(mWrite);
                }
            }
            else
            {
                int b = 0;
                b = leavel;
                if(leavel > 100)
                {
                    b = 100;
                }
                else if(b < 0)
                {
                    b = 0;
                }

                if(type == 2)
                {
//                    leavecode++;
                    preset = b;
                    leavecmd = xpxUsbManager.setLeave(b);
//                    cmds.add(xpxUsbManager.setLeave(b));
                    //testManager.xpxUsbManager.sendData(xpxUsbManager.setLeave(b));
                }
                else if(type == 1)
                {
//                    leavecode++;
                    preset = b;
                    leavecmd = xpxUsbManager.setLeave(b);
                    //cmds.add(xpxUsbManager.setLeave(b));
//                    mWrite.setValue(xpxUsbManager.setLeave(b));
//                    if(bluetoothSetManager.hashConnect.get(deviceMac) != null)
//                        bluetoothSetManager.hashConnect.get(deviceMac).writeCharacteristic(mWrite);

                    if(datagetList2.size() > 900)
                    {
                        datagetList2.remove(datagetList2.size()-1);
                    }
                    datagetList2.add(0,TimeUtils.getTimeSecond() +":"+ AppUtils.bytesToString2(xpxUsbManager.setLeave(b)));

                }
            }
        }
        else
        {

        }
    }

    public void addLeave() {
        if (test.currentLeavel < 100) {
            test.currentLeavel++;
        }
        setLeavel(test.currentLeavel);

    }

    public void setLeavestete(int leave)
    {
        if(leave < 100)
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
        if(TEST_MODE == false)
        {
//            if(type == 2)
//            {
//                testManager.xpxUsbManager.sendData(xpxUsbManager.check);
//            }
//            if(checkDeviceConnect())
//            {
//                if(mWrite != null)
//                {
//                    mWrite.setValue(xpxUsbManager.check);
//                    if(bluetoothSetManager.hashConnect.get(deviceMac) != null)
//                        bluetoothSetManager.hashConnect.get(deviceMac).writeCharacteristic(mWrite);
//                }
//            }
            //cmds.add(xpxUsbManager.check);
        }
        else
        {
            praseData4();
        }
        if(testHandler != null)
        {
            testHandler.removeMessages(SEND_DATA);
            testHandler.sendEmptyMessageDelayed(SEND_DATA, 1000);
        }

    }

    public void setData(String[] data) {
        if(test.isstart == true)
        {

            test.updateCurrent(data[2],true);
//            if (test.finish) {
//                for (int i = 0; i < sendData.size(); i++) {
//                    sendData.get(i).sendDataFinish(data);
//
//                }
//                test.recordData(data);
//                DBHelper.getInstance(DkPadApplication.mApp).saveRecord(test);
//
//            } else {
//                for (int i = 0; i < sendData.size(); i++) {
//                    sendData.get(i).sendData(data);
//
//                }
//                test.recordData(data);
//                DBHelper.getInstance(DkPadApplication.mApp).saveRecord(test);
//
//            }

            for (int i = 0; i < sendData.size(); i++) {
                sendData.get(i).sendData(data);

            }
            test.recordData(data);
            DBHelper.getInstance(DkPhoneApplication.mApp).saveRecord(test);
        }
        else{
            test.updateCurrent(data[2],true);
            for (int i = 0; i < sendData.size(); i++) {
                sendData.get(i).sendDeviceData(data);

            }
        }

    }

    public void setErrorData(String[] data) {
        if(test.isstart == true)
        {

            if(data[6].equals("0"))
                test.updateCurrent(data[2],false);
            else
                test.updateCurrent(data[2],true);
            test.recordData(data);
            DBHelper.getInstance(DkPhoneApplication.mApp).saveRecord(test);
        }
        for (int i = 0; i < sendData.size(); i++) {
            sendData.get(i).error(data);
        }

    }

//    public static TestItem praseOpData(Optation optation) {
//        TestItem testItem = new TestItem();
//        try {
//
//
//            JSONArray ja = new JSONArray(optation.data);
//            testItem.hashopLeave = ja;
//            if (testItem.hashopLeave.length() > 0) {
//                testItem.currentLeavel = testItem.hashopLeave.getInt(0);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return testItem;
//    }

//    public static TestItem praseOpData() {
//        TestItem testItem = new TestItem();
//        try {
//
//
//            JSONArray ja = new JSONArray(optation.data);
//            testItem.hashopLeave = ja;
//            if (testItem.hashopLeave.length() > 0) {
//                testItem.currentLeavel = testItem.hashopLeave.getInt(0);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return testItem;
//    }

//    public static void praseOpData(TestItem testItem, String optation) {
//        try {
//
//
//            JSONArray ja = new JSONArray(optation);
//            testItem.hashopLeave = ja;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    public static void praseStringData(TestItem testItem, String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            testItem.leavels = jsonObject.getJSONArray("leavel");
            testItem.herts = jsonObject.getJSONArray("hert");
            testItem.speeds = jsonObject.getJSONArray("speed");
            testItem.rpm = jsonObject.getJSONArray("rpm");
            testItem.carl = jsonObject.getJSONArray("carl");
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
            jsonObject.put("rpm", testItem.rpm);
            jsonObject.put("carl", testItem.carl);
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
                case EVENT_CODE_RECEIVE:
                    //codeReceive();
                    break;
                case EVENT_CMD_SEND:
                    sendCmds();
                    break;
            }

        }
    }

    public interface SendData {
        void sendDeviceConnect(boolean state);

        void sendDeviceData(String[] data);

        void sendData(String[] data);

        void sendDataFinish(String[] data);

        void error(String[] data);
    }

    public void prepareServiceData() {

        BluetoothGatt mBluetoothGatt = bluetoothSetManager.hashConnect.get(deviceMac);
        if(mBluetoothGatt != null)
        {
            List<BluetoothGattService> gattServices = mBluetoothGatt.getServices();
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
                        bluetoothSetManager.setCharacteristicNotification(characteristic,mBluetoothGatt,true);
                    }
                    else if(characteristic.getUuid().toString().equals(MY_UUID_DK_WRITE.toString()))
                    {
                        mWrite = characteristic;
                        bluetoothSetManager.setCharacteristicNotification(characteristic,mBluetoothGatt,true);
                    }
                    else if(characteristic.getUuid().toString().equals(MY_UUID_DK_IMF.toString()))
                    {
                        bluetoothSetManager.setCharacteristicNotification(characteristic,mBluetoothGatt,true);

                    }

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

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] array = characteristic.getValue();
            String b = new String(array);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            String b = AppUtils.bytesToString2(data);
            b = b.replaceAll("null","");
            bluetoothSetManager.buff += b;

            while (bluetoothSetManager.buff.length() >= 22 && bluetoothSetManager.buff.startsWith("55"))
            {
                String c = bluetoothSetManager.buff.substring(0,22);

                if(datagetList.size() > 900)
                {
                    datagetList.remove(datagetList.size()-1);
                }


                datagetList.add(0,TimeUtils.getTimeSecond()+"  "+ c);

                if(type == 1)
                {
                    if(data.length > 0)
                    {
                        if(XpxUsbManager.xpxUsbManager.SPEED_MODE == 1)
                        {
                            try {
                                praseData1(AppUtils.hex2byte(c));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else if(XpxUsbManager.xpxUsbManager.SPEED_MODE == 0)
                        {
                            try {
                                praseData2(AppUtils.hex2byte(c));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            try {
                                praseData3(AppUtils.hex2byte(c));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if(bluetoothSetManager.buff.length() > 22)
                {
                    bluetoothSetManager.buff = bluetoothSetManager.buff.substring(22,bluetoothSetManager.buff.length());
                    bluetoothSetManager.buff = bluetoothSetManager.buff.replaceAll("null","");
                }
                else
                    bluetoothSetManager.buff = "";

            }



        }
    };


    public void praseData1(byte[] data) {
        if(data[0] == 0x55)
        {
            if(data.length >= 9)
            {
                if(data[8] == 0x3f)
                {

                    if(data[1] == 0x25) {
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
                        rv = XpxUsbManager.DIRECT*XpxUsbManager.pie*5*50*36/10/v;

                        String[] dataget = new String[7];
                        DecimalFormat df = new DecimalFormat("#.00");
                        dataget[0] = String.valueOf(df.format(rv));
                        dataget[1] = "0";
                        dataget[2] = "0";
                        dataget[3] = String.valueOf(v);
                        dataget[4] = "0";
                        dataget[5] = "0";
                        if(data[1] == 0x15)
                        {
                            dataget[6] = "1";
                        }
                        else
                        {
                            dataget[6] = "0";
                        }
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
                            dataget[5] = "1";
                            if(bluetoothSetManager.deviceerror = false)
                                bluetoothSetManager.deviceerror = true;
                        }

                        if(getData != null)
                        {
                            getData.receiveData(dataget);
                        }
                    }
                    if(data[1] == 0x15)
                    {

                    }
                }
            }
        }
    }

    public void praseData2(byte[] data) {
        if(data[0] == 0x55)
        {
            if(data.length >= 11)
            {
                if(data[10] == 0x3f)
                {

                    if(data[1] == 0x25) {
                        String v1 = String.format("%02x", data[3]);
                        String v2 = String.format("%02x", data[2]);

                        String l1 = String.format("%02x", data[5]);
                        String l2 = String.format("%02x", data[4]);

                        String r1 = String.format("%02x", data[9]);
                        String r2 = String.format("%02x", data[8]);

                        String s = String.format("%02x", data[6]);
                        int d = Integer.valueOf(l1+l2,16);
                        int sate = Integer.valueOf(s,16);
                        int v = Integer.valueOf(v1+v2,16);
                        int r = Integer.valueOf(r1+r2,16);
                        String satue = Integer.toBinaryString(sate);
                        double rv = 0;
                        rv = v/10;

                        String[] dataget = new String[7];
                        DecimalFormat df = new DecimalFormat("#.00");
                        dataget[0] = String.valueOf(df.format(rv));
                        dataget[1] = "0";
                        dataget[2] = "0";
                        dataget[3] = String.valueOf(r);
                        dataget[4] = String.valueOf(v);
                        dataget[5] = "0";
                        if(data[1] == 0x15)
                        {
                            dataget[6] = "1";
                        }
                        else
                        {
                            dataget[6] = "0";
                        }
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
                            dataget[5] = "1";
                            if(bluetoothSetManager.deviceerror = false)
                                bluetoothSetManager.deviceerror = true;
                        }

                        if(getData != null)
                        {
                            getData.receiveData(dataget);
                        }
                    }
                    if(data[1] == 0x15)
                    {

                    }
                }
            }
        }
    }


    public void praseData3(byte[] data) {



        if(data[0] == 0x55)
        {
            if(data.length >= 11)
            {
                if(data[10] == 0x3f)
                {

                    if(data[1] == 0x25 || data[1] == 0x15) {
                        String v1 = String.format("%02x", data[3]);
                        String v2 = String.format("%02x", data[2]);

                        String l1 = String.format("%02x", data[5]);
                        String l2 = String.format("%02x", data[4]);

                        String r1 = String.format("%02x", data[9]);
                        String r2 = String.format("%02x", data[8]);

                        String p1 = String.format("%02x", data[7]);

                        String s = String.format("%02x", data[6]);
                        int d = Integer.valueOf(l1+l2,16);
                        int sate = Integer.valueOf(s,16);
                        int v = Integer.valueOf(v1+v2,16);
                        int r = Integer.valueOf(r1+r2,16);
                        int p = Integer.valueOf(p1,16);

                        String satue = Integer.toBinaryString(sate);
                        double rv = 0;
                        rv = v/10;

                        String[] dataget = new String[7];
                        DecimalFormat df = new DecimalFormat("#.00");
                        dataget[0] = String.valueOf(df.format(rv));
                        dataget[1] = "0";
                        dataget[2] = String.valueOf(p);
                        dataget[3] = String.valueOf(r);
                        dataget[4] = String.valueOf(v);
                        dataget[5] = "0";
                        if(data[1] == 0x15)
                        {
                            dataget[6] = "1";
                        }
                        else
                        {
                            dataget[6] = "0";
                        }

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
                            dataget[5] = "1";
                            if(bluetoothSetManager.deviceerror = false)
                                bluetoothSetManager.deviceerror = true;
                        }

                        if(getData != null)
                        {
                            getData.receiveData(dataget);
                        }


                    }
                    if(data[1] == 0x25)
                    {

                    }
                }
            }
        }
    }

    public void praseData4() {
        String[] dataget = new String[7];
        dataget[0] = "11.2";
        dataget[1] = "100";
        dataget[2] = String.valueOf(Testpersent);
        dataget[3] = "60";
        dataget[4] = "100";
        dataget[5] = "0";
        dataget[6] = "0";
        if(datagetList.size() > 900)
        {
            datagetList.remove(datagetList.size()-1);
        }

        {
            datagetList.add(0,TimeUtils.getTimeSecond()+"  "+ dataget[0]+"/"+dataget[1]+"/"+dataget[2]+"/"+dataget[3]+"/"+dataget[4]+"/"+dataget[5]);
        }

        getData.receiveData(dataget);
    }
}
