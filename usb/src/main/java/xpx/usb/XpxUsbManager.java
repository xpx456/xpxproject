package xpx.usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Message;
import android.widget.PopupWindow;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import intersky.apputils.AppUtils;
import xpx.usb.driver.UsbSerialDriver;
import xpx.usb.driver.UsbSerialPort;
import xpx.usb.driver.UsbSerialProber;
import xpx.usb.util.SerialInputOutputManager;

public class XpxUsbManager {

    public static final double pie = 3.1415;
    public static final double DIRECT = 0.8;
    public static final long CHANGE_TIME = 10000;
    public static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";
    public byte [] start = {0x25, 0x03, 0x12, 0x00, (byte) 0xa0};
    public byte [] stop = {0x25, 0x09, 0x00, 0x00, (byte) 0xa0};
    public byte [] check = {0x25, 0x08, 0x00, 0x00, (byte) 0xa0};
    public volatile static XpxUsbManager xpxUsbManager = null;
    public Context context;
    public UsbManager mUsbManager;
    public UsbDeviceConnection connection;
    public UsbSerialPort sPort;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
    public boolean connect = false;
    public UsbHandler usbHandler = new UsbHandler();
    public GetData getData;
    public boolean change = false;
    public long changecurrent;
    public boolean deviceerror = false;

    public byte[] setLeave(int leave)
    {
        BigInteger target = new BigInteger(String.valueOf(leave));
        long b = Long.parseLong(String.valueOf(leave));
        String a = Long.toHexString(b);
        if(a.length() == 1)
        {
            a = "0"+a;
        }
        String com = "2503"+a+"00a0";
        try {
            return AppUtils.hex2byte(com);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static XpxUsbManager init(Context context) {

        if (xpxUsbManager == null) {
            synchronized (XpxUsbManager.class) {
                if (xpxUsbManager == null) {
                    xpxUsbManager = new XpxUsbManager();
                    xpxUsbManager.context = context;
                    xpxUsbManager.mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
                    xpxUsbManager.checkUsbDevice();
                } else {
                    xpxUsbManager.context = context;
                    xpxUsbManager.mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
                    xpxUsbManager.checkUsbDevice();
                }
            }
        }
        return xpxUsbManager;
    }

    public void stopCheck()
    {
        usbHandler.removeMessages(UsbHandler.CHECK_USB_DEVICE);
    }

    public void checkUsbDevice()
    {
        if(connect == false)
        {
            initUsb();
        }
        if(usbHandler != null)
        {
            usbHandler.removeMessages(UsbHandler.CHECK_USB_DEVICE);
            usbHandler.sendEmptyMessageDelayed(UsbHandler.CHECK_USB_DEVICE,10000);
        }
    }

    public void initUsb() {

        final List<UsbSerialDriver> drivers =
                UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);

        final List<UsbSerialPort> result = new ArrayList<UsbSerialPort>();
        for (final UsbSerialDriver driver : drivers) {
            final List<UsbSerialPort> ports = driver.getPorts();
            result.addAll(ports);
        }
        for(int i = 0 ; i < result.size(); i++)
        {
            UsbDevice device = result.get(i).getDevice();
            String p = Integer.toHexString(device.getProductId());
            String v = Integer.toHexString(device.getVendorId());
            if (p.equals("7523") && v.equals("1a86"))
            {
                sPort = result.get(i);
                break;
            }
        }
        if(sPort == null && result.size() == 1)
        {
            sPort = result.get(0);
        }
        if(sPort != null)
        {
             connection = mUsbManager.openDevice(sPort.getDriver().getDevice());
            if(connection == null)
            {
                getUsbPermission(sPort.getDevice());
            }
            else
            {
                startUsb();
            }
        }

    }

    public void startUsb()
    {
        //初始化一个connection以实现USB通讯,详见Android USB Host API
        if(connect == false)
        {
            try {
                //开启端口
                sPort.open(connection);
                //为sPort设置参数
                sPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

                sPort.setDTR(true);
                sPort.setRTS(true);

                mSerialIoManager = new SerialInputOutputManager(sPort, mListener);
                mSerialIoManager.setReadTimeout(500);
                mSerialIoManager.setWriteTimeout(500);
                mExecutor.submit(mSerialIoManager);
                connect = true;

            } catch (IOException e) {
                try {
                    sPort.close();
                } catch (IOException e2) {
                    // Ignore.
                }
                sPort = null;
                return;
            }

        }




    }

    public SerialInputOutputManager.Listener mListener = new SerialInputOutputManager.Listener() {
        @Override
        public void onRunError(Exception e) {
            if(sPort != null)
            {
                try {
                    sPort.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            deviceerror = false;
            connect = false;
            change = false;
        }

        @Override
        public void onNewData(final byte[] data) {

            Message msg = new Message();
            msg.what = UsbHandler.GET_DATA;
            msg.obj = data;
            if(usbHandler != null)
            usbHandler.sendMessage(msg);
        }
    };

    public void getData(byte[] data)
    {
        if(data.length > 0)
        {
            if(data[0] == 0x55)
            {
                if(data.length >= 9)
                {
                    if(data[8] == 0x3f)
                    {
                        if(data[1] == 0x15 || data[1] == 0x25) {
                            String v1 = String.format("%02x", data[3]);
                            String v2 = String.format("%02x", data[2]);

                            String l1 = String.format("%02x", data[5]);
                            String l2 = String.format("%02x", data[4]);

                            String s = String.format("%02x", data[6]);
                            int d = Integer.valueOf(l1+l2,16);
                            int sate = Integer.valueOf(s,16);
                            int v = Integer.valueOf(v1+v2,16);
                            String satue = Integer.toBinaryString(sate);
                            double rv = 0;
                            if(v > 0 )
                            {
                                rv = DIRECT*pie*5*50/v;
                            }

                            String[] dataget = new String[3];
                            DecimalFormat df = new DecimalFormat("#.00");
                            dataget[0] = String.valueOf(df.format(rv));
                            dataget[1] = "0";
                            dataget[2] = "0";
                            if(satue.startsWith("1"))
                            {
                                if(change == false)
                                {
                                    change = true;
                                    changecurrent = System.currentTimeMillis();
                                }
                            }
                            else
                            {
                                change = false;
                            }

                            if((System.currentTimeMillis() - changecurrent) > CHANGE_TIME && change == true)
                            {
                                dataget[2] = "1";
                                if(deviceerror = false)
                                    deviceerror = true;
                            }

                            if(getData != null)
                            {
                                getData.receiveData(dataget);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean sendData(byte[] data) {
        if (mSerialIoManager != null) {
            if(data != null)
            mSerialIoManager.writeAsync(data);
        }
        return false;
    }


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    context.unregisterReceiver(mUsbReceiver);
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                            && sPort.getDevice().equals(device)) {
                        //TODO 授权成功，操作USB设备
                        UsbDeviceConnection connection = mUsbManager.openDevice(sPort.getDriver().getDevice());
                        checkUsbDevice();
                    }else{
                        //用户点击拒绝了
                    }
                }
            }
        }
    };

    private void getUsbPermission(UsbDevice mUSBDevice) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(mUsbReceiver, filter);
        mUsbManager.requestPermission(mUSBDevice, pendingIntent);
    }

    public interface GetData
    {
        public void receiveData(String data[]);
    }
}
