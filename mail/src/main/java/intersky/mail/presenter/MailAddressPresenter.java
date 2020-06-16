package intersky.mail.presenter;

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.widget.TextView;

import intersky.mail.R;
import intersky.mail.view.activity.MailAddressActivity;
import intersky.appbase.Presenter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class MailAddressPresenter implements Presenter {
	
	private MailAddressActivity mMailAddressActivity;
	
	public MailAddressPresenter(MailAddressActivity mMailAddressActivity)
	{
		this.mMailAddressActivity = mMailAddressActivity;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mMailAddressActivity.setContentView(R.layout.activity_show_mail_address);

		mMailAddressActivity.mGestureDetector = new GestureDetector((OnGestureListener) mMailAddressActivity);
		TextView mTextView = (TextView) mMailAddressActivity.findViewById(R.id.person_name);
		mTextView.setText(mMailAddressActivity.getIntent().getStringExtra("name"));
		mTextView = (TextView) mMailAddressActivity.findViewById(R.id.person_mail);
		ToolBarHelper.setTitle(mMailAddressActivity.mActionBar, mMailAddressActivity.getIntent().getStringExtra("name"));
		if(mMailAddressActivity.getIntent().getBooleanExtra("islocal", false) == true)
		{
			mTextView.setVisibility(View.INVISIBLE);
		}
		else
		{
			mTextView.setText(mMailAddressActivity.getIntent().getStringExtra("mail"));
		}
	}

	@Override
	public void Create() {
		// TODO Auto-generated method stub
		initView();
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		
	}

}
