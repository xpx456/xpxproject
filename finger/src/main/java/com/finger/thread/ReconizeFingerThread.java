package com.finger.thread;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;

import com.finger.FingerManger;
import com.finger.entity.Finger;
import com.zkteco.biometric.exception.ZKWFPModuleException;

import intersky.apputils.BitmapCache;
import jx.vein.javajar.JXFVJavaInterface;

public class ReconizeFingerThread extends Thread {

    public static final int GET_FINGER_SUCCESS = 13000;
    public static final int GET_FINGER_SAME = 13001;
    public static final int GET_FINGER_lEAVE = 13002;
    public static final int GET_FINGER_ERROR = 13003;
    public static final int GET_FINGER_SUCCESS_AGAIN = 13004;
    public static final int GET_FINGER_QUERITY_AGAIN = 13005;
    public static final int GET_FINGER_ERROR_FINISH = 13006;
    public static final int GET_FINGER_PLEASE_LEAVE = 13007;
    public static final int GET_FINGER_CHECK_SUCCESS = 13008;
    public FingerManger fingerManger;
    public boolean islogin = true;
    public Finger finger = new Finger();

    public ReconizeFingerThread(FingerManger fingerManger) {
        this.fingerManger = fingerManger;

    }


    @Override
    public void run() {
        if(fingerManger.type == FingerManger.TYPE_FINGER_EXHIBITION)
        {
            type1();
        }
        else
        {
            type2();
        }
        super.run();
    }

    public void type1()
    {
        int rat = fingerManger.jxfvJavaInterface.jxInitCapEnv(fingerManger.devHandle);
        int erreycount = 0;
        if (rat == 0) {
            while (finger.sampleimg.size() < 1 && erreycount < 5) {
                if (fingerManger.jxfvJavaInterface.jxIsFingerTouched(fingerManger.devHandle) == FingerManger.FINGER_LEAVE) {
                    while (true) {
                        if(islogin == false)
                        {
                            return;
                        }

                        if (fingerManger.fingertouchState == FingerManger.FINGER_TOCHED) {
                            break;
                        }
                    }
                }
                byte[] fingerbuf = new byte[JXFVJavaInterface.veinImgSize];
                int getrat = 0;
                try {
                    while (getrat != 2 && getrat != -100 && getrat != 3) {
                        if(islogin == false)
                        {
                            return;
                        }
                        getrat = fingerManger.jxfvJavaInterface.jxIsVeinImgOK(fingerManger.devHandle, fingerbuf);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (getrat == -100) {
                    erreycount++;
                    if (fingerManger.fingerHandler != null)
                        fingerManger.fingerHandler.sendEmptyMessage(GET_FINGER_ERROR);
                }
                if (getrat == 3) {
                    if (fingerManger.fingerHandler != null)
                        fingerManger.fingerHandler.sendEmptyMessage(GET_FINGER_lEAVE);
                }
                if (getrat == 2) {
                    boolean dosave = true;
                    byte[] sample = new byte[JXFVJavaInterface.veinSampleSize];
                    try {
                        fingerManger.jxfvJavaInterface.jxLoadVeinSample(fingerManger.devHandle, sample);
                        if (fingerManger.jxfvJavaInterface.jxCheckVeinSampleQuality(sample) == 0) {

                            byte[] fea = new byte[JXFVJavaInterface.veinFeatureSize];
                            int resu = fingerManger.jxfvJavaInterface.jxGrabVeinFeature(sample,fea);
                            if (dosave) {
                                Bitmap fingerbitmap = BitmapCache.Bytes2Bimap(fingerbuf, FingerManger.finerimgw, FingerManger.fingerimgh);
                                finger.sampleimg.add(fingerbitmap);
                                finger.feas.add(fea);
                                if (finger.sampleimg.size() < 1) {
                                    Message message = new Message();
                                    message.what = GET_FINGER_SUCCESS_AGAIN;
                                    message.arg1 = finger.sampleimg.size();
                                    message.arg2 = 1;
                                    if (fingerManger.fingerHandler != null) {
                                        fingerManger.fingerHandler.sendMessage(message);
                                    }
                                } else {
                                    Message message = new Message();
                                    message.what = GET_FINGER_CHECK_SUCCESS;
                                    message.obj = finger;
                                    if (fingerManger.fingerHandler != null) {
                                        fingerManger.fingerHandler.sendMessage(message);
                                    }
                                }
                            } else {
                                Message message = new Message();
                                message.what = GET_FINGER_QUERITY_AGAIN;
                                if (fingerManger.fingerHandler != null) {
                                    fingerManger.fingerHandler.sendMessage(message);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                while (true) {
                    if(islogin == false)
                    {
                        return;
                    }
                    if (fingerManger.fingertouchState == FingerManger.FINGER_LEAVE) {
                        break;
                    }
                }
            }

        }
        rat = fingerManger.jxfvJavaInterface.jxDeInitCapEnv(fingerManger.devHandle);
        if (rat == -1) {
            fingerManger.jxfvJavaInterface.jxDeInitCapEnv(fingerManger.devHandle);
        }
        if (erreycount >= 5) {
            if (fingerManger.fingerHandler != null)
                fingerManger.fingerHandler.sendEmptyMessage(GET_FINGER_ERROR_FINISH);
        }
    }

    public void type2()
    {

        while (fingerManger.fingerlogin)
        {
            if(fingerManger.isOpen){
                try {
                    int ret = fingerManger.module.captureFinger(0);
                    if(0 == ret){
                        int[] id = new int[1];
                        ret = fingerManger.module.identify(0,id);
                        if(ret == 0x100A){
                        }else if(ret == 0x1008||ret == 0xFFFF){
                        }else if(ret == 0x0000){
                            Intent intent1 = new Intent(FingerManger.ACTION_GET_LOGIN_SUCCESS);
                            intent1.putExtra("feaid",String.valueOf(id[0]));
                            intent1.putExtra("success",true);
                            fingerManger.context.sendBroadcast(intent1);
                        }
                    }else if(0x1012 == ret){
                    }
                } catch (ZKWFPModuleException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
