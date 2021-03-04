package com.dk.dkhome.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.dk.dkhome.handler.BigChartHandler;
import com.dk.dkhome.handler.DeviceHandler;
import com.dk.dkhome.utils.TestManager;
import com.dk.dkhome.view.DeviceView;
import com.dk.dkhome.view.ProgressView;

import intersky.appbase.BaseReceiver;
import intersky.scan.ScanUtils;
import xpx.bluetooth.BluetoothSetManager;

public class BigChartReceiver extends BaseReceiver {


	private Handler mHandler;
	public String action;

	public BigChartReceiver(Handler mHandler,String action)
	{
		this.mHandler = mHandler;
		this.action = action;
		intentFilter = new IntentFilter();
		intentFilter.addAction(action);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(action)){
			Message message = new Message();
			message.obj = intent;
			message.what = BigChartHandler.UP_DATA_VIEW;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
	}

}
