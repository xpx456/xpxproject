package intersky.notice.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.mywidget.WebEdit;
import intersky.notice.entity.Notice;
import intersky.notice.presenter.NewNoticePresenter;


@SuppressLint("ClickableViewAccessibility")
public class NewNoticeActivity extends BaseActivity
{
	public static final int EVENT_SUBMIT_SUCCESS = 1001;
	public static final int EVENT_ADD_PICTURE = 1002;
	public static final int EVENT_UPDATA_SUCCESS = 1003;
	public static final int EVENT_UPDATA_FAIL = 1004;
	public static final int EVENT_SAVE_SUCCESS = 1005;
	public static final int EVENT_SAVE_FAIL = 1006;
	public static final int EVENT_SET_SEND = 1008;
	public static final int EVENT_ADD_PHOTO = 1010;
	public static final int EVENT_GET_IMAGE = 1012;
	public static final int EVENT_DO_UPPIC = 1013;
	public static final int EVENT_DO_SPIC = 1014;
	public static final int EVENT_CHECK_SPIC = 1015;
	public static final int EVENT_SET_NET_IMAGE = 1020;
	public static final int EVENT_PIC_LOCL = 1021;
	public RelativeLayout mSenderLayer;
	public LinearLayout mImageLayer;
	public RelativeLayout mRelativeLayout;
	public EditText mTitleText;
	public WebEdit mCountText;
    public EditText mCountText1;
	public TextView mSenderText;
	public RelativeLayout takePhoto;
	public RelativeLayout getPhoto;
	public ArrayList<Contacts> mSenders = new ArrayList<Contacts>();
	public ArrayList<Attachment> mPics = new ArrayList<Attachment>();
	public PopupWindow popupWindow;
	public NewNoticePresenter mNewNoticePresenter = new NewNoticePresenter(this);
    public Notice notice;
	public boolean issub = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mNewNoticePresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mNewNoticePresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mNewNoticePresenter.Start();
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
		mNewNoticePresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mNewNoticePresenter.Resume();
	}

	public View.OnClickListener mSubmitListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mNewNoticePresenter.submit();
		}

	};


	public View.OnClickListener mDeletePicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mNewNoticePresenter.deletePic((Attachment) v.getTag());
		}
	};

	public View.OnClickListener mAddPicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mNewNoticePresenter.addPic();
		}
	};


	public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mNewNoticePresenter.takePhoto();
		}

	};


	public View.OnClickListener senderListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewNoticePresenter.selectSender();
		}
	};

	public View.OnClickListener publicListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNewNoticePresenter.submit();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		mNewNoticePresenter.takePhotoResult(requestCode, resultCode, data);
	}

    public View.OnClickListener startEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mNewNoticePresenter.startEdit();
        }
    };
}
