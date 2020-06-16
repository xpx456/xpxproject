package intersky.workreport.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.apputils.TimeUtils;
import intersky.appbase.entity.Contacts;
import intersky.workreport.R;
import intersky.workreport.WorkReportManager;
import intersky.workreport.entity.Report;
import intersky.workreport.handler.WorkReportHandler;
import intersky.workreport.receiver.WorkReportReceiver;
import intersky.workreport.view.activity.NewReportActivity;
import intersky.workreport.view.activity.ReportListActivity;
import intersky.workreport.view.activity.WorkReportActivity;
import xpx.com.toolbar.utils.ToolBarHelper;


public class WorkReportPresenter implements Presenter {

	public WorkReportActivity mWorkReportActivity;
	public WorkReportHandler mWorkReportHandler;

	public WorkReportPresenter(WorkReportActivity mWorkReportActivity) {
		this.mWorkReportActivity = mWorkReportActivity;
		mWorkReportHandler = new WorkReportHandler(mWorkReportActivity);
		mWorkReportActivity.setBaseReceiver( new WorkReportReceiver(mWorkReportHandler));
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mWorkReportActivity.setContentView(R.layout.activity_work_report);
		ToolBarHelper.setTitle(mWorkReportActivity.mActionBar,mWorkReportActivity.getString(R.string.xml_workreport));
		mWorkReportActivity.btnReceive = (RelativeLayout) mWorkReportActivity.findViewById(R.id.receive);
		mWorkReportActivity.btnSend = (RelativeLayout) mWorkReportActivity.findViewById(R.id.send);
		mWorkReportActivity.btnJoinin = (RelativeLayout) mWorkReportActivity.findViewById(R.id.joinin);
		mWorkReportActivity.btnDay = (RelativeLayout) mWorkReportActivity.findViewById(R.id.day);
		mWorkReportActivity.btnWeek = (RelativeLayout) mWorkReportActivity.findViewById(R.id.week);
		mWorkReportActivity.btnMonth = (RelativeLayout) mWorkReportActivity.findViewById(R.id.month);
		mWorkReportActivity.btnReceive.setOnClickListener(mWorkReportActivity.myReceiveClicklintener);
		mWorkReportActivity.btnSend.setOnClickListener(mWorkReportActivity.mySendClicklintener);
		mWorkReportActivity.btnJoinin.setOnClickListener(mWorkReportActivity.myJoininClicklintener);
		mWorkReportActivity.btnDay.setOnClickListener(mWorkReportActivity.writeDayReportClicklintener);
		mWorkReportActivity.btnWeek.setOnClickListener(mWorkReportActivity.writeWeeReportClicklintener);
		mWorkReportActivity.btnMonth.setOnClickListener(mWorkReportActivity.writeMonthReportClicklintener);
		mWorkReportActivity.hit1 = (TextView) mWorkReportActivity.findViewById(R.id.task_image_hit);
		mWorkReportActivity.hit2 = (TextView) mWorkReportActivity.findViewById(R.id.document_image_hit);
		WorkReportManager.getInstance().upDataHit(mWorkReportActivity);
	}

	@Override
	public void Create() {
		// TODO Auto-generated method stub
		initView();
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
	}





	public void doCreat(int type)
	{
		Intent intent = new Intent(mWorkReportActivity,NewReportActivity.class);
		Report report = new Report();
		report.mType = type;
		if (report.mType == WorkReportManager.TYPE_DAY) {
			report.mBegainTime = TimeUtils.getDate() + " 00:00:00";
			report.mEndTime = TimeUtils.getDate() + " 23:59:59";
		} else if (report.mType == WorkReportManager.TYPE_WEEK) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE,  - c.get(Calendar.DAY_OF_WEEK)+2);
			String data = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
					c.get(Calendar.DAY_OF_MONTH));
			report.mBegainTime = data + " 00:00:00";
			c = Calendar.getInstance();
			c.add(Calendar.DATE, 7- c.get(Calendar.DAY_OF_WEEK)+1);
			data = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
					c.get(Calendar.DAY_OF_MONTH));
			report.mEndTime = data + " 23:59:59";
		} else if (report.mType == WorkReportManager.TYPE_MONTH) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			Calendar a = Calendar.getInstance();
			try {
				a.setTime(format.parse(TimeUtils.getDateMYEx()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			int maxDate = a.get(Calendar.DATE);
			report.mBegainTime = String.format(TimeUtils.getDateMYEx() + "-01 00:00:00");
			report.mEndTime = String.format(TimeUtils.getDateMYEx() + "-%02d 23:59:59", maxDate);
		}
		if(WorkReportManager.getInstance().oaUtils.mAccount.mManagerId.length() != 0)
		{
			Contacts model = (Contacts) Bus.callData(mWorkReportActivity,"chat/getContactItem",WorkReportManager.getInstance().oaUtils.mAccount.mManagerId);
			report.mSenders = WorkReportManager.getInstance().getSenders();
			report.mCopyers = WorkReportManager.getInstance().getCopyers();
			if(report.mSenders.length() == 0)
			{
				report.mSenders = model.mRecordid;
			}
		}
		else
		{
			report.mSenders = WorkReportManager.getInstance().getSenders();
			report.mCopyers = WorkReportManager.getInstance().getCopyers();
		}
		intent.putExtra("report",report);
		mWorkReportActivity.startActivity(intent);
	}

	public void startList(int id)
	{
		Intent intent = new Intent(mWorkReportActivity,ReportListActivity.class);
		intent.putExtra("ntype",id);
		mWorkReportActivity.startActivity(intent);
	}

	public void updateHitView() {
		if(WorkReportManager.getInstance().hit1 > 0)
		{
			mWorkReportActivity.hit1.setVisibility(View.VISIBLE);
			if(WorkReportManager.getInstance().hit1 > 99)
			{
				mWorkReportActivity.hit1.setText("99+");
			}
			else
			{
				mWorkReportActivity.hit1.setText(String.valueOf(WorkReportManager.getInstance().hit1));
			}
		}
		else
		{
			mWorkReportActivity.hit1.setVisibility(View.INVISIBLE);
		}
		if(WorkReportManager.getInstance().hit2 > 0)
		{
			mWorkReportActivity.hit2.setVisibility(View.VISIBLE);
			if(WorkReportManager.getInstance().hit2 > 99)
			{
				mWorkReportActivity.hit2.setText("99+");
			}
			else
			{
				mWorkReportActivity.hit2.setText(String.valueOf(WorkReportManager.getInstance().hit2));
			}
		}
		else
		{
			mWorkReportActivity.hit2.setVisibility(View.INVISIBLE);
		}
	}
}
