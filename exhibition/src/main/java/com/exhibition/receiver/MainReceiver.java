package com.exhibition.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.exhibition.handler.LoginHandler;
import com.exhibition.handler.MainHandler;
import com.exhibition.view.ExhibitionApplication;
import com.finger.FingerManger;

import intersky.appbase.BaseReceiver;

public class MainReceiver extends BaseReceiver {

	private Handler mHandler;

	public MainReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(ExhibitionApplication.ACTION_SET_NAME);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(ExhibitionApplication.ACTION_SET_NAME)) {

			Message msg = new Message();
			msg.what = MainHandler.EVENT_SET_NAME;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
