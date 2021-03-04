package com.accessmaster.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Message;

import com.accessmaster.handler.AppHandler;
import com.accessmaster.handler.MainHandler;
import com.accessmaster.prase.MqttPrase;
import com.accessmaster.service.MyMqttService;
import com.accessmaster.view.AccessMasterApplication;

import intersky.appbase.BaseReceiver;

public class AppReceiver extends BaseReceiver {
	

	public AppReceiver()
	{
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(AccessMasterApplication.ACTION_START_MQTT);
		intentFilter.addAction(MyMqttService.ACTION_SERVICE_STARTED);
		intentFilter.addAction(MyMqttService.ACTION_SERVICE_CONNECTED);
		intentFilter.addAction(MqttPrase.ACTION_UPDATA_TIME);
		intentFilter.addAction(MqttPrase.ACTION_ADD_GUEST);
		intentFilter.addAction(MqttPrase.ACTION_DELETE_GUEST);
		intentFilter.addAction(MqttPrase.ACTION_UPDATA_GUEST);
		intentFilter.addAction(MqttPrase.ACTION_OPEN_DOOR);
		intentFilter.addAction(MqttPrase.ACTION_UPDATA_ALWAYS_OPEN);
		intentFilter.addAction(MqttPrase.ACTION_UPDATA_ALWAYS_CLOSE);
		intentFilter.addAction(MqttPrase.ACTION_OPEN_CONTACT);
		intentFilter.addAction(MqttPrase.ACTION_ADD_DEVICE);
		intentFilter.addAction(MqttPrase.ACTION_SENT_MATER);
		intentFilter.addAction(MqttPrase.ACTION_SET_PASSWORD);
		intentFilter.addAction(MqttPrase.ACTION_DEVICE_LEAVE);
		intentFilter.addAction(MqttPrase.ACTION_SET_DELY);
		intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		intentFilter.addAction(MqttPrase.ACTION_LIVE_BACK);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(AccessMasterApplication.ACTION_START_MQTT)) {

			Message msg = new Message();
			msg.what = AppHandler.START_MQTT;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MyMqttService.ACTION_SERVICE_STARTED)) {

			Message msg = new Message();
			msg.what = AppHandler.INIT_MQTT;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(MyMqttService.ACTION_SERVICE_CONNECTED))
		{
			Message msg = new Message();
			msg.what = AppHandler.MQTT_CONNECTED;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(MqttPrase.ACTION_ADD_DEVICE))
		{
			Message msg = new Message();
			msg.what = AppHandler.ADD_DEVICE;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_UPDATA_TIME)) {

			Message msg = new Message();
			msg.what = AppHandler.SET_TIME;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}

		else if (intent.getAction().equals(MqttPrase.ACTION_ADD_GUEST)) {

			Message msg = new Message();
			msg.what = AppHandler.ADD_GUEST;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_DELETE_GUEST)) {

			Message msg = new Message();
			msg.what = AppHandler.DELETE_GUEST;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_UPDATA_GUEST)) {

			Message msg = new Message();
			msg.what = AppHandler.UPDATA_GUEST;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}

		else if (intent.getAction().equals(MqttPrase.ACTION_OPEN_DOOR)) {

			Message msg = new Message();
			msg.what = AppHandler.OPEN_DOOR;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_UPDATA_ALWAYS_OPEN)) {

			Message msg = new Message();
			msg.what = AppHandler.ALWAYS_OPEN_DOOR;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_SET_PASSWORD)) {

			Message msg = new Message();
			msg.what = AppHandler.SET_PASSOWRD;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_UPDATA_ALWAYS_CLOSE)) {

			Message msg = new Message();
			msg.what = AppHandler.ALWAYS_CLOSE_DOOR;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(MqttPrase.ACTION_OPEN_CONTACT))
		{
			Message msg = new Message();
			msg.what = AppHandler.OPEN_CONTACT;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(MqttPrase.ACTION_SENT_MATER))
		{
			Message msg = new Message();
			msg.what = AppHandler.SEND_MASTER;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_SET_DELY)) {

			Message msg = new Message();
			msg.what = AppHandler.SET_DELY;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_DEVICE_LEAVE)) {

			Message msg = new Message();
			msg.what = AppHandler.REMOVE_DEVICE;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MqttPrase.ACTION_LIVE_BACK)) {

			Message msg = new Message();
			msg.what = AppHandler.LIVE_BACK;
			msg.obj = intent;
			if(AccessMasterApplication.mApp.appHandler!=null)
				AccessMasterApplication.mApp.appHandler.sendMessage(msg);
		}
	}

}
