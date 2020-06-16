package intersky.sign.receive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.sign.handler.StatisticsHandler;
import intersky.sign.view.activity.StatisticsActivity;


@SuppressLint("NewApi")
public class StatisticsReceiver extends BaseReceiver {

	private Handler mHandler;

	public StatisticsReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(StatisticsActivity.ACTION_SIGN_SET_CONTACTS);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(StatisticsActivity.ACTION_SIGN_SET_CONTACTS)) {

			Message msg = new Message();
			msg.what = StatisticsHandler.EVENT_SET_USER;
			msg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(msg);
		}
	}

}
