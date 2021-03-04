package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.SailAsks;
import com.bigwiner.android.entity.Company;
import com.bigwiner.android.entity.Complaint;
import com.bigwiner.android.entity.SailMember;
import com.bigwiner.android.handler.SailMemberHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.CompanyDetialActivity;
import com.bigwiner.android.view.activity.ContactDetialActivity;
import com.bigwiner.android.view.activity.SailMemberActivity;
import com.bigwiner.android.view.activity.ConversationListActivity;
import com.bigwiner.android.view.adapter.ContactsAdapter;
import com.bigwiner.android.view.adapter.SailMemberAdapter;

import java.io.File;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SailMemberPresenter implements Presenter {

    public SailMemberActivity mSailMemberActivity;
    public SailMemberHandler mSailMemberHandler;
    public SailMemberPresenter(SailMemberActivity mSailMemberActivity)
    {
        mSailMemberHandler = new SailMemberHandler(mSailMemberActivity);
        this.mSailMemberActivity = mSailMemberActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mSailMemberActivity, Color.argb(0, 255, 255, 255));
        mSailMemberActivity.setContentView(R.layout.activity_sailmenber);

        PullToRefreshView pullToRefreshView = mSailMemberActivity.findViewById(R.id.headview);
        pullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.onFooterRefreshComplete();
        pullToRefreshView.onHeaderRefreshComplete();
        pullToRefreshView.getmHeaderView().setVisibility(View.INVISIBLE);
        pullToRefreshView.getmFooterView().setVisibility(View.INVISIBLE);
        pullToRefreshView.setOnFooterRefreshListener(mSailMemberActivity.onFooterRefreshListener);
        pullToRefreshView.setOnHeaderRefreshListener(mSailMemberActivity.onHeadRefreshListener);
        mSailMemberActivity.searchView = mSailMemberActivity.findViewById(R.id.search);
        mSailMemberActivity.searchView.setOnCancleListener(doCancle);
        mSailMemberActivity.searchView.mSetOnSearchListener(mSailMemberActivity.onEditorActionListener);
        mSailMemberActivity.mToolBarHelper.hidToolbar(mSailMemberActivity, (RelativeLayout) mSailMemberActivity.findViewById(R.id.buttomaciton));
        mSailMemberActivity.measureStatubar(mSailMemberActivity, (RelativeLayout) mSailMemberActivity.findViewById(R.id.stutebar));
        mSailMemberActivity.listView = mSailMemberActivity.findViewById(R.id.contacts);
        mSailMemberActivity.listView.setLayoutManager(new LinearLayoutManager(mSailMemberActivity));
        mSailMemberActivity.back = mSailMemberActivity.findViewById(R.id.back);
        mSailMemberActivity.back.setOnClickListener(mSailMemberActivity.backListener);
        mSailMemberActivity.mSailMemberAdapter = new SailMemberAdapter(mSailMemberActivity.sailMembers,mSailMemberActivity);
        mSailMemberActivity.listView.setAdapter(mSailMemberActivity.mSailMemberAdapter);
        mSailMemberActivity.mSailMemberAdapter.setOnItemClickListener(mSailMemberActivity.onItemClickListener);
        mSailMemberActivity.waitDialog.show();
        SailAsks.getSailCompanyMember(mSailMemberActivity,mSailMemberHandler,mSailMemberActivity.sail.currentpage,mSailMemberActivity.sail.pagesize,mSailMemberActivity.searchView.getText());

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

    public void starCompanyDetial(SailMember complaint) {
        Company company = new Company();
        company.id = complaint.cid;
        company.name = complaint.cname;
        Intent intent = new Intent(mSailMemberActivity, CompanyDetialActivity.class);
        intent.putExtra("company",company);
        mSailMemberActivity.startActivity(intent);
    }

    public void doSearch(String keyword) {
        if(keyword.length() > 0)
        {
            mSailMemberActivity.waitDialog.show();
            mSailMemberActivity.sailMembers.clear();
            mSailMemberActivity.sail.reset();
            SailAsks.getSailCompanyMember(mSailMemberActivity,mSailMemberHandler,mSailMemberActivity.sail.currentpage,mSailMemberActivity.sail.pagesize,mSailMemberActivity.searchView.getText());
        }
        else
        {
            mSailMemberActivity.listView.setAdapter(mSailMemberActivity.mSailMemberAdapter);
        }
    }

    public SearchViewLayout.DoCancle doCancle = new SearchViewLayout.DoCancle() {
        @Override
        public void doCancle() {
            mSailMemberActivity.listView.setAdapter(mSailMemberActivity.mSailMemberAdapter);
        }
    };
}
