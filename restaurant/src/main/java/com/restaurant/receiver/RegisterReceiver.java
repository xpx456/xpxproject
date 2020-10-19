package com.restaurant.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.restaurant.handler.RegisterHandler;
import com.restaurant.handler.SettingHandler;
import com.restaurant.view.LocationView;

import intersky.appbase.BaseReceiver;

public class RegisterReceiver extends BaseReceiver {

    private Handler mHandler;

    public RegisterReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(LocationView.ACTION_GET_LOCATION);
        intentFilter.addAction("com.hyzn.sdk.EthernetAPI");
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
        else if (intent.getAction().equals("com.hyzn.sdk.EthernetAPI")) {

            Message msg = new Message();
            msg.what = SettingHandler.SET_IP;
            msg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(msg);
        }

    }

}
