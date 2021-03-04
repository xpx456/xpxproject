package com.bigwiner.android.presenter;


import android.content.Intent;
import android.graphics.Color;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigwiner.R;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.handler.SearchResultHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ChatActivity;
import com.bigwiner.android.view.activity.ConversationListActivity;
import com.bigwiner.android.view.activity.MeetingDetialActivity;
import com.bigwiner.android.view.activity.SearchResultActivity;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.bigwiner.android.view.adapter.ConversationAdapter;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import xpx.com.toolbar.utils.ToolBarHelper;

public class SearchResultPresenter implements Presenter {

    public SearchResultActivity mSearchResultActivity;
    public SearchResultHandler mSearchResultHandler;

    public SearchResultPresenter(SearchResultActivity mSearchResultActivity) {
        this.mSearchResultActivity = mSearchResultActivity;
        this.mSearchResultHandler = new SearchResultHandler(mSearchResultActivity);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        ToolBarHelper.setSutColor(mSearchResultActivity, Color.argb(0, 255, 255, 255));
        mSearchResultActivity.setContentView(R.layout.activity_search_result);
        mSearchResultActivity.mToolBarHelper.hidToolbar(mSearchResultActivity, (RelativeLayout) mSearchResultActivity.findViewById(R.id.buttomaciton));
        mSearchResultActivity.measureStatubar(mSearchResultActivity, (RelativeLayout) mSearchResultActivity.findViewById(R.id.stutebar));
        mSearchResultActivity.btnBack = mSearchResultActivity.findViewById(R.id.backlayer);
        mSearchResultActivity.searchView = mSearchResultActivity.findViewById(R.id.nomal);
        mSearchResultActivity.pullToRefreshView = mSearchResultActivity.findViewById(R.id.headview);
        mSearchResultActivity.listView = mSearchResultActivity.findViewById(R.id.busines_List);
        mSearchResultActivity.listView.setLayoutManager(new LinearLayoutManager(mSearchResultActivity));
        mSearchResultActivity.pullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        mSearchResultActivity.pullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        mSearchResultActivity.pullToRefreshView.onFooterRefreshComplete();
        mSearchResultActivity.pullToRefreshView.onHeaderRefreshComplete();
        mSearchResultActivity.pullToRefreshView.setOnFooterRefreshListener(mSearchResultActivity.onFooterRefreshListener);
        mSearchResultActivity.pullToRefreshView.setOnHeaderRefreshListener(mSearchResultActivity.onHeadRefreshListener);
        mSearchResultActivity.btnBack.setOnClickListener(mSearchResultActivity.backListener);
        mSearchResultActivity.searchView.setOnClickListener(mSearchResultActivity.showSearchListener);
        mSearchResultActivity.conversationAdapter = new ConversationAdapter(mSearchResultActivity.conversations, mSearchResultActivity, mSearchResultHandler, mSearchResultActivity.getIntent().getStringExtra("keyword"));
        mSearchResultActivity.listView.setAdapter(mSearchResultActivity.conversationAdapter);
        mSearchResultActivity.conversationAdapter.setOnItemClickListener(mSearchResultActivity.itemClickListener);
        upDataSearch();
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
        mSearchResultHandler = null;
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    public void doSearch() {
        mSearchResultActivity.finish();
    }

    public void onItemClick(Conversation conversation) {
        if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_MEETING)) {
            Meeting meeting = new Meeting();
            meeting.recordid = conversation.detialId;
            Intent intent = new Intent(mSearchResultActivity, MeetingDetialActivity.class);
            intent.putExtra("meeting", meeting);
            mSearchResultActivity.startActivity(intent);
        }
        else if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE)) {
            Contacts contacts = BigwinerApplication.mApp.contactManager.contactsHashMap.get(conversation.detialId);
            if (contacts == null) {
                contacts = new Contacts();
                contacts.mRecordid = conversation.detialId;
                contacts.mRName = conversation.mTitle;
                contacts.mName = conversation.mTitle;
            }
            Intent intent = new Intent(mSearchResultActivity, ChatActivity.class);
            intent.putExtra("isow", true);
            intent.putExtra("contacts", contacts);
            mSearchResultActivity.startActivity(intent);
        }
        else if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_NOTICE)) {
            Intent intent = new Intent(mSearchResultActivity, WebMessageActivity.class);
            if (conversation.sourcePath.length() == 0)
            {
                intent.putExtra("url", DetialAsks.getNoticeNewUrl(conversation.detialId));
                intent.putExtra("type",conversation.mType);
                intent.putExtra("title",conversation.mTitle);
                intent.putExtra("detialid",conversation.detialId);
                intent.putExtra("des",conversation.mSubject);
                intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
            }

            else
            {
                ConversationAsks.getUpdata(conversation.detialId);
                intent.putExtra("url", conversation.sourcePath);
                intent.putExtra("type",conversation.mType);
                intent.putExtra("title",conversation.mTitle);
                intent.putExtra("des",conversation.mSubject);
                intent.putExtra("detialid",conversation.detialId);
                intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
            }
            intent.putExtra("showshare", true);
            mSearchResultActivity.startActivity(intent);
        }
        else if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_NEWS)) {
            Intent intent = new Intent(mSearchResultActivity, WebMessageActivity.class);
            if (conversation.sourcePath.length() == 0)
            {
                intent.putExtra("url", DetialAsks.getNoticeNewUrl(conversation.detialId));
                intent.putExtra("type",conversation.mType);
                intent.putExtra("title",conversation.mTitle);
                intent.putExtra("detialid",conversation.detialId);
                intent.putExtra("des",conversation.mSubject);
                intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
            }
            else
            {
                ConversationAsks.getUpdata(conversation.detialId);
                intent.putExtra("url", conversation.sourcePath);
                intent.putExtra("type",conversation.mType);
                intent.putExtra("title",conversation.mTitle);
                intent.putExtra("detialid",conversation.detialId);
                intent.putExtra("des",conversation.mSubject);
                intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));

            }
            intent.putExtra("showshare", true);
            mSearchResultActivity.startActivity(intent);
        }
        else {
            Intent intent = new Intent(mSearchResultActivity, ConversationListActivity.class);
            if (conversation.mTitle.equals(mSearchResultActivity.getString(R.string.conversation_chat))) {
                intent.putParcelableArrayListExtra("data", mSearchResultActivity.messages);
            } else if (conversation.mTitle.equals(mSearchResultActivity.getString(R.string.conversation_notice))) {
                intent.putParcelableArrayListExtra("data", mSearchResultActivity.notices);
            } else if (conversation.mTitle.equals(mSearchResultActivity.getString(R.string.conversation_news))) {
                intent.putParcelableArrayListExtra("data", mSearchResultActivity.news);
            } else if (conversation.mTitle.equals(mSearchResultActivity.getString(R.string.conversation_meeting))) {
                intent.putParcelableArrayListExtra("data", mSearchResultActivity.meetings);
            }
            intent.putExtra("title", conversation.mTitle);
            intent.putExtra("keyword", mSearchResultActivity.getIntent().getStringExtra("keyword"));
            mSearchResultActivity.startActivity(intent);
        }
    }

    public void upDataSearch() {
        mSearchResultActivity.waitDialog.show();
        mSearchResultActivity.conversations.clear();
        mSearchResultActivity.meetings.clear();
        mSearchResultActivity.notices.clear();
        mSearchResultActivity.messages.clear();
        mSearchResultActivity.news.clear();
        mSearchResultActivity.noticeflg = false;
        mSearchResultActivity.meetingflg = false;
        mSearchResultActivity.newsflg = false;
        mSearchResultActivity.conversationAdapter.notifyDataSetChanged();
        mSearchResultActivity.messages.addAll(BigwinerApplication.mApp.conversationManager.searchMessage(mSearchResultActivity, mSearchResultActivity.getIntent().getStringExtra("keyword")));
        ConversationAsks.doSearch(mSearchResultActivity, mSearchResultHandler, mSearchResultActivity.getIntent().getStringExtra("keyword"), "notice");
        ConversationAsks.doSearch(mSearchResultActivity, mSearchResultHandler, mSearchResultActivity.getIntent().getStringExtra("keyword"), "new");
        ConversationAsks.doSearch(mSearchResultActivity, mSearchResultHandler, mSearchResultActivity.getIntent().getStringExtra("keyword"), "conference");
    }

    public void praseSearchResult() {
        if (mSearchResultActivity.messages.size() > 0) {
            Conversation conversation = new Conversation();
            conversation.mType = Conversation.CONVERSATION_TYPE_TITLE;
            conversation.mTitle = mSearchResultActivity.getString(R.string.conversation_chat);
            conversation.mHit = mSearchResultActivity.messages.size();
            mSearchResultActivity.conversations.add(conversation);
            if (mSearchResultActivity.messages.size() < 2) {
                mSearchResultActivity.conversations.addAll(mSearchResultActivity.messages);
            } else {
                mSearchResultActivity.conversations.add(mSearchResultActivity.messages.get(0));
                mSearchResultActivity.conversations.add(mSearchResultActivity.messages.get(1));
            }
        }
        if (mSearchResultActivity.notices.size() > 0) {
            Conversation conversation = new Conversation();
            conversation.mType = Conversation.CONVERSATION_TYPE_TITLE;
            conversation.mTitle = mSearchResultActivity.getString(R.string.conversation_notice);
            conversation.mHit = mSearchResultActivity.notices.size();
            mSearchResultActivity.conversations.add(conversation);
            if (mSearchResultActivity.notices.size() < 2) {
                mSearchResultActivity.conversations.addAll(mSearchResultActivity.notices);
            } else {
                mSearchResultActivity.conversations.add(mSearchResultActivity.notices.get(0));
                mSearchResultActivity.conversations.add(mSearchResultActivity.notices.get(1));
            }
        }

        if (mSearchResultActivity.news.size() > 0) {
            Conversation conversation = new Conversation();
            conversation.mType = Conversation.CONVERSATION_TYPE_TITLE;
            conversation.mTitle = mSearchResultActivity.getString(R.string.conversation_news);
            conversation.mHit = mSearchResultActivity.news.size();
            mSearchResultActivity.conversations.add(conversation);
            if (mSearchResultActivity.news.size() < 2) {
                mSearchResultActivity.conversations.addAll(mSearchResultActivity.news);
            } else {
                mSearchResultActivity.conversations.add(mSearchResultActivity.news.get(0));
                mSearchResultActivity.conversations.add(mSearchResultActivity.news.get(1));
            }
        }

        if (mSearchResultActivity.meetings.size() > 0) {
            Conversation conversation = new Conversation();
            conversation.mType = Conversation.CONVERSATION_TYPE_TITLE;
            conversation.mTitle = mSearchResultActivity.getString(R.string.conversation_meeting);
            conversation.mHit = mSearchResultActivity.meetings.size();
            mSearchResultActivity.conversations.add(conversation);
            if (mSearchResultActivity.meetings.size() < 2) {
                mSearchResultActivity.conversations.addAll(mSearchResultActivity.meetings);
            } else {
                mSearchResultActivity.conversations.add(mSearchResultActivity.meetings.get(0));
                mSearchResultActivity.conversations.add(mSearchResultActivity.meetings.get(1));
            }
        }
        mSearchResultActivity.conversationAdapter.notifyDataSetChanged();
    }
}
