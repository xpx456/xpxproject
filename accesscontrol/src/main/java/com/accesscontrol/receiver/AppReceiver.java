package com.accesscontrol.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Message;

import com.accesscontrol.handler.AppHandler;
import com.accesscontrol.prase.MqttPrase;
import com.accesscontrol.service.MyMqttService;
import com.accesscontrol.view.AccessControlApplication;
import com.finger.FingerManger;

import intersky.appbase.BaseReceiver;

public class AppReceiver extends BaseReceiver {
	

	public AppReceiver()
	{
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(AccessControlApplication.ACTION_START_MQTT);
		intentFilter.addAction(MyMqttService.ACTION_SERVICE_STARTED);
		intentFilter.addAction(MyMqttService.ACTION_SERVICE_CONNECTED);
		intentFilter.addAction(MyMqttService.ACTION_SEND_MESSAGE);
		intentFilter.addAction(MqttPrase.ACTION_UPDATA_TIME);
		intentFilter.addAction(MqttPrase.ACTION_ADD_GUEST);
		intentFilter.addAction(MqttPrase.ACTION_DELETE_GUEST);
		intentFilter.addAction(MqttPrase.ACTION_UPDATA_GUEST);
		intentFilter.addAction(MqttPrase.ACTION_OPEN_DOOR);
		intentFilter.addAction(MqttPrase.ACTION_UPDATA_ALWAYS_OPEN);
		intentFilter.addAction(MqttPrase.ACTION_UPDATA_ALWAYS_CLOSE);
		intentFilter.addAction(MqttPrase.ACTION_SHOW_VIEW);
		intentFilter.addAction(MqttPrase.ACTION_ADD_MASTER);
		intentFilter.addAction(MqttPrase.ACTION_SET_PASSWORD);
		intentFilter.addAction(FingerManger.ACTION_USB_PERMISSION);
		intentFilter.addAction(FingerManger.ACTION_GET_LOGIN_FINGER_SUCCESS2);
		intentFilter.addAction(FingerManger.ACTION_GET_LOGIN_FINGER_FAIL2);
		intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		intentFilter.addAction(MqttPrase.ACTION_SET_DELY);

		intentFilter.addAction(MqttPrase.ACTION_LIVE);
		intentFilter.addAction(MqttPrase.ACTION_SET_DELY);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(AccessControlApplication.ACTION_START_MQTT)) {

			Message msg = new Message();
			msg.what = AppHandler.START_MQTT;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MyMqttService.ACTION_SERVICE_STARTED)) {

			Message msg = new Message();
			msg.what = AppHandler.INIT_MQTT;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MyMqttService.ACTION_SERVICE_CONNECTED)) {

			Message msg = new Message();
			msg.what = AppHandler.MQTT_CONNECTED;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MyMqttService.ACTION_SEND_MESSAGE)) {

			Message msg = new Message();
			msg.what = AppHandler.SEND_IMF;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_UPDATA_TIME)) {

			Message msg = new Message();
			msg.what = AppHandler.SET_TIME;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}

		else if (intent.getAction().equals(MqttPrase.ACTION_ADD_GUEST)) {

			Message msg = new Message();
			msg.what = AppHandler.ADD_GUEST;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_DELETE_GUEST)) {

			Message msg = new Message();
			msg.what = AppHandler.DELETE_GUEST;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_UPDATA_GUEST)) {

			Message msg = new Message();
			msg.what = AppHandler.UPDATA_GUEST;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(FingerManger.ACTION_GET_LOGIN_FINGER_FAIL2)) {

			Message msg = new Message();
			msg.what = AppHandler.FINGER_FAIL;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_OPEN_DOOR)) {

			Message msg = new Message();
			msg.what = AppHandler.OPEN_DOOR;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_UPDATA_ALWAYS_OPEN)) {

			Message msg = new Message();
			msg.what = AppHandler.ALWAYS_OPEN_DOOR;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_UPDATA_ALWAYS_CLOSE)) {

			Message msg = new Message();
			msg.what = AppHandler.ALWAYS_CLOSE_DOOR;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_SET_PASSWORD)) {

			Message msg = new Message();
			msg.what = AppHandler.SET_PASSOWRD;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_SHOW_VIEW)) {

			Message msg = new Message();
			msg.what = AppHandler.SHOW_VIEW;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_ADD_MASTER)) {

			Message msg = new Message();
			msg.what = AppHandler.ADD_MASTER;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_SET_DELY)) {

			Message msg = new Message();
			msg.what = AppHandler.SET_DELY;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_LIVE)) {

			Message msg = new Message();
			msg.what = AppHandler.BACK_LIVE;
			msg.obj = intent;
			if(AccessControlApplication.mApp.appHandler!=null)
				AccessControlApplication.mApp.appHandler.sendMessage(msg);
		}
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
