package com.bigwiner.android.presenter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.Company;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.receiver.MainReceiver;
import com.bigwiner.android.view.activity.ChatActivity;
import com.bigwiner.android.view.activity.CompanyDetialActivity;
import com.bigwiner.android.view.activity.ComplaintActivity;
import com.bigwiner.android.view.activity.ContactDetialActivity;
import com.bigwiner.android.view.activity.MeetingDetialActivity;
import com.bigwiner.android.view.activity.PicViewActivity;
import com.bigwiner.android.view.activity.SailApplyActivity;
import com.bigwiner.android.view.activity.SailMemberActivity;
import com.bigwiner.android.view.activity.SettingActivity;
import com.bigwiner.android.view.activity.SourceAddListActivity;
import com.bigwiner.android.view.activity.SourceDetialActivity;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.bigwiner.R;
import com.bigwiner.android.handler.MainHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.adapter.ContactsAdapter;
import com.bigwiner.android.view.adapter.ConversationAdapter;
import com.bigwiner.android.view.adapter.SourceListAdapter;
import com.bigwiner.android.view.fragment.ContactsFragment;
import com.bigwiner.android.view.fragment.ConversationFragment;
import com.bigwiner.android.view.fragment.MyFragment;
import com.bigwiner.android.view.fragment.SailFragment;
import com.bigwiner.android.view.fragment.SourceFragment;
import com.bigwiner.android.view.fragment.ToolsFragment;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.AppActivityManager;
import intersky.appbase.FragmentTabAdapter;
import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.apputils.SystemUtil;
import intersky.budge.shortcutbadger.ShortcutBadger;
import intersky.chat.ChatUtils;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.database.BigWinerDBHelper;
import xpx.com.toolbar.utils.ToolBarHelper;

public class MainPresenter implements Presenter {

    public MainActivity mMainActivity;
    public MainHandler mMainHandler;


