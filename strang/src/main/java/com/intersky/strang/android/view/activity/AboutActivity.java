package com.intersky.strang.android.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.intersky.strang.android.presenter.AboutPresenter;

import intersky.appbase.BaseActivity;


@SuppressLint("ClickableViewAccessibility")
public class AboutActivity extends BaseActivity implements OnGestureListener,OnTouchListener
{
	public static final int TIMES = 5;
	public TextView safe;
	private AboutPresenter mAboutPresenter = new AboutPresenter(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mAboutPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mAboutPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mAboutPresenter.Start();
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
		mAboutPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mAboutPresenter.Resume();
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

	public View.OnClickListener safeListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mAboutPresenter.showSafe();
		}
	};
}
