package jx.vein.javajar.devices.usb.impl;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.io.File;

import jx.vein.javajar.devices.usb.USBConnectStrategy;

public class QRBoxConnect extends USBConnectStrategy {
    public static final String PID = "5aa7";
    public static final String VID = "1fc9";

    public static UsbDeviceConnection connection;
    public static String devPath = null;

    @Override
    public boolean filter(UsbDevice usbDevice) {
        if (usbDevice == null) return false;
        return PID.equals( Integer.toHexString(usbDevice.getProductId()))
                && VID.equals( Integer.toHexString(usbDevice.getVendorId()));
    }

    @Override
    public int connect(UsbManager usbManager, UsbDevice usbDevice) {
        if (usbDevice == null) return NO_USBDevice;

        try {
            connection = usbManager.openDevice(usbDevice);
        } catch (Exception e) {
            return FAILED_GET_PERMISSION;
        }

        if (connection == null) {
            return Cant_OPENDevice;
        }

        devPath = GetTTYACMPath();
        return devPath != null ? USBConnect_OK : FAILED_INIT_DEVICE_HANDLE;
    }

    public String GetTTYACMPath() {
        String name = null;
        String INPUT_DIR = "/dev";
        String classPath = null;
        // get sensor class path

        File file = new File(INPUT_DIR);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            Log.d("HGT", "====files[i].getName(): " + files[i].getName());
            if (files[i].getName().contains("ttyACM")) {
                name = files[i].getAbsolutePath();
                return name;
            }
        }
        return null;
    }

    @Override
    public void close() {
        if (connection == null) return;
        connection.close();
        devPath = null;
    }

    @Override
    public String getPID() {
        return PID;
    }

    @Override
    public String getVID() {
        return VID;
    }
}