    public MainPresenter(MainActivity mMainActivity) {
        this.mMainActivity = mMainActivity;
        this.mMainHandler = new MainHandler(mMainActivity);
        mMainActivity.setBaseReceiver(new MainReceiver(mMainHandler));
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        BigwinerApplication.mApp.chatUtils.getChatSourceDownload();
//        BigwinerApplication.mApp.chatUtils.upDataHeadList();
        BigwinerApplication.mApp.conversationManager.setUpload(mMainActivity.uploadFile);
        if(BigwinerApplication.mApp.mUpDataManager != null)
        BigwinerApplication.mApp.mUpDataManager.checkVersin();
        ToolBarHelper.setSutColor(mMainActivity, Color.argb(0, 255, 255, 255));
        mMainActivity.setContentView(R.layout.activity_main);
        mMainActivity.shade = (RelativeLayout) mMainActivity.findViewById(R.id.shade);
        mMainActivity.mToolBarHelper.hidToolbar(mMainActivity, (RelativeLayout) mMainActivity.findViewById(R.id.buttomaciton));
        mMainActivity.mConversation = mMainActivity.findViewById(R.id.conversation);
        mMainActivity.mConversationImg = mMainActivity.findViewById(R.id.conversation_image);
        mMainActivity.mConversationTxt = mMainActivity.findViewById(R.id.conversation_text);
        mMainActivity.mConversationHit = mMainActivity.findViewById(R.id.conversation_hit);
        mMainActivity.mContacts = mMainActivity.findViewById(R.id.contacts);
        mMainActivity.mContactsImg = mMainActivity.findViewById(R.id.contacts_image);
        mMainActivity.mContactsTxt = mMainActivity.findViewById(R.id.contacts_text);
        mMainActivity.mSource = mMainActivity.findViewById(R.id.source);
        mMainActivity.mSourceImg = mMainActivity.findViewById(R.id.source_image);
        mMainActivity.mSourceTxt = mMainActivity.findViewById(R.id.source_text);
        mMainActivity.mTools = mMainActivity.findViewById(R.id.tools);
        mMainActivity.mToolsImg = mMainActivity.findViewById(R.id.tools_image);
        mMainActivity.mToolsTxt = mMainActivity.findViewById(R.id.tools_text);
        mMainActivity.mMy = mMainActivity.findViewById(R.id.my);
        mMainActivity.mMyImg = mMainActivity.findViewById(R.id.my_image);
        mMainActivity.mMyTxt = mMainActivity.findViewById(R.id.my_text);
        mMainActivity.mConversationFragment = new ConversationFragment();
        mMainActivity.mContactsFragment = new ContactsFragment();
        mMainActivity.mSourceFragment = new SourceFragment();
        mMainActivity.mSailFragment = new SailFragment();
        mMainActivity.mMyFragment = new MyFragment();
        mMainActivity.mFragments.add(mMainActivity.mConversationFragment);
        mMainActivity.mFragments.add(mMainActivity.mContactsFragment);
        mMainActivity.mFragments.add(mMainActivity.mSourceFragment);
        mMainActivity.mFragments.add(mMainActivity.mSailFragment);
        mMainActivity.mFragments.add(mMainActivity.mMyFragment);
        mMainActivity.tabAdapter = new FragmentTabAdapter(mMainActivity, mMainActivity.mFragments, R.id.tab_content);
        RelativeLayout relativeLayout = mMainActivity.findViewById(R.id.tab_content);
//        StatusBarUtil.setTranslucentForImageView(mMainActivity, 0, relativeLayout);
        mMainActivity.mConversation.setOnClickListener(mMainActivity.showConverstionListener);
        mMainActivity.mContacts.setOnClickListener(mMainActivity.showContactsListener);
        mMainActivity.mSource.setOnClickListener(mMainActivity.showSourceListener);
        mMainActivity.mTools.setOnClickListener(mMainActivity.showToolsListener);
        mMainActivity.mMy.setOnClickListener(mMainActivity.showMyListener);
        mMainActivity.allAdapter = new ConversationAdapter(BigwinerApplication.mApp.conversationManager.mConversations, mMainActivity,mMainHandler);
        mMainActivity.meessageAdapter = new ConversationAdapter(BigwinerApplication.mApp.conversationManager.collectionConversation.get(Conversation.CONVERSATION_TYPE_MESSAGE), mMainActivity,mMainHandler);
        mMainActivity.noticeAdapter = new ConversationAdapter(BigwinerApplication.mApp.conversationManager.collectionConversation.get(Conversation.CONVERSATION_TYPE_NOTICE), mMainActivity,mMainHandler);
        mMainActivity.newsAdapter = new ConversationAdapter(BigwinerApplication.mApp.conversationManager.collectionConversation.get(Conversation.CONVERSATION_TYPE_NEWS), mMainActivity,mMainHandler);
        mMainActivity.meetingAdapter = new ConversationAdapter(BigwinerApplication.mApp.conversationManager.collectionConversation.get(Conversation.CONVERSATION_TYPE_MEETING), mMainActivity,mMainHandler);
        mMainActivity.contactsAdapter = new ContactsAdapter(BigwinerApplication.mApp.contactManager.mContactss, mMainActivity);
        mMainActivity.searchAdapter = new ContactsAdapter(BigwinerApplication.mApp.contactManager.mContactssearch, mMainActivity);
        mMainActivity.allSourceAdapter = new SourceListAdapter(mMainActivity.sourceAllData,mMainActivity,mMainHandler,mMainActivity.waitDialog);
        mMainActivity.wantSourceAdapter = new SourceListAdapter(mMainActivity.sourceWantData,mMainActivity,mMainHandler,mMainActivity.waitDialog);
        mMainActivity.gaveSourceAdapter = new SourceListAdapter(mMainActivity.sourceGaveData,mMainActivity,mMainHandler,mMainActivity.waitDialog);
        mMainActivity.searchSourceAdapter = new SourceListAdapter(mMainActivity.sourceSearchData,mMainActivity,mMainHandler,mMainActivity.waitDialog);

        if(!(BigwinerApplication.mApp.conversationManager.newfinish && BigwinerApplication.mApp.conversationManager.noticefinish && BigwinerApplication.mApp.conversationManager.meetingfinish))
        mMainActivity.waitDialog.show();

//        if(ChatUtils.getChatUtils().mLeaveMessageHandler != null )
//        {
//            ConversationAsks.getMessageUnread(mMainActivity,ChatUtils.getChatUtils().mLeaveMessageHandler);
//        }

        DetialAsks.getComplanyList(mMainActivity,mMainHandler);
        if(mMainActivity.isupdatacontact == false)
        {
            mMainActivity.isupdatacontact = true;
            ContactsAsks.getContactsList(mMainActivity,mMainHandler,BigwinerApplication.mApp.contactManager.contactPage.pagesize,BigwinerApplication.mApp.contactManager.contactPage.currentpage
                    ,mMainActivity.getString(R.string.contacts_head_type),mMainActivity.getString(R.string.contacts_head_area),
                    mMainActivity.getString(R.string.contacts_head_city));
        }
        setContent(MainActivity.CONVERSATION_PAGE);
        mMainActivity.sorceAllPageF.isfinish = false;
        mMainActivity.sorceWantPageF.isfinish = false;
        mMainActivity.sorceGavePageF.isfinish = false;
        SourceAsks.getSourceListAll(mMainActivity,mMainHandler,"0",mMainActivity.sorceAllPage.pagesize,mMainActivity.sorceAllPage.currentpage);
        SourceAsks.getSourceListAll(mMainActivity,mMainHandler,"1",mMainActivity.sorceWantPage.pagesize,mMainActivity.sorceWantPage.currentpage);
        SourceAsks.getSourceListAll(mMainActivity,mMainHandler,"2",mMainActivity.sorceGavePage.pagesize,mMainActivity.sorceGavePage.currentpage);
        checkToken();
    }


    public void checkToken()
    {
        if(mMainActivity.checkeFinish == true)
        {
            LoginAsks.checkToken(mMainActivity,mMainHandler);
            mMainActivity.checkeFinish = true;
        }
        //BigwinerApplication.mApp.contactManager.updataKey(mMainActivity);
        mMainHandler.sendEmptyMessageDelayed(MainActivity.CHECK_TOKEN_EVENT,6000);
    }

