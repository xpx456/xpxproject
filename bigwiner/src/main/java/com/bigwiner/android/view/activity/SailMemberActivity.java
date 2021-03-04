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
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.SailAsks;
import com.bigwiner.android.entity.Complaint;
import com.bigwiner.android.entity.SailMember;
import com.bigwiner.android.presenter.SailMemberPresenter;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.adapter.ContactsAdapter;
import com.bigwiner.android.view.adapter.SailMemberAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;

/**
 * Created by xpx on 2017/8/18.
 */

public class SailMemberActivity extends BaseActivity {

    public SailMemberPresenter mSailMemberPresenter = new SailMemberPresenter(this);
    public RecyclerView listView;
    public ImageView back;
    public SearchViewLayout searchView;
    public ModuleDetial sail = new ModuleDetial();
    public ArrayList<SailMember> sailMembers = new ArrayList<SailMember>();
    public SailMemberAdapter mSailMemberAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSailMemberPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSailMemberPresenter.Destroy();
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
            if (mSailMemberPresenter.mSailMemberActivity.sail.currentpage < mSailMemberPresenter.mSailMemberActivity.sail.totlepage) {
                mSailMemberPresenter.mSailMemberActivity.waitDialog.show();
                SailAsks.getSailCompanyMember(mSailMemberPresenter.mSailMemberActivity, mSailMemberPresenter.mSailMemberHandler
                        , mSailMemberPresenter.mSailMemberActivity.sail.currentpage + 1,mSailMemberPresenter.mSailMemberActivity.sail.pagesize,
                        mSailMemberPresenter.mSailMemberActivity.searchView.getText());
            } else {
                AppUtils.showMessage(mSailMemberPresenter.mSailMemberActivity, mSailMemberPresenter.mSailMemberActivity.getString(R.string.system_addall));
            }
            view.onFooterRefreshComplete();
        }
    };

    public PullToRefreshView.OnHeaderRefreshListener onHeadRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            mSailMemberPresenter.mSailMemberActivity.waitDialog.show();
            mSailMemberPresenter.mSailMemberActivity.sail.reset();
            mSailMemberPresenter.mSailMemberActivity.sailMembers.clear();
            SailAsks.getSailCompanyMember(mSailMemberPresenter.mSailMemberActivity, mSailMemberPresenter.mSailMemberHandler
                    , mSailMemberPresenter.mSailMemberActivity.sail.currentpage, mSailMemberPresenter.mSailMemberActivity.sail.pagesize,
                    mSailMemberPresenter.mSailMemberActivity.searchView.getText());
            view.onHeaderRefreshComplete();
        }
    };

    public SailMemberAdapter.OnItemClickListener onItemClickListener = new SailMemberAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(SailMember mSailMember, int position, View view) {
            mSailMemberPresenter.starCompanyDetial(mSailMember);
        }
    };

    public TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                mSailMemberPresenter.doSearch(v.getText().toString());
            }
            return true;
        }
    };

}
