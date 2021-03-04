package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.entity.SearchWord;
import com.bigwiner.android.presenter.SearchPresenter;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.adapter.SearchWordAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Conversation;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.SearchViewLayout;
import intersky.mywidget.TabHeadView;

public class SearchActivity extends BaseActivity
{
	public static final String ACTION_UPDATE_SEARCH_FRIENDS_LIST = "ACTION_UPDATE_SEARCH_FRIENDS_LIST";
	public SearchPresenter mSearchPresenter = new SearchPresenter(this);
	public SearchViewLayout searchView;
	public RelativeLayout btnBach;
	public MyLinearLayout history;
	public TabHeadView mTabHeadView;
	public TextView nologinBtn;
	public String hisKeyword = "";
	public static final int MAXHISTORY = 8;
	public ImageView cleanHis;
	public NoScrollViewPager mViewPager;
	public ConversationPageAdapter mLoderPageAdapter;
	public ArrayList<ListView> listViews = new ArrayList<ListView>();
	public SearchWordAdapter noticeAdapter;
	public SearchWordAdapter meetingAdapter;
	public SearchWordAdapter newsAdapter;
	public SearchWordAdapter friendAdapter;
	public ArrayList<SearchWord> notices = new ArrayList<SearchWord>();
	public ArrayList<SearchWord> meeting = new ArrayList<SearchWord>();
	public ArrayList<SearchWord> news = new ArrayList<SearchWord>();
	public ArrayList<SearchWord> friend = new ArrayList<SearchWord>();
	public ArrayList<View> mViews = new ArrayList<View>();
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mSearchPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mSearchPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mSearchPresenter.Start();
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
		mSearchPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mSearchPresenter.Resume();
		super.onResume();
	}

	public View.OnClickListener backListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	public View.OnClickListener cleanHistroyListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mSearchPresenter.cleanHistory();
		}
	};

	public TextView.OnEditorActionListener mOnSearchActionListener = new TextView.OnEditorActionListener()
	{

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			// TODO Auto-generated method stub
			if (actionId == EditorInfo.IME_ACTION_SEARCH)
			{
				mSearchPresenter.doSearch(v.getText().toString(),"");
			}
			return true;
		}
	};


	public View.OnClickListener searchLableListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			mSearchPresenter.doSearchLabke(v);
		}
	};

	public TabHeadView.OnTabLisener onTabLisener = new TabHeadView.OnTabLisener()
	{

		@Override
		public void TabClick(int tab) {
			if(tab == 4)
			{
				if(BigwinerApplication.mApp.mAccount.islogin == false)
				{
					nologinBtn.setVisibility(View.VISIBLE);
				}
				else
				{
					nologinBtn.setVisibility(View.INVISIBLE);
				}
			}
		}
	};
}
