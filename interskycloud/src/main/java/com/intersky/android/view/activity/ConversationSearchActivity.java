package com.intersky.android.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.TextView;

import com.intersky.android.presenter.ConversationSearchPresenter;

import java.util.ArrayList;

import intersky.appbase.entity.Conversation;
import intersky.conversation.view.adapter.ConversationAdapter;
import intersky.mywidget.SearchViewLayout;


@SuppressLint("ClickableViewAccessibility")
public class ConversationSearchActivity extends BaseActivity implements OnGestureListener,OnTouchListener
{
	public static final int TIMES = 5;
	public SearchViewLayout searchView;
	public ListView conversationList;
	public ConversationAdapter mConversationSearchAdapter;
	public ArrayList<Conversation> mSearchConversations = new ArrayList<Conversation>();
	private ConversationSearchPresenter mConversationSearchPresenter = new ConversationSearchPresenter(this);
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mConversationSearchPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mConversationSearchPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mConversationSearchPresenter.Start();
	}

	@Override
	protected void onStop()
	{
		super.onStop();

	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		mConversationSearchPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mConversationSearchPresenter.Resume();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);  
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
		// TODO Auto-generated method stub

	}

}
