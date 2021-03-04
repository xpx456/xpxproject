package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.handler.ContactsListHandler;
import com.bigwiner.android.handler.SailHandler;
import com.bigwiner.android.view.activity.ContactsListActivity;
import com.bigwiner.android.view.activity.SailActivity;

import intersky.appbase.BaseReceiver;

public class ContactsListReceiver extends BaseReceiver {

	private Handler mHandler;

	public ContactsListReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(ContactsAsks.ACTION_UPDATA_CONTACTS);

	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(ContactsAsks.ACTION_UPDATA_CONTACTS)) {

			Message msg = new Message();
			msg.what = ContactsListHandler.UPDATA_FRIENDS_CHANGE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
