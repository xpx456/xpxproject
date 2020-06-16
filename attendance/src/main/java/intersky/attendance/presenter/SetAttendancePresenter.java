package intersky.attendance.presenter;

import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import intersky.attendance.R;
import intersky.attendance.asks.AttdenanceAsks;
import intersky.attendance.entity.AttdanceSet;
import intersky.attendance.handler.SetAttendanceHandler;
import intersky.attendance.receive.SetAttendanceReceiver;
import intersky.attendance.view.activity.NewSetAttendanceActivity;
import intersky.attendance.view.activity.SetAttendanceActivity;
import intersky.attendance.view.adapter.AttdanceSetAdapter;
import intersky.appbase.Presenter;
import xpx.com.toolbar.utils.ToolBarHelper;


public class SetAttendancePresenter implements Presenter {

	public SetAttendanceActivity mSetAttendmanceActivity;
	public SetAttendanceHandler mSetAttendanceHandler;

	public SetAttendancePresenter(SetAttendanceActivity mSetAttendmanceActivity) {
		this.mSetAttendmanceActivity = mSetAttendmanceActivity;
		this.mSetAttendanceHandler = new SetAttendanceHandler(mSetAttendmanceActivity);
		mSetAttendmanceActivity.setBaseReceiver(new SetAttendanceReceiver(mSetAttendanceHandler));
	}




	@Override
	public void initView() {
		// TODO Auto-generated method stub

		mSetAttendmanceActivity.setContentView(R.layout.activity_set_attendance);
		ToolBarHelper.setTitle(mSetAttendmanceActivity.mActionBar, mSetAttendmanceActivity.getString(R.string.keyword_workattdenceset));
		mSetAttendmanceActivity.mAdd = (RelativeLayout) mSetAttendmanceActivity.findViewById(R.id.add_layer);
		mSetAttendmanceActivity.mAdd.setOnClickListener(mSetAttendmanceActivity.mCreatListener);
		mSetAttendmanceActivity.attList = (ListView) mSetAttendmanceActivity.findViewById(R.id.attdance_List);
		mSetAttendmanceActivity.mAttdanceSetAdapter = new AttdanceSetAdapter(mSetAttendmanceActivity,mSetAttendmanceActivity.mAttdanceSets);
		mSetAttendmanceActivity.attList.setAdapter(mSetAttendmanceActivity.mAttdanceSetAdapter);
		mSetAttendmanceActivity.attList.setOnItemClickListener(mSetAttendmanceActivity.mDetialshow);
		AttdenanceAsks.getSetList(mSetAttendanceHandler,mSetAttendmanceActivity);
	}

	@Override
	public void Create() {
		// TODO Auto-generated method stub
		initView();
		IntentFilter filter = new IntentFilter();

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

	public void doCreat()
	{
		Intent intent = new Intent(mSetAttendmanceActivity, NewSetAttendanceActivity.class);
		AttdanceSet item = new AttdanceSet();
		item.start = "08:30:00";
		item.end = "17:30:00";
		item.x = mSetAttendmanceActivity.getIntent().getDoubleExtra("x",0);
		item.y = mSetAttendmanceActivity.getIntent().getDoubleExtra("y",0);
		intent.putExtra("set",item);
		mSetAttendmanceActivity.startActivity(intent);
	}

	public void showDetial(AttdanceSet item)
	{
		Intent intent = new Intent(mSetAttendmanceActivity,NewSetAttendanceActivity.class);
		intent.putExtra("set",item);
		mSetAttendmanceActivity.startActivity(intent);
	}

}
