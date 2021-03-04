package com.xpxbluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Message;
import android.view.View;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.receiver.listener.BluetoothBondListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BluetoothSetManager {

    public static final String ACTION_ADD_DEVICE = "ACTION_ADD_DEVICE";
    public static final String ACTION_CLEAN_DEVICE = "ACTION_CLEAN_DEVICE";
    public static final String ACTION_DEVICE_FINISH = "ACTION_DEVICE_FINISH";
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public static volatile BluetoothSetManager bluetoothSetManager;
    public Context context;
    public BluetoothClient mClient;
    public String ping = "";
    public HashMap<String, BluetoothDevice> devices = new HashMap<String, BluetoothDevice>();
    public ArrayList<BluetoothDevice> deviceslist = new ArrayList<BluetoothDevice>();
    public HashMap<String, XpxConnect> connecthash = new HashMap<String, XpxConnect>();
    public BlueToothApis blueToothApis;
    public boolean change = false;
    public long changecurrent = 0;
    public boolean deviceerror = false;
    public ConnectView connectView;
    public IntentFilter intentFilter = new IntentFilter();
    public BlueToothHandler blueToothHandler;
    public BluetoothReceiver bluetoothReceiver;

    public static BluetoothSetManager init(Context context, BlueToothApis blueToothApis, String ping) {
        if (bluetoothSetManager == null) {
            synchronized (BluetoothSetManager.class) {
                if (bluetoothSetManager == null) {
                    bluetoothSetManager = new BluetoothSetManager(blueToothApis);
                    bluetoothSetManager.blueToothHandler = new BlueToothHandler(bluetoothSetManager);
                    bluetoothSetManager.context = context;
                    bluetoothSetManager.ping = ping;
                    bluetoothSetManager.mClient = new BluetoothClient(context);
                    bluetoothSetManager.bluetoothReceiver = new BluetoothReceiver(bluetoothSetManager.blueToothHandler);
                    bluetoothSetManager.context.registerReceiver(bluetoothSetManager.bluetoothReceiver,bluetoothSetManager.intentFilter);
                    bluetoothSetManager.xpxSearchBluetooth();

                } else {
                    bluetoothSetManager.devices.clear();
                    bluetoothSetManager.deviceslist.clear();
                    bluetoothSetManager.blueToothHandler = new BlueToothHandler(bluetoothSetManager);
                    bluetoothSetManager.context = context;
                    bluetoothSetManager.ping = ping;
                    bluetoothSetManager.mClient = new BluetoothClient(context);
                    bluetoothSetManager.bluetoothReceiver = new BluetoothReceiver(bluetoothSetManager.blueToothHandler);
                    bluetoothSetManager.context.registerReceiver(bluetoothSetManager.bluetoothReceiver,bluetoothSetManager.intentFilter);
                    bluetoothSetManager.xpxSearchBluetooth();
                }
            }
        }
        return bluetoothSetManager;
    }

    public BluetoothSetManager(BlueToothApis blueToothApis) {
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        this.blueToothApis = blueToothApis;
    }

    public void xpxSearchBluetooth() {
        devices.clear();
        mClient.stopSearch();
        Intent intent = new Intent(ACTION_CLEAN_DEVICE);
        context.sendBroadcast(intent);
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(15000)
                .searchBluetoothClassicDevice(15000)
                .build();
        mClient.search(request, mySearchResponse);
    }

    public void xpxStopSearch() {
        mClient.stopSearch();
    }

    private final BluetoothBondListener mBluetoothBondListener = new BluetoothBondListener() {
        @Override
        public void onBondStateChanged(String mac, int bondState) {
            // bondState = Constants.BOND_NONE, BOND_BONDING, BOND_BONDED
            if (bondState == Constants.BOND_BONDED) {
                //xpxConnectBluetooth(mac);
            }
        }
    };


    private SearchResponse mySearchResponse = new SearchResponse() {

        @Override
        public void onSearchStarted() {

        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            if (!devices.containsKey(device.device.getAddress())) {
                devices.put(device.device.getAddress(), device.device);
                deviceslist.add(device.device);
                Intent intent = new Intent(ACTION_ADD_DEVICE);
                intent.putExtra("mac", device.getAddress());
                context.sendBroadcast(intent);
            }
        }

        @Override
        public void onSearchStopped() {

        }

        @Override
        public void onSearchCanceled() {

        }
    };


    public void xpxConnectBluetooth(XpxConnect connect) {
        mClient.stopSearch();
        mClient.disconnect(connect.deviceMac);
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(2)   // 连接如果失败重试3次
                .setConnectTimeout(10000)   // 连接超时30s
                .setServiceDiscoverRetry(2)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(10000)  // 发现服务超时20s
                .build();
        mClient.connect(connect.deviceMac, options, new MyBleConnectResponse(connect));
    }

    public void xpxStopConnect(XpxConnect connect) {
        if (connect != null) {
            Message msg = new Message();
            msg.obj = connect;
            msg.what = BlueToothHandler.CONNECT_DEVICE_STOP;
            blueToothHandler.sendMessage(msg);
        }
        mClient.disconnect(connect.deviceMac);
        connecthash.remove(connect.deviceMac);
    }

    private class MyBleConnectResponse implements BleConnectResponse {

        public XpxConnect xpxConnect;

        public MyBleConnectResponse(XpxConnect xpxConnect)
        {
            this.xpxConnect = xpxConnect;
        }


        @Override
        public void onResponse(int code, BleGattProfile data) {
            if (data != null) {
                if (xpxConnect != null) {
                    Message msg = new Message();
                    msg.obj = xpxConnect;
                    msg.what = BlueToothHandler.CONNECT_DEVICE_FINISH;
                    blueToothHandler.sendMessage(msg);
                }

                blueToothApis.measureService(data);
            }
            else
            {
                if (xpxConnect != null) {
                    Message msg = new Message();
                    msg.obj = xpxConnect;
                    msg.what = BlueToothHandler.CONNECT_DEVICE_STOP;
                    blueToothHandler.sendMessage(msg);
                }
            }
        }
    }

    public void connectUpdata(XpxConnect xpxConnect) {
        if (xpxConnect.persent < 99) {
            xpxConnect.persent++;
        }
        if (connectView != null) {
            connectView.setTip(String.valueOf(xpxConnect.persent));
        }
        Message msg = new Message();
        msg.obj = xpxConnect;
        msg.what = BlueToothHandler.CONNECT_DEVICE_UPDATA;
        if (blueToothHandler != null)
            blueToothHandler.sendMessageDelayed(msg, 200);
    }

    public void connectFinish(XpxConnect xpxConnect) {
        xpxConnect.state = STATE_CONNECTED;
        if (connectView != null) {
            connectView.hid();
        }
        if (blueToothHandler != null)
            blueToothHandler.removeMessages(BlueToothHandler.CONNECT_DEVICE_UPDATA);
        SharedPreferences sharedPre = context.getSharedPreferences("bluetooth", 0);
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putString("last", xpxConnect.deviceMac);
        editor.commit();
        Intent intent = new Intent(ACTION_DEVICE_FINISH);
        context.sendBroadcast(intent);
    }

    public void connectStop(XpxConnect xpxConnect) {
        mClient.getConnectStatus(xpxConnect.deviceMac);
        xpxConnect.state = STATE_DISCONNECTED;
        if (connectView != null) {
            connectView.hid();
        }
        if (blueToothHandler != null)
            blueToothHandler.removeMessages(BlueToothHandler.CONNECT_DEVICE_UPDATA);
    }

    public void connectStop(String mac) {
        XpxConnect connect = connecthash.get(mac);
        if(connect != null)
        {
            connectStop(connect);
        }
    }


    public void dismiss(XpxConnect xpxConnect) {
        if (xpxConnect.state == STATE_DISCONNECTED) {
            xpxStopConnect(xpxConnect);
        }
        if (blueToothHandler != null)
            blueToothHandler.removeMessages(BlueToothHandler.CONNECT_DEVICE_UPDATA);
    }

    public void xpxWrite(String mac,XpxBleGattCharacter write, byte[] bytes) {
        XpxConnect nowconnect = connecthash.get(mac);
        if (nowconnect != null)
            mClient.write(nowconnect.deviceMac, write.service, write.bleGattCharacter.getUuid(), bytes, writeResponse);
    }


    public void xpxNotify(String mac,XpxBleGattCharacter not) {
        XpxConnect nowconnect = connecthash.get(mac);
        if (nowconnect != null)
            mClient.notify(nowconnect.deviceMac, not.service, not.bleGattCharacter.getUuid(), bleNotifyResponse);
    }


    private BleReadResponse bleReadResponse = new BleReadResponse() {

        @Override
        public void onResponse(int code, byte[] data) {
            blueToothApis.onCharacteristicRead(code, data);
        }
    };

    public BleReadResponse bleReadResponse2 = new BleReadResponse() {

        @Override
        public void onResponse(int code, byte[] data) {
            blueToothApis.onDescriptorRead(code, data);
        }
    };

    private BleWriteResponse writeResponse = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            blueToothApis.onCharacteristicWrite(code);
        }
    };

    private BleWriteResponse bleWriteResponse2 = new BleWriteResponse() {

        @Override
        public void onResponse(int code) {
            blueToothApis.onDescriptorWrite(code);
        }
    };

    private BleNotifyResponse bleNotifyResponse = new BleNotifyResponse() {

        @Override
        public void onResponse(int code) {

        }

        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            blueToothApis.onCharacteristicChanged(service, character, value);
        }
    };

    public interface BlueToothApis {
        void measureService(BleGattProfile data);

        void onDescriptorWrite(int code);

        void onDescriptorRead(int code, byte[] data);

        void onCharacteristicRead(int code, byte[] data);

        void onCharacteristicWrite(int code);

        void onCharacteristicChanged(UUID service, UUID character, byte[] value);
    }


    public String getLast() {
        SharedPreferences sharedPre = context.getSharedPreferences("bluetooth", 0);
        return sharedPre.getString("last", "");
    }

    public void cleanLast() {
        SharedPreferences sharedPre = context.getSharedPreferences("bluetooth", 0);
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putString("last", "");
        editor.commit();
    }

    public void autoConnectDevice(Context context, View location, String mac) {

        if(context != null)
        {

            XpxConnect  connect = new XpxConnect(mac);
            connecthash.put(mac,connect);
            connectView = new ConnectView(context,connect,blueToothHandler);
            connectView.setTip(String.valueOf(connect.persent));
            connectView.creatView(location);
            xpxConnectBluetooth(connect);
        }
    }

    public boolean getDeviceConnect(String mac)
    {
        XpxConnect xpxConnect = connecthash.get(mac);
        if(xpxConnect != null)
        {
            if(xpxConnect.state == STATE_CONNECTED)
            {
                if(mClient.getConnectStatus(mac) == BluetoothProfile.STATE_CONNECTED)
                {
                    return true;
                }
                xpxConnect.state = STATE_DISCONNECTED;
            }
        }
        return false;
    }
}
