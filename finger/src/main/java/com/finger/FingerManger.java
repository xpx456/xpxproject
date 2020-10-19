package com.finger;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.SystemClock;
import android.view.View;

import com.finger.entity.CacheFinger;
import com.finger.entity.Finger;
import com.finger.handler.FingerHandler;
import com.finger.thread.InitdeviceThread;
import com.finger.thread.ReconizeFingerThread;
import com.finger.thread.RegisterFingerThread;
import com.finger.view.GetFingerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import intersky.apputils.BitmapCache;
import intersky.mywidget.conturypick.PinyinUtil;
import jx.vein.javajar.JXFVJavaInterface;
import jx.vein.javajar.JXVeinJavaAPI;
import jx.vein.javajar.devices.usb.USBHelper;
import jx.vein.javajar.vein.GetUSBPermission;


public class FingerManger {

    public static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";
    public static final int PID = 0x1001;
    public static final int VID = 0xABCD;
    public static final int TYPE_FINGER_EXHIBITION = 0;
    public static final int TYPE_FINGER_RESTURANT = 1;
    public static final int TYPE_FINGER_DK = 2;
    public static final int STATE_CONNECT = 0; //已经连接
    public static final int STATE_NONE = -1; //未检测到设备
    public static final int STATE_USB_PROMISS_ERROR = -2;//usb权限错误
    public static final int STATE_PROMISS_ERROR = 0;//设备未授权

    public static final int FINGER_TOCHED = 1;
    public static final int FINGER_LEAVE = 0;

    public static final long FINGER_CHECKE_TIME = 200;

    public static final String ACTION_GET_FINGER_SUCCESS = "ACTION_GET_FINGER_SUCCESS";
    public static final String ACTION_GET_LOGIN_FINGER_SUCCESS = "ACTION_GET_LOGIN_FINGER_SUCCESS";
    public static final String ACTION_GET_LOGIN_SUCCESS = "ACTION_GET_LOGIN_SUCCESS";
    public static final String ACTION_GET_LOGIN_FINGER_SUCCESS2 = "ACTION_GET_LOGIN_FINGER_SUCCESS2";
    public static final String ACTION_GET_LOGIN_FINGER_FAIL2 = "ACTION_GET_LOGIN_FINGER_FAIL2";
    public int fingertouchState = 2;
    public int deviceState = -1;

    public static final int finerimgw = 460;
    public static final int fingerimgh = 362;
    public UsbDevice selectDevice;
    public volatile static FingerManger fingerManger = null;
    public Context context;
    public FingerHandler fingerHandler;
    public JXFVJavaInterface jxfvJavaInterface;
    public JXVeinJavaAPI jxVeinJavaAPI;
    public long devHandle = -1;
    public long dbHandle = -1;
    public InitdeviceThread initdeviceThread;
    public RegisterFingerThread registerFingerThread;
    public ReconizeFingerThread reconizeFingerThread;
    public GetFingerView getFingerView;
    public String dbname = "";
    public Finger lastgetFinger;
    public HashMap<String, ArrayList<CacheFinger>> hashFingers;
    public boolean fingerlogin = false;
    public int type;
    public boolean isOpen = false;
    public FeaCheck feaCheck;
    public UsbManager usbManager;
    public boolean isnew = false;

    public UsbDeviceConnection connection;

    public static FingerManger init(Context context, String dbname, int type, FeaCheck feaCheck,boolean isnew) {

        if (fingerManger == null) {
            synchronized (FingerManger.class) {
                if (fingerManger == null) {
                    fingerManger = new FingerManger(context);
                    fingerManger.isnew = isnew;
                    fingerManger.type = type;
                    fingerManger.fingerHandler = new FingerHandler(fingerManger);
                    fingerManger.dbname = dbname;
                    fingerManger.feaCheck = feaCheck;
                    fingerManger.usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
                    if (fingerManger.type == TYPE_FINGER_EXHIBITION) {
                        fingerManger.jxfvJavaInterface = new JXFVJavaInterface();

                    }
                    else {

                        new Thread(){
                            @Override
                            public void run() {
                                fingerManger.getFileAccess();
                                super.run();
                            }
                        }.start();
                    }
                } else {
                    fingerManger.context = context;
                    fingerManger.isnew = isnew;
                    fingerManger.type = type;
                    fingerManger.fingerHandler = new FingerHandler(fingerManger);
                    fingerManger.dbname = dbname;
                    fingerManger.feaCheck = feaCheck;
                    if (fingerManger.type == TYPE_FINGER_RESTURANT) {
                        fingerManger.jxVeinJavaAPI = JXVeinJavaAPI.getInstance();


                    }
                    else {
                        new Thread(){
                            @Override
                            public void run() {
                                fingerManger.getFileAccess();
                                super.run();
                            }
                        }.start();

                    }

                }
            }
        }
        return fingerManger;
    }

