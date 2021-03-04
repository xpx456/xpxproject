package intersky.mail.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.entity.Contacts;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.Mail;
import intersky.mail.entity.MailBox;
import intersky.mail.entity.MailContact;
import intersky.mail.entity.MailType;
import intersky.mail.presenter.MailPresenter;
import intersky.mail.view.adapter.LoderPageAdapter;
import intersky.mail.view.adapter.MailContactAdapter;
import intersky.mail.view.adapter.MailContactsAdapter;
import intersky.mail.view.adapter.MailItemAdapter;
import intersky.mywidget.MySlideBar;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import intersky.select.entity.Select;

/**
 * Created by xpx on 2017/8/18.
 */

public class MailActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    public static final int RECEIVE_TIME = 60;
    public static final String ACTION_UPDATA_MAILS = "ACTION_UPDATA_MAILS";
    public static final String ACTION_UPDATA_ALL_MAILS = "ACTION_UPDATA_ALL_MAILS";
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
    public MailContactsAdapter mMailUserAdapter;
    public ArrayList<Mail> mMailItems = new ArrayList<Mail>();
    public ArrayList<MailContact> mSearchItems = new ArrayList<MailContact>();
    public ArrayList<MailContact> mSearchHeadItems = new ArrayList<MailContact>();
    public MailContactsAdapter mSearchContactAdapter;


    public boolean shoujianOpened1 = false;
    public boolean shoujianOpened2 = false;
    public boolean shoujianOpened3 = false;
    public boolean shoujianOpened4 = false;
    public boolean shoujianOpened5 = false;
    public boolean shoujianOpened6 = false;
    public boolean shoujianOpened7 = false;
    public boolean shoujianOpened8 = false;
    public boolean shoujianOpened9 = false;

    public RelativeLayout cloudUnreadMail;
    public ImageView cloudUnreadMailimg;
    public TextView cloudUnreadMailtxt;
    public RelativeLayout cloudshoujianxiang;
    public ImageView cloudshoujianxiangimg;
    public LinearLayout mMoreMailBox1;
    public RelativeLayout cloudshenpixiang;
    public ImageView cloudshenpixiangimg;
    public LinearLayout mMoreMailBox2;
    public RelativeLayout cloudyifaxiang;
    public ImageView cloudyifaxiangimg;
    public LinearLayout mMoreMailBox3;
    public RelativeLayout cloudcaogaoxiang;
    public ImageView cloudcaogaoxiangimg;
    public LinearLayout mMoreMailBox4;
    public RelativeLayout cloudhuishouxiang;
    public ImageView cloudhuishouxiangimg;
    public LinearLayout mMoreMailBox5;
    public RelativeLayout cloudlajixiang;
    public ImageView cloudlajixiangimg;
    public LinearLayout mMoreMailBox6;

    public RelativeLayout cloudall;
    public ImageView cloudallimg;
    public LinearLayout mMoreMailBox7;

    public RelativeLayout cloudtab;
    public ImageView cloudtabimg;
    public ImageView cloudtabaddimg;
    public LinearLayout mMoreMailBox8;

    public RelativeLayout cloudfile;
    public ImageView cloudfileimg;
    public ImageView cloudfileaddimg;
    public LinearLayout mMoreMailBox9;


    public RelativeLayout cloudtype;
    public ImageView cloudtypeimg;
    public LinearLayout mMoreMailBox10;

    public TextView bntUsers;

    public TextView cloudunreadhit;
    public TextView cloudshoujianhit;
    public TextView cloudshenpi;
    public TextView cloudyifa;
    public TextView cloudcaogao;
    public TextView cloudhuishou;
    public TextView cloudlaji;
    public TextView cloudshenpiw;
    public TextView cloudshenpim;
    public HashMap<String, TextView> shoujianhits = new HashMap<String, TextView>();
    public HashMap<String, TextView> yifahits = new HashMap<String, TextView>();
    public HashMap<String, TextView> caogaohits = new HashMap<String, TextView>();
    public HashMap<String, TextView> huishouhits = new HashMap<String, TextView>();
    public HashMap<String, TextView> lajihits = new HashMap<String, TextView>();

    public ListView mListView;
    public ArrayList<Mail> mails = new ArrayList<Mail>();
    public MailItemAdapter mMailItemAdapter;
    public int startPos;
    public boolean isAll = false;
    public View leftView;
    public View userView;

    public int type1 = 0;
    public int type2 = 0;
    public int type3 = 0;
    public String mailboxid = null;
    public String mailname = null;
    //    public TextView mDelete;
    public TextView manager;
    public PopupWindow popupWindow;
    public PopupWindow popupWindow2;
    public TextView mcount;
    public RelativeLayout mTab;
    public NoScrollViewPager mViewPager;
    public RelativeLayout mRightTeb2;
    public RelativeLayout mRightTeb;
    public RelativeLayout mMiddeleTeb;
    public RelativeLayout mLefttTeb;
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
    public RelativeLayout shade;
    public View select;
    public TextView btnReceive;
    public int receiveTime = 0;
    public RelativeLayout bReceive;
    public ImageView iReceive;
    public LinearLayout buttomLayer2;
    public LinearLayout buttomLayer3;
    public LinearLayout buttomLayer4;
    public TextView bDelete;
    public TextView bDelete2;
    public TextView bGuibin;
    public TextView bSelect;
    public TextView bMove;
    public TextView bFenfa;
    public TextView bAccess;
    public TextView bVote;

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

    public View.OnClickListener mshoujianxianglaoListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_SHOUJIANXIAN, 1);

        }
    };

    public View.OnClickListener mshoujianxiangxinListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_SHOUJIANXIAN, 2);

        }
    };

    public View.OnClickListener mshengpixiangListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_SHENGPIXIANG, 0);

        }
    };

    public View.OnClickListener mshoujianxiangListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.shoujianxiangListener();
        }
    };

    public View.OnClickListener mfenfaxiangListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.fenfaxiangListener();
        }
    };

    public View.OnClickListener mfenfaxiangweilianxiListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FENFAXIAN, 0);

        }
    };

    public View.OnClickListener mfenfaxianglianxiListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FENFAXIAN, 1);

        }
    };

    public View.OnClickListener mfenfaxianglianxifailListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FENFAXIAN, 2);

        }
    };

    public View.OnClickListener mfenfaxianglianxisuccessListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FENFAXIAN, 3);

        }
    };

    public View.OnClickListener mfajianxiangdaishenListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FAJIANXIAN, 1);

        }
    };

    public View.OnClickListener mfajianxiangtongguoListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FAJIANXIAN, 2);

        }
    };

    public View.OnClickListener mfajianxiangbohuiListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_FAJIANXIAN, 3);

        }
    };

    public View.OnClickListener mfajianxiangListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.fajianxiangListener();
        }
    };

    public View.OnClickListener neibuListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_NEIBU, 0);
        }
    };

    public View.OnClickListener myifaxiangListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_YIFANXIAN, 0);

        }
    };

    public View.OnClickListener mlajixiangListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_LAJIXIAN, 0);

        }
    };

    public View.OnClickListener mshanchuxiangListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_SHANCHUXIAN, 0);

        }
    };

    public View.OnClickListener mcaogaoxiangListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.startMailList(MailListActivity.GET_MAIL_CAOGAOXIAN, 0);

        }
    };

    public View.OnClickListener  mailBoxListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (MailManager.getInstance().account.iscloud == false)
                mMailPresenter.startMailList(MailListActivity.GET_MAIL_SHOUJIANXIAN, 0, (MailBox) v.getTag());
            else {
                MailPresenter.Mailboxobj mailboxobj = (MailPresenter.Mailboxobj) v.getTag();
                if (mMailPresenter.mMailActivity.select != null) {
                    MailPresenter.Mailboxobj mailboxobj1 = (MailPresenter.Mailboxobj) mMailPresenter.mMailActivity.select.getTag();
                    if (mailboxobj1.type == 100) {
                        setImage(mailboxobj1.type, cloudUnreadMailimg, false);
                        mMailPresenter.mMailActivity.cloudUnreadMail.setBackgroundColor(Color.parseColor("#ffffff"));
                        mMailPresenter.mMailActivity.cloudUnreadMailtxt.setTextColor(Color.parseColor("#23272E"));
                    } else {
                        if (mailboxobj1.type == 7) {

                        } else if (mailboxobj1.type == 9) {
                            ImageView imageView = mMailPresenter.mMailActivity.select.findViewById(R.id.shoujianxiangxin_img);
                            imageView.setImageResource(R.drawable.array_down);
                        } else {
                            ImageView imageView = mMailPresenter.mMailActivity.select.findViewById(R.id.shoujianxiangxin_img);
                            setImage(mailboxobj1.type, imageView, false);
                        }
                        TextView name1 = (TextView) mMailPresenter.mMailActivity.select.findViewById(R.id.shoujianxiang_text_xin);
                        name1.setTextColor(Color.parseColor("#23272E"));
                        mMailPresenter.mMailActivity.select.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                }
                mMailPresenter.mMailActivity.select = v;
                MailPresenter.Mailboxobj mailboxobj1 = (MailPresenter.Mailboxobj) mMailPresenter.mMailActivity.select.getTag();
                if (mailboxobj1.type == 100) {
                    setImage(mailboxobj1.type, cloudUnreadMailimg, true);
                    mMailPresenter.mMailActivity.cloudUnreadMail.setBackgroundColor(Color.parseColor("#ffffff"));
                    mMailPresenter.mMailActivity.cloudUnreadMailtxt.setTextColor(Color.parseColor("#23272E"));
                } else {
                    if (mailboxobj1.type == 7) {

                    } else if (mailboxobj1.type == 9) {
                        ImageView imageView = mMailPresenter.mMailActivity.select.findViewById(R.id.shoujianxiangxin_img);
                        imageView.setImageResource(R.drawable.array_right);
                    } else {
                        ImageView imageView1 = mMailPresenter.mMailActivity.select.findViewById(R.id.shoujianxiangxin_img);
                        setImage(mailboxobj1.type, imageView1, true);
                    }
                    TextView name1 = (TextView) mMailPresenter.mMailActivity.select.findViewById(R.id.shoujianxiang_text_xin);
                    name1.setTextColor(Color.parseColor("#1EA1F3"));
                    mMailPresenter.mMailActivity.select.setBackgroundColor(Color.parseColor("#F5F5F5"));

                }
                if (mailboxobj.type != 9)
                    mMailPresenter.startMailList((MailPresenter.Mailboxobj) v.getTag());
                else {
                    MailType mailType = (MailType) mailboxobj.object;
                    if (mailType.isMail == true) {
                        mMailPresenter.startMailList((MailPresenter.Mailboxobj) v.getTag());
                    } else {
                        if(mailType.show == false)
                        {
                            mMailPresenter.mMailActivity.waitDialog.show();
                            MailAsks.getMailCustoms(mMailPresenter.mMailHandler, mMailPresenter.mMailActivity, (MailPresenter.Mailboxobj) v.getTag());
                        }
                        else{
                            mMailPresenter.doHid(mailType);
                        }
                    }
                }
            }
        }

    };

    public View.OnClickListener editLableListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailPresenter.editLable((View) v.getTag());
        }
    };

    public View.OnClickListener deleteLableListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailPresenter.deleteLable((View) v.getTag());
        }
    };

    public View.OnClickListener deleteFileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailPresenter.deleteFile((View) v.getTag());
        }
    };

    public View.OnClickListener editFileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailPresenter.editFile((View) v.getTag());
        }
    };

    public void setImage(int type, ImageView imageView, boolean select) {
        switch (type) {
            case 100:
                if (select) {
                    imageView.setImageResource(R.drawable.mail_unreads);
                } else {
                    imageView.setImageResource(R.drawable.mail_unread);
                }
                break;
            case 0:
                if (select) {
                    imageView.setImageResource(R.drawable.shoujianxiangs);
                } else {
                    imageView.setImageResource(R.drawable.shoujianxiang);
                }
                break;
            case 1:
                if (select) {
                    imageView.setImageResource(R.drawable.shenpi_imgs);
                } else {
                    imageView.setImageResource(R.drawable.shenpi_img);
                }
                break;
            case 2:
                if (select) {
                    imageView.setImageResource(R.drawable.yifa_imgs);
                } else {
                    imageView.setImageResource(R.drawable.yifa_img);
                }
                break;
            case 3:
                if (select) {
                    imageView.setImageResource(R.drawable.caogao_imgs);
                } else {
                    imageView.setImageResource(R.drawable.caogao_img);
                }
                break;
            case 4:
                if (select) {
                    imageView.setImageResource(R.drawable.shanchu_imgs);
                } else {
                    imageView.setImageResource(R.drawable.shanchu_img);
                }
                break;
            case 5:
                if (select) {
                    imageView.setImageResource(R.drawable.laji_imgs);
                } else {
                    imageView.setImageResource(R.drawable.laji_img);
                }
                break;
            case 61:
                if (select) {
                    imageView.setImageResource(R.drawable.yifa_imgs);
                } else {
                    imageView.setImageResource(R.drawable.yifa_img);
                }
                break;
            case 62:
                if (select) {
                    imageView.setImageResource(R.drawable.caogao_imgs);
                } else {
                    imageView.setImageResource(R.drawable.caogao_img);
                }
                break;
            case 8:
                if (select) {
                    imageView.setImageResource(R.drawable.mailfiles);
                } else {
                    imageView.setImageResource(R.drawable.mailfile);
                }
                break;
            case 9:
                if (select) {
                    imageView.setImageResource(R.drawable.mailtypes);
                } else {
                    imageView.setImageResource(R.drawable.mailtype);
                }
                break;

        }
    }

    public View.OnClickListener mOnShowSearchListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailPresenter.mShowSearchListener();
        }
    };

    public SearchViewLayout.DoCancle mHidSearchListener = new SearchViewLayout.DoCancle() {

        @Override
        public void doCancle() {
            mMailPresenter.hidSearch();
        }
    };


    public View.OnClickListener mMailBoxSelectListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.showUserids();
        }
    };

    public View.OnClickListener mBackListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.doBackListenre();

        }
    };

    public View.OnClickListener mWriteMailListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.wrightMail();
        }
    };


    public View.OnClickListener mShowLeftListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.showLeftView();
        }
    };


    public TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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

    public PullToRefreshView.OnHeaderRefreshListener onHeaderRefreshListener = new PullToRefreshView.OnHeaderRefreshListener() {

        @Override
        public void onHeaderRefresh(PullToRefreshView view) {
            mMailPresenter.onHead();
            view.onHeaderRefreshComplete();
        }
    };

    public PullToRefreshView.OnFooterRefreshListener onFooterRefreshListener = new PullToRefreshView.OnFooterRefreshListener() {


        @Override
        public void onFooterRefresh(PullToRefreshView view) {
            mMailPresenter.onFoot();
            view.onFooterRefreshComplete();
        }
    };

    public View.OnClickListener munreadListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.unreadListener();
        }
    };


    public View.OnClickListener mcshoujianxiangListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.cshoujianxiangListener();
        }
    };

    public View.OnClickListener mcshengpiListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.cshengpiListener();
        }
    };

    public View.OnClickListener mcyifaListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.cyifaListener();
        }
    };

    public View.OnClickListener mccaogaoListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.ccaogaoListener();
        }
    };

    public View.OnClickListener mchuishouListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.chuishouListener();
        }
    };

    public View.OnClickListener mclajiListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.clajiListener();
        }
    };

    public View.OnClickListener mallListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.allListener();
        }
    };

    public View.OnClickListener mtabListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.tabListener();
        }
    };


    public View.OnClickListener mfileListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.fileListener();
        }
    };

    public View.OnClickListener mtypeListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mMailPresenter.typeListener();
        }
    };

