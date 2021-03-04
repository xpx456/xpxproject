package intersky.workreport.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.workreport.WorkReportManager;
import intersky.workreport.handler.NewReportHandler;


@SuppressLint("NewApi")
public class NewReportReceiver extends BaseReceiver {

	public Handler mHandler;

	public NewReportReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(WorkReportManager.ACTION_REPORT_UPATE_SENDER);
		intentFilter.addAction(WorkReportManager.ACTION_REPORT_UPATE_COPYER);
		intentFilter.addAction(WorkReportManager.ACTION_REPORT_ADDPICTORE);
		intentFilter.addAction(WorkReportManager.ACTION_SET_WORK_CONTENT5);
		intentFilter.addAction(WorkReportManager.ACTION_SET_WORK_CONTENT1);
		intentFilter.addAction(WorkReportManager.ACTION_SET_WORK_CONTENT2);
		intentFilter.addAction(WorkReportManager.ACTION_SET_WORK_CONTENT3);
		intentFilter.addAction(WorkReportManager.ACTION_SET_WORK_CONTENT4);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 if (intent.getAction().equals(WorkReportManager.ACTION_REPORT_ADDPICTORE)) {

			Message msg = new Message();
			msg.what = NewReportHandler.EVENT_ADD_PIC;
			msg.obj = intent;
			if(mHandler != null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(WorkReportManager.ACTION_REPORT_UPATE_SENDER))
		{
			Message msg = new Message();
			msg.what = NewReportHandler.EVENT_SET_SEND;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(WorkReportManager.ACTION_REPORT_UPATE_COPYER))
		{
			Message msg = new Message();
			msg.what = NewReportHandler.EVENT_SET_COPY;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		 else if(intent.getAction().equals(WorkReportManager.ACTION_SET_WORK_CONTENT5))
		 {
			 Message msg = new Message();
			 msg.obj = intent.getStringExtra("value");
			 msg.what = NewReportHandler.EVENT_WORK_SET_CONTENT5;
			 if(mHandler!=null)
				 mHandler.sendMessage(msg);
		 }
		 else if(intent.getAction().equals(WorkReportManager.ACTION_SET_WORK_CONTENT1))
		 {
			 Message msg = new Message();
			 msg.obj = intent.getStringExtra("value");
			 msg.what = NewReportHandler.EVENT_WORK_SET_CONTENT1;
			 if(mHandler!=null)
				 mHandler.sendMessage(msg);
		 }
		 else if(intent.getAction().equals(WorkReportManager.ACTION_SET_WORK_CONTENT2))
		 {
			 Message msg = new Message();
			 msg.obj = intent.getStringExtra("value");
			 msg.what = NewReportHandler.EVENT_WORK_SET_CONTENT2;
			 if(mHandler!=null)
				 mHandler.sendMessage(msg);
		 }
		 else if(intent.getAction().equals(WorkReportManager.ACTION_SET_WORK_CONTENT3))
		 {
			 Message msg = new Message();
			 msg.obj = intent.getStringExtra("value");
			 msg.what = NewReportHandler.EVENT_WORK_SET_CONTENT3;
			 if(mHandler!=null)
				 mHandler.sendMessage(msg);
		 }
		 else if(intent.getAction().equals(WorkReportManager.ACTION_SET_WORK_CONTENT4))
		 {
			 Message msg = new Message();
			 msg.obj = intent.getStringExtra("value");
			 msg.what = NewReportHandler.EVENT_WORK_SET_CONTENT4;
			 if(mHandler!=null)
				 mHandler.sendMessage(msg);
		 }
	}

}
