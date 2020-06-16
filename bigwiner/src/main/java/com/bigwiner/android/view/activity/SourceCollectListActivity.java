package com.bigwiner.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.presenter.SourceCollectListPresenter;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.adapter.SourceListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.mywidget.PullToRefreshView;

/**
 * Created by xpx on 2017/8/18.
 */

public class SourceCollectListActivity extends BaseActivity {

    public static final int TYPE_MY = 0;
    public static final int TYPE_WANT = 1;
    public static final int TYPE_ALL = 2;
    public SourceCollectListPresenter mSourceCollectListPresenter = new SourceCollectListPresenter(this);
    public RecyclerView listView;
    public ImageView back;
    public TextView showList;
    public SourceListAdapter collectAdapter;
    public Meeting meeting;
    public ArrayList<SourceData> collectDatas = new ArrayList<SourceData>();
    public HashMap<String,SourceData> collectHashData = new HashMap<String ,SourceData>();
    public ModuleDetial collectdetial = new ModuleDetial();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSourceCollectListPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSourceCollectListPresenter.Destroy();
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
            if (mSourceCollectListPresenter.mSourceCollectListActivity.collectdetial.currentpage < mSourceCollectListPresenter.mSourceCollectListActivity.collectdetial.totlepage) {
                mSourceCollectListPresenter.mSourceCollectListActivity.waitDialog.show();
                SourceAsks.getSourceListCollect(mSourceCollectListPresenter.mSourceCollectListActivity,mSourceCollectListPresenter.mSourceCollectListHandler,
                        mSourceCollectListPresenter.mSourceCollectListActivity.collectdetial.pagesize,
                        mSourceCollectListPresenter.mSourceCollectListActivity.collectdetial.currentpage+1);

            } else {
                AppUtils.showMessage(mSourceCollectListPresenter.mSourceCollectListActivity, mSourceCollectListPresenter.mSourceCollectListActivity.getString(R.string.system_addall));
            }
            view.onFooterRefreshComplete();
        }
    };

    public PullToRefreshView.OnHeaderRefreshListener onHeadRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {


            mSourceCollectListPresenter.mSourceCollectListActivity.collectdetial.reset();
            mSourceCollectListPresenter.mSourceCollectListActivity.collectDatas.clear();
            mSourceCollectListPresenter.mSourceCollectListActivity.collectAdapter.notifyDataSetChanged();
            mSourceCollectListPresenter.mSourceCollectListActivity.collectHashData.clear();
            mSourceCollectListPresenter.mSourceCollectListActivity.waitDialog.show();
            SourceAsks.getSourceListCollect(mSourceCollectListPresenter.mSourceCollectListActivity,mSourceCollectListPresenter.mSourceCollectListHandler,
                    mSourceCollectListPresenter.mSourceCollectListActivity.collectdetial.pagesize,
                    mSourceCollectListPresenter.mSourceCollectListActivity.collectdetial.currentpage);
            view.onHeaderRefreshComplete();
        }
    };

    public SourceListAdapter.OnItemClickListener onContactItemClickListener = new SourceListAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(SourceData sourceData, int position, View view) {
            mSourceCollectListPresenter.starSourceDetial(sourceData);
        }
    };

}
