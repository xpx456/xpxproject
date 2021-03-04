package com.intersky.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.intersky.android.handler.ServiceListHandler;
import com.intersky.android.view.activity.ServiceSettingActivity;

import intersky.appbase.BaseReceiver;


public class ServiceListReceiver extends BaseReceiver {

    public Handler mHandler;

    public ServiceListReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceSettingActivity.ACTION_SERVICE_UPDATA);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ServiceSettingActivity.ACTION_SERVICE_UPDATA))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = ServiceListHandler.UPDATA_LIST;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }
        }
    }
}
