package intersky.sign.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import intersky.mywidget.PullToRefreshView;
import intersky.sign.entity.Sign;
import intersky.sign.presenter.StatisticsPresenter;
import intersky.sign.view.adapter.SignAdapter;


public class StatisticsActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

	public static final String ACTION_SIGN_SET_CONTACTS = "ACTION_SIGN_SET_CONTACTS";
	public ImageView mSetDate;
	public PopupWindow popupWindow1;
	public TextView mDate;
	public TextView hit;
	public TextView head;
	public ListView mItemList;
	public String startTime;
	public ArrayList<Sign> mAttendances = new ArrayList<Sign>();
	public SignAdapter mSignAdapter;
	public PullToRefreshView mPullToRefreshView;
	public StatisticsPresenter mStatisticsPresenter = new StatisticsPresenter(this);
	public RelativeLayout mshada;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStatisticsPresenter.Create();
	}
	
	public OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			mStatisticsPresenter.onItemClick(mAttendances.get(arg2));
		}

	};

	public View.OnClickListener dateListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mStatisticsPresenter.showTimeDialog();
		}
	};


	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mStatisticsPresenter.onFoot();
		mPullToRefreshView.onFooterRefreshComplete();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mStatisticsPresenter.onHead();
		mPullToRefreshView.onHeaderRefreshComplete();

	}

	public View.OnClickListener mMoreListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mStatisticsPresenter.showMore();
		}

	};

	public View.OnClickListener mShowMy = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mStatisticsPresenter.showMy();
		}

	};

	public View.OnClickListener mShowOther = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mStatisticsPresenter.showOther();
		}

	};

}
