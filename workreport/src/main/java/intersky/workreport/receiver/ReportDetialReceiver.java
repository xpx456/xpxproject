package intersky.workreport.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.workreport.WorkReportManager;
import intersky.workreport.handler.ReportDetialHandler;


@SuppressLint("NewApi")
public class ReportDetialReceiver extends BaseReceiver {

	public Handler mHandler;

	public ReportDetialReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(WorkReportManager.ACTION_WORK_REPORT_UPDATE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(WorkReportManager.ACTION_WORK_REPORT_UPDATE))
		{
			Message smsg = new Message();
			smsg.what = ReportDetialHandler.EVENT_DETIAL_UPDATA;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(WorkReportManager.ACTION_WORK_REPORT_DELETE))
		{
			Message smsg = new Message();
			smsg.what = ReportDetialHandler.EVENT_DETIAL_UPDATA;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}

	}

}
