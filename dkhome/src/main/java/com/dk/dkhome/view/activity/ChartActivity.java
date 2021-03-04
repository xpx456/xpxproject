package com.dk.dkhome.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dk.dkhome.presenter.ChartPresenter;


public class ChartActivity extends BaseActivity
{
	public ChartPresenter mChartPresenter = new ChartPresenter(this);

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mChartPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mChartPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mChartPresenter.Start();
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
		mChartPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mChartPresenter.Resume();
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
