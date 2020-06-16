package intersky.workreport.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.workreport.WorkReportManager;
import intersky.workreport.handler.ReportListHandler;


@SuppressLint("NewApi")
public class ReportListReceiver extends BaseReceiver {

	public Handler mHandler;

	public ReportListReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(WorkReportManager.ACTION_WORK_REPORT_ACTION_UPDATA_HIT);
		intentFilter.addAction(WorkReportManager.ACTION_WORK_REPORT_UPDATE);
		intentFilter.addAction(WorkReportManager.ACTION_WORK_REPORT_CONVERSATION_UPDATE);
		intentFilter.addAction(WorkReportManager.ACTION_WORK_REPORT_DELETE);
		intentFilter.addAction(WorkReportManager.ACTION_WORK_REPORT_ADD);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(WorkReportManager.ACTION_WORK_REPORT_CONVERSATION_UPDATE))
		{
			Message smsg = new Message();
			smsg.what = ReportListHandler.EVENT_REPORT_CONVERSATION_UPDATA;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(WorkReportManager.ACTION_WORK_REPORT_UPDATE)||intent.getAction().equals(WorkReportManager.ACTION_WORK_REPORT_DELETE)
		||intent.getAction().equals(WorkReportManager.ACTION_WORK_REPORT_ADD))
		{
			Message smsg = new Message();
			smsg.what = ReportListHandler.EVENT_REPORT_UPDATA;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(WorkReportManager.ACTION_WORK_REPORT_ACTION_UPDATA_HIT))
		{
			Message smsg = new Message();
			smsg.what = ReportListHandler.EVENT_GET_HIT_SUCCESS;
			smsg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(smsg);
		}
	}

}
