package com.dk.dkhome.handler;

import android.os.Handler;
import android.os.Message;

import com.dk.dkhome.view.activity.MainActivity;
import com.dk.dkhome.view.activity.SportDetialActivity;


//01


public class SportDetialHandler extends Handler {


    public SportDetialActivity theActivity;
    public static final int UPDATA_TIME = 100000;
    public static final int CLOSE_SUCCESS = 100001;
    public SportDetialHandler(SportDetialActivity mSportDetialActivity) {
        theActivity = mSportDetialActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case UPDATA_TIME:
                break;
            case CLOSE_SUCCESS:
                break;
        }

    }
}
