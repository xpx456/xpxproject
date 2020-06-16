package intersky.attendance.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.attendance.entity.AttdanceSet;
import intersky.attendance.presenter.NewSetAttendancePresenter;
import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.mywidget.MyLinearLayout;
import intersky.select.entity.Select;


@SuppressLint("ClickableViewAccessibility")
public class NewSetAttendanceActivity extends BaseActivity
{
	public static final String  ACTION_SET_ATTENDANCE_CONTACTS = "ACTION_SET_ATTENDANCE_CONTACTS";
	public static final String  ACTION_SET_ATTENDANCE_REMINDS = "ACTION_SET_ATTENDANCE_REMINDS";
	public NewSetAttendancePresenter mNewSetAttendancePresenter = new NewSetAttendancePresenter(this);
	public ArrayList<Contacts> mCopyers = new ArrayList<Contacts>();
	public ArrayList<Select> mReminds = new ArrayList<Select>();
	public PopupWindow popupWindow1;
	public EditText name;
	public TextView startTime;
	public TextView endTime;
	public TextView day;
	public TextView btndel;
	public RelativeLayout startTimelayer;
	public RelativeLayout endTimelayer;
	public RelativeLayout dayTimelayer;
	public MyLinearLayout copyer;
	public RelativeLayout remindLayer;
	public boolean edit = false;
	public AttdanceSet set;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mNewSetAttendancePresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mNewSetAttendancePresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mNewSetAttendancePresenter.Start();
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
		mNewSetAttendancePresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mNewSetAttendancePresenter.Resume();
	}

	public View.OnClickListener saveListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewSetAttendancePresenter.dosave();
		}
	};

	public View.OnClickListener deleteListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewSetAttendancePresenter.deleteData();
		}
	};

	public View.OnClickListener timeStartListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewSetAttendancePresenter.showTimeDialogStart();
		}
	};

	public View.OnClickListener timeEndListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewSetAttendancePresenter.showTimeDialogEnd();
		}
	};


	public View.OnClickListener copyerListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewSetAttendancePresenter.selectCopyer();
		}
	};

	public View.OnClickListener mDeleteCopyerListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mNewSetAttendancePresenter.deleteCopyer( v);
		}
	};

	public View.OnClickListener setRemind = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mNewSetAttendancePresenter.doRemind();
		}
	};
}
