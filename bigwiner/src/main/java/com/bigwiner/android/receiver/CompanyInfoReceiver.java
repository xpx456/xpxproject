package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.handler.CompanyInfoHandler;
import com.bigwiner.android.handler.UserInfoHandler;
import com.bigwiner.android.view.activity.CompanyInfoActivity;
import com.bigwiner.android.view.activity.UserInfoActivity;

import intersky.appbase.BaseReceiver;

public class CompanyInfoReceiver extends BaseReceiver {

	private Handler mHandler;

	public CompanyInfoReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(CompanyInfoActivity.ACTION_PROVIENCE_SELECT);
		intentFilter.addAction(CompanyInfoActivity.ACTION_COMPANY_CITY_SELECT);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(CompanyInfoActivity.ACTION_PROVIENCE_SELECT)) {

			Message msg = new Message();
			msg.what = CompanyInfoHandler.EVENT_SET_AREA;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(CompanyInfoActivity.ACTION_COMPANY_CITY_SELECT)) {

			Message msg = new Message();
			msg.what = CompanyInfoHandler.EVENT_SET_CIYT;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
