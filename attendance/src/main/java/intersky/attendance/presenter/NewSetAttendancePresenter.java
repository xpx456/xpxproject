package intersky.attendance.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import intersky.attendance.AttendanceManager;
import intersky.attendance.R;
import intersky.attendance.asks.AttdenanceAsks;
import intersky.attendance.handler.NewSetAttendanceHandler;
import intersky.attendance.receive.NewSetAttendanceReceiver;
import intersky.attendance.view.activity.NewSetAttendanceActivity;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.appbase.entity.Contacts;
import intersky.mywidget.MyLinearLayout;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import xpx.com.toolbar.utils.ToolBarHelper;

public class NewSetAttendancePresenter implements Presenter {

	public NewSetAttendanceActivity mNewSetAttendanceActivity;
	public NewSetAttendanceHandler mSetAttendanceHandler;

	public NewSetAttendancePresenter(NewSetAttendanceActivity mNewSetAttendanceActivity) {
		this.mNewSetAttendanceActivity = mNewSetAttendanceActivity;
		this.mSetAttendanceHandler = new NewSetAttendanceHandler(mNewSetAttendanceActivity);
		mNewSetAttendanceActivity.setBaseReceiver(new NewSetAttendanceReceiver(mSetAttendanceHandler));
	}




	@Override
	public void initView() {
		// TODO Auto-generated method stub

		mNewSetAttendanceActivity.setContentView(R.layout.activity_new_set_attendance);
		mNewSetAttendanceActivity.set = mNewSetAttendanceActivity.getIntent().getParcelableExtra("set");
		ImageView back = mNewSetAttendanceActivity.findViewById(R.id.back);
		back.setOnClickListener(mNewSetAttendanceActivity.mBackListener);
		TextView save = mNewSetAttendanceActivity.findViewById(R.id.title);
		save.setOnClickListener(mNewSetAttendanceActivity.saveListener);
		mNewSetAttendanceActivity.edit = mNewSetAttendanceActivity.getIntent().getBooleanExtra("edit",false);
		mNewSetAttendanceActivity.btndel = (TextView) mNewSetAttendanceActivity.findViewById(R.id.btndelete);
		mNewSetAttendanceActivity.name = (EditText) mNewSetAttendanceActivity.findViewById(R.id.name_value);
		mNewSetAttendanceActivity.startTime = (TextView) mNewSetAttendanceActivity.findViewById(R.id.atteance_start_value);
		mNewSetAttendanceActivity.endTime = (TextView) mNewSetAttendanceActivity.findViewById(R.id.atteance_end_value);
		mNewSetAttendanceActivity.day = (TextView) mNewSetAttendanceActivity.findViewById(R.id.atteance_day_value);
		mNewSetAttendanceActivity.remindLayer= (RelativeLayout) mNewSetAttendanceActivity.findViewById(R.id.atteance_day_layer);
		mNewSetAttendanceActivity.startTimelayer = (RelativeLayout) mNewSetAttendanceActivity.findViewById(R.id.atteance_start_layer);
		mNewSetAttendanceActivity.endTimelayer = (RelativeLayout) mNewSetAttendanceActivity.findViewById(R.id.atteance_end_layer);
		mNewSetAttendanceActivity.dayTimelayer = (RelativeLayout) mNewSetAttendanceActivity.findViewById(R.id.atteance_day_layer);
		mNewSetAttendanceActivity.copyer = (MyLinearLayout) mNewSetAttendanceActivity.findViewById(R.id.copyer);
		mNewSetAttendanceActivity.remindLayer.setOnClickListener(mNewSetAttendanceActivity.setRemind);
		mNewSetAttendanceActivity.startTimelayer.setTag(mNewSetAttendanceActivity.startTime);
		mNewSetAttendanceActivity.startTimelayer.setOnClickListener(mNewSetAttendanceActivity.timeStartListener);
		mNewSetAttendanceActivity.endTimelayer.setTag(mNewSetAttendanceActivity.endTime);
		mNewSetAttendanceActivity.endTimelayer.setOnClickListener(mNewSetAttendanceActivity.timeEndListener);
		mNewSetAttendanceActivity.btndel.setOnClickListener(mNewSetAttendanceActivity.deleteListener);
		initRemind();
		if(mNewSetAttendanceActivity.set.aId.length() > 0)
		{
			mNewSetAttendanceActivity.btndel.setVisibility(View.VISIBLE);
			AttdenanceAsks.getDetial(mSetAttendanceHandler,mNewSetAttendanceActivity,mNewSetAttendanceActivity.set);
		}
		else
		{
			initDetial();
		}
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
		mSetAttendanceHandler = null;
	}

	public void showTimeDialogStart() {
		AppUtils.creatTimePicker(mNewSetAttendanceActivity,mNewSetAttendanceActivity.startTime.getText().toString(), mNewSetAttendanceActivity.getString(R.string.keyword_signtime),mOnBeginSetListener);
	}

