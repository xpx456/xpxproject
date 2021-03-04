package com.interskypad.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.interskypad.handler.LoginHandler;
import com.interskypad.manager.ServiceManager;

import intersky.appbase.BaseReceiver;


public class LoginReceiver extends BaseReceiver {

    public Handler mHandler;

    public LoginReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceManager.ACTION_SERVICE_UPDATA);
        intentFilter.addAction(ServiceManager.ACTION_SERVICE_DELETE);
        intentFilter.addAction(ServiceManager.ACTION_LOGINOUT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ServiceManager.ACTION_SERVICE_UPDATA))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = LoginHandler.UPDATA_SNIPER;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
        else  if(intent.getAction().equals(ServiceManager.ACTION_SERVICE_DELETE))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = LoginHandler.DELETE_SNIPER;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }

    }
}
