package jx.vein.javajar.devices.usb;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class USBHelper {
    private static final Integer MAX_RETRY_CNT = 3;
    private static final String ACTION_USB_PERMISSION = "jx.vein.javajar.USB_PERMISSION";

    private static UsbManager mUsbManager;
    private static Map<String, USBConnectStrategy> usbConnectStrategyMap = new ConcurrentHashMap<>();
    private static final USBHelper usbHelper = new USBHelper();

    public static USBHelper getInstance() {
        return usbHelper;
    }

    public Map<String, Integer> doConnects(Context context, List<USBConnectStrategy> usbConnectStrategies) {
        usbConnectStrategyMap.clear();
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();

        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0,
                new Intent(ACTION_USB_PERMISSION), 0);

        Map<String, Integer> deviceConnectionStatus = new ConcurrentHashMap<>();

        Iterator<USBConnectStrategy> connects = usbConnectStrategies.iterator();

        while (connects.hasNext()) {
            USBConnectStrategy connectStrategy = connects.next();
            usbConnectStrategyMap.put(getKeyFromConnect(connectStrategy), connectStrategy);
        }

        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            String deviceKey = getKeyFromDevice(device);
            USBConnectStrategy usbConnectStrategy = usbConnectStrategyMap.get(deviceKey);
            if (usbConnectStrategy == null) continue;

            for (int i=0; i<MAX_RETRY_CNT; i++) {
                if (mUsbManager.hasPermission(device)) {
                    break;
                }

                mUsbManager.requestPermission(device, mPermissionIntent);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (usbConnectStrategy.filter(device)) {
                deviceConnectionStatus.put(getKeyFromDevice(device)
                        , usbConnectStrategy.connect(mUsbManager, device));
            }
        }

        return deviceConnectionStatus;
    }

    public void closeAll(List<USBConnectStrategy> usbConnectStrategies) {
        for (USBConnectStrategy usbConnectStrategy : usbConnectStrategies) {
            usbConnectStrategy.close();
        }
    }

    public static String getKeyFromConnect(USBConnectStrategy usbConnectStrategy) {
        return usbConnectStrategy.getPID() + "@" + usbConnectStrategy.getVID();
    }

    public static String getKeyFromDevice(UsbDevice usbDevice) {
        return Integer.toHexString(usbDevice.getProductId()) + "@" + Integer.toHexString(usbDevice.getVendorId());
    }

}
