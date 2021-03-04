package com.accesscontrol.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.accesscontrol.handler.ChatHandler;
import com.accesscontrol.handler.SettingHandler;
import com.accesscontrol.prase.MqttPrase;

import intersky.appbase.BaseReceiver;

public class SettingReceiver extends BaseReceiver {

    private Handler mHandler;

    public SettingReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction("com.ynh.broadcast.ip");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals("com.ynh.broadcast.ip")) {

            Message msg = new Message();
            msg.what = SettingHandler.SET_IP;
            msg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(msg);
        }


    }

}
