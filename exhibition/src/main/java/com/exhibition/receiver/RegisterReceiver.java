package com.exhibition.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.exhibition.handler.MainHandler;
import com.exhibition.handler.RegisterHandler;
import com.exhibition.view.ExhibitionApplication;
import com.finger.FingerManger;

import intersky.appbase.BaseReceiver;

public class RegisterReceiver extends BaseReceiver {

	private Handler mHandler;

	public RegisterReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(FingerManger.ACTION_GET_FINGER_SUCCESS);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(FingerManger.ACTION_GET_FINGER_SUCCESS)) {

			Message msg = new Message();
			msg.what = RegisterHandler.EVENT_GET_FINGER;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
