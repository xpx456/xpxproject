package intersky.leave.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Attachment;
import intersky.leave.entity.Leave;
import intersky.leave.presenter.LeavePresenter;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.WebEdit;


@SuppressLint("ClickableViewAccessibility")
public class LeaveActivity extends BaseActivity
{

	public static final int EVENT_UPDATA_TYPE = 1000;
	public static final int EVENT_SUBMIT_SUCCESS = 1001;
	public static final int EVENT_ADD_PICTURE = 1002;
	public static final int EVENT_UPDATA_SUCCESS = 1003;
	public static final int EVENT_UPDATA_FAIL = 1004;
	public static final int EVENT_SAVE_SUCCESS = 1005;
	public static final int EVENT_SAVE_FAIL = 1006;
	public static final int EVENT_SET_SEND = 1008;
	public static final int EVENT_SET_COPY = 1009;
	public static final int EVENT_ADD_PHOTO = 1010;
	public static final int EVENT_GET_IMAGE = 1012;
	public static final int EVENT_SET_TYPE = 1013;
	public static final int EVENT_DO_UPPIC = 1014;
	public static final int EVENT_DO_SPIC = 1015;
	public static final int EVENT_CHECK_SPIC = 1016;
	public static final int EVENT_SET_NET_IMAGE = 1029;
	public static final int EVENT_PIC_LOCL = 1030;
	public RelativeLayout mBeginLayer;
	public RelativeLayout mEndLayer;
	public RelativeLayout mTypeLayer;
	public MyLinearLayout sender;
	public MyLinearLayout copyer;
	public ArrayList<Contacts> mSenders = new ArrayList<Contacts>();
	public ArrayList<Contacts> mCopyers = new ArrayList<Contacts>();
	public ArrayList<Attachment> mPics = new ArrayList<Attachment>();
	public TextView mBeginText;
	public TextView mEndText;
	public TextView mTypeText;
	public EditText mCountText;
	public TextView mSubmit;
	public RelativeLayout mRelativeLayout;
    public RelativeLayout takePhoto;
    public RelativeLayout getPhoto;
	public EditText mReasonText1;
	public WebEdit mReasonText;
	public ListView selectList;
	public PopupWindow popupWindow;
	public LinearLayout mImageLayer;
	public ArrayList<String> durls = new ArrayList<String>();
	public  boolean issub = false;
	public Leave mLeave;
	public LeavePresenter mLeavePresenter = new LeavePresenter(this);
    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mLeavePresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mLeavePresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mLeavePresenter.Start();
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
		mLeavePresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mLeavePresenter.Resume();
	}

	public View.OnClickListener mStartTextListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mLeavePresenter.showBeginTimeDialog();
		}

	};

	public View.OnClickListener mEndTextListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mLeavePresenter.showEndTimeDialog();
		}

	};

	public View.OnClickListener mTypeListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mLeavePresenter.setType();
		}

	};

	public View.OnClickListener mSubmitListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub

			mLeavePresenter.submit();
		}

	};


	public View.OnClickListener mAddPicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeavePresenter.addPic();
		}
	};

	public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeavePresenter.takePhoto();
		}

	};

	public View.OnClickListener senderListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mLeavePresenter.selectSender();
		}
	};

	public View.OnClickListener copyerListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mLeavePresenter.selectCopyer();
		}
	};

	public View.OnClickListener mDeleteSenderListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeavePresenter.deleteSender( v);
		}
	};

	public View.OnClickListener mDeleteCopyerListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeavePresenter.deleteCopyer( v);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		mLeavePresenter.takePhotoResult(requestCode, resultCode, data);
	}

	public View.OnClickListener mDeletePicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeavePresenter.deletePic((Attachment) v.getTag());
		}
	};

	public View.OnClickListener startEditListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mLeavePresenter.startEdit();
		}
	};
}
