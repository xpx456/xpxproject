package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.android.presenter.SearchResultPresenter;
import com.bigwiner.android.view.adapter.ConversationAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Conversation;
import intersky.mywidget.PullToRefreshView;


public class SearchResultActivity extends BaseActivity
{

	public SearchResultPresenter mSearchResultPresenter = new SearchResultPresenter(this);
	public RelativeLayout searchView;
	public RelativeLayout btnBack;
	public ArrayList<Conversation> conversations = new ArrayList<Conversation>();
	public ArrayList<Conversation> notices = new ArrayList<Conversation>();
	public ArrayList<Conversation> meetings = new ArrayList<Conversation>();
	public ArrayList<Conversation> news = new ArrayList<Conversation>();
	public ArrayList<Conversation> messages = new ArrayList<Conversation>();
	public ConversationAdapter conversationAdapter;
	public PullToRefreshView pullToRefreshView;
	public RecyclerView listView;
	public boolean meetingflg = false;
	public boolean noticeflg = false;
	public boolean newsflg = false;
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mSearchResultPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mSearchResultPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mSearchResultPresenter.Start();
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	@Override
	protected void onPause()
	{
		mSearchResultPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mSearchResultPresenter.Resume();
		super.onResume();
	}

	public View.OnClickListener backListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	public View.OnClickListener showSearchListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mSearchResultPresenter.doSearch();
		}
	};

	public ConversationAdapter.OnItemClickListener itemClickListener = new ConversationAdapter.OnItemClickListener()
	{

		@Override
		public void onItemClick(Conversation conversation, int position, View view) {
			mSearchResultPresenter.onItemClick(conversation);
		}
	};

	public PullToRefreshView.OnFooterRefreshListener onFooterRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {

		@Override
		public void onFooterRefresh(PullToRefreshView view) {
			mSearchResultPresenter.upDataSearch();
			view.onFooterRefreshComplete();
		}
	};

	public PullToRefreshView.OnHeaderRefreshListener onHeadRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

		@Override
		public void onHeaderRefresh(PullToRefreshView view) {
			mSearchResultPresenter.upDataSearch();
			view.onHeaderRefreshComplete();
		}
	};
}
