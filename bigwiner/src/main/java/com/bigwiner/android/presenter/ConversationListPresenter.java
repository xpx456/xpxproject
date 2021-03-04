package com.bigwiner.android.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigwiner.R;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.handler.ConversationListHandler;
import com.bigwiner.android.receiver.ConversationListReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ChatActivity;
import com.bigwiner.android.view.activity.ConversationListActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.MeetingDetialActivity;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.bigwiner.android.view.adapter.ConversationAdapter;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.chat.ChatUtils;
import intersky.chat.handler.LeaveMessageHandler;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ConversationListPresenter implements Presenter {

    public ConversationListActivity mConversationListActivity;
    public ConversationListHandler mConversationListHandler;

    public ConversationListPresenter(ConversationListActivity mConversationListActivity) {
        mConversationListHandler = new ConversationListHandler(mConversationListActivity);
        this.mConversationListActivity = mConversationListActivity;
        mConversationListActivity.setBaseReceiver(new ConversationListReceiver(mConversationListHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mConversationListActivity, Color.argb(0, 255, 255, 255));
        mConversationListActivity.setContentView(R.layout.activity_conversationslist);
        mConversationListActivity.mToolBarHelper.hidToolbar(mConversationListActivity, (RelativeLayout) mConversationListActivity.findViewById(R.id.buttomaciton));
        mConversationListActivity.measureStatubar(mConversationListActivity, (RelativeLayout) mConversationListActivity.findViewById(R.id.stutebar));
        mConversationListActivity.back = mConversationListActivity.findViewById(R.id.back11);
        mConversationListActivity.back.setOnClickListener(mConversationListActivity.backListener);
        mConversationListActivity.listView = mConversationListActivity.findViewById(R.id.contacts);
        mConversationListActivity.listView.setLayoutManager(new LinearLayoutManager(mConversationListActivity));
        TextView textView = mConversationListActivity.findViewById(R.id.titletext);
        textView.setText(mConversationListActivity.getIntent().getStringExtra("title"));
        if (mConversationListActivity.getIntent().hasExtra("data")) {
            mConversationListActivity.mConversationAdapter = new ConversationAdapter(mConversationListActivity.getIntent().getParcelableArrayListExtra("data"), mConversationListActivity, mConversationListHandler, mConversationListActivity.getIntent().getStringExtra("keyword"));
            mConversationListActivity.listView.setAdapter(mConversationListActivity.mConversationAdapter);
            mConversationListActivity.mConversationAdapter.setOnItemClickListener(mConversationListActivity.startClickListener);
        } else {
            mConversationListActivity.mConversationAdapter = new ConversationAdapter(BigwinerApplication.mApp.conversationManager.collectionConversation.get(Conversation.CONVERSATION_TYPE_MESSAGE), mConversationListActivity, mConversationListHandler);
            mConversationListActivity.listView.setAdapter(mConversationListActivity.mConversationAdapter);
            mConversationListActivity.mConversationAdapter.setOnItemClickListener(mConversationListActivity.startClickListener);
            mConversationListActivity.mConversationAdapter.setConversationFunction(swipClickListener);
        }


    }



    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {
    }

    public void startChat(Conversation conversation) {
        Contacts contacts = BigwinerApplication.mApp.contactManager.contactsHashMap.get(conversation.detialId);
        if (contacts == null) {
            contacts = BigwinerApplication.mApp.contactManager.friendHashMap.get(conversation.detialId);
        }
        if (contacts == null) {
            contacts = new Contacts();
            contacts.mRName = conversation.mTitle;
            contacts.mRecordid = conversation.detialId;
            contacts.mName = conversation.mTitle;
        }
        Intent intent = new Intent(mConversationListActivity, ChatActivity.class);
        intent.putExtra("isow", true);
        intent.putExtra("contacts", contacts);
        mConversationListActivity.startActivity(intent);
    }

    public void onItemClick(Conversation conversation) {
        if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_MEETING)) {
            Meeting meeting = new Meeting();
            meeting.recordid = conversation.detialId;
            Intent intent = new Intent(mConversationListActivity, MeetingDetialActivity.class);
            intent.putExtra("meeting", meeting);
            mConversationListActivity.startActivity(intent);
        } else if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE)) {
            Contacts contacts = BigwinerApplication.mApp.contactManager.contactsHashMap.get(conversation.detialId);
            if (contacts == null) {
                contacts = new Contacts();
                contacts.mRecordid = conversation.detialId;
                contacts.mRName = conversation.mTitle;
                contacts.mName = conversation.mTitle;
            }
            Intent intent = new Intent(mConversationListActivity, ChatActivity.class);
            intent.putExtra("isow", true);
            intent.putExtra("contacts", contacts);
            mConversationListActivity.startActivity(intent);
        } else if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_NOTICE)) {
            Intent intent = new Intent(mConversationListActivity, WebMessageActivity.class);
            if (conversation.sourcePath.length() == 0)
            {
                intent.putExtra("url", DetialAsks.getNoticeNewUrl(conversation.detialId));
                intent.putExtra("detialid",conversation.detialId);
                intent.putExtra("type",conversation.mType);
                intent.putExtra("title",conversation.mTitle);
                intent.putExtra("des",conversation.mSubject);
                intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
            }
            else
            {
                ConversationAsks.getUpdata(conversation.detialId);
                intent.putExtra("url", conversation.sourcePath);
                intent.putExtra("detialid",conversation.detialId);
                intent.putExtra("type",conversation.mType);
                intent.putExtra("title",conversation.mTitle);
                intent.putExtra("des",conversation.mSubject);
                intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
            }

            intent.putExtra("showshare", true);
            mConversationListActivity.startActivity(intent);
        } else if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_NEWS)) {
            Intent intent = new Intent(mConversationListActivity, WebMessageActivity.class);
            if (conversation.sourcePath.length() == 0)
            {
                intent.putExtra("url", DetialAsks.getNoticeNewUrl(conversation.detialId));
                intent.putExtra("detialid",conversation.detialId);
                intent.putExtra("type",conversation.mType);
                intent.putExtra("title",conversation.mTitle);
                intent.putExtra("des",conversation.mSubject);
                intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
            }
            else
            {
                ConversationAsks.getUpdata(conversation.detialId);
                intent.putExtra("url", conversation.sourcePath);
                intent.putExtra("detialid",conversation.detialId);
                intent.putExtra("type",conversation.mType);
                intent.putExtra("title",conversation.mTitle);
                intent.putExtra("des",conversation.mSubject);
                intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
            }
            intent.putExtra("showshare", true);
            mConversationListActivity.startActivity(intent);
        }
    }

    public void menuclick(Conversation conversation ,int id) {
        if(id == R.id.tv_remove)
        {
            Intent intent = new Intent(MainActivity.ACTION_REMOVE_MESSAGE);
            intent.putExtra("msg",conversation);
            intent.setPackage(BigwinerApplication.mApp.getPackageName());
            mConversationListActivity.sendBroadcast(intent);
        }
    }

    public ConversationAdapter.ConversationFunction swipClickListener = new ConversationAdapter.ConversationFunction() {

        @Override
        public void read(Conversation conversation) {
            doRead(conversation);
        }

        @Override
        public void delete(Conversation conversation) {
            doDelete(conversation);
        }

    };

    public void doRead(Conversation msg) {
        BigwinerApplication.mApp.conversationManager.setRead(mConversationListActivity,msg.detialId);
        Intent intent = new Intent(MainActivity.ACTION_UPDATA_MESSAGE);
        intent.setPackage(BigwinerApplication.mApp.getPackageName());
        mConversationListActivity.sendBroadcast(intent);
    }

    public void doDelete(Conversation msg) {
        AppUtils.creatDialogTowButton(mConversationListActivity,mConversationListActivity.getString(R.string.delete_conversation),"",
                mConversationListActivity.getString(R.string.button_word_cancle),mConversationListActivity.getString(R.string.button_delete),
                null,new OnDeleteListener(msg));
    }

    public class OnDeleteListener implements DialogInterface.OnClickListener {

        public Conversation msg;

        public OnDeleteListener(Conversation msg) {
            this.msg = msg;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            BigwinerApplication.mApp.conversationManager.deleteMessage(mConversationListActivity,msg.detialId);
            BigwinerApplication.mApp.chatPagerHashMap.remove(msg.detialId);
            if(ChatUtils.getChatUtils().mLeaveMessageHandler != null) {
                Message message = new Message();
                message.what = LeaveMessageHandler.DELETE_CHAT_SOURCE_DETIAL;
                message.obj = msg.detialId;
                ChatUtils.getChatUtils().mLeaveMessageHandler.sendMessage(message);
            }
            Intent intent = new Intent(MainActivity.ACTION_UPDATA_MESSAGE);
            intent.setPackage(BigwinerApplication.mApp.getPackageName());
            mConversationListActivity.sendBroadcast(intent);
        }
    }
}
