package intersky.sign.receive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.sign.handler.SignDetialHandler;
import intersky.sign.view.activity.SignDetialActivity;


@SuppressLint("NewApi")
public class SignDetialReceiver extends BaseReceiver {

	private Handler mHandler;

	public SignDetialReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(SignDetialActivity.ACTION_SIGN_ADDPICTORE);
		intentFilter.addAction(SignDetialActivity.ACTION_SIGN_DELETEPICTORE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(SignDetialActivity.ACTION_SIGN_ADDPICTORE))
		{
			Message smsg = new Message();
			smsg.obj = intent;
			smsg.what = SignDetialHandler.EVENT_ADD_PIC;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if (intent.getAction().equals(SignDetialActivity.ACTION_SIGN_DELETEPICTORE)) {

			Message msg = new Message();
			msg.what = SignDetialHandler.EVENT_DELETE_PIC;
			msg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(msg);
		}
	}

}
