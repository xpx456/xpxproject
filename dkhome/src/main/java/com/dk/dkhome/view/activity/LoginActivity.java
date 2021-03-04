package com.dk.dkhome.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.dk.dkhome.presenter.LoginPresenter;


public class LoginActivity extends BaseActivity
{
	public final static int SPLASH_DISPLAY_LENGHT = 3000; // 延迟3秒
	public View popupWindow;
	public RelativeLayout shade;
	public LoginPresenter mLoginPresenter = new LoginPresenter(this);

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mLoginPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mLoginPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mLoginPresenter.Start();
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
		mLoginPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mLoginPresenter.Resume();
		super.onResume();
	}

	@Override
	public void onGetPermissionFinish()
	{
		// TODO Auto-generated method stub
	}
}
