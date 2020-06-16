package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.handler.ChatHandler;
import com.bigwiner.android.view.activity.MainActivity;

import intersky.appbase.BaseReceiver;
import intersky.chat.ChatUtils;
import intersky.conversation.BigWinerConversationManager;;


public class ChatReceiver extends BaseReceiver {

    public Handler mHandler;

    public ChatReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(ChatUtils.ACTION_CHAT_PHOTO_SELECT);
        intentFilter.addAction(ChatUtils.ACTION_ADD_CHAT_MESSAGE);
        intentFilter.addAction(ChatUtils.ACTION_OPEN_ITEM);
        intentFilter.addAction(ChatUtils.ACTION_CHAT_LOCATION_SELECT);
        intentFilter.addAction(ChatUtils.ACTION_CHAT_CARD_SELECT);
        intentFilter.addAction(BigWinerConversationManager.ACTION_ADD_CONVERSATION_MESSAGE);
        intentFilter.addAction(BigWinerConversationManager.ACTION_UPDATA_CONVERSATION_MESSAGE);
        intentFilter.addAction(BigWinerConversationManager.ACTION_DELETE_CONVERSATION_MESSAGE);
        intentFilter.addAction(BigWinerConversationManager.ACTION_DOAUDIO_CONVERSATION_MESSAGE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        if(intent.getAction().equals(ChatUtils.ACTION_CHAT_PHOTO_SELECT))
        {
            Message smsg = new Message();
            smsg.what = ChatHandler.SET_PIC;
            smsg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(smsg);
        }
        else if(intent.getAction().equals(ChatUtils.ACTION_ADD_CHAT_MESSAGE))
        {
            Message smsg = new Message();
            smsg.what = ChatHandler.ADD_MESSAGE;
            smsg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(smsg);
        }
        else if(intent.getAction().equals(BigWinerConversationManager.ACTION_ADD_CONVERSATION_MESSAGE))
        {
            Message smsg = new Message();
            smsg.what = ChatHandler.ADD_MESSAGE;
            smsg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(smsg);
        }
        else if(intent.getAction().equals(BigWinerConversationManager.ACTION_UPDATA_CONVERSATION_MESSAGE))
        {
            Message smsg = new Message();
            smsg.what = ChatHandler.UPDATA_MESSAGE;
            smsg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(smsg);
        }
        else if(intent.getAction().equals(BigWinerConversationManager.ACTION_DOAUDIO_CONVERSATION_MESSAGE))
        {
            Message smsg = new Message();
            smsg.what = ChatHandler.DOAUDIO;
            smsg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(smsg);
        }
        else if(intent.getAction().equals(BigWinerConversationManager.ACTION_DELETE_CONVERSATION_MESSAGE))
        {
            Message smsg = new Message();
            smsg.what = ChatHandler.DELETE_MSG;
            smsg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(smsg);
        }
        else if(intent.getAction().equals(ChatUtils.ACTION_OPEN_ITEM))
        {
            Message smsg = new Message();
            smsg.what = ChatHandler.OPEN_ITEM;
            smsg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(smsg);
        }
        else if(intent.getAction().equals(ChatUtils.ACTION_CHAT_LOCATION_SELECT))
        {
            Message smsg = new Message();
            smsg.what = ChatHandler.SEND_LOCATION;
            smsg.obj = intent;
            if(mHandler!=null)
                mHandler.sendMessage(smsg);
        }

    }
}
