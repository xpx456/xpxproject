package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.handler.ContactsListHandler;
import com.bigwiner.android.handler.SourceCollectListHandler;
import com.bigwiner.android.handler.SourceSelectHandler;

import intersky.appbase.BaseReceiver;

public class SourceCollectListReceiver extends BaseReceiver {

	private Handler mHandler;

	public SourceCollectListReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(SourceAsks.ACIONT_SOURCE_EDIT);

	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(SourceAsks.ACIONT_SOURCE_EDIT)) {

			Message msg = new Message();
			msg.what = SourceCollectListHandler.SOURCE_LIST_UPDATA;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