	public void showTimeDialogEnd() {
		AppUtils.creatTimePicker(mNewSetAttendanceActivity,mNewSetAttendanceActivity.endTime.getText().toString(), mNewSetAttendanceActivity.getString(R.string.keyword_signtime),mOnEndSetListener);
	}


	public void initRemind()
	{
		mNewSetAttendanceActivity.mReminds.add(new Select("1",mNewSetAttendanceActivity.getString(R.string.keyword_mon_e)));
		mNewSetAttendanceActivity.mReminds.add(new Select("2",mNewSetAttendanceActivity.getString(R.string.keyword_tue_e)));
		mNewSetAttendanceActivity.mReminds.add(new Select("3",mNewSetAttendanceActivity.getString(R.string.keyword_wen_e)));
		mNewSetAttendanceActivity.mReminds.add(new Select("4",mNewSetAttendanceActivity.getString(R.string.keyword_ths_e)));
		mNewSetAttendanceActivity.mReminds.add(new Select("5",mNewSetAttendanceActivity.getString(R.string.keyword_fri_e)));
		mNewSetAttendanceActivity.mReminds.add(new Select("6",mNewSetAttendanceActivity.getString(R.string.keyword_sat_e)));
		mNewSetAttendanceActivity.mReminds.add(new Select("0",mNewSetAttendanceActivity.getString(R.string.keyword_sun_e)));
	}

	public void dosave()
	{

		if(mNewSetAttendanceActivity.mCopyers.size() ==0)
		{
			AppUtils.showMessage(mNewSetAttendanceActivity,mNewSetAttendanceActivity.getString(R.string.keyword_set_attdance_person));
			return;
		}
		String copyer = AttendanceManager.getInstance().oaUtils.mContactManager.getMemberIds(mNewSetAttendanceActivity.mCopyers);
		String id = "";
		for (int i = 0; i < mNewSetAttendanceActivity.mReminds.size(); i++) {
			if (mNewSetAttendanceActivity.mReminds.get(i).iselect == true) {
				if (id.length() == 0) {
					id += mNewSetAttendanceActivity.mReminds.get(i).mId;
				} else {
					id += "," + mNewSetAttendanceActivity.mReminds.get(i).mId;
				}
			}
		}
		if(mNewSetAttendanceActivity.edit == true)
		{
			if(mNewSetAttendanceActivity.set.copyer.equals(copyer)&&mNewSetAttendanceActivity.set.dayid.equals(id)
					&&mNewSetAttendanceActivity.set.name.equals(mNewSetAttendanceActivity.name.getText().toString())
					&&mNewSetAttendanceActivity.set.start.substring(0,5).equals(mNewSetAttendanceActivity.startTime.getText().toString())
					&&mNewSetAttendanceActivity.set.end.substring(0,5).equals(mNewSetAttendanceActivity.endTime.getText().toString()))
			{
				mNewSetAttendanceActivity.finish();
				return;
			}
		}
		mNewSetAttendanceActivity.set.copyer = copyer;
		mNewSetAttendanceActivity.set.dayid = id;
		mNewSetAttendanceActivity.set.name = mNewSetAttendanceActivity.name.getText().toString();
		mNewSetAttendanceActivity.set.start = mNewSetAttendanceActivity.startTime.getText().toString()+":00";
		mNewSetAttendanceActivity.set.end = mNewSetAttendanceActivity.endTime.getText().toString()+":00";
		mNewSetAttendanceActivity.waitDialog.show();
		AttdenanceAsks.saveSet(mSetAttendanceHandler,mNewSetAttendanceActivity,mNewSetAttendanceActivity.set);

	}






	public void initselectView(ArrayList<Contacts> mselectitems, MyLinearLayout mlayer, View.OnClickListener senderListener, View.OnClickListener itemListener) {
		LayoutInflater mInflater = (LayoutInflater) mNewSetAttendanceActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mlayer.removeAllViews();
		if (mselectitems.size() > 0) {
			for (int i = 0; i < mselectitems.size(); i++) {
				Contacts mContact = mselectitems.get(i);
				View mview = mInflater.inflate(R.layout.sample_contact_item_ex, null);
				TextView mhead = (TextView) mview.findViewById(R.id.contact_img);
				Bus.callData(mNewSetAttendanceActivity,"setContactCycleHead",mhead,mContact);
				TextView name = (TextView) mview.findViewById(R.id.title);
				name.setText(mContact.mName);
				mview.setTag(mContact);
				mview.setOnClickListener(itemListener);
				mlayer.addView(mview);
			}

		}
		View mview = mInflater.inflate(R.layout.sample_contact_item_add, null);
		mview.setOnClickListener(senderListener);
		mlayer.addView(mview);

	}

	public void selectCopyer() {
		AttendanceManager.getInstance().oaUtils.mContactManager.selectListContact(mNewSetAttendanceActivity
				,mNewSetAttendanceActivity.mCopyers,NewSetAttendanceActivity.ACTION_SET_ATTENDANCE_CONTACTS,mNewSetAttendanceActivity.getString(R.string.select_contact),false);
	}

