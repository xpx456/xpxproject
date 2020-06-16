package intersky.mail.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.mail.entity.Mail;
import intersky.mail.presenter.MailListPresenter;
import intersky.mail.view.adapter.LoderPageAdapter;
import intersky.mail.view.adapter.MailItemAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;

/**
 * Created by xpx on 2017/8/18.
 */

public class MailListActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    public static final int GET_MAIL_SHOUJIANXIAN = 1;
    public static final int GET_MAIL_FENFAXIAN = 7;
    public static final int GET_MAIL_FAJIANXIAN = 2;
    public static final int GET_MAIL_YIFANXIAN = 3;
    public static final int GET_MAIL_LAJIXIAN = 4;
    public static final int GET_MAIL_SHANCHUXIAN = 5;
    public static final int GET_MAIL_CAOGAOXIAN = 6;
    public static final int GET_MAIL_SHENGPIXIANG = 8;
    public static final int GET_MAIL_NEIBU = 9;

    public static final int GET_MAIL_CAOGAOXIAN_C = 0;
    public static final int GET_MAIL_SHENGPIXIANG_C = -1;
    public ArrayList<Mail> mails = new ArrayList<Mail>();
    public MailItemAdapter mMailItemAdapter;
    public ListView mListView;
    public PullToRefreshView mPullToRefreshView;
    public RelativeLayout buttomlyaer;
    public TextView mDelete;
    public int startPos;
    public boolean isAll = false;
    public int type1;
    public int type2;
    public String mailboxid = null;
    public MailListPresenter mMailListPresenter = new MailListPresenter(this);

    public NoScrollViewPager mViewPager;
    public RelativeLayout mRightTeb2;
    public RelativeLayout mRightTeb;
    public RelativeLayout mMiddeleTeb;
    public RelativeLayout mLefttTeb;
    public RelativeLayout mLine1;
    public RelativeLayout mLine2;
    public RelativeLayout mLine3;
    public RelativeLayout mLine4;
    public RelativeLayout mLine11;
    public RelativeLayout mLine21;
    public RelativeLayout mLine31;
    public RelativeLayout mLine41;
    public ListView mAllList;
    public ListView mWaitList;
    public ListView mAccessList;
    public ListView mVoteList;
    public TextView mRightImg;
    public TextView mMiddleImg;
    public TextView mLefttImg;
    public TextView mRightImg2;
    public TextView hit1;
    public TextView hit2;
    public TextView hit3;
    public TextView hit4;
    public PullToRefreshView mPullToRefreshView1;
    public PullToRefreshView mPullToRefreshView2;
    public PullToRefreshView mPullToRefreshView3;
    public PullToRefreshView mPullToRefreshView4;
    public LoderPageAdapter mLoderPageAdapter;
    public ArrayList<View> mViews = new ArrayList<View>();
    public int mStartpos1 = 1;
    public int mStartpos2 = 1;
    public int mStartpos3 = 1;
    public int mStartpos4 = 1;
    public boolean isall1 = false;
    public boolean isall2 = false;
    public boolean isall3 = false;
    public boolean isall4 = false;
    public MailItemAdapter mMailItemAdapter1;
    public ArrayList<Mail> mMailItems1 = new ArrayList<Mail>();
    public MailItemAdapter mMailItemAdapter2;
    public ArrayList<Mail> mMailItems2 = new ArrayList<Mail>();
    public MailItemAdapter mMailItemAdapter3;
    public ArrayList<Mail> mMailItems3 = new ArrayList<Mail>();
    public MailItemAdapter mMailItemAdapter4;
    public ArrayList<Mail> mMailItems4 = new ArrayList<Mail>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMailListPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMailListPresenter.Destroy();
        super.onDestroy();
    }

    public AdapterView.OnItemClickListener mailClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mMailListPresenter.startMail((Mail) parent.getAdapter().getItem(position));
        }
    };

    public AdapterView.OnItemLongClickListener mailLongClickListener = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mMailListPresenter.startDelete((Mail) parent.getAdapter().getItem(position));
            return false;
        }

    };

    public View.OnClickListener mWriteMailListener= new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mMailListPresenter.wrightMail();
        }
    };

    public View.OnClickListener deleteMailsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailListPresenter.deleteMail();
        }
    };

    public View.OnClickListener selectAllListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailListPresenter.selectAll();
        }
    };

    public View.OnClickListener selectCancleListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailListPresenter.cancle();
        }
    };

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mMailListPresenter.onFoot();

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mMailListPresenter.onHead();
    }


    public View.OnClickListener leftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailListPresenter.showLeft();
            if(mMailItems1.size() == 0)
                mMailListPresenter.getMails(false);
        }
    };

    public View.OnClickListener middleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mMailListPresenter.showMiddle();
            if(mMailItems2.size() == 0)
                mMailListPresenter.getMails(false);
        }
    };

    public View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailListPresenter.showRight();
            if(mMailItems3.size() == 0)
                mMailListPresenter.getMails(false);
        }
    };

    public View.OnClickListener rightClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailListPresenter.showRight2();
            if(mMailItems4.size() == 0)
                mMailListPresenter.getMails(false);
        }
    };
}
