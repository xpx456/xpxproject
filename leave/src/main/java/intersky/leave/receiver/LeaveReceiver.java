package intersky.leave.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.leave.LeaveManager;
import intersky.leave.handler.LeaveHandler;


@SuppressLint("NewApi")
public class LeaveReceiver extends BaseReceiver {

	public Handler mHandler;

	public LeaveReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(LeaveManager.ACTION_SET_LEAVE_TYPE);
		intentFilter.addAction(LeaveManager.ACTION_LEAVE_ADDPICTORE);
		intentFilter.addAction(LeaveManager.ACTION_LEAVE_UPATE_SENDER);
		intentFilter.addAction(LeaveManager.ACTION_LEAVE_UPATE_COPYER);
		intentFilter.addAction(LeaveManager.ACTION_SET_LEAVE_CONTENT);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(LeaveManager.ACTION_SET_LEAVE_TYPE))
		{
			Message smsg = new Message();
			smsg.what = LeaveHandler.EVENT_SET_TYPE;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(LeaveManager.ACTION_LEAVE_ADDPICTORE))
		{
			Message smsg = new Message();
			smsg.what = LeaveHandler.EVENT_ADD_PIC;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(LeaveManager.ACTION_LEAVE_UPATE_SENDER))
		{
			Message smsg = new Message();
			smsg.what = LeaveHandler.EVENT_SET_SEND;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(LeaveManager.ACTION_LEAVE_UPATE_COPYER))
		{
			Message smsg = new Message();
			smsg.what = LeaveHandler.EVENT_SET_COPY;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if (intent.getAction().equals(LeaveManager.ACTION_SET_LEAVE_CONTENT)) {
			Message msg = new Message();
			msg.what = LeaveHandler.EVENT_LEAVE_SET_CONTENT;
			msg.obj = intent.getStringExtra("value");
			if (mHandler != null)
				mHandler.sendMessage(msg);
		}
	}

}
