package intersky.notice.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import intersky.notice.R;
import intersky.notice.asks.NoticeAsks;
import intersky.notice.entity.Notice;
import intersky.notice.handler.NoticeHandler;
import intersky.notice.receicer.NoticeReceiver;
import intersky.notice.view.activity.NewNoticeActivity;
import intersky.notice.view.activity.NoticeActivity;
import intersky.notice.view.activity.NoticeDetialActivity;
import intersky.notice.view.adapter.LoderPageAdapter;
import intersky.notice.view.adapter.NoticeAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;


public class NoticePresenter implements Presenter {


	public NoticeActivity mNoticeActivity;
	public NoticeHandler mNoticeHandler;

	public NoticePresenter(NoticeActivity mNoticeActivity) {
		this.mNoticeActivity = mNoticeActivity;
		this.mNoticeHandler = new NoticeHandler(mNoticeActivity);
		mNoticeActivity.setBaseReceiver(new NoticeReceiver(mNoticeHandler));
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mNoticeActivity.setContentView(R.layout.activity_notice_list);
		mNoticeActivity.mshada = (RelativeLayout) mNoticeActivity.findViewById(R.id.shade);
		ToolBarHelper.setTitle(mNoticeActivity.mActionBar, mNoticeActivity.getString(R.string.keyword_system));
		mNoticeActivity.mViewPager = (NoScrollViewPager) mNoticeActivity.findViewById(R.id.load_pager);
		mNoticeActivity.mLefttTeb = (RelativeLayout) mNoticeActivity.findViewById(R.id.day);
		mNoticeActivity.mRightTeb = (RelativeLayout) mNoticeActivity.findViewById(R.id.month);
		mNoticeActivity.mLine1 = (RelativeLayout) mNoticeActivity.findViewById(R.id.line13);
		mNoticeActivity.mLine3 = (RelativeLayout) mNoticeActivity.findViewById(R.id.line33);
		mNoticeActivity.mLine11 = (RelativeLayout) mNoticeActivity.findViewById(R.id.line12);
		mNoticeActivity.mLine31 = (RelativeLayout) mNoticeActivity.findViewById(R.id.line32);
		mNoticeActivity.mLefttImg = (TextView) mNoticeActivity.findViewById(R.id.daytxt);
		mNoticeActivity.mRightImg = (TextView) mNoticeActivity.findViewById(R.id.monthtxt);
		mNoticeActivity.mCteat = (TextView) mNoticeActivity.findViewById(R.id.creatnotice);
		mNoticeActivity.mCteat.setOnClickListener(mNoticeActivity.mCreatListener);
		View mView1 = null;
		View mView3 = null;
		mView1 = mNoticeActivity.getLayoutInflater().inflate(R.layout.noticepager, null);
		mNoticeActivity.mPullToRefreshView1 = (PullToRefreshView) mView1
				.findViewById(R.id.task_pull_refresh_view);
		mNoticeActivity.mMyList = (ListView) mView1.findViewById(R.id.busines_List);
		mNoticeActivity.mMyList.setOnItemClickListener(mNoticeActivity.clickAdapterListener);
		mView3 = mNoticeActivity.getLayoutInflater().inflate(R.layout.noticepager, null);
		mNoticeActivity.mPullToRefreshView3 = (PullToRefreshView) mView3
				.findViewById(R.id.task_pull_refresh_view);
		mNoticeActivity.mCopyList = (ListView) mView3.findViewById(R.id.busines_List);
		mNoticeActivity.mCopyList.setOnItemClickListener(mNoticeActivity.clickAdapterListener);
		mNoticeActivity.mViews.add(mView1);
		mNoticeActivity.mViews.add(mView3);
		mNoticeActivity.mLoderPageAdapter = new LoderPageAdapter(mNoticeActivity.mViews);
		mNoticeActivity.mViewPager.setAdapter(mNoticeActivity.mLoderPageAdapter);
		mNoticeActivity.mViewPager.setNoScroll(true);
		mNoticeActivity.mLefttTeb.setOnClickListener(mNoticeActivity.leftClickListener);
		mNoticeActivity.mRightTeb.setOnClickListener(mNoticeActivity.rightClickListener);

		mNoticeActivity.mPullToRefreshView1.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
		mNoticeActivity.mPullToRefreshView1.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
		mNoticeActivity.mPullToRefreshView3.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
		mNoticeActivity.mPullToRefreshView3.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
		mNoticeActivity.mPullToRefreshView1.setOnHeaderRefreshListener(mNoticeActivity);
		mNoticeActivity.mPullToRefreshView1.setOnFooterRefreshListener(mNoticeActivity);
		mNoticeActivity.mPullToRefreshView3.setOnHeaderRefreshListener(mNoticeActivity);
		mNoticeActivity.mPullToRefreshView3.setOnFooterRefreshListener(mNoticeActivity);
		initData();

	}

	@Override
	public void Create() {
		// TODO Auto-generated method stub
		initView();
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		mNoticeHandler = null;
	}

	public void creat()
	{
		Intent intent = new Intent(mNoticeActivity, NewNoticeActivity.class);
		Notice notice = new Notice();
		intent.putExtra("notice",notice);
		mNoticeActivity.startActivity(intent);
	}

