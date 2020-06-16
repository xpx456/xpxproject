package intersky.workreport.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.WebEdit;
import intersky.workreport.R;
import intersky.workreport.entity.Report;
import intersky.workreport.presenter.NewReportPresenter;


@SuppressLint("ClickableViewAccessibility")
public class NewReportActivity extends BaseActivity
{

	public TextView worktimename;
	public TextView worktimename1;
	public TextView worktimename2;
	public ArrayList<String> durls = new ArrayList<String>();
	public EditText mNowWork1;
	public EditText mNextWork1;
	public EditText mWorkHelp1;
	public EditText mRemark1;
	public EditText summerText1;
	public WebEdit mNowWork;
	public WebEdit mNextWork;
	public WebEdit mWorkHelp;
	public WebEdit mRemark;
	public WebEdit summerText;
	public RelativeLayout mworktime;
	public RelativeLayout mworkSender;
	public RelativeLayout mworkCopyer;
	public RelativeLayout mshada;
	public RelativeLayout takePhoto;
	public RelativeLayout getPhoto;
	public ScrollView scollayer;
	public MyLinearLayout sender;
	public MyLinearLayout copyer;
	public ArrayList<Contacts> mSenders = new ArrayList<Contacts>();
	public ArrayList<Contacts> mCopyers = new ArrayList<Contacts>();
	public ArrayList<Attachment> mPics = new ArrayList<Attachment>();
	public LinearLayout mImageLayer;
	public NewReportPresenter mNewReportPresenter = new NewReportPresenter(this);
	public Report mReport;
	public boolean issub = false;
    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mNewReportPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mNewReportPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mNewReportPresenter.Start();
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
		mNewReportPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mNewReportPresenter.Resume();
	}

	public View.OnClickListener detepickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewReportPresenter.onDataPick();
		}
	};

	public View.OnClickListener detepickListener1 = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewReportPresenter.onDataPick1();
		}
	};

	public View.OnClickListener detepickListener2 = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewReportPresenter.onDataPick2();
		}
	};

	public View.OnClickListener senderListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewReportPresenter.selectSender();
		}
	};

	public View.OnClickListener copyerListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewReportPresenter.selectCopyer();
		}
	};

	public View.OnClickListener saveListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewReportPresenter.saveData();
		}
	};

	public View.OnClickListener mAddPicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mNewReportPresenter.addPic();
		}
	};

	public View.OnClickListener mShowPicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mNewReportPresenter.showPic( v);
		}
	};

	public View.OnClickListener mDeleteSenderListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mNewReportPresenter.deleteSender( v);
		}
	};

	public View.OnClickListener mDeleteCopyerListener = new View.OnClickListener() {

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mNewReportPresenter.deleteCopyer( v);
	}
};


	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		//触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
		if ((view.getId() == R.id.content2text && mNewReportPresenter.canVerticalScroll(mNextWork1))) {
			view.getParent().requestDisallowInterceptTouchEvent(true);
			if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
				view.getParent().requestDisallowInterceptTouchEvent(false);
			}
		}
		else if ((view.getId() == R.id.content1text && mNewReportPresenter.canVerticalScroll(mNowWork1))) {
			view.getParent().requestDisallowInterceptTouchEvent(true);
			if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
				view.getParent().requestDisallowInterceptTouchEvent(false);
			}
		}
		else if ((view.getId() == R.id.content3text && mNewReportPresenter.canVerticalScroll(mWorkHelp1))) {
			view.getParent().requestDisallowInterceptTouchEvent(true);
			if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
				view.getParent().requestDisallowInterceptTouchEvent(false);
			}
		}
		else if ((view.getId() == R.id.content4text && mNewReportPresenter.canVerticalScroll(mRemark1))) {
			view.getParent().requestDisallowInterceptTouchEvent(true);
			if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
				view.getParent().requestDisallowInterceptTouchEvent(false);
			}
		}
		if(summerText1 != null)
		{
			if ((view.getId() == R.id.content5text && mNewReportPresenter.canVerticalScroll(summerText1))) {
				view.getParent().requestDisallowInterceptTouchEvent(true);
				if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
					view.getParent().requestDisallowInterceptTouchEvent(false);
				}
			}
		}

		return false;
	}

	public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mNewReportPresenter.takePhoto();
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		mNewReportPresenter.takePhotoResult(requestCode, resultCode, data);
	}

	public View.OnClickListener mDeletePicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mNewReportPresenter.deletePic((Attachment) v.getTag());
		}
	};

	public View.OnClickListener startEdit5Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewReportPresenter.startEdit5();
		}
	};

	public View.OnClickListener startEdit1Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewReportPresenter.startEdit1();
		}
	};

	public View.OnClickListener startEdit2Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewReportPresenter.startEdit2();
		}
	};

	public View.OnClickListener startEdit3Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewReportPresenter.startEdit3();
		}
	};

	public View.OnClickListener startEdit4Listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewReportPresenter.startEdit4();
		}
	};
}
