package com.exhibition.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.exhibition.handler.LoginHandler;
import com.exhibition.view.ExhibitionApplication;
import com.finger.FingerManger;

import intersky.appbase.BaseReceiver;

public class LoginReceiver extends BaseReceiver {

	private Handler mHandler;

	public LoginReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(ExhibitionApplication.ACTION_UPDATA_TIMEOUT);
		intentFilter.addAction(FingerManger.ACTION_GET_LOGIN_FINGER_SUCCESS);
		intentFilter.addAction(FingerManger.ACTION_GET_LOGIN_SUCCESS);
		intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);   //接受外媒挂载过滤器
		intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);   //接受外媒挂载过滤器
		intentFilter.addDataScheme("file");
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(ExhibitionApplication.ACTION_UPDATA_TIMEOUT)) {

			Message msg = new Message();
			msg.what = LoginHandler.UPDATA_TIME;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(FingerManger.ACTION_GET_LOGIN_FINGER_SUCCESS)) {

			Message msg = new Message();
			msg.what = LoginHandler.GET_FINGER_IMG_SUCCESS;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(FingerManger.ACTION_GET_LOGIN_SUCCESS)) {

			Message msg = new Message();
			msg.what = LoginHandler.GET_LOGIN_SUCCESS;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)){
			Message msg = new Message();
			msg.what = LoginHandler.INIT_DATA;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);

		}else if(intent.getAction().equals(Intent.ACTION_MEDIA_REMOVED)){

		}

	}

}
