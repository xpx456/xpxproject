package com.dk.dkphone.handler;

import android.os.Handler;
import android.os.Message;

import com.dk.dkphone.view.activity.OptationActivity;


//01


public class OptationHandler extends Handler {

    public static final int EVENT_SET_ADDRESS = 230001;

    public OptationActivity theActivity;
    public OptationHandler(OptationActivity registerActivity) {
        theActivity = registerActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {


        }

    }
}
