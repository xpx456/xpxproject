package intersky.mail.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import intersky.mail.presenter.MailFilePresenter;

@SuppressLint("ClickableViewAccessibility")
public class MailFileActivity extends BaseActivity
{

	public MailFilePresenter mMailFilePresenter = new MailFilePresenter(this);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mMailFilePresenter.Create();
		
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		mMailFilePresenter.Pause();

	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		mMailFilePresenter.Resume();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mMailFilePresenter.Destroy();
	}

}
