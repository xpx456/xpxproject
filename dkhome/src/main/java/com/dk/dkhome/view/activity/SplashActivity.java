package com.dk.dkhome.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.dk.dkhome.presenter.SplashPresenter;



public class SplashActivity extends BaseActivity
{
	public final static int SPLASH_DISPLAY_LENGHT = 3000; // 延迟3秒
	public View popupWindow;
	public RelativeLayout shade;
	public SplashPresenter mSplashPresenter = new SplashPresenter(this);

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mSplashPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mSplashPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mSplashPresenter.Start();
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
		mSplashPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mSplashPresenter.Resume();
		super.onResume();
	}

	@Override
	public void onGetPermissionFinish()
	{
		// TODO Auto-generated method stub
		mSplashPresenter.startMain();
	}
}
