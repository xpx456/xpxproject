package com.dk.dkhome.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dk.dkhome.presenter.DevicePresenter;


public class DeviceActivity extends BaseActivity
{
	public DevicePresenter mDevicePresenter = new DevicePresenter(this);

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mDevicePresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mDevicePresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mDevicePresenter.Start();
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
		mDevicePresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mDevicePresenter.Resume();
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
