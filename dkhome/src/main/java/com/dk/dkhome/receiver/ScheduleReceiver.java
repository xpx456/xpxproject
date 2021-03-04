package com.dk.dkhome.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.dk.dkhome.handler.MainHandler;
import com.dk.dkhome.handler.ScheduleHandler;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.activity.ScheduleActivity;

import intersky.appbase.BaseReceiver;



public class ScheduleReceiver extends BaseReceiver {

    public Handler mHandler;

    public ScheduleReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(DkhomeApplication.ACTION_ADD_PLAN);
        intentFilter.addAction(DkhomeApplication.ACTION_UPDATA_PLAN);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(DkhomeApplication.ACTION_UPDATA_PLAN)){
            Message message = new Message();
            message.obj = intent;
            message.what = ScheduleHandler.UPDATA_EVENT;
            if(mHandler != null)
            {
                mHandler.sendMessage(message);
            }
        }
        else if(intent.getAction().equals(DkhomeApplication.ACTION_ADD_PLAN)){
            Message message = new Message();
            message.obj = intent;
            message.what = ScheduleHandler.UPDATA_EVENT;
            if(mHandler != null)
            {
                mHandler.sendMessage(message);
            }
        }

    }
}
