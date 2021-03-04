package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.handler.SourceAddListHandler;
import com.bigwiner.android.receiver.SourceAddListReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.ContactDetialActivity;
import com.bigwiner.android.view.activity.SourceAddListActivity;
import com.bigwiner.android.view.activity.ConversationListActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.SourceCreatActivity;
import com.bigwiner.android.view.activity.SourceDetialActivity;
import com.bigwiner.android.view.adapter.ContactsAdapter;
import com.bigwiner.android.view.adapter.SourceListAdapter;
import com.bigwiner.android.view.adapter.SourceListEditAdapter;

import java.io.File;
import java.sql.Time;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.handler.SendMessageHandler;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.PullToRefreshView;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SourceAddListPresenter implements Presenter {

    public SourceAddListActivity mSourceAddListActivity;
    public SourceAddListHandler mSourceAddListHandler;

    public SourceAddListPresenter(SourceAddListActivity mSourceAddListActivity) {
        mSourceAddListHandler = new SourceAddListHandler(mSourceAddListActivity);
        this.mSourceAddListActivity = mSourceAddListActivity;
        mSourceAddListActivity.setBaseReceiver(new SourceAddListReceiver(mSourceAddListHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mSourceAddListActivity, Color.argb(0, 255, 255, 255));
        mSourceAddListActivity.wantData.add(new SourceData("0"));
        mSourceAddListActivity.offerData.add(new SourceData("0"));
        mSourceAddListActivity.setContentView(R.layout.activity_sourceadd_list);
        mSourceAddListActivity.mToolBarHelper.hidToolbar(mSourceAddListActivity, (RelativeLayout) mSourceAddListActivity.findViewById(R.id.buttomaciton));
        mSourceAddListActivity.measureStatubar(mSourceAddListActivity, (RelativeLayout) mSourceAddListActivity.findViewById(R.id.stutebar));
        mSourceAddListActivity.tabOffer = mSourceAddListActivity.findViewById(R.id.taboffer);
        mSourceAddListActivity.offerTxt = mSourceAddListActivity.findViewById(R.id.txtoffer);
        mSourceAddListActivity.tabWant = mSourceAddListActivity.findViewById(R.id.tabwant);
        mSourceAddListActivity.wantTxt = mSourceAddListActivity.findViewById(R.id.txtwant);
        mSourceAddListActivity.mViewPager = mSourceAddListActivity.findViewById(R.id.load_pager);
        mSourceAddListActivity.back = mSourceAddListActivity.findViewById(R.id.back);
        mSourceAddListActivity.mWantSourceListAdapter = new SourceListEditAdapter(mSourceAddListActivity.wantData, mSourceAddListActivity, mSourceAddListHandler, mSourceAddListActivity.waitDialog);
        mSourceAddListActivity.mOfferSourceListAdapter = new SourceListEditAdapter(mSourceAddListActivity.offerData, mSourceAddListActivity, mSourceAddListHandler, mSourceAddListActivity.waitDialog);
        View mView1 = mSourceAddListActivity.getLayoutInflater().inflate(intersky.conversation.R.layout.conversation_pager, null);
        mSourceAddListActivity.listWant = mView1.findViewById(R.id.busines_List);
        mSourceAddListActivity.listWant.setLayoutManager(new LinearLayoutManager(mSourceAddListActivity));
        mSourceAddListActivity.listWant.setAdapter(mSourceAddListActivity.mWantSourceListAdapter);
        PullToRefreshView pullToRefreshView = mView1.findViewById(R.id.headview);
        pullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.onFooterRefreshComplete();
        pullToRefreshView.onHeaderRefreshComplete();
        pullToRefreshView.setOnFooterRefreshListener(mSourceAddListActivity.onFooterRefreshListener);
        pullToRefreshView.setOnHeaderRefreshListener(mSourceAddListActivity.onHeadRefreshListener);
        mSourceAddListActivity.mViews.add(mView1);
        View mView2 = mSourceAddListActivity.getLayoutInflater().inflate(intersky.conversation.R.layout.conversation_pager, null);
        mSourceAddListActivity.listOffer = mView2.findViewById(R.id.busines_List);
        mSourceAddListActivity.listOffer.setLayoutManager(new LinearLayoutManager(mSourceAddListActivity));
        mSourceAddListActivity.listOffer.setAdapter(mSourceAddListActivity.mOfferSourceListAdapter);
        pullToRefreshView = mView2.findViewById(R.id.headview);
        pullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.onFooterRefreshComplete();
        pullToRefreshView.onHeaderRefreshComplete();
        pullToRefreshView.setOnFooterRefreshListener(mSourceAddListActivity.onFooterRefreshListener);
        pullToRefreshView.setOnHeaderRefreshListener(mSourceAddListActivity.onHeadRefreshListener);
        mSourceAddListActivity.mWantSourceListAdapter.setOnItemClickListener(mSourceAddListActivity.onSourceItemClickListener);
        mSourceAddListActivity.mOfferSourceListAdapter.setOnItemClickListener(mSourceAddListActivity.onSourceItemClickListener);
        mSourceAddListActivity.mViews.add(mView2);
        mSourceAddListActivity.tabWant.setOnClickListener(mSourceAddListActivity.tabWantListener);
        mSourceAddListActivity.tabOffer.setOnClickListener(mSourceAddListActivity.tabOfferListener);
        mSourceAddListActivity.mLoderPageAdapter = new ConversationPageAdapter(mSourceAddListActivity.mViews, new String[2]);
        mSourceAddListActivity.mViewPager.setAdapter(mSourceAddListActivity.mLoderPageAdapter);
        mSourceAddListActivity.back.setOnClickListener(mSourceAddListActivity.backListener);
        tabWant();
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

    public void tabWant() {
        mSourceAddListActivity.tabWant.setBackgroundResource(R.drawable.shape_source_tab_select_l);
        mSourceAddListActivity.wantTxt.setTextColor(0xffffffff);
        mSourceAddListActivity.tabOffer.setBackgroundResource(R.drawable.shape_source_tab_r);
        mSourceAddListActivity.offerTxt.setTextColor(0xffff9800);
        mSourceAddListActivity.mViewPager.setCurrentItem(0);
        if (mSourceAddListActivity.wantData.size() == 1) {
            mSourceAddListActivity.waitDialog.show();
            SourceAsks.getSourceListMy(mSourceAddListActivity, mSourceAddListHandler
                    , "1", mSourceAddListActivity.sourceWantDetial.pagesize, mSourceAddListActivity.sourceWantDetial.currentpage);
        }

    }

    public void tabOffer() {
        mSourceAddListActivity.tabOffer.setBackgroundResource(R.drawable.shape_source_tab_select_r);
        mSourceAddListActivity.offerTxt.setTextColor(0xffffffff);
        mSourceAddListActivity.tabWant.setBackgroundResource(R.drawable.shape_source_tab_l);
        mSourceAddListActivity.wantTxt.setTextColor(0xffff9800);
        mSourceAddListActivity.mViewPager.setCurrentItem(1);
        if (mSourceAddListActivity.offerData.size() == 1) {
            mSourceAddListActivity.waitDialog.show();
            SourceAsks.getSourceListMy(mSourceAddListActivity, mSourceAddListHandler
                    , "2", mSourceAddListActivity.sourceOfferDetial.pagesize, mSourceAddListActivity.sourceOfferDetial.currentpage);
        }

    }


    public void starSourceDetial(SourceData sourceData) {

        if (sourceData.sourcetype.equals("0")) {
            Intent intent = new Intent(mSourceAddListActivity, SourceCreatActivity.class);
            SourceData sourceData1;
            if (mSourceAddListActivity.mViewPager.getCurrentItem() == 0) {
                sourceData1 = new SourceData(mSourceAddListActivity.getString(R.string.source_type_want));
            } else {
                sourceData1 = new SourceData(mSourceAddListActivity.getString(R.string.source_type_offer));
            }
            sourceData1.start = TimeUtils.getDate() + " 00:00:00";
            sourceData1.end = TimeUtils.getDateTomorrow() + " 00:00:00";
            intent.putExtra("source", sourceData1);
            mSourceAddListActivity.waitDialog.show();
            SourceAsks.getSourceAddCheck(mSourceAddListActivity, mSourceAddListHandler, intent);

//            mSourceAddListActivity.startActivity(intent);
        } else {
            Intent intent = new Intent(mSourceAddListActivity, SourceDetialActivity.class);
            intent.putExtra("source", sourceData);
            mSourceAddListActivity.startActivity(intent);
        }
    }

    public void updataSourceList(SourceData sourceData, String action) {
        if (sourceData.sourcetype.equals(mSourceAddListActivity.getString(R.string.source_type_want))) {
            mSourceAddListActivity.waitDialog.show();
            mSourceAddListActivity.wantData.clear();
            mSourceAddListActivity.wantData.add(new SourceData("0"));
            mSourceAddListActivity.mWantSourceListAdapter.notifyDataSetChanged();
            if (mSourceAddListActivity.sourceWantDetial.currentszie > mSourceAddListActivity.sourceWantDetial.pagesize) {
                SourceAsks.getSourceListMy(mSourceAddListActivity, mSourceAddListHandler, "1",
                        mSourceAddListActivity.sourceWantDetial.currentszie, 1);
            } else {
                SourceAsks.getSourceListMy(mSourceAddListActivity, mSourceAddListHandler, "1",
                        mSourceAddListActivity.sourceWantDetial.pagesize, 1);
            }

        } else {
            mSourceAddListActivity.waitDialog.show();
            mSourceAddListActivity.offerData.clear();
            mSourceAddListActivity.offerData.add(new SourceData("0"));
            mSourceAddListActivity.mOfferSourceListAdapter.notifyDataSetChanged();
            if (mSourceAddListActivity.sourceOfferDetial.currentszie > mSourceAddListActivity.sourceOfferDetial.pagesize) {
                SourceAsks.getSourceListMy(mSourceAddListActivity, mSourceAddListHandler, "2",
                        mSourceAddListActivity.sourceOfferDetial.currentszie, 1);
            } else {
                SourceAsks.getSourceListMy(mSourceAddListActivity, mSourceAddListHandler, "2",
                        mSourceAddListActivity.sourceOfferDetial.pagesize, 1);
            }

        }
        Intent intent = new Intent(action);
        intent.putExtra("source", sourceData);
        intent.setPackage(BigwinerApplication.mApp.getPackageName());
        mSourceAddListActivity.sendBroadcast(intent);
    }

    public void updataSourceList(Intent intent) {
        SourceData sourceData = intent.getParcelableExtra("source");
        if (sourceData.sourcetype.equals(mSourceAddListActivity.getString(R.string.source_type_want)) || sourceData.sourcetype.equals("1")) {
            mSourceAddListActivity.waitDialog.show();
            mSourceAddListActivity.wantData.clear();
            mSourceAddListActivity.wantData.add(new SourceData("0"));
            mSourceAddListActivity.mWantSourceListAdapter.notifyDataSetChanged();
            if (mSourceAddListActivity.sourceWantDetial.currentszie > mSourceAddListActivity.sourceWantDetial.pagesize) {
                SourceAsks.getSourceListMy(mSourceAddListActivity, mSourceAddListHandler, "1",
                        mSourceAddListActivity.sourceWantDetial.currentszie, 1);
            } else {
                SourceAsks.getSourceListMy(mSourceAddListActivity, mSourceAddListHandler, "1",
                        mSourceAddListActivity.sourceWantDetial.pagesize, 1);
            }

        } else {
            mSourceAddListActivity.waitDialog.show();
            mSourceAddListActivity.offerData.clear();
            mSourceAddListActivity.offerData.add(new SourceData("0"));
            mSourceAddListActivity.mOfferSourceListAdapter.notifyDataSetChanged();
            if (mSourceAddListActivity.sourceOfferDetial.currentszie > mSourceAddListActivity.sourceOfferDetial.pagesize) {
                SourceAsks.getSourceListMy(mSourceAddListActivity, mSourceAddListHandler, "2",
                        mSourceAddListActivity.sourceOfferDetial.currentszie, 1);
            } else {
                SourceAsks.getSourceListMy(mSourceAddListActivity, mSourceAddListHandler, "2",
                        mSourceAddListActivity.sourceOfferDetial.pagesize, 1);
            }

        }
    }

}
