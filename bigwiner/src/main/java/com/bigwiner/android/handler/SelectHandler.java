package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.view.activity.SelectActivity;
import com.bigwiner.android.view.activity.UserInfoActivity;

import intersky.xpxnet.net.NetObject;

//08

public class SelectHandler extends Handler {

    public static final int CHAGE_CITY = 3011000;
    public SelectActivity theActivity;
    public SelectHandler(SelectActivity mSelectActivity) {
        theActivity = mSelectActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case CHAGE_CITY:
                break;
        }

    }
}
