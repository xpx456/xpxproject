package com.restaurant.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.restaurant.handler.VideoHandler;
import com.restaurant.view.RestaurantApplication;

import intersky.appbase.BaseReceiver;

public class VideoReceiver extends BaseReceiver {

	private Handler mHandler;

	public VideoReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(RestaurantApplication.SHOW_ACCESS_SUCCESS_VIEW);
		intentFilter.addAction(RestaurantApplication.ACTION_UPDATA_VIDEO_VIEW);
		intentFilter.addAction(RestaurantApplication.ACTION_CLOSE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(RestaurantApplication.SHOW_ACCESS_SUCCESS_VIEW)) {

			Message msg = new Message();
			msg.what = VideoHandler.SHOW_SUCCESS_VIEW;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(RestaurantApplication.ACTION_UPDATA_VIDEO_VIEW))
		{
			Message msg = new Message();
			msg.what = VideoHandler.UPDATA_GALLY;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(RestaurantApplication.ACTION_CLOSE))
		{
			Message msg = new Message();
			msg.what = VideoHandler.CLOSE_VIDEO;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
