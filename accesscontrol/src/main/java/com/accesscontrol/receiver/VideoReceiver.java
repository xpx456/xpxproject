package com.accesscontrol.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.accesscontrol.handler.VideoHandler;
import com.accesscontrol.view.AccessControlApplication;

import intersky.appbase.BaseReceiver;

public class VideoReceiver extends BaseReceiver {

	private Handler mHandler;

	public VideoReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(AccessControlApplication.SHOW_ACCESS_SUCCESS_VIEW);
		intentFilter.addAction(AccessControlApplication.ACTION_CLOSE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(AccessControlApplication.SHOW_ACCESS_SUCCESS_VIEW)) {

			Message msg = new Message();
			msg.what = VideoHandler.SHOW_SUCCESS_VIEW;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(AccessControlApplication.ACTION_UPDATA_VIDEO_VIEW))
		{
			Message msg = new Message();
			msg.what = VideoHandler.UPDATA_GALLY;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(AccessControlApplication.ACTION_CLOSE))
		{
			Message msg = new Message();
			msg.what = VideoHandler.CLOSE_VIDEO;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
