package com.intersky.android.presenter;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intersky.R;
import com.intersky.android.view.activity.AboutActivity;
import com.intersky.android.view.activity.SafeActivity;

import intersky.appbase.Presenter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class AboutPresenter implements Presenter {

	public AboutActivity mAboutActivity;
	
	public AboutPresenter(AboutActivity mAboutActivity) {
		this.mAboutActivity = mAboutActivity;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mAboutActivity.setContentView(R.layout.activity_about);
		mAboutActivity.safe = mAboutActivity.findViewById(R.id.a6);
		mAboutActivity.safe.setOnClickListener(mAboutActivity.safeListener);
		PackageManager packageManager = mAboutActivity.getPackageManager();
		try
		{
			PackageInfo packInfo = packageManager.getPackageInfo(
					mAboutActivity.getPackageName(), 0);
			TextView version = (TextView) mAboutActivity.findViewById(R.id.version_name);
			version.setText("V" + packInfo.versionName);
		}
		catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ToolBarHelper.setTitle(mAboutActivity.mActionBar,mAboutActivity.getResources().getString(R.string.aboutIntersky));
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

	public void showSafe() {
		Intent intent = new Intent(mAboutActivity, SafeActivity.class);
		mAboutActivity.startActivity(intent);
	}

}
