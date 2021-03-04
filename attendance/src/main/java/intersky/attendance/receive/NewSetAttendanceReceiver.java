package intersky.attendance.receive;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;


import intersky.attendance.handler.NewSetAttendanceHandler;
import intersky.attendance.view.activity.NewSetAttendanceActivity;
import intersky.appbase.BaseReceiver;

public class NewSetAttendanceReceiver extends BaseReceiver {

	private Handler mHandler;

	public NewSetAttendanceReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(NewSetAttendanceActivity.ACTION_SET_ATTENDANCE_CONTACTS);
		intentFilter.addAction(NewSetAttendanceActivity.ACTION_SET_ATTENDANCE_REMINDS);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(NewSetAttendanceActivity.ACTION_SET_ATTENDANCE_CONTACTS)) {

			Message msg = new Message();
			msg.what = NewSetAttendanceHandler.EVENT_SET_USER;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}else if(intent.getAction().equals(NewSetAttendanceActivity.ACTION_SET_ATTENDANCE_REMINDS))
		{
			Message msg = new Message();
			msg.what = NewSetAttendanceHandler.EVENT_SET_REMIND;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
