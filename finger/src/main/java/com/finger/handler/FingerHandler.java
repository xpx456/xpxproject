package com.finger.handler;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.finger.FingerManger;
import com.finger.R;
import com.finger.entity.Finger;
import com.finger.thread.InitdeviceThread;
import com.finger.thread.ReconizeFingerThread;
import com.finger.thread.RegisterFingerThread;

import java.util.ArrayList;

public class FingerHandler extends Handler {

    public static final int CHECK_FINGER_TOUCHE = 10000;

    public FingerManger fingerManger;

    public FingerHandler(FingerManger fingerManger)
    {
        this.fingerManger = fingerManger;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case CHECK_FINGER_TOUCHE:
                fingerManger.checkFingerTouch();
                break;
            case InitdeviceThread.INIT_DEVICE_SUB_FINISH:
                fingerManger.devHandle = (long) msg.obj;
                break;
            case InitdeviceThread.GET_DEVICE_DB_FINISH:
                fingerManger.dbHandle = (long) msg.obj;
                break;
            case InitdeviceThread.GET_DEVICE_CONNECT_STAE:
                fingerManger.deviceState = (int) msg.obj;
                if(fingerManger.deviceState == FingerManger.STATE_CONNECT)
                {
                    fingerManger.checkFingerTouch();
                }
                break;
            case RegisterFingerThread.GET_FINGER_ERROR:
                fingerManger.setGetImf(fingerManger.context.getString(R.string.finger_error));
                break;
            case RegisterFingerThread.GET_FINGER_lEAVE:
                fingerManger.setGetImf(fingerManger.context.getString(R.string.finger_leave));
                break;
            case RegisterFingerThread.GET_FINGER_SAME:
                fingerManger.setGetImf(fingerManger.context.getString(R.string.finger_same));
                break;
            case RegisterFingerThread.GET_FINGER_SUCCESS_AGAIN:
                fingerManger.setGetImf(fingerManger.context.getString(R.string.finger_success_again));
                break;
            case RegisterFingerThread.GET_FINGER_QUERITY_AGAIN:
                fingerManger.setGetImf(fingerManger.context.getString(R.string.finger_querity_again));
                break;
            case RegisterFingerThread.GET_FINGER_ERROR_FINISH:
                fingerManger.setGetImf(fingerManger.context.getString(R.string.finger_error_max));
                break;
            case RegisterFingerThread.GET_FINGER_SUCCESS:
                fingerManger.setGetFingerFinish((Finger) msg.obj);
                break;
            case ReconizeFingerThread.GET_FINGER_CHECK_SUCCESS:
                fingerManger.setGetCheckFingerFinish((Finger) msg.obj);
                break;
        }
    }


}
