package intersky.notice.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import intersky.notice.entity.Notice;
import intersky.notice.presenter.NoticePresenter;
import intersky.notice.view.adapter.LoderPageAdapter;
import intersky.notice.view.adapter.NoticeAdapter;


@SuppressLint("ClickableViewAccessibility")
public class NoticeActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener
{
	public NoScrollViewPager mViewPager;
	public RelativeLayout mRightTeb;
	public RelativeLayout mLefttTeb;
	public RelativeLayout mLine1;
	public RelativeLayout mLine3;
	public RelativeLayout mLine11;
	public RelativeLayout mLine31;
	public ListView mMyList;
	public ListView mCopyList;
	public TextView mRightImg;
	public TextView mLefttImg;
	public TextView mCteat;
	public ArrayList<View> mViews = new ArrayList<View>();
	public ArrayList<Notice> mReadNotices = new ArrayList<Notice>();
	public ArrayList<Notice> mUnReadNotices = new ArrayList<Notice>();
	public NoticeAdapter mReadNoticeAdapter;
	public NoticeAdapter mUnReadeAdapter;
	public LoderPageAdapter mLoderPageAdapter;
	public NoticePresenter mNoticePresenter = new NoticePresenter(this);
	public PullToRefreshView mPullToRefreshView1;
	public PullToRefreshView mPullToRefreshView3;
	public RelativeLayout mshada;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mNoticePresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mNoticePresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mNoticePresenter.Start();
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
		mNoticePresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mNoticePresenter.Resume();
	}

	public View.OnClickListener leftClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNoticePresenter.showLeft();
			if(mUnReadNotices.size() == 0)
			mNoticePresenter.getNotice();
		}
	};

	public View.OnClickListener creatListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			mNoticePresenter.creat();
		}
	};

	public View.OnClickListener rightClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mNoticePresenter.showRight();
			if(mReadNotices.size() == 0)
				mNoticePresenter.getNotice();
		}
	};

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mNoticePresenter.onfoot();
		view.onFooterRefreshComplete();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mNoticePresenter.onhead();
		view.onHeaderRefreshComplete();
	}

	public AdapterView.OnItemClickListener clickAdapterListener = new AdapterView.OnItemClickListener()
	{


		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mNoticePresenter.onitemcick((Notice) parent.getAdapter().getItem(position));
		}
	};

	public View.OnClickListener mCreatListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mNoticePresenter.creat();
		}
	};

}
