package com.bigwiner.android.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.handler.ContactsListHandler;
import com.bigwiner.android.receiver.ContactsListReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ContactDetialActivity;
import com.bigwiner.android.view.activity.ContactsListActivity;
import com.bigwiner.android.view.activity.ConversationListActivity;
import com.bigwiner.android.view.adapter.ContactsAdapter;
import com.bigwiner.android.view.adapter.ContactsSelectAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Collections;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.apputils.TimeUtils;
import intersky.chat.ContactManager;
import intersky.chat.SortContactComparator;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.handler.SendMessageHandler;
import intersky.mywidget.MySlideBar;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ContactsListPresenter implements Presenter {

    public ContactsListActivity mContactsListActivity;
    public ContactsListHandler contactsListHandler;

    public ContactsListPresenter(ContactsListActivity mContactsListActivity) {
        contactsListHandler = new ContactsListHandler(mContactsListActivity);
        this.mContactsListActivity = mContactsListActivity;
        mContactsListActivity.setBaseReceiver(new ContactsListReceiver(contactsListHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mContactsListActivity, Color.argb(0, 255, 255, 255));
        mContactsListActivity.setContentView(R.layout.activity_contactslist);
        mContactsListActivity.mToolBarHelper.hidToolbar(mContactsListActivity, (RelativeLayout) mContactsListActivity.findViewById(R.id.buttomaciton));
        mContactsListActivity.measureStatubar(mContactsListActivity, (RelativeLayout) mContactsListActivity.findViewById(R.id.stutebar));
        mContactsListActivity.msbar = (MySlideBar) mContactsListActivity.findViewById(R.id.slideBar);
        mContactsListActivity.mLetterText = (TextView) mContactsListActivity.findViewById(intersky.chat.R.id.letter_text);
        mContactsListActivity.mRelativeLetter = (RelativeLayout) mContactsListActivity.findViewById(intersky.chat.R.id.letter_layer);
        mContactsListActivity.msbar.setmRelativeLayout(mContactsListActivity.mRelativeLetter);
        mContactsListActivity.msbar.setMletterView(mContactsListActivity.mLetterText);
        mContactsListActivity.msbar.setOnTouchLetterChangeListenner(mContactsListActivity.mOnTouchLetterChangeListenner);
        PullToRefreshView pullToRefreshView = mContactsListActivity.findViewById(R.id.headview);
        pullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.onFooterRefreshComplete();
        pullToRefreshView.onHeaderRefreshComplete();
        pullToRefreshView.getmHeaderView().setVisibility(View.INVISIBLE);
        pullToRefreshView.getmFooterView().setVisibility(View.INVISIBLE);
        pullToRefreshView.setOnFooterRefreshListener(mContactsListActivity.onFooterRefreshListener);
        pullToRefreshView.setOnHeaderRefreshListener(mContactsListActivity.onHeadRefreshListener);
        mContactsListActivity.searchViewLayout = mContactsListActivity.findViewById(R.id.search);
        mContactsListActivity.listView = mContactsListActivity.findViewById(R.id.contacts);
        mContactsListActivity.listView.setLayoutManager(new LinearLayoutManager(mContactsListActivity));
        mContactsListActivity.back = mContactsListActivity.findViewById(R.id.back);
        mContactsListActivity.showList = mContactsListActivity.findViewById(R.id.setting);
        mContactsListActivity.back.setOnClickListener(mContactsListActivity.backListener);
        mContactsListActivity.showList.setOnClickListener(mContactsListActivity.showChatListListener);
        if (mContactsListActivity.getIntent().hasExtra("select") == true) {
            mContactsListActivity.searchViewLayout.setVisibility(View.VISIBLE);
            mContactsListActivity.searchViewLayout.setDotextChange(doTextChange);
            pullToRefreshView.enablePullTorefresh = false;
            pullToRefreshView.enablePullLoadMoreDataStatus = false;
            mContactsListActivity.contactsSelectAdapter = new ContactsSelectAdapter(BigwinerApplication.mApp.contactManager.mContactsFall, mContactsListActivity);
            mContactsListActivity.contactsSelectAdapter.setOnItemClickListener(mContactsListActivity.onContactItemClickListener2);
            mContactsListActivity.contactsSelectSearchAdapter = new ContactsSelectAdapter(mContactsListActivity.mSearchItems, mContactsListActivity);
            mContactsListActivity.contactsSelectSearchAdapter.setOnItemClickListener(mContactsListActivity.onContactItemClickListener2);
            mContactsListActivity.listView.setAdapter(mContactsListActivity.contactsSelectAdapter);
            mContactsListActivity.msbar.setmRelativeLayout(mContactsListActivity.mRelativeLetter);
            mContactsListActivity.msbar.setMletterView(mContactsListActivity.mLetterText);
            mContactsListActivity.showList.setVisibility(View.INVISIBLE);
            updataContactView(false);
        } else if (mContactsListActivity.getIntent().hasExtra("type")) {
            mContactsListActivity.waitDialog.show();
            mContactsListActivity.meeting = mContactsListActivity.getIntent().getParcelableExtra("meeting");
            TextView textView = mContactsListActivity.findViewById(R.id.titletext);
            textView.setText(mContactsListActivity.getIntent().getStringExtra("title"));
            mContactsListActivity.contactsAdapter = new ContactsAdapter(mContactsListActivity.listcontacts, mContactsListActivity);
            mContactsListActivity.contactsAdapter.setOnItemClickListener(mContactsListActivity.onContactItemClickListener);
            switch (mContactsListActivity.getIntent().getIntExtra("type", ContactsListActivity.TYPE_ALL)) {
                case ContactsListActivity.TYPE_MY:
                    ContactsAsks.getMeetingAttMyContacts(mContactsListActivity, contactsListHandler, mContactsListActivity.meeting, mContactsListActivity.contactdetial.pagesize, mContactsListActivity.contactdetial.currentpage);
                    break;
                case ContactsListActivity.TYPE_WANT:
                    ContactsAsks.getMeetingAttWantContacts(mContactsListActivity, contactsListHandler, mContactsListActivity.meeting, mContactsListActivity.contactdetial.pagesize, mContactsListActivity.contactdetial.currentpage);
                    break;
                case ContactsListActivity.TYPE_ALL:
                    ContactsAsks.getMeetingAttAllContacts(mContactsListActivity, contactsListHandler, mContactsListActivity.meeting, mContactsListActivity.contactdetial.pagesize, mContactsListActivity.contactdetial.currentpage);
                    break;
            }
            mContactsListActivity.showList.setVisibility(View.INVISIBLE);
            mContactsListActivity.listView.setAdapter(mContactsListActivity.contactsAdapter);
        } else {
            pullToRefreshView.enablePullTorefresh = false;
            pullToRefreshView.enablePullLoadMoreDataStatus = false;
            mContactsListActivity.contactsAdapter = new ContactsAdapter(BigwinerApplication.mApp.contactManager.mContactsfs, mContactsListActivity);
            mContactsListActivity.listView.setAdapter(mContactsListActivity.contactsAdapter);
            mContactsListActivity.contactsAdapter.setOnItemClickListener(mContactsListActivity.onContactItemClickListener);
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

    public void updataContactView(boolean isShowSearch) {

        if (mContactsListActivity.contactsSelectAdapter != null) {
            mContactsListActivity.contactsSelectAdapter.notifyDataSetChanged();
            mContactsListActivity.contactsSelectSearchAdapter.notifyDataSetChanged();
        }
        if (mContactsListActivity.msbar != null) {
            if (isShowSearch == false) {
                String[] ss = new String[ContactManager.mContactManager.mContactsFHead.size()];
                for (int i = 0; i < ContactManager.mContactManager.mContactsFHead.size(); i++) {
                    ss[i] = ContactManager.mContactManager.mContactsFHead.get(i).getName();
                }
                mContactsListActivity.msbar.setAddletters(ss);
                mContactsListActivity.msbar.requestLayout();
                mContactsListActivity.msbar.setVisibility(View.VISIBLE);
            } else {

                String[] ss = new String[mContactsListActivity.mSearchHeadItems.size()];
                for (int i = 0; i < mContactsListActivity.mSearchHeadItems.size(); i++) {
                    ss[i] = mContactsListActivity.mSearchHeadItems.get(i).getName();
                }
                mContactsListActivity.msbar.setAddletters(ss);
                mContactsListActivity.msbar.requestLayout();
                mContactsListActivity.msbar.setVisibility(View.VISIBLE);
            }

        }
    }

    public void startChatList() {
        Intent intent = new Intent(mContactsListActivity, ConversationListActivity.class);
        mContactsListActivity.startActivity(intent);
    }

    public void startContactsDetial(Contacts contacts) {

        if (mContactsListActivity.getIntent().hasExtra("subject")) {
            doSendMessage(mContactsListActivity.getIntent().getStringExtra("subject"), contacts);
            mContactsListActivity.finish();
        } else if (mContactsListActivity.getIntent().hasExtra("path")) {
            File mFile = new File(mContactsListActivity.getIntent().getStringExtra("path"));
            sendFile(mFile.getPath(), Conversation.MESSAGE_TYPE_IMAGE, contacts);
            mContactsListActivity.finish();
        } else if (mContactsListActivity.getIntent().hasExtra("json")) {
            sendLocation(mContactsListActivity, contacts, mContactsListActivity.getIntent().getStringExtra("json"));
            mContactsListActivity.finish();
        } else if (mContactsListActivity.getIntent().hasExtra("contacts")) {
            Contacts contacts1 = mContactsListActivity.getIntent().getParcelableExtra("contacts");
            doSendCard(contacts1, contacts);
            mContactsListActivity.finish();
        } else {
            Intent intent = new Intent(mContactsListActivity, ContactDetialActivity.class);
            intent.putExtra("contacts", contacts);
            mContactsListActivity.startActivity(intent);
        }

    }

    public void sendLocation(Context context, Contacts contacts, String json) {
        String name = "";
        String path = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            name = jsonObject.getString("locationName");
            path = jsonObject.getString("path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        File mfile = new File(path);
        Conversation msg = new Conversation();
        msg.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
        msg.mRecordId = AppUtils.getguid();
        msg.mTime = TimeUtils.getDate() + " " + TimeUtils.getTimeSecond();
        msg.mTitle = contacts.getmRName();
        msg.detialId = contacts.mRecordid;
        msg.sourcePath = path;
        msg.isRead = true;
        msg.isSendto = true;
        msg.mSubject = "位置:" + name;
        msg.sourceName = json;
        msg.sourceType = Conversation.MESSAGE_TYPE_MAP;
        if (mfile.exists()) {
            msg.sourceSize = mfile.length();
            ConversationAsks.uploadFile(context, BigWinerConversationManager.getInstance().mSendMessageHandler, msg, SendMessageHandler.SEND_UPLOADFILE_SUCCESS);
            BigwinerApplication.mApp.conversationManager.sendMessage(context, msg);
        } else {
            AppUtils.showMessage(context, "找不到文件");
        }
    }

    public void doSendMessage(String message, Contacts contacts) {
        String msgS = message;
        if (null != msgS && (0 != msgS.length())) {
            mContactsListActivity.waitDialog.show();
            Conversation msg = new Conversation();
            msg.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
            msg.mRecordId = AppUtils.getguid();
            msg.mTime = TimeUtils.getDate() + " " + TimeUtils.getTimeSecond();
            msg.mTitle = contacts.getName();
            msg.mSubject = msgS;
            msg.mHit = 0;
            msg.detialId = contacts.mRecordid;
            msg.sourceType = Conversation.MESSAGE_TYPE_NOMAL;
            msg.isRead = true;
            msg.isSendto = true;
            ConversationAsks.sendMsg(mContactsListActivity, BigWinerConversationManager.getInstance().mSendMessageHandler, msg, SendMessageHandler.SEND_MESSAGE_SUCCESS
                    , Calendar.getInstance().getTimeInMillis(), 0);
            BigwinerApplication.mApp.conversationManager.sendMessage(mContactsListActivity, msg);
        } else {
            AppUtils.showMessage(mContactsListActivity, "消息不能为空");
        }
    }

    public void sendFile(String path, int type, Contacts contacts) {
        if (type != Conversation.MESSAGE_TYPE_VOICE) {
            File mfile = new File(path);
            Conversation msg = new Conversation();
            msg.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
            msg.mRecordId = AppUtils.getguid();
            msg.mTime = TimeUtils.getDate() + " " + TimeUtils.getTimeSecond();
            msg.mTitle = contacts.getName();
            msg.detialId = contacts.mRecordid;
            msg.sourceName = mfile.getName();
            msg.sourcePath = path;
            msg.isRead = true;
            msg.isSendto = true;
            if (type == Conversation.MESSAGE_TYPE_IMAGE) {
                msg.mSubject = "[图片]";
                msg.sourceType = Conversation.MESSAGE_TYPE_IMAGE;
            }

            if (mfile.exists()) {
                msg.sourceSize = mfile.length();
                ConversationAsks.uploadFile(mContactsListActivity, BigWinerConversationManager.getInstance().mSendMessageHandler, msg, SendMessageHandler.SEND_UPLOADFILE_SUCCESS);
            } else {
                AppUtils.showMessage(mContactsListActivity, "找不到文件");
            }
        } else {
            File mfile = new File(path);
            if (mfile.exists()) {
                mfile.delete();
            }
        }
    }

    public void doSendCard(Contacts contacts, Contacts contacts2) {
        mContactsListActivity.waitDialog.show();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("carduserid", contacts2.mRecordid);
            jsonObject.put("cardusername", contacts2.getmRName());
            jsonObject.put("carduserphone", contacts2.mMobile);
            Conversation msg = new Conversation();
            msg.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
            msg.mRecordId = AppUtils.getguid();
            msg.mTime = TimeUtils.getDate() + " " + TimeUtils.getTimeSecond();
            msg.mTitle = contacts.getName();
            msg.mSubject = mContactsListActivity.getString(R.string.button_word_card);
            msg.mHit = 0;
            msg.detialId = contacts.mRecordid;
            msg.sourceType = Conversation.MESSAGE_TYPE_CARD;
            msg.isRead = true;
            msg.isSendto = true;
            msg.sourceName = jsonObject.toString();
            ConversationAsks.sendMsg(mContactsListActivity, BigWinerConversationManager.getInstance().mSendMessageHandler, msg, SendMessageHandler.SEND_MESSAGE_SUCCESS
                    , Calendar.getInstance().getTimeInMillis(), 0);
            BigwinerApplication.mApp.conversationManager.sendMessage(mContactsListActivity, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public SearchViewLayout.DoTextChange doTextChange = new SearchViewLayout.DoTextChange() {

        @Override
        public void doTextChange(boolean visiable) {
            if (visiable) {
                mContactsListActivity.mSearchItems.clear();
                mContactsListActivity.mSearchHeadItems.clear();
                mContactsListActivity.typeboolsearch = new boolean[27];
                for (int i = 0; i < BigwinerApplication.mApp.contactManager.mContactsfs.size(); i++) {
                    Contacts contacts = BigwinerApplication.mApp.contactManager.mContactsfs.get(i);
                    if (contacts.getmPingyin().contains(mContactsListActivity.searchViewLayout.getText()) ||
                            contacts.getName().contains(mContactsListActivity.searchViewLayout.getText())) {
                        mContactsListActivity.mSearchItems.add(contacts);
                        String s = contacts.getmPingyin().substring(0, 1).toUpperCase();
                        int pos = CharacterParser.typeLetter.indexOf(s);
                        if (pos != -1) {
                            if (mContactsListActivity.typeboolsearch[pos] == false) {
                                Contacts contactsh = new Contacts(s);
                                mContactsListActivity.mSearchHeadItems.add(contactsh);
                                mContactsListActivity.mSearchItems.add(0, contactsh);
                                mContactsListActivity.typeboolsearch[pos] = true;
                            }
                        } else {
                            s = "#";
                            pos = CharacterParser.typeLetter.indexOf(s);
                            if (pos != -1) {
                                if (mContactsListActivity.typeboolsearch[pos] == false) {
                                    Contacts contactsh = new Contacts(s);
                                    mContactsListActivity.mSearchHeadItems.add(contactsh);
                                    mContactsListActivity.mSearchItems.add(0, contactsh);
                                    mContactsListActivity.typeboolsearch[pos] = true;

                                }
                            }
                        }
                    }
                }
                Collections.sort(mContactsListActivity.mSearchItems, new SortContactComparator());
                Collections.sort(mContactsListActivity.mSearchHeadItems, new SortContactComparator());

                if (mContactsListActivity.showSearch == false) {
                    mContactsListActivity.showSearch = true;
                    mContactsListActivity.listView.setAdapter(mContactsListActivity.contactsSelectSearchAdapter);
                }
                updataContactView(true);
            } else {
                if (mContactsListActivity.showSearch == true) {
                    mContactsListActivity.showSearch = false;
                    mContactsListActivity.listView.setAdapter(mContactsListActivity.contactsSelectAdapter);
                    updataContactView(false);
                }
            }
        }
    };

    public void LetterChange(int s) {
        Contacts model = ContactManager.mContactManager.mContactsFHead.get(s);
        int a = ContactManager.mContactManager.mContactsFall.indexOf(model);
        mContactsListActivity.listView.scrollToPosition(a);
//        mContactsListActivity.listView.setSelectionFromTop(a, 0);
    }
}