    public void getFileAccess()
    {
        SystemClock.sleep(1000);
        File file = new File("/dev/bus/usb");
        if(!file.canRead() || !file.canWrite()){
            new GetUSBPermission();
            getFileAccess();
        }
        else
        {
            fingerManger.jxVeinJavaAPI = JXVeinJavaAPI.getInstance();
            fingerManger.initdeviceThread = new InitdeviceThread(fingerManger);
            fingerManger.initdeviceThread.start();
        }
    }

    public void getDeviceList() {
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        List<UsbDevice> usbDevices = new ArrayList<>();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            String p = Integer.toHexString(device.getProductId());
            String v = Integer.toHexString(device.getVendorId());
            if (p.equals("7523") && v.equals("1a86")) {
                selectDevice = device;
                connection = usbManager.openDevice(device);
                if (connection == null) {
//                    new GetUSBPermission();
                    //new GetUSBPermission();
                    getUsbPermission(device);
                    return;
                }
                int fd = connection.getFileDescriptor();
            }


            usbDevices.add(device);
        }
    }



    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    context.unregisterReceiver(mUsbReceiver);
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                            && selectDevice.equals(device)) {
                        //TODO 授权成功，操作USB设备
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
        usbManager.requestPermission(mUSBDevice, pendingIntent);
    }

    public FingerManger(Context context) {
        this.context = context;
    }

    public void startGetFingerImage(Context context, View location, int count, Finger finger) {
        getFingerView = new GetFingerView(context);
        getFingerView.creatView(location);
        getFingerView.tip.setText(context.getString(R.string.finger_get_start));
        if(registerFingerThread != null)
        {
            registerFingerThread.stop = true;
        }
        registerFingerThread = new RegisterFingerThread(fingerManger, count, finger);
        registerFingerThread.start();
    }

    public void setGetImf(String imf) {
        if (getFingerView != null) {
            getFingerView.setText(imf);
        }
    }

    public void setGetFingerFinish(Finger finger) {
        if (getFingerView != null) {
            getFingerView.hid();
        }
        lastgetFinger = finger;
        Intent intent = new Intent(ACTION_GET_FINGER_SUCCESS);
        context.sendBroadcast(intent);
    }

    public void setGetCheckFingerFinish(Finger finger) {
        lastgetFinger = finger;
        Intent intent = new Intent(ACTION_GET_LOGIN_FINGER_SUCCESS);
        context.sendBroadcast(intent);
        String feaid = reconize(finger.feas.get(0));
        if (feaid.length() > 0) {
            Intent intent1 = new Intent(ACTION_GET_LOGIN_SUCCESS);
            intent1.putExtra("feaid", feaid);
            intent1.putExtra("success", true);
            context.sendBroadcast(intent1);
        } else {
            Intent intent1 = new Intent(ACTION_GET_LOGIN_SUCCESS);
            intent1.putExtra("feaid", feaid);
            intent1.putExtra("success", false);
            context.sendBroadcast(intent1);
        }

    }

    public void setGetCheckFingerFinish2(String id) {
        Intent intent = new Intent(ACTION_GET_LOGIN_FINGER_SUCCESS2);
        intent.putExtra("rid", id);
        context.sendBroadcast(intent);
    }

    public void setGetCheckFingerFinish2Fail() {
        Intent intent = new Intent(ACTION_GET_LOGIN_FINGER_FAIL2);
        context.sendBroadcast(intent);
    }

    public boolean checkSame(byte[] samble1, byte[] amble2) {
        byte[] f1 = new byte[JXFVJavaInterface.veinFeatureSize];
        byte[] f2 = new byte[JXFVJavaInterface.veinFeatureSize];
        try {
            jxfvJavaInterface.jxGrabVeinFeature(samble1, f1);
            jxfvJavaInterface.jxGrabVeinFeature(amble2, f2);
            if (jxfvJavaInterface.jxVericateTwoVeinFeature(f1, f2) == 1) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkFinger(Finger finger, byte[] samble) {
        boolean issame = false;
        for (int i = 0; i < finger.sampleimg.size(); i++) {
            byte[] samblet = BitmapCache.Bitmap2Bytes(finger.sampleimg.get(i));
            if (checkSame(samble, samblet)) {
                issame = true;
                break;
            }
        }
        return issame;
    }

    public void destory() {
        fingerHandler = null;
        initdeviceThread = null;
        registerFingerThread = null;
        if (type == FingerManger.TYPE_FINGER_EXHIBITION) {
            jxfvJavaInterface.jxDeInitUSBDriver(devHandle);
        } else {
            jxVeinJavaAPI.jxReleaseVeinSDK();
        }

    }

    public ArrayList<Finger> measureFiners(File finger) {
        ArrayList<Finger> fingers = new ArrayList<Finger>();
        if (finger.isDirectory()) {
            File[] files = finger.listFiles();
            if (files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        File[] files1 = files[i].listFiles();
                        Finger myfinger = new Finger();
                        myfinger.gid = files[i].getName();
                        myfinger.path = files[i].getPath();
                        for (int j = 0; j < files1.length; i++) {
                            if (files1[j].isFile())
                                myfinger.sampleimg.add(BitmapFactory.decodeFile(files1[j].getPath()));
                        }
                        fingers.add(myfinger);
                    }
                }
            }
        }
        return fingers;
    }

    public void getFingerImage(Finger finger, File path) {
        if (path.isDirectory()) {
            File[] files1 = path.listFiles();
            for (int j = 0; j < files1.length; j++) {
                if (files1[j].isFile())
                    finger.sampleimg.add(BitmapFactory.decodeFile(files1[j].getPath()));
            }
        }
    }

    public void deleteFea(File path) {
        if (path.isDirectory()) {
            File[] files1 = path.listFiles();
            for (int j = 0; j < files1.length; j++) {
                if (files1[j].isDirectory())
                {
                    deleteFea(path.getName(),files1[j].getName());
                    files1[j].delete();
                }
            }
        }
    }

    public void deleteGuestFea(File path) {
        deleteGropFea(path.getName());
    }

    public boolean saveFea(Finger finger) {
        int rat = 0;
        switch (finger.sampleimg.size()) {
            case 1:
                rat = seaveFea1(finger);
                break;
            case 2:
                rat = seaveFea2(finger);
                break;
            case 3:
                rat = seaveFea3(finger);
                break;

        }
        if (rat == 0) {
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteFea(String guid,String vie) {
        int rat = 0;
        byte[] gid = getFingerGudid(guid).getBytes();
        byte[] vid = getFingerrid(vie).getBytes();
        try {
            rat = jxfvJavaInterface.jxRemoveVeinFeature(dbHandle,vid,gid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rat == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteGropFea(String guid) {
        int rat = 0;
        byte[] gid = getFingerGudid(guid).getBytes();
        try {
            rat = jxfvJavaInterface.jxRemoveGroupVeinFeature(dbHandle,gid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rat == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkExist(byte[] fea) {
        try {
            if (jxfvJavaInterface.jxIsFeatureDuplicate(dbHandle, fea) == 1)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void startReconize() {
        fingerManger.fingerlogin = true;
        if(reconizeFingerThread == null)
        {
            reconizeFingerThread = new ReconizeFingerThread(fingerManger);
            reconizeFingerThread.start();
        }
        else {
            reconizeFingerThread.stop = true;
            reconizeFingerThread = new ReconizeFingerThread(fingerManger);
            reconizeFingerThread.start();
        }

    }

    public void stopReconize() {
        fingerManger.fingerlogin = false;
        if (reconizeFingerThread != null) {
            reconizeFingerThread.stop = true;
            reconizeFingerThread.islogin = false;
        }
    }

    public String reconize(byte[] fea) {
        byte[] rid = new byte[5 * JXFVJavaInterface.veinIDLength];
        try {
            int rat = jxfvJavaInterface.jxRecognizeVeinFeature(dbHandle, fea, rid);
            if (rat > 0) {
                String rrid = new String(rid);
                String onerid = rrid.substring(0, JXFVJavaInterface.veinIDLength);
                return onerid;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private int seaveFea1(Finger finger) {
        byte[] vid = getFingerrid(finger.gid).getBytes();
        byte[] gid = getFingerGudid(finger.rid).getBytes();
        int rat = -1;
        try {
            rat = jxfvJavaInterface.jxAddOneVeinFeature(dbHandle, finger.feas.get(0), vid, gid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rat;
    }

    private int seaveFea2(Finger finger) {

        byte[] vid = getFingerrid(finger.gid).getBytes();
        byte[] gid = getFingerGudid(finger.rid).getBytes();
        int rat = -1;
        try {
            rat = jxfvJavaInterface.jxAddTwoVeinFeature(dbHandle, finger.feas.get(0), finger.feas.get(1), vid, gid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rat;
    }

    public static String  getFingerGudid(String id)
    {
        return id+"\0";
    }

    public static String  getFingerrid(String id)
    {
        return id+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
    }

    private int seaveFea3(Finger finger) {
        byte[] vid = getFingerrid(finger.gid).getBytes();
        byte[] gid = getFingerGudid(finger.rid).getBytes();
        int rat = -1;
        try {
            rat = jxfvJavaInterface.jxAddThreeVeinFeature(dbHandle, finger.feas.get(0), finger.feas.get(1), finger.feas.get(2), vid, gid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rat;
    }

    private byte[] getFea(Bitmap bitmap) {
        byte[] sample = new byte[JXFVJavaInterface.veinSampleSize];
        byte[] fea = new byte[JXFVJavaInterface.veinFeatureSize];
        try {
            jxfvJavaInterface.jxGrabVeinFeature(sample, fea);
            return fea;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface FeaCheck {
        public String FeaCheck(byte[] fea);
    }

}
