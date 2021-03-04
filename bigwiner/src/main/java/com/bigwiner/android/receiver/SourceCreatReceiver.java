package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.handler.SourceCreatHandler;
import com.bigwiner.android.handler.UserInfoHandler;
import com.bigwiner.android.view.activity.SourceCreatActivity;
import com.bigwiner.android.view.activity.UserInfoActivity;

import intersky.appbase.BaseReceiver;

public class SourceCreatReceiver extends BaseReceiver {

	private Handler mHandler;

	public SourceCreatReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(SourceCreatActivity.ACTION_AREA_SELECT);
		intentFilter.addAction(SourceCreatActivity.ACTION_TYPE_SELECT);
		intentFilter.addAction(SourceCreatActivity.ACTION_PORT_SELECT);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(UserInfoActivity.ACTION_AREA_SELECT)) {

			Message msg = new Message();
			msg.what = SourceCreatHandler.EVENT_SET_AREA;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(UserInfoActivity.ACTION_TYPE_SELECT)) {

			Message msg = new Message();
			msg.what = SourceCreatHandler.EVENT_SET_TYPE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(SourceCreatActivity.ACTION_PORT_SELECT)) {

			Message msg = new Message();
			msg.what = SourceCreatHandler.EVENT_SET_PORT;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
