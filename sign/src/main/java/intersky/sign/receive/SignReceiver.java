package intersky.sign.receive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;


import intersky.appbase.BaseReceiver;
import intersky.sign.SignManager;
import intersky.sign.handler.SignHandler;


@SuppressLint("NewApi")
public class SignReceiver extends BaseReceiver {

	private Handler mHandler;
	
	public SignReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(SignManager.ACTION_AMPA_SET_ADDTESS);
		intentFilter.addAction(SignManager.ACTION_UPDATE_SIGN_COUNT);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(SignManager.ACTION_AMPA_SET_ADDTESS))
		{
			Message smsg = new Message();
			smsg.what = SignHandler.EVENT_SET_ADDRESS_SELECT;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if (intent.getAction().equals(SignManager.ACTION_UPDATE_SIGN_COUNT))
		{
			Message smsg = new Message();
			smsg.what = SignHandler.EVENT_UP_COUNT;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}

	}

}
