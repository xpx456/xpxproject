package intersky.leave.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Attachment;
import intersky.leave.entity.Approve;
import intersky.leave.entity.Leave;
import intersky.leave.presenter.LeaveDetialPresenter;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.WebEdit;


@SuppressLint("ClickableViewAccessibility")
public class LeaveDetialActivity extends BaseActivity
{
	public static final int EVENT_UPDATA_TYPE = 1000;
	public static final int EVENT_UPDATA_TYPE_FAIL = 1013;
	public static final int EVENT_SUBMIT_SUCCESS = 1001;
	public static final int EVENT_UPDATA_SUCCESS = 1003;
	public static final int EVENT_UPDATA_FAIL = 1004;
	public static final int EVENT_SAVE_SUCCESS = 1005;
	public static final int EVENT_SAVE_FAIL = 1006;
	public static final int EVENT_GET_DETIAL_SUCCESS = 1011;
	public static final int EVENT_GET_DETIAL_FAIL = 1012;
	public static final int EVENT_GET_IMAGE = 1014;
	public static final int EVENT_DO_DELET_SUCCESS = 1015;
	public static final int EVENT_DO_DELET_FAIL = 1016;
	public static final int EVENT_DO_APPROVE_ACCEPT_SUCCESS = 1017;
	public static final int EVENT_DO_APPROVE_REFOUS_SUCCESS = 1018;
	public static final int EVENT_DO_APPROVE_FAIL = 1019;
	public static final int EVENT_REGET_DETIAL = 1020;
	public static final int EVENT_GET_ATTACHMENT_FAIL = 1021;
	public static final int EVENT_GET_ATTACHMENT_SUCCESS = 1022;

	public RelativeLayout mbuttomLayer;
	public LinearLayout copyer;
	public LinearLayout mSendlayer;
	public PopupWindow popupWindow1;
	public ArrayList<String> durls = new ArrayList<String>();
	public ArrayList<Contacts> mSenders = new ArrayList<Contacts>();
	public ArrayList<Contacts> mCopyers = new ArrayList<Contacts>();
	public ArrayList<Attachment> mPics = new ArrayList<Attachment>();
	public TextView mBeginText;
	public TextView mEndText;
	public TextView mTypeText;
	public TextView mCountText;
	public TextView mBtnleft;
	public TextView mBtnright;
	public TextView head;
	public RelativeLayout mRelativeLayout;
	public WebEdit mReasonText;
	public ImageView statuImage;
	public PopupWindow popupWindow;
	public LinearLayout mImageLayer;
	public RelativeLayout mCopyerLayer;
	public TextView more;
	public ArrayList<Approve> mApproves = new ArrayList<Approve>();
	public LeaveDetialPresenter mLeaveDetialPresenter = new LeaveDetialPresenter(this);
	public Leave mLeave;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mLeaveDetialPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mLeaveDetialPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mLeaveDetialPresenter.Start();
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
		mLeaveDetialPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mLeaveDetialPresenter.Resume();
	}


	public View.OnClickListener mCancleListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeaveDetialPresenter.dodismiss();
		}

	};

	public View.OnClickListener mEditListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			mLeaveDetialPresenter.onEdit();
		}

	};

	public View.OnClickListener mDeleteListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeaveDetialPresenter.onDelete();
		}

	};

	public View.OnClickListener mAcceptListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeaveDetialPresenter.doAccept();
		}

	};

	public View.OnClickListener mRefuseListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeaveDetialPresenter.doRefouse();
		}

	};

	public View.OnClickListener mMoreListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeaveDetialPresenter.showEdit();
		}

	};
}
