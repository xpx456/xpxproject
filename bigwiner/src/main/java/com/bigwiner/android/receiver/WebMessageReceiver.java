package com.bigwiner.android.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.view.activity.WebMessageActivity;

import intersky.appbase.BaseReceiver;


@SuppressLint("NewApi")
public class WebMessageReceiver extends BaseReceiver {

	public static final String GET_PICTURE_PATH= "get_picture_path";

	public Handler mHandler;

	public WebMessageReceiver(Handler mHandler)
	{

		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(GET_PICTURE_PATH);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(GET_PICTURE_PATH))
		{
			Message msg = new Message();
			msg.what = WebMessageActivity.GET_PIC_PATH;
			msg.obj = intent.getStringExtra("path");
			if(mHandler != null)
				mHandler.sendMessage(msg);
		}
	}

}
