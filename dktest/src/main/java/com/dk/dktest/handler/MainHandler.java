package com.dk.dktest.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.dk.dktest.view.activity.MainActivity;


//01


public class MainHandler extends Handler {


    public MainActivity theActivity;
    public static final int UPDATA_TIME = 100000;
    public static final int CLOSE_SUCCESS = 100001;
    public static final int ADD_DEVICE = 100002;
    public static final int CLEAN_DEVICE = 100003;
    public static final int UPDATA_DEVICE = 100004;
    public static final int WAIT_HID = 100005;
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
            case ADD_DEVICE:
            case UPDATA_DEVICE:
                if(theActivity.equipFragment != null)
                {
                    theActivity.equipFragment.updataView((Intent) msg.obj);
                }
                break;
            case CLEAN_DEVICE:
                if(theActivity.equipFragment != null)
                {
                    theActivity.equipFragment.cleanViews((Intent) msg.obj);
                }
                break;
            case WAIT_HID:
                theActivity.waitDialog.hide();
                break;
        }

    }
}
