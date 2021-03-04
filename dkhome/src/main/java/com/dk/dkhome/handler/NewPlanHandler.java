package com.dk.dkhome.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.dk.dkhome.view.activity.MainActivity;
import com.dk.dkhome.view.activity.NewPlanActivity;


//01


public class NewPlanHandler extends Handler {


    public NewPlanActivity theActivity;
    public static final int SET_VIDEO = 102000;
    public NewPlanHandler(NewPlanActivity newPlanActivity) {
        theActivity = newPlanActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case SET_VIDEO:
                theActivity.mNewPlanPresenter.setVideo((Intent) msg.obj);
                break;
        }

    }
}
