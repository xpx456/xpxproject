package com.intersky.strang.android.presenter;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import com.intersky.strang.R;
import com.intersky.strang.android.view.activity.TestActivity;
import com.intersky.strang.android.view.activity.SafeActivity;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

public class TestPresenter implements Presenter {

	public TestActivity mTestActivity;
	public TestThread testThread;
	public TestPresenter(TestActivity mTestActivity) {
		this.mTestActivity = mTestActivity;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mTestActivity.setContentView(R.layout.activity_about);
		mTestActivity.safe = mTestActivity.findViewById(R.id.a6);
		mTestActivity.safe.setOnClickListener(mTestActivity.safeListener);
		PackageManager packageManager = mTestActivity.getPackageManager();
		try
		{
			PackageInfo packInfo = packageManager.getPackageInfo(
					mTestActivity.getPackageName(), 0);
			TextView version = (TextView) mTestActivity.findViewById(R.id.version_name);
			version.setText("V" + packInfo.versionName);
		}
		catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ToolBarHelper.setTitle(mTestActivity.mActionBar,mTestActivity.getResources().getString(R.string.aboutIntersky));
		testThread = new TestThread(mTestActivity);
		testThread.start();
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
		Intent intent = new Intent(mTestActivity, SafeActivity.class);
		testThread.looperThreadHandler.sendEmptyMessage(1);
		mTestActivity.startActivity(intent);

	}

	public class TestThread  extends Thread{
		public Looper myLooper;
		public TestActivity testActivity;
		public Handler looperThreadHandler;
		public TestThread(TestActivity testActivity) {
			this.testActivity = testActivity;
		}
		@Override
		public void run() {
			Looper.prepare();
			synchronized (this) {
				myLooper = Looper.myLooper();
				notifyAll();
			}
			looperThreadHandler=new Handler(myLooper){


				public void handleMessage(Message msg) {
					switch (msg.what) {
						case 1:
							AppUtils.showMessage(testActivity,"SafeActivity");
							break;
					}
				}
			};
			Looper.loop();
			super.run();
		}

	}


}
