package com.intersky.android.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.intersky.android.asks.ConversationAsks;
import com.intersky.android.manager.ServiceManager;
import com.intersky.android.prase.OaMessagePrase;
import com.intersky.android.view.InterskyApplication;
import com.intersky.android.view.activity.MainActivity;
import com.intersky.android.view.activity.LoginActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.function.FunctionUtils;
import intersky.xpxnet.net.NetObject;

public class MainHandler extends Handler {

    public static final int INIT_DATA = 1030006;
    public static final int GET_LEAVE_MESSAGE = 1030007;
    public static final int GET_LOCAL_CONTACTS = 1030008;
    public static final int GET_MAIL_COUNT = 1030009;
    public static final int UPDATE_CONVERSATION = 1030010;
    public static final int GET_TASK_COUNT = 1030011;
    public static final int OPEN_ACTIVITY = 1030012;
    public static final int GET_FUN_BASE_COUNT = 1030013;
    public static final int GET_FUN_OA_COUNT = 1030014;
    public static final int EVENT_EXIST = 1030015;

    MainActivity theActivity;

    public MainHandler(MainActivity mMainActivity) {
        theActivity = mMainActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {

            case INIT_DATA:
                theActivity.mMainPresenter.initData();
                if(theActivity.getIntent().hasExtra("type"))
                {
                    Intent intentnew = new Intent();
                    intentnew.putExtra("type",theActivity.getIntent().getStringExtra("type"));
                    intentnew.putExtra("json",theActivity.getIntent().getStringExtra("json"));
                    intentnew.putExtra("detialid",theActivity.getIntent().getStringExtra("detialid"));
                    intentnew.putExtra("newstart",true);
                    if(!intentnew.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_MESSAGE)) {
                        InterskyApplication.mApp.chatUtils.getLeaveMessage();
                    }
                    Message message = new Message();
                    message.obj = intentnew;
                    message.what = OPEN_ACTIVITY;
                    theActivity.mMainPresenter.mMainHandler.sendMessage(message);
                }
                else
                {
                    InterskyApplication.mApp.chatUtils.getLeaveMessage();
                }
                break;
            case OPEN_ACTIVITY:
                theActivity.mMainPresenter.openConversationActivity((Intent) msg.obj);
                break;
            case GET_LEAVE_MESSAGE:
                theActivity.mMainPresenter.mConversationAdapter.notifyDataSetChanged();
                break;
            case GET_LOCAL_CONTACTS:
                theActivity.mMainPresenter.updataContactView();

                break;
            case GET_MAIL_COUNT:
                theActivity.mMainPresenter.mWorkFragment.updataMailHit();
                FunctionUtils.getInstance().measureWorkHit();
                theActivity.mMainPresenter.setWorkhit();
                break;
            case UPDATE_CONVERSATION:
                theActivity.mMainPresenter.mConversationAdapter.notifyDataSetChanged();
                ArrayList<Conversation> conversations = new ArrayList<Conversation>();
                if(msg.arg1 == 1)
                {
                    intent = (Intent) msg.obj;
                    conversations.add(intent.getParcelableExtra("msg"));
                    InterskyApplication.mApp.chatUtils.addMessage(conversations);
                }
                else if(msg.arg1 == 2)
                {
                    intent = (Intent) msg.obj;
                    conversations.addAll(intent.getParcelableArrayListExtra("msgs"));
                    InterskyApplication.mApp.chatUtils.addMessage(conversations);
                }
                else if(msg.arg1 == 3)
                {
                    intent = (Intent) msg.obj;
                    InterskyApplication.mApp.chatUtils.updataMessage(intent.getParcelableExtra("msg"));
                }
                break;
            case GET_TASK_COUNT:
                if(theActivity.mMainPresenter.mWorkFragment != null)
                theActivity.mMainPresenter.mWorkFragment.updataTaskHit();
                FunctionUtils.getInstance().measureWorkHit();
                theActivity.mMainPresenter.setWorkhit();
                break;
            case GET_FUN_BASE_COUNT:
                theActivity.mMainPresenter.upDateWorkView();
                FunctionUtils.getInstance().measureWorkHit();
                theActivity.mMainPresenter.setWorkhit();
                break;
            case GET_FUN_OA_COUNT:
                theActivity.mMainPresenter.upDateWorkView();
                if(FunctionUtils.getInstance().account.isouter == true) {
                    theActivity.mMainPresenter.mWorkFragment.upDateOa();
                }
                FunctionUtils.getInstance().measureWorkHit();
                theActivity.mMainPresenter.setWorkhit();
                theActivity.mMainPresenter.setWorkhit();
                break;
            case EVENT_EXIST:
                theActivity.waitDialog.hide();
                intent = new Intent(theActivity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                theActivity.startActivity(intent);
                break;
            case ConversationAsks.OAMESSAGE_SUCCESS:
                OaMessagePrase.praseConversationList(theActivity,(NetObject) msg.obj);
                theActivity.mMainPresenter.mConversationAdapter.notifyDataSetChanged();
                break;
            case ConversationAsks.OAMESSAGE_ONE_SUCCESS:
                OaMessagePrase.praseConversationOne((NetObject) msg.obj);
                theActivity.mMainPresenter.mConversationAdapter.notifyDataSetChanged();
                break;
            case ConversationAsks.GROP_SUCCESS:
                OaMessagePrase.praseGropMessage(theActivity,(NetObject) msg.obj);
                theActivity.mMainPresenter.mConversationAdapter.notifyDataSetChanged();
                break;
            case ConversationAsks.OAMESSAGE_READ_SUCCESS:
                if(OaMessagePrase.praseData((NetObject) msg.obj)) {
                    theActivity.readid = "";
                    theActivity.unreadcount = 0;
                    theActivity.mMainPresenter.mConversationAdapter.notifyDataSetChanged();
                }
                break;
        }

    }
}
