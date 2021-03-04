package com.dk.dkhome.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.dk.dkhome.handler.MainHandler;
import com.dk.dkhome.handler.NewPlanHandler;
import com.dk.dkhome.presenter.NewPlanPresenter;
import com.dk.dkhome.utils.FoodManager;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.activity.MainActivity;

import intersky.appbase.BaseReceiver;

public class MainReceiver extends BaseReceiver {


	private Handler mHandler;

	public MainReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		intentFilter = new IntentFilter();
		intentFilter.addAction(DkhomeApplication.ACTION_ADD_PLAN);
		intentFilter.addAction(DkhomeApplication.ACTION_UPDATA_PLAN);
		intentFilter.addAction(DkhomeApplication.ACTION_DELETE_PLAN);
		intentFilter.addAction(FoodManager.ACTION_UPDATA_FOOD);
		intentFilter.addAction(MainActivity.ACTION_UPDATA_DAIRY);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(DkhomeApplication.ACTION_UPDATA_PLAN)){
			Message message = new Message();
			message.obj = intent;
			message.what = MainHandler.UPDATA_PLAN;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		else if(intent.getAction().equals(DkhomeApplication.ACTION_ADD_PLAN)){
			Message message = new Message();
			message.obj = intent;
			message.what = MainHandler.ADD_PLAN;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		else if(intent.getAction().equals(DkhomeApplication.ACTION_DELETE_PLAN)){
			Message message = new Message();
			message.obj = intent;
			message.what = MainHandler.DELETE_PLAN;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		else if(intent.getAction().equals(FoodManager.ACTION_UPDATA_FOOD)){
			Message message = new Message();
			message.obj = intent;
			message.what = MainHandler.UPDATA_VIEW;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		else if(intent.getAction().equals(MainActivity.ACTION_UPDATA_DAIRY)){
			Message message = new Message();
			message.obj = intent;
			message.what = MainHandler.UPDATA_VIEW;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
	}

}
