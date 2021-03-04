package com.dk.dkhome.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.dk.dkhome.presenter.MainPresenter;


public class MainActivity extends BaseActivity
{
	public final static int SPLASH_DISPLAY_LENGHT = 3000; // 延迟3秒
	public static final String ACTION_UPDATA_DAIRY = "ACTION_UPDATA_DAIRY";
	public MainPresenter mMainPresenter = new MainPresenter(this);

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mMainPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mMainPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mMainPresenter.Start();
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
		mMainPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mMainPresenter.Resume();
		super.onResume();
	}

	@Override
	public void onGetPermissionFinish()
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		mMainPresenter.dairyView.takePhotoResult(requestCode, resultCode, data);

	}

	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
