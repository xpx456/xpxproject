package intersky.notice.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.mywidget.MyLinearLayout;
import intersky.notice.entity.Notice;
import intersky.notice.presenter.NoticeReadlPresenter;


@SuppressLint("ClickableViewAccessibility")
public class NoticeReadlActivity extends BaseActivity
{
	public PopupWindow popupWindow1;
	public TextView senderText;
	public TextView copyerText;
	public RelativeLayout mshada;
	public MyLinearLayout sender;
	public MyLinearLayout copyer;
	public ArrayList<Contacts> mSenders = new ArrayList<Contacts>();
	public ArrayList<Contacts> mCopyers = new ArrayList<Contacts>();
	public Notice notice;
	public NoticeReadlPresenter mNoticeReadPresenter = new NoticeReadlPresenter(this);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mNoticeReadPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mNoticeReadPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mNoticeReadPresenter.Start();
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
		mNoticeReadPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mNoticeReadPresenter.Resume();
	}

}
