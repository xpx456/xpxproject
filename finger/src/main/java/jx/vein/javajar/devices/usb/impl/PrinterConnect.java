package jx.vein.javajar.devices.usb.impl;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import jx.vein.javajar.devices.usb.USBConnectStrategy;

public class PrinterConnect extends USBConnectStrategy {
    private final static String VID = "28e9";
    private final static String PID = "289";

    private UsbEndpoint ep = null;
    private UsbInterface usbIf = null;
    private UsbDeviceConnection connection = null;

    @Override
    public boolean filter(UsbDevice usbDevice) {
        if (usbDevice == null) return false;
        return PID.equals( Integer.toHexString(usbDevice.getProductId()))
                && VID.equals( Integer.toHexString(usbDevice.getVendorId()));
    }

    @Override
    public int connect(UsbManager usbManager, UsbDevice usbDevice) {

        try {
            connection = usbManager.openDevice(usbDevice);
        } catch (Exception e) {
            return FAILED_GET_PERMISSION;
        }
        if (connection == null) {
            return Cant_OPENDevice;
        }

        this.usbIf = usbDevice.getInterface(0);
        if (this.usbIf.getEndpointCount() == 0) {

        }
        return USBConnect_OK;
    }

    @Override
    public void close() {
        if(this.connection != null) {
            this.connection.close();
            this.ep = null;
            this.usbIf = null;
            this.connection = null;
        }
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
