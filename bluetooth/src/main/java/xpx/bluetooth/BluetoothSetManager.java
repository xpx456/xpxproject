package xpx.bluetooth;

import android.bluetooth.BluetoothAdapter;
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
import android.os.Message;
import android.util.Log;
import android.view.View;


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

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public int mConnectionState = STATE_DISCONNECTED;

    public boolean hasgetservice = false;
    public BluetoothDevice netBluetoothDevice;
    public BluetoothGatt mBluetoothGatt;
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
    public BluetoothDevice bluetoothDevice;
    public BlueToothApis blueToothApis;
    public String connectMac = "";
    public String ping = "";
    public int findper = 1;
    public BlueConnectThread blueConnectThread;

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
                    //bluetoothSetManager.doDiscovery();
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

    public BluetoothSetManager(Context context, BlueToothApis blueToothApis) {
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
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
        if(connectView != null)
        {
            stopDisvery();
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
                    connectView.setmessage("正在查找设备蓝牙"+String.valueOf(findper)+"%");
                }
            }
        }
        else
        {
            findper++;
            if(connectView != null)
            connectView.setmessage("正在查找设备蓝牙"+String.valueOf(findper)+"%");
        }


    }

    public void connectDevice(BluetoothDevice device) {
        if(bluetoothDevice == null)
        {
            bluetoothDevice = device;
            if(bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED)
            {
                device.createBond();
            }
            else
            {
                unpairDevice(device);
            }
        }
        else
        {
            if(!bluetoothDevice.getAddress().equals(device.getAddress()))
            {
                bluetoothDevice = device;
                device.createBond();
            }
        }

    }

    public void autoConnectDevice(Context context,View location,BluetoothDevice device) {
        stopDisvery();
        if(bluetoothDevice == null)
        {
            bluetoothDevice = device;
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
            if(!bluetoothDevice.getAddress().equals(device.getAddress()))
            {
                bluetoothDevice = device;
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
        if(bluetoothDevice == null)
        {
            bluetoothDevice = device;

            if(blueConnectThread != null)
            {
                blueConnectThread.stop = true;
            }
            blueConnectThread = new BlueConnectThread(bluetoothSetManager,device);
            blueConnectThread.start();
        }
        else
        {
            if(!bluetoothDevice.getAddress().equals(device.getAddress()))
            {
                bluetoothDevice = device;
                if(blueConnectThread != null)
                {
                    blueConnectThread.stop = true;
                }
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

    public void updataFinish(String msg)
    {
        findaddress = "";
        findper = 1;
        if(connectView != null)
        {
            connectView.setTip(msg);
            connectView.hid();
        }
        SharedPreferences sharedPre = context.getSharedPreferences("bluetooth", 0);
        SharedPreferences.Editor editor = sharedPre.edit();
        if(netBluetoothDevice != null)
        {
            editor.putString("last",netBluetoothDevice.getAddress());
            editor.commit();
        }
        Intent intent = new Intent(ACTION_DEVICE_FINISH);
        context.sendBroadcast(intent);
    }


    public String getLast()
    {
        SharedPreferences sharedPre = context.getSharedPreferences("bluetooth", 0);
        return sharedPre.getString("last","");
    }

    public void reBond(BluetoothDevice device) {
        if(bluetoothDevice != null) {
            if(bluetoothDevice.getAddress().equals(device.getAddress()))
            {
                bluetoothDevice.createBond();
            }
        }
    }


    public final static BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                System.out.println("---------------------------->已经连接");
                bluetoothSetManager.hasgetservice = false;
                Intent intent = new Intent(ACTION_DEVICE_CONNECTED);
                bluetoothSetManager.mConnectionState = STATE_CONNECTED;
                bluetoothSetManager.context.sendBroadcast(intent);
                bluetoothSetManager.mBluetoothGatt.discoverServices();

            }
            // GATT Server disconnected
            else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                System.out.println("---------------------------->连接断开");
                Intent intent = new Intent(ACTION_DEVICE_DISCONNECT);
                bluetoothSetManager.mConnectionState = STATE_DISCONNECTED;
                bluetoothSetManager.context.sendBroadcast(intent);
                bluetoothSetManager.mBluetoothGatt.close();
                bluetoothSetManager.mBluetoothGatt = null;
//                BluetoothSetManager.getInstance().mBluetoothGatt.discoverServices();
            }
            // GATT Server disconnected
            else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                System.out.println("---------------------------->正在连接");
                Intent intent = new Intent(ACTION_DEVICE_CONNECTING);
                bluetoothSetManager.mConnectionState = STATE_CONNECTING;
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
        String currentDevAddress = device.getAddress();
        String currentDevName = device.getName();
        //如果是连接状态，断开，重新连接
        if (blueToothAdapter.isEnabled() == false) {
            blueToothAdapter.enable();
        }
        if (mConnectionState != STATE_DISCONNECTED || mBluetoothGatt != null) {
            if (mBluetoothGatt != null) {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
                mConnectionState = STATE_DISCONNECTED;
            } else {
                mConnectionState = STATE_DISCONNECTED;
            }
        }
        if (mBluetoothGatt != null)
            mBluetoothGatt.close();
        netBluetoothDevice = blueToothAdapter.getRemoteDevice(currentDevAddress);
        device = netBluetoothDevice;
        BleConnectThread mBleConnectThread = new BleConnectThread(context);
        mBleConnectThread.start();
        mConnectionState = STATE_CONNECTING;
    }

    private String mBtMac;

    public boolean connect(Context context,String btMac) {
        if (blueToothAdapter == null || btMac == null) {
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBtMac != null && btMac.equals(mBtMac)
                && mBluetoothGatt != null) {
            return mBluetoothGatt.connect();
        }

        BluetoothDevice device = blueToothAdapter.getRemoteDevice(btMac);
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
        mBtMac = btMac;

        return true;
    }

    public void  disconnect()
    {
        disConnectDevice();
        if(bluetoothDevice != null)
        {
            bluetoothSetManager.unpairDevice(bluetoothDevice);
        }
    }

    public void disConnectDevice() {

        if (mConnectionState != STATE_DISCONNECTED || mBluetoothGatt != null) {
            if(mBluetoothGatt != null)
            {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
            }
        }

    }



    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
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


    public void scanLeDevice() {
        stopLeScan();
        devices.clear();
        deviceslist.clear();
        Intent intent = new Intent(ACTION_CLEAN_DEVICE);
        context.sendBroadcast(intent);
        blueToothHandler.sendEmptyMessageDelayed(BlueToothHandler.EVENT_STOP_BLE_SCAN_DEVICE,10000);
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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                BluetoothLeScanner mBluetoothLeScanner = blueToothAdapter.getBluetoothLeScanner();
                mBluetoothLeScanner.startScan(mScanCallback);
            }
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
                stopLeScan();
                Message message = new Message();
                message.what = BlueToothHandler.EVENT_UN_FIND_DEVICE;
                blueToothHandler.sendMessage(message);
            }
        }
    };

    public void stopLeScan() {
        BluetoothLeScanner mBluetoothLeScanner = blueToothAdapter.getBluetoothLeScanner();
        mBluetoothLeScanner.stopScan(mScanCallback);
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

}
