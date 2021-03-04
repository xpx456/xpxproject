package com.accessmaster.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.accessmaster.handler.MainHandler;
import com.accessmaster.prase.MqttPrase;
import com.accessmaster.view.AccessMasterApplication;
import com.accessmaster.view.activity.MainActivity;

import intersky.appbase.BaseReceiver;

public class MainReceiver extends BaseReceiver {

	private Handler mHandler;

	public MainReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(AccessMasterApplication.SHOW_ACCESS_SUCCESS_VIEW);
		intentFilter.addAction(MainActivity.ACTION_UPDATA_MAIN_GRIDE);
		intentFilter.addAction(MainActivity.ACTION_UPDTATA_BTN);
		intentFilter.addAction(MqttPrase.ACTION_DEVICE_LEAVE);
		intentFilter.addAction(MqttPrase.ACTION_EXIST);
		intentFilter.addAction(MqttPrase.ACTION_BUSY);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(AccessMasterApplication.SHOW_ACCESS_SUCCESS_VIEW)) {

			Message msg = new Message();
			msg.what = MainHandler.SHOW_SUCCESS_VIEW;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(MainActivity.ACTION_UPDATA_MAIN_GRIDE))
		{
			Message msg = new Message();
			msg.what = MainHandler.UPDATA_VIEW;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MainActivity.ACTION_UPDTATA_BTN)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_BTN;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_EXIST)) {

			Message msg = new Message();
			msg.what = MainHandler.EXIST;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_BUSY)) {

			Message msg = new Message();
			msg.what = MainHandler.BUSY;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
