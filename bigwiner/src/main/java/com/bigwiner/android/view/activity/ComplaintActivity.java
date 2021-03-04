package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.asks.SailAsks;
import com.bigwiner.android.entity.Complaint;
import com.bigwiner.android.presenter.ComplaintPresenter;
import com.bigwiner.android.view.adapter.ComplaintAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;

/**
 * Created by xpx on 2017/8/18.
 */

public class ComplaintActivity extends BaseActivity {

    public ComplaintPresenter mComplaintPresenter = new ComplaintPresenter(this);
    public RecyclerView listView;
    public ImageView back;
    public SearchViewLayout searchView;
    public ModuleDetial sail = new ModuleDetial();
    public ArrayList<Complaint> Complaints = new ArrayList<Complaint>();
    public ComplaintAdapter mComplaintAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComplaintPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mComplaintPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public PullToRefreshView.OnFooterRefreshListener onFooterRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {

        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            if (mComplaintPresenter.mComplaintActivity.sail.currentpage < mComplaintPresenter.mComplaintActivity.sail.totlepage) {
                mComplaintPresenter.mComplaintActivity.waitDialog.show();
                SailAsks.getComplantMember(mComplaintPresenter.mComplaintActivity, mComplaintPresenter.mComplaintHandler
                        , mComplaintPresenter.mComplaintActivity.sail.currentpage + 1, mComplaintPresenter.mComplaintActivity.sail.pagesize,searchView.getText());
            } else {
                AppUtils.showMessage(mComplaintPresenter.mComplaintActivity, mComplaintPresenter.mComplaintActivity.getString(R.string.system_addall));
            }
            view.onFooterRefreshComplete();
        }
    };

    public PullToRefreshView.OnHeaderRefreshListener onHeadRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            mComplaintPresenter.mComplaintActivity.waitDialog.show();
            mComplaintPresenter.mComplaintActivity.sail.reset();
            mComplaintPresenter.mComplaintActivity.Complaints.clear();;
            SailAsks.getComplantMember(mComplaintPresenter.mComplaintActivity, mComplaintPresenter.mComplaintHandler
                    , mComplaintPresenter.mComplaintActivity.sail.currentpage, mComplaintPresenter.mComplaintActivity.sail.pagesize,searchView.getText());
            view.onHeaderRefreshComplete();
        }
    };

    public ComplaintAdapter.OnItemClickListener onItemClickListener = new ComplaintAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Complaint complaint, int position, View view) {
            mComplaintPresenter.starCompanyDetial(complaint);
        }
    };

    public TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                mComplaintPresenter.doSearch(v.getText().toString());
            }
            return true;
        }
    };

}
