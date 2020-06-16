package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.handler.ContactsListHandler;
import com.bigwiner.android.handler.SourceAddListHandler;

import intersky.appbase.BaseReceiver;

public class SourceAddListReceiver extends BaseReceiver {

	private Handler mHandler;

	public SourceAddListReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(SourceAsks.ACIONT_SOURCE_EDIT);
		intentFilter.addAction(SourceAsks.ACIONT_SOURCE_CREAT);

	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(SourceAsks.ACIONT_SOURCE_EDIT)
				||intent.getAction().equals(SourceAsks.ACIONT_SOURCE_CREAT)) {

			Message msg = new Message();
			msg.what = SourceAddListHandler.UPDATA_SOURCE_LIST;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
