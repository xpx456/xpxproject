package com.dk.dkphone.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.dk.dkphone.handler.MainHandler;
import com.dk.dkphone.view.activity.MainActivity;

import intersky.appbase.BaseReceiver;
import intersky.scan.ScanUtils;
import xpx.bluetooth.BluetoothSetManager;

public class MainReceiver extends BaseReceiver {

	public static final String ACTION_UPDTATA_OPTATION = "ACTION_UPDTATA_OPTATION";
	public static final String ACTION_UPDTATA_HEAD = "ACTION_UPDTATA_HEAD";

	private Handler mHandler;

	public MainReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		intentFilter = new IntentFilter();
//		this.intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);   //接受外媒挂载过滤器
//		this.intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);   //接受外媒挂载过滤器
		intentFilter.addAction(ACTION_UPDTATA_OPTATION);   //接受外媒挂载过滤器
		intentFilter.addAction(ACTION_UPDTATA_HEAD);   //接受外媒挂载过滤器
		intentFilter.addAction(BluetoothSetManager.ACTION_ADD_DEVICE);
		intentFilter.addAction(BluetoothSetManager.ACTION_CLEAN_DEVICE);
		intentFilter.addAction(BluetoothSetManager.ACTION_DEVICE_FINISH);
		intentFilter.addAction(MainActivity.BLUE_STATE);
		intentFilter.addAction(ScanUtils.ACTION_SCAN_FINISH);
//		this.intentFilter.addDataScheme("file");
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(ACTION_UPDTATA_OPTATION)){
			Message message = new Message();
			message.obj = intent;
			message.what = MainHandler.UPDATA_OP_VIEW;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		else if(intent.getAction().equals(ACTION_UPDTATA_HEAD)){
			Message message = new Message();
			message.obj = intent;
			message.what = MainHandler.UPDATA_HEAD_VIEW;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		else if (intent.getAction().equals(BluetoothSetManager.ACTION_ADD_DEVICE)) {

			Message msg = new Message();
			msg.what = MainHandler.ADD_DEVICE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(BluetoothSetManager.ACTION_CLEAN_DEVICE))
		{
			Message msg = new Message();
			msg.what = MainHandler.CLEAN_DEVICE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_FINISH))
		{
			Message msg = new Message();
			msg.what = MainHandler.UPDATA_DEVICE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(MainActivity.BLUE_STATE))
		{
			Message msg = new Message();
			msg.what = MainHandler.UPDATA_BLUETOOTH;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(ScanUtils.ACTION_SCAN_FINISH))
		{
			Message msg = new Message();
			msg.what = MainHandler.SCAN_FINISH;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
//		else if (intent.getAction().equals(GlobalVariable.READ_BLE_VERSION_ACTION)) {
//			String version = intent.getStringExtra(GlobalVariable.INTENT_BLE_VERSION_EXTRA);
//
//		} else if (intent.getAction().equals(GlobalVariable.READ_BATTERY_ACTION)) {
//			int battery = intent.getIntExtra(
//					GlobalVariable.INTENT_BLE_BATTERY_EXTRA, -1);
//		}

	}

}