    public void cancleCheckToken()
    {
        mMainHandler.removeMessages(MainActivity.CHECK_TOKEN_EVENT);
    }

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
//		MobclickAgent.onResume(mSplashActivity);
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
//		MobclickAgent.onPause(mSplashActivity);
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        cancleCheckToken();
        ChatUtils.getChatUtils().stopHeadList();
        mMainHandler = null;
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mMainActivity.backflag)
            {
                BigwinerApplication.mApp.exist();
            }
            else
            {
                AppUtils.showMessage(mMainActivity,mMainActivity.getString(R.string.system_exist));
                mMainActivity.backflag = true;
                mMainHandler.sendEmptyMessageDelayed(MainHandler.BACK_TIME_UPDATE,2000);
            }
            return true;
        }
        else {
            return false;
        }
    }


    public void setContent(int page) {
        mMainActivity.lastpage = mMainActivity.tabAdapter.getCurrentTab();
        mMainActivity.tabAdapter.onCheckedChanged(page);
        switch (page) {
            case MainActivity.CONVERSATION_PAGE:
                mMainActivity.mConversationImg.setImageResource(R.drawable.conversations);
                mMainActivity.mContactsImg.setImageResource(R.drawable.contacts);
                mMainActivity.mSourceImg.setImageResource(R.drawable.source);
                mMainActivity.mToolsImg.setImageResource(R.drawable.tsail);
                mMainActivity.mMyImg.setImageResource(R.drawable.my);
                mMainActivity.mConversationTxt.setTextColor(Color.rgb(0, 0, 0));
                mMainActivity.mContactsTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mSourceTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mToolsTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mMyTxt.setTextColor(Color.rgb(221, 221, 221));
                break;
            case MainActivity.CONTACTS_PAGE:
                mMainActivity.mConversationImg.setImageResource(R.drawable.conversation);
                mMainActivity.mContactsImg.setImageResource(R.drawable.contactss);
                mMainActivity.mSourceImg.setImageResource(R.drawable.source);
                mMainActivity.mToolsImg.setImageResource(R.drawable.tsail);
                mMainActivity.mMyImg.setImageResource(R.drawable.my);
                mMainActivity.mConversationTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mContactsTxt.setTextColor(Color.rgb(0, 0, 0));
                mMainActivity.mSourceTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mToolsTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mMyTxt.setTextColor(Color.rgb(221, 221, 221));
                if(BigwinerApplication.mApp.contactManager.mContactss.size() == 0)
                {
                    if(mMainActivity.isupdatacontact == false)
                    {
                        mMainActivity.isupdatacontact = true;
                        mMainActivity.waitDialog.show();
                        ContactsAsks.getContactsList(mMainActivity,mMainHandler,BigwinerApplication.mApp.contactManager.contactPage.pagesize,BigwinerApplication.mApp.contactManager.contactPage.currentpage
                                ,mMainActivity.getString(R.string.contacts_head_type),mMainActivity.getString(R.string.contacts_head_area),
                                mMainActivity.getString(R.string.contacts_head_city));
                    }
                }
                break;
            case MainActivity.SOURCE_PAGE:
                mMainActivity.mConversationImg.setImageResource(R.drawable.conversation);
                mMainActivity.mContactsImg.setImageResource(R.drawable.contacts);
                mMainActivity.mSourceImg.setImageResource(R.drawable.sources);
                mMainActivity.mToolsImg.setImageResource(R.drawable.tsail);
                mMainActivity.mMyImg.setImageResource(R.drawable.my);
                mMainActivity.mConversationTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mContactsTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mSourceTxt.setTextColor(Color.rgb(0, 0, 0));
                mMainActivity.mToolsTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mMyTxt.setTextColor(Color.rgb(221, 221, 221));
                if(mMainActivity.sourceAllData.size() == 0 && mMainActivity.sorceAllPageF.isfinish == true)
                {
                    mMainActivity.waitDialog.show();
                    SourceAsks.getSourceListAll(mMainActivity,mMainHandler,"0",mMainActivity.sorceAllPage.pagesize,mMainActivity.sorceAllPage.currentpage);
                }
                if(mMainActivity.sourceWantData.size() == 0 && mMainActivity.sorceWantPageF.isfinish == true)
                {
                    SourceAsks.getSourceListAll(mMainActivity,mMainHandler,"1",mMainActivity.sorceWantPage.pagesize,mMainActivity.sorceWantPage.currentpage);
                }
                if(mMainActivity.sourceGaveData.size() == 0 && mMainActivity.sorceGavePageF.isfinish == true)
                {
                    SourceAsks.getSourceListAll(mMainActivity,mMainHandler,"2",mMainActivity.sorceGavePage.pagesize,mMainActivity.sorceGavePage.currentpage);
                }
                break;
            case MainActivity.SAIL_PAGE:
                mMainActivity.mConversationImg.setImageResource(R.drawable.conversation);
                mMainActivity.mContactsImg.setImageResource(R.drawable.contacts);
                mMainActivity.mSourceImg.setImageResource(R.drawable.source);
                mMainActivity.mToolsImg.setImageResource(R.drawable.tsails);
                mMainActivity.mMyImg.setImageResource(R.drawable.my);
                mMainActivity.mConversationTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mContactsTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mSourceTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mToolsTxt.setTextColor(Color.rgb(0, 0, 0));
                mMainActivity.mMyTxt.setTextColor(Color.rgb(221, 221, 221));
                break;
            case MainActivity.MY_PAGE:
                mMainActivity.mConversationImg.setImageResource(R.drawable.conversation);
                mMainActivity.mContactsImg.setImageResource(R.drawable.contacts);
                mMainActivity.mSourceImg.setImageResource(R.drawable.source);
                mMainActivity.mToolsImg.setImageResource(R.drawable.tsail);
                mMainActivity.mMyImg.setImageResource(R.drawable.mys);
                mMainActivity.mConversationTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mContactsTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mSourceTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mToolsTxt.setTextColor(Color.rgb(221, 221, 221));
                mMainActivity.mMyTxt.setTextColor(Color.rgb(0, 0, 0));
                LoginAsks.getUserInfo(mMainActivity,mMainHandler);
                break;
        }
    }

    public void updataConversationHit() {
        AppUtils.setHit( BigWinerDBHelper.getInstance(mMainActivity).getAllUnReadCount(Conversation.CONVERSATION_TYPE_MESSAGE),mMainActivity.mConversationHit);
        int count = BigWinerDBHelper.getInstance(mMainActivity).getAllUnReadCount(Conversation.CONVERSATION_TYPE_MESSAGE);
        ShortcutBadger.applyCount(mMainActivity,AppUtils.getHit(count));
    }

    public void updateConversation(Intent intent) {
        if(intent.getStringExtra("module").equals("MemberManager"))
        {
            if(ChatUtils.getChatUtils().mLeaveMessageHandler != null )
            {
                ConversationAsks.getMessageUnread(mMainActivity,ChatUtils.getChatUtils().mLeaveMessageHandler);
            }
        }
        else if(intent.getStringExtra("module").equals("'MemberIsAuthentication"))
        {
            LoginAsks.getUserInfo(mMainActivity,mMainHandler);
        }
        else if(intent.getStringExtra("module").equals("'NewsManage"))
        {
            for(int i = 0 ; i < BigWinerConversationManager.getInstance().mConversations.size() ; i++)
            {
                if(BigWinerConversationManager.getInstance().mConversations.get(i).mType.equals(Conversation.CONVERSATION_TYPE_NOTICE))
                {
                    BigWinerConversationManager.getInstance().mConversations.remove(i);
                    i--;
                }
                if(BigWinerConversationManager.getInstance().mConversations.get(i).mType.equals(Conversation.CONVERSATION_TYPE_NEWS))
                {
                    BigWinerConversationManager.getInstance().mConversations.remove(i);
                    i--;
                }
            }
            mMainActivity.allAdapter.notifyDataSetChanged();
            mMainActivity.waitDialog.show();
            BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_NOTICE).clear();
            ModuleDetial moduleDetial = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE);
            moduleDetial.reset();
            mMainActivity.noticeAdapter.notifyDataSetChanged();
            ConversationAsks.getNewsAndNotices(mMainActivity,mMainHandler,ConversationAsks.TYPE_NOTICE,
                    BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).pagesize, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).currentpage);
            mMainActivity.waitDialog.show();
            BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_NEWS).clear();
            ModuleDetial moduleDetial1 = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS);
            moduleDetial1.reset();
            mMainActivity.newsAdapter.notifyDataSetChanged();
            ConversationAsks.getNewsAndNotices(mMainActivity,mMainHandler,ConversationAsks.TYPE_NEWS,
                    BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).pagesize, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).currentpage);

        }
        else if(intent.getStringExtra("module").equals("ConferenceManage"))
        {
            for(int i = 0 ; i < BigWinerConversationManager.getInstance().mConversations.size() ; i++)
            {
                if(BigWinerConversationManager.getInstance().mConversations.get(i).mType.equals(Conversation.CONVERSATION_TYPE_MEETING))
                {
                    BigWinerConversationManager.getInstance().mConversations.remove(i);
                    i--;
                }
            }
            BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_MEETING).clear();
            ModuleDetial moduleDetial = BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING);
            moduleDetial.reset();
            mMainActivity.meetingAdapter.notifyDataSetChanged();
            ConversationAsks.getMettings(mMainActivity,mMainHandler, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).pagesize
                    , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).currentpage);
        }
    }

    public void setMessageRead(Intent intent) {
        String rid = intent.getStringExtra("id");
        ArrayList<Conversation> conversations = BigwinerApplication.mApp.conversationManager.messageConversation.get(rid);
        if(conversations != null)
        {
            for(int i = 0 ; i < conversations.size() ; i++)
            {
                if(conversations.get(i).isRead == false)
                {
                    conversations.get(i).isRead = true;
                }

            }
        }
        Conversation conversationhead = BigwinerApplication.mApp.conversationManager.messageHConversation.get(rid);
        if(conversationhead != null)
        {
            conversationhead.mHit = 0;
            conversationhead.isRead = true;
        }
        BigWinerDBHelper.getInstance(mMainActivity).setConversationMessaegRed(rid);
        mMainActivity.allAdapter.notifyDataSetChanged();
        mMainActivity.meessageAdapter.notifyDataSetChanged();
    }

    public void updataMeeting(Intent intent) {
        Meeting meeting = intent.getParcelableExtra("meeting");
        boolean change = false;
        for(int i = 0 ; i < BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_MEETING).size() ; i++)
        {
            Conversation conversation = BigWinerConversationManager.getInstance().collectionConversation.get(Conversation.CONVERSATION_TYPE_MEETING).get(i);
            if(conversation.detialId.equals(meeting.recordid))
            {
                conversation.isSendto = meeting.isjionin;
                change = true;
            }
        }
        if(change)
        {
            mMainActivity.allAdapter.notifyDataSetChanged();
            mMainActivity.meetingAdapter.notifyDataSetChanged();
        }

    }

    public void finishUpdata(Intent intent)
    {
        Conversation msg = intent.getParcelableExtra("item");
        Conversation conversation = BigwinerApplication.mApp.conversationManager.messageConversation2.get(msg.detialId).get(msg.mRecordId);
        if(conversation.sourceSize == 0 )
        {
            conversation.sourceSize = intent.getLongExtra("totalsize",0);
            BigWinerDBHelper.getInstance(mMainActivity).addConversation(conversation);
            BigWinerConversationManager.getInstance().updataMessage(mMainActivity,conversation);
        }
        else
        {
            if(intent.getLongExtra("finishsize",0) == intent.getLongExtra("totalsize",0))
                BigWinerConversationManager.getInstance().updataMessage(mMainActivity,conversation);
        }
    }

    public void doaudio(Intent intent) {
        Conversation msg = intent.getParcelableExtra("item");
        Conversation conversation = BigwinerApplication.mApp.conversationManager.messageConversation2.get(msg.detialId).get(msg.mRecordId);
        if(conversation.sourceSize == 0 )
        {
            conversation.sourceSize = intent.getLongExtra("totalsize",0);
            BigWinerDBHelper.getInstance(mMainActivity).addConversation(conversation);
            BigWinerConversationManager.getInstance().doaudo(mMainActivity,conversation);
        }
        else
        {
            if(intent.getLongExtra("finishsize",0) == intent.getLongExtra("totalsize",0))
                BigWinerConversationManager.getInstance().doaudo(mMainActivity,conversation);
        }
    }

    public void doimg(Intent intent) {
        Conversation msg = intent.getParcelableExtra("item");
        Conversation conversation = BigwinerApplication.mApp.conversationManager.messageConversation2.get(msg.detialId).get(msg.mRecordId);
        conversation.sourceSize = intent.getLongExtra("size",0);
        BigWinerDBHelper.getInstance(mMainActivity).addConversation(conversation);
        BigWinerConversationManager.getInstance().updataMessage(mMainActivity,conversation);
    }

    public void openActivity(Intent intent) {
        if(intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_MESSAGE))
        {
            Contacts contacts = new Contacts();
            contacts.mRecordid = intent.getStringExtra("detialid");
            if(BigwinerApplication.mApp.contactManager.contactsHashMap.containsKey(contacts.mRecordid))
            {

                satartChat(BigwinerApplication.mApp.contactManager.contactsHashMap.get(contacts.mRecordid));
            }
            else if(BigwinerApplication.mApp.contactManager.friendHashMap.containsKey(contacts.mRecordid))
            {
                satartChat(BigwinerApplication.mApp.contactManager.friendHashMap.get(contacts.mRecordid));
            }
            else
            {
                mMainActivity.waitDialog.show();
                ContactsAsks.getContactDetial(mMainActivity,mMainHandler,contacts);
            }
        }
        else  if(intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_NOTICE) || intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_NEWS))
        {
            Intent intent1 = new Intent(mMainActivity, WebMessageActivity.class);
//            if (conversation.sourcePath.length() == 0)
            intent1.putExtra("url", intent.getStringExtra("detialid"));
//            else
//            intent.putExtra("url", conversation.sourcePath);
            intent.putExtra("detialid",intent.getStringExtra("detialid"));
            intent.putExtra("type",intent.getStringExtra("type"));
            intent.putExtra("title","");
            intent1.putExtra("showshare", true);
            mMainActivity.startActivity(intent1);
        }
        else  if(intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_MEETING) )
        {
            Meeting meeting = new Meeting();
            meeting.recordid = intent.getStringExtra("detialid");
            Intent intent1 = new Intent(mMainActivity, MeetingDetialActivity.class);
            intent1.putExtra("meeting",meeting);
            mMainActivity.startActivity(intent1);
        }
        else  if(intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_RESOURCE) )
        {
            SourceData sourceData1 = new SourceData();
            sourceData1.id = intent.getStringExtra("detialid");
            Intent intent1 = new Intent(mMainActivity, SourceDetialActivity.class);
            intent1.putExtra("source",sourceData1);
            mMainActivity.startActivity(intent1);
        }
        else  if(intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_CONTACT) )
        {
            Contacts contacts = new Contacts();
            contacts.mRecordid = intent.getStringExtra("detialid");
            Intent intent1 = new Intent(mMainActivity, ContactDetialActivity.class);
            intent1.putExtra("contacts",contacts);
            mMainActivity.startActivity(intent1);
        }
        else  if(intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_COMPANY) )
        {
            Company company = new Company();
            company.id = intent.getStringExtra("detialid");
            Intent intent1 = new Intent(mMainActivity, CompanyDetialActivity.class);
            intent1.putExtra("company",company);
            mMainActivity.startActivity(intent1);
        }

    }

    public void satartChat(Contacts contacts)
    {
        Intent intent = new Intent(mMainActivity, ChatActivity.class);
        intent.putExtra("isow",true);
        intent.putExtra("contacts",contacts);
        mMainActivity.startActivity(intent);
    }

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.TAKE_PHOTO_HEAD:
            case MainActivity.TAKE_PHOTO_BG:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(BigwinerApplication.mApp.mFileUtils.takePhotoPath);
                    String name = file.getName().substring(0,file.getName().lastIndexOf("."));
                    if (requestCode == MainActivity.TAKE_PHOTO_HEAD) {
                        // 开启裁剪,设置requestCode为CROP_PHOTO
                        BigwinerApplication.mApp.mFileUtils.cropPhoto(mMainActivity,1,1, BigwinerApplication.mApp.mFileUtils.takePhotoPath, BigwinerApplication.mApp.mFileUtils.getOutputHeadMediaFileUri(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("head/photo"),name).getPath()
                                ,(int) (60 * mMainActivity.mBasePresenter.mScreenDefine.density), (int) (60 * mMainActivity.mBasePresenter.mScreenDefine.density),MainActivity.CROP_HEAD);
                    } else {
                        // 开启裁剪,设置requestCode为CROP_PHOTO
                        BigwinerApplication.mApp.mFileUtils.cropPhoto(mMainActivity,9,5, BigwinerApplication.mApp.mFileUtils.takePhotoPath, BigwinerApplication.mApp.mFileUtils.getOutputBgMediaFileUri(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("bg/photo"),name).getPath()
                                ,mMainActivity.mBasePresenter.mScreenDefine.ScreenWidth, (int) (200*mMainActivity.mBasePresenter.mScreenDefine.density),MainActivity.CROP_BG);
                    }
                }
                break;
            case MainActivity.CHOSE_PICTURE_HEAD:
            case MainActivity.CHOSE_PICTURE_BG:
                if (resultCode == Activity.RESULT_OK) {
                    String path = AppUtils.getFileAbsolutePath(mMainActivity, data.getData());
                    File file = new File(path);
                    String name = file.getName().substring(0,file.getName().lastIndexOf("."));
                    if (file.exists()) {
                        // 设置可缩放
                        if (requestCode == MainActivity.CHOSE_PICTURE_HEAD) {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            BigwinerApplication.mApp.mFileUtils.cropPhoto(mMainActivity,1,1,path, BigwinerApplication.mApp.mFileUtils.getOutputHeadMediaFileUri(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("head/photo"),name).getPath()
                                    ,(int) (60 * mMainActivity.mBasePresenter.mScreenDefine.density), (int) (60 * mMainActivity.mBasePresenter.mScreenDefine.density),MainActivity.CROP_HEAD);
                        } else {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            BigwinerApplication.mApp.mFileUtils.cropPhoto(mMainActivity,9,5,path, BigwinerApplication.mApp.mFileUtils.getOutputBgMediaFileUri(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("bg/photo"),name).getPath()
                                    ,mMainActivity.mBasePresenter.mScreenDefine.ScreenWidth, (int) (200*mMainActivity.mBasePresenter.mScreenDefine.density),MainActivity.CROP_BG);

                        }
                    }
                }
                break;
            case MainActivity.CROP_HEAD:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> paths = BigwinerApplication.mApp.mFileUtils.getImage(data);
                    File mFile = new File(paths.get(0));
