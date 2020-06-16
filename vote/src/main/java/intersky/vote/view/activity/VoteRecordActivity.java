package intersky.vote.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.vote.entity.Reocrd;
import intersky.vote.entity.Vote;
import intersky.vote.presenter.VoteRecordPresenter;
import intersky.vote.view.adapter.RecordAdapter;


@SuppressLint("ClickableViewAccessibility")
public class VoteRecordActivity extends BaseActivity
{
	public VoteRecordPresenter mVoteRecordPresenter = new VoteRecordPresenter(this);
	public ListView mListView;
	public RecordAdapter mRecordAdapter;
	public ArrayList<Reocrd> mReocrds = new ArrayList<Reocrd>();
	public Vote vote;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mVoteRecordPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mVoteRecordPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mVoteRecordPresenter.Start();
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
		mVoteRecordPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mVoteRecordPresenter.Resume();
	}





}
