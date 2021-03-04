package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.handler.SailHandler;
import com.bigwiner.android.handler.UserInfoHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.SailActivity;
import com.bigwiner.android.view.activity.SailApplyActivity;

import intersky.appbase.BaseReceiver;

public class SailReceiver extends BaseReceiver {

	private Handler mHandler;

	public SailReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(SailActivity.ACTION_SAIL_APPLY_SUCCESS);

	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(SailActivity.ACTION_SAIL_APPLY_SUCCESS)) {

			Message msg = new Message();
			msg.what = SailHandler.SAIL_SUCCESS;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
