package com.finger;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.text.TextUtils;
import android.view.View;

import com.finger.entity.CacheFinger;
import com.finger.entity.Finger;
import com.finger.handler.FingerHandler;
import com.finger.receicer.UsbReceiver;
import com.finger.thread.InitdeviceThread;
import com.finger.thread.ReconizeFingerThread;
import com.finger.thread.RegisterFingerThread;
import com.finger.view.GetFingerView;
import com.zkteco.biometric.ZKWFPModule;
import com.zkteco.biometric.exception.ZKWFPModuleException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import intersky.apputils.BitmapCache;
import jx.vein.javajar.JXFVJavaInterface;


public class FingerManger {

    public static final String ACTION_USB_PERMISSION = "com.zkteco.finger.USB_PERMISSION";
    public static final int PID = 0x1001;
    public static final int VID = 0xABCD;
    public static final int TYPE_FINGER_EXHIBITION = 0;
    public static final int TYPE_FINGER_RESTURANT = 1;

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
    public int fingertouchState = 2;
    public int deviceState = -1;

    public static final int finerimgw = 362;
    public static final int fingerimgh = 460;

    public volatile static FingerManger fingerManger = null;
    public Context context;
    public FingerHandler fingerHandler;
    public JXFVJavaInterface jxfvJavaInterface;
    public long devHandle = -1;
    public long dbHandle = -1;
    public InitdeviceThread initdeviceThread;
    public RegisterFingerThread registerFingerThread;
    public ReconizeFingerThread reconizeFingerThread;
    public GetFingerView getFingerView;
    public String dbname = "";
    public Finger lastgetFinger;
    public HashMap<String,ArrayList<CacheFinger>> hashFingers;
    public boolean fingerlogin = false;
    public int type;
    public UsbManager musbManager;
    public ZKWFPModule module;
    public boolean isOpen = false;
    public UsbReceiver usbReceiver;
    public static FingerManger init(Context context,String dbname,int type) {

        if (fingerManger == null) {
            synchronized (FingerManger.class) {
                if (fingerManger == null) {
                    fingerManger = new FingerManger(context);
                    fingerManger.type = type;
                    fingerManger.fingerHandler = new FingerHandler(fingerManger);
                    fingerManger.dbname = dbname;
                    if(fingerManger.type == TYPE_FINGER_EXHIBITION)
                    {
                        fingerManger.jxfvJavaInterface = new JXFVJavaInterface();

                    }
                    else
                    {
                        fingerManger.usbReceiver = new UsbReceiver(fingerManger);
                        fingerManger.musbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
                    }
                    fingerManger.initdeviceThread = new InitdeviceThread(fingerManger);
                    fingerManger.initdeviceThread.start();
                }
                else
                {
                    fingerManger.context = context;
                    fingerManger.type = type;
                    fingerManger.fingerHandler = new FingerHandler(fingerManger);
                    fingerManger.dbname = dbname;
                    if(fingerManger.type == TYPE_FINGER_RESTURANT)
                    {
                        fingerManger.jxfvJavaInterface = new JXFVJavaInterface();
                    }
                    else
                    {
                        fingerManger.usbReceiver = new UsbReceiver(fingerManger);
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction(ACTION_USB_PERMISSION);
                        intentFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
                        fingerManger.context.unregisterReceiver(fingerManger.usbReceiver);
                        fingerManger.context.registerReceiver(fingerManger.usbReceiver,intentFilter);
                    }
                    fingerManger.initdeviceThread = new InitdeviceThread(fingerManger);
                    fingerManger.initdeviceThread.start();
                }
            }
        }
        return fingerManger;
    }

    public void checkFingerTouch() {
        if(devHandle != -1)
        {
            fingertouchState = jxfvJavaInterface.jxIsFingerTouched(devHandle);
        }
        if(fingerHandler != null)
        {
            fingerHandler.removeMessages(FingerHandler.CHECK_FINGER_TOUCHE);
            fingerHandler.sendEmptyMessageDelayed(FingerHandler.CHECK_FINGER_TOUCHE,FINGER_CHECKE_TIME);
        }

    }

    public FingerManger(Context context) {
        this.context = context;
    }

    public void startGetFingerImage(Context context, View location, int count,Finger finger) {
        getFingerView = new GetFingerView(context);
        getFingerView.creatView(location);
        getFingerView.tip.setText(context.getString(R.string.finger_get_start));
        registerFingerThread = new RegisterFingerThread(fingerManger,count,finger);
        registerFingerThread.start();
    }

    public void setGetImf(String imf) {
        if(getFingerView != null)
        {
            getFingerView.setText(imf);
        }
    }

