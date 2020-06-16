package intersky.workreport.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import intersky.workreport.R;
import intersky.workreport.WorkReportManager;
import intersky.workreport.asks.WorkReportAsks;
import intersky.workreport.entity.Report;
import intersky.workreport.handler.ReportListHandler;
import intersky.workreport.receiver.ReportListReceiver;
import intersky.workreport.view.activity.ReportDetialActivity;
import intersky.workreport.view.activity.ReportListActivity;
import intersky.workreport.view.adapter.LoderPageAdapter;
import intersky.workreport.view.adapter.ReportAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;


public class ReportListPresenter implements Presenter {


    public ReportListActivity mReportListActivity;
    public ReportListHandler mWorkReportHandler;

    public ReportListPresenter(ReportListActivity mReportListActivity) {
        this.mReportListActivity = mReportListActivity;
        this.mWorkReportHandler = new ReportListHandler(mReportListActivity);
        mReportListActivity.setBaseReceiver(new ReportListReceiver(mWorkReportHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mReportListActivity.setContentView(R.layout.activity_report_list);
        if (mReportListActivity.getIntent().getIntExtra("ntype", 0) == ReportListActivity.TYPE_RECEIVE)
            ToolBarHelper.setTitle(mReportListActivity.mActionBar, mReportListActivity.getString(R.string.xml_workreport_my_receive));
        if (mReportListActivity.getIntent().getIntExtra("ntype", 0) == ReportListActivity.TYPE_COPY)
            ToolBarHelper.setTitle(mReportListActivity.mActionBar, mReportListActivity.getString(R.string.xml_workreport_my_join));
        if (mReportListActivity.getIntent().getIntExtra("ntype", 0) == ReportListActivity.TYPE_SEND)
            ToolBarHelper.setTitle(mReportListActivity.mActionBar, mReportListActivity.getString(R.string.xml_workreport_my_send));
        mReportListActivity.mshade = mReportListActivity.findViewById(R.id.shade);
        ToolBarHelper.setRightBtnText(mReportListActivity.mActionBar,mReportListActivity.editListener,mReportListActivity.getString(R.string.button_edit));
        mReportListActivity.mSearchViewLayout = (SearchViewLayout) mReportListActivity.findViewById(R.id.top_layer);
        mReportListActivity.mSearchViewLayout.mSetOnSearchListener(mReportListActivity.mOnEditorActionListener);
        mReportListActivity.hit1 = (TextView) mReportListActivity.findViewById(R.id.daytxt);
        mReportListActivity.hit2 = (TextView) mReportListActivity.findViewById(R.id.weektxt);
        mReportListActivity.hit3 = (TextView) mReportListActivity.findViewById(R.id.monthtxt);
        mReportListActivity.buttom = mReportListActivity.findViewById(R.id.buttomlayer);
//        mReportListActivity.delete = mReportListActivity.findViewById(R.id.delete);
        mReportListActivity.read = mReportListActivity.findViewById(R.id.read);
        mReportListActivity.mViewPager = (NoScrollViewPager) mReportListActivity.findViewById(R.id.load_pager);
        mReportListActivity.mLefttTeb = (RelativeLayout) mReportListActivity.findViewById(R.id.day);
        mReportListActivity.mMiddeleTeb = (RelativeLayout) mReportListActivity.findViewById(R.id.week);
        mReportListActivity.mRightTeb = (RelativeLayout) mReportListActivity.findViewById(R.id.month);
        mReportListActivity.mLine1 = (RelativeLayout) mReportListActivity.findViewById(R.id.line13);
        mReportListActivity.mLine2 = (RelativeLayout) mReportListActivity.findViewById(R.id.line23);
        mReportListActivity.mLine3 = (RelativeLayout) mReportListActivity.findViewById(R.id.line33);
        mReportListActivity.mLine11 = (RelativeLayout) mReportListActivity.findViewById(R.id.line12);
        mReportListActivity.mLine21 = (RelativeLayout) mReportListActivity.findViewById(R.id.line22);
        mReportListActivity.mLine31 = (RelativeLayout) mReportListActivity.findViewById(R.id.line32);
        mReportListActivity.mLefttImg = (TextView) mReportListActivity.findViewById(R.id.daytxt);
        mReportListActivity.mMiddleImg = (TextView) mReportListActivity.findViewById(R.id.weektxt);
        mReportListActivity.mRightImg = (TextView) mReportListActivity.findViewById(R.id.monthtxt);
        View mView1 = null;
        View mView2 = null;
        View mView3 = null;
        mView1 = mReportListActivity.getLayoutInflater().inflate(R.layout.reportpager, null);
        mReportListActivity.mPullToRefreshView1 = (PullToRefreshView) mView1
                .findViewById(R.id.task_pull_refresh_view);
        mReportListActivity.mDayList = (ListView) mView1.findViewById(R.id.busines_List);
        mReportListActivity.mDayList.setOnItemClickListener(mReportListActivity.clickAdapterListener);
        mReportListActivity.mDayList.setOnScrollListener(mReportListActivity.mscoll);
        mView2 = mReportListActivity.getLayoutInflater().inflate(R.layout.reportpager, null);
        mReportListActivity.mPullToRefreshView2 = (PullToRefreshView) mView2
                .findViewById(R.id.task_pull_refresh_view);
        mReportListActivity.mWeekList = (ListView) mView2.findViewById(R.id.busines_List);
        mReportListActivity.mWeekList.setOnItemClickListener(mReportListActivity.clickAdapterListener);
        mReportListActivity.mWeekList.setOnScrollListener(mReportListActivity.mscoll);
        mView3 = mReportListActivity.getLayoutInflater().inflate(R.layout.reportpager, null);
        mReportListActivity.mPullToRefreshView3 = (PullToRefreshView) mView3
                .findViewById(R.id.task_pull_refresh_view);
        mReportListActivity.mMonthList = (ListView) mView3.findViewById(R.id.busines_List);
        mReportListActivity.mMonthList.setOnItemClickListener(mReportListActivity.clickAdapterListener);
        mReportListActivity.mMonthList.setOnScrollListener(mReportListActivity.mscoll);
        mReportListActivity.mViews.add(mView1);
        mReportListActivity.mViews.add(mView2);
        mReportListActivity.mViews.add(mView3);
        mReportListActivity.mLoderPageAdapter = new LoderPageAdapter(mReportListActivity.mViews);
        mReportListActivity.mViewPager.setAdapter(mReportListActivity.mLoderPageAdapter);
        mReportListActivity.mViewPager.setNoScroll(true);
        mReportListActivity.mLefttTeb.setOnClickListener(mReportListActivity.leftClickListener);
        mReportListActivity.mMiddeleTeb.setOnClickListener(mReportListActivity.middleClickListener);
        mReportListActivity.mRightTeb.setOnClickListener(mReportListActivity.rightClickListener);
        if(mReportListActivity.getIntent().getIntExtra("ntype", 0) == ReportListActivity.TYPE_RECEIVE ||
                mReportListActivity.getIntent().getIntExtra("ntype", 0) == ReportListActivity.TYPE_COPY)
        {
            mReportListActivity.read.setTextColor(0xFF0082FF);
            mReportListActivity.read.setText(mReportListActivity.getString(R.string.button_set_read));
            mReportListActivity.read.setOnClickListener(mReportListActivity.readListener);
        }
        else
        {
            mReportListActivity.read.setTextColor(0xFFFF0000);
            mReportListActivity.read.setText(mReportListActivity.getString(R.string.button_delete));
            mReportListActivity.read.setOnClickListener(mReportListActivity.deleteListener);
        }


        mReportListActivity.mReporttype = mReportListActivity.getIntent().getIntExtra("ntype", ReportListActivity.TYPE_RECEIVE);
        mReportListActivity.mPullToRefreshView1.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        mReportListActivity.mPullToRefreshView1.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        mReportListActivity.mPullToRefreshView2.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        mReportListActivity.mPullToRefreshView2.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        mReportListActivity.mPullToRefreshView3.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        mReportListActivity.mPullToRefreshView3.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        mReportListActivity.mPullToRefreshView1.setOnHeaderRefreshListener(mReportListActivity);
        mReportListActivity.mPullToRefreshView1.setOnFooterRefreshListener(mReportListActivity);
        mReportListActivity.mPullToRefreshView2.setOnHeaderRefreshListener(mReportListActivity);
        mReportListActivity.mPullToRefreshView2.setOnFooterRefreshListener(mReportListActivity);
        mReportListActivity.mPullToRefreshView3.setOnHeaderRefreshListener(mReportListActivity);
        mReportListActivity.mPullToRefreshView3.setOnFooterRefreshListener(mReportListActivity);
        WorkReportManager.getInstance().upDataHit(mReportListActivity);
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
        mWorkReportHandler = null;
    }

    public void doClean() {
        mReportListActivity.mSearchViewLayout.getText();
    }

    public void onSearch() {
        if (mReportListActivity.mSearchViewLayout.getText().length() == 0) {
            switch (mReportListActivity.mViewPager.getCurrentItem()) {
                case 0:
                    mReportListActivity.stext1 = mReportListActivity.mSearchViewLayout.getText().toString();
                    mReportListActivity.mDayList.setAdapter(mReportListActivity.dayReportAdapter);
                    break;
                case 1:
                    mReportListActivity.stext2 = mReportListActivity.mSearchViewLayout.getText().toString();
                    mReportListActivity.mWeekList.setAdapter(mReportListActivity.weekReportAdapter);
                    break;
                case 2:
                    mReportListActivity.stext3 = mReportListActivity.mSearchViewLayout.getText().toString();
                    mReportListActivity.mMonthList.setAdapter(mReportListActivity.monthReportAdapter);
                    break;
            }

        } else {
            switch (mReportListActivity.mViewPager.getCurrentItem()) {
                case 0:
                    mReportListActivity.stext1 = mReportListActivity.mSearchViewLayout.getText().toString();
                    getSearchItem(mReportListActivity.mDayReports, mReportListActivity.mDaysReports);
                    mReportListActivity.mDayList.setAdapter(mReportListActivity.daysReportAdapter);
                    break;
                case 1:
                    mReportListActivity.stext2 = mReportListActivity.mSearchViewLayout.getText().toString();
                    getSearchItem(mReportListActivity.mWeekReports, mReportListActivity.mWeeksReports);
                    mReportListActivity.mWeekList.setAdapter(mReportListActivity.weeksReportAdapter);
                    break;
                case 2:
                    mReportListActivity.stext3 = mReportListActivity.mSearchViewLayout.getText().toString();
                    getSearchItem(mReportListActivity.mMonthReports, mReportListActivity.mMonthsReports);
                    mReportListActivity.mMonthList.setAdapter(mReportListActivity.monthsReportAdapter);
                    break;
            }
        }
    }

    public void getSearchItem(ArrayList<Report> mreports, ArrayList<Report> mreports2) {
        mreports2.clear();
        for (int i = 0; i < mreports.size(); i++) {
            Report mReport = mreports.get(i);
            if (mReport.userName.contains(mReportListActivity.mSearchViewLayout.getText())) {
                mreports2.add(mReport);
            }
        }
    }

    public void showLeft() {

        mReportListActivity.mViewPager.setCurrentItem(0);
        mReportListActivity.mLine1.setBackgroundColor(Color.rgb(98, 153, 243));
        mReportListActivity.mLine2.setBackgroundColor(Color.rgb(242, 242, 242));
        mReportListActivity.mLine3.setBackgroundColor(Color.rgb(242, 242, 242));
        mReportListActivity.mLine1.setVisibility(View.VISIBLE);
        mReportListActivity.mLine2.setVisibility(View.INVISIBLE);
        mReportListActivity.mLine3.setVisibility(View.INVISIBLE);
        mReportListActivity.mRightImg.setTextColor(Color.rgb(0, 0, 0));
        mReportListActivity.mMiddleImg.setTextColor(Color.rgb(0, 0, 0));
        mReportListActivity.mLefttImg.setTextColor(Color.rgb(98, 153, 243));
        mReportListActivity.mPullToRefreshView1.onFooterRefreshComplete();
    }

    public void showMiddle() {

        mReportListActivity.mViewPager.setCurrentItem(1);
        mReportListActivity.mLine1.setBackgroundColor(Color.rgb(242, 242, 242));
        mReportListActivity.mLine2.setBackgroundColor(Color.rgb(98, 153, 243));
        mReportListActivity.mLine3.setBackgroundColor(Color.rgb(242, 242, 242));
        mReportListActivity.mLine1.setVisibility(View.INVISIBLE);
        mReportListActivity.mLine2.setVisibility(View.VISIBLE);
        mReportListActivity.mLine3.setVisibility(View.INVISIBLE);
        mReportListActivity.mRightImg.setTextColor(Color.rgb(0, 0, 0));
        mReportListActivity.mMiddleImg.setTextColor(Color.rgb(98, 153, 243));
        mReportListActivity.mLefttImg.setTextColor(Color.rgb(0, 0, 0));
        mReportListActivity.mPullToRefreshView2.onFooterRefreshComplete();

    }

    public void showRight() {

        mReportListActivity.mViewPager.setCurrentItem(2);
        mReportListActivity.mLine1.setBackgroundColor(Color.rgb(242, 242, 242));
        mReportListActivity.mLine2.setBackgroundColor(Color.rgb(242, 242, 242));
        mReportListActivity.mLine3.setBackgroundColor(Color.rgb(98, 153, 243));
        mReportListActivity.mLine1.setVisibility(View.INVISIBLE);
        mReportListActivity.mLine2.setVisibility(View.INVISIBLE);
        mReportListActivity.mLine3.setVisibility(View.VISIBLE);
        mReportListActivity.mRightImg.setTextColor(Color.rgb(98, 153, 243));
        mReportListActivity.mMiddleImg.setTextColor(Color.rgb(0, 0, 0));
        mReportListActivity.mLefttImg.setTextColor(Color.rgb(0, 0, 0));
        mReportListActivity.mPullToRefreshView3.onFooterRefreshComplete();


    }

    public void initData() {
        mReportListActivity.dayReportAdapter = new ReportAdapter(mReportListActivity.mDayReports, mReportListActivity);
        mReportListActivity.weekReportAdapter = new ReportAdapter(mReportListActivity.mWeekReports, mReportListActivity);
        mReportListActivity.monthReportAdapter = new ReportAdapter(mReportListActivity.mMonthReports, mReportListActivity);
        mReportListActivity.daysReportAdapter = new ReportAdapter(mReportListActivity.mDaysReports, mReportListActivity);
        mReportListActivity.weeksReportAdapter = new ReportAdapter(mReportListActivity.mWeeksReports, mReportListActivity);
        mReportListActivity.monthsReportAdapter = new ReportAdapter(mReportListActivity.mMonthsReports, mReportListActivity);
        mReportListActivity.mDayList.setAdapter(mReportListActivity.dayReportAdapter);
        mReportListActivity.mWeekList.setAdapter(mReportListActivity.weekReportAdapter);
        mReportListActivity.mMonthList.setAdapter(mReportListActivity.monthReportAdapter);
        showLeft();
        getReport();
    }

    public void getReport() {
        int page = 0;
        if (mReportListActivity.mViewPager.getCurrentItem() == 0) {
            if (mReportListActivity.dayReportAdapter.getCount() >= mReportListActivity.dayReportAdapter.totalCount && mReportListActivity.dayReportAdapter.totalCount != -1) {
                return;
            }
            page = mReportListActivity.dayReportAdapter.nowpage;
        } else if (mReportListActivity.mViewPager.getCurrentItem() == 1) {
            if (mReportListActivity.weekReportAdapter.getCount() >= mReportListActivity.weekReportAdapter.totalCount && mReportListActivity.weekReportAdapter.totalCount != -1) {
                return;
            }
            page = mReportListActivity.weekReportAdapter.nowpage;
        } else {
            if (mReportListActivity.monthReportAdapter.getCount() >= mReportListActivity.monthReportAdapter.totalCount && mReportListActivity.monthReportAdapter.totalCount != -1) {
                return;
            }
            page = mReportListActivity.monthReportAdapter.nowpage;
        }
        WorkReportAsks.getReport(mReportListActivity, mWorkReportHandler, mReportListActivity.mViewPager.getCurrentItem() + 1, mReportListActivity.mReporttype, page);
        mReportListActivity.waitDialog.show();
    }


    public void onfoot() {

        if (mReportListActivity.mViewPager.getCurrentItem() == 0) {

            mReportListActivity.mPullToRefreshView1.onFooterRefreshComplete();
            if (mReportListActivity.dayReportAdapter.getCount() >= mReportListActivity.dayReportAdapter.totalCount && mReportListActivity.dayReportAdapter.totalCount != -1) {
                AppUtils.showMessage(mReportListActivity, mReportListActivity.getString(R.string.keyword_lodingfinish));
                return;
            }
        } else if (mReportListActivity.mViewPager.getCurrentItem() == 1) {

            mReportListActivity.mPullToRefreshView2.onFooterRefreshComplete();
            if (mReportListActivity.weekReportAdapter.getCount() >= mReportListActivity.weekReportAdapter.totalCount && mReportListActivity.weekReportAdapter.totalCount != -1) {
                AppUtils.showMessage(mReportListActivity, mReportListActivity.getString(R.string.keyword_lodingfinish));
                return;
            }
        } else {
            mReportListActivity.mPullToRefreshView3.onFooterRefreshComplete();
            if (mReportListActivity.monthReportAdapter.getCount() >= mReportListActivity.monthReportAdapter.totalCount && mReportListActivity.monthReportAdapter.totalCount != -1) {
                AppUtils.showMessage(mReportListActivity, mReportListActivity.getString(R.string.keyword_lodingfinish));
                return;
            }
        }
        getReport();
    }

    public void onhead() {
        if (mReportListActivity.mViewPager.getCurrentItem() == 0) {
            mReportListActivity.dayReportAdapter.totalCount = -1;
            mReportListActivity.dayReportAdapter.nowpage = 1;
            mReportListActivity.dayReportAdapter.getmReports().clear();
            mReportListActivity.dayReportAdapter.notifyDataSetChanged();
            mReportListActivity.mPullToRefreshView1.onHeaderRefreshComplete();
        } else if (mReportListActivity.mViewPager.getCurrentItem() == 1) {
            mReportListActivity.weekReportAdapter.totalCount = -1;
            mReportListActivity.weekReportAdapter.nowpage = 1;
            mReportListActivity.weekReportAdapter.getmReports().clear();
            mReportListActivity.weekReportAdapter.notifyDataSetChanged();
            mReportListActivity.mPullToRefreshView2.onHeaderRefreshComplete();
        } else {
            mReportListActivity.monthReportAdapter.totalCount = -1;
            mReportListActivity.monthReportAdapter.nowpage = 1;
            mReportListActivity.monthReportAdapter.getmReports().clear();
            mReportListActivity.monthReportAdapter.notifyDataSetChanged();
            mReportListActivity.mPullToRefreshView3.onHeaderRefreshComplete();
        }
        getReport();
    }


    public void onitemcick(Report mReport) {
        if(mReportListActivity.edit == false)
        {
            Intent intent = new Intent(mReportListActivity, ReportDetialActivity.class);
            intent.putExtra("report", mReport);
            mReportListActivity.startActivity(intent);
        }
        else
        {
            if(mReport.select == false)
            {
                mReport.select = true;
                mReportListActivity.selectReports.add(mReport);
            }
            else
            {
                mReport.select = false;
                mReportListActivity.selectReports.remove(mReport);
            }
            switch (mReportListActivity.mViewPager.getCurrentItem() ) {
                case 0:
                    mReportListActivity.dayReportAdapter.notifyDataSetChanged();
                    mReportListActivity.daysReportAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    mReportListActivity.weekReportAdapter.notifyDataSetChanged();
                    mReportListActivity.weeksReportAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    mReportListActivity.monthReportAdapter.notifyDataSetChanged();
                    mReportListActivity.monthsReportAdapter.notifyDataSetChanged();
                    break;

            }

        }



    }


    public void updataHit() {
        if (mReportListActivity.getIntent().getIntExtra("ntype", 0) == ReportListActivity.TYPE_RECEIVE) {
            if (WorkReportManager.getInstance().day1 > 0) {
                mReportListActivity.hit1.setText(mReportListActivity.getString(R.string.xml_workreport_dday) + "(" + String.valueOf(WorkReportManager.getInstance().day1) + ")");
            } else {
                mReportListActivity.hit1.setText(mReportListActivity.getString(R.string.xml_workreport_dday));
            }
            if (WorkReportManager.getInstance().week1 > 0) {
                mReportListActivity.hit2.setText(mReportListActivity.getString(R.string.xml_workreport_dweek) + "(" + String.valueOf(WorkReportManager.getInstance().week1) + ")");
            } else {
                mReportListActivity.hit2.setText(mReportListActivity.getString(R.string.xml_workreport_dweek));
            }
            if (WorkReportManager.getInstance().month1 > 0) {
                mReportListActivity.hit3.setText(mReportListActivity.getString(R.string.xml_workreport_dmonthy) + "(" + String.valueOf(WorkReportManager.getInstance().month1) + ")");
            } else {
                mReportListActivity.hit3.setText(mReportListActivity.getString(R.string.xml_workreport_dmonthy));
            }
        }
        if (mReportListActivity.getIntent().getIntExtra("ntype", 0) == ReportListActivity.TYPE_COPY) {
            if (WorkReportManager.getInstance().day2 > 0) {
                mReportListActivity.hit1.setText(mReportListActivity.getString(R.string.xml_workreport_dday) + "(" + String.valueOf(WorkReportManager.getInstance().day2) + ")");
            } else {
                mReportListActivity.hit1.setText(mReportListActivity.getString(R.string.xml_workreport_dday));
            }
            if (WorkReportManager.getInstance().week2 > 0) {
                mReportListActivity.hit2.setText(mReportListActivity.getString(R.string.xml_workreport_dweek) + "(" + String.valueOf(WorkReportManager.getInstance().week2) + ")");
            } else {
                mReportListActivity.hit2.setText(mReportListActivity.getString(R.string.xml_workreport_dweek));
            }
            if (WorkReportManager.getInstance().month2 > 0) {
                mReportListActivity.hit3.setText(mReportListActivity.getString(R.string.xml_workreport_dmonthy) + "(" + String.valueOf(WorkReportManager.getInstance().month2) + ")");
            } else {
                mReportListActivity.hit3.setText(mReportListActivity.getString(R.string.xml_workreport_dmonthy));
            }
        }

    }

    public void reportConversationUpdate() {
        mReportListActivity.dayReportAdapter.totalCount = -1;
        mReportListActivity.dayReportAdapter.nowpage = 1;
        mReportListActivity.dayReportAdapter.getmReports().clear();
        mReportListActivity.dayReportAdapter.notifyDataSetChanged();
        mReportListActivity.mPullToRefreshView1.onHeaderRefreshComplete();
        mReportListActivity.weekReportAdapter.totalCount = -1;
        mReportListActivity.weekReportAdapter.nowpage = 1;
        mReportListActivity.weekReportAdapter.getmReports().clear();
        mReportListActivity.weekReportAdapter.notifyDataSetChanged();
        mReportListActivity.mPullToRefreshView2.onHeaderRefreshComplete();
        mReportListActivity.monthReportAdapter.totalCount = -1;
        mReportListActivity.monthReportAdapter.nowpage = 1;
        mReportListActivity.monthReportAdapter.getmReports().clear();
        mReportListActivity.monthReportAdapter.notifyDataSetChanged();
        mReportListActivity.mPullToRefreshView3.onHeaderRefreshComplete();
        getReport();
    }

    public void updataAll() {
        if(mReportListActivity.dayReportAdapter.getmReports().size() > 0)
        {
            mReportListActivity.dayReportAdapter.totalCount = -1;
            mReportListActivity.dayReportAdapter.endPage = mReportListActivity.dayReportAdapter.nowpage;
            mReportListActivity.dayReportAdapter.nowpage = 1;
            mReportListActivity.dayReportAdapter.getmReports().clear();
            mReportListActivity.dayReportAdapter.notifyDataSetChanged();
            WorkReportAsks.getReportList(mReportListActivity,mWorkReportHandler, WorkReportManager.TYPE_DAY,mReportListActivity.mReporttype
                    ,mReportListActivity.dayReportAdapter.nowpage);
        }
        if(mReportListActivity.weekReportAdapter.getmReports().size() > 0)
        {
            mReportListActivity.weekReportAdapter.totalCount = -1;
            mReportListActivity.weekReportAdapter.endPage = mReportListActivity.weekReportAdapter.nowpage;
            mReportListActivity.weekReportAdapter.nowpage = 1;
            mReportListActivity.weekReportAdapter.getmReports().clear();
            mReportListActivity.weekReportAdapter.notifyDataSetChanged();
            WorkReportAsks.getReportList(mReportListActivity,mWorkReportHandler, WorkReportManager.TYPE_WEEK,mReportListActivity.mReporttype
                    ,mReportListActivity.weekReportAdapter.nowpage);
        }

        if(mReportListActivity.monthReportAdapter.getmReports().size() > 0)
        {
            mReportListActivity.monthReportAdapter.totalCount = -1;
            mReportListActivity.monthReportAdapter.endPage = mReportListActivity.monthReportAdapter.nowpage;
            mReportListActivity.monthReportAdapter.nowpage = 1;
            mReportListActivity.monthReportAdapter.getmReports().clear();
            mReportListActivity.monthReportAdapter.notifyDataSetChanged();
            WorkReportAsks.getReportList(mReportListActivity,mWorkReportHandler, WorkReportManager.TYPE_MONTH,mReportListActivity.mReporttype
                    ,mReportListActivity.monthReportAdapter.nowpage);
        }
    }

    public void setEdit()
    {
        if(mReportListActivity.edit == false)
        {
            mReportListActivity.edit = true;
            mReportListActivity.dayReportAdapter.edit = true;
            mReportListActivity.weekReportAdapter.edit = true;
            mReportListActivity.monthReportAdapter.edit = true;
            mReportListActivity.daysReportAdapter.edit = true;
            mReportListActivity.weeksReportAdapter.edit = true;
            mReportListActivity.monthsReportAdapter.edit = true;
            switch (mReportListActivity.mViewPager.getCurrentItem() ) {
                case 0:
                    mReportListActivity.dayReportAdapter.notifyDataSetChanged();
                    mReportListActivity.daysReportAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    mReportListActivity.weekReportAdapter.notifyDataSetChanged();
                    mReportListActivity.weeksReportAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    mReportListActivity.monthReportAdapter.notifyDataSetChanged();
                    mReportListActivity.monthsReportAdapter.notifyDataSetChanged();
                    break;

            }
            ToolBarHelper.setRightBtnText(mReportListActivity.mActionBar,mReportListActivity.editListener,mReportListActivity.getString(R.string.button_word_cancle));
            mReportListActivity.buttom.setVisibility(View.VISIBLE);
        }
    }

    public void hidEdit() {
        if(mReportListActivity.edit == true)
        {
            mReportListActivity.edit = false;
            mReportListActivity.dayReportAdapter.edit = false;
            mReportListActivity.weekReportAdapter.edit = false;
            mReportListActivity.monthReportAdapter.edit = false;
            mReportListActivity.daysReportAdapter.edit = false;
            mReportListActivity.weeksReportAdapter.edit = false;
            mReportListActivity.monthsReportAdapter.edit = false;
            switch (mReportListActivity.mViewPager.getCurrentItem() ) {
                case 0:
                    mReportListActivity.dayReportAdapter.notifyDataSetChanged();
                    mReportListActivity.daysReportAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    mReportListActivity.weekReportAdapter.notifyDataSetChanged();
                    mReportListActivity.weeksReportAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    mReportListActivity.monthReportAdapter.notifyDataSetChanged();
                    mReportListActivity.monthsReportAdapter.notifyDataSetChanged();
                    break;

            }
            cleanSelect();
            ToolBarHelper.setRightBtnText(mReportListActivity.mActionBar,mReportListActivity.editListener,mReportListActivity.getString(R.string.button_edit));
            mReportListActivity.buttom.setVisibility(View.GONE);
        }
    }

    public void cleanSelect() {
        for(int i = 0; i < mReportListActivity.selectReports.size() ; i++)
        {
            mReportListActivity.selectReports.get(i).select = false;
        }
        mReportListActivity.selectReports.clear();
        mReportListActivity.count = 0;
    }

    public void setRead() {
        mReportListActivity.waitDialog.show();
        boolean send = WorkReportAsks.setReadAll(mReportListActivity,mWorkReportHandler,mReportListActivity.selectReports);
        hidEdit();
        if(send == false)
        {
            mReportListActivity.waitDialog.hide();
        }
    }

    public void deleteAll(){
        if(mReportListActivity.selectReports.size() > 0)
        {
            mReportListActivity.waitDialog.show();
            WorkReportAsks.setDeleteAll(mReportListActivity,mWorkReportHandler,mReportListActivity.selectReports);
        }
        hidEdit();
    }

}
