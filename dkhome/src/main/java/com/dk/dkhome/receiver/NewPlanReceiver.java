package com.dk.dkhome.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;


import com.dk.dkhome.handler.NewPlanHandler;
import com.dk.dkhome.presenter.NewPlanPresenter;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.activity.NewPlanActivity;

import intersky.appbase.BaseReceiver;
import xpx.bluetooth.BluetoothSetManager;

public class NewPlanReceiver extends BaseReceiver {


	private Handler mHandler;

	public NewPlanReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		intentFilter = new IntentFilter();
		intentFilter.addAction(NewPlanPresenter.ACTION_SET_VIDEO);
		intentFilter.addAction(DkhomeApplication.ACTION_ADD_PLAN);
		intentFilter.addAction(DkhomeApplication.ACTION_UPDATA_PLAN);

	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(NewPlanPresenter.ACTION_SET_VIDEO)){
			Message message = new Message();
			message.obj = intent;
			message.what = NewPlanHandler.SET_VIDEO;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
	}

}
