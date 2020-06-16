package intersky.attendance.receive;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.attendance.AttendanceManager;
import intersky.attendance.handler.SetAttendanceHandler;
import intersky.appbase.BaseReceiver;

public class SetAttendanceReceiver extends BaseReceiver {

	private Handler mHandler;

	public SetAttendanceReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(AttendanceManager.ACTION_UPDATE_ATTANDENCE_SET_LIST);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(AttendanceManager.ACTION_UPDATE_ATTANDENCE_SET_LIST)) {

			Message msg = new Message();
			msg.what = SetAttendanceHandler.EVENT_SET_LIST_UP;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
