package intersky.workreport.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.workreport.WorkReportManager;
import intersky.workreport.presenter.WorkReportPresenter;


@SuppressLint("ClickableViewAccessibility")
public class WorkReportActivity extends BaseActivity implements OnGestureListener,OnTouchListener
{

	public WorkReportPresenter mWorkReportPresenter = new WorkReportPresenter(this);
	public RelativeLayout btnReceive;
	public RelativeLayout btnSend;
	public RelativeLayout btnJoinin;
	public RelativeLayout btnDay;
	public RelativeLayout btnWeek;
	public RelativeLayout btnMonth;
	public TextView title;
	public TextView hit1;
	public TextView hit2;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mWorkReportPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mWorkReportPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mWorkReportPresenter.Start();
	}

	@Override
	protected void onStop()
	{
		super.onStop();

	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		mWorkReportPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mWorkReportPresenter.Resume();
	}

	public View.OnClickListener myReceiveClicklintener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mWorkReportPresenter.startList(ReportListActivity.TYPE_RECEIVE);
		}
	};

	public View.OnClickListener mySendClicklintener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mWorkReportPresenter.startList(ReportListActivity.TYPE_SEND);
		}
	};

	public View.OnClickListener myJoininClicklintener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mWorkReportPresenter.startList(ReportListActivity.TYPE_COPY);
		}
	};

	public View.OnClickListener writeDayReportClicklintener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mWorkReportPresenter.doCreat(WorkReportManager.TYPE_DAY);
		}
	};

	public View.OnClickListener writeWeeReportClicklintener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mWorkReportPresenter.doCreat(WorkReportManager.TYPE_WEEK);
		}
	};

	public View.OnClickListener writeMonthReportClicklintener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mWorkReportPresenter.doCreat(WorkReportManager.TYPE_MONTH);
		}
	};
}
