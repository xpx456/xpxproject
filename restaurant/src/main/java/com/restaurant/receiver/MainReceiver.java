package com.restaurant.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;

import com.finger.FingerManger;
import com.iccard.IcCardManager;
import com.restaurant.handler.AppHandler;
import com.restaurant.handler.MainHandler;
import com.restaurant.prase.MqttPrase;
import com.restaurant.service.MyMqttService;
import com.restaurant.view.RestaurantApplication;
import com.restaurant.view.activity.MainActivity;

import intersky.appbase.BaseReceiver;

public class MainReceiver extends BaseReceiver {

	public Handler mHandler;

	public MainReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(FingerManger.ACTION_GET_LOGIN_FINGER_SUCCESS2);
		intentFilter.addAction(FingerManger.ACTION_GET_LOGIN_FINGER_FAIL2);
		intentFilter.addAction(RestaurantApplication.ACTION_UPDATA_SDCARD_DATA);
		intentFilter.addAction(RestaurantApplication.ACTION_UPDATA_SDCARD_DATA_FINISH);
		intentFilter.addAction(MainActivity.ACTION_UPDTATA_BTN);
		intentFilter.addAction(IcCardManager.ACTION_FIND_CARD_DEVICE);
		intentFilter.addAction(IcCardManager.ACTION_UN_FIND_CARD_DEVICE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 if (intent.getAction().equals(FingerManger.ACTION_GET_LOGIN_FINGER_SUCCESS2)) {

			Message msg = new Message();
			msg.what = AppHandler.FINGER_SUCCESS;
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
		 else if (intent.getAction().equals(MainActivity.ACTION_UPDTATA_BTN)) {

			 Message msg = new Message();
			 msg.what = MainHandler.UPDATA_BTN;
			 msg.obj = intent;
			 if(mHandler!=null)
				 mHandler.sendMessage(msg);
		 }
		 else if (intent.getAction().equals(IcCardManager.ACTION_FIND_CARD_DEVICE)) {

			 Message msg = new Message();
			 msg.what = MainHandler.FIND_CARD_DEVICE;
			 msg.obj = intent;
			 if(mHandler!=null)
				 mHandler.sendMessage(msg);
		 }
		 else if (intent.getAction().equals(IcCardManager.ACTION_UN_FIND_CARD_DEVICE)) {

			 Message msg = new Message();
			 msg.what = MainHandler.UN_FIND_CARD_DEVICE;
			 msg.obj = intent;
			 if(mHandler!=null)
				 mHandler.sendMessage(msg);
		 }
//		 else if (intent.getAction().equals(RestaurantApplication.ACTION_UPDATA_SDCARD_DATA)) {
//
//			 Message msg = new Message();
//			 msg.what = MainHandler.UPDATA_SDCARD;
//			 msg.obj = intent;
//			 if(RestaurantApplication.mApp.appHandler!=null)
//				 RestaurantApplication.mApp.appHandler.sendMessage(msg);
//		 }
//		 else if (intent.getAction().equals(RestaurantApplication.ACTION_UPDATA_SDCARD_DATA_FINISH)) {
//
//			 Message msg = new Message();
//			 msg.what = MainHandler.UPDATA_SDCARD_FINISH;
//			 msg.obj = intent;
//			 if(RestaurantApplication.mApp.appHandler!=null)
//				 RestaurantApplication.mApp.appHandler.sendMessage(msg);
//		 }

	}

}
