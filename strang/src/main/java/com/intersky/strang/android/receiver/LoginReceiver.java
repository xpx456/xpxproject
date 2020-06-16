package com.intersky.strang.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.intersky.strang.android.handler.LoginHandler;
import com.intersky.strang.android.manager.ServiceManager;
import com.intersky.strang.android.view.activity.LoginActivity;

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
        intentFilter.addAction(LoginActivity.ACTION_UPDATA_BUDGE);
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
        else if (intent.getAction().equals(ServiceManager.ACTION_LOGINOUT))
        {
            if(mHandler != null)
            {
//                Message msg = new Message();
//                msg.what = LoginHandler.UPDATA_BUDGE;
//                msg.obj = intent;
//                mHandler.sendMessage(msg);
            }
        }
        else if (intent.getAction().equals(LoginActivity.ACTION_UPDATA_BUDGE))
        {
            if(mHandler != null)
            {
//                Message msg = new Message();
//                msg.what = LoginHandler.UPDATA_BUDGE;
//                msg.obj = intent;
//                mHandler.sendMessage(msg);
            }
        }
    }
}
