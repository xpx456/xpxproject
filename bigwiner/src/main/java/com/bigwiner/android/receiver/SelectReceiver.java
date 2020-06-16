package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.handler.UserInfoHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.UserInfoActivity;

import intersky.appbase.BaseReceiver;

public class SelectReceiver extends BaseReceiver {

	private Handler mHandler;

	public SelectReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(BigwinerApplication.ACTION_LOCATION_CHANGE);

	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(BigwinerApplication.ACTION_LOCATION_CHANGE)) {

			Message msg = new Message();
			msg.what = UserInfoHandler.EVENT_SET_AREA;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
