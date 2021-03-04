package com.accessmaster.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;


import com.accessmaster.handler.RegisterHandler;
import com.accessmaster.view.LocationView;

import intersky.appbase.BaseReceiver;

public class RegisterReceiver extends BaseReceiver {

    private Handler mHandler;

    public RegisterReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(LocationView.ACTION_GET_LOCATION);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(LocationView.ACTION_GET_LOCATION)) {

            Message msg = new Message();
            msg.what = RegisterHandler.EVENT_SET_ADDRESS;
            msg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(msg);
        }

    }

}
