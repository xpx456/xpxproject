package intersky.workreport.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.workreport.WorkReportManager;
import intersky.workreport.handler.WorkReportHandler;


@SuppressLint("NewApi")
public class WorkReportReceiver extends BaseReceiver {

	public Handler mHandler;

	public WorkReportReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(WorkReportManager.ACTION_WORK_REPORT_ACTION_UPDATA_HIT);
		intentFilter.addAction(WorkReportManager.ACTION_WORK_REPORT_UPDATE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(WorkReportManager.ACTION_WORK_REPORT_ACTION_UPDATA_HIT))
		{
			Message smsg = new Message();
			smsg.what = WorkReportHandler.EVENT_UPDATA_HIT;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(WorkReportManager.ACTION_WORK_REPORT_UPDATE))
		{
			Message smsg = new Message();
			smsg.what = WorkReportHandler.EVENT_UPDATA_HIT;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
	}

}
