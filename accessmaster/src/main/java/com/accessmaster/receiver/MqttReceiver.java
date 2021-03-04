package com.accessmaster.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.accessmaster.handler.MqttHandler;
import com.accessmaster.service.MyMqttService;

import intersky.appbase.BaseReceiver;

public class MqttReceiver extends BaseReceiver {

	private Handler mHandler;

	public MqttReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(MyMqttService.ACTION_INIT_MQTT);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(MyMqttService.ACTION_INIT_MQTT)) {

			Message msg = new Message();
			msg.what = MqttHandler.START_SERVICE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}


	}

}
