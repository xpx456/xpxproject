package com.dk.dkphone.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.dk.dkphone.entity.UserDefine;
import com.dk.dkphone.handler.MainHandler;
import com.dk.dkphone.handler.VideoHandler;
import com.dk.dkphone.view.activity.MainActivity;

import intersky.appbase.BaseReceiver;
import intersky.scan.ScanUtils;
import xpx.bluetooth.BluetoothSetManager;

public class VideoReceiver extends BaseReceiver {


	private Handler mHandler;

	public VideoReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		intentFilter = new IntentFilter();
		intentFilter.addAction(UserDefine.ACTION_LOGO_MOOD);   //接受外媒挂载过滤器\
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(UserDefine.ACTION_LOGO_MOOD)){
			Message message = new Message();
			message.obj = intent;
			message.what = VideoHandler.UPDATA_LOGO;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
	}

}
