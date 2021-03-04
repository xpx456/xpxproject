package com.dk.dkhome.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.dk.dkhome.handler.DeviceHandler;
import com.dk.dkhome.handler.DeviceHandler;
import com.dk.dkhome.utils.TestManager;
import com.dk.dkhome.view.DeviceView;
import com.dk.dkhome.view.ProgressView;

import intersky.appbase.BaseReceiver;
import intersky.scan.ScanUtils;
import xpx.bluetooth.BluetoothSetManager;

public class DeviceReceiver extends BaseReceiver {


	private Handler mHandler;

	public DeviceReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		intentFilter = new IntentFilter();
		intentFilter.addAction(DeviceView.ACTION_UPDTAT_DEIVECONNECT_IMF);
		intentFilter.addAction(TestManager.ACTION_CONNECT_TYPE_CHANGE);
		intentFilter.addAction(DeviceView.ACTION_UPDTAT_DEVICE_TYPE);
		intentFilter.addAction(BluetoothSetManager.ACTION_ADD_DEVICE);
		intentFilter.addAction(BluetoothSetManager.ACTION_DEVICE_FINISH);
		intentFilter.addAction(ScanUtils.ACTION_SCAN_FINISH);
		intentFilter.addAction(DeviceView.ACTION_UPDTAT_DEVICE_TYPE_NAME);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(DeviceView.ACTION_UPDTAT_DEIVECONNECT_IMF)){
			Message message = new Message();
			message.obj = intent;
			message.what = ProgressView.UPDTAT_PERSENT;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		else if(intent.getAction().equals(TestManager.ACTION_CONNECT_TYPE_CHANGE)){
			Message message = new Message();
			message.obj = intent;
			message.what = DeviceHandler.UPDTATA_DEVICE_STATE;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		else if(intent.getAction().equals(DeviceView.ACTION_UPDTAT_DEVICE_TYPE)){
			Message message = new Message();
			message.obj = intent;
			message.what = DeviceHandler.SET_SPORT_TYPE;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		else if(intent.getAction().equals(DeviceView.ACTION_UPDTAT_DEVICE_TYPE_NAME)){
			Message message = new Message();
			message.obj = intent;
			message.what = DeviceHandler.SET_SPORT_TYPE_NAME;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		else if(intent.getAction().equals(BluetoothSetManager.ACTION_ADD_DEVICE)){
			Message message = new Message();
			message.obj = intent;
			message.what = DeviceHandler.UPDATA_DEVICE_LIST;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		else if(intent.getAction().equals(BluetoothSetManager.ACTION_DEVICE_FINISH)){
			Message message = new Message();
			message.obj = intent;
			message.what = DeviceHandler.CONNECT_FINISH;
			if(mHandler != null)
			{
				mHandler.sendMessage(message);
			}
		}
		else if(intent.getAction().equals(ScanUtils.ACTION_SCAN_FINISH))
		{
			Message msg = new Message();
			msg.what = DeviceHandler.SCAN_FINISH;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
