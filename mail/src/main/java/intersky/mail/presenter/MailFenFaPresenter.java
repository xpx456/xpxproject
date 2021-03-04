package intersky.mail.presenter;

import android.content.Intent;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.handler.MailFengFaHandler;
import intersky.mail.receiver.MailReceiver;
import intersky.mail.view.activity.MailFengFaActivity;
import intersky.mail.view.adapter.MailLableAdapter;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.TimeUtils;
import xpx.com.toolbar.utils.ToolBarHelper;
public class MailFenFaPresenter implements Presenter {

	private MailFengFaActivity mMailFengFaActivity;
	public MailFengFaHandler mMailFengFaHandler;
	
	public MailFenFaPresenter(MailFengFaActivity mMailFengFaActivity)
	{
		this.mMailFengFaActivity = mMailFengFaActivity;
		mMailFengFaHandler = new MailFengFaHandler(mMailFengFaActivity);
        mMailFengFaActivity.setBaseReceiver( new MailReceiver(mMailFengFaHandler));
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mMailFengFaActivity.setContentView(R.layout.activity_mail_feng_fa);
		RelativeLayout mLinearLayout = (RelativeLayout) mMailFengFaActivity.findViewById(R.id.activity_mail_feng_fa);
		mLinearLayout.setOnTouchListener(mMailFengFaActivity);
		mLinearLayout.setLongClickable(true);
		mMailFengFaActivity.mail = mMailFengFaActivity.getIntent().getParcelableExtra("maildata");
		mMailFengFaActivity.mGestureDetector = new GestureDetector((OnGestureListener) mMailFengFaActivity);
		ImageView back = mMailFengFaActivity.findViewById(R.id.back);
		back.setOnClickListener(mMailFengFaActivity.mBackListener);
		mMailFengFaActivity.mCustomer = (RelativeLayout) mMailFengFaActivity.findViewById(R.id.custom_layer);
		mMailFengFaActivity.mTime = (RelativeLayout) mMailFengFaActivity.findViewById(R.id.time_layer);
		mMailFengFaActivity.mLable = (RelativeLayout) mMailFengFaActivity.findViewById(R.id.lable_layer);
		mMailFengFaActivity.mFengfaTime = (TextView) mMailFengFaActivity.findViewById(R.id.time_text);
        mMailFengFaActivity.mFengfaTime.setText(TimeUtils.getDate()+" "+TimeUtils.getTime());
		mMailFengFaActivity.mListView = (ListView) mMailFengFaActivity.findViewById(R.id.serverList);
		mMailFengFaActivity.mFengfaName = (TextView) mMailFengFaActivity.findViewById(R.id.custom_text);
		mMailFengFaActivity.mFengfaBiaoqian = (TextView) mMailFengFaActivity.findViewById(R.id.lable_text);
		mMailFengFaActivity.mFengfaditial = (EditText) mMailFengFaActivity.findViewById(R.id.detial_text);
		mMailFengFaActivity.mFengfaOk = (TextView) mMailFengFaActivity.findViewById(R.id.ok_text);
		mMailFengFaActivity.mFengfaCancle = (TextView) mMailFengFaActivity.findViewById(R.id.cancle_text);
		mMailFengFaActivity.mListlayer = (RelativeLayout) mMailFengFaActivity.findViewById(R.id.List_layer);
		mMailFengFaActivity.mMailSelectAdapter = new MailLableAdapter(mMailFengFaActivity, MailManager.getInstance().mMyLabols);
		mMailFengFaActivity.mListView.setAdapter(mMailFengFaActivity.mMailSelectAdapter);
		mMailFengFaActivity.mCustomer.setOnClickListener(mMailFengFaActivity.mCustomerListener);
		mMailFengFaActivity.mTime.setOnClickListener(mMailFengFaActivity.mTimeListener);
		mMailFengFaActivity.mLable.setOnClickListener(mMailFengFaActivity.mLableListener);
		mMailFengFaActivity.mListView.setOnItemClickListener(mMailFengFaActivity.mUserMailItemClick);
		mMailFengFaActivity.mFengfaOk.setOnClickListener(mMailFengFaActivity.mOkListener);
		mMailFengFaActivity.mFengfaCancle.setOnClickListener(mMailFengFaActivity.mCancleListener);
		if(MailManager.getInstance().mMyLabols.size() == 0)
		{
			mMailFengFaActivity.waitDialog.setTitle(mMailFengFaActivity.getString(R.string.mail_loading));
			MailManager.getInstance().getLables();

		}
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
		mMailFengFaActivity.mFengfaName.setText(mMailFengFaActivity.personname);
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
	}
	
