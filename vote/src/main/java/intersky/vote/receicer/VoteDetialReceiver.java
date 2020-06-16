package intersky.vote.receicer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.vote.VoteManager;
import intersky.vote.handler.VoteDetialHandler;


@SuppressLint("NewApi")
public class VoteDetialReceiver extends BaseReceiver {

	public Handler mHandler;

	public VoteDetialReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(VoteManager.ACTION_VOTE_UPDATE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(VoteManager.ACTION_VOTE_UPDATE))
		{
			Message smsg = new Message();
			smsg.what = VoteDetialHandler.EVENT_VOTE_UPDATA;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(VoteManager.ACTION_VOTE_DELETE))
		{
			Message smsg = new Message();
			smsg.what = VoteDetialHandler.EVENT_VOTE_UPDATA;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
	}

}