    public void setGetFingerFinish(Finger finger) {
        if(getFingerView != null)
        {
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
        if(feaid.length() > 0)
        {
            Intent intent1 = new Intent(ACTION_GET_LOGIN_SUCCESS);
            intent1.putExtra("feaid",feaid);
            intent1.putExtra("success",true);
            context.sendBroadcast(intent1);
        }
        else
        {
            Intent intent1 = new Intent(ACTION_GET_LOGIN_SUCCESS);
            intent1.putExtra("feaid",feaid);
            intent1.putExtra("success",false);
            context.sendBroadcast(intent1);
        }

    }

    public boolean checkSame(byte[] samble1,byte[] amble2) {
        byte[] f1= new byte[JXFVJavaInterface.veinFeatureSize];
        byte[] f2= new byte[JXFVJavaInterface.veinFeatureSize];
        try {
            jxfvJavaInterface.jxGrabVeinFeature(samble1,f1);
            jxfvJavaInterface.jxGrabVeinFeature(amble2,f2);
            if(jxfvJavaInterface.jxVericateTwoVeinFeature(f1,f2) == 1)
            {
                return true;
            }
            else
            {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkFinger(Finger finger,byte[] samble) {
        boolean issame = false;
        for(int i = 0; i < finger.sampleimg.size() ; i++)
        {
            byte[] samblet = BitmapCache.Bitmap2Bytes(finger.sampleimg.get(i));
            if(checkSame(samble,samblet))
            {
                issame = true;
                break;
            }
        }
        return issame;
    }

    public void destory()
    {
        fingerHandler = null;
        initdeviceThread = null;
        registerFingerThread = null;
        if(type == FingerManger.TYPE_FINGER_EXHIBITION)
        {
            jxfvJavaInterface.jxDeInitUSBDriver(devHandle);
        }
        else
        {
            fingerManger.context.unregisterReceiver(usbReceiver);
        }

    }

    public ArrayList<Finger> measureFiners(File finger)
    {
        ArrayList<Finger> fingers = new ArrayList<Finger>();
        if(finger.isDirectory())
        {
            File[] files = finger.listFiles();
            if(files.length > 0)
            {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()){
                        File[] files1 = files[i].listFiles();
                        Finger myfinger = new Finger();
                        myfinger.gid = files[i].getName();
                        myfinger.path = files[i].getPath();
                        for (int j = 0; j < files1.length; i++) {
                            if(files1[j].isFile())
                                myfinger.sampleimg.add(BitmapFactory.decodeFile(files1[j].getPath()));
                        }
                        fingers.add(myfinger);
                    }
                }
            }
        }
        return fingers;
    }

    public void getFingerImage(Finger finger,File path)
    {
        if(path.isDirectory())
        {
            File[] files1 = path.listFiles();
            for (int j = 0; j < files1.length; j++) {
                if(files1[j].isFile())
                    finger.sampleimg.add(BitmapFactory.decodeFile(files1[j].getPath()));
            }
        }
    }

    public boolean saveFea(Finger finger)
    {
        for(int i = 0 ; i < finger.feas.size() ; i++)
        {
            if(checkExist(finger.feas.get(i)))
            {
                return false;
            }
        }
        int rat = 0;
        switch (finger.sampleimg.size())
        {
            case 1:
                rat = seaveFea1( finger);
                break;
            case 2:
                rat = seaveFea2(finger);
                break;
            case 3:
                rat = seaveFea3(finger);
                break;

        }
        if(rat == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean checkExist(byte[] fea)
    {
        try {
            if(jxfvJavaInterface.jxIsFeatureDuplicate(dbHandle,fea) == 1)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void startReconize()
    {
        fingerManger.fingerlogin = true;
        reconizeFingerThread = new ReconizeFingerThread(fingerManger);
        reconizeFingerThread.start();

    }

    public void stopReconize()
    {
        fingerManger.fingerlogin = false;
        if(reconizeFingerThread != null)
        {
            reconizeFingerThread.islogin = false;
        }
    }

    public String reconize(byte[] fea)
    {
        byte[] rid = new byte[5*JXFVJavaInterface.veinIDLength];
        try {
            int rat = jxfvJavaInterface.jxRecognizeVeinFeature(dbHandle,fea,rid);
            if(rat > 0)
            {
                String rrid = new String(rid);
                String onerid = rrid.substring(0,50);
                return onerid;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private int seaveFea1(Finger finger)
    {
        byte[] vid = finger.rid.getBytes();
        byte[] gid = finger.gid.getBytes();
        int rat = -1;
        try {
            rat = jxfvJavaInterface.jxAddOneVeinFeature(dbHandle,finger.feas.get(0),vid,gid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rat;
    }

    private int seaveFea2(Finger finger)
    {

        byte[] vid = finger.rid.getBytes();
        byte[] gid = finger.gid.getBytes();
        int rat = -1;
        try {
            rat = jxfvJavaInterface.jxAddTwoVeinFeature(dbHandle,finger.feas.get(0),finger.feas.get(1),vid,gid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rat;
    }

    private int seaveFea3(Finger finger)
    {
        byte[] vid = finger.rid.getBytes();
        byte[] gid = finger.gid.getBytes();
        int rat = -1;
        try {
            rat = jxfvJavaInterface.jxAddThreeVeinFeature(dbHandle,finger.feas.get(0),finger.feas.get(1),finger.feas.get(2),vid,gid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rat;
    }

    private byte[] getFea(Bitmap bitmap)
    {
        byte[] sample = new byte[JXFVJavaInterface.veinSampleSize];
        byte[] fea = new byte[JXFVJavaInterface.veinFeatureSize];
        try {
            jxfvJavaInterface.jxGrabVeinFeature(sample,fea);
            return fea;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //-------------------------------------------------------------------------------------


    public void initDevice() {
        musbManager = (UsbManager)context.getSystemService(Context.USB_SERVICE);
        for (UsbDevice device : musbManager.getDeviceList().values())
        {
            if (device.getVendorId() == VID && device.getProductId() == PID)
            {
                if(!musbManager.hasPermission(device)){
                    Intent intent = new Intent(ACTION_USB_PERMISSION);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                    musbManager.requestPermission(device, pendingIntent);
                }else{
                    openDevice();
                }
            }
        }
    }

    public void OnBnOpen(){
        if(isOpen){
            return;
        }
        initDevice();
    }

    public void  openDevice(){
        try {
            module.open(0);
            module.writeSystemParam(0,4,0);
            isOpen = true;
        } catch (ZKWFPModuleException e) {
            e.printStackTrace();
        }
    }


}
