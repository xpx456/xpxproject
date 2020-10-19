package com.dk.dktest.receive;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;


import com.dk.dktest.handler.MainHandler;

import intersky.appbase.BaseReceiver;
import xpx.bluetooth.BluetoothSetManager;

public class MainReceiver extends BaseReceiver {

	private Handler mHandler;

	public MainReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothSetManager.ACTION_ADD_DEVICE);
		intentFilter.addAction(BluetoothSetManager.ACTION_CLEAN_DEVICE);
		intentFilter.addAction(BluetoothSetManager.ACTION_DEVICE_FINISH);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(BluetoothSetManager.ACTION_ADD_DEVICE)) {

			Message msg = new Message();
			msg.what = MainHandler.ADD_DEVICE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(BluetoothSetManager.ACTION_CLEAN_DEVICE))
		{
			Message msg = new Message();
			msg.what = MainHandler.CLEAN_DEVICE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_FINISH))
		{
			Message msg = new Message();
			msg.what = MainHandler.UPDATA_DEVICE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
