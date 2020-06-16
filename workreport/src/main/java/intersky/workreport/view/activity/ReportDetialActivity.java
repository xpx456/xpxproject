package intersky.workreport.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Reply;
import intersky.appbase.entity.Attachment;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.WebEdit;
import intersky.workreport.entity.Report;
import intersky.workreport.presenter.ReportDetialPresenter;


@SuppressLint("ClickableViewAccessibility")
public class ReportDetialActivity extends BaseActivity
{

	public PopupWindow popupWindow1;
	public TextView worktimename;
	public TextView worktimename1;
	public TextView worktimename2;
	public TextView answertitle;
	public WebEdit mNowWork;
	public WebEdit mNextWork;
	public WebEdit mWorkHelp;
	public WebEdit mRemark;
	public EditText mEditTextContent;
	public WebEdit summerText;
	public RelativeLayout mworktime;
	public RelativeLayout mworkSender;
	public RelativeLayout mworkCopyer;
	public RelativeLayout answer;
	public RelativeLayout mshada;
	public ScrollView scollayer;
	public MyLinearLayout sender;
	public MyLinearLayout copyer;
	public LinearLayout answerLayers;
	public ArrayList<Contacts> mSenders = new ArrayList<Contacts>();
	public ArrayList<Contacts> mCopyers = new ArrayList<Contacts>();
	public ArrayList<Reply> mReplys= new ArrayList<Reply>();
	public ArrayList<Attachment> mPics = new ArrayList<Attachment>();
	public LinearLayout mImageLayer;
	public Report report;
	public ReportDetialPresenter mReportDetialPresenter = new ReportDetialPresenter(this);
	public String attjson = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mReportDetialPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mReportDetialPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mReportDetialPresenter.Start();
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
		mReportDetialPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mReportDetialPresenter.Resume();
	}

	public View.OnClickListener mMoreListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mReportDetialPresenter.showEdit();
		}

	};

	public View.OnClickListener mEditListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			mReportDetialPresenter.onEdit();
		}

	};

	public View.OnClickListener mDeleteListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mReportDetialPresenter.onDelete((Report) v.getTag());
		}

	};

	public View.OnClickListener mDeleteReplyListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mReportDetialPresenter.showDeleteReply((Reply) v.getTag());
		}

	};

	public TextView.OnEditorActionListener sendtext = new TextView.OnEditorActionListener(){


		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEND)
			{
				mReportDetialPresenter.senderReply();

			}

			return true;
		}


	};
}
