package intersky.mail.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.Mail;
import intersky.mail.entity.MailBox;
import intersky.mail.entity.MailContact;
import intersky.mail.entity.SortMailContactComparator;
import intersky.mail.handler.MailHandler;
import intersky.mail.receiver.MailReceiver;
import intersky.mail.view.activity.MailActivity;
import intersky.mail.view.activity.MailEditActivity;
import intersky.mail.view.activity.MailListActivity;
import intersky.mail.view.adapter.MailContactAdapter;
import intersky.mywidget.MySlideBar;
import intersky.mywidget.PullToRefreshView;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class MailPresenter implements Presenter {

    public MailHandler mMailHandler;
    public MailActivity mMailActivity;
    public MailPresenter(MailActivity mMailActivity)
    {
        this.mMailActivity = mMailActivity;
        this.mMailHandler = new MailHandler(mMailActivity);
        mMailActivity.setBaseReceiver( new MailReceiver(mMailHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        if(MailManager.getInstance().account.iscloud == false)
        {
            mMailActivity.setContentView(R.layout.activity_mail);
            ToolBarHelper.setTitle3(mMailActivity.mActionBar, mMailActivity.getString(R.string.title_mymailbox), mMailActivity.mMailBoxSelectListener);
            ToolBarHelper.setBackListenr(mMailActivity.mActionBar, mMailActivity.mBackListener);
            ToolBarHelper.setRightBtn(mMailActivity.mActionBar, mMailActivity.mWriteMailListener, R.drawable.editnew);
            mMailActivity.mUserSearchView = mMailActivity.findViewById(R.id.user_search);

            mMailActivity.shoujianxiang = (RelativeLayout) mMailActivity.findViewById(R.id.shoujianxiang_laye);
            mMailActivity.shoujianxiangxin = (RelativeLayout) mMailActivity.findViewById(R.id.shoujianxiang_laye_xin);
            mMailActivity.shoujianxianglao = (RelativeLayout) mMailActivity.findViewById(R.id.shoujianxiang_laye_lao);
            mMailActivity.mMoreMailBox = (LinearLayout) mMailActivity.findViewById(R.id.morebox);
            mMailActivity.fenfaxiang = (RelativeLayout) mMailActivity.findViewById(R.id.fengfa_laye);
            mMailActivity.fenfaweilianxi = (RelativeLayout) mMailActivity.findViewById(R.id.fengfa_laye_weilianxi);
            mMailActivity.fenfalianxi = (RelativeLayout) mMailActivity.findViewById(R.id.fengfa_laye_lianxi);
            mMailActivity.fenfalianxifail = (RelativeLayout) mMailActivity.findViewById(R.id.fengfa_laye_lianxifial);
            mMailActivity.fenfalianxisuccess = (RelativeLayout) mMailActivity.findViewById(R.id.fengfa_laye_lianxisuccess);
            mMailActivity.neibuxiang = (RelativeLayout) mMailActivity.findViewById(R.id.neibu_laye);
            mMailActivity.fajianxiang = (RelativeLayout) mMailActivity.findViewById(R.id.fajian_laye);
            mMailActivity.fajianxiangdaishen = (RelativeLayout) mMailActivity.findViewById(R.id.fajian_laye_daishen);
            mMailActivity.fajianxiangtongguo = (RelativeLayout) mMailActivity.findViewById(R.id.fajian_laye_tongguo);
            mMailActivity.fajianxiangbohui = (RelativeLayout) mMailActivity.findViewById(R.id.fajian_laye_bohui);
            mMailActivity.yifaxiang = (RelativeLayout) mMailActivity.findViewById(R.id.yifa_laye);
            mMailActivity.lajixiang = (RelativeLayout) mMailActivity.findViewById(R.id.laji_laye);
            mMailActivity.shanchuxiang = (RelativeLayout) mMailActivity.findViewById(R.id.shanchu_laye);
            mMailActivity.caogaoxiang = (RelativeLayout) mMailActivity.findViewById(R.id.caogao_laye);
            mMailActivity.shenpixiang = (RelativeLayout) mMailActivity.findViewById(R.id.shenpi_laye);
            mMailActivity.shoujianCount = (TextView) mMailActivity.findViewById(R.id.shoujianxiang_count_text);
            mMailActivity.shoujianCountlao = (TextView) mMailActivity.findViewById(R.id.shoujianxiang_count_text_lao);
            mMailActivity.shoujianCountxin = (TextView) mMailActivity.findViewById(R.id.shoujianxiang_count_text_xin);
            mMailActivity.shenpiCount = (TextView) mMailActivity.findViewById(R.id.shenpi_count_text);
            mMailActivity.fenfaCount = (TextView) mMailActivity.findViewById(R.id.fengfa_count_text);
            mMailActivity.fenfaweilianxiCount = (TextView) mMailActivity.findViewById(R.id.fengfa_count_text_weilianxi);
            mMailActivity.fenfalianxiCount = (TextView) mMailActivity.findViewById(R.id.fengfa_count_text_lianxi);
            mMailActivity.fenfalianxifailCount = (TextView) mMailActivity.findViewById(R.id.fengfa_count_text_lianxifial);
            mMailActivity.fenfalianxisuccessCount = (TextView) mMailActivity.findViewById(R.id.fengfa_count_text_lianxisuccess);
            mMailActivity.fajianCount = (TextView) mMailActivity.findViewById(R.id.fajian_count_text);
            mMailActivity.fajiandaipiCount = (TextView) mMailActivity.findViewById(R.id.fajian_count_text_daishen);
            mMailActivity.fajiantongguoCount = (TextView) mMailActivity.findViewById(R.id.fajian_count_text_tongguo);
            mMailActivity.fajianbohuiCount = (TextView) mMailActivity.findViewById(R.id.fajian_count_text_bohui);
            mMailActivity.yifaCount = (TextView) mMailActivity.findViewById(R.id.yifa_count_text);
            mMailActivity.lajiCount = (TextView) mMailActivity.findViewById(R.id.laji_count_text);
            mMailActivity.shanchuCount = (TextView) mMailActivity.findViewById(R.id.shanchu_count_text);
            mMailActivity.caogaoCount = (TextView) mMailActivity.findViewById(R.id.caogao_count_text);
            mMailActivity.neibuCount = (TextView) mMailActivity.findViewById(R.id.neibu_count_text);

            mMailActivity.shoujianxiang.setOnClickListener(mMailActivity.mshoujianxiangListener);
            mMailActivity.fenfaxiang.setOnClickListener(mMailActivity.mfenfaxiangListener);
            mMailActivity.neibuxiang.setOnClickListener(mMailActivity.neibuListener);
            mMailActivity.fajianxiang.setOnClickListener(mMailActivity.mfajianxiangListener);
            mMailActivity.yifaxiang.setOnClickListener(mMailActivity.myifaxiangListener);
            mMailActivity.lajixiang.setOnClickListener(mMailActivity.mlajixiangListener);
            mMailActivity.shanchuxiang.setOnClickListener(mMailActivity.mshanchuxiangListener);
            mMailActivity.caogaoxiang.setOnClickListener(mMailActivity.mcaogaoxiangListener);
            mMailActivity.shenpixiang.setOnClickListener(mMailActivity.mshengpixiangListener);
            mMailActivity.shoujianxianglao.setOnClickListener(mMailActivity.mshoujianxianglaoListener);
            mMailActivity.shoujianxiangxin.setOnClickListener(mMailActivity.mshoujianxiangxinListener);
            mMailActivity.fajianxiangdaishen.setOnClickListener(mMailActivity.mfajianxiangdaishenListener);
            mMailActivity.fajianxiangtongguo.setOnClickListener(mMailActivity.mfajianxiangtongguoListener);
            mMailActivity.fajianxiangbohui.setOnClickListener(mMailActivity.mfajianxiangbohuiListener);
            mMailActivity.fenfaweilianxi.setOnClickListener(mMailActivity.mfenfaxiangweilianxiListener);
            mMailActivity.fenfalianxi.setOnClickListener(mMailActivity.mfenfaxianglianxiListener);
            mMailActivity.fenfalianxifail.setOnClickListener(mMailActivity.mfenfaxianglianxifailListener);
            mMailActivity.fenfalianxisuccess.setOnClickListener(mMailActivity.mfenfaxianglianxisuccessListener);

            mMailActivity.contactList = (ListView)mMailActivity.findViewById(R.id.contacts_List);
            mMailActivity.contactList.setOnItemClickListener(onContactItemClickListener);
            mMailActivity.searchView = mMailActivity.findViewById(R.id.searchuser);
            mMailActivity.msbar = (MySlideBar) mMailActivity.findViewById(R.id.slideBar);
            mMailActivity.mLetterText = (TextView) mMailActivity.findViewById(R.id.letter_text);
            mMailActivity.mRelativeLetter = (RelativeLayout) mMailActivity.findViewById(R.id.letter_layer);
            mMailActivity.msbar.setVisibility(View.INVISIBLE);
            mMailActivity.msbar.setOnTouchLetterChangeListenner(mOnTouchLetterChangeListenner);
            mMailActivity.searchView.mSetOnSearchListener(mOnSearchActionListener);
            mMailActivity.contactList.setOnScrollListener(mOnScoll);
            mMailActivity.msbar.setmRelativeLayout(mMailActivity.mRelativeLetter);
            mMailActivity.msbar.setMletterView(mMailActivity.mLetterText);
            if (mMailActivity.msbar != null) {
                String[] ss = new String[MailManager.getInstance().mMailUnderlineHeadUsers.size()];
                for (int i = 0; i < MailManager.getInstance().mMailUnderlineHeadUsers.size(); i++) {
                    ss[i] = MailManager.getInstance().mMailUnderlineHeadUsers.get(i).mName;
                }
                mMailActivity.msbar.setAddletters(ss);
                mMailActivity.msbar.requestLayout();
                mMailActivity.msbar.setVisibility(View.VISIBLE);
            }
            mMailActivity.mSearchContactAdapter = new MailContactAdapter(mMailActivity,mMailActivity.mSearchItems,true);
            mMailActivity.mMailUserAdapter = new MailContactAdapter(mMailActivity,MailManager.getInstance().mMailUnderlineUsers,true);
            mMailActivity.contactList.setAdapter(mMailActivity.mMailUserAdapter);
            checkMailBoxView();
            setnewcountView();
        }
        else
        {
            mMailActivity.setContentView(R.layout.activity_mail_cloud);
            mMailActivity.cloudshoujianxiang = (RelativeLayout) mMailActivity.findViewById(R.id.shoujianxiang_laye);
            mMailActivity.cloudshenpixiang = (RelativeLayout) mMailActivity.findViewById(R.id.shenpi_laye);
            mMailActivity.cloudyifaxiang = (RelativeLayout) mMailActivity.findViewById(R.id.yifa_laye);
            mMailActivity.cloudcaogaoxiang = (RelativeLayout) mMailActivity.findViewById(R.id.caogao_laye);
            mMailActivity.cloudhuishouxiang = (RelativeLayout) mMailActivity.findViewById(R.id.laji_laye);
            mMailActivity.cloudlajixiang = (RelativeLayout) mMailActivity.findViewById(R.id.shanchu_laye);
            mMailActivity.mMoreMailBox1 = (LinearLayout) mMailActivity.findViewById(R.id.shoujian_morebox);
            mMailActivity.mMoreMailBox2 = (LinearLayout) mMailActivity.findViewById(R.id.shenpi_morebox);
            mMailActivity.mMoreMailBox3 = (LinearLayout) mMailActivity.findViewById(R.id.yifa_morebox);
            mMailActivity.mMoreMailBox4 = (LinearLayout) mMailActivity.findViewById(R.id.caogao_morebox);
            mMailActivity.mMoreMailBox5 = (LinearLayout) mMailActivity.findViewById(R.id.laji_morebox);
            mMailActivity.mMoreMailBox6 = (LinearLayout) mMailActivity.findViewById(R.id.shanchu_morebox);

            mMailActivity.cloudshoujianhit = (TextView) mMailActivity.findViewById(R.id.shoujianxiang_count_text);
            mMailActivity.cloudshenpi = (TextView) mMailActivity.findViewById(R.id.shenpi_count_text);
            mMailActivity.cloudyifa = (TextView) mMailActivity.findViewById(R.id.yifa_count_text);
            mMailActivity.cloudcaogao = (TextView) mMailActivity.findViewById(R.id.caogao_count_text);
            mMailActivity.cloudhuishou = (TextView) mMailActivity.findViewById(R.id.laji_count_text);
            mMailActivity.cloudlaji = (TextView) mMailActivity.findViewById(R.id.shanchu_count_text);


            mMailActivity.cloudshoujianxiang.setOnClickListener(mMailActivity.mcshoujianxiangListener);
            mMailActivity.cloudshenpixiang.setOnClickListener(mMailActivity.mcshengpiListener);
            mMailActivity.cloudyifaxiang.setOnClickListener(mMailActivity.mcyifaListener);
            mMailActivity.cloudcaogaoxiang.setOnClickListener(mMailActivity.mccaogaoListener);
            mMailActivity.cloudhuishouxiang.setOnClickListener(mMailActivity.mchuishouListener);
            mMailActivity.cloudlajixiang.setOnClickListener(mMailActivity.mclajiListener);

            View mview2 = mMailActivity.getLayoutInflater().inflate(R.layout.mail_box_layer, null);
            Mailboxobj item= new Mailboxobj();
            item.state = -1;
            item.id = 1;
            mview2.setTag(item);
            mview2.setOnClickListener(mMailActivity.mailBoxListener);
            TextView name2 = (TextView) mview2.findViewById(R.id.shoujianxiang_text_xin);
            name2.setText("待我审批");
            mMailActivity.cloudshenpiw = (TextView) mview2.findViewById(R.id.shoujianxiang_count_text_xin);
            View mview3 = mMailActivity.getLayoutInflater().inflate(R.layout.mail_box_layer, null);
            item= new Mailboxobj();
            item.state = -1;
            item.id = 3;
            mview3.setTag(item);
            mview3.setOnClickListener(mMailActivity.mailBoxListener);
            TextView name3 = (TextView) mview3.findViewById(R.id.shoujianxiang_text_xin);
            name3.setText("我的审批");
            mMailActivity.cloudshenpim = (TextView) mview3.findViewById(R.id.shoujianxiang_count_text_xin);
            mMailActivity.mMoreMailBox2.addView(mview2);
            mMailActivity.mMoreMailBox2.addView(mview3);

            ToolBarHelper.setTitle(mMailActivity.mActionBar, mMailActivity.getString(R.string.keyword_mymailbox));
            checkMailBoxViewc();
            setnewcountViewCloud();
        }
        mMailActivity.mPullToRefreshView = (PullToRefreshView) mMailActivity.findViewById(R.id.mail_pull_refresh_view);
        mMailActivity.mSearchLayer = (RelativeLayout) mMailActivity.findViewById(R.id.search_layer);
        mMailActivity.mSearchView = (RelativeLayout) mMailActivity.findViewById(R.id.mail_search);
        mMailActivity.mMailSearchView = mMailActivity.findViewById(R.id.search);
        mMailActivity.mSearchLayer.setOnClickListener(mMailActivity.mOnShowSearchListener);
        mMailActivity.mMailSearchView.setOnCancleListener(mMailActivity.mHidSearchListener);
        mMailActivity.mMailSearchView.mSetOnSearchListener(mMailActivity.mOnEditorActionListener);
        mMailActivity.mPullToRefreshView.setOnFooterRefreshListener(mMailActivity);
        mMailActivity.mPullToRefreshView.setOnHeaderRefreshListener(mMailActivity);
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

    public void startMailList(int id, int type) {

        Intent mIntent = new Intent();
        mIntent.setClass(mMailActivity, MailListActivity.class);
        mIntent.putExtra("id", id);
        mIntent.putExtra("type", type);
        mMailActivity.startActivity(mIntent);

    }

    public void startMailList(int id, int type, MailBox mMailBox) {
        Intent mIntent = new Intent();
        mIntent.setClass(mMailActivity, MailListActivity.class);
        mIntent.putExtra("id", id);
        mIntent.putExtra("type", type);
        mIntent.putExtra("mailboxid", mMailBox.mRecordId);
        mIntent.putExtra("mailname", getMailName(mMailBox.mAddress));
        mMailActivity.startActivity(mIntent);
    }

    public void startMailList(Mailboxobj mMailBoxModel) {
        Intent mIntent = new Intent();
        mIntent.setClass(mMailActivity, MailListActivity.class);
        mIntent.putExtra("id", mMailBoxModel.id);
        mIntent.putExtra("type", mMailBoxModel.state);
        mIntent.putExtra("mailboxid", mMailBoxModel.mailboxid);
        mMailActivity.startActivity(mIntent);
    }

    public void checkMailBoxView() {
        LayoutInflater mInflater = (LayoutInflater) mMailActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMailActivity.mMoreMailBox.removeAllViews();
        for (int i = 0; i < MailManager.getInstance().mMailBoxs.size(); i++) {
            MailBox mMailBox = MailManager.getInstance().mMailBoxs.get(i);
            if (!mMailBox.isloacl) {
                View mview = mInflater.inflate(R.layout.mail_box_layer, null);

                mview.setTag(mMailBox);
                mview.setOnClickListener(mMailActivity.mailBoxListener);
                TextView name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                name.setText(mMailBox.mAddress);
                mMailActivity.mMoreMailBox.addView(mview);
            }

        }
        if (mMailActivity.shoujianOpened == true) {
            mMailActivity.shoujianxiangxin.setVisibility(View.VISIBLE);
            mMailActivity.shoujianxianglao.setVisibility(View.VISIBLE);
            if (mMailActivity.mMoreMailBox.getChildCount() > 0) {
                mMailActivity.mMoreMailBox.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.fajianxiang.getLayoutParams();
                mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.morebox);
                mMailActivity.fajianxiang.setLayoutParams(mFajianLayerParams);
            } else {
                RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.fajianxiang.getLayoutParams();
                mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shoujianxiang_laye_lao);
                mMailActivity.fajianxiang.setLayoutParams(mFajianLayerParams);
            }

        } else {
            mMailActivity.shoujianxiangxin.setVisibility(View.INVISIBLE);
            mMailActivity.shoujianxianglao.setVisibility(View.INVISIBLE);

            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.fajianxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shoujianxiang_laye);
            mMailActivity.fajianxiang.setLayoutParams(mFajianLayerParams);
        }
    }

    public void shoujianxiangListener() {
        if (mMailActivity.shoujianOpened == false) {
            mMailActivity.shoujianOpened = true;
            mMailActivity.shoujianxiangxin.setVisibility(View.VISIBLE);
            mMailActivity.shoujianxianglao.setVisibility(View.VISIBLE);
            if (mMailActivity.mMoreMailBox.getChildCount() > 0) {
                mMailActivity.mMoreMailBox.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.fajianxiang.getLayoutParams();
                mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.morebox);
                mMailActivity.fajianxiang.setLayoutParams(mFajianLayerParams);
            } else {
                RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.fajianxiang.getLayoutParams();
                mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shoujianxiang_laye_lao);
                mMailActivity.fajianxiang.setLayoutParams(mFajianLayerParams);
            }

        } else {
            mMailActivity.shoujianOpened = false;
            mMailActivity.shoujianxiangxin.setVisibility(View.INVISIBLE);
            mMailActivity.shoujianxianglao.setVisibility(View.INVISIBLE);

            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.fajianxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shoujianxiang_laye);
            mMailActivity.fajianxiang.setLayoutParams(mFajianLayerParams);
        }

    }

    public void fenfaxiangListener() {
        if (mMailActivity.fenfaOpened == false) {
            mMailActivity.fenfaOpened = true;
            mMailActivity.fenfaweilianxi.setVisibility(View.VISIBLE);
            mMailActivity.fenfalianxi.setVisibility(View.VISIBLE);
            mMailActivity.fenfalianxifail.setVisibility(View.VISIBLE);
            mMailActivity.fenfalianxisuccess.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.shenpixiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.fengfa_laye_lianxisuccess);
            mMailActivity.shenpixiang.setLayoutParams(mFajianLayerParams);
        } else {
            mMailActivity.fenfaOpened = false;
            mMailActivity.fenfaweilianxi.setVisibility(View.INVISIBLE);
            mMailActivity.fenfalianxi.setVisibility(View.INVISIBLE);
            mMailActivity.fenfalianxifail.setVisibility(View.INVISIBLE);
            mMailActivity.fenfalianxisuccess.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.shenpixiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.fengfa_laye);
            mMailActivity.shenpixiang.setLayoutParams(mFajianLayerParams);
        }
    }

    public void fajianxiangListener() {
        if (mMailActivity.fajianOpened == false) {
            mMailActivity.fajianOpened = true;
            mMailActivity.fajianxiangdaishen.setVisibility(View.VISIBLE);
            mMailActivity.fajianxiangtongguo.setVisibility(View.VISIBLE);
            mMailActivity.fajianxiangbohui.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.fenfaxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.fajian_laye_bohui);
            mMailActivity.fenfaxiang.setLayoutParams(mFajianLayerParams);
        } else {
            mMailActivity.fajianOpened = false;
            mMailActivity.fajianxiangdaishen.setVisibility(View.INVISIBLE);
            mMailActivity.fajianxiangtongguo.setVisibility(View.INVISIBLE);
            mMailActivity.fajianxiangbohui.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.fenfaxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.fajian_laye);
            mMailActivity.fenfaxiang.setLayoutParams(mFajianLayerParams);
        }
    }

    public void mShowSearchListener() {

        mMailActivity.mSearchView.setVisibility(View.VISIBLE);
        mMailActivity.mMailSearchView.showEdit();
        mMailActivity.mMailItems.clear();
    }

    public void hidSearch()
    {
        mMailActivity.mSearchView.setVisibility(View.INVISIBLE);
        mMailActivity.mMailSearchView.hidEdit();
        mMailActivity.mMailSearchView.cleanText();
    }

    public void showUserids() {
        mMailActivity.mUserSearchView.setVisibility(View.VISIBLE);

    }

    public TextView.OnEditorActionListener mOnSearchActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                doSearch(mMailActivity.searchView.getText());

            }
            return true;
        }
    };

    public AbsListView.OnScrollListener mOnScoll = new AbsListView.OnScrollListener()
    {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(mMailActivity.searchView.ishow)
            {
                if(mMailActivity.searchView.getText().length() == 0)
                {
                    mMailActivity.searchView.hidEdit();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

    public MySlideBar.OnTouchLetterChangeListenner mOnTouchLetterChangeListenner = new MySlideBar.OnTouchLetterChangeListenner()
    {

        @Override
        public void onTouchLetterChange(MotionEvent event, int s)
        {
            // TODO Auto-generated method stub
            letterChange(s);

        }
    };

    public AdapterView.OnItemClickListener onContactItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            contactsClick((MailContact) parent.getAdapter().getItem(position));

        }
    };

    public void wrightMail() {
        Intent mIntent = new Intent();
        mIntent.setClass(mMailActivity, MailEditActivity.class);
        mIntent.putExtra("maildata",new Mail());
        mMailActivity.startActivity(mIntent);
    }


    public void doBackListenre() {
        if(mMailActivity.mSearchView.getVisibility() == View.VISIBLE)
        {
            hidSearch();
        }
        else
        {
            mMailActivity.finish();
        }
    }

    public void setnewcountViewCloud() {

        AppUtils.setHit(MailManager.getInstance().allcount1, mMailActivity.cloudshoujianhit);
        AppUtils.setHit(MailManager.getInstance().allcount1, mMailActivity.shoujianhits.get("0"));
        AppUtils.setHit(MailManager.getInstance().me_approval, mMailActivity.cloudshenpim);
        AppUtils.setHit(MailManager.getInstance().to_me_approval, mMailActivity.cloudshenpiw);
        for(int i = 0 ; i < MailManager.getInstance().mMailBoxs.size(); i++)
        {
            AppUtils.setHit(MailManager.getInstance().mMailBoxs.get(i).count1,mMailActivity.shoujianhits.get(MailManager.getInstance().mMailBoxs.get(i).mRecordId));
            AppUtils.setHit(MailManager.getInstance().mMailBoxs.get(i).count2,mMailActivity.yifahits.get(MailManager.getInstance().mMailBoxs.get(i).mRecordId));
            AppUtils.setHit(MailManager.getInstance().mMailBoxs.get(i).count3,mMailActivity.caogaohits.get(MailManager.getInstance().mMailBoxs.get(i).mRecordId));
            AppUtils.setHit(MailManager.getInstance().mMailBoxs.get(i).count4,mMailActivity.huishouhits.get(MailManager.getInstance().mMailBoxs.get(i).mRecordId));
            AppUtils.setHit(MailManager.getInstance().mMailBoxs.get(i).count5,mMailActivity.lajihits.get(MailManager.getInstance().mMailBoxs.get(i).mRecordId));

        }
        AppUtils.setHit(MailManager.getInstance().allcount2, mMailActivity.yifahits.get("0"));
        AppUtils.setHit(MailManager.getInstance().allcount3, mMailActivity.caogaohits.get("0"));
        AppUtils.setHit(MailManager.getInstance().allcount4, mMailActivity.huishouhits.get("0"));
        AppUtils.setHit(MailManager.getInstance().allcount5, mMailActivity.lajihits.get("0"));
        AppUtils.setHit(MailManager.getInstance().me_approval+MailManager.getInstance().to_me_approval, mMailActivity.cloudshenpi);
        AppUtils.setHit(MailManager.getInstance().allcount2, mMailActivity.cloudyifa);
        AppUtils.setHit(MailManager.getInstance().allcount3, mMailActivity.cloudcaogao);
        AppUtils.setHit(MailManager.getInstance().allcount4, mMailActivity.cloudhuishou);
        AppUtils.setHit(MailManager.getInstance().allcount5, mMailActivity.cloudlaji);
    }


    public void setnewcountView() {

        AppUtils.setHit(MailManager.getInstance().shoujiancounts, mMailActivity.shoujianCount);
        AppUtils.setHit(MailManager.getInstance().laocounts, mMailActivity.shoujianCountlao);
        AppUtils.setHit(MailManager.getInstance().xincounts, mMailActivity.shoujianCountxin);
        AppUtils.setHit(MailManager.getInstance().shenpikcounts, mMailActivity.shenpiCount);
        AppUtils.setHit(MailManager.getInstance().fajiancounts, mMailActivity.fajianCount);
        AppUtils.setHit(MailManager.getInstance().fajiandaipicounts, mMailActivity.fajiandaipiCount);
        AppUtils.setHit(MailManager.getInstance().fajiantongguocounts, mMailActivity.fajiantongguoCount);
        AppUtils.setHit(MailManager.getInstance().fajianbohuicounts, mMailActivity.fajianbohuiCount);
        AppUtils.setHit(MailManager.getInstance().fenfacounts, mMailActivity.fenfaCount);
        AppUtils.setHit(MailManager.getInstance().fenfaweilianxicounts, mMailActivity.fenfaweilianxiCount);
        AppUtils.setHit(MailManager.getInstance().fenfalianxicounts, mMailActivity.fenfalianxiCount);
        AppUtils.setHit(MailManager.getInstance().fenfalianxifailcounts, mMailActivity.fenfalianxifailCount);
        AppUtils.setHit(MailManager.getInstance().fenfalianxitongguocounts, mMailActivity.fenfalianxisuccessCount);
        AppUtils.setHit(MailManager.getInstance().neibuxiangcounts, mMailActivity.neibuCount);
    }

    private void letterChange(int s) {
        MailContact model = MailManager.getInstance().mMailUnderlineHeadUsers.get(s);
        int a = MailManager.getInstance().mMailUnderlineUsers.indexOf(model);
        ListView list = mMailActivity.contactList;
        list.setSelectionFromTop(a, 0);
    }

    private void contactsClick(MailContact contacts) {
        mMailActivity.mUserSearchView.setVisibility(View.INVISIBLE);
        if(!contacts.mailRecordID.equals(MailManager.getInstance().mSelectUser.mailRecordID))
        {
            MailAsks.setUser(mMailActivity,mMailHandler,contacts);
        }
    }

    private String getMailName(String name) {
        if (name.indexOf("(") > 0) {
            return name.substring(0, name.indexOf("("));
        } else {
            return name;
        }

    }

    private void doSearch(String keyword) {
        if(keyword.length() == 0)
        {
            if(mMailActivity.isShowSearch == true)
            {
                mMailActivity.isShowSearch = false;
                mMailActivity.contactList.setAdapter(mMailActivity.mMailUserAdapter);
            }
            return;
        }
        boolean typebooleans[] = new boolean[27];
        ArrayList<MailContact> temps = new ArrayList<MailContact>();
        ArrayList<MailContact> tempheads = new ArrayList<MailContact>();
        for (int i = 0; i < MailManager.getInstance().mMailUnderlineUsers.size(); i++) {
            MailContact mContactModel = MailManager.getInstance().mMailUnderlineUsers.get(i);
            if (mContactModel.type == 0) {
                if (mContactModel.pingyin.contains(keyword.toLowerCase()) || mContactModel.mName.contains(keyword)) {
                    temps.add(mContactModel);

                    String s = mContactModel.pingyin.substring(0, 1).toUpperCase();
                    int pos = CharacterParser.typeLetter.indexOf(s);
                    if (pos != -1) {
                        if (typebooleans[pos] == false) {
                            tempheads.add(new MailContact(s,""));
                            typebooleans[pos] = true;
                        }
                    }
                    else
                    {
                        s = "#";
                        pos = CharacterParser.typeLetter.indexOf(s);
                        if (pos != -1) {
                            if (typebooleans[pos] == false) {
                                tempheads.add(new MailContact(s,""));
                                typebooleans[pos] = true;
                            }
                        }
                    }
                }
            }
        }
        if (temps.size() == 0) {
            AppUtils.showMessage(mMailActivity, mMailActivity.getString(R.string.searchview_search_none));
        } else {
            mMailActivity.mSearchItems.clear();
            mMailActivity.mSearchHeadItems.clear();
            mMailActivity.mSearchItems.addAll(temps);
            mMailActivity.mSearchHeadItems.addAll(tempheads);
            mMailActivity.mSearchItems.addAll(0, mMailActivity.mSearchHeadItems);
            Collections.sort(mMailActivity.mSearchItems, new SortMailContactComparator());
            Collections.sort(mMailActivity.mSearchHeadItems, new SortMailContactComparator());
            mMailActivity.contactList.setAdapter(mMailActivity.mSearchContactAdapter);
            mMailActivity.isShowSearch = true;
        }


    }

    public void MailSearchEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if(MailManager.getInstance().account.iscloud == false)
            MailAsks.searchMials(mMailActivity,mMailActivity.mMailPresenter.mMailHandler,mMailActivity.mMailSearchView.getText());
            else
                MailAsks.searchMialsc(mMailActivity,mMailActivity.mMailPresenter.mMailHandler,mMailActivity.mMailSearchView.getText());
        }
    }

    public class Mailboxobj{
        int state = 1;
        int id = -1;
        String mailboxid = "";
    }

    public void cshoujianxiangListener() {
        if (mMailActivity.shoujianOpened1 == false) {
            mMailActivity.shoujianOpened1 = true;
            mMailActivity.mMoreMailBox1.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudshenpixiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shoujian_morebox);
            mMailActivity.cloudshenpixiang.setLayoutParams(mFajianLayerParams);

        } else {
            mMailActivity.shoujianOpened1 = false;
            mMailActivity.mMoreMailBox1.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudshenpixiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shoujianxiang_laye);
            mMailActivity.cloudshenpixiang.setLayoutParams(mFajianLayerParams);
        }

    }

    public void cshengpiListener() {
        if (mMailActivity.shoujianOpened2 == false) {
            mMailActivity.shoujianOpened2 = true;
            mMailActivity.mMoreMailBox2.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudyifaxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shenpi_morebox);
            mMailActivity.cloudyifaxiang.setLayoutParams(mFajianLayerParams);

        } else {
            mMailActivity.shoujianOpened2 = false;
            mMailActivity.mMoreMailBox2.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudyifaxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shenpi_laye);
            mMailActivity.cloudyifaxiang.setLayoutParams(mFajianLayerParams);
        }

    }

    public void cyifaListener() {
        if (mMailActivity.shoujianOpened3 == false) {
            mMailActivity.shoujianOpened3 = true;
            mMailActivity.mMoreMailBox3.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudcaogaoxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.yifa_morebox);
            mMailActivity.cloudcaogaoxiang.setLayoutParams(mFajianLayerParams);

        } else {
            mMailActivity.shoujianOpened3 = false;
            mMailActivity.mMoreMailBox3.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudcaogaoxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.yifa_laye);
            mMailActivity.cloudcaogaoxiang.setLayoutParams(mFajianLayerParams);
        }

    }

    public void ccaogaoListener() {
        if (mMailActivity.shoujianOpened4 == false) {
            mMailActivity.shoujianOpened4 = true;
            mMailActivity.mMoreMailBox4.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudhuishouxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.caogao_morebox);
            mMailActivity.cloudhuishouxiang.setLayoutParams(mFajianLayerParams);

        } else {
            mMailActivity.shoujianOpened4 = false;
            mMailActivity.mMoreMailBox4.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudhuishouxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.caogao_laye);
            mMailActivity.cloudhuishouxiang.setLayoutParams(mFajianLayerParams);
        }

    }

    public void chuishouListener() {
        if (mMailActivity.shoujianOpened5 == false) {
            mMailActivity.shoujianOpened5 = true;
            mMailActivity.mMoreMailBox5.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudlajixiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.laji_morebox);
            mMailActivity.cloudlajixiang.setLayoutParams(mFajianLayerParams);
        } else {
            mMailActivity.shoujianOpened5 = false;
            mMailActivity.mMoreMailBox5.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudlajixiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.laji_laye);
            mMailActivity.cloudlajixiang.setLayoutParams(mFajianLayerParams);
        }

    }

    public void clajiListener() {
        if (mMailActivity.shoujianOpened6 == false) {
            mMailActivity.shoujianOpened6 = true;
            if (mMailActivity.mMoreMailBox6.getChildCount() > 0) {
                mMailActivity.mMoreMailBox6.setVisibility(View.VISIBLE);
            } else {
            }

        } else {
            mMailActivity.shoujianOpened6 = false;
            mMailActivity.mMoreMailBox6.setVisibility(View.INVISIBLE);
        }

    }

    public void checkMailBoxViewc() {
        LayoutInflater mInflater = (LayoutInflater) mMailActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMailActivity.mMoreMailBox1.removeAllViews();
        mMailActivity.mMoreMailBox3.removeAllViews();
        mMailActivity.mMoreMailBox4.removeAllViews();
        mMailActivity.mMoreMailBox5.removeAllViews();
        mMailActivity.mMoreMailBox6.removeAllViews();
        View mview1 = mInflater.inflate(R.layout.mail_box_layer, null);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        Mailboxobj item= new Mailboxobj();
        item.state = 1;
        mview1.setTag(item);
        TextView name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText("全部");
        TextView textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        getcount(MailManager.getInstance().mMailcount,textViewhit);
        mMailActivity.shoujianhits.put("0",textViewhit);
        mMailActivity.mMoreMailBox1.addView(mview1);
        mview1 = mInflater.inflate(R.layout.mail_box_layer, null);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        item= new Mailboxobj();
        item.state = 2;
        mview1.setTag(item);
        name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText("全部");
        textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        mMailActivity.yifahits.put("0",textViewhit);
        getcount(MailManager.getInstance().allcount1,textViewhit);
        mMailActivity.mMoreMailBox3.addView(mview1);
        mview1 = mInflater.inflate(R.layout.mail_box_layer, null);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        item= new Mailboxobj();
        item.state = 0;
        mview1.setTag(item);
        name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText("全部");
        textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        mMailActivity.caogaohits.put("0",textViewhit);
        getcount(MailManager.getInstance().allcount2,textViewhit);
        mMailActivity.mMoreMailBox4.addView(mview1);
        mview1 = mInflater.inflate(R.layout.mail_box_layer, null);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        item= new Mailboxobj();
        item.state = 3;
        mview1.setTag(item);
        name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText("全部");
        textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        mMailActivity.huishouhits.put("0",textViewhit);
        getcount(MailManager.getInstance().allcount3,textViewhit);
        mMailActivity.mMoreMailBox5.addView(mview1);
        mview1 = mInflater.inflate(R.layout.mail_box_layer, null);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        item= new Mailboxobj();
        item.state = 4;
        mview1.setTag(item);
        name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText("全部");
        textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        mMailActivity.lajihits.put("0",textViewhit);
        getcount(MailManager.getInstance().allcount4,textViewhit);
        mMailActivity.mMoreMailBox6.addView(mview1);

        for (int i = 0; i < MailManager.getInstance().mMailBoxs.size(); i++) {
            MailBox mMailBox = MailManager.getInstance().mMailBoxs.get(i);
            if (!mMailBox.isloacl) {
                View mview = mInflater.inflate(R.layout.mail_box_layer, null);
                item= new Mailboxobj();
                item.state = 1;
                item.mailboxid = mMailBox.mRecordId;
                mview.setTag(item);
                mview.setOnClickListener(mMailActivity.mailBoxListener);
                TextView name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                name.setText(mMailBox.mAddress);
                TextView textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                mMailActivity.shoujianhits.put(mMailBox.mRecordId,textViewhit1);
                getcount(mMailBox.count1,textViewhit1);
                mMailActivity.mMoreMailBox1.addView(mview);
                mview = mInflater.inflate(R.layout.mail_box_layer, null);
                item= new Mailboxobj();
                item.state = 2;
                item.mailboxid = mMailBox.mRecordId;
                mview.setTag(item);
                mview.setOnClickListener(mMailActivity.mailBoxListener);
                name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                name.setText(mMailBox.mAddress);
                textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                mMailActivity.yifahits.put(mMailBox.mRecordId,textViewhit1);
                getcount(mMailBox.count2,textViewhit1);
                mMailActivity.mMoreMailBox3.addView(mview);
                mview = mInflater.inflate(R.layout.mail_box_layer, null);
                item= new Mailboxobj();
                item.state = 0;
                item.mailboxid = mMailBox.mRecordId;
                mview.setTag(item);
                mview.setOnClickListener(mMailActivity.mailBoxListener);
                name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                name.setText(mMailBox.mAddress);
                textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                mMailActivity.caogaohits.put(mMailBox.mRecordId,textViewhit1);
                getcount(mMailBox.count3,textViewhit1);
                mMailActivity.mMoreMailBox4.addView(mview);
                mview = mInflater.inflate(R.layout.mail_box_layer, null);
                item= new Mailboxobj();
                item.state = 3;
                item.mailboxid = mMailBox.mRecordId;
                mview.setTag(item);
                mview.setOnClickListener(mMailActivity.mailBoxListener);
                name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                name.setText(mMailBox.mAddress);
                textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                mMailActivity.huishouhits.put(mMailBox.mRecordId,textViewhit1);
                getcount(mMailBox.count4,textViewhit1);
                mMailActivity.mMoreMailBox5.addView(mview);
                mview = mInflater.inflate(R.layout.mail_box_layer, null);
                item= new Mailboxobj();
                item.state = 4;
                item.mailboxid = mMailBox.mRecordId;
                mview.setTag(item);
                mview.setOnClickListener(mMailActivity.mailBoxListener);
                name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                name.setText(mMailBox.mAddress);
                textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                mMailActivity.lajihits.put(mMailBox.mRecordId,textViewhit1);
                getcount(mMailBox.count5,textViewhit1);
                mMailActivity.mMoreMailBox6.addView(mview);

            }

        }
    }

    private void getcount(int count, TextView mTextView) {
        if(count == 0)
        {
            mTextView.setVisibility(View.INVISIBLE);
        }
        else
        {
            mTextView.setVisibility(View.VISIBLE);
            if (count > 99) {
                mTextView.setText(String.valueOf(99) + "+");
            } else {
                mTextView.setText(String.valueOf(count));
            }
        }

    }
}
