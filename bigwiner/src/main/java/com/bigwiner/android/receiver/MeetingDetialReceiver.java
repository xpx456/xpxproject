package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.handler.CompanyDetialHandler;
import com.bigwiner.android.handler.MainHandler;
import com.bigwiner.android.handler.MeetingDetiallHandler;
import com.bigwiner.android.view.activity.MainActivity;

import intersky.appbase.BaseReceiver;

public class MeetingDetialReceiver extends BaseReceiver {

	private Handler mHandler;

	public MeetingDetialReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(DetialAsks.ACTION_MEETING_JOIN_SUCCESS);
		intentFilter.addAction(DetialAsks.ACTION_MEETING_SIGN_SUCCESS);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(DetialAsks.ACTION_MEETING_JOIN_SUCCESS)) {

			Message msg = new Message();
			msg.what = MeetingDetiallHandler.JOIN_SUCCESS;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(DetialAsks.ACTION_MEETING_SIGN_SUCCESS)) {

			Message msg = new Message();
			msg.what = MeetingDetiallHandler.SIGN_SUCCESS;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
