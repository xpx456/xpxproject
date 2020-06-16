package intersky.attendance.receive;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;


import intersky.attendance.handler.AttendanceHandler;
import intersky.attendance.view.activity.AttendanceActivity;
import intersky.appbase.BaseReceiver;

public class AttendanceReceiver extends BaseReceiver {

	private Handler mHandler;

	public AttendanceReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(AttendanceActivity.ACTION_LEAVE_SET_CONTACTS);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(AttendanceActivity.ACTION_LEAVE_SET_CONTACTS)) {

			Message msg = new Message();
			msg.what = AttendanceHandler.EVENT_SET_USER;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
