package com.finger.thread;


import android.graphics.Bitmap;
import android.os.Message;

import com.finger.FingerManger;
import com.finger.entity.Finger;

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
        super.run();
    }
}
