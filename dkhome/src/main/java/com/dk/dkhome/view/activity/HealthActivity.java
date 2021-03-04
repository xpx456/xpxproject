package com.dk.dkhome.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.dk.dkhome.presenter.HealthPresenter;


public class HealthActivity extends BaseActivity
{
	public HealthPresenter mHealthPresenter = new HealthPresenter(this);

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mHealthPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mHealthPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mHealthPresenter.Start();
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
		mHealthPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mHealthPresenter.Resume();
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

	}
}