	public void showLeft() {

		mNoticeActivity.mViewPager.setCurrentItem(0);
		mNoticeActivity.mLine1.setBackgroundColor(Color.rgb(98,153,243));
		mNoticeActivity.mLine3.setBackgroundColor(Color.rgb(242,242,242));
		mNoticeActivity.mLine1.setVisibility(View.VISIBLE);
		mNoticeActivity.mLine3.setVisibility(View.INVISIBLE);
		mNoticeActivity.mRightImg.setTextColor(Color.rgb(0,0,0));
		mNoticeActivity.mLefttImg.setTextColor(Color.rgb(98,153,243));
		mNoticeActivity.mPullToRefreshView1.onFooterRefreshComplete();
	}

	public void showRight() {

		mNoticeActivity.mViewPager.setCurrentItem(1);
		mNoticeActivity.mLine1.setBackgroundColor(Color.rgb(242,242,242));
		mNoticeActivity.mLine3.setBackgroundColor(Color.rgb(98,153,243));
		mNoticeActivity.mLine1.setVisibility(View.INVISIBLE);
		mNoticeActivity.mLine3.setVisibility(View.VISIBLE);
		mNoticeActivity.mRightImg.setTextColor(Color.rgb(98,153,243));
		mNoticeActivity.mLefttImg.setTextColor(Color.rgb(0,0,0));
		mNoticeActivity.mPullToRefreshView3.onFooterRefreshComplete();


	}

	public void initData()
	{
		mNoticeActivity.mReadNoticeAdapter = new NoticeAdapter(mNoticeActivity.mReadNotices, mNoticeActivity);
		mNoticeActivity.mUnReadeAdapter = new NoticeAdapter(mNoticeActivity.mUnReadNotices, mNoticeActivity);
		mNoticeActivity.mMyList.setAdapter(mNoticeActivity.mUnReadeAdapter);
		mNoticeActivity.mCopyList.setAdapter(mNoticeActivity.mReadNoticeAdapter);
		showLeft();
		getNotice();
	}


	public void getNotice()
	{
		if(mNoticeActivity.mViewPager.getCurrentItem() == 0)
		{
			if(mNoticeActivity.mUnReadeAdapter.getCount() >= mNoticeActivity.mUnReadeAdapter.totalCount && mNoticeActivity.mUnReadeAdapter.totalCount != -1)
			{
				AppUtils.showMessage(mNoticeActivity,mNoticeActivity.getString(R.string.add_all_item));
				return;
			}
			else
			{
				mNoticeActivity.waitDialog.show();
				NoticeAsks.getNoticesList(mNoticeActivity,mNoticeHandler,0,mNoticeActivity.mUnReadeAdapter.nowpage);
			}
		}
		else
		{
			if(mNoticeActivity.mReadNoticeAdapter.getCount() >= mNoticeActivity.mReadNoticeAdapter.totalCount && mNoticeActivity.mReadNoticeAdapter.totalCount != -1)
			{
				AppUtils.showMessage(mNoticeActivity,mNoticeActivity.getString(R.string.add_all_item));
				return;
			}
			else
			{
				mNoticeActivity.waitDialog.show();
				NoticeAsks.getNoticesList(mNoticeActivity,mNoticeHandler,1,mNoticeActivity.mReadNoticeAdapter.nowpage);
			}

		}

	}

	public void onfoot()
	{
		getNotice();
	}

	public void onhead()
	{
		if(mNoticeActivity.mViewPager.getCurrentItem() == 1)
		{
			mNoticeActivity.mReadNoticeAdapter.totalCount = -1;
			mNoticeActivity.mReadNoticeAdapter.nowpage = 1;
			mNoticeActivity.mReadNoticeAdapter.getList().clear();
			mNoticeActivity.mReadNoticeAdapter.notifyDataSetChanged();
		}
		else
		{
			mNoticeActivity.mUnReadeAdapter.totalCount = -1;
			mNoticeActivity.mUnReadeAdapter.nowpage = 1;
			mNoticeActivity.mUnReadeAdapter.getList().clear();
			mNoticeActivity.mUnReadeAdapter.notifyDataSetChanged();
		}
		getNotice();
	}



//10000*y*n1*n2*(1-a)
//10000*y*n1*n2*(1-c)dayci
	public void onitemcick(Notice mNotice)
	{
		Intent intent = new Intent(mNoticeActivity, NoticeDetialActivity.class);
		intent.putExtra("notice",mNotice);
		mNoticeActivity.startActivity(intent);
	}

	public void updataAll()
	{

		if(mNoticeActivity.mReadNotices.size() > 0) {
			mNoticeActivity.mReadNoticeAdapter.endPage = mNoticeActivity.mReadNoticeAdapter.nowpage;
			mNoticeActivity.mReadNoticeAdapter.nowpage = 1;
			mNoticeActivity.mReadNoticeAdapter.totalCount = -1;
			mNoticeActivity.mReadNotices.clear();
			NoticeAsks.getNoticesList(mNoticeActivity,mNoticeHandler,1,mNoticeActivity.mReadNoticeAdapter.nowpage);
		}

		if(mNoticeActivity.mUnReadNotices.size() > 0) {
			mNoticeActivity.mUnReadeAdapter.endPage = mNoticeActivity.mUnReadeAdapter.nowpage;
			mNoticeActivity.mUnReadeAdapter.nowpage = 1;
			mNoticeActivity.mUnReadeAdapter.totalCount = -1;
			mNoticeActivity.mUnReadNotices.clear();
			NoticeAsks.getNoticesListList(mNoticeActivity,mNoticeHandler,0,mNoticeActivity.mReadNoticeAdapter.nowpage);
		}
	}
}
