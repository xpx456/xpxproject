package com.exhibition.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.exhibition.handler.LoginHandler;
import com.exhibition.view.ExhibitionApplication;

import intersky.appbase.BaseReceiver;

public class LoginReceiver extends BaseReceiver {

	private Handler mHandler;

	public LoginReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(ExhibitionApplication.ACTION_UPDATA_TIMEOUT);

	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(ExhibitionApplication.ACTION_UPDATA_TIMEOUT)) {

			Message msg = new Message();
			msg.what = LoginHandler.UPDATA_TIME;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
