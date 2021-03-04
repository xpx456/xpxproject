package jx.vein.javajar.devices.usb;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

public abstract class USBConnectStrategy {

    public static final Integer USBConnect_OK = 0;
    public static final Integer NO_USBDevice = -101;
    public static final Integer Cant_OPENDevice = -102;
    public static final Integer FAILED_INIT_DEVICE_HANDLE = -103;
    public static final Integer FAILED_GET_PERMISSION = -104;

    public abstract boolean filter(UsbDevice usbDevice);
    public abstract int connect(UsbManager usbManager, UsbDevice usbDevice);
    public abstract void close();


    public abstract String getPID();
    public abstract String getVID();
}
