package intersky.mail.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.Mail;
import intersky.mail.handler.MailListHandler;
import intersky.mail.receiver.MailListReceiver;
import intersky.mail.view.activity.MailEditActivity;
import intersky.mail.view.activity.MailListActivity;
import intersky.mail.view.activity.MailShowActivity;
import intersky.mail.view.adapter.LoderPageAdapter;
import intersky.mail.view.adapter.MailItemAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class MailListPresenter implements Presenter {

    public MailListHandler mMailListHandler;
    public MailListActivity mMailListActivity;
    public MailListPresenter(MailListActivity mMailListActivity)
    {
        this.mMailListActivity = mMailListActivity;
        this.mMailListHandler = new MailListHandler(mMailListActivity);
        mMailListActivity.setBaseReceiver(new MailListReceiver(mMailListHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {


        if(MailManager.getInstance().account.iscloud == false)
        {
            mMailListActivity.type1 = mMailListActivity.getIntent().getIntExtra("id", 0);
            mMailListActivity.type2 = mMailListActivity.getIntent().getIntExtra("type", 0);
            if(mMailListActivity.getIntent().hasExtra("mailboxid"))
            mMailListActivity.mailboxid = mMailListActivity.getIntent().getStringExtra("mailboxid");
        }
        else
        {
            mMailListActivity.type1 = mMailListActivity.getIntent().getIntExtra("id", 0);
            mMailListActivity.type2 = mMailListActivity.getIntent().getIntExtra("type", 0);
            mMailListActivity.mailboxid = mMailListActivity.getIntent().getStringExtra("mailboxid");
        }

        if(mMailListActivity.type2 == -1 && MailManager.getInstance().account.iscloud == true) {
            mMailListActivity.setContentView(R.layout.activity_mail_list_c);
            mMailListActivity.hit1 = (TextView) mMailListActivity.findViewById(R.id.alltxt);
            mMailListActivity.hit2 = (TextView) mMailListActivity.findViewById(R.id.waittxt);
            mMailListActivity.hit3 = (TextView) mMailListActivity.findViewById(R.id.accesstxt);
            mMailListActivity.hit4 = (TextView) mMailListActivity.findViewById(R.id.votetxt);
            mMailListActivity.mViewPager = (NoScrollViewPager) mMailListActivity.findViewById(R.id.load_pager);
            mMailListActivity.mLefttTeb = (RelativeLayout) mMailListActivity.findViewById(R.id.all);
            mMailListActivity.mMiddeleTeb = (RelativeLayout) mMailListActivity.findViewById(R.id.wait);
            mMailListActivity.mRightTeb = (RelativeLayout) mMailListActivity.findViewById(R.id.access);
            mMailListActivity.mRightTeb2 = (RelativeLayout) mMailListActivity.findViewById(R.id.vote);
            mMailListActivity.mLine1 = (RelativeLayout) mMailListActivity.findViewById(R.id.line13);
            mMailListActivity.mLine2 = (RelativeLayout) mMailListActivity.findViewById(R.id.line23);
            mMailListActivity.mLine3 = (RelativeLayout) mMailListActivity.findViewById(R.id.line33);
            mMailListActivity.mLine4 = (RelativeLayout) mMailListActivity.findViewById(R.id.line43);
            mMailListActivity.mLine11 = (RelativeLayout) mMailListActivity.findViewById(R.id.line12);
            mMailListActivity.mLine21 = (RelativeLayout) mMailListActivity.findViewById(R.id.line22);
            mMailListActivity.mLine31 = (RelativeLayout) mMailListActivity.findViewById(R.id.line32);
            mMailListActivity.mLine41 = (RelativeLayout) mMailListActivity.findViewById(R.id.line42);
            mMailListActivity.mLefttImg = (TextView) mMailListActivity.findViewById(R.id.alltxt);
            mMailListActivity.mMiddleImg = (TextView) mMailListActivity.findViewById(R.id.waittxt);
            mMailListActivity.mRightImg = (TextView) mMailListActivity.findViewById(R.id.accesstxt);
            mMailListActivity.mRightImg2 = (TextView) mMailListActivity.findViewById(R.id.votetxt);
            View mView1 = null;
            View mView2 = null;
            View mView3 = null;
            View mView4 = null;
            mView1 = mMailListActivity.getLayoutInflater().inflate(R.layout.leavepager, null);
            mMailListActivity.mPullToRefreshView1 = (PullToRefreshView) mView1
                    .findViewById(R.id.task_pull_refresh_view);
            mMailListActivity.mAllList = (ListView) mView1.findViewById(R.id.busines_List);
            mMailListActivity.mAllList.setOnItemClickListener(mMailListActivity.mailClickListener);
            mMailListActivity.mAllList.setOnItemLongClickListener(mMailListActivity.mailLongClickListener);
            mView2 = mMailListActivity.getLayoutInflater().inflate(R.layout.leavepager, null);
            mMailListActivity.mPullToRefreshView2 = (PullToRefreshView) mView2
                    .findViewById(R.id.task_pull_refresh_view);
            mMailListActivity.mWaitList = (ListView) mView2.findViewById(R.id.busines_List);
            mMailListActivity.mWaitList.setOnItemClickListener(mMailListActivity.mailClickListener);
            mMailListActivity.mWaitList.setOnItemLongClickListener(mMailListActivity.mailLongClickListener);
            mView3 = mMailListActivity.getLayoutInflater().inflate(R.layout.leavepager, null);
            mMailListActivity.mPullToRefreshView3 = (PullToRefreshView) mView3
                    .findViewById(R.id.task_pull_refresh_view);
            mMailListActivity.mAccessList = (ListView) mView3.findViewById(R.id.busines_List);
            mMailListActivity.mAccessList.setOnItemClickListener(mMailListActivity.mailClickListener);
            mMailListActivity.mAccessList.setOnItemLongClickListener(mMailListActivity.mailLongClickListener);
            mView4 = mMailListActivity.getLayoutInflater().inflate(R.layout.leavepager, null);
            mMailListActivity.mPullToRefreshView4 = (PullToRefreshView) mView4
                    .findViewById(R.id.task_pull_refresh_view);
            mMailListActivity.mVoteList = (ListView) mView3.findViewById(R.id.busines_List);
            mMailListActivity.mVoteList.setOnItemClickListener(mMailListActivity.mailClickListener);
            mMailListActivity.mVoteList.setOnItemLongClickListener(mMailListActivity.mailLongClickListener);
            mMailListActivity.mViews.add(mView1);
            mMailListActivity.mViews.add(mView2);
            mMailListActivity.mViews.add(mView3);
            mMailListActivity.mViews.add(mView4);
            mMailListActivity.mLoderPageAdapter = new LoderPageAdapter(mMailListActivity.mViews);
            mMailListActivity.mViewPager.setAdapter(mMailListActivity.mLoderPageAdapter);
            mMailListActivity.mViewPager.setNoScroll(true);
            mMailListActivity.mLefttTeb.setOnClickListener(mMailListActivity.leftClickListener);
            mMailListActivity.mMiddeleTeb.setOnClickListener(mMailListActivity.middleClickListener);
            mMailListActivity.mRightTeb.setOnClickListener(mMailListActivity.rightClickListener);
            mMailListActivity.mRightTeb2.setOnClickListener(mMailListActivity.rightClickListener2);
            mMailListActivity.mPullToRefreshView1.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailListActivity.mPullToRefreshView1.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailListActivity.mPullToRefreshView2.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailListActivity.mPullToRefreshView2.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailListActivity.mPullToRefreshView3.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailListActivity.mPullToRefreshView3.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailListActivity.mPullToRefreshView4.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailListActivity.mPullToRefreshView4.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailListActivity.mPullToRefreshView1.setOnHeaderRefreshListener(mMailListActivity);
            mMailListActivity.mPullToRefreshView1.setOnFooterRefreshListener(mMailListActivity);
            mMailListActivity.mPullToRefreshView2.setOnHeaderRefreshListener(mMailListActivity);
            mMailListActivity.mPullToRefreshView2.setOnFooterRefreshListener(mMailListActivity);
            mMailListActivity.mPullToRefreshView3.setOnHeaderRefreshListener(mMailListActivity);
            mMailListActivity.mPullToRefreshView3.setOnFooterRefreshListener(mMailListActivity);
            mMailListActivity.mPullToRefreshView4.setOnHeaderRefreshListener(mMailListActivity);
            mMailListActivity.mPullToRefreshView4.setOnFooterRefreshListener(mMailListActivity);
            initData();
            getMails(false);
        }
        else
        {
            mMailListActivity.setContentView(R.layout.activity_mail_list);
            mMailListActivity.mPullToRefreshView = (PullToRefreshView) mMailListActivity.findViewById(R.id.mail_pull_refresh_view);
            mMailListActivity.mListView = (ListView) mMailListActivity.findViewById(R.id.mail_List);
            mMailListActivity.mPullToRefreshView.setOnHeaderRefreshListener(mMailListActivity);
            mMailListActivity.mPullToRefreshView.setOnFooterRefreshListener(mMailListActivity);
            mMailListActivity.mMailItemAdapter =  new MailItemAdapter(mMailListActivity,mMailListActivity.mails);
            mMailListActivity.mListView.setAdapter(mMailListActivity.mMailItemAdapter);
            mMailListActivity.mListView.setOnItemClickListener(mMailListActivity.mailClickListener);
            mMailListActivity.mListView.setOnItemLongClickListener(mMailListActivity.mailLongClickListener);

            getMails(false);
        }
        mMailListActivity.buttomlyaer = (RelativeLayout) mMailListActivity.findViewById(R.id.mail_buttom_layer);
        mMailListActivity.buttomlyaer.setVisibility(View.INVISIBLE);
        mMailListActivity.mDelete = (TextView) mMailListActivity.findViewById(R.id.delete);
        mMailListActivity.mDelete.setOnClickListener(mMailListActivity.deleteMailsListener);
        ImageView back = mMailListActivity.findViewById(R.id.back);
        back.setOnClickListener(mMailListActivity.mBackListener);
        ImageView add = mMailListActivity.findViewById(R.id.add);
        add.setOnClickListener(mMailListActivity.mWriteMailListener);
        if(MailManager.getInstance().account.iscloud == false)
        {

            TextView title = mMailListActivity.findViewById(R.id.title);
            title.setText(getTitle(mMailListActivity.type1));
        }
        else
        {
            TextView title = mMailListActivity.findViewById(R.id.title);
            title.setText(getTitlec(mMailListActivity.type2));
        }
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }

    public void getMails(boolean isupdatall) {
        if(MailManager.getInstance().account.iscloud == false)
        {
            if(isupdatall)
            {
                mMailListActivity.mails.clear();
                mMailListActivity.isAll = false;
                mMailListActivity.startPos = 0;
                mMailListActivity.waitDialog.show();
                MailAsks.getMials(mMailListActivity,mMailListHandler, mMailListActivity.getIntent().getIntExtra("id",0),
                        mMailListActivity.getIntent().getIntExtra("type",0),mMailListActivity.getIntent().getStringExtra("mailboxid"),mMailListActivity.startPos);
            }
            else
            {
                if(mMailListActivity.isAll == false)
                {
                    mMailListActivity.waitDialog.show();
                    MailAsks.getMials(mMailListActivity,mMailListHandler, mMailListActivity.getIntent().getIntExtra("id",0),
                            mMailListActivity.getIntent().getIntExtra("type",0),mMailListActivity.getIntent().getStringExtra("mailboxid"),mMailListActivity.startPos);
                }
                else
                    AppUtils.showMessage(mMailListActivity,mMailListActivity.getString(R.string.message_addall));
            }
        }
        else
        {
            if(mMailListActivity.type2 == -1)
            {

                if(mMailListActivity.mViewPager.getCurrentItem() == 0)
                {
                    if(isupdatall)
                    {
                        mMailListActivity.mMailItems1.clear();
                        mMailListActivity.isall1 = false;
                        mMailListActivity.waitDialog.show();
                        MailAsks.getMials(mMailListActivity,mMailListHandler, mMailListActivity.type1,
                                mMailListActivity.type2,mMailListActivity.mViewPager.getCurrentItem(),mMailListActivity.mailboxid,1,mMailListActivity.mStartpos1*40);
                        mMailListActivity.mStartpos1 = 1;
                    }
                    else
                    {
                        if(mMailListActivity.isall1 == false)
                        {
                            mMailListActivity.waitDialog.show();
                            MailAsks.getMials(mMailListActivity,mMailListHandler, mMailListActivity.type1,
                                    mMailListActivity.type2,mMailListActivity.mViewPager.getCurrentItem(),mMailListActivity.mailboxid,mMailListActivity.mStartpos1,40);
                        }
                        else
                            AppUtils.showMessage(mMailListActivity,mMailListActivity.getString(R.string.message_addall));
                    }
                }
                else if(mMailListActivity.mViewPager.getCurrentItem() == 1)
                {
                    if(isupdatall)
                    {
                        mMailListActivity.mMailItems2.clear();
                        mMailListActivity.isall2 = false;
                        mMailListActivity.waitDialog.show();
                        MailAsks.getMials(mMailListActivity,mMailListHandler, mMailListActivity.type1,
                                mMailListActivity.type2,mMailListActivity.mViewPager.getCurrentItem(),mMailListActivity.mailboxid,1,mMailListActivity.mStartpos2*40);
                        mMailListActivity.mStartpos2 = 1;
                    }
                    else
                    {
                        if(mMailListActivity.isall2 == false)
                        {
                            mMailListActivity.waitDialog.show();
                            MailAsks.getMials(mMailListActivity,mMailListHandler, mMailListActivity.type1,
                                    mMailListActivity.type2,mMailListActivity.mViewPager.getCurrentItem(),mMailListActivity.mailboxid,mMailListActivity.mStartpos2,40);
                        }
                        else
                            AppUtils.showMessage(mMailListActivity,mMailListActivity.getString(R.string.message_addall));
                    }
                }
                else if(mMailListActivity.mViewPager.getCurrentItem() == 2)
                {
                    if(isupdatall)
                    {
                        mMailListActivity.mMailItems3.clear();
                        mMailListActivity.isall3 = false;
                        mMailListActivity.waitDialog.show();
                        MailAsks.getMials(mMailListActivity,mMailListHandler, mMailListActivity.type1,
                                mMailListActivity.type2,mMailListActivity.mViewPager.getCurrentItem(),mMailListActivity.mailboxid,1,mMailListActivity.mStartpos3*40);
                        mMailListActivity.mStartpos2 = 1;
                    }
                    else
                    {
                        if(mMailListActivity.isall3 == false)
                        {
                            mMailListActivity.waitDialog.show();
                            MailAsks.getMials(mMailListActivity,mMailListHandler, mMailListActivity.type1,
                                    mMailListActivity.type2,mMailListActivity.mViewPager.getCurrentItem(),mMailListActivity.mailboxid,mMailListActivity.mStartpos3,40);
                        }
                        else
                            AppUtils.showMessage(mMailListActivity,mMailListActivity.getString(R.string.message_addall));
                    }
                }
                else if(mMailListActivity.mViewPager.getCurrentItem() == 3)
                {
                    if(isupdatall)
                    {
                        mMailListActivity.mMailItems4.clear();
                        mMailListActivity.isall4 = false;
                        mMailListActivity.waitDialog.show();
                        MailAsks.getMials(mMailListActivity,mMailListHandler, mMailListActivity.type1,
                                mMailListActivity.type2,mMailListActivity.mViewPager.getCurrentItem(),mMailListActivity.mailboxid,1,mMailListActivity.mStartpos4*40);
                        mMailListActivity.mStartpos2 = 1;
                    }
                    else
                    {
                        if(mMailListActivity.isall4 == false)
                        {
                            mMailListActivity.waitDialog.show();
                            MailAsks.getMials(mMailListActivity,mMailListHandler, mMailListActivity.type1,
                                    mMailListActivity.type2,mMailListActivity.mViewPager.getCurrentItem(),mMailListActivity.mailboxid,mMailListActivity.mStartpos4,40);
                        }

                    }
                }
            }
            else
            {
                if(isupdatall)
                {
                    mMailListActivity.mails.clear();
                    mMailListActivity.isAll = false;
                    mMailListActivity.waitDialog.show();
                    MailAsks.getMials(mMailListActivity,mMailListHandler, mMailListActivity.type1,
                            mMailListActivity.type2,0,mMailListActivity.mailboxid,1,mMailListActivity.startPos*40);
                }
                else
                {
                    if( mMailListActivity.isAll == false) {
                        mMailListActivity.waitDialog.show();
                        MailAsks.getMials(mMailListActivity,mMailListHandler, mMailListActivity.type1,
                                mMailListActivity.type2,0,mMailListActivity.mailboxid,mMailListActivity.startPos,40);
                    }
                    else
                        AppUtils.showMessage(mMailListActivity,mMailListActivity.getString(R.string.message_addall));
                }

            }
        }

    }

    private String getTitle(int id) {
        String title = "";
        switch (id) {
            case MailListActivity.GET_MAIL_SHOUJIANXIAN:
                if (mMailListActivity.type2 == 2) {
                    title = mMailListActivity.getString(R.string.keyword_mailtitlenew);
                } else if (mMailListActivity.type2 == 1) {
                    title = mMailListActivity.getString(R.string.keyword_mailtitlenold);
                }
                break;
            case MailListActivity.GET_MAIL_FENFAXIAN:
                if (mMailListActivity.type2 == 0) {
                    title = mMailListActivity.getString(R.string.keyword_mailtitlenotcontact);
                } else if (mMailListActivity.type2 == 1) {
                    title = mMailListActivity.getString(R.string.keyword_mailtitlecontacting);
                } else if (mMailListActivity.type2 == 2) {
                    title = mMailListActivity.getString(R.string.keyword_mailtitlecontactsuccess);
                } else if (mMailListActivity.type2 == 3) {
                    title = mMailListActivity.getString(R.string.keyword_mailtitlecontactfail);
                }
                break;
            case MailListActivity.GET_MAIL_FAJIANXIAN:
                if (mMailListActivity.type2 == 1) {
                    title = mMailListActivity.getString(R.string.keyword_mailtitlewait);
                } else if (mMailListActivity.type2 == 2) {
                    title = mMailListActivity.getString(R.string.keyword_mailtitlealready);
                } else if (mMailListActivity.type2 == 3) {
                    title = mMailListActivity.getString(R.string.keyword_mailtitleback);
                }
                break;
            case MailListActivity.GET_MAIL_YIFANXIAN:
                title = mMailListActivity.getString(R.string.keyword_mailtitleasend);
                break;
            case MailListActivity.GET_MAIL_LAJIXIAN:
                title = mMailListActivity.getString(R.string.keyword_mailtitlercycle);
                break;
            case MailListActivity.GET_MAIL_SHANCHUXIAN:
                title = mMailListActivity.getString(R.string.keyword_mailtitldelete);
                break;
            case MailListActivity.GET_MAIL_CAOGAOXIAN:
                title = mMailListActivity.getString(R.string.keyword_mailtitledraft);
                break;
            case MailListActivity.GET_MAIL_SHENGPIXIANG:
                title = mMailListActivity.getString(R.string.keyword_mailtitleaplrove);
                break;
            case MailListActivity.GET_MAIL_NEIBU:
                title = mMailListActivity.getString(R.string.keyword_innerbox);
                break;
        }

        if (mMailListActivity.mailboxid != null) {
            title = mMailListActivity.getIntent().getStringExtra("mailname");
        }

        return title;
    }

    private String getTitlec(int id) {
        String title = "";
        switch (id) {
            case -1:
                title = mMailListActivity.getString(R.string.keyword_mailtitleaplrove);
                break;
            case 1:
                title = mMailListActivity.getString(R.string.mail_shoujian);
                break;
            case 2:
                title = mMailListActivity.getString(R.string.keyword_mailtitleasend);
                break;
            case 0:
                title = mMailListActivity.getString(R.string.keyword_mailtitledraft);
                break;
            case 3:
                title = mMailListActivity.getString(R.string.keyword_mailtitldelete2);
                break;
            case 4:
                title = mMailListActivity.getString(R.string.keyword_mailtitlercycle);
                break;
        }
        if (mMailListActivity.mailboxid != null) {
            title = mMailListActivity.getIntent().getStringExtra("mailname");
        }

        return title;
    }

    public void showLeft() {

        mMailListActivity.mViewPager.setCurrentItem(0);
        mMailListActivity.mLine1.setBackgroundColor(Color.rgb(98, 153, 243));
        mMailListActivity.mLine2.setBackgroundColor(Color.rgb(242, 242, 242));
        mMailListActivity.mLine3.setBackgroundColor(Color.rgb(242, 242, 242));
        mMailListActivity.mLine4.setBackgroundColor(Color.rgb(242, 242, 242));
        mMailListActivity.mLine1.setVisibility(View.VISIBLE);
        mMailListActivity.mLine2.setVisibility(View.INVISIBLE);
        mMailListActivity.mLine3.setVisibility(View.INVISIBLE);
        mMailListActivity.mLine4.setVisibility(View.INVISIBLE);
        mMailListActivity.mRightImg.setTextColor(Color.rgb(0, 0, 0));
        mMailListActivity.mMiddleImg.setTextColor(Color.rgb(0, 0, 0));
        mMailListActivity.mRightImg2.setTextColor(Color.rgb(0, 0, 0));
        mMailListActivity.mLefttImg.setTextColor(Color.rgb(98, 153, 243));
        mMailListActivity.mPullToRefreshView1.onFooterRefreshComplete();
    }

    public void showMiddle() {

        mMailListActivity.mViewPager.setCurrentItem(1);
        mMailListActivity.mLine1.setBackgroundColor(Color.rgb(242, 242, 242));
        mMailListActivity.mLine2.setBackgroundColor(Color.rgb(98, 153, 243));
        mMailListActivity.mLine3.setBackgroundColor(Color.rgb(242, 242, 242));
        mMailListActivity.mLine4.setBackgroundColor(Color.rgb(242, 242, 242));
        mMailListActivity.mLine1.setVisibility(View.INVISIBLE);
        mMailListActivity.mLine2.setVisibility(View.VISIBLE);
        mMailListActivity.mLine3.setVisibility(View.INVISIBLE);
        mMailListActivity.mLine4.setVisibility(View.INVISIBLE);
        mMailListActivity.mRightImg.setTextColor(Color.rgb(0, 0, 0));
        mMailListActivity.mRightImg2.setTextColor(Color.rgb(0, 0, 0));
        mMailListActivity.mMiddleImg.setTextColor(Color.rgb(98, 153, 243));
        mMailListActivity.mLefttImg.setTextColor(Color.rgb(0, 0, 0));
        mMailListActivity.mPullToRefreshView2.onFooterRefreshComplete();

    }

    public void showRight() {

        mMailListActivity.mViewPager.setCurrentItem(2);
        mMailListActivity.mLine1.setBackgroundColor(Color.rgb(242, 242, 242));
        mMailListActivity.mLine2.setBackgroundColor(Color.rgb(242, 242, 242));
        mMailListActivity.mLine4.setBackgroundColor(Color.rgb(242, 242, 242));
        mMailListActivity.mLine3.setBackgroundColor(Color.rgb(98, 153, 243));
        mMailListActivity.mLine1.setVisibility(View.INVISIBLE);
        mMailListActivity.mLine2.setVisibility(View.INVISIBLE);
        mMailListActivity.mLine4.setVisibility(View.INVISIBLE);
        mMailListActivity.mLine3.setVisibility(View.VISIBLE);
        mMailListActivity.mRightImg.setTextColor(Color.rgb(98, 153, 243));
        mMailListActivity.mMiddleImg.setTextColor(Color.rgb(0, 0, 0));
        mMailListActivity.mLefttImg.setTextColor(Color.rgb(0, 0, 0));
        mMailListActivity.mRightImg2.setTextColor(Color.rgb(0, 0, 0));
        mMailListActivity.mPullToRefreshView3.onFooterRefreshComplete();


    }

    public void showRight2() {

        mMailListActivity.mViewPager.setCurrentItem(2);
        mMailListActivity.mLine1.setBackgroundColor(Color.rgb(242, 242, 242));
        mMailListActivity.mLine2.setBackgroundColor(Color.rgb(242, 242, 242));
        mMailListActivity.mLine3.setBackgroundColor(Color.rgb(242, 242, 242));
        mMailListActivity.mLine4.setBackgroundColor(Color.rgb(98, 153, 243));
        mMailListActivity.mLine1.setVisibility(View.INVISIBLE);
        mMailListActivity.mLine2.setVisibility(View.INVISIBLE);
        mMailListActivity.mLine3.setVisibility(View.INVISIBLE);
        mMailListActivity.mLine4.setVisibility(View.VISIBLE);
        mMailListActivity.mRightImg2.setTextColor(Color.rgb(98, 153, 243));
        mMailListActivity.mMiddleImg.setTextColor(Color.rgb(0, 0, 0));
        mMailListActivity.mLefttImg.setTextColor(Color.rgb(0, 0, 0));
        mMailListActivity.mRightImg.setTextColor(Color.rgb(0, 0, 0));
        mMailListActivity.mPullToRefreshView4.onFooterRefreshComplete();


    }

    public void initData() {
        mMailListActivity.mMailItemAdapter1 = new MailItemAdapter(mMailListActivity,mMailListActivity.mMailItems1);
        mMailListActivity.mMailItemAdapter2 = new MailItemAdapter(mMailListActivity,mMailListActivity.mMailItems2);
        mMailListActivity.mMailItemAdapter3 = new MailItemAdapter(mMailListActivity,mMailListActivity.mMailItems3);
        mMailListActivity.mMailItemAdapter4 = new MailItemAdapter(mMailListActivity,mMailListActivity.mMailItems4);
        mMailListActivity.mAllList.setAdapter(mMailListActivity.mMailItemAdapter1);
        mMailListActivity.mWaitList.setAdapter(mMailListActivity.mMailItemAdapter2);
        mMailListActivity.mAccessList.setAdapter(mMailListActivity.mMailItemAdapter3);
        mMailListActivity.mVoteList.setAdapter(mMailListActivity.mMailItemAdapter4);
        showMiddle();
    }

    public void onFoot()
    {
        getMails(false);
        mMailListActivity.mPullToRefreshView.onFooterRefreshComplete();
    }

    public void onHead()
    {
        getMails(true);
        mMailListActivity.mPullToRefreshView.onHeaderRefreshComplete();
    }

    public void startMail(Mail mail)
    {
        if(mMailListActivity.buttomlyaer.getVisibility() == View.VISIBLE)
        {
            if(mail.isSelect == false)
            {
                mail.isSelect = true;
            }
            else
            {
                mail.isSelect = false;
            }
            mMailListActivity.mMailItemAdapter.notifyDataSetChanged();
        }
        else
        {
            Intent mIntent = new Intent();
            mIntent.setClass(mMailListActivity, MailShowActivity.class);
            mIntent.putExtra("mail",mail);
            if(MailManager.getInstance().account.iscloud == false)
                mIntent.putExtra("type", mMailListActivity.type1);
            else
                mIntent.putExtra("type", mMailListActivity.type2);
            mMailListActivity.startActivity(mIntent);
        }
    }

    public void startDelete(Mail mail)
    {
        if(mMailListActivity.buttomlyaer.getVisibility() == View.INVISIBLE)
        {
            mMailListActivity.buttomlyaer.setVisibility(View.VISIBLE);
            if(mail.isSelect == false)
            {
                mail.isSelect = true;
            }
        }

    }

    public void deleteMail()
    {
        mMailListActivity.waitDialog.show();
        ArrayList<Mail> mails = new ArrayList<Mail>();
        for (int i = 0; i < mMailListActivity.mails.size(); i++) {
            if (mMailListActivity.mails.get(i).isSelect == true) {
                mails.add(mMailListActivity.mails.get(i));
            }
        }
        MailAsks.deleteMails(mMailListActivity,mMailListHandler,mails);
    }

    public void selectAll()
    {
        for(int i = 0 ; i < mMailListActivity.mails.size() ; i++)
        {
            mMailListActivity.mails.get(i).isSelect = true;
        }
        mMailListActivity.mMailItemAdapter.notifyDataSetChanged();
    }

    public void slectNone()
    {
        for(int i = 0 ; i < mMailListActivity.mails.size() ; i++)
        {
            mMailListActivity.mails.get(i).isSelect = false;
        }
        mMailListActivity.mMailItemAdapter.notifyDataSetChanged();
    }

    public void cancle()
    {
        mMailListActivity.buttomlyaer.setVisibility(View.INVISIBLE);
        slectNone();
    }

    public void wrightMail() {
        Intent mIntent = new Intent();
        mIntent.setClass(mMailListActivity, MailEditActivity.class);
        mIntent.putExtra("maildata",new Mail());
        mMailListActivity.startActivity(mIntent);
    }
}
