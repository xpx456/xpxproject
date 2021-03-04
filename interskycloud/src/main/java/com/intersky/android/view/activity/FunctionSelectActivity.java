package com.intersky.android.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.intersky.android.presenter.FunctionSelectPresenter;
import com.intersky.android.view.adapter.FunctionAddAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import intersky.function.entity.Function;
import intersky.mywidget.SearchViewLayout;


@SuppressLint("ClickableViewAccessibility")
public class FunctionSelectActivity extends BaseActivity implements OnGestureListener,OnTouchListener
{

	public static final String ACTION_UPDATA_WORK_ITEMS = "ACTION_UPDATA_WORK_ITEMS";
	public ImageView back;
	public RecyclerView listView;
	public FunctionAddAdapter functionAddAdapter;
	public FunctionAddAdapter functionSearchAddAdapter;
	public RelativeLayout buttomlayer;
	public TextView titleAdd;
	public TextView btnAdd;
	public int oldcount;
	public SearchViewLayout searchView;
	public ArrayList<Function> add = new ArrayList<Function>();
	public ArrayList<Function> other = new ArrayList<Function>();
	public ArrayList<Function> search = new ArrayList<Function>();
	private FunctionSelectPresenter mFunctionSelectPresenter = new FunctionSelectPresenter(this);
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mFunctionSelectPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mFunctionSelectPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mFunctionSelectPresenter.Start();
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
		mFunctionSelectPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mFunctionSelectPresenter.Resume();
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
