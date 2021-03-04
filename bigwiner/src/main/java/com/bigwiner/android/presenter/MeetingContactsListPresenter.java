package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.handler.MeetingContactsListHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ContactDetialActivity;
import com.bigwiner.android.view.activity.MeetingContactsListActivity;
import com.bigwiner.android.view.activity.ConversationListActivity;
import com.bigwiner.android.view.adapter.ContactsAdapter;

import java.io.File;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class MeetingContactsListPresenter implements Presenter {

    public MeetingContactsListActivity mMeetingContactsListActivity;
    public MeetingContactsListHandler mMeetingContactsListHandler;
    public MeetingContactsListPresenter(MeetingContactsListActivity mMeetingContactsListActivity)
    {
        mMeetingContactsListHandler = new MeetingContactsListHandler(mMeetingContactsListActivity);
        this.mMeetingContactsListActivity = mMeetingContactsListActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mMeetingContactsListActivity, Color.argb(0, 255, 255, 255));
        mMeetingContactsListActivity.setContentView(R.layout.activity_meetingcontactslist);
        mMeetingContactsListActivity.mToolBarHelper.hidToolbar(mMeetingContactsListActivity, (RelativeLayout) mMeetingContactsListActivity.findViewById(R.id.buttomaciton));
        mMeetingContactsListActivity.measureStatubar(mMeetingContactsListActivity, (RelativeLayout) mMeetingContactsListActivity.findViewById(R.id.stutebar));
        mMeetingContactsListActivity.meeting = mMeetingContactsListActivity.getIntent().getParcelableExtra("meeting");
        mMeetingContactsListActivity.back = mMeetingContactsListActivity.findViewById(R.id.back);
        mMeetingContactsListActivity.showList = mMeetingContactsListActivity.findViewById(R.id.setting);
        mMeetingContactsListActivity.back.setOnClickListener(mMeetingContactsListActivity.backListener);
        mMeetingContactsListActivity.showList.setOnClickListener(mMeetingContactsListActivity.showChatListListener);
        mMeetingContactsListActivity.canAdapter = new ContactsAdapter(mMeetingContactsListActivity.candDateContacts,mMeetingContactsListActivity,1,mMeetingContactsListHandler,mMeetingContactsListActivity.meeting);
        mMeetingContactsListActivity.needAdapter = new ContactsAdapter(mMeetingContactsListActivity.applyContacts,mMeetingContactsListActivity,2,mMeetingContactsListHandler,mMeetingContactsListActivity.meeting);
        mMeetingContactsListActivity.readyAdapter = new ContactsAdapter(mMeetingContactsListActivity.readyContacts,mMeetingContactsListActivity,3,mMeetingContactsListHandler,mMeetingContactsListActivity.meeting);
        mMeetingContactsListActivity.mTabHeadView = mMeetingContactsListActivity.findViewById(R.id.head);
        String[] names = new String[5];
        names[0] = mMeetingContactsListActivity.getString(R.string.attdence_candate);
        names[1] = mMeetingContactsListActivity.getString(R.string.attdence_need);
        names[2] = mMeetingContactsListActivity.getString(R.string.attdence_ready);
        for(int i =0 ; i < names.length ; i++)
        {

            switch (i)
            {
                case 0:
                    View mView1 = mMeetingContactsListActivity.getLayoutInflater().inflate(R.layout.conversation_pager3, null);
                    RecyclerView listView = mView1.findViewById(R.id.busines_List);
                    listView.setLayoutManager(new LinearLayoutManager(mMeetingContactsListActivity));
                    PullToRefreshView pullToRefreshView = mView1.findViewById(R.id.headview);
                    pullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
                    pullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
                    pullToRefreshView.onFooterRefreshComplete();
                    pullToRefreshView.onHeaderRefreshComplete();
                    pullToRefreshView.setOnFooterRefreshListener(mMeetingContactsListActivity.onFooterRefreshListener);
                    pullToRefreshView.setOnHeaderRefreshListener(mMeetingContactsListActivity.onHeadRefreshListener);
                    listView.setAdapter(mMeetingContactsListActivity.canAdapter);
                    mMeetingContactsListActivity.listViews.add(listView);
                    mMeetingContactsListActivity.mViews.add(mView1);
                    mMeetingContactsListActivity.canAdapter.setOnItemClickListener(mMeetingContactsListActivity.onContactItemClickListener);
                    break;
                case 1:
                    View mView2 = mMeetingContactsListActivity.getLayoutInflater().inflate(R.layout.conversation_pager3, null);
                    RecyclerView listView2 = mView2.findViewById(R.id.busines_List);
                    PullToRefreshView pullToRefreshView1 = mView2.findViewById(R.id.headview);
                    pullToRefreshView1.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
                    pullToRefreshView1.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
                    pullToRefreshView1.onFooterRefreshComplete();
                    pullToRefreshView1.onHeaderRefreshComplete();
                    pullToRefreshView1.setOnFooterRefreshListener(mMeetingContactsListActivity.onFooterRefreshListener);
                    pullToRefreshView1.setOnHeaderRefreshListener(mMeetingContactsListActivity.onHeadRefreshListener);
                    listView2.setLayoutManager(new LinearLayoutManager(mMeetingContactsListActivity));
                    listView2.setAdapter(mMeetingContactsListActivity.needAdapter);
                    mMeetingContactsListActivity.listViews.add(listView2);
                    mMeetingContactsListActivity.mViews.add(mView2);
                    mMeetingContactsListActivity.needAdapter.setOnItemClickListener(mMeetingContactsListActivity.onContactItemClickListener);
                    break;
                case 2:
                    View mView3 = mMeetingContactsListActivity.getLayoutInflater().inflate(R.layout.conversation_pager3, null);
                    RecyclerView listView3 = mView3.findViewById(R.id.busines_List);
                    PullToRefreshView pullToRefreshView2 = mView3.findViewById(R.id.headview);
                    pullToRefreshView2.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
                    pullToRefreshView2.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
                    pullToRefreshView2.onFooterRefreshComplete();
                    pullToRefreshView2.onHeaderRefreshComplete();
                    pullToRefreshView2.setOnFooterRefreshListener(mMeetingContactsListActivity.onFooterRefreshListener);
                    pullToRefreshView2.setOnHeaderRefreshListener(mMeetingContactsListActivity.onHeadRefreshListener);
                    listView3.setLayoutManager(new LinearLayoutManager(mMeetingContactsListActivity));
                    listView3.setAdapter(mMeetingContactsListActivity.readyAdapter);
                    mMeetingContactsListActivity.listViews.add(listView3);
                    mMeetingContactsListActivity.mViews.add(mView3);
                    mMeetingContactsListActivity.readyAdapter.setOnItemClickListener(mMeetingContactsListActivity.onContactItemClickListener);
                    break;
            }

        }
        mMeetingContactsListActivity.mViewPager = (NoScrollViewPager) mMeetingContactsListActivity.findViewById(intersky.conversation.R.id.load_pager);
        mMeetingContactsListActivity.mViewPager.setNoScroll(false);
        mMeetingContactsListActivity.mLoderPageAdapter = new ConversationPageAdapter(mMeetingContactsListActivity.mViews,names);
        mMeetingContactsListActivity.mViewPager.setAdapter(mMeetingContactsListActivity.mLoderPageAdapter);
        mMeetingContactsListActivity.mTabHeadView.setViewPager(mMeetingContactsListActivity.mViewPager);
        mMeetingContactsListActivity.mTabHeadView.setmOnTabLisener(mMeetingContactsListActivity.mOnTabLisener);
        mMeetingContactsListActivity.mViewPager.setCurrentItem(0);
        if(mMeetingContactsListActivity.candDateContacts.size() == 0)
        {
            ContactsAsks.getMeetingDateContacts(mMeetingContactsListActivity,mMeetingContactsListHandler
                    ,mMeetingContactsListActivity.meeting,mMeetingContactsListActivity.dateDetial.pagesize,mMeetingContactsListActivity.dateDetial.currentpage);
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

    public void startChatList()
    {
        Intent intent = new Intent(mMeetingContactsListActivity, ConversationListActivity.class);
        mMeetingContactsListActivity.startActivity(intent);
    }

    public void startContactsDetial(Contacts contacts) {
        Intent intent = new Intent(mMeetingContactsListActivity, ContactDetialActivity.class);
        intent.putExtra("contacts", contacts);
        mMeetingContactsListActivity.startActivity(intent);
    }



    public void onTabListener(int tab)
    {
        if(tab == 0)
        {
            if(mMeetingContactsListActivity.candDateContacts.size() == 0)
            {
                ContactsAsks.getMeetingDateContacts(mMeetingContactsListActivity,mMeetingContactsListHandler
                        ,mMeetingContactsListActivity.meeting,mMeetingContactsListActivity.dateDetial.pagesize,mMeetingContactsListActivity.dateDetial.currentpage);
            }
        }
        else if(tab == 1)
        {
            if(mMeetingContactsListActivity.applyContacts.size() == 0)
            {
                ContactsAsks.getMeetingMeaseContacts(mMeetingContactsListActivity,mMeetingContactsListHandler
                        ,mMeetingContactsListActivity.meeting,1,mMeetingContactsListActivity.applyDetial1.pagesize,mMeetingContactsListActivity.applyDetial1.currentpage);
            }
        }
        else
        {
            if(mMeetingContactsListActivity.readyContacts.size() == 0)
            {
                ContactsAsks.getMeetingMeaseContacts(mMeetingContactsListActivity,mMeetingContactsListHandler
                        ,mMeetingContactsListActivity.meeting,2,mMeetingContactsListActivity.applyDetial2.pagesize,mMeetingContactsListActivity.applyDetial2.currentpage);
            }
        }
    }

    public void onFoot()
    {
        if(mMeetingContactsListActivity.mViewPager.getCurrentItem() == 0)
        {
            if(mMeetingContactsListActivity.dateDetial.currentpage < mMeetingContactsListActivity.dateDetial.totlepage)
            {
                mMeetingContactsListActivity.waitDialog.show();
                ContactsAsks.getMeetingDateContacts(mMeetingContactsListActivity,mMeetingContactsListHandler
                        ,mMeetingContactsListActivity.meeting,mMeetingContactsListActivity.dateDetial.pagesize,mMeetingContactsListActivity.dateDetial.currentpage+1);
            }
            else
            {
                AppUtils.showMessage(mMeetingContactsListActivity, mMeetingContactsListActivity.getString(R.string.system_addall));
            }
        }
        else if(mMeetingContactsListActivity.mViewPager.getCurrentItem() == 1)
        {
            if(mMeetingContactsListActivity.applyDetial1.currentpage < mMeetingContactsListActivity.applyDetial1.totlepage)
            {
                mMeetingContactsListActivity.waitDialog.show();
                ContactsAsks.getMeetingMeaseContacts(mMeetingContactsListActivity,mMeetingContactsListHandler
                        ,mMeetingContactsListActivity.meeting,1,mMeetingContactsListActivity.applyDetial1.pagesize,mMeetingContactsListActivity.applyDetial1.currentpage+1);
            }
            else
            {
                AppUtils.showMessage(mMeetingContactsListActivity, mMeetingContactsListActivity.getString(R.string.system_addall));
            }
        }
        else if(mMeetingContactsListActivity.mViewPager.getCurrentItem() == 2)
        {
            if(mMeetingContactsListActivity.applyDetial1.currentpage < mMeetingContactsListActivity.applyDetial1.totlepage)
            {
                mMeetingContactsListActivity.waitDialog.show();
                ContactsAsks.getMeetingMeaseContacts(mMeetingContactsListActivity,mMeetingContactsListHandler
                        ,mMeetingContactsListActivity.meeting,2,mMeetingContactsListActivity.applyDetial2.pagesize,mMeetingContactsListActivity.applyDetial2.currentpage+1);
            }
            else
            {
                AppUtils.showMessage(mMeetingContactsListActivity, mMeetingContactsListActivity.getString(R.string.system_addall));
            }
        }


    }

    public void onHead()
    {
        if(mMeetingContactsListActivity.mViewPager.getCurrentItem() == 0)
        {
            mMeetingContactsListActivity.waitDialog.show();
            mMeetingContactsListActivity.dateDetial.reset();
            mMeetingContactsListActivity.candDateContacts.clear();
            ContactsAsks.getMeetingDateContacts(mMeetingContactsListActivity,mMeetingContactsListHandler
                    ,mMeetingContactsListActivity.meeting,mMeetingContactsListActivity.dateDetial.pagesize,mMeetingContactsListActivity.dateDetial.currentpage);
        }
        else if(mMeetingContactsListActivity.mViewPager.getCurrentItem() == 1)
        {
            mMeetingContactsListActivity.waitDialog.show();
            mMeetingContactsListActivity.applyDetial1.reset();
            mMeetingContactsListActivity.applyContacts.clear();
            ContactsAsks.getMeetingMeaseContacts(mMeetingContactsListActivity,mMeetingContactsListHandler
                    ,mMeetingContactsListActivity.meeting,1,mMeetingContactsListActivity.applyDetial1.pagesize,mMeetingContactsListActivity.applyDetial1.currentpage);
        }
        else if(mMeetingContactsListActivity.mViewPager.getCurrentItem() == 2)
        {
            mMeetingContactsListActivity.waitDialog.show();
            mMeetingContactsListActivity.applyDetial2.reset();
            mMeetingContactsListActivity.readyContacts.clear();
            ContactsAsks.getMeetingMeaseContacts(mMeetingContactsListActivity,mMeetingContactsListHandler
                    ,mMeetingContactsListActivity.meeting,2,mMeetingContactsListActivity.applyDetial2.pagesize,mMeetingContactsListActivity.applyDetial2.currentpage);
        }
    }
}
