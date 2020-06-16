package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.handler.AttdenceHandler;
import com.bigwiner.android.handler.UserInfoHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.AttdenceActivity;

import intersky.appbase.BaseReceiver;

public class AttdenceReceiver extends BaseReceiver {

	private Handler mHandler;

	public AttdenceReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(AttdenceActivity.ACTION_UPDATA_SOURCE_SELCET);

	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(AttdenceActivity.ACTION_UPDATA_SOURCE_SELCET)) {

			Message msg = new Message();
			msg.what = AttdenceHandler.UPDTATA_SOURCE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
