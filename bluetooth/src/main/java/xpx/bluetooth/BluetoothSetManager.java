package xpx.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Message;
import android.util.Log;
import android.view.View;


import com.inuker.bluetooth.library.BluetoothClient;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import intersky.apputils.AppUtils;

public class BluetoothSetManager {

    public static final String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
    public static final String ACTION_DEVICE_CONNECTED = "ACTION_DEVICE_CONNECTED";
    public static final String ACTION_DEVICE_DISCONNECT = "ACTION_DEVICE_DISCONNECT";
    public static final String ACTION_DEVICE_CONNECTING = "ACTION_DEVICE_CONNECTING";
    public static final String ACTION_ADD_DEVICE = "ACTION_ADD_DEVICE";
    public static final String ACTION_CLEAN_DEVICE = "ACTION_CLEAN_DEVICE";
    public static final String ACTION_DEVICE_FINISH = "ACTION_DEVICE_FINISH";
    public static final String ACTION_DEVICE_CONNECT_FAIL= "ACTION_DEVICE_CONNECT_FAIL";
    public static final String ACTION_UPDATA_STATE = "ACTION_UPDATA_STATE";
    public static final String ACTION_NEW_APP_START_FIND = "ACTION_NEW_APP_START_FIND";
    public static final String ACTION_NEW_APP_START_CONNECT = "ACTION_NEW_APP_START_CONNECT";
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public String buff = "";
    public boolean hasgetservice = false;
//    public BluetoothDevice netBluetoothDevice;
    //public BluetoothGatt mBluetoothGatt;
    public HashMap<String,BluetoothGatt> hashConnect = new HashMap<String,BluetoothGatt>();
    public ConnectView connectView;
    public boolean change = false;
    public long changecurrent;
    public boolean deviceerror = false;
    public static volatile BluetoothSetManager bluetoothSetManager;
    public String findaddress = "";

//    public UUID MY_UUID_SECURE;

    public BlueToothHandler blueToothHandler;
    public Context context;
    public BluetoothManager mBluetoothManager;
    public BluetoothAdapter blueToothAdapter;
    public HashMap<String, BluetoothDevice> devices = new HashMap<String, BluetoothDevice>();
    public ArrayList<BluetoothDevice> deviceslist = new ArrayList<BluetoothDevice>();
    public BluetoothReceiver bluetoothReceiver;
    public IntentFilter intentFilter = new IntentFilter();
    public BlueToothApis blueToothApis;
    public String connectMac = "";
    public String ping = "";
    public int findper = 1;
    public BlueConnectThread blueConnectThread;
    public NewBlueConnectThread blueNewConnectThread;
    public BluetoothClient mClient;
    public static BluetoothSetManager init(Context context, BlueToothApis blueToothApis,String ping) {
        if (bluetoothSetManager == null) {
            synchronized (BluetoothSetManager.class) {
                if (bluetoothSetManager == null) {
                    bluetoothSetManager = new BluetoothSetManager(context, blueToothApis);
                    bluetoothSetManager.context = context;
                    bluetoothSetManager.ping = ping;
                    bluetoothSetManager.blueToothHandler = new BlueToothHandler(bluetoothSetManager);
                    bluetoothSetManager.bluetoothReceiver = new BluetoothReceiver(bluetoothSetManager.blueToothHandler);
                    bluetoothSetManager.context.registerReceiver(bluetoothSetManager.bluetoothReceiver,bluetoothSetManager.intentFilter);
                }
                else
                {
                    bluetoothSetManager.context = context;
                    bluetoothSetManager.ping = ping;
                    bluetoothSetManager.blueToothApis = blueToothApis;
                    bluetoothSetManager.devices = new HashMap<String, BluetoothDevice>();
                    bluetoothSetManager.mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
                    bluetoothSetManager.blueToothAdapter = bluetoothSetManager.mBluetoothManager.getAdapter();
                    bluetoothSetManager.blueToothHandler = new BlueToothHandler(bluetoothSetManager);
                    bluetoothSetManager.bluetoothReceiver = new BluetoothReceiver(bluetoothSetManager.blueToothHandler);
                    bluetoothSetManager.context.registerReceiver(bluetoothSetManager.bluetoothReceiver,bluetoothSetManager.intentFilter);
                    //bluetoothSetManager.doDiscovery();
                }
            }
        }
        return bluetoothSetManager;
    }


