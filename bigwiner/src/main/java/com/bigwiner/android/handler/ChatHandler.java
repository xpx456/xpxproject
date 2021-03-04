package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ChatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import intersky.appbase.PermissionCode;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.chat.R;
import intersky.conversation.BigWinerConversationManager;
import intersky.mywidget.CustomScrollView;
import intersky.xpxnet.net.NetUtils;

public class ChatHandler extends Handler {


    public final static int DOWNLOAD_FIAL = 1230600;
    public final static int DOWNLOAD_UPDATA = 1230601;
    public final static int DOWNLOAD_FINISH = 1230602;
    public final static int DOWNLOAD_AFINISH = 1230612;
    public final static int DOWNLOAD_IMGFINISH = 1230614;
    public final static int ADD_MESSAGE = 1230603;
    public final static int UPDATA_MESSAGE = 1230610;
    public final static int OPEN_ITEM = 1230605;
    public final static int SET_PIC = 1230604;
    public final static int DELETE_MSG = 1230606;
    public final static int CODE_RESULT = 1230607;
    public final static int SCOLL_END = 1230608;
    public final static int SCOLL_END_FINISH = 1230609;
    public final static int DOAUDIO = 1230611;
    public final static int ADD_SEND_ITEM = 1230613;
    public final static int SEND_LOCATION = 1230615;
    public ChatActivity theActivity;

    public ChatHandler(ChatActivity mChatActivity) {
        theActivity = mChatActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case ADD_MESSAGE:
                if(theActivity.mChatPresenter.checkAdd(((Intent)msg.obj).<Conversation>getParcelableArrayListExtra("msgs")) == true)
                {
                    theActivity.chatPager.addConversation(((Intent)msg.obj).getIntExtra("addcount",0));
                    theActivity.mChatPresenter.updataViews(((Intent)msg.obj).getParcelableArrayListExtra("msgs"));
                    Intent intent1 = new Intent();
                    intent1.putExtra("id", theActivity.mContacts.mRecordid);
                    intent1.setAction(BigWinerConversationManager.ACTION_CONVERSATION_SET_READ);
                    intent1.setPackage(theActivity.getPackageName());
                    theActivity.sendBroadcast(intent1);
                }
                else
                {
                    ArrayList<Conversation> conversations = ((Intent)msg.obj).getParcelableArrayListExtra("msgs");
                    for(int i = 0 ; i < conversations.size() ; i++)
                    {
                        theActivity.mChatPresenter.updataView(conversations.get(i));
                    }
                }
                break;
            case UPDATA_MESSAGE:
                theActivity.mChatPresenter.updataView(((Intent)msg.obj).getParcelableExtra("msg"));
                break;
            case DOAUDIO:
                theActivity.mChatPresenter.doAudio(((Intent)msg.obj).getParcelableExtra("msg"));
                break;
            case SET_PIC:
                theActivity.mChatPresenter.setPicResult((Intent) msg.obj);
                break;
            case PermissionCode.PERMISSION_REQUEST_AUDIORECORD:
                theActivity.impuArer.canadudio = true;
                break;
            case NetUtils.NO_INTERFACE:
                theActivity.waitDialog.hide();
                theActivity.impuArer.btnSend.setEnabled(true);
                AppUtils.showMessage(theActivity,theActivity.getString(com.bigwiner.R.string.error_net_network));
                break;
            case NetUtils.NO_NET_WORK:
                theActivity.waitDialog.hide();
                theActivity.impuArer.btnSend.setEnabled(true);
                AppUtils.showMessage(theActivity,theActivity.getString(com.bigwiner.R.string.no_net_network));
                break;
            case OPEN_ITEM:
                theActivity.mChatPresenter.openItem((Intent) msg.obj);
                break;
            case SCOLL_END:
                //theActivity.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                theActivity.scrollView.scrollTo(0,theActivity.chatView.getHeight());
                break;
            case CustomScrollView.CODE_TO_END:
                theActivity.mChatPresenter.onHead();
                break;
            case SCOLL_END_FINISH:
//                theActivity.waitDialog.hide();
                int height3= theActivity.chatView.getHeight();
                int height4 = 0;
                if(msg.obj != null)
                {
                    View view = (View) msg.obj;
                    height4 = view.getHeight();
                }
                theActivity.scrollView.scrollTo(0,height3-msg.arg1+height4);
                break;
            case CODE_RESULT:
                theActivity.mChatPresenter.showAgain((String) msg.obj);
                break;
            case DELETE_MSG:
                theActivity.waitDialog.hide();
                theActivity.mChatPresenter.deleteView((Intent) msg.obj);
                break;
            case ADD_SEND_ITEM:
                theActivity.mChatPresenter.sendtest();
                break;
            case SEND_LOCATION:
                theActivity.mChatPresenter.sendLocation((Intent) msg.obj);
                break;
            case NetUtils.TOKEN_ERROR:
                if(BigwinerApplication.mApp.mAccount.islogin == true) {
                    BigwinerApplication.mApp.logout(BigwinerApplication.mApp.mAppHandler,BigwinerApplication.mApp.appActivityManager.getCurrentActivity());
                    NetUtils.getInstance().cleanTasks();
                }
                break;
        }

    }
}
