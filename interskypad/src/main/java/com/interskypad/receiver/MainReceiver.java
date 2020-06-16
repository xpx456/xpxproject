package com.interskypad.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.interskypad.asks.LoginAsks;
import com.interskypad.handler.MainHandler;
import com.interskypad.manager.OrderManager;
import com.interskypad.manager.ProducterManager;

import intersky.appbase.BaseReceiver;


public class MainReceiver extends BaseReceiver {

    public Handler mHandler;

    public MainReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(OrderManager.ACTION_UPDATE_CATALOG_COUNT);
        intentFilter.addAction(ProducterManager.ACTION_SEARCH_SCAN);
        intentFilter.addAction(LoginAsks.ACTION_LOAGIN_OUT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(OrderManager.ACTION_UPDATE_CATALOG_COUNT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MainHandler.UPDATE_ORDER_HIT;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
        else if(intent.getAction().equals(ProducterManager.ACTION_SEARCH_SCAN))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = MainHandler.DO_SEARCH_SCAN;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }

        else if(intent.getAction().equals(LoginAsks.ACTION_LOAGIN_OUT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = LoginAsks.LOGOUT_SUCCESS;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }
    }
}
