package intersky.vote.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.entity.Reply;
import intersky.mywidget.WebEdit;
import intersky.vote.entity.Vote;
import intersky.vote.entity.VoteSelect;
import intersky.vote.presenter.VoteDetialPresenter;


@SuppressLint("ClickableViewAccessibility")
public class VoteDetialActivity extends BaseActivity
{

	public RelativeLayout mAnsallLayer;
	public LinearLayout mVoteItemlayer;
	public LinearLayout mAnswerlayer;
	public PopupWindow popupWindow1;
	public ArrayList<String> durls = new ArrayList<String>();
	public ArrayList<VoteSelect> mSelectItemModels = new ArrayList<VoteSelect>();
    public ArrayList<Reply> mReplys= new ArrayList<Reply>();
	public TextView mTypeText;
	public TextView mNameType;
	public TextView mTime;
	public TextView mSubmit;
	public RelativeLayout mRelativeLayout;
	public TextView mContent;
	public TextView head;
	public TextView more;
	public PopupWindow popupWindow;
	public VoteDetialPresenter mVoteDetialPresenter = new VoteDetialPresenter(this);
	public TextView mAnswer;
	public EditText mEditTextContent;
	public Vote vote;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mVoteDetialPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mVoteDetialPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mVoteDetialPresenter.Start();
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
		mVoteDetialPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mVoteDetialPresenter.Resume();
	}

	public View.OnClickListener mSendListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVoteDetialPresenter.mdoVote();
		}
	};


	public View.OnClickListener mShowPicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVoteDetialPresenter.showPic( v);
		}
	};

	public View.OnClickListener mCloseListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			mVoteDetialPresenter.onClose();
		}

	};

	public View.OnClickListener mDeleteListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVoteDetialPresenter.onDelete();
		}

	};


	public View.OnClickListener mRecordListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVoteDetialPresenter.showRecord();
		}

	};

	public View.OnClickListener mMoreListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVoteDetialPresenter.showEdit();
		}

	};

	public TextView.OnEditorActionListener sendtext = new TextView.OnEditorActionListener() {


		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEND) {
				mVoteDetialPresenter.senderReply();

			}

			return true;
		}


	};

    public View.OnClickListener mDeleteReplyListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mVoteDetialPresenter.showDeleteReply((Reply) v.getTag());
        }

    };

	public View.OnClickListener mOnItemListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVoteDetialPresenter.clickVote(v);
		}

	};
}
