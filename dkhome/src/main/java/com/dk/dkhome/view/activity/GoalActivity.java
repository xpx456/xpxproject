package com.dk.dkhome.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dk.dkhome.presenter.GoalPresenter;


public class GoalActivity extends BaseActivity
{
	public GoalPresenter mGoalPresenter = new GoalPresenter(this);

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mGoalPresenter.Create();
	}
	
	@Override
	protected void onDestroy()
	{
		mGoalPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mGoalPresenter.Start();
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
		mGoalPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mGoalPresenter.Resume();
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
