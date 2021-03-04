package com.dk.dkpad.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.dk.dkpad.view.activity.OptationActivity;

import intersky.xpxnet.net.NetObject;


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
