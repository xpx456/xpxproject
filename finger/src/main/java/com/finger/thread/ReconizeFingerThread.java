package com.finger.thread;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.finger.FingerManger;
import com.finger.entity.Finger;

import intersky.apputils.BitmapCache;
import intersky.apputils.SystemUtil;
import jx.vein.javajar.JXFVJavaInterface;
import jx.vein.javajar.JXVeinJavaAPI;

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
    public static final int GET_FINGER_CHECK_SUCCESS_TYPE2 = 13009;
    public static final int GET_FINGER_CHECK_FAIL_TYPE2 = 13010;
    public FingerManger fingerManger;
    public boolean islogin = true;
    public Finger finger = new Finger();
    public static final int RESTURANT_MATCH = 2;
    public long current = 0;
    public boolean stop = false;
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

    public void type1() {
        while (true)
        {
            if(stop)
            {
                return;
            }
            int rat = fingerManger.jxfvJavaInterface.jxInitCapEnv(fingerManger.devHandle);
            if (rat == 0) {
                while (true)
                {
                    if(stop)
                    {
                        return;
                    }
                    if(fingerManger.jxfvJavaInterface.jxIsFingerTouched(fingerManger.devHandle) == FingerManger.FINGER_TOCHED)
                    {

                        byte[] fingerbuf = new byte[JXFVJavaInterface.veinImgSize];
                        int getrat = 0;
                        try {
                            while (getrat != 2 && getrat != -100 && getrat != 3) {
                                if(stop)
                                {
                                    return;
                                }
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
                                        Bitmap fingerbitmap = BitmapCache.createBitmapGray(fingerbuf, FingerManger.finerimgw, FingerManger.fingerimgh);
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
                        break;
                    }
                }
            }
            fingerManger.jxfvJavaInterface.jxDeInitCapEnv(fingerManger.devHandle);
        }

    }


    public void type2() {

        while (true) {
            if(stop)
            {
                return;
            }
            try {

                if(fingerManger.feaCheck == null) {
                    SystemClock.sleep(100);
                    continue;
                }
                int a = fingerManger.jxVeinJavaAPI.jxIsFingerTouched();
                if ( fingerManger.jxVeinJavaAPI.jxIsFingerTouched() == FingerManger.FINGER_TOCHED)
                {
                    String rid = "";
                    byte[] fea = new byte[JXVeinJavaAPI.veinFeatSize];

                    int rat = -99;



                    for (int j=0; j<2; j++) {
                        if (fingerManger.jxVeinJavaAPI.jxIsFingerTouched() == FingerManger.FINGER_LEAVE) break;

                        rat = fingerManger.jxVeinJavaAPI.jxCapVeinFeat(fea);

                        if (rat == -5) {
                            SystemClock.sleep(100);
                            continue;
                        }

                        if (rat < 0) {
                            SystemClock.sleep(100);
                            break;
                        }

                        rid = fingerManger.feaCheck.FeaCheck(fea);
                        if (!rid.isEmpty()) {
                            Message message = new Message();
                            message.what = GET_FINGER_CHECK_SUCCESS_TYPE2;
                            message.obj = rid;
                            if (fingerManger.fingerHandler != null) {
                                fingerManger.fingerHandler.sendMessage(message);
                            }

                            for (int i=0;i<100;i++) {
                                if (fingerManger.jxVeinJavaAPI.jxIsFingerTouched() == FingerManger.FINGER_LEAVE) break;
                                SystemClock.sleep(30);
                            }

                            break;
                        }
                    }


                    if (rid.isEmpty() && fingerManger.fingerHandler != null)
                        fingerManger.fingerHandler.sendEmptyMessage(GET_FINGER_CHECK_FAIL_TYPE2);

                }
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

//    public void type2() {
//
//        while (true) {
//            if(stop)
//            {
//                return;
//            }
//            try {
//                if ( fingerManger.jxVeinJavaAPI.jxIsFingerTouched() == FingerManger.FINGER_TOCHED) {
//
//
//
//
//                    byte[] fea = new byte[JXVeinJavaAPI.veinFeatSize];
//                    int rat = fingerManger.jxVeinJavaAPI.jxCapVeinFeat(fea);
//                    if(fingerManger.feaCheck != null)
//                    {
//                        String rid = "";
//                        if(rat <=5 && rat >= 1)
//                        {
//                            rid = fingerManger.feaCheck.FeaCheck(fea);
//                        }
//                        if(rid.length() > 0 && System.currentTimeMillis()-current > 1000)
//                        {
//                            current = System.currentTimeMillis();
//                            Message message = new Message();
//                            message.what = GET_FINGER_CHECK_SUCCESS_TYPE2;
//                            message.obj = rid;
//                            if (fingerManger.fingerHandler != null) {
//                                fingerManger.fingerHandler.sendMessage(message);
//                            }
//                        }
//                        else
//                        {
//                            if (fingerManger.fingerHandler != null)
//                                fingerManger.fingerHandler.sendEmptyMessage(GET_FINGER_CHECK_FAIL_TYPE2);
//                        }
//                    }
//
//                }
//                sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }



}