    public boolean getDeviceConnect(String mac)
    {
        if(mac == null)
        {
            return false;
        }

        BluetoothDevice bluetoothDevice = devices.get(mac);
        if(bluetoothDevice != null)
        {
            BluetoothGatt bluetoothGatt = hashConnect.get(mac);
            if(bluetoothGatt != null)
            {
                if(mBluetoothManager.getConnectionState(bluetoothDevice,BluetoothProfile.GATT)== BluetoothProfile.STATE_CONNECTED)
                    return true;
            }
        }
        return false;
    }

    public BluetoothSetManager(Context context, BlueToothApis blueToothApis) {
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothSetManager.ACTION_GATT_SERVICES_DISCOVERED);
        this.blueToothApis = blueToothApis;
        mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        blueToothAdapter = mBluetoothManager.getAdapter();
    }

    public void startConnectDeviceByMac(String mac) {
        connectMac = mac;
        doDiscovery();

    }

    public void doDiscovery() {
        if (blueToothAdapter.isEnabled() == false) {
            blueToothAdapter.enable();
        }
        devices.clear();
        deviceslist.clear();
        Intent intent = new Intent(ACTION_CLEAN_DEVICE);
        context.sendBroadcast(intent);
        blueToothAdapter.cancelDiscovery();
        blueToothAdapter.startDiscovery();

    }

    public void stopDisvery() {
        if (blueToothAdapter.isEnabled() == false) {
            blueToothAdapter.enable();
        }
        blueToothAdapter.cancelDiscovery();
    }

    public void unfindDevice()
    {
        stopDisvery();
        if(connectView != null)
        {
            findper = 1;
            findaddress = "";
            connectView.hidView();
            AppUtils.showMessage(connectView.context,"未发现设备蓝牙，请重启设备");
        }

    }

    public void addDevice(BluetoothDevice device) {

        if(!devices.containsKey(device.getAddress()))
        {
            devices.put(device.getAddress(),device);
            deviceslist.add(device);
            Intent intent = new Intent(ACTION_ADD_DEVICE);
            intent.putExtra("mac",device.getAddress());
            context.sendBroadcast(intent);
            //device.setPin(BluetoothSetManager.bluetoothSetManager.ping.getBytes());
            if(findaddress.length() > 0)
            {
                if(device.getAddress().equals(findaddress))
                {
                    autoConnectDevice(device);

                }
                else
                {
                    findper++;
                    if(connectView != null)
                    {
                        if(connectView.state == 0)
                        connectView.setmessage("正在查找设备蓝牙"+String.valueOf(findper)+"%");
                    }

                }
            }
        }
        else
        {
            findper++;
            if(connectView != null)
            {
                if(connectView.state == 0)
                connectView.setmessage("正在查找设备蓝牙"+String.valueOf(findper)+"%");
            }

        }


    }

    public void autoConnectDevice(Context context,View location,String mac) {
        stopDisvery();
        BluetoothDevice device = devices.get(mac);
        if(getDeviceConnect(mac) == false)
        {
            if(device == null)
            {
                if(context != null)
                {
                    connectView = new ConnectView(context);
                    connectView.creatView(location);
                    connectView.state = 1;
                    connectView.tip.setText(context.getString(R.string.connect_start)+"1%");
                }

                if(blueConnectThread != null)
                {
                    blueConnectThread.stop = true;
                }
                blueConnectThread = new BlueConnectThread(bluetoothSetManager,device);
                blueConnectThread.start();
            }
            else
            {
                if(context != null)
                {
                    connectView = new ConnectView(context);
                    connectView.creatView(location);
                    connectView.state = 1;
                    connectView.tip.setText(context.getString(R.string.connect_start)+"1%");
                }
                if(blueConnectThread != null)
                {
                    blueConnectThread.stop = true;
                }
                blueConnectThread = new BlueConnectThread(bluetoothSetManager,device);
                blueConnectThread.start();
            }
        }
    }

    public void autoConnectDevice(BluetoothDevice device) {
        stopDisvery();
        if(connectView != null)
            connectView.state = 1;
        if(device == null)
        {
            if(blueConnectThread != null)
            {
                blueConnectThread.stop = true;
            }
            blueConnectThread = new BlueConnectThread(bluetoothSetManager,device);
            blueConnectThread.start();
        }
        else
        {
            if(blueConnectThread == null)
            {
                blueConnectThread = new BlueConnectThread(bluetoothSetManager,device);
                blueConnectThread.start();
            }
        }
    }


    public void autoFindConnectDevice(Context context,View location,String findaddress) {

        if(context != null)
        {
            this.findaddress = findaddress;
            connectView = new ConnectView(context);
            connectView.creatView(location);
            connectView.tip.setText("正在查找设备蓝牙"+"1%");
            scanLeDevice();
        }
    }

    public void connectFail()
    {
        if(context != null)
        {
            findaddress = "";
            findper = 1;
            if(connectView != null)
            {
                connectView.setTip("连接失败");
                connectView.hid();
                Intent intent = new Intent(ACTION_DEVICE_CONNECT_FAIL);
                context.sendBroadcast(intent);
            }
        }
    }

    public void stopAuto() {
        if(blueConnectThread != null)
        blueConnectThread.stop = true;
    }

    public void updataHit(String msg)
    {
        if(connectView != null)
        {
            connectView.setTip(msg);
        }
    }

    public void updataFinish(BluetoothDevice device)
    {
        findaddress = "";
        findper = 1;
        if(connectView != null)
        {
            connectView.hid();
        }
        SharedPreferences sharedPre = context.getSharedPreferences("bluetooth", 0);
        SharedPreferences.Editor editor = sharedPre.edit();
        if(device != null)
        {
            editor.putString("last",device.getAddress());
            editor.commit();
        }
        Intent intent = new Intent(ACTION_DEVICE_FINISH);
        context.sendBroadcast(intent);
    }

    public void updatamewFinish(BluetoothDevice device)
    {
        SharedPreferences sharedPre = context.getSharedPreferences("bluetooth", 0);
        SharedPreferences.Editor editor = sharedPre.edit();
        if(device != null)
        {
            editor.putString("last",device.getAddress());
            editor.commit();
        }
        Intent intent = new Intent(ACTION_DEVICE_FINISH);
        context.sendBroadcast(intent);
    }

    public void cleanLast()
    {
        SharedPreferences sharedPre = context.getSharedPreferences("bluetooth", 0);
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putString("last","");
        editor.commit();
    }


    public String getLast()
    {
        SharedPreferences sharedPre = context.getSharedPreferences("bluetooth", 0);
        return sharedPre.getString("last","");
    }



    public final static BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                System.out.println("---------------------------->已经连接");
                bluetoothSetManager.hasgetservice = false;
                Intent intent = new Intent(ACTION_DEVICE_CONNECTED);
                bluetoothSetManager.context.sendBroadcast(intent);
                bluetoothSetManager.hashConnect.put(gatt.getDevice().getAddress(),gatt);
                gatt.discoverServices();

            }
            // GATT Server disconnected
            else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                System.out.println("---------------------------->连接断开");
                Intent intent = new Intent(ACTION_DEVICE_DISCONNECT);
                bluetoothSetManager.context.sendBroadcast(intent);
                bluetoothSetManager.hashConnect.remove(gatt.getDevice().getAddress());
                gatt.close();
