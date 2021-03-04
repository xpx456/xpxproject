package intersky.leave.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.leave.LeaveManager;
import intersky.leave.handler.LeaveListHandler;
import intersky.leave.view.activity.LeaveListActivity;


@SuppressLint("NewApi")
public class LeaveListReceiver extends BaseReceiver {

	public Handler mHandler;

	public LeaveListReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(LeaveManager.ACTION_LEAVE_UPDATA_HIT);
		intentFilter.addAction(LeaveListActivity.ACTION_LEAVE_SET_CONTACTS);
		intentFilter.addAction(LeaveManager.ACTION_LEAVE_UPDATE);
		intentFilter.addAction(LeaveManager.ACTION_LEAVE_DELETE);
		intentFilter.addAction(LeaveManager.ACTION_LEAVE_ADD);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(LeaveManager.ACTION_LEAVE_UPDATA_HIT))
		{
			Message smsg = new Message();
			smsg.what = LeaveListHandler.EVENT_UPDATA_HIT;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else  if(intent.getAction().equals(LeaveListActivity.ACTION_LEAVE_SET_CONTACTS))
		{
			Message smsg = new Message();
			smsg.what = LeaveListHandler.EVENT_SET_USER;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(LeaveManager.ACTION_LEAVE_UPDATE)||
		intent.getAction().equals(LeaveManager.ACTION_LEAVE_DELETE)||intent.getAction().equals(LeaveManager.ACTION_LEAVE_ADD))
		{
			Message smsg = new Message();
			smsg.what = LeaveListHandler.EVENT_LEAVE_UPDATA;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
	}

}
