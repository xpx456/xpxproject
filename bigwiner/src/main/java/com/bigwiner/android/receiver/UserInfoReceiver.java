package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.handler.MeetingDetiallHandler;
import com.bigwiner.android.handler.UserInfoHandler;
import com.bigwiner.android.view.activity.UserInfoActivity;

import intersky.appbase.BaseReceiver;

public class UserInfoReceiver extends BaseReceiver {

	private Handler mHandler;

	public UserInfoReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(UserInfoActivity.ACTION_AREA_SELECT);
		intentFilter.addAction(UserInfoActivity.ACTION_TYPE_SELECT);
		intentFilter.addAction(UserInfoActivity.ACTION_CITY_SELECT);
		intentFilter.addAction(UserInfoActivity.ACTION_SEX_SELECT);
		intentFilter.addAction(UserInfoActivity.ACTION_POSITION_SELECT);
		intentFilter.addAction(UserInfoActivity.ACTION_COMPANY_SELECT);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(UserInfoActivity.ACTION_AREA_SELECT)) {

			Message msg = new Message();
			msg.what = UserInfoHandler.EVENT_SET_AREA;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(UserInfoActivity.ACTION_TYPE_SELECT)) {

			Message msg = new Message();
			msg.what = UserInfoHandler.EVENT_SET_TYPE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(UserInfoActivity.ACTION_CITY_SELECT)) {

			Message msg = new Message();
			msg.what = UserInfoHandler.EVENT_SET_CIYT;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(UserInfoActivity.ACTION_SEX_SELECT)) {

			Message msg = new Message();
			msg.what = UserInfoHandler.EVENT_SET_SEX;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(UserInfoActivity.ACTION_POSITION_SELECT)) {

			Message msg = new Message();
			msg.what = UserInfoHandler.EVENT_SET_POSITION;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(UserInfoActivity.ACTION_COMPANY_SELECT)) {

			Message msg = new Message();
			msg.what = UserInfoHandler.EVENT_SET_COMPANY;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
