package com.finger.thread;


import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.finger.FingerManger;
import com.finger.entity.Finger;
import com.zkteco.biometric.exception.ZKWFPModuleException;

import java.io.File;
import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.apputils.BitmapCache;
import jx.vein.javajar.JXFVJavaInterface;

public class RegisterFingerThread extends Thread {

    public static final int GET_FINGER_SUCCESS = 13000;
    public static final int GET_FINGER_SAME = 13001;
    public static final int GET_FINGER_lEAVE = 13002;
    public static final int GET_FINGER_ERROR = 13003;
    public static final int GET_FINGER_SUCCESS_AGAIN = 13004;
    public static final int GET_FINGER_QUERITY_AGAIN = 13005;
    public static final int GET_FINGER_ERROR_FINISH = 13006;
    public static final int GET_FINGER_PLEASE_LEAVE= 13007;
    public FingerManger fingerManger;
    public int min = 3;
    public Finger finger;
    public RegisterFingerThread(FingerManger fingerManger,int min,Finger finger)
    {
        this.fingerManger = fingerManger;
        this.min = min;
        this.finger = finger;
    }


    @Override
    public void run() {
        type1();
        super.run();
    }

    public void type1()
    {
        int rat = fingerManger.jxfvJavaInterface.jxInitCapEnv(fingerManger.devHandle);
        int erreycount = 0;
        if(rat == 0) {
            while (finger.sampleimg.size() < this.min && erreycount < 5)
            {
                if( fingerManger.jxfvJavaInterface.jxIsFingerTouched(fingerManger.devHandle) == FingerManger.FINGER_LEAVE)
                {
                    while (true)
                    {
                        if(fingerManger.fingertouchState == FingerManger.FINGER_TOCHED)
                        {
                            break;
                        }
                    }
                }
                byte[] fingerbuf = new byte[JXFVJavaInterface.veinImgSize];
                int getrat = 0;
                try {
                    while (getrat != 2 && getrat != -100 && getrat != 3)
                    {
                        getrat = fingerManger.jxfvJavaInterface.jxIsVeinImgOK(fingerManger.devHandle,fingerbuf);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(getrat == -100) {
                    erreycount++;
                    if(fingerManger.fingerHandler != null)
                        fingerManger.fingerHandler.sendEmptyMessage(GET_FINGER_ERROR);
                }
                if(getrat == 3) {
                    if(fingerManger.fingerHandler != null)
                        fingerManger.fingerHandler.sendEmptyMessage(GET_FINGER_lEAVE);
                }
                if(getrat == 2) {
                    boolean dosave = true;
                    byte[] sample = new byte[JXFVJavaInterface.veinSampleSize];
                    try {
                        fingerManger.jxfvJavaInterface.jxLoadVeinSample(fingerManger.devHandle,sample);
                        if(fingerManger.jxfvJavaInterface.jxCheckVeinSampleQuality(sample) == 0) {

                            byte[] fea = new byte[JXFVJavaInterface.veinFeatureSize];
                            int resu = fingerManger.jxfvJavaInterface.jxGrabVeinFeature(sample,fea);
                            if(fingerManger.jxfvJavaInterface.jxIsFeatureDuplicate(fingerManger.dbHandle,fea) == 1) {

                                Message message = new Message();
                                message.what = GET_FINGER_SAME;
                                if(fingerManger.fingerHandler != null)
                                {
                                    fingerManger.fingerHandler.sendMessage(message);
                                }
                                dosave = false;
                            }
                            if(dosave) {
                                Bitmap fingerbitmap = BitmapCache.Bytes2Bimap(fingerbuf,FingerManger.finerimgw,FingerManger.fingerimgh);
                                finger.sampleimg.add(fingerbitmap);
                                finger.feas.add(fea);
                                if(finger.sampleimg.size() < min)
                                {
                                    Message message = new Message();
                                    message.what = GET_FINGER_SUCCESS_AGAIN;
                                    message.arg1 = finger.sampleimg.size();
                                    message.arg2 = min;
                                    if(fingerManger.fingerHandler != null)
                                    {
                                        fingerManger.fingerHandler.sendMessage(message);
                                    }
                                }
                                else
                                {
                                    Message message = new Message();
                                    message.what = GET_FINGER_SUCCESS;
                                    message.obj = finger;
                                    if(fingerManger.fingerHandler != null)
                                    {
                                        fingerManger.fingerHandler.sendMessage(message);
                                    }
                                }
                            }
                            else {
                                Message message = new Message();
                                message.what = GET_FINGER_QUERITY_AGAIN;
                                if(fingerManger.fingerHandler != null)
                                {
                                    fingerManger.fingerHandler.sendMessage(message);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                while (true)
                {
                    if(fingerManger.fingertouchState == FingerManger.FINGER_LEAVE)
                    {
                        break;
                    }
                }
            }

        }
        rat = fingerManger.jxfvJavaInterface.jxDeInitCapEnv(fingerManger.devHandle);
        if(rat == -1)
        {
            fingerManger.jxfvJavaInterface.jxDeInitCapEnv(fingerManger.devHandle);
        }
        if(erreycount >= 5)
        {
            if(fingerManger.fingerHandler != null)
                fingerManger.fingerHandler.sendEmptyMessage(GET_FINGER_ERROR_FINISH);
        }
    }

}
