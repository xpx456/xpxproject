package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.handler.SourceCollectListHandler;
import com.bigwiner.android.receiver.SourceCollectListReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ContactDetialActivity;
import com.bigwiner.android.view.activity.SourceCollectListActivity;
import com.bigwiner.android.view.activity.ConversationListActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.SourceCreatActivity;
import com.bigwiner.android.view.activity.SourceDetialActivity;
import com.bigwiner.android.view.adapter.ContactsAdapter;
import com.bigwiner.android.view.adapter.SourceListAdapter;

import java.io.File;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.handler.SendMessageHandler;
import intersky.mywidget.PullToRefreshView;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SourceCollectListPresenter implements Presenter {

    public SourceCollectListActivity mSourceCollectListActivity;
    public SourceCollectListHandler mSourceCollectListHandler;
    public SourceCollectListPresenter(SourceCollectListActivity mSourceCollectListActivity)
    {
        mSourceCollectListHandler = new SourceCollectListHandler(mSourceCollectListActivity);
        this.mSourceCollectListActivity = mSourceCollectListActivity;
        mSourceCollectListActivity.setBaseReceiver(new SourceCollectListReceiver(mSourceCollectListHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mSourceCollectListActivity, Color.argb(0, 255, 255, 255));
        mSourceCollectListActivity.setContentView(R.layout.activity_sourcecollect_list);
        mSourceCollectListActivity.mToolBarHelper.hidToolbar(mSourceCollectListActivity, (RelativeLayout) mSourceCollectListActivity.findViewById(R.id.buttomaciton));
        mSourceCollectListActivity.measureStatubar(mSourceCollectListActivity, (RelativeLayout) mSourceCollectListActivity.findViewById(R.id.stutebar));
        PullToRefreshView pullToRefreshView = mSourceCollectListActivity.findViewById(R.id.headview);
        pullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.onFooterRefreshComplete();
        pullToRefreshView.onHeaderRefreshComplete();
        pullToRefreshView.getmHeaderView().setVisibility(View.INVISIBLE);
        pullToRefreshView.getmFooterView().setVisibility(View.INVISIBLE);
        pullToRefreshView.setOnFooterRefreshListener(mSourceCollectListActivity.onFooterRefreshListener);
        pullToRefreshView.setOnHeaderRefreshListener(mSourceCollectListActivity.onHeadRefreshListener);
        mSourceCollectListActivity.collectAdapter = new SourceListAdapter(mSourceCollectListActivity.collectDatas
                ,mSourceCollectListActivity,mSourceCollectListHandler,mSourceCollectListActivity.waitDialog);
        mSourceCollectListActivity.mToolBarHelper.hidToolbar(mSourceCollectListActivity, (RelativeLayout) mSourceCollectListActivity.findViewById(R.id.buttomaciton));
        mSourceCollectListActivity.measureStatubar(mSourceCollectListActivity, (RelativeLayout) mSourceCollectListActivity.findViewById(R.id.stutebar));
        mSourceCollectListActivity.listView = mSourceCollectListActivity.findViewById(R.id.contacts);
        mSourceCollectListActivity.listView.setLayoutManager(new LinearLayoutManager(mSourceCollectListActivity));
        mSourceCollectListActivity.back = mSourceCollectListActivity.findViewById(R.id.back);
        mSourceCollectListActivity.back.setOnClickListener(mSourceCollectListActivity.backListener);
        mSourceCollectListActivity.listView.setAdapter(mSourceCollectListActivity.collectAdapter);
        mSourceCollectListActivity.collectAdapter.setOnItemClickListener(mSourceCollectListActivity.onContactItemClickListener);
        mSourceCollectListActivity.waitDialog.hide();
        SourceAsks.getSourceListCollect(mSourceCollectListActivity,mSourceCollectListHandler,mSourceCollectListActivity.collectdetial.pagesize,mSourceCollectListActivity.collectdetial.currentpage);
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


    public void starSourceDetial(SourceData sourceData) {

        Intent intent = new Intent(mSourceCollectListActivity, SourceDetialActivity.class);
        intent.putExtra("source", sourceData);
        mSourceCollectListActivity.startActivity(intent);

    }

    public void updataSourceList(Intent intent) {
        mSourceCollectListActivity.collectDatas.clear();
        mSourceCollectListActivity.collectAdapter.notifyDataSetChanged();
        mSourceCollectListActivity.waitDialog.show();
        if(mSourceCollectListActivity.collectdetial.currentszie > mSourceCollectListActivity.collectdetial.pagesize)
            SourceAsks.getSourceListCollect(mSourceCollectListActivity,mSourceCollectListHandler,mSourceCollectListActivity.collectdetial.currentszie
                    ,1);
        else
            SourceAsks.getSourceListCollect(mSourceCollectListActivity,mSourceCollectListHandler,mSourceCollectListActivity.collectdetial.pagesize
                    ,1);

    }
    
}
