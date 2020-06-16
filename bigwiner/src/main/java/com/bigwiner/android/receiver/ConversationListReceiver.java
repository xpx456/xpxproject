package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.handler.ContactsListHandler;
import com.bigwiner.android.handler.ConversationListHandler;
import com.bigwiner.android.view.activity.MainActivity;

import intersky.appbase.BaseReceiver;
import intersky.appbase.entity.Conversation;

public class ConversationListReceiver extends BaseReceiver {

	private Handler mHandler;

	public ConversationListReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(MainActivity.ACTION_UPDATA_MESSAGE);

	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(MainActivity.ACTION_UPDATA_MESSAGE)) {

			Message msg = new Message();
			msg.what = ConversationListHandler.UPDATA_MESSAGE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
