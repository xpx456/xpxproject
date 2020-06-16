package com.bigwiner.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.presenter.SourceAddListPresenter;
import com.bigwiner.android.view.adapter.SourceListEditAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.ModuleDetial;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;

/**
 * Created by xpx on 2017/8/18.
 */

public class SourceAddListActivity extends BaseActivity {

    public SourceAddListPresenter mSourceAddListPresenter = new SourceAddListPresenter(this);
    public RecyclerView listWant;
    public RecyclerView listOffer;
    public NoScrollViewPager mViewPager;
    public ConversationPageAdapter mLoderPageAdapter;
    public ImageView back;
    public SourceListEditAdapter mWantSourceListAdapter;
    public SourceListEditAdapter mOfferSourceListAdapter;
    public RelativeLayout tabWant;
    public TextView wantTxt;
    public RelativeLayout tabOffer;
    public TextView offerTxt;
    public ArrayList<SourceData> wantData = new ArrayList<SourceData>();
    public ArrayList<SourceData> offerData = new ArrayList<SourceData>();
    public ModuleDetial sourceWantDetial = new ModuleDetial();
    public ModuleDetial sourceOfferDetial = new ModuleDetial();
    public ArrayList<View> mViews = new ArrayList<View>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSourceAddListPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSourceAddListPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener tabWantListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceAddListPresenter.tabWant();
        }
    };

    public View.OnClickListener tabOfferListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceAddListPresenter.tabOffer();
        }
    };

    public PullToRefreshView.OnFooterRefreshListener onFooterRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {

        @Override
        public void onFooterRefresh(PullToRefreshView view) {

            view.onFooterRefreshComplete();
        }
    };

    public PullToRefreshView.OnHeaderRefreshListener onHeadRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            view.onHeaderRefreshComplete();
        }
    };

    public SourceListEditAdapter.OnItemClickListener onSourceItemClickListener = new SourceListEditAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(SourceData sourceData, int position, View view) {
            mSourceAddListPresenter.starSourceDetial(sourceData);
        }

    };

}
