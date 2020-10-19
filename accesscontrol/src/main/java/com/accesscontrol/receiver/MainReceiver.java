package com.accesscontrol.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.accesscontrol.handler.AppHandler;
import com.accesscontrol.handler.MainHandler;
import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.activity.MainActivity;
import com.finger.FingerManger;

import intersky.appbase.BaseReceiver;

public class MainReceiver extends BaseReceiver {

	private Handler mHandler;

	public MainReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(AccessControlApplication.SHOW_ACCESS_SUCCESS_VIEW);
		intentFilter.addAction(FingerManger.ACTION_GET_LOGIN_FINGER_FAIL2);
		intentFilter.addAction(MainActivity.ACTION_UPDTATA_BTN);
		intentFilter.addAction(FingerManger.ACTION_GET_LOGIN_FINGER_SUCCESS2);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(AccessControlApplication.SHOW_ACCESS_SUCCESS_VIEW)) {

			Message msg = new Message();
			msg.what = MainHandler.SHOW_SUCCESS_VIEW;
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
		else if (intent.getAction().equals(FingerManger.ACTION_GET_LOGIN_FINGER_SUCCESS2)) {

			Message msg = new Message();
			msg.what = MainHandler.FINGER_SUCCESS;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(FingerManger.ACTION_GET_LOGIN_FINGER_FAIL2)) {

			Message msg = new Message();
			msg.what = MainHandler.FINGER_FAIL;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}

	}

}
