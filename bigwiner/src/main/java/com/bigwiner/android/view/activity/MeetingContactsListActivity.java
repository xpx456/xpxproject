package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.presenter.MeetingContactsListPresenter;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.adapter.ContactsAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.TabHeadView;

/**
 * Created by xpx on 2017/8/18.
 */

public class MeetingContactsListActivity extends BaseActivity {

    public MeetingContactsListPresenter mMeetingContactsListPresenter = new MeetingContactsListPresenter(this);
    public ListView listView;
    public ImageView back;
    public TextView showList;
    public TabHeadView mTabHeadView;
    public ArrayList<RecyclerView> listViews = new ArrayList<RecyclerView>();
    public ArrayList<View> mViews = new ArrayList<View>();
    public ArrayList<Contacts> candDateContacts = new ArrayList<Contacts>();
    public ArrayList<Contacts> applyContacts = new ArrayList<Contacts>();
    public ArrayList<Contacts> readyContacts = new ArrayList<Contacts>();
    public ModuleDetial dateDetial = new ModuleDetial();
    public Meeting meeting;
    public NoScrollViewPager mViewPager;
    public ConversationPageAdapter mLoderPageAdapter;
    public ContactsAdapter canAdapter;
    public ContactsAdapter needAdapter;
    public ContactsAdapter readyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMeetingContactsListPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMeetingContactsListPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener showChatListListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMeetingContactsListPresenter.startChatList();
        }
    };

    public PullToRefreshView.OnFooterRefreshListener onFooterRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {

        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            mMeetingContactsListPresenter.onFoot();
            view.onFooterRefreshComplete();
        }
    };

    public PullToRefreshView.OnHeaderRefreshListener onHeadRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            mMeetingContactsListPresenter.onHead();
            view.onHeaderRefreshComplete();
        }
    };

    public ContactsAdapter.OnItemClickListener onContactItemClickListener = new ContactsAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(Contacts contacts, int position, View view) {
            mMeetingContactsListPresenter.startContactsDetial(contacts);
        }
    };

    public TabHeadView.OnTabLisener mOnTabLisener = new TabHeadView.OnTabLisener() {
        @Override
        public void TabClick(int tab) {

            mMeetingContactsListPresenter.onTabListener(tab);
        }
    };

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