//                BluetoothSetManager.getInstance().mBluetoothGatt.discoverServices();
            }
            // GATT Server disconnected
            else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                System.out.println("---------------------------->正在连接");
                Intent intent = new Intent(ACTION_DEVICE_CONNECTING);
                bluetoothSetManager.context.sendBroadcast(intent);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            // GATT Services discovered
            //发现新的服务
            if (status == BluetoothGatt.GATT_SUCCESS) {
                bluetoothSetManager.hasgetservice = true;
                BluetoothSetManager.bluetoothSetManager.
                        blueToothHandler.sendEmptyMessage(BlueToothHandler.EVENT_DISCOVER_SERVEICE);
            } else {

            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                System.out.println("onDescriptorWrite GATT_SUCCESS------------------->SUCCESS");
            } else if (status == BluetoothGatt.GATT_FAILURE) {
                System.out.println("onDescriptorWrite GATT_FAIL------------------->FAIL");
            }
            BluetoothSetManager.bluetoothSetManager.blueToothApis.onDescriptorRead(gatt,descriptor);
        }
        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {


            System.out.println("onDescriptorRead ------------------->GATT_SUCC");
            BluetoothSetManager.bluetoothSetManager.blueToothApis.onDescriptorRead(gatt,descriptor);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //write操作会调用此方法
            BluetoothSetManager.bluetoothSetManager.blueToothApis.onCharacteristicWrite(gatt,characteristic);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

            System.out.println("onCharacteristicWrite ------------------->read");
            BluetoothSetManager.bluetoothSetManager.blueToothApis.onCharacteristicRead(gatt,characteristic);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            /**
             * Android 底层貌似做了限制只能接受20个字节
             * There are four basic operations for moving data in BLE: read, write, notify, and indicate.
             * The BLE protocol specification requires that the maximum data payload size for these
             * operations is 20 bytes, or in the case of read operations, 22 bytes.
             * BLE is built for low power consumption, for infrequent short-burst data transmissions.
             * Sending lots of data is possible, but usually ends up being less efficient than classic
             * Bluetooth when trying to achieve maximum throughput.
             */
            System.out.println("onCharacteristicChanged -------------------> changed");
            //notify 会回调用此方法
            BluetoothSetManager.bluetoothSetManager.blueToothApis.onCharacteristicChanged(gatt,characteristic);
        }
    };

    public void connectDevice(BluetoothDevice device, Context context) {
        //如果是连接状态，断开，重新连接
        if (blueToothAdapter.isEnabled() == false) {
            blueToothAdapter.enable();
        }
        BluetoothGatt mBluetoothGatt = hashConnect.get(device.getAddress());
        if ( mBluetoothGatt != null) {
            if(mBluetoothManager.getConnectionState(device,BluetoothProfile.GATT)== BluetoothProfile.STATE_CONNECTED) {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
            } else {
            }
        }
        if (mBluetoothGatt != null)
            mBluetoothGatt.close();
        BleConnectThread mBleConnectThread = new BleConnectThread(context,device);
        mBleConnectThread.start();
    }


    public boolean connect(Context context,String btMac) {
        if (blueToothAdapter == null || btMac == null) {
            return false;
        }

        // Previously connected device.  Try to reconnect.
        BluetoothGatt mBluetoothGatt = hashConnect.get(btMac);
        if (mBluetoothGatt != null) {
            return mBluetoothGatt.connect();
        }

        BluetoothDevice device = blueToothAdapter.getRemoteDevice(btMac);
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
        return true;
    }


    public void connectStop(String mac) {
        BluetoothGatt bluetoothGatt = hashConnect.get(mac);
        if(bluetoothGatt != null)
        {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            hashConnect.remove(mac);
        }

    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,BluetoothGatt mBluetoothGatt, boolean enabled) {
        if (blueToothAdapter == null || mBluetoothGatt == null) {
            return;
        }
//        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        boolean isEnableNotification =  mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        if(isEnableNotification) {
            List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();
            if(descriptorList != null && descriptorList.size() > 0) {
                for(BluetoothGattDescriptor descriptor : descriptorList) {
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    mBluetoothGatt.writeDescriptor(descriptor);
                }
            }
        }
    }


    private static void broadcastNotifyUpdate(final BluetoothGattCharacteristic characteristic) {

    }

    public static final boolean isGpsEnable(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    public void scanLeDevice() {
        stopLeScan();
        devices.clear();
        deviceslist.clear();
        Intent intent = new Intent(ACTION_CLEAN_DEVICE);
        context.sendBroadcast(intent);
        blueToothHandler.sendEmptyMessageDelayed(BlueToothHandler.EVENT_STOP_BLE_SCAN_DEVICE,20000);
        if(blueToothAdapter.isEnabled())
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                BluetoothLeScanner mBluetoothLeScanner = blueToothAdapter.getBluetoothLeScanner();
                mBluetoothLeScanner.startScan(mScanCallback);
            }
        }
        else
        {
            blueToothAdapter.isEnabled();
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                BluetoothLeScanner mBluetoothLeScanner = blueToothAdapter.getBluetoothLeScanner();
//                mBluetoothLeScanner.startScan(mScanCallback);
//            }
        }
    }

    public ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if(result != null){

                BluetoothDevice device = result.getDevice();
                Message message = new Message();
                message.what = BlueToothHandler.EVENT_FIND_DEVICE;
                message.obj = device;
                blueToothHandler.sendMessage(message);
            }
            else
            {
                Message message = new Message();
                message.what = BlueToothHandler.EVENT_UN_FIND_DEVICE;
                blueToothHandler.sendMessage(message);
            }
        }
    };

    public void stopLeScan() {
        if(blueToothAdapter.isEnabled() == false)
        {
            blueToothAdapter.enable();
        }
        if(blueToothAdapter != null)
        {
            if(blueToothAdapter.isEnabled() == true)
            {
                BluetoothLeScanner mBluetoothLeScanner = blueToothAdapter.getBluetoothLeScanner();
                mBluetoothLeScanner.stopScan(mScanCallback);
            }
        }

    }

    public void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.setAccessible(true);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e("ble",e.toString());
        }
    }

    public interface BlueToothApis
    {
        void measureService();
        void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);
        void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor characteristic);
        void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);
        void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);
        void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);
    }


    //for new app
    public void scanLeDevice(String mac,String action) {
        stopLeScan();
        Intent intent = new Intent(ACTION_CLEAN_DEVICE);
        context.sendBroadcast(intent);
        blueToothHandler.sendEmptyMessageDelayed(BlueToothHandler.EVENT_STOP_BLE_SCAN_DEVICE,20000);
        if(blueToothAdapter.isEnabled())
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                BluetoothLeScanner mBluetoothLeScanner = blueToothAdapter.getBluetoothLeScanner();
                mBluetoothLeScanner.startScan(new MyScanCallback(mac,action));
            }
        }
        else
        {
            blueToothAdapter.isEnabled();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                BluetoothLeScanner mBluetoothLeScanner = blueToothAdapter.getBluetoothLeScanner();
                mBluetoothLeScanner.startScan(new MyScanCallback(mac,action));
            }
        }
    }

    public void autoConnectDevice(Context context,String action,String mac) {
        stopDisvery();
        BluetoothDevice device = devices.get(mac);
        if(getDeviceConnect(mac) == false)
        {
            if(device == null)
            {
                Intent intent = new Intent(ACTION_NEW_APP_START_FIND);
                if(context != null)
                {
                    context.sendBroadcast(intent);
                    scanLeDevice(mac,action);
                }
            }
            else
            {
                if(blueNewConnectThread != null)
                {
                    blueNewConnectThread.stop = true;
                }
                blueNewConnectThread = new NewBlueConnectThread(bluetoothSetManager,device,action);
                blueNewConnectThread.start();
            }
        }
    }

    class MyScanCallback extends ScanCallback {

        public String mac = "";
        public String action = "";
        public MyScanCallback(String mac,String action) {
            this.mac = mac;
            this.action = action;
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if(result != null){

                BluetoothDevice device = result.getDevice();
                if(device.getAddress().equals(mac))
                {
                    Message message = new Message();
                    message.what = BlueToothHandler.EVENT_FIND_DEVICE;
                    message.obj = device;
                    blueToothHandler.sendMessage(message);
                    if(blueNewConnectThread != null)
                    {
                        blueNewConnectThread.stop = true;
                    }
                    stopDisvery();
                    blueNewConnectThread = new NewBlueConnectThread(bluetoothSetManager,device,action);
                    blueConnectThread.start();
                }
            }
            else
            {
                stopDisvery();
                Intent intent1 = new Intent(action);
                intent1.putExtra("connect",false);
                if(context != null)
                    context.sendBroadcast(intent1);

            }
        }
    };
}
