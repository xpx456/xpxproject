package com.intersky.strang.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.intersky.strang.android.view.activity.ServiceListActivity;

public class ServiceListHandler extends Handler {

    public static final int UPDATA_LIST = 1030005;

    public ServiceListActivity theActivity;

    public ServiceListHandler(ServiceListActivity mServiceListActivity) {
        theActivity = mServiceListActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case UPDATA_LIST:
                theActivity.mServiceListPresenter.upDataList((Intent) msg.obj);
                break;
        }

    }
}
