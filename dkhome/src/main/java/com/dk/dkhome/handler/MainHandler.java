package com.dk.dkhome.handler;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;

import com.dk.dkhome.view.activity.MainActivity;

import xpx.bluetooth.BlueToothHandler;


//01


public class MainHandler extends Handler {


    public MainActivity theActivity;
    public static final int UPDATA_TIME = 100000;
    public static final int CLOSE_SUCCESS = 100001;
    public MainHandler(MainActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case UPDATA_TIME:
                break;
            case CLOSE_SUCCESS:
                break;
            case BlueToothHandler.EVENT_FIND_DEVICE:
                theActivity.sportFragment.addView((BluetoothDevice) msg.obj);
                break;
        }

    }
}