//                    Glide.with(mMainActivity).load(mFile).into(mMainActivity.headImg);
//                    SharedPreferences sharedPre = mMainActivity.getSharedPreferences(BigwinerApplication.mApp.mAccount.mUserName, 0);
//                    SharedPreferences.Editor e = sharedPre.edit();
//                    e.putString(UserDefine.HEAD_PATH,mFile.getPath());
//                    e.commit();
//                    BigwinerApplication.mApp.mAccount.headpath = mFile.getPath();
                    LoginAsks.setUploadHead(mMainActivity,mMainHandler,mFile);
                }
                break;
            case MainActivity.CROP_BG:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> paths = BigwinerApplication.mApp.mFileUtils.getImage(data);
                    File mFile = new File(paths.get(0));
//                    Glide.with(mMainActivity).load(mFile).into(mMainActivity.bgImg);
//                    SharedPreferences sharedPre = mMainActivity.getSharedPreferences(BigwinerApplication.mApp.mAccount.mUserName, 0);
//                    SharedPreferences.Editor e = sharedPre.edit();
//                    e.putString(UserDefine.BG_PATH,mFile.getPath());
//                    e.commit();
//                    BigwinerApplication.mApp.mAccount.bgpath = mFile.getPath();
                    mMainActivity.waitDialog.show();
                    LoginAsks.setUploadBg(mMainActivity,mMainHandler,mFile);
                }
                break;
