package intersky.mail.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import intersky.mail.presenter.MailAddressPresenter;

@SuppressLint("ClickableViewAccessibility")
public class MailAddressActivity extends BaseActivity
{

	private MailAddressPresenter mMailAddressPresenter = new MailAddressPresenter(this);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mMailAddressPresenter.Create();
		
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		mMailAddressPresenter.Pause();

	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		mMailAddressPresenter.Resume();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mMailAddressPresenter.Destroy();
	}

}
