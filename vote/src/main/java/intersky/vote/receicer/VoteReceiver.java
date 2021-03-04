package intersky.vote.receicer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.vote.VoteManager;
import intersky.vote.handler.VoteHandler;


@SuppressLint("NewApi")
public class VoteReceiver extends BaseReceiver {

	public Handler mHandler;

	public VoteReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(VoteManager.ACTION_SET_VOTE_TYPE);
		intentFilter.addAction(VoteManager.ACTION_SET_VOTE_TYPE2);
		intentFilter.addAction(VoteManager.ACTION_SET_PIC);
		intentFilter.addAction(VoteManager.ACTION_SET_VOTER);
		intentFilter.addAction(VoteManager.ACTION_DELETE_PIC);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(VoteManager.ACTION_SET_VOTE_TYPE))
		{
			Message smsg = new Message();
			smsg.what = VoteHandler.EVENT_SET_VOTE_TYPE;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(VoteManager.ACTION_SET_VOTE_TYPE2))
		{
			Message smsg = new Message();
			smsg.what = VoteHandler.EVENT_SET_VOTE_TYPE2;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(VoteManager.ACTION_SET_PIC))
		{
			Message smsg = new Message();
			smsg.what = VoteHandler.EVENT_ADD_PHOTO;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(VoteManager.ACTION_DELETE_PIC))
		{
			Message smsg = new Message();
			smsg.what = VoteHandler.EVENT_DELETE_PIC;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(VoteManager.ACTION_SET_VOTER))
		{
			Message smsg = new Message();
			smsg.what = VoteHandler.EVENT_SET_VOTER;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
	}

}
