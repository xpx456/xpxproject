package jx.vein.javajar.devices.usb.impl;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import jx.vein.javajar.devices.usb.USBConnectStrategy;

public class JXFVDConnect extends USBConnectStrategy {
    public static final String PID = "617";
    public static final String VID = "a7a9";
    public static UsbDeviceConnection connection;

    @Override
    public boolean filter(UsbDevice usbDevice) {
        if (usbDevice == null) return false;
        return PID.equals( Integer.toHexString(usbDevice.getProductId()))
                && VID.equals( Integer.toHexString(usbDevice.getVendorId()));
    }

    @Override
    public int connect(UsbManager usbManager, UsbDevice usbDevice) {
//        if (usbDevice == null) return NO_USBDevice;
//        JXFVJavaInterface jxfvJavaInterface = JXFVJavaInterface.getInstance();
//
//        try {
//            connection = usbManager.openDevice(usbDevice);
//        } catch (Exception e) {
//            return FAILED_GET_PERMISSION;
//        }
//        if (connection == null) {
//            return Cant_OPENDevice;
//        }
//        int fd = connection.getFileDescriptor();
//        if (JXFVJavaInterface.devHandle == 0) {
//            JXFVJavaInterface.devHandle = jxfvJavaInterface.jxInitUSBDriver();
//        }
//        return JXFVJavaInterface.devHandle == 0? FAILED_INIT_DEVICE_HANDLE : jxfvJavaInterface.jxConnFVD(JXFVJavaInterface.devHandle, fd);
        return FAILED_INIT_DEVICE_HANDLE;
    }

    @Override
    public void close() {
        if (connection == null) return;
        connection.close();
//        JXFVJavaInterface.devHandle = 0;
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