	private void sendFengfa()
	{
        MailAsks.sendFengfa(mMailFengFaHandler,mMailFengFaActivity,mMailFengFaActivity.mail.mRecordId,mMailFengFaActivity.mFengfaditial.getText().toString(),
                mMailFengFaActivity.lableRecord,mMailFengFaActivity.personid, mMailFengFaActivity.mFengfaTime.getText().toString());
        mMailFengFaActivity.waitDialog.show();
	}
	
	public void showTimeDialog()
	{
        AppUtils.creatDataAndTimePicker(mMailFengFaActivity,mMailFengFaActivity.mFengfaTime.getText().toString(), mMailFengFaActivity.getString(R.string.xml_chuliqixian2),mOnBeginSetListener);
	}
	
	public void showSelectDialog()
	{
		if(MailManager.getInstance().mMyLabols.size() == 0)
		{
			AppUtils.showMessage(mMailFengFaActivity, mMailFengFaActivity.getString(R.string.keyword_nosig));
		}
		else
		{
			if(mMailFengFaActivity.mListlayer.getVisibility() == View.INVISIBLE)
			{
				mMailFengFaActivity.mListlayer.setVisibility(View.VISIBLE);
			}
			else
			{
				mMailFengFaActivity.mListlayer.setVisibility(View.INVISIBLE);
			}

		}

	}
	
	public void setlboal(int position)
	{
		String biaoqian = MailManager.getInstance().mMyLabols.get(position).mName;
		mMailFengFaActivity.lableRecord = MailManager.getInstance().mMyLabols.get(position).mId;
		mMailFengFaActivity.mFengfaBiaoqian.setText(biaoqian);
		mMailFengFaActivity.mListlayer.setVisibility(View.INVISIBLE);
	}
	
	public void setCustomer() {
        Bus.callData(mMailFengFaActivity,"chat/setContacts",mMailFengFaActivity.getString(R.string.xml_fenfagei),MailManager.ACTION_MAIL_SET_FENFA_PERSON);
	}
	
	public void dofenfa()
	{
		if(mMailFengFaActivity.mListlayer.getVisibility() == View.INVISIBLE)
		{				
			if(mMailFengFaActivity.personid.length() > 0)
			{
				sendFengfa();
			}
			else
			{
				AppUtils.showMessage(mMailFengFaActivity, mMailFengFaActivity.getString(R.string.keyword_selectfengfa));
			}
		}
	}
	
	public void doCancle()
	{
		if(mMailFengFaActivity.mListlayer.getVisibility() == View.INVISIBLE)
		{
			mMailFengFaActivity.finish();

		}
	}
	


	public void upDatePerson(Intent mintent)
	{
        Contacts contacts = mintent.getParcelableExtra("contacts");
		mMailFengFaActivity.personid = contacts.mRecordid;
		mMailFengFaActivity.personname = contacts.mName;
		mMailFengFaActivity.mFengfaName.setText(mMailFengFaActivity.personname);
	}

    public DoubleDatePickerDialog.OnDateSetListener mOnBeginSetListener = new DoubleDatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d %02d:%02d", startYear, startMonthOfYear + 1, startDayOfMonth, hour, miniute);
            mMailFengFaActivity.mFengfaTime.setText(textString);
        }
    };
}
