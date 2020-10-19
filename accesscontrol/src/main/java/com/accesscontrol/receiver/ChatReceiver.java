package com.accesscontrol.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.accesscontrol.handler.ChatHandler;
import com.accesscontrol.handler.MainHandler;
import com.accesscontrol.prase.MqttPrase;
import com.accesscontrol.view.AccessControlApplication;

import intersky.appbase.BaseReceiver;

public class ChatReceiver extends BaseReceiver {

    public static final String ACITON_CONNECT_ACCEPT = "ACITON_CONNECT_ACCEPT";
    public static final String ACITON_CONNECT_REFOUS = "ACITON_CONNECT_REFOUS";
    public static final String ACITON_CONNECT_EXIST = "ACITON_CONNECT_EXIST";
    public static final String ACITON_ADD_MASTER = "ACITON_ADD_MASTER";
    public static final String ACITON_START_CONTACT = "ACITON_START_CONTACT";

    public ChatReceiver(Handler handler)
    {
        this.handler = handler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(ACITON_CONNECT_ACCEPT);
        intentFilter.addAction(ACITON_CONNECT_REFOUS);
        intentFilter.addAction(ACITON_CONNECT_EXIST);
        intentFilter.addAction(ACITON_ADD_MASTER);
        intentFilter.addAction(MqttPrase.ACTION_BUESY);
        intentFilter.addAction(ACITON_START_CONTACT);
        intentFilter.addAction(MqttPrase.ACTION_RECEIVER_OFFER);
        intentFilter.addAction(MqttPrase.ACTION_RECEIVER_ICE);
        intentFilter.addAction(MqttPrase.ACTION_ASK_SEND_ICE);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(ACITON_CONNECT_ACCEPT)) {

            Message msg = new Message();
            msg.what = ChatHandler.CHAT_ACCEPT;
            msg.obj = intent;
            if(handler!=null)
                handler.sendMessage(msg);
        }
        else if(intent.getAction().equals(ACITON_CONNECT_REFOUS))
        {
            Message msg = new Message();
            msg.what = ChatHandler.CHAT_REFOUS;
            msg.obj = intent;
            if(handler!=null)
                handler.sendMessage(msg);
        }
        else if(intent.getAction().equals(ACITON_CONNECT_EXIST))
        {
            Message msg = new Message();
            msg.what = ChatHandler.CHAT_EXIST;
            msg.obj = intent;
            if(handler!=null)
                handler.sendMessage(msg);
        }
        else if(intent.getAction().equals(ACITON_ADD_MASTER))
        {
            Message msg = new Message();
            msg.what = ChatHandler.ADD_MASTER;
            msg.obj = intent;
            if(handler!=null)
                handler.sendMessage(msg);
        }
        else if(intent.getAction().equals(MqttPrase.ACTION_BUESY))
        {
            Message msg = new Message();
            msg.what = ChatHandler.BUESY;
            msg.obj = intent;
            if(handler!=null)
                handler.sendMessage(msg);
        }
        else if(intent.getAction().equals(MqttPrase.ACTION_RECEIVER_OFFER))
        {
            Message msg = new Message();
            msg.what = ChatHandler.RECEIVER_OFFER;
            msg.obj = intent;
            if(handler!=null)
                handler.sendMessage(msg);
        }
        else if(intent.getAction().equals(MqttPrase.ACTION_RECEIVER_ICE))
        {
            Message msg = new Message();
            msg.what = ChatHandler.RECEIVER_ICE;
            msg.obj = intent;
            if(handler!=null)
                handler.sendMessage(msg);
        }
        else if(intent.getAction().equals(MqttPrase.ACTION_ASK_SEND_ICE))
        {
            Message msg = new Message();
            msg.what = ChatHandler.SEND_ICE_AGAIN;
            msg.obj = intent;
            if(handler!=null)
                handler.sendMessage(msg);
        }
        else if(intent.getAction().equals(ACITON_START_CONTACT))
        {
            Message msg = new Message();
            msg.what = ChatHandler.START_CONTACE;
            msg.obj = intent;
            if(handler!=null)
                handler.sendMessage(msg);
        }

    }

}
