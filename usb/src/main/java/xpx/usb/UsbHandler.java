package xpx.usb;

import android.os.Handler;
import android.os.Message;
import android.os.UserManager;

public class UsbHandler extends Handler {

    public static final int CHECK_USB_DEVICE = 1000;
    public static final int GET_DATA = 1001;
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what)
        {
            case CHECK_USB_DEVICE:
                XpxUsbManager.xpxUsbManager.checkUsbDevice();
                break;
            case GET_DATA:
                XpxUsbManager.xpxUsbManager.getOrgData((byte[]) msg.obj);
                XpxUsbManager.xpxUsbManager.getData((byte[]) msg.obj);
                break;
        }
    }
}
