package com.dk.dkhome.utils;

import android.bluetooth.BluetoothDevice;
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

import com.dk.dkhome.R;
import com.dk.dkhome.entity.Course;
import com.dk.dkhome.entity.Device;
import com.dk.dkhome.entity.EquipData;
import com.dk.dkhome.view.DkhomeApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import xpx.bluetooth.BluetoothSetManager;
import xpx.usb.XpxUsbManager;

public class TestManager {


    public static final boolean TEST_MODE = false;
    public static final String ACTION_CONNECT_TYPE_CHANGE = "ACTION_CONNECT_TYPE_CHANGE";
    public static final int USB_CONNECT = 2;
    public static final int BLUE_CONNECT = 1;
    public static final int BOTH_CONNECT = 3;
    public static final int DIS_CONNECT = -1;
    public static final int EVENT_CONNECT_TYPE = 300013;
    public static final int EVENT_CODE_RECEIVE = 300014;
    public static final int EVENT_CMD_SEND = 300015;
    public static final int RECEIVE = 600;
    public static final int SENDE = 1000;
    public static UUID MY_UUID_DK_READ = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    public static UUID MY_UUID_DK_WRITE = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    public static UUID MY_UUID_DK_IMF = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public static final int SEND_DATA = 1000;
    public static final int GET_DATA = 1001;
    public static TestManager testManager;
    public XpxUsbManager xpxUsbManager;
    public BluetoothSetManager bluetoothSetManager;
    public BluetoothGattCharacteristic mRead;
    public BluetoothGattCharacteristic mWrite;
    public ArrayList<String> datagetList = new ArrayList<>();
    public ArrayList<String> datagetList2 = new ArrayList<>();
    public Context context;
    public int connectType = -1;
    public TestHandler testHandler = new TestHandler();
    public Device device = new Device();
    public boolean change = false;
    public long changecurrent;
    public boolean deviceerror = false;
    public int Testpersent = 30;
    public byte[] leavecmd = null;
    public boolean state = false;
    public boolean hertconnect = false;
    public int perleave = -1;
    public ArrayList<SendData> sendData = new ArrayList<SendData>();


    public static TestManager init(Context context) {
        if (testManager == null) {
            testManager = new TestManager();
            testManager.context = context;
            testManager.xpxUsbManager = XpxUsbManager.init(testManager.context);
            testManager.bluetoothSetManager = BluetoothSetManager.init(testManager.context, testManager.blueToothApis, "");
            testManager.xpxUsbManager.stopCheck();
            testManager.xpxUsbManager.getOrgData = testManager.getData;
            testManager.checkConnectType();
            testManager.sendCmds();
            if(TEST_MODE)
            {
                testManager.device.typeName = DkhomeApplication.mApp.getString(R.string.device_type_spinning_bike);
            }
        } else {
            testManager.context = context;
            testManager.xpxUsbManager = XpxUsbManager.init(testManager.context);
            testManager.bluetoothSetManager = BluetoothSetManager.init(testManager.context, testManager.blueToothApis, "");
            testManager.xpxUsbManager.stopCheck();
            testManager.xpxUsbManager.getOrgData = testManager.getData;
            testManager.checkConnectType();
            testManager.sendCmds();
            if(TEST_MODE)
            {
                testManager.device.typeName = DkhomeApplication.mApp.getString(R.string.device_type_spinning_bike);
            }
        }
        return testManager;
    }

    public XpxUsbManager.GetOrgData getData = new XpxUsbManager.GetOrgData() {
        @Override
        public void receiveData(byte[] data) {
            if(device != null)
            EquipData.praseData(data,device,testManager);
        }
    };

