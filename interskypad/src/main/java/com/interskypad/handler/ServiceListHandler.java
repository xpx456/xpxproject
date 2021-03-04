package com.interskypad.handler;

import android.content.Intent;
import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo;
import android.os.Handler;
import android.os.Message;

import com.interskypad.view.activity.ServiceListActivity;

import java.lang.ref.WeakReference;

public class ServiceListHandler extends Handler {

    public static final int UPDATA_LIST = 2030005;

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
