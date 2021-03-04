package com.dk.dkhome.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.dk.dkhome.presenter.BigChartPresenter;


@SuppressLint("ClickableViewAccessibility")
public class BigChartActivity extends BaseActivity implements OnGestureListener,OnTouchListener
{
	public static final int TIMES = 5;
	public TextView safe;
	public BigChartPresenter mBigChartPresenter = new BigChartPresenter(this);
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mBigChartPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mBigChartPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mBigChartPresenter.Start();
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
		mBigChartPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mBigChartPresenter.Resume();
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
