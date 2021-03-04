package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigwiner.R;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.handler.HistoryHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ChatActivity;
import com.bigwiner.android.view.activity.HistoryActivity;
import com.bigwiner.android.view.activity.MeetingDetialActivity;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.bigwiner.android.view.adapter.HistoryAdapter;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class HistoryPresenter implements Presenter {

    public HistoryActivity mHistoryActivity;
    public HistoryHandler mHistoryHandler;

    public HistoryPresenter(HistoryActivity mHistoryActivity) {
        this.mHistoryHandler = new HistoryHandler(mHistoryActivity);
        this.mHistoryActivity = mHistoryActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mHistoryActivity, Color.argb(0, 255, 255, 255));
        mHistoryActivity.setContentView(R.layout.activity_history);
        mHistoryActivity.mToolBarHelper.hidToolbar(mHistoryActivity, (RelativeLayout) mHistoryActivity.findViewById(R.id.buttomaciton));
        mHistoryActivity.measureStatubar(mHistoryActivity, (RelativeLayout) mHistoryActivity.findViewById(R.id.stutebar));
        mHistoryActivity.back = mHistoryActivity.findViewById(R.id.back);
        mHistoryActivity.back.setOnClickListener(mHistoryActivity.backListener);
        mHistoryActivity.mTabHeadView = mHistoryActivity.findViewById(intersky.conversation.R.id.head);
        String[] names = new String[5];
        names[0] = mHistoryActivity.getString(R.string.history_scan);
        names[1] = mHistoryActivity.getString(R.string.history_active);
        View mView1 = mHistoryActivity.getLayoutInflater().inflate(intersky.conversation.R.layout.conversation_pager, null);
        mHistoryActivity.listView1 = mView1.findViewById(R.id.busines_List);
        mHistoryActivity.listView1.setLayoutManager(new LinearLayoutManager(mHistoryActivity));
        PullToRefreshView pullToRefreshView = mView1.findViewById(R.id.headview);
        pullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.onFooterRefreshComplete();
        pullToRefreshView.onHeaderRefreshComplete();
        pullToRefreshView.setOnFooterRefreshListener(mHistoryActivity.onFooterRefreshListener);
        pullToRefreshView.setOnHeaderRefreshListener(mHistoryActivity.onHeadRefreshListener);
        mHistoryActivity.mViews.add(mView1);
        View mView2 = mHistoryActivity.getLayoutInflater().inflate(intersky.conversation.R.layout.conversation_pager, null);
        mHistoryActivity.listView2 = mView2.findViewById(R.id.busines_List);
        mHistoryActivity.listView2.setLayoutManager(new LinearLayoutManager(mHistoryActivity));
        PullToRefreshView pullToRefreshView2 = mView2.findViewById(R.id.headview);
        pullToRefreshView2.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView2.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView2.onFooterRefreshComplete();
        pullToRefreshView2.onHeaderRefreshComplete();
        pullToRefreshView2.setOnFooterRefreshListener(mHistoryActivity.onFooterRefreshListener);
        pullToRefreshView2.setOnHeaderRefreshListener(mHistoryActivity.onHeadRefreshListener);
        mHistoryActivity.mViews.add(mView2);
        mHistoryActivity.mViewPager = (NoScrollViewPager) mHistoryActivity.findViewById(intersky.conversation.R.id.load_pager);
        mHistoryActivity.mViewPager.setNoScroll(false);
        mHistoryActivity.mLoderPageAdapter = new ConversationPageAdapter(mHistoryActivity.mViews, names);
        mHistoryActivity.mViewPager.setAdapter(mHistoryActivity.mLoderPageAdapter);
        mHistoryActivity.mConversationAdapter1 = new HistoryAdapter(BigwinerApplication.mApp.conversationManager.historys, mHistoryActivity);
        mHistoryActivity.mConversationAdapter2 = new HistoryAdapter(BigwinerApplication.mApp.conversationManager.activitys, mHistoryActivity);
        mHistoryActivity.listView1.setAdapter(mHistoryActivity.mConversationAdapter1);
        mHistoryActivity.listView2.setAdapter(mHistoryActivity.mConversationAdapter2);
        mHistoryActivity.mConversationAdapter1.setOnItemClickListener(mHistoryActivity.itemClickListener);
        mHistoryActivity.mConversationAdapter2.setOnItemClickListener(mHistoryActivity.itemClickListener);
        mHistoryActivity.mViewPager.setCurrentItem(0);
        mHistoryActivity.mTabHeadView.setViewPager(mHistoryActivity.mViewPager);
        if (BigwinerApplication.mApp.conversationManager.historys.size() == 0)
        {
            ConversationAsks.getNewsAndNoticesHis(mHistoryActivity, mHistoryHandler, 1, BigwinerApplication.mApp.conversationManager.hismodul.pagesize, BigwinerApplication.mApp.conversationManager.hismodul.currentpage);
        }
        else
        {
            if(BigwinerApplication.mApp.hisupdata)
            {
                BigwinerApplication.mApp.hisupdata = false;
                onHead(1);
            }
        }
        if (BigwinerApplication.mApp.conversationManager.activitys.size() == 0)
        {
            ConversationAsks.getNewsAndNoticesHis(mHistoryActivity, mHistoryHandler, 2, BigwinerApplication.mApp.conversationManager.activitysmodul.pagesize, BigwinerApplication.mApp.conversationManager.activitysmodul.currentpage);
        }
        else
        {
            if(BigwinerApplication.mApp.hisupdata)
            {
                BigwinerApplication.mApp.hisupdata = false;
                onHead(2);
            }
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

    public void startDetial(Conversation conversation) {

    }

    public void onFoot() {
        if (mHistoryActivity.mViewPager.getCurrentItem() == 0) {
            if (BigwinerApplication.mApp.conversationManager.hismodul.currentpage < BigwinerApplication.mApp.conversationManager.hismodul.totlepage)
                ConversationAsks.getNewsAndNoticesHis(mHistoryActivity, mHistoryHandler, 1, BigwinerApplication.mApp.conversationManager.hismodul.pagesize, BigwinerApplication.mApp.conversationManager.hismodul.currentpage + 1);

        } else {
            if (BigwinerApplication.mApp.conversationManager.activitysmodul.currentpage < BigwinerApplication.mApp.conversationManager.activitysmodul.totlepage)
                ConversationAsks.getNewsAndNoticesHis(mHistoryActivity, mHistoryHandler, 2, BigwinerApplication.mApp.conversationManager.activitysmodul.pagesize, BigwinerApplication.mApp.conversationManager.activitysmodul.currentpage + 1);

        }
    }

    public void onHead() {
        if (mHistoryActivity.mViewPager.getCurrentItem() == 0) {
            BigwinerApplication.mApp.conversationManager.hismodul.reset();
            BigwinerApplication.mApp.conversationManager.historys.clear();
            mHistoryActivity.mConversationAdapter1.notifyDataSetChanged();
            ConversationAsks.getNewsAndNoticesHis(mHistoryActivity, mHistoryHandler, 1, BigwinerApplication.mApp.conversationManager.hismodul.pagesize, BigwinerApplication.mApp.conversationManager.hismodul.currentpage);

        } else {
            BigwinerApplication.mApp.conversationManager.activitysmodul.reset();
            BigwinerApplication.mApp.conversationManager.activitys.clear();
            mHistoryActivity.mConversationAdapter2.notifyDataSetChanged();
            ConversationAsks.getNewsAndNoticesHis(mHistoryActivity, mHistoryHandler, 2, BigwinerApplication.mApp.conversationManager.activitysmodul.pagesize, BigwinerApplication.mApp.conversationManager.activitysmodul.currentpage);

        }
    }

    public void onHead(int type) {
        if (type == 1) {
            BigwinerApplication.mApp.conversationManager.hismodul.reset();
            BigwinerApplication.mApp.conversationManager.historys.clear();
            mHistoryActivity.mConversationAdapter1.notifyDataSetChanged();
            ConversationAsks.getNewsAndNoticesHis(mHistoryActivity, mHistoryHandler, type, BigwinerApplication.mApp.conversationManager.hismodul.pagesize, BigwinerApplication.mApp.conversationManager.hismodul.currentpage);

        } else {
            BigwinerApplication.mApp.conversationManager.activitysmodul.reset();
            BigwinerApplication.mApp.conversationManager.activitys.clear();
            mHistoryActivity.mConversationAdapter2.notifyDataSetChanged();
            ConversationAsks.getNewsAndNoticesHis(mHistoryActivity, mHistoryHandler, type, BigwinerApplication.mApp.conversationManager.activitysmodul.pagesize, BigwinerApplication.mApp.conversationManager.activitysmodul.currentpage);

        }
    }

    public void clickItem(Conversation conversation) {
        if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_MEETING)) {
            Meeting meeting = new Meeting();
            meeting.recordid = conversation.detialId;
            Intent intent = new Intent(mHistoryActivity, MeetingDetialActivity.class);
            intent.putExtra("meeting", meeting);
            mHistoryActivity.startActivity(intent);
        } else if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_MESSAGE)) {
            Contacts contacts = BigwinerApplication.mApp.contactManager.contactsHashMap.get(conversation.detialId);
            if (contacts == null) {
                contacts = new Contacts();
                contacts.mRecordid = conversation.detialId;
                contacts.mRName = conversation.mTitle;
                contacts.mName = conversation.mTitle;
            }
            Intent intent = new Intent(mHistoryActivity, ChatActivity.class);
            intent.putExtra("isow", true);
            intent.putExtra("contacts", contacts);
            mHistoryActivity.startActivity(intent);
        } else if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_NOTICE)) {
            Intent intent = new Intent(mHistoryActivity, WebMessageActivity.class);
            if (conversation.sourcePath.length() == 0)
            {
                intent.putExtra("type",conversation.mType);
                intent.putExtra("title",conversation.mTitle);
                intent.putExtra("detialid",conversation.detialId);
                intent.putExtra("des",conversation.mSubject);
                intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
                intent.putExtra("url", DetialAsks.getNoticeNewUrl(conversation.detialId));
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
            mHistoryActivity.startActivity(intent);
        } else if (conversation.mType.equals(Conversation.CONVERSATION_TYPE_NEWS)) {
            Intent intent = new Intent(mHistoryActivity, WebMessageActivity.class);
            if (conversation.sourcePath.length() == 0)
            {
                intent.putExtra("type",conversation.mType);
                intent.putExtra("title",conversation.mTitle);
                intent.putExtra("detialid",conversation.detialId);
                intent.putExtra("des",conversation.mSubject);
                intent.putExtra("img",BigwinerApplication.mApp.measureImg(conversation.sourceId));
                intent.putExtra("url", DetialAsks.getNoticeNewUrl(conversation.detialId));
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
            mHistoryActivity.startActivity(intent);
        }
    }


}
