package intersky.attendance.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


import java.util.ArrayList;

import intersky.attendance.entity.AttdanceSet;
import intersky.attendance.presenter.SetAttendancePresenter;
import intersky.attendance.view.adapter.AttdanceSetAdapter;

@SuppressLint("ClickableViewAccessibility")
public class SetAttendanceActivity extends BaseActivity
{

	public SetAttendancePresenter mSetAttendancePresenter = new SetAttendancePresenter(this);
	public AttdanceSetAdapter mAttdanceSetAdapter;
	public PopupWindow popupWindow1;
	public RelativeLayout mAdd;
	public ListView attList;
	public ArrayList<AttdanceSet> mAttdanceSets = new ArrayList<AttdanceSet>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mSetAttendancePresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mSetAttendancePresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mSetAttendancePresenter.Start();
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
		mSetAttendancePresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mSetAttendancePresenter.Resume();
	}

	public View.OnClickListener mCreatListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mSetAttendancePresenter.doCreat();
		}
	};

	public AdapterView.OnItemClickListener mDetialshow = new AdapterView.OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mSetAttendancePresenter.showDetial((AttdanceSet) parent.getAdapter().getItem(position));
		}
	};
}
