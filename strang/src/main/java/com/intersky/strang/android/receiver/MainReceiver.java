package com.intersky.strang.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.intersky.strang.android.handler.MainHandler;

import intersky.appbase.BaseReceiver;
import intersky.chat.ChatUtils;
import intersky.chat.ContactManager;
import intersky.conversation.ConversationManager;
import intersky.conversation.NotifictionManager;
import intersky.function.FunctionUtils;


public class MainReceiver extends BaseReceiver {

    public Handler mHandler;

    public MainReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        super.handler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(ChatUtils.GET_LAEVE_MESSAGE_FINISHN);
        intentFilter.addAction(ContactManager.GET_LOCAL_CONTACTS_FINISH);
        intentFilter.addAction(FunctionUtils.ACTION_GET_OA_HIT_FINISH);
        intentFilter.addAction(FunctionUtils.ACTION_GET_MAIL_HIT);
        intentFilter.addAction(FunctionUtils.ACTION_GET_TASK_HIT);
        intentFilter.addAction(FunctionUtils.ACTION_GET_BASE_HIT_FINISH);
        intentFilter.addAction(ConversationManager.ACTION_UPDATA_CONVERSATION);
        intentFilter.addAction(ConversationManager.ACTION_ADD_CONVERSATION);
        intentFilter.addAction(ConversationManager.ACTION_ADD_CONVERSATION_LIST);
        intentFilter.addAction(ConversationManager.ACTION_DELETE_CONVERSATION);
        intentFilter.addAction(ConversationManager.ACTION_REMOVE_CONVERSATION);
        intentFilter.addAction(ConversationManager.ACTION_READ_CONVERSATION);
        intentFilter.addAction(NotifictionOpenReceiver.ACTION_NOTIFICTION_OPEN_ACTIVITY);
        intentFilter.addAction(NotifictionManager.ACTION_UPDATA_CONVERSATION_CLOD);
        intentFilter.addAction(NotifictionManager.ACTION_KICK_OUT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ChatUtils.GET_LAEVE_MESSAGE_FINISHN))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.obj = intent;
                msg.what = MainHandler.GET_LEAVE_MESSAGE;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(ContactManager.GET_LOCAL_CONTACTS_FINISH))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.obj = intent;
                msg.what = MainHandler.GET_LOCAL_CONTACTS;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(FunctionUtils.ACTION_GET_MAIL_HIT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.obj = intent;
                msg.what = MainHandler.GET_MAIL_COUNT;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(FunctionUtils.ACTION_GET_TASK_HIT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.obj = intent;
                msg.what = MainHandler.GET_TASK_COUNT;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(FunctionUtils.ACTION_GET_BASE_HIT_FINISH))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.obj = intent;
                msg.what = MainHandler.GET_FUN_BASE_COUNT;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(FunctionUtils.ACTION_GET_OA_HIT_FINISH))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.obj = intent;
                msg.what = MainHandler.GET_FUN_OA_COUNT;
                mHandler.sendMessage(msg);
            }
        }
        else if(intent.getAction().equals(ConversationManager.ACTION_UPDATA_CONVERSATION)
        ||intent.getAction().equals(ConversationManager.ACTION_ADD_CONVERSATION)
                ||intent.getAction().equals(ConversationManager.ACTION_ADD_CONVERSATION_LIST)
                ||intent.getAction().equals(ConversationManager.ACTION_DELETE_CONVERSATION)
                ||intent.getAction().equals(ConversationManager.ACTION_REMOVE_CONVERSATION)
                ||intent.getAction().equals(ConversationManager.ACTION_READ_CONVERSATION))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.obj = intent;
                msg.what = MainHandler.UPDATE_CONVERSATION;
                if(intent.getAction().equals(ConversationManager.ACTION_ADD_CONVERSATION))
                msg.arg1 = 1;
                else if(intent.getAction().equals(ConversationManager.ACTION_ADD_CONVERSATION_LIST))
                    msg.arg1 = 2;
                else
                    msg.arg1 = 3;
                mHandler.sendMessage(msg);
            }
        }
        else
        {
            super.onReceive(context, intent);
        }
    }
}
