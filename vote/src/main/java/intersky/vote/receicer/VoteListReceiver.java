package intersky.vote.receicer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.vote.VoteManager;
import intersky.vote.handler.VoteListHandler;


@SuppressLint("NewApi")
public class VoteListReceiver extends BaseReceiver {

	public Handler mHandler;

	public VoteListReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(VoteManager.ACTION_VOTE_UPDATE);
		intentFilter.addAction(VoteManager.ACTION_VOTE_ADD);
		intentFilter.addAction(VoteManager.ACTION_VOTE_DELETE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(VoteManager.ACTION_VOTE_UPDATE)||intent.getAction().equals(VoteManager.ACTION_VOTE_ADD)
		||intent.getAction().equals(VoteManager.ACTION_VOTE_DELETE))
		{
			Message smsg = new Message();
			smsg.what = VoteListHandler.EVENT_VOTE_UPDATA;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
	}

}
