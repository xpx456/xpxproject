package intersky.workreport.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import intersky.workreport.entity.Report;
import intersky.workreport.presenter.ReportListPresenter;
import intersky.workreport.view.adapter.LoderPageAdapter;
import intersky.workreport.view.adapter.ReportAdapter;


@SuppressLint("ClickableViewAccessibility")
public class ReportListActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener
{
	public static final int TYPE_RECEIVE = 1;
	public static final int TYPE_SEND = 3;
	public static final int TYPE_COPY = 2;
	public NoScrollViewPager mViewPager;
	public RelativeLayout mRightTeb;
	public RelativeLayout mMiddeleTeb;
	public RelativeLayout mLefttTeb;
	public RelativeLayout mLine1;
	public RelativeLayout mLine2;
	public RelativeLayout mLine3;
	public RelativeLayout mLine11;
	public RelativeLayout mLine21;
	public RelativeLayout mLine31;
	public ListView mDayList;
	public ListView mWeekList;
	public ListView mMonthList;
	public TextView mRightImg;
	public TextView mMiddleImg;
	public TextView mLefttImg;
	public SearchViewLayout mSearchViewLayout;
	public ArrayList<View> mViews = new ArrayList<View>();
	public ArrayList<Report> mDayReports = new ArrayList<Report>();
	public ArrayList<Report> mWeekReports = new ArrayList<Report>();
	public ArrayList<Report> mMonthReports = new ArrayList<Report>();
	public ReportAdapter dayReportAdapter;
	public ReportAdapter weekReportAdapter;
	public ReportAdapter monthReportAdapter;
	public ReportAdapter daysReportAdapter;
	public ReportAdapter weeksReportAdapter;
	public ReportAdapter monthsReportAdapter;
	public ArrayList<Report> mDaysReports = new ArrayList<Report>();
	public ArrayList<Report> mWeeksReports = new ArrayList<Report>();
	public ArrayList<Report> mMonthsReports = new ArrayList<Report>();
	public LoderPageAdapter mLoderPageAdapter;
	public ReportListPresenter mReportListPresenter = new ReportListPresenter(this);
	public PullToRefreshView mPullToRefreshView1;
	public PullToRefreshView mPullToRefreshView2;
	public PullToRefreshView mPullToRefreshView3;
	public String stext1 = "";
	public String stext2 = "";
	public String stext3 = "";
	public TextView hit1;
	public TextView hit2;
	public TextView hit3;
	public TextView read;
	public LinearLayout buttom;
	public int mReporttype = 1;
	public boolean edit = false;
	public PopupWindow popupWindow;
	public RelativeLayout mshade;
	public ArrayList<Report> selectReports = new ArrayList<Report>();
	public int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mReportListPresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mReportListPresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mReportListPresenter.Start();
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
		mReportListPresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mReportListPresenter.Resume();
	}

	public View.OnClickListener leftClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mReportListPresenter.showLeft();
			if(mDayReports.size() == 0)
			mReportListPresenter.getReport();
		}
	};

	public View.OnClickListener middleClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mReportListPresenter.showMiddle();
			if(mWeekReports.size() == 0)
			mReportListPresenter.getReport();
		}
	};

	public View.OnClickListener rightClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mReportListPresenter.showRight();
			if(mMonthReports.size() == 0)
			mReportListPresenter.getReport();
		}
	};

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mReportListPresenter.onfoot();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mReportListPresenter.onhead();
	}

	public AdapterView.OnItemClickListener clickAdapterListener = new AdapterView.OnItemClickListener()
	{


		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mReportListPresenter.onitemcick((Report) parent.getAdapter().getItem(position));
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
				mReportListPresenter.onSearch();

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

	public View.OnClickListener editListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(edit == false)
			{
				mReportListPresenter.setEdit();
			}
			else
			{
				mReportListPresenter.hidEdit();
			}
		}
	};

	public View.OnClickListener deleteListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mReportListPresenter.deleteAll();
		}
	};

	public View.OnClickListener readListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mReportListPresenter.setRead();
		}
	};

}
