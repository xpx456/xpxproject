package intersky.leave.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.leave.LeaveManager;
import intersky.leave.handler.LeaveDetialHandler;


@SuppressLint("NewApi")
public class LeaveDetialReceiver extends BaseReceiver {

	public Handler mHandler;

	public LeaveDetialReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(LeaveManager.ACTION_LEAVE_UPDATE);
		intentFilter.addAction(LeaveManager.ACTION_LEAVE_DELETE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(LeaveManager.ACTION_LEAVE_UPDATE))
		{
			Message smsg = new Message();
			smsg.what = LeaveDetialHandler.EVENT_DETIAL_UPDATA;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(LeaveManager.ACTION_LEAVE_DELETE))
		{
			Message smsg = new Message();
			smsg.what = LeaveDetialHandler.EVENT_DETIAL_DELETE;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
	}

}
