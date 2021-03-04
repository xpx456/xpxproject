package intersky.notice.receicer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.notice.NoticeManager;
import intersky.notice.handler.NoticeDetialHandler;


@SuppressLint("NewApi")
public class NoticeDetialReceiver extends BaseReceiver {

	public Handler mHandler;

	public NoticeDetialReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(NoticeManager.ACTION_NOTICE_UPDATE);
		intentFilter.addAction(NoticeManager.ACTION_NOTICE_DELETE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 if(intent.getAction().equals(NoticeManager.ACTION_NOTICE_UPDATE))
		{
			Message smsg = new Message();
			smsg.what = NoticeDetialHandler.EVENT_DETIAL_UPDATA;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(NoticeManager.ACTION_NOTICE_DELETE))
		 {
			 Message smsg = new Message();
			 smsg.what = NoticeDetialHandler.EVENT_DETIAL_UPDATA;
			 smsg.obj = intent;
			 if(mHandler != null)
				 mHandler.sendMessage(smsg);
		 }
	}

}
