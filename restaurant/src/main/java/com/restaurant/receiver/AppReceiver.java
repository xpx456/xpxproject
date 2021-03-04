package com.restaurant.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Message;

import com.finger.FingerManger;
import com.restaurant.handler.AppHandler;
import com.restaurant.prase.MqttPrase;
import com.restaurant.service.MyMqttService;
import com.restaurant.view.RestaurantApplication;

import intersky.appbase.BaseReceiver;

public class AppReceiver extends BaseReceiver {
	

	public AppReceiver()
	{
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(RestaurantApplication.ACTION_START_CREAT_MQTT);
		intentFilter.addAction(MyMqttService.ACTION_SERVICE_STARTED);
		intentFilter.addAction(MqttPrase.ACTION_UPDATA_TIME);
		intentFilter.addAction(MqttPrase.ACTION_ADD_GUEST);
		intentFilter.addAction(MqttPrase.ACTION_DELETE_GUEST);
		intentFilter.addAction(MqttPrase.ACTION_UPDATA_GUEST);
		intentFilter.addAction(MqttPrase.ACTION_CLEAN_GUEST);
		intentFilter.addAction(MqttPrase.ACTION_OPEN_DOOR);
		intentFilter.addAction(MqttPrase.ACTION_UPDATA_ALWAYS_OPEN);
		intentFilter.addAction(MqttPrase.ACTION_UPDATA_ALWAYS_CLOSE);
		intentFilter.addAction(MqttPrase.ACTION_SET_PASSWORD);
		intentFilter.addAction(FingerManger.ACTION_USB_PERMISSION);
		intentFilter.addAction(FingerManger.ACTION_GET_LOGIN_FINGER_SUCCESS2);
		intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		intentFilter.addAction(MqttPrase.ACTION_SET_DELY);
		intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
//		intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);   //接受外媒挂载过滤器
//		intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);   //接受外媒挂载过滤器
//		intentFilter.addDataScheme("file");
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(RestaurantApplication.ACTION_START_CREAT_MQTT)) {

			Message msg = new Message();
			msg.what = AppHandler.START_MQTT;
			msg.obj = intent;
			if(RestaurantApplication.mApp.appHandler!=null)
				RestaurantApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MyMqttService.ACTION_SERVICE_STARTED)) {

			Message msg = new Message();
			msg.what = AppHandler.INIT_MQTT;
			msg.obj = intent;
			if(RestaurantApplication.mApp.appHandler!=null)
				RestaurantApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_UPDATA_TIME)) {

			Message msg = new Message();
			msg.what = AppHandler.SET_TIME;
			msg.obj = intent;
			if(RestaurantApplication.mApp.appHandler!=null)
				RestaurantApplication.mApp.appHandler.sendMessage(msg);
		}

		else if (intent.getAction().equals(MqttPrase.ACTION_ADD_GUEST)) {

			Message msg = new Message();
			msg.what = AppHandler.ADD_GUEST;
			msg.obj = intent;
			if(RestaurantApplication.mApp.appHandler!=null)
				RestaurantApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_DELETE_GUEST)) {

			Message msg = new Message();
			msg.what = AppHandler.DELETE_GUEST;
			msg.obj = intent;
			if(RestaurantApplication.mApp.appHandler!=null)
				RestaurantApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_UPDATA_GUEST)) {

			Message msg = new Message();
			msg.what = AppHandler.UPDATA_GUEST;
			msg.obj = intent;
			if(RestaurantApplication.mApp.appHandler!=null)
				RestaurantApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_SET_DELY)) {

			Message msg = new Message();
			msg.what = AppHandler.SET_DELY;
			msg.obj = intent;
			if(RestaurantApplication.mApp.appHandler!=null)
				RestaurantApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_SET_PASSWORD)) {

			Message msg = new Message();
			msg.what = AppHandler.SET_PASSOWRD;
			msg.obj = intent;
			if(RestaurantApplication.mApp.appHandler!=null)
				RestaurantApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_CLEAN_GUEST)) {

			Message msg = new Message();
			msg.what = AppHandler.CLEAN_GUEST;
			msg.obj = intent;
			if(RestaurantApplication.mApp.appHandler!=null)
				RestaurantApplication.mApp.appHandler.sendMessage(msg);
		}
//		else if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
//
//			Message msg = new Message();
//			msg.what = AppHandler.SDCARD_CONNECT;
//			msg.obj = intent;
//			if(RestaurantApplication.mApp.appHandler!=null)
//				RestaurantApplication.mApp.appHandler.sendMessage(msg);
//		}
//		else if (intent.getAction().equals(Intent.ACTION_MEDIA_REMOVED)) {
//
//			Message msg = new Message();
//			msg.what = AppHandler.SDCARD_REMOVE;
//			msg.obj = intent;
//			if(RestaurantApplication.mApp.appHandler!=null)
//				RestaurantApplication.mApp.appHandler.sendMessage(msg);
//		}
		else if (FingerManger.ACTION_USB_PERMISSION.equals(intent.getAction())) {
			synchronized (this) {
//				mContext.unregisterReceiver(mUsbReceiver);
				if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
					//TODO 授权成功，操作USB设备
					int a;
				}else{
					//用户点击拒绝了
				}
			}
		}
	}

}