	public void deleteCopyer(View v) {
		Contacts copy = (Contacts) v.getTag();
		mNewSetAttendanceActivity.copyer.removeView(v);
		mNewSetAttendanceActivity.mCopyers.remove(copy);
	}

	public void setCopyer() {
		mNewSetAttendanceActivity.mCopyers.clear();
		mNewSetAttendanceActivity.mCopyers.addAll((ArrayList<Contacts>)Bus.callData(mNewSetAttendanceActivity,"chat/mselectitems"));
		initselectView(mNewSetAttendanceActivity.mCopyers, mNewSetAttendanceActivity.copyer, mNewSetAttendanceActivity.copyerListener, mNewSetAttendanceActivity.mDeleteCopyerListener);

	}

	public void doRemind() {
		SelectManager.getInstance().startSelectView(mNewSetAttendanceActivity,mNewSetAttendanceActivity.mReminds
				,mNewSetAttendanceActivity.getString(R.string.xml_attdance_day),NewSetAttendanceActivity.ACTION_SET_ATTENDANCE_REMINDS,false,false);
	}

	public void setRemind() {
		String text = "";
		String text1 = "";
		for (int i = 0; i < SelectManager.getInstance().mSelects.size(); i++) {
			mNewSetAttendanceActivity.mReminds.get(i).iselect = SelectManager.getInstance().mSelects.get(i).iselect;
			if (mNewSetAttendanceActivity.mReminds.get(i).iselect == true) {
				if (text.length() == 0) {
					text += mNewSetAttendanceActivity.mReminds.get(i).mId;
					text1 += mNewSetAttendanceActivity.mReminds.get(i).mName;
				} else {
					text += "," + mNewSetAttendanceActivity.mReminds.get(i).mId;
					text1 += "," + mNewSetAttendanceActivity.mReminds.get(i).mName;
				}
			}

		}
		String day = AttendanceManager.initDayString(text);
		if(day.length() == 0)
		{
			mNewSetAttendanceActivity.day.setText(text1);
		}
		else
		{
			mNewSetAttendanceActivity.day.setText(day);
		}
	}

	public void initDetial()
	{
		mNewSetAttendanceActivity.name.setText(mNewSetAttendanceActivity.set.name);
		mNewSetAttendanceActivity.startTime.setText(mNewSetAttendanceActivity.set.start.substring(0,5));
		mNewSetAttendanceActivity.endTime.setText(mNewSetAttendanceActivity.set.end.substring(0,5));
		String a =mNewSetAttendanceActivity.set.dayid;
		String b = AttendanceManager.initDayString(a);
		if(b.length() == 0 && a.length() != 0)
		{
			String[] strs = a.split(",");
			for(int j = 0 ; j < strs.length ; j++)
			{
				if(Integer.valueOf(strs[j]) == 0)
				{
					mNewSetAttendanceActivity.mReminds.get(6).iselect = true;
				}
				else
				{
					mNewSetAttendanceActivity.mReminds.get(Integer.valueOf(strs[j])-1).iselect = true;
				}
				if(j != 0)
				{
					b += ","+ AttendanceManager.getweek(Integer.valueOf(strs[j]));
				}
				else
				{
					b += AttendanceManager.getweek(Integer.valueOf(strs[j]));
				}
			}
		}
		mNewSetAttendanceActivity.set.day = b;
		mNewSetAttendanceActivity.day.setText(mNewSetAttendanceActivity.set.day);
		AttendanceManager.getInstance().oaUtils.mContactManager.getContacts(mNewSetAttendanceActivity.set.copyer,mNewSetAttendanceActivity.mCopyers);
		initselectView(mNewSetAttendanceActivity.mCopyers, mNewSetAttendanceActivity.copyer, mNewSetAttendanceActivity.copyerListener, mNewSetAttendanceActivity.mDeleteCopyerListener);
	}

	public void deleteData() {
		AppUtils.creatDialogTowButton(mNewSetAttendanceActivity,mNewSetAttendanceActivity.getString(R.string.xml_attdance_delete_msg)
		,mNewSetAttendanceActivity.getString(R.string.xml_attdance_delete),mNewSetAttendanceActivity.getString(R.string.button_word_cancle)
				,mNewSetAttendanceActivity.getString(R.string.button_word_ok),null, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						AttdenanceAsks.doDelete(mSetAttendanceHandler,mNewSetAttendanceActivity,mNewSetAttendanceActivity.set);
					}
				});

	}

	public DoubleDatePickerDialog.OnDateSetListener mOnBeginSetListener = new DoubleDatePickerDialog.OnDateSetListener(){

		@Override
		public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
			String textString = String.format("%02d:%02d", hour, miniute);
			mNewSetAttendanceActivity.startTime.setText(textString);
		}
	};

	public DoubleDatePickerDialog.OnDateSetListener mOnEndSetListener = new DoubleDatePickerDialog.OnDateSetListener(){

		@Override
		public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
			String textString = String.format("%02d:%02d", hour, miniute);
			mNewSetAttendanceActivity.endTime.setText(textString);
		}
	};

}
