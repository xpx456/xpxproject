package com.xpxbluetooth;

import android.bluetooth.BluetoothDevice;

public class XpxConnect {

    public int state = BluetoothSetManager.STATE_DISCONNECTED;
    public int persent = 0;
    public String deviceMac = "";
    public BluetoothDevice bluetoothDevice;
    public XpxConnect(String mac)
    {
        deviceMac = mac;
    }
}
