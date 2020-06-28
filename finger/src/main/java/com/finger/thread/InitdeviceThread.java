package com.finger.thread;


import android.graphics.BitmapFactory;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;

import com.finger.FingerManger;
import com.finger.entity.CacheFinger;
import com.finger.entity.Finger;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.biometric.ZKWFPModuleFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import intersky.apputils.BitmapCache;
import jx.vein.javajar.JXFVJavaInterface;

public class InitdeviceThread extends Thread {

    public static final int INIT_DEVICE_SUB_FINISH = 11000;
    public static final int GET_DEVICE_CONNECT_STAE = 11001;
    public static final int GET_DEVICE_DB_FINISH = 11002;
    public long devHandle;
    public long dbHandle;
    public FingerManger fingerManger;
    public InitdeviceThread(FingerManger fingerManger)
    {
        this.fingerManger = fingerManger;
    }

    //初始化usb驱动
    private void initUSBDriver()
    {
        devHandle = fingerManger.jxfvJavaInterface.jxInitUSBDriver();
        Message msg = new Message();
        msg.what = INIT_DEVICE_SUB_FINISH;
        msg.obj = devHandle;
        if(fingerManger.fingerHandler != null)
        {
            fingerManger.fingerHandler.sendMessage(msg);
        }
    }

    //连接设备
    public void checkDeviceConnect()
    {
        Message msg = new Message();
        msg.what = GET_DEVICE_CONNECT_STAE;
        msg.obj = fingerManger.jxfvJavaInterface.jxConnFVD(devHandle);
        if(fingerManger.fingerHandler != null)
        {
            fingerManger.fingerHandler.sendMessage(msg);
        }

    }

    public void ceatDb()
    {
        if(fingerManger.jxfvJavaInterface.jxIsVeinDBExist(fingerManger.dbname) == 0)
        {
            fingerManger.jxfvJavaInterface.jxCreateVeinDatabase(fingerManger.dbname);
        }
        dbHandle = fingerManger.jxfvJavaInterface.jxInitVeinDatabase(fingerManger.dbname);
        int a = fingerManger.jxfvJavaInterface.jxCountAllFeatures(dbHandle);
        Message msg = new Message();
        msg.what = GET_DEVICE_DB_FINISH;
        msg.obj = dbHandle;
        if(fingerManger.fingerHandler != null)
        {
            fingerManger.fingerHandler.sendMessage(msg);
        }
    }


    @Override
    public void run() {
        if(fingerManger.type == FingerManger.TYPE_FINGER_EXHIBITION)
        {
            initUSBDriver();
            checkDeviceConnect();
            ceatDb();
        }
        else if(fingerManger.type == FingerManger.TYPE_FINGER_RESTURANT)
        {
            Map fingerprintParams = new HashMap();
            //set vid
            fingerprintParams.put(ParameterHelper.PARAM_KEY_VID, FingerManger.VID);
            //set pid
            fingerprintParams.put(ParameterHelper.PARAM_KEY_PID, FingerManger.PID);
            fingerManger.module = ZKWFPModuleFactory.createFingerprintSensor(fingerManger.context, TransportType.USBSCSI, fingerprintParams);
            if(fingerManger.isOpen == false)
            fingerManger.OnBnOpen();

        }
        super.run();
    }
}
