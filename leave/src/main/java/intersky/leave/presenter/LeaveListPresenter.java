package intersky.leave.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.leave.LeaveManager;
import intersky.leave.R;
import intersky.leave.asks.LeaveAsks;
import intersky.leave.entity.Leave;
import intersky.leave.handler.LeaveListHandler;
import intersky.leave.receiver.LeaveListReceiver;
import intersky.leave.view.activity.LeaveActivity;
import intersky.leave.view.activity.LeaveDetialActivity;
import intersky.leave.view.activity.LeaveListActivity;
import intersky.leave.view.adapter.LeaveAdapter;
import intersky.leave.view.adapter.LoderPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import xpx.com.toolbar.utils.ToolBarHelper;


public class LeaveListPresenter implements Presenter {

    public LeaveListActivity mLeaveListActivity;
    public LeaveListHandler mLeaveListHandler;

    public LeaveListPresenter(LeaveListActivity mLeaveListActivity) {
        this.mLeaveListActivity = mLeaveListActivity;
        this.mLeaveListHandler = new LeaveListHandler(mLeaveListActivity);
        mLeaveListActivity.setBaseReceiver(new LeaveListReceiver(mLeaveListHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mLeaveListActivity.setContentView(R.layout.activity_leave_list);
        mLeaveListActivity.mshada = (RelativeLayout) mLeaveListActivity.findViewById(R.id.shade);
        ImageView back = mLeaveListActivity.findViewById(R.id.back);
        back.setOnClickListener(mLeaveListActivity.mBackListener);
        TextView more = mLeaveListActivity.findViewById(R.id.more);
        more.setOnClickListener(mLeaveListActivity.mShowMore);
        ImageView creat = mLeaveListActivity.findViewById(R.id.creatnotice);
        creat.setOnClickListener(mLeaveListActivity.creatListener);

        mLeaveListActivity.mSearchViewLayout = (SearchViewLayout) mLeaveListActivity.findViewById(R.id.top_layer);
        mLeaveListActivity.mSearchViewLayout.mSetOnSearchListener(mLeaveListActivity.mOnEditorActionListener);
        mLeaveListActivity.hit1 = (TextView) mLeaveListActivity.findViewById(R.id.daytxt);
        mLeaveListActivity.hit2 = (TextView) mLeaveListActivity.findViewById(R.id.weektxt);
        mLeaveListActivity.hit3 = (TextView) mLeaveListActivity.findViewById(R.id.monthtxt);
        mLeaveListActivity.mViewPager = (NoScrollViewPager) mLeaveListActivity.findViewById(R.id.load_pager);
        mLeaveListActivity.mLefttTeb = (RelativeLayout) mLeaveListActivity.findViewById(R.id.day);
        mLeaveListActivity.mMiddeleTeb = (RelativeLayout) mLeaveListActivity.findViewById(R.id.week);
        mLeaveListActivity.mRightTeb = (RelativeLayout) mLeaveListActivity.findViewById(R.id.month);

        mLeaveListActivity.mLefttImg = (TextView) mLeaveListActivity.findViewById(R.id.daytxt);
        mLeaveListActivity.mMiddleImg = (TextView) mLeaveListActivity.findViewById(R.id.weektxt);
        mLeaveListActivity.mRightImg = (TextView) mLeaveListActivity.findViewById(R.id.monthtxt);
        View mView1 = null;
        View mView2 = null;
        View mView3 = null;
        mView1 = mLeaveListActivity.getLayoutInflater().inflate(R.layout.leavepager, null);
        mLeaveListActivity.mPullToRefreshView1 = (PullToRefreshView) mView1.findViewById(R.id.task_pull_refresh_view);
        mLeaveListActivity.mMyList = (ListView) mView1.findViewById(R.id.busines_List);
        mLeaveListActivity.mMyList.setOnItemClickListener(mLeaveListActivity.clickAdapterListener);
        mLeaveListActivity.mMyList.setOnScrollListener(mLeaveListActivity.mscoll);
        mView2 = mLeaveListActivity.getLayoutInflater().inflate(R.layout.leavepager, null);
        mLeaveListActivity.mPullToRefreshView2 = (PullToRefreshView) mView2.findViewById(R.id.task_pull_refresh_view);
        mLeaveListActivity.mApproveList = (ListView) mView2.findViewById(R.id.busines_List);
        mLeaveListActivity.mApproveList.setOnItemClickListener(mLeaveListActivity.clickAdapterListener);
        mLeaveListActivity.mApproveList.setOnScrollListener(mLeaveListActivity.mscoll);
        mView3 = mLeaveListActivity.getLayoutInflater().inflate(R.layout.leavepager, null);
        mLeaveListActivity.mPullToRefreshView3 = (PullToRefreshView) mView3.findViewById(R.id.task_pull_refresh_view);
        mLeaveListActivity.mCopyList = (ListView) mView3.findViewById(R.id.busines_List);
        mLeaveListActivity.mCopyList.setOnItemClickListener(mLeaveListActivity.clickAdapterListener);
        mLeaveListActivity.mCopyList.setOnScrollListener(mLeaveListActivity.mscoll);
        mLeaveListActivity.mViews.add(mView1);
        mLeaveListActivity.mViews.add(mView2);
        mLeaveListActivity.mViews.add(mView3);
        mLeaveListActivity.mLoderPageAdapter = new LoderPageAdapter(mLeaveListActivity.mViews);
        mLeaveListActivity.mViewPager.setAdapter(mLeaveListActivity.mLoderPageAdapter);
        mLeaveListActivity.mViewPager.setNoScroll(true);
        mLeaveListActivity.mLefttTeb.setOnClickListener(mLeaveListActivity.leftClickListener);
        mLeaveListActivity.mMiddeleTeb.setOnClickListener(mLeaveListActivity.middleClickListener);
        mLeaveListActivity.mRightTeb.setOnClickListener(mLeaveListActivity.rightClickListener);
        mLeaveListActivity.mMyList.setAdapter(mLeaveListActivity.myLeaveAdapter);
        mLeaveListActivity.mApproveList.setAdapter(mLeaveListActivity.approveLeaveAdapter);
        mLeaveListActivity.mCopyList.setAdapter(mLeaveListActivity.copyLeaveAdapter);
        mLeaveListActivity.mPullToRefreshView1.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        mLeaveListActivity.mPullToRefreshView1.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        mLeaveListActivity.mPullToRefreshView2.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        mLeaveListActivity.mPullToRefreshView2.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        mLeaveListActivity.mPullToRefreshView3.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        mLeaveListActivity.mPullToRefreshView3.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        mLeaveListActivity.mPullToRefreshView1.setOnHeaderRefreshListener(mLeaveListActivity);
        mLeaveListActivity.mPullToRefreshView1.setOnFooterRefreshListener(mLeaveListActivity);
        mLeaveListActivity.mPullToRefreshView2.setOnHeaderRefreshListener(mLeaveListActivity);
        mLeaveListActivity.mPullToRefreshView2.setOnFooterRefreshListener(mLeaveListActivity);
        mLeaveListActivity.mPullToRefreshView3.setOnHeaderRefreshListener(mLeaveListActivity);
        mLeaveListActivity.mPullToRefreshView3.setOnFooterRefreshListener(mLeaveListActivity);
        LeaveManager.getInstance().upDateHit();
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
        mLeaveListHandler = null;
    }

    public void onSearch() {
        if (mLeaveListActivity.mSearchViewLayout.getText().length() == 0) {
            switch (mLeaveListActivity.mViewPager.getCurrentItem()) {
                case 0:
                    mLeaveListActivity.stext1 = mLeaveListActivity.mSearchViewLayout.getText().toString();
                    mLeaveListActivity.mMyList.setAdapter(mLeaveListActivity.myLeaveAdapter);
                    break;
                case 1:
                    mLeaveListActivity.stext2 = mLeaveListActivity.mSearchViewLayout.getText().toString();
                    mLeaveListActivity.mApproveList.setAdapter(mLeaveListActivity.approveLeaveAdapter);
                    break;
                case 2:
                    mLeaveListActivity.stext3 = mLeaveListActivity.mSearchViewLayout.getText().toString();
                    mLeaveListActivity.mCopyList.setAdapter(mLeaveListActivity.copyLeaveAdapter);
                    break;
            }

        } else {
            switch (mLeaveListActivity.mViewPager.getCurrentItem()) {
                case 0:
                    mLeaveListActivity.stext1 = mLeaveListActivity.mSearchViewLayout.getText().toString();
                    getSearchItem(mLeaveListActivity.mMyLeaves, mLeaveListActivity.mMyLeaves);
                    mLeaveListActivity.mMyList.setAdapter(mLeaveListActivity.mysLeaveAdapter);
                    break;
                case 1:
                    mLeaveListActivity.stext2 = mLeaveListActivity.mSearchViewLayout.getText().toString();
                    getSearchItem(mLeaveListActivity.mApproveLeaves, mLeaveListActivity.mApprovesLeaves);
                    mLeaveListActivity.mApproveList.setAdapter(mLeaveListActivity.approvesLeaveAdapter);
                    break;
                case 2:
                    mLeaveListActivity.stext3 = mLeaveListActivity.mSearchViewLayout.getText().toString();
                    getSearchItem(mLeaveListActivity.mCopyLeaves, mLeaveListActivity.mCopysLeaves);
                    mLeaveListActivity.mCopyList.setAdapter(mLeaveListActivity.copysLeaveAdapter);
                    break;
            }
        }
    }

    public void getSearchItem(ArrayList<Leave> mleaves, ArrayList<Leave> mleaves2) {
        mleaves2.clear();
        for (int i = 0; i < mleaves.size(); i++) {
            Leave mLeave = mleaves.get(i);
            if (mLeave.name.contains(mLeaveListActivity.mSearchViewLayout.getText())) {
                mleaves2.add(mLeave);
            }
        }
    }

    public void creat() {
        Intent intent = new Intent(mLeaveListActivity, LeaveActivity.class);
        Leave leave = new Leave();
        if(LeaveManager.getInstance().oaUtils.mAccount.mManagerId.length() != 0)
        {
            Contacts model = (Contacts) Bus.callData(mLeaveListActivity,"chat/getContactItem",LeaveManager.getInstance().oaUtils.mAccount.mManagerId);
            leave.senders = LeaveManager.getInstance().getSenders();
            leave.copyers = LeaveManager.getInstance().getCopyers();
            if(leave.senders.length() == 0)
            {
                leave.senders = model.mRecordid;
            }
        }
        else
        {
            leave.senders = LeaveManager.getInstance().getSenders();
            leave.copyers = LeaveManager.getInstance().getCopyers();
        }
        intent.putExtra("leave", leave);
        mLeaveListActivity.startActivity(intent);
    }

    public void showLeft() {

        mLeaveListActivity.mViewPager.setCurrentItem(0);


        mLeaveListActivity.mRightTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mLeaveListActivity.mRightImg.setTextColor(Color.parseColor("#8F9093"));
        mLeaveListActivity.mMiddeleTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mLeaveListActivity.mMiddleImg.setTextColor(Color.parseColor("#8F9093"));
        mLeaveListActivity.mLefttTeb.setBackgroundResource(R.drawable.shape_bg_readbg);
        mLeaveListActivity.mLefttImg.setTextColor(Color.parseColor("#FFFFFF"));
        mLeaveListActivity.mPullToRefreshView1.onFooterRefreshComplete();

        mLeaveListActivity.mPullToRefreshView1.onFooterRefreshComplete();
    }

    public void showMiddle() {

        mLeaveListActivity.mViewPager.setCurrentItem(1);
        mLeaveListActivity.mRightTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mLeaveListActivity.mRightImg.setTextColor(Color.parseColor("#8F9093"));
        mLeaveListActivity.mLefttTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mLeaveListActivity.mLefttImg.setTextColor(Color.parseColor("#8F9093"));
        mLeaveListActivity.mMiddeleTeb.setBackgroundResource(R.drawable.shape_bg_readbg);
        mLeaveListActivity.mMiddleImg.setTextColor(Color.parseColor("#FFFFFF"));
        mLeaveListActivity.mPullToRefreshView2.onFooterRefreshComplete();

    }

    public void showRight() {

        mLeaveListActivity.mViewPager.setCurrentItem(2);
        mLeaveListActivity.mMiddeleTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mLeaveListActivity.mMiddleImg.setTextColor(Color.parseColor("#8F9093"));
        mLeaveListActivity.mLefttTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mLeaveListActivity.mLefttImg.setTextColor(Color.parseColor("#8F9093"));
        mLeaveListActivity.mRightTeb.setBackgroundResource(R.drawable.shape_bg_readbg);
        mLeaveListActivity.mRightImg.setTextColor(Color.parseColor("#FFFFFF"));
        mLeaveListActivity.mPullToRefreshView3.onFooterRefreshComplete();


    }

    public void initData() {
        mLeaveListActivity.myLeaveAdapter = new LeaveAdapter(mLeaveListActivity.mMyLeaves, mLeaveListActivity,0,mLeaveListHandler);
        mLeaveListActivity.approveLeaveAdapter = new LeaveAdapter(mLeaveListActivity.mApproveLeaves, mLeaveListActivity,1,mLeaveListHandler);
        mLeaveListActivity.copyLeaveAdapter = new LeaveAdapter(mLeaveListActivity.mCopyLeaves, mLeaveListActivity,2,mLeaveListHandler);
        mLeaveListActivity.mysLeaveAdapter = new LeaveAdapter(mLeaveListActivity.mMysLeaves, mLeaveListActivity,0,mLeaveListHandler);
        mLeaveListActivity.approvesLeaveAdapter = new LeaveAdapter(mLeaveListActivity.mApprovesLeaves, mLeaveListActivity,1,mLeaveListHandler);
        mLeaveListActivity.copysLeaveAdapter = new LeaveAdapter(mLeaveListActivity.mCopysLeaves, mLeaveListActivity,2,mLeaveListHandler);
        mLeaveListActivity.mMyList.setAdapter(mLeaveListActivity.myLeaveAdapter);
        mLeaveListActivity.mApproveList.setAdapter(mLeaveListActivity.approveLeaveAdapter);
        mLeaveListActivity.mCopyList.setAdapter(mLeaveListActivity.copyLeaveAdapter);
        showLeft();
        getLeave();
    }

    public void getLeave() {

        if (mLeaveListActivity.mViewPager.getCurrentItem() == 0) {
            if (mLeaveListActivity.myLeaveAdapter.getCount() >= mLeaveListActivity.myLeaveAdapter.totalCount && mLeaveListActivity.myLeaveAdapter.totalCount != -1) {

                return;
            } else {
                LeaveAsks.getLeave(mLeaveListHandler, mLeaveListActivity, LeaveManager.getInstance().getSetUserId(), LeaveManager.TYPE_SEND, mLeaveListActivity.myLeaveAdapter.nowpage);
                mLeaveListActivity.waitDialog.show();
            }
        } else if (mLeaveListActivity.mViewPager.getCurrentItem() == 1) {
            if (mLeaveListActivity.approveLeaveAdapter.getCount() >= mLeaveListActivity.approveLeaveAdapter.totalCount && mLeaveListActivity.approveLeaveAdapter.totalCount != -1) {
                return;
            } else {
                LeaveAsks.getLeave(mLeaveListHandler, mLeaveListActivity, LeaveManager.getInstance().getSetUserId(), LeaveManager.TYPE_APPROVE, mLeaveListActivity.approveLeaveAdapter.nowpage);
                mLeaveListActivity.waitDialog.show();
            }
        } else {
            if (mLeaveListActivity.copyLeaveAdapter.getCount() >= mLeaveListActivity.copyLeaveAdapter.totalCount && mLeaveListActivity.copyLeaveAdapter.totalCount != -1) {
                return;
            } else {
                LeaveAsks.getLeave(mLeaveListHandler, mLeaveListActivity, LeaveManager.getInstance().getSetUserId(), LeaveManager.TYPE_COPY, mLeaveListActivity.copyLeaveAdapter.nowpage);
                mLeaveListActivity.waitDialog.show();
            }
        }

    }

    public void onfoot() {

        if (mLeaveListActivity.mViewPager.getCurrentItem() == 0) {

            mLeaveListActivity.mPullToRefreshView1.onFooterRefreshComplete();
            if (mLeaveListActivity.myLeaveAdapter.getCount() >= mLeaveListActivity.myLeaveAdapter.totalCount && mLeaveListActivity.myLeaveAdapter.totalCount != -1) {
                AppUtils.showMessage(mLeaveListActivity, mLeaveListActivity.getString(R.string.keyword_lodingfinish));
                return;
            }
        } else if (mLeaveListActivity.mViewPager.getCurrentItem() == 1) {

            mLeaveListActivity.mPullToRefreshView2.onFooterRefreshComplete();
            if (mLeaveListActivity.approveLeaveAdapter.getCount() >= mLeaveListActivity.approveLeaveAdapter.totalCount && mLeaveListActivity.approveLeaveAdapter.totalCount != -1) {
                AppUtils.showMessage(mLeaveListActivity, mLeaveListActivity.getString(R.string.keyword_lodingfinish));
                return;
            }
        } else {
            mLeaveListActivity.mPullToRefreshView3.onFooterRefreshComplete();
            if (mLeaveListActivity.copyLeaveAdapter.getCount() >= mLeaveListActivity.copyLeaveAdapter.totalCount && mLeaveListActivity.copyLeaveAdapter.totalCount != -1) {
                AppUtils.showMessage(mLeaveListActivity, mLeaveListActivity.getString(R.string.keyword_lodingfinish));
                return;
            }
        }
        getLeave();
    }

    public void onhead() {
        if (mLeaveListActivity.mViewPager.getCurrentItem() == 0) {
            mLeaveListActivity.myLeaveAdapter.totalCount = -1;
            mLeaveListActivity.myLeaveAdapter.nowpage = 1;
            mLeaveListActivity.myLeaveAdapter.getmLeaves().clear();
            mLeaveListActivity.myLeaveAdapter.notifyDataSetChanged();
            mLeaveListActivity.mPullToRefreshView1.onHeaderRefreshComplete();
        } else if (mLeaveListActivity.mViewPager.getCurrentItem() == 1) {
            mLeaveListActivity.approveLeaveAdapter.totalCount = -1;
            mLeaveListActivity.approveLeaveAdapter.nowpage = 1;
            mLeaveListActivity.approveLeaveAdapter.getmLeaves().clear();
            mLeaveListActivity.approveLeaveAdapter.notifyDataSetChanged();
            mLeaveListActivity.mPullToRefreshView2.onHeaderRefreshComplete();
        } else {
            mLeaveListActivity.copyLeaveAdapter.totalCount = -1;
            mLeaveListActivity.copyLeaveAdapter.nowpage = 1;
            mLeaveListActivity.copyLeaveAdapter.getmLeaves().clear();
            mLeaveListActivity.copyLeaveAdapter.notifyDataSetChanged();
            mLeaveListActivity.mPullToRefreshView3.onHeaderRefreshComplete();
        }
        getLeave();
    }

    public void updataAll() {
        mLeaveListActivity.myLeaveAdapter.totalCount = -1;
        mLeaveListActivity.myLeaveAdapter.nowpage = 1;
        mLeaveListActivity.myLeaveAdapter.getmLeaves().clear();
        mLeaveListActivity.mysLeaveAdapter.getmLeaves().clear();
        mLeaveListActivity.myLeaveAdapter.notifyDataSetChanged();
        mLeaveListActivity.mysLeaveAdapter.notifyDataSetChanged();
        mLeaveListActivity.approveLeaveAdapter.totalCount = -1;
        mLeaveListActivity.approveLeaveAdapter.nowpage = 1;
        mLeaveListActivity.approveLeaveAdapter.getmLeaves().clear();
        mLeaveListActivity.approvesLeaveAdapter.getmLeaves().clear();
        mLeaveListActivity.approveLeaveAdapter.notifyDataSetChanged();
        mLeaveListActivity.approvesLeaveAdapter.notifyDataSetChanged();
        mLeaveListActivity.copyLeaveAdapter.totalCount = -1;
        mLeaveListActivity.copyLeaveAdapter.nowpage = 1;
        mLeaveListActivity.copyLeaveAdapter.getmLeaves().clear();
        mLeaveListActivity.copysLeaveAdapter.getmLeaves().clear();
        mLeaveListActivity.copyLeaveAdapter.notifyDataSetChanged();
        mLeaveListActivity.copysLeaveAdapter.notifyDataSetChanged();
        getLeave();
    }

    public void onitemcick(Leave mLeave) {
//		mleaveModel.setIsread(true);
//		mNoticeActivity.mLoderPageAdapter.notifyDataSetChanged();
        Intent intent = new Intent(mLeaveListActivity, LeaveDetialActivity.class);
        intent.putExtra("leave", mLeave);
        mLeaveListActivity.startActivity(intent);
    }

    public void showButtom() {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem mMenuItem = new MenuItem();
        mMenuItem.btnName = mLeaveListActivity.getString(R.string.keyword_myleave);
        mMenuItem.mListener = mLeaveListActivity.mShowMy;
        mMenuItem = new MenuItem();
        items.add(mMenuItem);
        mMenuItem.btnName = mLeaveListActivity.getString(R.string.keyword_otherleave);
        mMenuItem.mListener = mLeaveListActivity.mShowOther;
        items.add(mMenuItem);
        mLeaveListActivity.popupWindow1 = AppUtils.creatButtomMenu(mLeaveListActivity, mLeaveListActivity.mshada, items, mLeaveListActivity.findViewById(R.id.activity_busines2));
    }


    public void showMy() {

        mLeaveListActivity.popupWindow1.dismiss();
        TextView title = mLeaveListActivity.findViewById(R.id.title);
        title.setText(mLeaveListActivity.getString(R.string.xml_leave_title_approve));
        if(LeaveManager.getInstance().setContacts != null) {
            LeaveManager.getInstance().setContacts = null;
            updataAll();
        }

    }

    public void showOther() {
        Bus.callData(mLeaveListActivity,"chat/setUnderlineContacts",mLeaveListActivity,"",LeaveListActivity.ACTION_LEAVE_SET_CONTACTS);
    }

    public void setuser(Intent intent) {
        LeaveManager.getInstance().setContacts = intent.getParcelableExtra("contacts");
        TextView title = mLeaveListActivity.findViewById(R.id.title);
        title.setText(LeaveManager.getInstance().setContacts.mName + mLeaveListActivity.getString(R.string.xml_leave_title_approve_ex));
        updataAll();
    }

    public void initHit() {
        if (LeaveManager.getInstance().mehit > 0) {
            mLeaveListActivity.hit1.setText(mLeaveListActivity.getString(R.string.xml_leave_my_send) + "(" + String.valueOf(LeaveManager.getInstance().mehit) + ")");
        } else {
            mLeaveListActivity.hit1.setText(mLeaveListActivity.getString(R.string.xml_leave_my_send));
        }
        if (LeaveManager.getInstance().sendhit > 0) {
            mLeaveListActivity.hit2.setText(mLeaveListActivity.getString(R.string.xml_leave_my_approve) + "(" + String.valueOf(LeaveManager.getInstance().sendhit) + ")");
        } else {
            mLeaveListActivity.hit2.setText(mLeaveListActivity.getString(R.string.xml_leave_my_approve));
        }
        if (LeaveManager.getInstance().copyerhit > 0) {
            mLeaveListActivity.hit3.setText(mLeaveListActivity.getString(R.string.xml_leave_my_join) + "(" + String.valueOf(LeaveManager.getInstance().copyerhit) + ")");
        } else {
            mLeaveListActivity.hit3.setText(mLeaveListActivity.getString(R.string.xml_leave_my_join));
        }
    }

    public void leaveUpdateAll() {
        //if(mLeaveListActivity.myLeaveAdapter.getmLeaves().size() > 0)
        {
            mLeaveListActivity.myLeaveAdapter.totalCount = -1;
            mLeaveListActivity.myLeaveAdapter.endPage = mLeaveListActivity.myLeaveAdapter.nowpage;
            mLeaveListActivity.myLeaveAdapter.nowpage = 1;
            mLeaveListActivity.myLeaveAdapter.getmLeaves().clear();
            LeaveAsks.getLeaveList(mLeaveListHandler,mLeaveListActivity,LeaveManager.getInstance().getSetUserId(),LeaveManager.TYPE_SEND,mLeaveListActivity.myLeaveAdapter.nowpage);
        }
        //if(mLeaveListActivity.approveLeaveAdapter.getmLeaves().size() > 0)
        {
            mLeaveListActivity.approveLeaveAdapter.totalCount = -1;
            mLeaveListActivity.approveLeaveAdapter.endPage = mLeaveListActivity.myLeaveAdapter.nowpage;
            mLeaveListActivity.approveLeaveAdapter.nowpage = 1;
            mLeaveListActivity.approveLeaveAdapter.getmLeaves().clear();
            LeaveAsks.getLeaveList(mLeaveListHandler,mLeaveListActivity,LeaveManager.getInstance().getSetUserId(),LeaveManager.TYPE_APPROVE,mLeaveListActivity.approveLeaveAdapter.nowpage);
        }
        //if(mLeaveListActivity.copyLeaveAdapter.getmLeaves().size() > 0)
        {
            mLeaveListActivity.copyLeaveAdapter.totalCount = -1;
            mLeaveListActivity.copyLeaveAdapter.endPage = mLeaveListActivity.copyLeaveAdapter.nowpage;
            mLeaveListActivity.copyLeaveAdapter.nowpage = 1;
            mLeaveListActivity.copyLeaveAdapter.getmLeaves().clear();
            LeaveAsks.getLeaveList(mLeaveListHandler,mLeaveListActivity,LeaveManager.getInstance().getSetUserId(),LeaveManager.TYPE_COPY,mLeaveListActivity.copyLeaveAdapter.nowpage);
        }
    }

}
