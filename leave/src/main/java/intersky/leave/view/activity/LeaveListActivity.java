package intersky.leave.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.leave.entity.Leave;
import intersky.leave.presenter.LeaveListPresenter;
import intersky.leave.view.adapter.LeaveAdapter;
import intersky.leave.view.adapter.LoderPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;


@SuppressLint("ClickableViewAccessibility")
public class LeaveListActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener
{

	public static final String ACTION_LEAVE_SET_CONTACTS = "ACTION_LEAVE_SET_CONTACTS";
	public NoScrollViewPager mViewPager;
	public RelativeLayout mRightTeb;
	public RelativeLayout mMiddeleTeb;
	public RelativeLayout mLefttTeb;

	public ListView mMyList;
	public ListView mApproveList;
	public ListView mCopyList;
	public TextView mRightImg;
	public TextView mMiddleImg;
	public TextView mLefttImg;
	public SearchViewLayout mSearchViewLayout;
	public ArrayList<View> mViews = new ArrayList<View>();
	public ArrayList<Leave> mMyLeaves = new ArrayList<Leave>();
	public ArrayList<Leave> mApproveLeaves = new ArrayList<Leave>();
	public ArrayList<Leave> mCopyLeaves = new ArrayList<Leave>();
	public LeaveAdapter myLeaveAdapter;
	public LeaveAdapter approveLeaveAdapter;
	public LeaveAdapter copyLeaveAdapter;
	public LeaveAdapter mysLeaveAdapter;
	public LeaveAdapter approvesLeaveAdapter;
	public LeaveAdapter copysLeaveAdapter;
	public ArrayList<Leave> mMysLeaves = new ArrayList<Leave>();
	public ArrayList<Leave> mApprovesLeaves = new ArrayList<Leave>();
	public ArrayList<Leave> mCopysLeaves = new ArrayList<Leave>();
	public LoderPageAdapter mLoderPageAdapter;
	public LeaveListPresenter mLeaveListPresenter = new LeaveListPresenter(this);
	public PullToRefreshView mPullToRefreshView1;
	public PullToRefreshView mPullToRefreshView2;
	public PullToRefreshView mPullToRefreshView3;
	public TextView hit1;
	public TextView hit2;
	public TextView hit3;
	public String stext1 = "";
	public String stext2 = "";
	public String stext3 = "";
	public PopupWindow popupWindow1;
	public RelativeLayout mshada;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mLeaveListPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mLeaveListPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mLeaveListPresenter.Start();
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
		mLeaveListPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mLeaveListPresenter.Resume();
	}

	public View.OnClickListener leftClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mLeaveListPresenter.showLeft();
			if(mMyLeaves.size() == 0)
			mLeaveListPresenter.getLeave();
		}
	};

	public View.OnClickListener middleClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			mLeaveListPresenter.showMiddle();
			if(mApproveLeaves.size() == 0)
			mLeaveListPresenter.getLeave();
		}
	};

	public View.OnClickListener creatListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			mLeaveListPresenter.creat();
		}
	};

	public View.OnClickListener rightClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mLeaveListPresenter.showRight();
			if(mCopyLeaves.size() == 0)
				mLeaveListPresenter.getLeave();
		}
	};

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mLeaveListPresenter.onfoot();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mLeaveListPresenter.onhead();
	}

	public AdapterView.OnItemClickListener clickAdapterListener = new AdapterView.OnItemClickListener()
	{


		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mLeaveListPresenter.onitemcick((Leave) parent.getAdapter().getItem(position));
		}
	};

	public TextWatcher mTextchange = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if(mSearchViewLayout.getText().toString().length()> 0)
			{
				mSearchViewLayout.showEdit();
			}
			else
			{
				mSearchViewLayout.hidEdit();
				mLeaveListPresenter.onSearch();
			}


		}
	};


	public View.OnClickListener mShowMore = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeaveListPresenter.showButtom();
		}

	};

	public View.OnClickListener mShowMy = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeaveListPresenter.showMy();
		}

	};

	public View.OnClickListener mShowOther = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLeaveListPresenter.showOther();
		}

	};


	public TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener()
	{

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			// TODO Auto-generated method stub
			if (actionId == EditorInfo.IME_ACTION_SEARCH)
			{
				mLeaveListPresenter.onSearch();

			}
			return true;
		}
	};

	public AbsListView.OnScrollListener mscoll = new AbsListView.OnScrollListener()
	{

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			mSearchViewLayout.hidEdit();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}
	};


}