    public void checkConnectType() {

        if (TestManager.TEST_MODE == false) {
            int old = connectType;
            if (bluetoothSetManager.getDeviceConnect(device.deviceMac)) {
                switch (connectType) {
                    case DIS_CONNECT:
                        connectType = BLUE_CONNECT;
                        break;
                    case USB_CONNECT:
                        connectType = BOTH_CONNECT;
                        break;
                    default:
                        break;

                }
                state = true;
            } else {
                switch (connectType) {
                    case BLUE_CONNECT:
                        connectType = DIS_CONNECT;
                        break;
                    case BOTH_CONNECT:
                        connectType = USB_CONNECT;
                        ;
                        break;
                    default:
                        break;
                }
                state = false;
            }
            if (xpxUsbManager.connect) {
                if (connectType == BLUE_CONNECT) {
                    connectType = BOTH_CONNECT;
                } else if (connectType == DIS_CONNECT) {
                    connectType = USB_CONNECT;
                }
                state = true;
            } else {
                if (connectType == USB_CONNECT) {
                    connectType = DIS_CONNECT;
                } else if (connectType == BLUE_CONNECT) {
                    connectType = BOTH_CONNECT;
                }
            }
            if (old != connectType) {
                Intent intent = new Intent(ACTION_CONNECT_TYPE_CHANGE);
                context.sendBroadcast(intent);
            }

        }
        else
        {
            state = true;
        }
        testHandler.sendEmptyMessageDelayed(EVENT_CONNECT_TYPE, 500);
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
                case EVENT_CMD_SEND:
                    sendCmds();
                    break;
            }

        }
    }

    public void sendCmds() {
        if (TEST_MODE == false)
        {
            if (connectType == USB_CONNECT) {
                if (leavecmd != null) {
                    testManager.xpxUsbManager.sendData(leavecmd);
                    leavecmd = null;
                } else {
                    testManager.xpxUsbManager.sendData(EquipData.check);
                }

            } else if (connectType == BLUE_CONNECT || connectType == BOTH_CONNECT) {
                if (mWrite != null) {
                    if (bluetoothSetManager.hashConnect.get(device.deviceMac) != null) {
                        if (leavecmd != null) {
                            mWrite.setValue(leavecmd);
                            leavecmd = null;
                        } else {
                            mWrite.setValue(EquipData.check);
                        }
                        bluetoothSetManager.hashConnect.get(device.deviceMac).writeCharacteristic(mWrite);
                    }

                }
            }
            testHandler.sendEmptyMessageDelayed(EVENT_CMD_SEND, SENDE);
        }
        else
        {
            praseData4();
            testHandler.sendEmptyMessageDelayed(EVENT_CMD_SEND, SENDE);
        }


    }

    public void connectBlueDevice(Context context, String action, Device device) {
        this.device = device;
        if (device.deviceMac.length() > 0) {
            bluetoothSetManager.autoConnectDevice(context, action, device.deviceMac);
        }
    }

    public boolean chekcFind(String mac) {
        BluetoothDevice device = bluetoothSetManager.devices.get(mac);
        if(device == null)
            return false;
        else
            return true;
    }

    public Boolean chekcConnect(String mac) {
        return bluetoothSetManager.getDeviceConnect(mac);

    }

    public void stopConnect(Device device) {
        testManager.bluetoothSetManager.connectStop(device.deviceMac);
    }

    public void setLeavel(int leavel, Device device) {
        if (TEST_MODE == false) {
            perleave = leavel;
            if(device != null)
            {
                int b = EquipData.setLeave(leavel, device);
                if (connectType == USB_CONNECT) {
                    leavecmd = EquipData.setLeaveCmd(b, device);
                } else if (connectType == BOTH_CONNECT || connectType == USB_CONNECT) {
                    leavecmd = EquipData.setLeaveCmd(b, device);
                }
                if (datagetList2.size() > 900) {
                    datagetList2.remove(datagetList2.size() - 1);
                }
                datagetList2.add(0, TimeUtils.getTimeSecond() + ":" + AppUtils.bytesToString2(EquipData.setLeaveCmd(b,device)));
            }
        } else {
            Testpersent = leavel;
        }
    }

    public void getData(String[] data) {

        data[1] = String.valueOf(0);
        setData(data);

    }

    public void sendData() {
        if (TEST_MODE == false) {

        } else {
            praseData4();
        }
        if (testHandler != null) {
            testHandler.removeMessages(SEND_DATA);
            testHandler.sendEmptyMessageDelayed(SEND_DATA, 1000);
        }

    }

    public void setData(String[] data) {
        for (int i = 0; i < sendData.size(); i++) {
            sendData.get(i).sendData(data);

        }

    }



    public interface SendData {
        void sendData(String[] data);
    }

    public void prepareServiceData() {

        BluetoothGatt mBluetoothGatt = bluetoothSetManager.hashConnect.get(device.deviceMac);
        if (mBluetoothGatt != null) {
            List<BluetoothGattService> gattServices = mBluetoothGatt.getServices();
            if (gattServices == null)
                return;
            for (BluetoothGattService gattService : gattServices) {
                ArrayList<BluetoothGattCharacteristic> clist = new ArrayList<BluetoothGattCharacteristic>();
                clist.addAll(gattService.getCharacteristics());
                for (int j = 0; j < clist.size(); j++) {

                    BluetoothGattCharacteristic characteristic = clist.get(j);
                    if (characteristic.getUuid().toString().equals(MY_UUID_DK_READ.toString())) {
                        mRead = characteristic;
                        bluetoothSetManager.setCharacteristicNotification(characteristic, mBluetoothGatt, true);
                    } else if (characteristic.getUuid().toString().equals(MY_UUID_DK_WRITE.toString())) {
                        mWrite = characteristic;
                        bluetoothSetManager.setCharacteristicNotification(characteristic, mBluetoothGatt, true);
                    } else if (characteristic.getUuid().toString().equals(MY_UUID_DK_IMF.toString())) {
                        bluetoothSetManager.setCharacteristicNotification(characteristic, mBluetoothGatt, true);

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
            int a = 0;
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor characteristic) {
            int a = 0;
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
            b = b.replaceAll("null", "");
            bluetoothSetManager.buff += b;

            while (bluetoothSetManager.buff.length() >= 22 && bluetoothSetManager.buff.startsWith("55")) {
                String c = bluetoothSetManager.buff.substring(0, 22);
                if (datagetList.size() > 900) {
                    datagetList.remove(datagetList.size() - 1);
                }
                datagetList.add(0, TimeUtils.getTimeSecond() + "  " + c);
                if (connectType == USB_CONNECT || connectType == BOTH_CONNECT) {
                    if (data.length > 0) {
                        try {
                            EquipData.praseData(AppUtils.hex2byte(c),device,testManager);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (bluetoothSetManager.buff.length() > 22) {
                    bluetoothSetManager.buff = bluetoothSetManager.buff.substring(22, bluetoothSetManager.buff.length());
                    bluetoothSetManager.buff = bluetoothSetManager.buff.replaceAll("null", "");
                } else
                    bluetoothSetManager.buff = "";

            }


        }
    };


    public void praseData4() {
        String[] dataget = new String[8];
        Random a = new Random();
        double v = 10.2+a.nextInt(5);
        int rpm = (int) (v*10/36/(0.8*3.14)*60);
        dataget[0] = String.valueOf(v);
        dataget[1] = "100";
        dataget[2] = String.valueOf(Testpersent);
        dataget[3] = String.valueOf(rpm);
        dataget[4] = "100";
        dataget[5] = "0";
        dataget[6] = "0";
        dataget[7] = "0";
        if (datagetList.size() > 900) {
            datagetList.remove(datagetList.size() - 1);
        }

        {
            datagetList.add(0, TimeUtils.getTimeSecond() + "  " + dataget[0] + "/" + dataget[1] + "/" + dataget[2] + "/" + dataget[3] + "/" + dataget[4] + "/" + dataget[5]);
        }
        if (testHandler != null)
        {
            Message message = new Message();
            message.what = GET_DATA;
            message.obj = dataget;
            testHandler.sendMessage(message);
        }

    }


}
