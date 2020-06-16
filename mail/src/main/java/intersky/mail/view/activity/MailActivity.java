package intersky.mail.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.BaseActivity;
import intersky.mail.MailManager;
import intersky.mail.entity.Mail;
import intersky.mail.entity.MailBox;
import intersky.mail.entity.MailContact;
import intersky.mail.presenter.MailPresenter;
import intersky.mail.view.adapter.MailContactAdapter;
import intersky.mywidget.MySlideBar;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;

/**
 * Created by xpx on 2017/8/18.
 */

public class MailActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    public MailPresenter mMailPresenter = new MailPresenter(this);
    public RelativeLayout mSearchLayer;
    public RelativeLayout mSearchView;
    public SearchViewLayout mMailSearchView;
    public RelativeLayout mUserSearchView;
    public PullToRefreshView mPullToRefreshView;
    public RelativeLayout shoujianxiang;
    public RelativeLayout shoujianxiangxin;
    public RelativeLayout shoujianxianglao;
    public LinearLayout mMoreMailBox;
    public RelativeLayout fenfaxiang;
    public RelativeLayout neibuxiang;
    public RelativeLayout fenfaweilianxi;
    public RelativeLayout fenfalianxi;
    public RelativeLayout fenfalianxifail;
    public RelativeLayout fenfalianxisuccess;
    public RelativeLayout fajianxiang;
    public RelativeLayout fajianxiangdaishen;
    public RelativeLayout fajianxiangtongguo;
    public RelativeLayout fajianxiangbohui;
    public RelativeLayout yifaxiang;
    public RelativeLayout lajixiang;
    public RelativeLayout shanchuxiang;
    public RelativeLayout caogaoxiang;
    public RelativeLayout shenpixiang;
    public TextView shoujianCount;
    public TextView shoujianCountlao;
    public TextView shoujianCountxin;
    public TextView shenpiCount;
    public TextView fenfaCount;
    public TextView fenfaweilianxiCount;
    public TextView fenfalianxiCount;
    public TextView fenfalianxifailCount;
    public TextView fenfalianxisuccessCount;
    public TextView fajianCount;
    public TextView fajiandaipiCount;
    public TextView fajiantongguoCount;
    public TextView fajianbohuiCount;
    public TextView neibuCount;
    public TextView yifaCount;
    public TextView lajiCount;
    public TextView shanchuCount;
    public TextView caogaoCount;
    public ListView contactList;
    public SearchViewLayout searchView;
    public MySlideBar msbar;
    public TextView mLetterText;
    public RelativeLayout mRelativeLetter;

    public boolean isShowSearch = false;
    public boolean shoujianOpened = false;
    public boolean fajianOpened = false;
    public boolean fenfaOpened = false;
    public MailContactAdapter mMailUserAdapter;
    public ArrayList<Mail> mMailItems = new ArrayList<Mail>();
    public ArrayList<MailContact> mSearchItems = new ArrayList<MailContact>();
    public ArrayList<MailContact> mSearchHeadItems = new ArrayList<MailContact>();
    public MailContactAdapter mSearchContactAdapter;


    public boolean shoujianOpened1 = false;
    public boolean shoujianOpened2 = false;
    public boolean shoujianOpened3 = false;
    public boolean shoujianOpened4 = false;
    public boolean shoujianOpened5 = false;
    public boolean shoujianOpened6 = false;
    public RelativeLayout cloudshoujianxiang;
    public LinearLayout mMoreMailBox1;
    public RelativeLayout cloudshenpixiang;
    public LinearLayout mMoreMailBox2;
    public RelativeLayout cloudyifaxiang;
    public LinearLayout mMoreMailBox3;
    public RelativeLayout cloudcaogaoxiang;
    public LinearLayout mMoreMailBox4;
    public RelativeLayout cloudhuishouxiang;
    public LinearLayout mMoreMailBox5;
    public RelativeLayout cloudlajixiang;
    public LinearLayout mMoreMailBox6;
    public TextView cloudshoujianhit;
    public TextView cloudshenpi;
    public TextView cloudyifa;
    public TextView cloudcaogao;
    public TextView cloudhuishou;
    public TextView cloudlaji;
    public TextView cloudshenpiw;
    public TextView cloudshenpim;
    public HashMap<String,TextView> shoujianhits = new HashMap<String,TextView>();
    public HashMap<String,TextView> yifahits = new HashMap<String,TextView>();
    public HashMap<String,TextView> caogaohits = new HashMap<String,TextView>();
    public HashMap<String,TextView> huishouhits = new HashMap<String,TextView>();
    public HashMap<String,TextView> lajihits = new HashMap<String,TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMailPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMailPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mshoujianxianglaoListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_SHOUJIANXIAN, 1);

        }
    };

    public View.OnClickListener mshoujianxiangxinListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_SHOUJIANXIAN, 2);

        }
    };

    public View.OnClickListener mshengpixiangListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_SHENGPIXIANG,0);

        }
    };

    public View.OnClickListener mshoujianxiangListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.shoujianxiangListener();
        }
    };

    public View.OnClickListener mfenfaxiangListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.fenfaxiangListener();
        }
    };

    public View.OnClickListener mfenfaxiangweilianxiListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FENFAXIAN, 0);

        }
    };

    public View.OnClickListener mfenfaxianglianxiListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FENFAXIAN, 1);

        }
    };

    public View.OnClickListener mfenfaxianglianxifailListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FENFAXIAN, 2);

        }
    };

    public View.OnClickListener mfenfaxianglianxisuccessListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FENFAXIAN, 3);

        }
    };

    public View.OnClickListener mfajianxiangdaishenListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FAJIANXIAN, 1);

        }
    };

    public View.OnClickListener mfajianxiangtongguoListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FAJIANXIAN, 2);

        }
    };

    public View.OnClickListener mfajianxiangbohuiListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FAJIANXIAN, 3);

        }
    };

    public View.OnClickListener mfajianxiangListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.fajianxiangListener();
        }
    };

    public View.OnClickListener neibuListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_NEIBU,0);
        }
    };

    public View.OnClickListener myifaxiangListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_YIFANXIAN,0);

        }
    };

    public View.OnClickListener mlajixiangListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_LAJIXIAN,0);

        }
    };

    public View.OnClickListener mshanchuxiangListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_SHANCHUXIAN,0);

        }
    };

    public View.OnClickListener mcaogaoxiangListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_CAOGAOXIAN,0);

        }
    };

    public View.OnClickListener mailBoxListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            if(MailManager.getInstance().account.iscloud == false)
                mMailPresenter.startMailList(MailListActivity.GET_MAIL_SHOUJIANXIAN,0,(MailBox)v.getTag());
            else
            {
                mMailPresenter.startMailList((MailPresenter.Mailboxobj) v.getTag());
            }
        }
    };

    public View.OnClickListener mOnShowSearchListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {
            mMailPresenter.mShowSearchListener();
        }
    };

    public SearchViewLayout.DoCancle mHidSearchListener = new SearchViewLayout.DoCancle()
    {

        @Override
        public void doCancle() {
            mMailPresenter.hidSearch();
        }
    };


    public View.OnClickListener mMailBoxSelectListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.showUserids();
        }
    };

    public View.OnClickListener mBackListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.doBackListenre();

        }
    };

    public View.OnClickListener mWriteMailListener= new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.wrightMail();
        }
    };


    public TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            mMailPresenter.MailSearchEditorAction(v, actionId, event);
            return true;
        }
    };

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        MailManager.getInstance().getReadCount();
        view.onFooterRefreshComplete();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        MailManager.getInstance().getReadCount();
        view.onHeaderRefreshComplete();
    }


    public View.OnClickListener mcshoujianxiangListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.cshoujianxiangListener();
        }
    };

    public View.OnClickListener mcshengpiListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.cshengpiListener();
        }
    };

    public View.OnClickListener mcyifaListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.cyifaListener();
        }
    };

    public View.OnClickListener mccaogaoListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.ccaogaoListener();
        }
    };

    public View.OnClickListener mchuishouListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.chuishouListener();
        }
    };

    public View.OnClickListener mclajiListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailPresenter.clajiListener();
        }
    };
}
