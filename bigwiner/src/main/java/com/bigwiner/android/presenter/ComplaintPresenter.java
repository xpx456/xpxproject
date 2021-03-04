package com.bigwiner.android.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigwiner.R;
import com.bigwiner.android.asks.SailAsks;
import com.bigwiner.android.entity.Company;
import com.bigwiner.android.entity.Complaint;
import com.bigwiner.android.handler.ComplaintHandler;
import com.bigwiner.android.view.activity.CompanyDetialActivity;
import com.bigwiner.android.view.activity.ComplaintActivity;
import com.bigwiner.android.view.adapter.ComplaintAdapter;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ComplaintPresenter implements Presenter {

    public ComplaintActivity mComplaintActivity;
    public ComplaintHandler mComplaintHandler;
    public ComplaintPresenter(ComplaintActivity mComplaintActivity)
    {
        mComplaintHandler = new ComplaintHandler(mComplaintActivity);
        this.mComplaintActivity = mComplaintActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mComplaintActivity, Color.argb(0, 255, 255, 255));
        mComplaintActivity.setContentView(R.layout.activity_complant);

        PullToRefreshView pullToRefreshView = mComplaintActivity.findViewById(R.id.headview);
        pullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        pullToRefreshView.onFooterRefreshComplete();
        pullToRefreshView.onHeaderRefreshComplete();
        pullToRefreshView.getmHeaderView().setVisibility(View.INVISIBLE);
        pullToRefreshView.getmFooterView().setVisibility(View.INVISIBLE);
        pullToRefreshView.setOnFooterRefreshListener(mComplaintActivity.onFooterRefreshListener);
        pullToRefreshView.setOnHeaderRefreshListener(mComplaintActivity.onHeadRefreshListener);
        mComplaintActivity.searchView = mComplaintActivity.findViewById(R.id.search);
        mComplaintActivity.searchView.mSetOnSearchListener(mComplaintActivity.onEditorActionListener);
        mComplaintActivity.searchView.setOnCancleListener(doCancle);
        mComplaintActivity.mToolBarHelper.hidToolbar(mComplaintActivity, (RelativeLayout) mComplaintActivity.findViewById(R.id.buttomaciton));
        mComplaintActivity.measureStatubar(mComplaintActivity, (RelativeLayout) mComplaintActivity.findViewById(R.id.stutebar));

        mComplaintActivity.listView = mComplaintActivity.findViewById(R.id.contacts);
        mComplaintActivity.listView.setLayoutManager(new LinearLayoutManager(mComplaintActivity));
        mComplaintActivity.back = mComplaintActivity.findViewById(R.id.back);
        mComplaintActivity.back.setOnClickListener(mComplaintActivity.backListener);
        mComplaintActivity.mComplaintAdapter = new ComplaintAdapter(mComplaintActivity.Complaints,mComplaintActivity);
        mComplaintActivity.listView.setAdapter(mComplaintActivity.mComplaintAdapter);
        mComplaintActivity.mComplaintAdapter.setOnItemClickListener(mComplaintActivity.onItemClickListener);
        mComplaintActivity.waitDialog.show();
        SailAsks.getComplantMember(mComplaintActivity,mComplaintHandler,mComplaintActivity.sail.currentpage,mComplaintActivity.sail.pagesize,mComplaintActivity.searchView.getText());

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

    public void starCompanyDetial(Complaint complaint) {
        if(complaint.cid.length() > 0)
        {
            Company company = new Company();
            company.id = complaint.cid;
            company.name = complaint.cname;
            Intent intent = new Intent(mComplaintActivity, CompanyDetialActivity.class);
            intent.putExtra("company",company);
            mComplaintActivity.startActivity(intent);
        }
        else
        {
            AppUtils.showMessage(mComplaintActivity,mComplaintActivity.getString(R.string.company_not_exist));
        }
    }

    public void doSearch(String keyword) {
        if(keyword.length() > 0)
        {
            mComplaintActivity.waitDialog.show();
            mComplaintActivity.sail.reset();
            mComplaintActivity.Complaints.clear();;
            SailAsks.getComplantMember(mComplaintActivity, mComplaintHandler
                    , mComplaintActivity.sail.pagesize, mComplaintActivity.sail.currentpage,keyword);
        }
    }

    public SearchViewLayout.DoCancle doCancle = new SearchViewLayout.DoCancle() {
        @Override
        public void doCancle() {
            mComplaintActivity.waitDialog.show();
            mComplaintActivity.sail.reset();
            mComplaintActivity.Complaints.clear();;
            SailAsks.getComplantMember(mComplaintActivity, mComplaintHandler
                    , mComplaintActivity.sail.pagesize, mComplaintActivity.sail.currentpage,"");
        }
    };


}
