package com.bigwiner.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.presenter.HistoryPresenter;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.adapter.HistoryAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.TabHeadView;

/**
 * Created by xpx on 2017/8/18.
 */

public class HistoryActivity extends BaseActivity {

    public HistoryPresenter mHistoryPresenter = new HistoryPresenter(this);
    public RecyclerView listView1;
    public RecyclerView listView2;
    public ArrayList<View> mViews = new ArrayList<View>();
    public TabHeadView mTabHeadView;
    public NoScrollViewPager mViewPager;
    public ImageView back;
    public ConversationPageAdapter mLoderPageAdapter;
    public HistoryAdapter mConversationAdapter1;
    public HistoryAdapter mConversationAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mHistoryPresenter.Destroy();
        super.onDestroy();
    }

    public AdapterView.OnItemClickListener startDetialListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mHistoryPresenter.startDetial((Conversation) parent.getAdapter().getItem(position));
        }
    };

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public PullToRefreshView.OnFooterRefreshListener onFooterRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {

        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            mHistoryPresenter.onFoot();
            view.onFooterRefreshComplete();
        }
    };

    public PullToRefreshView.OnHeaderRefreshListener onHeadRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            mHistoryPresenter.onHead();
            view.onHeaderRefreshComplete();
        }
    };

    public HistoryAdapter.OnItemClickListener itemClickListener = new HistoryAdapter.OnItemClickListener()
    {

        @Override
        public void onItemClick(Conversation conversation, int position, View view) {
            mHistoryPresenter.clickItem(conversation);
        }

    };

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