//    @Override
//    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//        return mMailPresenter.onFling(motionEvent, motionEvent1, v, v1);
//    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }

    public AdapterView.OnItemClickListener mailClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mMailPresenter.startMail((Mail) parent.getAdapter().getItem(position));
        }
    };

//    public AdapterView.OnItemLongClickListener mailLongClickListener = new AdapterView.OnItemLongClickListener() {
//
//        @Override
//        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//            mMailPresenter.startDelete((Mail) parent.getAdapter().getItem(position));
//            return true;
//        }
//
//    };

    public View.OnClickListener managerClickListener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            mMailPresenter.startManager();
        }
    };

    public View.OnClickListener deleteMailsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailPresenter.deleteMail();
        }
    };

    public View.OnClickListener guibinMailsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailPresenter.guibinMail();
        }
    };

    public View.OnClickListener selectMailsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailPresenter.seletTabMail();
        }
    };

    public View.OnClickListener moveMailsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailPresenter.moveMail();
        }
    };
    public View.OnClickListener fenfaMailsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailPresenter.fengfaMail();
        }
    };

    public View.OnClickListener accessListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailPresenter.access();
        }
    };


    public View.OnClickListener voteListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailPresenter.vote();
        }
    };

    public View.OnClickListener selectAllListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mMailPresenter.selectAll();
        }
    };

    public View.OnClickListener selectCancleListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //mMailPresenter.cancle();
        }
    };

    public View.OnClickListener leftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailPresenter.showLeft();
            if (mMailItems1.size() == 0)
                mMailPresenter.getMails(false, mMailPresenter.mailboxobj);
        }
    };

    public View.OnClickListener middleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mMailPresenter.showMiddle();
            if (mMailItems2.size() == 0)
                mMailPresenter.getMails(false, mMailPresenter.mailboxobj);
        }
    };

    public View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailPresenter.showRight();
            if (mMailItems3.size() == 0)
                mMailPresenter.getMails(false, mMailPresenter.mailboxobj);
        }
    };

    public View.OnClickListener rightClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailPresenter.showRight2();
            if (mMailItems4.size() == 0)
                mMailPresenter.getMails(false, mMailPresenter.mailboxobj);
        }
    };

    public View.OnClickListener receiveMailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailPresenter.doReceiveMail();
        }
    };

    public View.OnClickListener newTabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailPresenter.newTab();
        }
    };

    public View.OnClickListener newFileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMailPresenter.newFile();
        }
    };


    public TextView.OnEditorActionListener mOnSearchActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mMailPresenter.doSearch(searchView.getText());

            }
            return true;
        }
    };


    public AbsListView.OnScrollListener mOnScoll = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (searchView.ishow) {
                if (searchView.getText().length() == 0) {
                    searchView.hidEdit();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

    public MySlideBar.OnTouchLetterChangeListenner mOnTouchLetterChangeListenner = new MySlideBar.OnTouchLetterChangeListenner() {

        @Override
        public void onTouchLetterChange(MotionEvent event, int s) {
            // TODO Auto-generated method stub
            mMailPresenter.letterChange(s);

        }
    };

    public AdapterView.OnItemClickListener onContactItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            mMailPresenter.contactsClick((MailContact) parent.getAdapter().getItem(position));
        }
    };
}
