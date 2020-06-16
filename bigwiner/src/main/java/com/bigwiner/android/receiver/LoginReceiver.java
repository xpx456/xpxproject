package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.handler.LoginHandler;
import com.bigwiner.android.handler.RegisterHandler;
import com.bigwiner.android.handler.UserInfoHandler;
import com.bigwiner.android.view.activity.LoginActivity;
import com.bigwiner.android.view.activity.RegisterActivity;
import com.bigwiner.android.view.activity.UserInfoActivity;

import intersky.appbase.BaseReceiver;

public class LoginReceiver extends BaseReceiver {

	private Handler mHandler;

	public LoginReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(LoginActivity.ACTION_REGIST_SUCCESS);
		intentFilter.addAction(LoginActivity.ACTION_CHANGE_SUCCESS);
		intentFilter.addAction(RegisterActivity.ACTION_AREA);
	}
	
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(LoginActivity.ACTION_REGIST_SUCCESS)) {

			Message msg = new Message();
			msg.what = LoginHandler.EVENT_START_MAIN;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(LoginActivity.ACTION_CHANGE_SUCCESS)) {

			Message msg = new Message();
			msg.what = LoginHandler.EVENT_START_RELOGIN;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(RegisterActivity.ACTION_AREA)) {

			Message msg = new Message();
			msg.what = LoginHandler.EVENT_SET_AREA_CODE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
