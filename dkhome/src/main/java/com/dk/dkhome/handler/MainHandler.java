package com.dk.dkhome.handler;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.dk.dkhome.asks.CourseAsks;
import com.dk.dkhome.view.activity.MainActivity;

import intersky.xpxnet.net.NetObject;
import xpx.bluetooth.BlueToothHandler;


//01


public class MainHandler extends Handler {


    public MainActivity theActivity;
    public static final int ADD_PLAN = 100000;
    public static final int UPDATA_PLAN = 100001;
    public static final int DELETE_PLAN = 100003;
    public static final int UPDATA_VIEW = 100002;
    public MainHandler(MainActivity mLoginActivity) {
        theActivity = mLoginActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent;
        switch (msg.what) {
            case ADD_PLAN:
                intent = (Intent) msg.obj;
                theActivity.mMainPresenter.dairyView.addView(intent.getParcelableExtra("plan"));
                break;
            case UPDATA_PLAN:
                intent = (Intent) msg.obj;
                theActivity.mMainPresenter.dairyView.updataView(intent.getParcelableExtra("plan"));
                break;
            case DELETE_PLAN:
                intent = (Intent) msg.obj;
                theActivity.mMainPresenter.dairyView.deleteView(intent.getParcelableExtra("plan"));
                break;
            case CourseAsks.EVENT_GETCOURSE_SUCCESS:
                theActivity.mMainPresenter.courseView.praseCourse((NetObject) msg.obj);
                break;
            case UPDATA_VIEW:
                theActivity.mMainPresenter.dairyView.updataData();
                break;
        }

    }
}
