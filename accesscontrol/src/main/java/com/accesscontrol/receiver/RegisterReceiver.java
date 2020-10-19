package com.accesscontrol.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.accesscontrol.handler.ChatHandler;
import com.accesscontrol.handler.RegisterHandler;
import com.accesscontrol.handler.SettingHandler;
import com.accesscontrol.view.LocationView;

import intersky.appbase.BaseReceiver;

public class RegisterReceiver extends BaseReceiver {

    private Handler mHandler;

    public RegisterReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(LocationView.ACTION_GET_LOCATION);
        intentFilter.addAction("com.ynh.broadcast.ip");
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
        else if (intent.getAction().equals("com.ynh.broadcast.ip")) {

            Message msg = new Message();
            msg.what = SettingHandler.SET_IP;
            msg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(msg);
        }

    }

}
