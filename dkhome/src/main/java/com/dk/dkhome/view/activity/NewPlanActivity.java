package com.dk.dkhome.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.dk.dkhome.presenter.NewPlanPresenter;


public class NewPlanActivity extends BaseActivity{

	public NewPlanPresenter mNewPlanPresenter = new NewPlanPresenter(this);

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mNewPlanPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mNewPlanPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mNewPlanPresenter.Start();
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
		mNewPlanPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mNewPlanPresenter.Resume();
		super.onResume();
	}

	@Override
	public void onGetPermissionFinish()
	{
		// TODO Auto-generated method stub
	}
}
