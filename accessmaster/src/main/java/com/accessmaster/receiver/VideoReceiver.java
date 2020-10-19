package com.accessmaster.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.accessmaster.handler.VideoHandler;
import com.accessmaster.view.AccessMasterApplication;

import intersky.appbase.BaseReceiver;

public class VideoReceiver extends BaseReceiver {

	private Handler mHandler;

	public VideoReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(AccessMasterApplication.SHOW_ACCESS_SUCCESS_VIEW);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(AccessMasterApplication.SHOW_ACCESS_SUCCESS_VIEW)) {

			Message msg = new Message();
			msg.what = VideoHandler.SHOW_SUCCESS_VIEW;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
