package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.ConversationPrase;
import com.bigwiner.android.prase.DetialPrase;
import com.bigwiner.android.prase.ImPrase;
import com.bigwiner.android.prase.LoginPrase;
import com.bigwiner.android.prase.SourcePrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.io.File;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideApp;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.conversation.handler.SendMessageHandler;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//04

public class AppHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case ConversationAsks.BASE_DATA_RESULT:
                ConversationPrase.praseBaseData(BigwinerApplication.mApp, (NetObject) msg.obj);
                if(BigwinerApplication.mApp.ports.hashMap.size() == 0)
                {
                    BigwinerApplication.mApp.ports.clear();
                    BigwinerApplication.mApp.positions.clear();
                    BigwinerApplication.mApp.businessareaSelect.clear();
                    BigwinerApplication.mApp.businesstypeSelect.clear();
                    ConversationAsks.getBaseData(BigwinerApplication.mApp,BigwinerApplication.mApp.mAppHandler,"all");
                }
                break;
            case ConversationAsks.BASE_DATA_FAIL:
                ConversationAsks.getBaseData(BigwinerApplication.mApp,BigwinerApplication.mApp.mAppHandler,"all");
                break;
            case NetUtils.NO_INTERFACE:
            case NetUtils.NO_NET_WORK:
                ConversationAsks.getBaseData(BigwinerApplication.mApp,BigwinerApplication.mApp.mAppHandler,"all");
                break;
            case ConversationAsks.NEW_NOTICE_RESULT:
                if(BigwinerApplication.mApp.conversationManager.messageback.size() > 0)
                {
                    BigwinerApplication.mApp.conversationManager.mConversations.addAll(BigwinerApplication.mApp.conversationManager.messageback);
                    BigwinerApplication.mApp.conversationManager.messageback.clear();
                }
                ConversationPrase.praseConversationNoticeAndNews(BigwinerApplication.mApp, (NetObject) msg.obj);
                if(((NetObject) msg.obj).item.equals(ConversationAsks.TYPE_NOTICE))
                {
                    BigwinerApplication.mApp.conversationManager.noticefinish = true;
                }
                else
                {
                    BigwinerApplication.mApp.conversationManager.newfinish = true;
                }
                if(BigwinerApplication.mApp.conversationManager.newfinish && BigwinerApplication.mApp.conversationManager.noticefinish && BigwinerApplication.mApp.conversationManager.meetingfinish)
                {
                    Intent intent = new Intent(MainActivity.ACTION_CONVERSATION_ALL_UPDATA);
                    intent.setPackage(BigwinerApplication.mApp.getPackageName());
                    BigwinerApplication.mApp.sendBroadcast(intent);
                }
                Intent intent = new Intent(MainActivity.ACTION_CONVERSATION_NOTICE_UPDATA);
                intent.setPackage(BigwinerApplication.mApp.getPackageName());
                BigwinerApplication.mApp.sendBroadcast(intent);
                break;
            case ConversationAsks.MEETING_RESULT:
                if(BigwinerApplication.mApp.conversationManager.messageback.size() > 0)
                {
                    BigwinerApplication.mApp.conversationManager.mConversations.addAll(BigwinerApplication.mApp.conversationManager.messageback);
                    BigwinerApplication.mApp.conversationManager.messageback.clear();
                }
                ConversationPrase.praseConversationMeeting(BigwinerApplication.mApp, (NetObject) msg.obj);
                BigwinerApplication.mApp.conversationManager.meetingfinish = true;
                Intent intent1 = new Intent(MainActivity.ACTION_CONVERSATION_MEETING_UPDATA);
                intent1.setPackage(BigwinerApplication.mApp.getPackageName());
                BigwinerApplication.mApp.sendBroadcast(intent1);
                if(BigwinerApplication.mApp.conversationManager.newfinish && BigwinerApplication.mApp.conversationManager.noticefinish && BigwinerApplication.mApp.conversationManager.meetingfinish)
                {
                    Intent intent2 = new Intent(MainActivity.ACTION_CONVERSATION_ALL_UPDATA);
                    intent2.setPackage(BigwinerApplication.mApp.getPackageName());
                    BigwinerApplication.mApp.sendBroadcast(intent2);
                }
                break;
            case NetUtils.TOKEN_ERROR:
                if(BigwinerApplication.mApp.mAccount.islogin == true) {
                    NetObject netObject = (NetObject) msg.obj;
                    AppUtils.showMessage(BigwinerApplication.mApp.appActivityManager.getCurrentActivity(),BigwinerApplication.mApp.getString(R.string.token_error));
                    BigwinerApplication.mApp.logout(BigwinerApplication.mApp.mAppHandler,BigwinerApplication.mApp.appActivityManager.getCurrentActivity());
                    NetUtils.getInstance().cleanTasks();
                }
                break;
        }

    }
}
