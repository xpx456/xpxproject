package com.accessmaster.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.accessmaster.handler.ChatHandler;
import com.accessmaster.prase.MqttPrase;

import intersky.appbase.BaseReceiver;

public class ChatReceiver extends BaseReceiver {

    public static final String ACTION_CALL_MASTER = "ACTION_CALL_MASTER";

    public static final String ACITON_RECEICE_CALL = "ACITON_RECEICE_CALL";
    private Handler mHandler;



    public ChatReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(MqttPrase.ACTION_OTHER_CONNECT);
        intentFilter.addAction(MqttPrase.ACTION_STOP_CONNECT);
        intentFilter.addAction(MqttPrase.ACTION_ACCEPT_ICE);
        intentFilter.addAction(ACTION_CALL_MASTER);

        intentFilter.addAction(MqttPrase.ACTION_ANSWER_RECEIVER);
        intentFilter.addAction(MqttPrase.ACTION_RECEIVER_ICE);
        intentFilter.addAction(MqttPrase.ACTION_ASK_SEND_ICE);
        intentFilter.addAction(ACITON_RECEICE_CALL);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(MqttPrase.ACTION_OTHER_CONNECT)) {

            Message msg = new Message();
            msg.what = ChatHandler.CHAT_ACCEPT;
            msg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(msg);
        }
        else if(intent.getAction().equals(MqttPrase.ACTION_STOP_CONNECT))
        {

            Message msg = new Message();
            msg.what = ChatHandler.STOP_CONNECT;
            msg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(msg);
        }
        else if(intent.getAction().equals(MqttPrase.ACTION_ACCEPT_ICE))
        {

            Message msg = new Message();
            msg.what = ChatHandler.ACCEPT_ICE;
            msg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(msg);
        }
        else if(intent.getAction().equals(ACTION_CALL_MASTER))
        {

            Message msg = new Message();
            msg.what = ChatHandler.CALL_MASTER;
            msg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(msg);
        }
        else if(intent.getAction().equals(MqttPrase.ACTION_ANSWER_RECEIVER))
        {

            Message msg = new Message();
            msg.what = ChatHandler.ANSWER_RECEIVER;
            msg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(msg);
        }
        else if(intent.getAction().equals(MqttPrase.ACTION_RECEIVER_ICE))
        {

            Message msg = new Message();
            msg.what = ChatHandler.ICE_RECEIVER;
            msg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(msg);
        }
        else if(intent.getAction().equals(MqttPrase.ACTION_ASK_SEND_ICE))
        {

            Message msg = new Message();
            msg.what = ChatHandler.ICE_RECEIVER;
            msg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(msg);
        }
        else if(intent.getAction().equals(ACITON_RECEICE_CALL))
        {
            Message msg = new Message();
            msg.what = ChatHandler.RECEIVER_CALL;
            msg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(msg);
        }
    }

}