//            case WebMessageActivity.SCANNIN_GREQUEST_CODE:
//                if (resultCode == Activity.RESULT_OK) {
//                    Bundle bundle = data.getExtras();
//                    mMainActivity.mSailFragment.showCode(bundle.getString("result"));
//
//                }
//                break;
//            case WebMessageActivity.SCANNIN_GREQUEST_CODE_WITH_JSON:
//                if (resultCode == Activity.RESULT_OK) {
//                    Bundle bundle = data.getExtras();
//                    mMainActivity.mSailFragment.showCode2(bundle.getString("result"));
//
//                }
//                else
//                {
//                    mMainActivity.mSailFragment.showCode2("");
//                }
//                break;
            case SettingActivity.TAKE_PHOTO_CONFRIM:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(BigwinerApplication.mApp.mFileUtils.takePhotoPath);
                    LoginAsks.setUploadConfirm(mMainActivity,mMainActivity.mMainPresenter.mMainHandler,file);
                }
                break;
            case SettingActivity.CHOSE_PHOTO_CONFRIM:
                if (resultCode == Activity.RESULT_OK) {
                    String path = AppUtils.getFileAbsolutePath(mMainActivity, data.getData());
                    File file = new File(path);
                    LoginAsks.setUploadConfirm(mMainActivity,mMainActivity.mMainPresenter.mMainHandler,file);
                }
                break;
        }
    }

    public void updataContactState(Intent intent) {
        Contacts contacts = intent.getParcelableExtra("contacts");
        if(contacts.isadd)
        {
            mMainActivity.mMainPresenter.addFriend(contacts);
        }
        else
        {
            Contacts contacts1 =  BigwinerApplication.mApp.contactManager.friendHashMap.get(contacts.mRecordid);
            BigwinerApplication.mApp.contactManager.removeFriend(contacts1);
            BigWinerDBHelper.getInstance(mMainActivity).removeContacts(contacts1);
            Intent intent1 = new Intent(ContactsAsks.ACTION_UPDATA_CONTACTS);
            intent1.setPackage(BigwinerApplication.mApp.getPackageName());
            mMainActivity.sendBroadcast(intent1);
        }
        if(BigwinerApplication.mApp.contactManager.contactsHashMap.containsKey(contacts.mRecordid))
        {
            BigwinerApplication.mApp.contactManager.contactsHashMap.get(contacts.mRecordid).isadd = contacts.isadd;
        }
        mMainActivity.contactsAdapter.notifyDataSetChanged();

    }

    public void addFriend(Contacts contacts) {
        ContactsAsks.getContactDetial2(mMainActivity,mMainHandler,contacts);
    }

    public void takePhoto() {
        BigwinerApplication.mApp.mFileUtils.checkPermissionTakePhoto(mMainActivity, BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("businesscard"), SettingActivity.TAKE_PHOTO_CONFRIM);
        mMainActivity.popupWindow.dismiss();
    }

    public void pickPhoto() {
        BigwinerApplication.mApp.mFileUtils.selectPhotos(mMainActivity,SettingActivity.CHOSE_PHOTO_CONFRIM);
        mMainActivity.popupWindow.dismiss();
    }

    public void addFriend(String url) {
        mMainActivity.waitDialog.show();
        ContactsAsks.scanCode(mMainActivity,mMainHandler,url);
    }

    public void updataMessageName(Conversation conversation)
    {
        if(!mMainActivity.detialids.containsKey(conversation.detialId))
        {
            mMainActivity.detialids.put(conversation.detialId,conversation.detialId);
            Contacts contacts = new Contacts();
            contacts.mRecordid = conversation.detialId;
            ContactsAsks.getUpdataConversationName(mMainActivity,mMainHandler,contacts);
        }

    }

    public void removeMessage(Intent intent)
    {
        Conversation conversation = intent.getParcelableExtra("msg");
        Conversation conversation1 = BigwinerApplication.mApp.conversationManager.messageHConversation.get(conversation.detialId);
        BigwinerApplication.mApp.conversationManager.collectionConversation.get(Conversation.CONVERSATION_TYPE_MESSAGE).remove(conversation1);
        BigwinerApplication.mApp.conversationManager.messageHConversation.remove(conversation.detialId);
        BigwinerApplication.mApp.conversationManager.messageConversation.remove(conversation.detialId);
        if(BigwinerApplication.mApp.conversationManager.mConversations.size() == 1)
        {
            if(BigwinerApplication.mApp.conversationManager.mConversations.get(0).detialId.equals(conversation.detialId))
            {
                BigwinerApplication.mApp.conversationManager.mConversations.remove(0);
            }
        }
        else if(BigwinerApplication.mApp.conversationManager.mConversations.size() == 2)
        {
            if(BigwinerApplication.mApp.conversationManager.mConversations.get(0).detialId.equals(conversation.detialId))
            {
                BigwinerApplication.mApp.conversationManager.mConversations.remove(0);
            }
            if(BigwinerApplication.mApp.conversationManager.mConversations.get(1).detialId.equals(conversation.detialId))
            {
                BigwinerApplication.mApp.conversationManager.mConversations.remove(1);
            }
        }
        mMainActivity.allAdapter.notifyDataSetChanged();
        mMainActivity.meessageAdapter.notifyDataSetChanged();
        BigWinerDBHelper.getInstance(mMainActivity).setConversationMessaegDelete(conversation.detialId);
        Intent intent1 = new Intent(MainActivity.ACTION_UPDATA_MESSAGE);
        intent1.setPackage(BigwinerApplication.mApp.getPackageName());
        mMainActivity.sendBroadcast(intent1);
    }

    public void updataSourceList(Intent intent) {
        SourceData sourceData = intent.getParcelableExtra("source");
        mMainActivity.waitDialog.show();
        mMainActivity.sourceAllData.clear();
        mMainActivity.allSourceAdapter.notifyDataSetChanged();
        if(mMainActivity.sorceAllPage.currentszie > mMainActivity.sorceAllPage.pagesize)
        SourceAsks.getSourceListAll(mMainActivity,mMainHandler,"0",mMainActivity.sorceAllPage.currentszie,1);
        else
            SourceAsks.getSourceListAll(mMainActivity,mMainHandler,"0",mMainActivity.sorceAllPage.pagesize,1);
        if(sourceData.sourcetype.equals(mMainActivity.getString(R.string.source_type_want)))
        {
            mMainActivity.sourceWantData.clear();
            mMainActivity.wantSourceAdapter.notifyDataSetChanged();
            if(mMainActivity.sorceWantPage.currentszie > mMainActivity.sorceWantPage.pagesize)
            {
                SourceAsks.getSourceListAll(mMainActivity,mMainHandler,"1",mMainActivity.sorceWantPage.currentszie,1);
            }
            else {
                SourceAsks.getSourceListAll(mMainActivity,mMainHandler,"1",mMainActivity.sorceWantPage.pagesize,1);
            }

        }
        else
        {
            mMainActivity.sourceGaveData.clear();
            mMainActivity.gaveSourceAdapter.notifyDataSetChanged();
            if(mMainActivity.sorceGavePage.currentszie > mMainActivity.sorceGavePage.pagesize)
            {
                SourceAsks.getSourceListAll(mMainActivity,mMainHandler,"2",mMainActivity.sorceGavePage.currentszie,1);
            }
            else {
                SourceAsks.getSourceListAll(mMainActivity,mMainHandler,"2",mMainActivity.sorceGavePage.pagesize,1);
            }

        }
        if(mMainActivity.sourceDataHashMap.containsKey(sourceData.id))
        {
            SourceData sourceData1 = mMainActivity.sourceDataHashMap.get(sourceData.id);
            if(intent.getAction().equals(SourceAsks.ACIONT_SOURCE_EDIT))
            {
                sourceData1.updata(sourceData);
                mMainActivity.searchSourceAdapter.notifyDataSetChanged();
            }
            else if(intent.getAction().equals(SourceAsks.ACIONT_SOURCE_DELETE))
            {
                mMainActivity.sourceDataHashMap.remove(sourceData1);
                mMainActivity.sourceSearchData.remove(sourceData1);
                mMainActivity.searchSourceAdapter.notifyDataSetChanged();
            }

        }


    }


    public void openShareActivity(Intent intent) {
        if(intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_MESSAGE))
        {
            Contacts contacts = new Contacts();
            contacts.mRecordid = intent.getStringExtra("detialid");
            if(BigwinerApplication.mApp.contactManager.contactsHashMap.containsKey(contacts.mRecordid))
            {

                satartChat(BigwinerApplication.mApp.contactManager.contactsHashMap.get(contacts.mRecordid));
            }
            else if(BigwinerApplication.mApp.contactManager.friendHashMap.containsKey(contacts.mRecordid))
            {
                satartChat(BigwinerApplication.mApp.contactManager.friendHashMap.get(contacts.mRecordid));
            }
            else
            {
                mMainActivity.waitDialog.show();
                ContactsAsks.getContactDetial(mMainActivity,mMainHandler,contacts);
            }
        }
        else  if(intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_NOTICE) || intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_NEWS))
        {
            Intent intent1 = new Intent(mMainActivity, WebMessageActivity.class);
//            if (conversation.sourcePath.length() == 0)
            mMainActivity.waitDialog.show();
            AppUtils.showMessage(mMainActivity,"正在打开请稍后");
            intent1.putExtra("url", DetialAsks.getNoticeNewUrl(intent.getStringExtra("detialid")));
            intent1.putExtra("detialid",intent.getStringExtra("detialid"));
            intent1.putExtra("type",intent.getStringExtra("type"));
            intent1.putExtra("title","");
            intent1.putExtra("showshare", true);
            String type = intent.getStringExtra("typemodule");
            ConversationAsks.getOutlink(mMainHandler,intent.getStringExtra("detialid"),type,intent1);
        }
        else  if(intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_MEETING) )
        {
            Meeting meeting = new Meeting();
            meeting.recordid = intent.getStringExtra("detialid");
            Intent intent1 = new Intent(mMainActivity, MeetingDetialActivity.class);
            intent1.putExtra("meeting",meeting);
            mMainActivity.startActivity(intent1);
        }
        else  if(intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_RESOURCE) )
        {
            SourceData sourceData1 = new SourceData();
            sourceData1.id = intent.getStringExtra("detialid");
            Intent intent1 = new Intent(mMainActivity, SourceDetialActivity.class);
            intent1.putExtra("source",sourceData1);
            mMainActivity.startActivity(intent1);
        }
        else  if(intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_CONTACT) )
        {
            Contacts contacts = new Contacts();
            contacts.mRecordid = intent.getStringExtra("detialid");
            Intent intent1 = new Intent(mMainActivity, ContactDetialActivity.class);
            intent1.putExtra("contacts",contacts);
            mMainActivity.startActivity(intent1);
        }
        else  if(intent.getStringExtra("type").equals(Conversation.CONVERSATION_TYPE_COMPANY) )
        {
            Company company = new Company();
            company.id = intent.getStringExtra("detialid");
            Intent intent1 = new Intent(mMainActivity, CompanyDetialActivity.class);
            intent1.putExtra("company",company);
            mMainActivity.startActivity(intent1);
        }
    }

    public boolean praseIntent(Intent intent) {
        if(intent.hasExtra("shareopen"))
        {
            openShareActivity(intent);
            return true;
        }
        else
        {
            return false;
        }
    }


    public void setRead(Intent intent) {
        String id = intent.getStringExtra("id");
        if(BigwinerApplication.mApp.conversationManager.unreads.containsKey(id))
        {
            Conversation conversation = BigwinerApplication.mApp.conversationManager.unreads.get(id);
            conversation.isRead = true;
            BigwinerApplication.mApp.conversationManager.unreads.remove(id);
            if(conversation.mType.equals(Conversation.CONVERSATION_TYPE_NEWS))
            {
                mMainActivity.newsAdapter.notifyDataSetChanged();
                mMainActivity.allAdapter.notifyDataSetChanged();
            }
            else
            {
                mMainActivity.noticeAdapter.notifyDataSetChanged();
                mMainActivity.allAdapter.notifyDataSetChanged();
            }
        }
    }



    public void showDes()
    {
        Intent intent = new Intent(mMainActivity, PicViewActivity.class);
        intent.putExtra("id","saildes.jpg");
        intent.putExtra("title",mMainActivity.getString(R.string.sail_des_title));
        mMainActivity.startActivity(intent);
    }

    public void showMember()
    {
        if(BigwinerApplication.mApp.mAccount.issail)
        {
            Intent intent = new Intent(mMainActivity, SailMemberActivity.class);
            mMainActivity.startActivity(intent);
        }
        else
        {
            AppUtils.showMessage(mMainActivity,mMainActivity.getString(R.string.sail_error_member));
        }
    }

    public void showComplaint()
    {
//        if(BigwinerApplication.mApp.mAccount.issail)
//        {
//            Intent intent = new Intent(mSailActivity, ComplaintActivity.class);
//            mSailActivity.startActivity(intent);
//        }
//        else
//        {
//            AppUtils.showMessage(mSailActivity,mSailActivity.getString(R.string.sail_error_member));
//        }
        Intent intent = new Intent(mMainActivity, ComplaintActivity.class);
        mMainActivity.startActivity(intent);
    }

    public void doApplay() {
        Intent intent = new Intent(mMainActivity, SailApplyActivity.class);
        mMainActivity.startActivity(intent);
    }
}