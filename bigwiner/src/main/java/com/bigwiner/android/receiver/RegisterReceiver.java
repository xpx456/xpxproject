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

public class RegisterReceiver extends BaseReceiver {

	private Handler mHandler;

	public RegisterReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(LoginActivity.ACTION_REGIST_SUCCESS);
		intentFilter.addAction(RegisterActivity.ACTION_L_AREA_SELECT);
		intentFilter.addAction(RegisterActivity.ACTION_L_TYPE_SELECT);
		intentFilter.addAction(RegisterActivity.ACTION_L_CITY_SELECT);
		intentFilter.addAction(RegisterActivity.ACTION_AREA);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(LoginActivity.ACTION_REGIST_SUCCESS)) {

			Message msg = new Message();
			msg.what = RegisterHandler.EVENT_REGISTER_SUCCESS;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(RegisterActivity.ACTION_L_AREA_SELECT)) {

			Message msg = new Message();
			msg.what = RegisterHandler.EVENT_SET_AREA;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(RegisterActivity.ACTION_L_TYPE_SELECT)) {

			Message msg = new Message();
			msg.what = RegisterHandler.EVENT_SET_TYPE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(RegisterActivity.ACTION_L_CITY_SELECT)) {

			Message msg = new Message();
			msg.what = RegisterHandler.EVENT_SET_CIYT;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(RegisterActivity.ACTION_AREA)) {

			Message msg = new Message();
			msg.what = RegisterHandler.EVENT_SET_AREA_CODE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
