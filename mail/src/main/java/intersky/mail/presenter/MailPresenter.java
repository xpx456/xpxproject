package intersky.mail.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import java.util.Collections;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.apputils.MenuItem;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.Mail;
import intersky.mail.entity.MailBox;
import intersky.mail.entity.MailContact;
import intersky.mail.entity.MailFile;
import intersky.mail.entity.MailType;
import intersky.mail.entity.SortMailContactComparator;
import intersky.mail.handler.MailHandler;
import intersky.mail.receiver.MailReceiver;
import intersky.mail.view.InputView;
import intersky.mail.view.NewLableView;
import intersky.mail.view.activity.MailActivity;
import intersky.mail.view.activity.MailContactsActivity;
import intersky.mail.view.activity.MailEditActivity;
import intersky.mail.view.activity.MailFileActivity;
import intersky.mail.view.activity.MailLableActivity;
import intersky.mail.view.activity.MailListActivity;
import intersky.mail.view.activity.MailShowActivity;
import intersky.mail.view.adapter.LoderPageAdapter;
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

public class MailPresenter implements Presenter {

    public MailHandler mMailHandler;
    public MailActivity mMailActivity;
    public Mailboxobj mailboxobj;
    public Mailboxobj unreadmailboxobj = new Mailboxobj();
    public TextView title;
    public NewLableView newLableView;
    public InputView timeInput;
    public PopupWindow popupWindowbutom;
    public MailPresenter.Mailboxobj mailtypeboxobj = new Mailboxobj();
    public MailPresenter(MailActivity mMailActivity) {
        this.mMailActivity = mMailActivity;
        this.mMailHandler = new MailHandler(mMailActivity);
        mMailActivity.setBaseReceiver(new MailReceiver(mMailHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        if (MailManager.getInstance().account.iscloud == false) {
            mMailActivity.setContentView(R.layout.activity_mail);
            ImageView back = mMailActivity.findViewById(R.id.back);
            back.setOnClickListener(mMailActivity.mBackListener);
            title = mMailActivity.findViewById(R.id.title);
            title.setText(mMailActivity.getString(R.string.title_mymailbox));
            title.setOnClickListener(mMailActivity.mMailBoxSelectListener);
            ImageView reight = mMailActivity.findViewById(R.id.add);
            reight.setOnClickListener(mMailActivity.mWriteMailListener);

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

            mMailActivity.contactList = (ListView) mMailActivity.findViewById(R.id.contacts_List);
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
            mMailActivity.mSearchContactAdapter = new MailContactsAdapter(mMailActivity, mMailActivity.mSearchItems, true);
            mMailActivity.mMailUserAdapter = new MailContactsAdapter(mMailActivity, MailManager.getInstance().mMailUnderlineUsers, true);
            mMailActivity.contactList.setAdapter(mMailActivity.mMailUserAdapter);
            checkMailBoxView();
            setnewcountView();

            mMailActivity.mPullToRefreshView = (PullToRefreshView) mMailActivity.findViewById(R.id.mail_pull_refresh_view);
            mMailActivity.mSearchLayer = (RelativeLayout) mMailActivity.findViewById(R.id.search_layer);
            mMailActivity.mSearchView = (RelativeLayout) mMailActivity.findViewById(R.id.mail_search);
            mMailActivity.mMailSearchView = mMailActivity.findViewById(R.id.search);
            mMailActivity.mSearchLayer.setOnClickListener(mMailActivity.mOnShowSearchListener);
            mMailActivity.mMailSearchView.setOnCancleListener(mMailActivity.mHidSearchListener);
            mMailActivity.mMailSearchView.mSetOnSearchListener(mMailActivity.mOnEditorActionListener);
            mMailActivity.mPullToRefreshView.setOnFooterRefreshListener(mMailActivity);
            mMailActivity.mPullToRefreshView.setOnHeaderRefreshListener(mMailActivity);
        } else {
            mMailActivity.setContentView(R.layout.activity_mail_cloud2);
            mMailActivity.shade = (RelativeLayout) mMailActivity.findViewById(R.id.shade);
            timeInput = new InputView(mMailActivity,doFileOkListener, InputType.TYPE_CLASS_TEXT ,mMailActivity.getString(R.string.mail_lable_file_input_hit));
            newLableView = new NewLableView(mMailActivity,mMailActivity.shade);
            ImageView back = mMailActivity.findViewById(R.id.back);
            back.setOnClickListener(mMailActivity.mBackListener);
            LayoutInflater inflater = LayoutInflater.from(mMailActivity);
            mMailActivity.userView = inflater.inflate(R.layout.mail_user_select, null);

            mMailActivity.contactList = (ListView)mMailActivity.userView.findViewById(R.id.contacts_List);
            mMailActivity.searchView = mMailActivity.userView.findViewById(R.id.search);
            mMailActivity.msbar = (MySlideBar) mMailActivity.userView.findViewById(R.id.slideBar);
            mMailActivity.mLetterText = (TextView) mMailActivity.userView.findViewById(R.id.letter_text);
            mMailActivity.mRelativeLetter = (RelativeLayout) mMailActivity.userView.findViewById(R.id.letter_layer);
            mMailActivity.msbar.setVisibility(View.INVISIBLE);
            mMailActivity.msbar.setOnTouchLetterChangeListenner(mMailActivity.mOnTouchLetterChangeListenner);
            //mMailActivity.searchView.mSetOnSearchListener(mMailActivity.mOnSearchActionListener);
            mMailActivity.searchView.setDotextChange(doTextChange);
            mMailActivity.contactList.setOnScrollListener(mMailActivity.mOnScoll);
            mMailActivity.contactList.setOnItemClickListener(mMailActivity.onContactItemClickListener);
            mMailActivity.msbar.setmRelativeLayout(mMailActivity.mRelativeLetter);
            mMailActivity.msbar.setMletterView(mMailActivity.mLetterText);
            mMailActivity.mSearchContactAdapter = new MailContactsAdapter(mMailActivity, mMailActivity.mSearchItems, true);
            mMailActivity.mMailUserAdapter = new MailContactsAdapter(mMailActivity, MailManager.getInstance().mMailUnderlineUsers, true);
            mMailActivity.contactList.setAdapter(mMailActivity.mMailUserAdapter);
            if (mMailActivity.msbar != null) {
                String[] ss = new String[MailManager.getInstance().mMailUnderlineHeadUsers.size()];
                for (int i = 0; i < MailManager.getInstance().mMailUnderlineHeadUsers.size(); i++) {
                    ss[i] = MailManager.getInstance().mMailUnderlineHeadUsers.get(i).mName;
                }
                mMailActivity.msbar.setAddletters(ss);
                mMailActivity.msbar.requestLayout();
                mMailActivity.msbar.setVisibility(View.VISIBLE);
            }

            mMailActivity.leftView = inflater.inflate(R.layout.mail_left, null);
            mMailActivity.bntUsers = mMailActivity.leftView.findViewById(R.id.username);
            mMailActivity.bntUsers.setOnClickListener(showUserListener);
            PullToRefreshView refreshView = mMailActivity.leftView.findViewById(R.id.mail_pull_refresh_view);
            refreshView.setOnFooterRefreshListener(mMailActivity);
            refreshView.setOnHeaderRefreshListener(mMailActivity);
            mMailActivity.cloudUnreadMail = (RelativeLayout) mMailActivity.leftView.findViewById(R.id.unreadmail_laye);
            mMailActivity.cloudUnreadMailtxt = mMailActivity.leftView.findViewById(R.id.unreadmail_text);
            mMailActivity.cloudUnreadMail.setTag(unreadmailboxobj);
            mMailActivity.cloudshoujianxiang = (RelativeLayout) mMailActivity.leftView.findViewById(R.id.shoujianxiang_laye);
            mMailActivity.cloudshenpixiang = (RelativeLayout) mMailActivity.leftView.findViewById(R.id.shenpi_laye);
            mMailActivity.cloudyifaxiang = (RelativeLayout) mMailActivity.leftView.findViewById(R.id.yifa_laye);
            mMailActivity.cloudcaogaoxiang = (RelativeLayout) mMailActivity.leftView.findViewById(R.id.caogao_laye);
            mMailActivity.cloudhuishouxiang = (RelativeLayout) mMailActivity.leftView.findViewById(R.id.shanchu_laye);
            mMailActivity.cloudlajixiang = (RelativeLayout) mMailActivity.leftView.findViewById(R.id.laji_laye);
            mMailActivity.cloudall = mMailActivity.leftView.findViewById(R.id.all_laye);
            mMailActivity.cloudtab = mMailActivity.leftView.findViewById(R.id.tab_laye);
            mMailActivity.cloudfile = mMailActivity.leftView.findViewById(R.id.file_laye);
            mMailActivity.cloudtype = mMailActivity.leftView.findViewById(R.id.type_laye);

            unreadmailboxobj.state = 1;
            unreadmailboxobj.type = 100;

            mMailActivity.cloudUnreadMailimg = mMailActivity.leftView.findViewById(R.id.unreadmail_img);
            mMailActivity.cloudshoujianxiangimg = mMailActivity.leftView.findViewById(R.id.shoujianxiang_img);
            mMailActivity.cloudshenpixiangimg = mMailActivity.leftView.findViewById(R.id.shenpi_img);
            mMailActivity.cloudyifaxiangimg = mMailActivity.leftView.findViewById(R.id.yifa_img);
            mMailActivity.cloudcaogaoxiangimg = mMailActivity.leftView.findViewById(R.id.caogao_img);
            mMailActivity.cloudhuishouxiangimg = mMailActivity.leftView.findViewById(R.id.shanchu_img);
            mMailActivity.cloudlajixiangimg = mMailActivity.leftView.findViewById(R.id.laji_img);
            mMailActivity.cloudallimg = mMailActivity.leftView.findViewById(R.id.all_img);
            mMailActivity.cloudtabimg = mMailActivity.leftView.findViewById(R.id.tab_img);
            mMailActivity.cloudtypeimg = mMailActivity.leftView.findViewById(R.id.type_img);
            mMailActivity.cloudfileimg = mMailActivity.leftView.findViewById(R.id.file_img);
            mMailActivity.cloudfileaddimg = mMailActivity.leftView.findViewById(R.id.file_addimg);
            mMailActivity.cloudtabaddimg = mMailActivity.leftView.findViewById(R.id.tab_addimg);
            mMailActivity.cloudfileaddimg.setOnClickListener(mMailActivity.newFileListener);
            mMailActivity.cloudtabaddimg.setOnClickListener(mMailActivity.newTabListener);

            mMailActivity.mMoreMailBox1 = (LinearLayout) mMailActivity.leftView.findViewById(R.id.shoujian_morebox);
            mMailActivity.mMoreMailBox2 = (LinearLayout) mMailActivity.leftView.findViewById(R.id.shenpi_morebox);
            mMailActivity.mMoreMailBox3 = (LinearLayout) mMailActivity.leftView.findViewById(R.id.yifa_morebox);
            mMailActivity.mMoreMailBox4 = (LinearLayout) mMailActivity.leftView.findViewById(R.id.caogao_morebox);
            mMailActivity.mMoreMailBox5 = (LinearLayout) mMailActivity.leftView.findViewById(R.id.shanchu_morebox);
            mMailActivity.mMoreMailBox6 = (LinearLayout) mMailActivity.leftView.findViewById(R.id.laji_morebox);
            mMailActivity.mMoreMailBox7 = (LinearLayout) mMailActivity.leftView.findViewById(R.id.all_morebox);
            mMailActivity.mMoreMailBox8 = (LinearLayout) mMailActivity.leftView.findViewById(R.id.tab_morebox);
            mMailActivity.mMoreMailBox9 = (LinearLayout) mMailActivity.leftView.findViewById(R.id.file_morebox);
            mMailActivity.mMoreMailBox10 = (LinearLayout) mMailActivity.leftView.findViewById(R.id.type_morebox);

            mMailActivity.cloudunreadhit = (TextView) mMailActivity.leftView.findViewById(R.id.unreadmail_count_text);
            mMailActivity.cloudshoujianhit = (TextView) mMailActivity.leftView.findViewById(R.id.shoujianxiang_count_text);
            mMailActivity.cloudshenpi = (TextView) mMailActivity.leftView.findViewById(R.id.shenpi_count_text);
            mMailActivity.cloudyifa = (TextView) mMailActivity.leftView.findViewById(R.id.yifa_count_text);
            mMailActivity.cloudcaogao = (TextView) mMailActivity.leftView.findViewById(R.id.caogao_count_text);
            mMailActivity.cloudhuishou = (TextView) mMailActivity.leftView.findViewById(R.id.shanchu_count_text);
            mMailActivity.cloudlaji = (TextView) mMailActivity.leftView.findViewById(R.id.laji_count_text);


            mMailActivity.btnReceive = mMailActivity.leftView.findViewById(R.id.receive);
            mMailActivity.bReceive = mMailActivity.leftView.findViewById(R.id.receiveb);
            mMailActivity.iReceive = mMailActivity.leftView.findViewById(R.id.icon);
            mMailActivity.bReceive.setOnClickListener(mMailActivity.receiveMailListener);

            RelativeLayout layer = mMailActivity.leftView.findViewById(R.id.layer);
            layer.setFocusable(true);

            mMailActivity.cloudUnreadMail.setOnClickListener(mMailActivity.munreadListener);
            mMailActivity.cloudshoujianxiang.setOnClickListener(mMailActivity.mcshoujianxiangListener);
            mMailActivity.cloudshenpixiang.setOnClickListener(mMailActivity.mcshengpiListener);
            mMailActivity.cloudyifaxiang.setOnClickListener(mMailActivity.mcyifaListener);
            mMailActivity.cloudcaogaoxiang.setOnClickListener(mMailActivity.mccaogaoListener);
            mMailActivity.cloudhuishouxiang.setOnClickListener(mMailActivity.mchuishouListener);
            mMailActivity.cloudlajixiang.setOnClickListener(mMailActivity.mclajiListener);
            mMailActivity.cloudall.setOnClickListener(mMailActivity.mallListener);
            mMailActivity.cloudtab.setOnClickListener(mMailActivity.mtabListener);
            mMailActivity.cloudfile.setOnClickListener(mMailActivity.mfileListener);
            mMailActivity.cloudtype.setOnClickListener(mMailActivity.mtypeListener);

            View mview2 = mMailActivity.getLayoutInflater().inflate(R.layout.mail_box_layer1, null);
            Mailboxobj item = new Mailboxobj();
            item.state = -1;
            item.id = 1;
            item.type = 1;
            item.name = mMailActivity.getString(R.string.mail_wati_me);
            mview2.setTag(item);
            mview2.setOnClickListener(mMailActivity.mailBoxListener);
            TextView name2 = (TextView) mview2.findViewById(R.id.shoujianxiang_text_xin);
            ImageView img = mview2.findViewById(R.id.shoujianxiangxin_img);
            img.setImageResource(R.drawable.shenpi_img);
            name2.setText(mMailActivity.getString(R.string.mail_wati_me));
            mMailActivity.cloudshenpiw = (TextView) mview2.findViewById(R.id.shoujianxiang_count_text_xin);

            View mview3 = mMailActivity.getLayoutInflater().inflate(R.layout.mail_box_layer1, null);
            item = new Mailboxobj();
            item.state = -1;
            item.id = 3;
            item.type = 1;
            item.name = mMailActivity.getString(R.string.mail_me_approve);
            mview3.setTag(item);
            mview3.setOnClickListener(mMailActivity.mailBoxListener);
            TextView name3 = (TextView) mview3.findViewById(R.id.shoujianxiang_text_xin);
            img = mview3.findViewById(R.id.shoujianxiangxin_img);
            img.setImageResource(R.drawable.shenpi_img);
            name3.setText(mMailActivity.getString(R.string.mail_me_approve));
            mMailActivity.cloudshenpim = (TextView) mview3.findViewById(R.id.shoujianxiang_count_text_xin);

            View mview4 = mMailActivity.getLayoutInflater().inflate(R.layout.mail_box_layer1, null);
            item = new Mailboxobj();
            item.state = -2;
            item.id = 1;
            item.type = 61;
            item.name = mMailActivity.getString(R.string.mail_mailtitleasend);
            mview4.setTag(item);
            mview4.setOnClickListener(mMailActivity.mailBoxListener);
            ImageView icon = mview4.findViewById(R.id.shoujianxiangxin_img);
            icon.setImageResource(R.drawable.yifa_img);
            TextView name4 = (TextView) mview4.findViewById(R.id.shoujianxiang_text_xin);
            name4.setText(mMailActivity.getString(R.string.mail_mailtitleasend));


            View mview5 = mMailActivity.getLayoutInflater().inflate(R.layout.mail_box_layer1, null);
            item = new Mailboxobj();
            item.state = -2;
            item.id = 0;
            item.type = 62;
            item.name = mMailActivity.getString(R.string.mail_mailtitledraft);
            mview5.setTag(item);
            ImageView icon1 = mview5.findViewById(R.id.shoujianxiangxin_img);
            icon1.setImageResource(R.drawable.caogao_img);
            mview5.setOnClickListener(mMailActivity.mailBoxListener);
            TextView name5 = (TextView) mview5.findViewById(R.id.shoujianxiang_text_xin);
            name5.setText(mMailActivity.getString(R.string.mail_mailtitledraft));


            MailManager.getInstance().mailType.mailTypes.clear();
            MailManager.getInstance().mailType.show = false;
            mailtypeboxobj.object = MailManager.getInstance().mailType;
            mailtypeboxobj.type = 9;
            mailtypeboxobj.state = -5;
            MailManager.getInstance().mSelectUser = null;
            mMailActivity.mMoreMailBox2.addView(mview2);
            mMailActivity.mMoreMailBox2.addView(mview3);
            mMailActivity.mMoreMailBox7.addView(mview4);
            mMailActivity.mMoreMailBox7.addView(mview5);

            mMailActivity.mPullToRefreshView = (PullToRefreshView) mMailActivity.findViewById(R.id.mail_pull_refresh_view);
            mMailActivity.mListView = (ListView) mMailActivity.findViewById(R.id.mail_List);
            mMailActivity.mPullToRefreshView.setOnHeaderRefreshListener(mMailActivity.onHeaderRefreshListener);
            mMailActivity.mPullToRefreshView.setOnFooterRefreshListener(mMailActivity.onFooterRefreshListener);
            mMailActivity.mMailItemAdapter = new MailItemAdapter(mMailActivity, mMailActivity.mails);
            mMailActivity.mListView.setAdapter(mMailActivity.mMailItemAdapter);
            mMailActivity.mListView.setOnItemClickListener(mMailActivity.mailClickListener);
            //mMailActivity.mListView.setOnItemLongClickListener(mMailActivity.mailLongClickListener);

            mMailActivity.mTab = mMailActivity.findViewById(R.id.teblayer);

            mMailActivity.manager = (TextView) mMailActivity.findViewById(R.id.manager);
            mMailActivity.bDelete = mMailActivity.findViewById(R.id.bdelete);
            mMailActivity.bMove = mMailActivity.findViewById(R.id.bmove);
            mMailActivity.bGuibin = mMailActivity.findViewById(R.id.bguibin);
            mMailActivity.bSelect = mMailActivity.findViewById(R.id.bselect);
            mMailActivity.bFenfa = mMailActivity.findViewById(R.id.bfengfa);
            mMailActivity.mcount = mMailActivity.findViewById(R.id.titlemail);
            mMailActivity.buttomLayer2 = mMailActivity.findViewById(R.id.mail_buttom_layer1);
            mMailActivity.buttomLayer3 = mMailActivity.findViewById(R.id.mail_buttom_layer3);
            mMailActivity.buttomLayer4 = mMailActivity.findViewById(R.id.mail_buttom_layer4);
            mMailActivity.bDelete2 = mMailActivity.findViewById(R.id.bdelete2);
            mMailActivity.bAccess = mMailActivity.findViewById(R.id.btnaccess);
            mMailActivity.bVote = mMailActivity.findViewById(R.id.btnaccess);
            mMailActivity.manager.setOnClickListener(mMailActivity.managerClickListener);
            mMailActivity.bDelete.setOnClickListener(mMailActivity.deleteMailsListener);
            mMailActivity.bDelete2.setOnClickListener(mMailActivity.deleteMailsListener);
            mMailActivity.bAccess.setOnClickListener(mMailActivity.accessListener);
            mMailActivity.bVote.setOnClickListener(mMailActivity.voteListener);
            mMailActivity.bGuibin.setOnClickListener(mMailActivity.guibinMailsListener);
            mMailActivity.bSelect.setOnClickListener(mMailActivity.selectMailsListener);
            mMailActivity.bMove.setOnClickListener(mMailActivity.moveMailsListener);
            mMailActivity.bFenfa.setOnClickListener(mMailActivity.fenfaMailsListener);

            mMailActivity.hit1 = (TextView) mMailActivity.findViewById(R.id.alltxt);
            mMailActivity.hit2 = (TextView) mMailActivity.findViewById(R.id.waittxt);
            mMailActivity.hit3 = (TextView) mMailActivity.findViewById(R.id.accesstxt);
            mMailActivity.hit4 = (TextView) mMailActivity.findViewById(R.id.votetxt);
            mMailActivity.mViewPager = (NoScrollViewPager) mMailActivity.findViewById(R.id.load_pager);
            mMailActivity.mLefttTeb = (RelativeLayout) mMailActivity.findViewById(R.id.all);
            mMailActivity.mMiddeleTeb = (RelativeLayout) mMailActivity.findViewById(R.id.wait);
            mMailActivity.mRightTeb = (RelativeLayout) mMailActivity.findViewById(R.id.access);
            mMailActivity.mRightTeb2 = (RelativeLayout) mMailActivity.findViewById(R.id.vote);
            mMailActivity.mLefttImg = (TextView) mMailActivity.findViewById(R.id.alltxt);
            mMailActivity.mMiddleImg = (TextView) mMailActivity.findViewById(R.id.waittxt);
            mMailActivity.mRightImg = (TextView) mMailActivity.findViewById(R.id.accesstxt);
            mMailActivity.mRightImg2 = (TextView) mMailActivity.findViewById(R.id.votetxt);
            View mView1 = null;
            View mView2 = null;
            View mView3 = null;
            View mView4 = null;
            mView1 = mMailActivity.getLayoutInflater().inflate(R.layout.leavepager, null);
            mMailActivity.mPullToRefreshView1 = (PullToRefreshView) mView1
                    .findViewById(R.id.task_pull_refresh_view);
            mMailActivity.mAllList = (ListView) mView1.findViewById(R.id.busines_List);
            mMailActivity.mAllList.setOnItemClickListener(mMailActivity.mailClickListener);
//            mMailActivity.mAllList.setOnItemLongClickListener(mMailActivity.mailLongClickListener);
            mView2 = mMailActivity.getLayoutInflater().inflate(R.layout.leavepager, null);
            mMailActivity.mPullToRefreshView2 = (PullToRefreshView) mView2
                    .findViewById(R.id.task_pull_refresh_view);
            mMailActivity.mWaitList = (ListView) mView2.findViewById(R.id.busines_List);
            mMailActivity.mWaitList.setOnItemClickListener(mMailActivity.mailClickListener);
//            mMailActivity.mWaitList.setOnItemLongClickListener(mMailActivity.mailLongClickListener);
            mView3 = mMailActivity.getLayoutInflater().inflate(R.layout.leavepager, null);
            mMailActivity.mPullToRefreshView3 = (PullToRefreshView) mView3
                    .findViewById(R.id.task_pull_refresh_view);
            mMailActivity.mAccessList = (ListView) mView3.findViewById(R.id.busines_List);
            mMailActivity.mAccessList.setOnItemClickListener(mMailActivity.mailClickListener);
//            mMailActivity.mAccessList.setOnItemLongClickListener(mMailActivity.mailLongClickListener);
            mView4 = mMailActivity.getLayoutInflater().inflate(R.layout.leavepager, null);
            mMailActivity.mPullToRefreshView4 = (PullToRefreshView) mView4
                    .findViewById(R.id.task_pull_refresh_view);
            mMailActivity.mVoteList = (ListView) mView4.findViewById(R.id.busines_List);
            mMailActivity.mVoteList.setOnItemClickListener(mMailActivity.mailClickListener);
//            mMailActivity.mVoteList.setOnItemLongClickListener(mMailActivity.mailLongClickListener);
            mMailActivity.mViews.add(mView1);
            mMailActivity.mViews.add(mView2);
            mMailActivity.mViews.add(mView3);
            mMailActivity.mViews.add(mView4);
            mMailActivity.mLoderPageAdapter = new LoderPageAdapter(mMailActivity.mViews);
            mMailActivity.mViewPager.setAdapter(mMailActivity.mLoderPageAdapter);
            mMailActivity.mViewPager.setNoScroll(true);
            mMailActivity.mLefttTeb.setOnClickListener(mMailActivity.leftClickListener);
            mMailActivity.mMiddeleTeb.setOnClickListener(mMailActivity.middleClickListener);
            mMailActivity.mRightTeb.setOnClickListener(mMailActivity.rightClickListener);
            mMailActivity.mRightTeb2.setOnClickListener(mMailActivity.rightClickListener2);
            mMailActivity.mPullToRefreshView1.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailActivity.mPullToRefreshView1.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailActivity.mPullToRefreshView2.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailActivity.mPullToRefreshView2.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailActivity.mPullToRefreshView3.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailActivity.mPullToRefreshView3.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailActivity.mPullToRefreshView4.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailActivity.mPullToRefreshView4.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
            mMailActivity.mPullToRefreshView1.setOnHeaderRefreshListener(mMailActivity.onHeaderRefreshListener);
            mMailActivity.mPullToRefreshView1.setOnFooterRefreshListener(mMailActivity.onFooterRefreshListener);
            mMailActivity.mPullToRefreshView2.setOnHeaderRefreshListener(mMailActivity.onHeaderRefreshListener);
            mMailActivity.mPullToRefreshView2.setOnFooterRefreshListener(mMailActivity.onFooterRefreshListener);
            mMailActivity.mPullToRefreshView3.setOnHeaderRefreshListener(mMailActivity.onHeaderRefreshListener);
            mMailActivity.mPullToRefreshView3.setOnFooterRefreshListener(mMailActivity.onFooterRefreshListener);
            mMailActivity.mPullToRefreshView4.setOnHeaderRefreshListener(mMailActivity.onHeaderRefreshListener);
            mMailActivity.mPullToRefreshView4.setOnFooterRefreshListener(mMailActivity.onFooterRefreshListener);
            initData();

            ImageView add = mMailActivity.findViewById(R.id.rightmenu);
            add.setOnClickListener(mMailActivity.mWriteMailListener);

            ImageView left = mMailActivity.findViewById(R.id.leftmenu);
            left.setOnClickListener(mMailActivity.mShowLeftListener);

            checkMailBoxViewc1();
            setnewcountViewCloud();
//            TextView textView = mMailActivity.findViewById(R.id.title);
//            textView.setText(getTitlec(mMailActivity.type2));


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
        mailboxobj = mMailBoxModel;
        if (mMailActivity.type1 == mMailBoxModel.id && mMailActivity.type2 == mMailBoxModel.state &&
                mMailActivity.mailboxid.equals(mMailBoxModel.mailboxid) && mMailActivity.type3 == mMailBoxModel.type) {

        } else {
            mMailActivity.type1 = mMailBoxModel.id;
            mMailActivity.type2 = mMailBoxModel.state;
            mMailActivity.type3 = mMailBoxModel.type;
            mMailActivity.mailboxid = mMailBoxModel.mailboxid;
            mMailActivity.mailname = mMailBoxModel.name;
            mMailActivity.startPos = 1;
            mMailActivity.mails.clear();
            mMailActivity.isAll = false;
            if(mMailActivity.mMailItemAdapter != null)
            mMailActivity.mMailItemAdapter.notifyDataSetChanged();
            if(mMailActivity.mMailItemAdapter1 != null)
            mMailActivity.mMailItemAdapter1.notifyDataSetChanged();
            if(mMailActivity.mMailItemAdapter2 != null)
            mMailActivity.mMailItemAdapter2.notifyDataSetChanged();
            if(mMailActivity.mMailItemAdapter3 != null)
            mMailActivity.mMailItemAdapter3.notifyDataSetChanged();
            if(mMailActivity.mMailItemAdapter4 != null)
            mMailActivity.mMailItemAdapter4.notifyDataSetChanged();
            updataListViewSet(mMailBoxModel);

        }
        mMailActivity.mcount.setText(mMailActivity.mMailPresenter.getTitlec(mMailActivity.type2)
                + getCount(mMailBoxModel));
    }

    public void checkMailBoxView() {
        LayoutInflater mInflater = (LayoutInflater) mMailActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMailActivity.mMoreMailBox.removeAllViews();
        for (int i = 0; i < MailManager.getInstance().mMailBoxs.size(); i++) {
            MailBox mMailBox = MailManager.getInstance().mMailBoxs.get(i);
            if (!mMailBox.isloacl) {
                View mview = mInflater.inflate(R.layout.mail_box_layerold, null);

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

    public void hidSearch() {
        mMailActivity.mSearchView.setVisibility(View.INVISIBLE);
        mMailActivity.mMailSearchView.hidEdit();
        mMailActivity.mMailSearchView.cleanText();
    }

    public void showUserids() {
        mMailActivity.mUserSearchView.setVisibility(View.VISIBLE);

    }

    public TextView.OnEditorActionListener mOnSearchActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(mMailActivity.searchView.getText());

            }
            return true;
        }
    };

    public AbsListView.OnScrollListener mOnScoll = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mMailActivity.searchView.ishow) {
                if (mMailActivity.searchView.getText().length() == 0) {
                    mMailActivity.searchView.hidEdit();
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
            letterChange(s);

        }
    };

    public AdapterView.OnItemClickListener onContactItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            contactsClick((MailContact) parent.getAdapter().getItem(position));

        }
    };

    public void wrightMail() {
        Intent mIntent = new Intent();
        mIntent.setClass(mMailActivity, MailEditActivity.class);
        mIntent.putExtra("maildata", new Mail());
        mMailActivity.startActivity(mIntent);
    }


    public void doBackListenre() {
        mMailActivity.finish();
    }

    public void setnewcountViewCloud() {
        AppUtils.setHit(MailManager.getInstance().allcount0, mMailActivity.cloudunreadhit);
        AppUtils.setHit(MailManager.getInstance().allcount1, mMailActivity.cloudshoujianhit);
        AppUtils.setHit(MailManager.getInstance().allcount1, mMailActivity.shoujianhits.get("0"));
        AppUtils.setHit(MailManager.getInstance().me_approval, mMailActivity.cloudshenpim);
        AppUtils.setHit(MailManager.getInstance().to_me_approval, mMailActivity.cloudshenpiw);
        for (int i = 0; i < MailManager.getInstance().mMailBoxs.size(); i++) {
            AppUtils.setHit(MailManager.getInstance().mMailBoxs.get(i).count1, mMailActivity.shoujianhits.get(MailManager.getInstance().mMailBoxs.get(i).mRecordId));
            AppUtils.setHit(MailManager.getInstance().mMailBoxs.get(i).count2, mMailActivity.yifahits.get(MailManager.getInstance().mMailBoxs.get(i).mRecordId));
            AppUtils.setHit(MailManager.getInstance().mMailBoxs.get(i).count4, mMailActivity.caogaohits.get(MailManager.getInstance().mMailBoxs.get(i).mRecordId));
            AppUtils.setHit(MailManager.getInstance().mMailBoxs.get(i).count5, mMailActivity.huishouhits.get(MailManager.getInstance().mMailBoxs.get(i).mRecordId));
            AppUtils.setHit(MailManager.getInstance().mMailBoxs.get(i).count6, mMailActivity.lajihits.get(MailManager.getInstance().mMailBoxs.get(i).mRecordId));

        }
        AppUtils.setHit(MailManager.getInstance().allcount2, mMailActivity.yifahits.get("0"));
        AppUtils.setHit(MailManager.getInstance().allcount4, mMailActivity.caogaohits.get("0"));
        AppUtils.setHit(MailManager.getInstance().allcount5, mMailActivity.huishouhits.get("0"));
        AppUtils.setHit(MailManager.getInstance().allcount6, mMailActivity.lajihits.get("0"));
        AppUtils.setHit(MailManager.getInstance().me_approval + MailManager.getInstance().to_me_approval, mMailActivity.cloudshenpi);
        AppUtils.setHit(MailManager.getInstance().allcount2, mMailActivity.cloudyifa);
        AppUtils.setHit(MailManager.getInstance().allcount4, mMailActivity.cloudcaogao);
        AppUtils.setHit(MailManager.getInstance().allcount5, mMailActivity.cloudhuishou);
        AppUtils.setHit(MailManager.getInstance().allcount6, mMailActivity.cloudlaji);
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

    public void letterChange(int s) {
        MailContact model = MailManager.getInstance().mMailUnderlineHeadUsers.get(s);
        int a = MailManager.getInstance().mMailUnderlineUsers.indexOf(model);
        ListView list = mMailActivity.contactList;
        list.setSelectionFromTop(a, 0);
    }

    public void contactsClick(MailContact contacts) {
        if(MailManager.getInstance().account.iscloud)
        {

            if(MailManager.getInstance().mSelectUser != null)
            {
                if(MailManager.getInstance().mSelectUser.mRecordid.equals(contacts.mRecordid))
                    return;
            }

            if(contacts.mRecordid.equals(MailManager.getInstance().account.mRecordId))
            {
                MailManager.getInstance().mSelectUser = null;
                showMyView();
            }
            else
            {
                MailManager.getInstance().mSelectUser = contacts;
                showOtherView();
            }
            if(MailManager.getInstance().mSelectUser != null)
            mMailActivity.bntUsers.setText(MailManager.getInstance().mSelectUser.mName);
            else
                mMailActivity.bntUsers.setText(mMailActivity.getString(R.string.keyword_mymailbox));
            MailManager.getInstance().resetUser();
            if(mMailActivity.popupWindow2 != null)
            {
                mMailActivity.popupWindow2.dismiss();
            }

        }
        else {
            mMailActivity.mUserSearchView.setVisibility(View.INVISIBLE);
            if (!contacts.mRecordid.equals(MailManager.getInstance().mSelectUser.mRecordid)) {
                MailAsks.setUser(mMailActivity, mMailHandler, contacts);
            }
        }

    }

    public void showMyView()
    {
        mMailActivity.cloudcaogaoxiang.setVisibility(View.VISIBLE);
        mMailActivity.cloudshenpixiang.setVisibility(View.VISIBLE);
        updataAllsendView();
        mMailActivity.cloudtab.setVisibility(View.VISIBLE);
        mMailActivity.cloudtype.setVisibility(View.VISIBLE);
        mMailActivity.cloudfileaddimg.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudfile.getLayoutParams();
        if(mMailActivity.shoujianOpened8)
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.tab_morebox);
        else
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.tab_laye);
        mMailActivity.cloudfile.setLayoutParams(mFajianLayerParams);

        mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudhuishouxiang.getLayoutParams();
        if(mMailActivity.shoujianOpened4)
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.caogao_morebox);
        else
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.caogao_laye);
        mMailActivity.cloudhuishouxiang.setLayoutParams(mFajianLayerParams);

        mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudyifaxiang.getLayoutParams();
        if(mMailActivity.shoujianOpened2)
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shenpi_morebox);
        else
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shenpi_laye);
        mMailActivity.cloudyifaxiang.setLayoutParams(mFajianLayerParams);

    }

    public void showOtherView() {
        mMailActivity.cloudcaogaoxiang.setVisibility(View.INVISIBLE);
        mMailActivity.cloudshenpixiang.setVisibility(View.INVISIBLE);
        mMailActivity.cloudall.setVisibility(View.INVISIBLE);
        mMailActivity.cloudtab.setVisibility(View.INVISIBLE);
        mMailActivity.cloudtype.setVisibility(View.INVISIBLE);
        mMailActivity.cloudfileaddimg.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudfile.getLayoutParams();
        if(mMailActivity.shoujianOpened6)
        mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.laji_morebox);
        else
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.laji_laye);
        mMailActivity.cloudfile.setLayoutParams(mFajianLayerParams);

        mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudhuishouxiang.getLayoutParams();
        if(mMailActivity.shoujianOpened3)
        mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.yifa_morebox);
        else
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.yifa_laye);
        mMailActivity.cloudhuishouxiang.setLayoutParams(mFajianLayerParams);

        mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudyifaxiang.getLayoutParams();
        if(mMailActivity.shoujianOpened1)
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shoujian_morebox);
        else
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shoujianxiang_laye);
        mMailActivity.cloudyifaxiang.setLayoutParams(mFajianLayerParams);
    }

    private String getMailName(String name) {
        if (name.indexOf("(") > 0) {
            return name.substring(0, name.indexOf("("));
        } else {
            return name;
        }

    }

    public void doSearch(String keyword) {
        if (keyword.length() == 0) {
            if (mMailActivity.isShowSearch == true) {
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
                            tempheads.add(new MailContact(s, ""));
                            typebooleans[pos] = true;
                        }
                    } else {
                        s = "#";
                        pos = CharacterParser.typeLetter.indexOf(s);
                        if (pos != -1) {
                            if (typebooleans[pos] == false) {
                                tempheads.add(new MailContact(s, ""));
                                typebooleans[pos] = true;
                            }
                        }
                    }
                }
            }
        }
        if (temps.size() == 0) {
            //AppUtils.showMessage(mMailActivity, mMailActivity.getString(R.string.searchview_search_none));
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
            if (MailManager.getInstance().account.iscloud == false)
                MailAsks.searchMials(mMailActivity, mMailActivity.mMailPresenter.mMailHandler, mMailActivity.mMailSearchView.getText());
            else
                MailAsks.searchMialsc(mMailActivity, mMailActivity.mMailPresenter.mMailHandler, mMailActivity.mMailSearchView.getText());
        }
    }

    public class Mailboxobj {
        public int state = 1;
        public int id = -1;
        public int type = 0;
        public String name = "";
        public String mailboxid = "";
        public int hit = 0;
        public Object object;
    }

    public void unreadListener() {
        MailPresenter.Mailboxobj mailboxobj = unreadmailboxobj;
        if(mMailActivity.select != null)
        {
            MailPresenter.Mailboxobj mailboxobj1 = (MailPresenter.Mailboxobj) mMailActivity.select.getTag();
            if(mailboxobj1.type == 100)
            {
                mMailActivity.setImage(mailboxobj1.type,mMailActivity.cloudUnreadMailimg,false);
                mMailActivity.cloudUnreadMailtxt.setTextColor(Color.parseColor("#23272E"));
            }
            else
            {
                if(mailboxobj1.type == 7)
                {

                }
                else if(mailboxobj1.type == 9)
                {
                    ImageView imageView = mMailActivity.select.findViewById(R.id.shoujianxiangxin_img);
                    imageView.setImageResource(R.drawable.array_down);
                }
                else
                {
                    ImageView imageView = mMailActivity.select.findViewById(R.id.shoujianxiangxin_img);
                    mMailActivity.setImage(mailboxobj1.type,imageView,false);
                }
                TextView name1 = (TextView) mMailActivity.select.findViewById(R.id.shoujianxiang_text_xin);
                name1.setTextColor(Color.parseColor("#23272E"));
            }
            mMailActivity.select.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        mMailActivity.select = mMailActivity.cloudUnreadMail;
        mMailActivity.setImage(mailboxobj.type,mMailActivity.cloudUnreadMailimg,true);
        mMailActivity.cloudUnreadMailtxt.setTextColor(Color.parseColor("#1EA1F3"));
        mMailActivity.cloudUnreadMail.setBackgroundColor(Color.parseColor("#F5F5F5"));
        startMailList(unreadmailboxobj);

    }

    public void cshoujianxiangListener() {
        if (mMailActivity.shoujianOpened1 == false) {
            mMailActivity.shoujianOpened1 = true;
//            mMailActivity.cloudshoujianxiangimg.setImageResource(R.drawable.shoujianxiangs);
//            mMailActivity.cloudshoujianhit.setTextColor(Color.parseColor("#1EA1F3"));
//            mMailActivity.cloudshoujianxiang.setBackgroundColor(Color.parseColor("#F5F5F5"));
            mMailActivity.mMoreMailBox1.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudshenpixiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shoujian_morebox);
            mMailActivity.cloudshenpixiang.setLayoutParams(mFajianLayerParams);

        } else {
            mMailActivity.shoujianOpened1 = false;
//            mMailActivity.cloudshoujianxiangimg.setImageResource(R.drawable.shoujianxiang);
//            mMailActivity.cloudshoujianhit.setTextColor(Color.parseColor("#23272E"));
//            mMailActivity.cloudshoujianxiang.setBackgroundColor(Color.parseColor("#ffffff"));
            mMailActivity.mMoreMailBox1.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudshenpixiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shoujianxiang_laye);
            mMailActivity.cloudshenpixiang.setLayoutParams(mFajianLayerParams);
        }

    }

    public void cshengpiListener() {
        if (mMailActivity.shoujianOpened2 == false) {
            mMailActivity.shoujianOpened2 = true;
//            mMailActivity.cloudshenpixiangimg.setImageResource(R.drawable.shenpi_imgs);
//            mMailActivity.cloudshenpi.setTextColor(Color.parseColor("#1EA1F3"));
//            mMailActivity.cloudshenpixiang.setBackgroundColor(Color.parseColor("#F5F5F5"));
            mMailActivity.mMoreMailBox2.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudyifaxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shenpi_morebox);
            mMailActivity.cloudyifaxiang.setLayoutParams(mFajianLayerParams);

        } else {
            mMailActivity.shoujianOpened2 = false;
//            mMailActivity.cloudshenpixiangimg.setImageResource(R.drawable.shenpi_img);
//            mMailActivity.cloudshenpi.setTextColor(Color.parseColor("#23272E"));
//            mMailActivity.cloudshenpixiang.setBackgroundColor(Color.parseColor("#ffffff"));
            mMailActivity.mMoreMailBox2.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudyifaxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shenpi_laye);
            mMailActivity.cloudyifaxiang.setLayoutParams(mFajianLayerParams);
        }

    }

    public void cyifaListener() {
        if (mMailActivity.shoujianOpened3 == false) {
            mMailActivity.shoujianOpened3 = true;
//            mMailActivity.cloudyifaxiangimg.setImageResource(R.drawable.yifa_imgs);
//            mMailActivity.cloudyifa.setTextColor(Color.parseColor("#1EA1F3"));
//            mMailActivity.cloudyifaxiang.setBackgroundColor(Color.parseColor("#F5F5F5"));
            mMailActivity.mMoreMailBox3.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudcaogaoxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.yifa_morebox);
            mMailActivity.cloudcaogaoxiang.setLayoutParams(mFajianLayerParams);

        } else {
            mMailActivity.shoujianOpened3 = false;
//            mMailActivity.cloudyifaxiangimg.setImageResource(R.drawable.yifa_img);
//            mMailActivity.cloudyifa.setTextColor(Color.parseColor("#23272E"));
//            mMailActivity.cloudyifaxiang.setBackgroundColor(Color.parseColor("#ffffff"));
            mMailActivity.mMoreMailBox3.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudcaogaoxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.yifa_laye);
            mMailActivity.cloudcaogaoxiang.setLayoutParams(mFajianLayerParams);
        }

    }

    public void ccaogaoListener() {
        if (mMailActivity.shoujianOpened4 == false) {
            mMailActivity.shoujianOpened4 = true;
//            mMailActivity.cloudcaogaoxiangimg.setImageResource(R.drawable.caogao_imgs);
//            mMailActivity.cloudcaogao.setTextColor(Color.parseColor("#1EA1F3"));
//            mMailActivity.cloudcaogaoxiang.setBackgroundColor(Color.parseColor("#F5F5F5"));
            mMailActivity.mMoreMailBox4.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudhuishouxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.caogao_morebox);
            mMailActivity.cloudhuishouxiang.setLayoutParams(mFajianLayerParams);

        } else {
            mMailActivity.shoujianOpened4 = false;
//            mMailActivity.cloudcaogaoxiangimg.setImageResource(R.drawable.caogao_img);
//            mMailActivity.cloudcaogao.setTextColor(Color.parseColor("#23272E"));
//            mMailActivity.cloudcaogaoxiang.setBackgroundColor(Color.parseColor("#ffffff"));
            mMailActivity.mMoreMailBox4.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudhuishouxiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.caogao_laye);
            mMailActivity.cloudhuishouxiang.setLayoutParams(mFajianLayerParams);
        }

    }

    public void chuishouListener() {
        if (mMailActivity.shoujianOpened5 == false) {
            mMailActivity.shoujianOpened5 = true;
//            mMailActivity.cloudhuishouxiangimg.setImageResource(R.drawable.shanchu_imgs);
//            mMailActivity.cloudhuishou.setTextColor(Color.parseColor("#1EA1F3"));
//            mMailActivity.cloudhuishouxiang.setBackgroundColor(Color.parseColor("#F5F5F5"));
            mMailActivity.mMoreMailBox5.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudlajixiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shanchu_morebox);
            mMailActivity.cloudlajixiang.setLayoutParams(mFajianLayerParams);

        } else {
            mMailActivity.shoujianOpened5 = false;
//            mMailActivity.cloudhuishouxiangimg.setImageResource(R.drawable.shanchu_img);
//            mMailActivity.cloudhuishou.setTextColor(Color.parseColor("#23272E"));
//            mMailActivity.cloudhuishouxiang.setBackgroundColor(Color.parseColor("#ffffff"));
            mMailActivity.mMoreMailBox5.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudlajixiang.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.shanchu_laye);
            mMailActivity.cloudlajixiang.setLayoutParams(mFajianLayerParams);
        }

    }

    public void clajiListener() {
        if (mMailActivity.shoujianOpened6 == false) {
            mMailActivity.shoujianOpened6 = true;
//            mMailActivity.cloudlajixiangimg.setImageResource(R.drawable.laji_imgs);
//            mMailActivity.cloudlaji.setTextColor(Color.parseColor("#1EA1F3"));
//            mMailActivity.cloudlajixiang.setBackgroundColor(Color.parseColor("#F5F5F5"));
            mMailActivity.mMoreMailBox6.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudall.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.laji_morebox);
            mMailActivity.cloudall.setLayoutParams(mFajianLayerParams);


        } else {
            mMailActivity.shoujianOpened6 = false;
//            mMailActivity.cloudlajixiangimg.setImageResource(R.drawable.laji_img);
//            mMailActivity.cloudlaji.setTextColor(Color.parseColor("#23272E"));
//            mMailActivity.cloudlajixiang.setBackgroundColor(Color.parseColor("#ffffff"));
            mMailActivity.mMoreMailBox6.setVisibility(View.INVISIBLE);

            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudall.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.laji_laye);
            mMailActivity.cloudall.setLayoutParams(mFajianLayerParams);

        }

    }

    public void allListener() {
        if (mMailActivity.shoujianOpened7 == false) {
            mMailActivity.shoujianOpened7 = true;
//            mMailActivity.cloudallimg.setImageResource(R.drawable.mailsendalls);
//            mMailActivity.cloudlaji.setTextColor(Color.parseColor("#1EA1F3"));
//            mMailActivity.cloudall.setBackgroundColor(Color.parseColor("#F5F5F5"));
            mMailActivity.mMoreMailBox7.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudtab.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.all_morebox);
            mMailActivity.cloudtab.setLayoutParams(mFajianLayerParams);

        } else {
            mMailActivity.shoujianOpened7 = false;
//            mMailActivity.cloudallimg.setImageResource(R.drawable.mailsendall);
//            mMailActivity.cloudlaji.setTextColor(Color.parseColor("#23272E"));
//            mMailActivity.cloudall.setBackgroundColor(Color.parseColor("#ffffff"));
            mMailActivity.mMoreMailBox7.setVisibility(View.INVISIBLE);

            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudtab.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.all_laye);
            mMailActivity.cloudtab.setLayoutParams(mFajianLayerParams);

        }

    }

    public void tabListener() {
        if (mMailActivity.shoujianOpened8 == false) {
            mMailActivity.shoujianOpened8 = true;
//            mMailActivity.cloudtabimg.setImageResource(R.drawable.mailtabs);
//            mMailActivity.cloudlaji.setTextColor(Color.parseColor("#1EA1F3"));
//            mMailActivity.cloudtab.setBackgroundColor(Color.parseColor("#F5F5F5"));
            mMailActivity.mMoreMailBox8.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudfile.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.tab_morebox);
            mMailActivity.cloudfile.setLayoutParams(mFajianLayerParams);

        } else {
            mMailActivity.shoujianOpened8 = false;
//            mMailActivity.cloudtabimg.setImageResource(R.drawable.mailtab);
//            mMailActivity.cloudlaji.setTextColor(Color.parseColor("#23272E"));
//            mMailActivity.cloudtab.setBackgroundColor(Color.parseColor("#ffffff"));
            mMailActivity.mMoreMailBox8.setVisibility(View.INVISIBLE);

            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudfile.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.tab_laye);
            mMailActivity.cloudfile.setLayoutParams(mFajianLayerParams);
        }
    }

    public void fileListener() {
        if (mMailActivity.shoujianOpened9 == false) {
            mMailActivity.shoujianOpened9 = true;
//            mMailActivity.cloudfileimg.setImageResource(R.drawable.mailfiles);
//            mMailActivity.cloudlaji.setTextColor(Color.parseColor("#1EA1F3"));
//            mMailActivity.cloudfile.setBackgroundColor(Color.parseColor("#F5F5F5"));
            mMailActivity.mMoreMailBox9.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudtype.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.file_morebox);
            mMailActivity.cloudtype.setLayoutParams(mFajianLayerParams);

        } else {
            mMailActivity.shoujianOpened9 = false;
//            mMailActivity.cloudfileimg.setImageResource(R.drawable.mailfile);
//            mMailActivity.cloudlaji.setTextColor(Color.parseColor("#23272E"));
//            mMailActivity.cloudfile.setBackgroundColor(Color.parseColor("#ffffff"));
            mMailActivity.mMoreMailBox9.setVisibility(View.INVISIBLE);

            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailActivity.cloudtype.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.file_laye);
            mMailActivity.cloudtype.setLayoutParams(mFajianLayerParams);
        }
    }

    public void typeListener() {
        if (MailManager.getInstance().mailType.show == false) {
            mMailActivity.waitDialog.show();
            MailAsks.getMailCustoms(mMailHandler,mMailActivity,mailtypeboxobj);

        } else {
            doHid(MailManager.getInstance().mailType);
        }
    }


    public void checkMailBoxViewc(boolean getmail) {
        LayoutInflater mInflater = (LayoutInflater) mMailActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMailActivity.mMoreMailBox1.removeAllViews();
        mMailActivity.mMoreMailBox3.removeAllViews();
        mMailActivity.mMoreMailBox4.removeAllViews();
        mMailActivity.mMoreMailBox5.removeAllViews();
        mMailActivity.mMoreMailBox6.removeAllViews();
        View mview1 = mInflater.inflate(R.layout.mail_box_layer1, null);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        Mailboxobj item = new Mailboxobj();
        item.state = 1;
        item.type = 0;
        mview1.setTag(item);
        if(getmail == true)
        {
            mMailActivity.type2 = -10;
            mMailActivity.select = mview1;
            startMailList(item);
        }
        TextView name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText(mMailActivity.getString(R.string.mail_all));
        TextView textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        getcount(MailManager.getInstance().allcount1, textViewhit);
        mMailActivity.shoujianhits.put("0", textViewhit);
        mMailActivity.mMoreMailBox1.addView(mview1);
        mview1 = mInflater.inflate(R.layout.mail_box_layer1, null);
        ImageView imageView = mview1.findViewById(R.id.shoujianxiangxin_img);
        imageView.setImageResource(R.drawable.yifa_img);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        item = new Mailboxobj();
        item.state = 2;
        item.type = 2;
        mview1.setTag(item);
        name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText(mMailActivity.getString(R.string.mail_all));
        textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        mMailActivity.yifahits.put("0", textViewhit);
        getcount(MailManager.getInstance().allcount2, textViewhit);
        mMailActivity.mMoreMailBox3.addView(mview1);
        mview1 = mInflater.inflate(R.layout.mail_box_layer1, null);
        imageView = mview1.findViewById(R.id.shoujianxiangxin_img);
        imageView.setImageResource(R.drawable.caogao_img);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        item = new Mailboxobj();
        item.state = 0;
        item.type = 3;
        mview1.setTag(item);
        name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText(mMailActivity.getString(R.string.mail_all));
        textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        mMailActivity.caogaohits.put("0", textViewhit);
        getcount(MailManager.getInstance().allcount4, textViewhit);
        mMailActivity.mMoreMailBox4.addView(mview1);
        mview1 = mInflater.inflate(R.layout.mail_box_layer1, null);
        imageView = mview1.findViewById(R.id.shoujianxiangxin_img);
        imageView.setImageResource(R.drawable.shanchu_img);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        item = new Mailboxobj();
        item.state = 3;
        item.type = 4;
        mview1.setTag(item);
        name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText(mMailActivity.getString(R.string.mail_all));
        textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        mMailActivity.huishouhits.put("0", textViewhit);
        getcount(MailManager.getInstance().allcount5, textViewhit);
        mMailActivity.mMoreMailBox5.addView(mview1);
        mview1 = mInflater.inflate(R.layout.mail_box_layer1, null);
        imageView = mview1.findViewById(R.id.shoujianxiangxin_img);
        imageView.setImageResource(R.drawable.laji_img);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        item = new Mailboxobj();
        item.state = 4;
        item.type = 5;
        mview1.setTag(item);
        name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText(mMailActivity.getString(R.string.mail_all));
        textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        mMailActivity.lajihits.put("0", textViewhit);
        getcount(MailManager.getInstance().allcount6, textViewhit);
        mMailActivity.mMoreMailBox6.addView(mview1);
        if(MailManager.getInstance().mSelectUser != null)
        {
            for (int i = 0; i < MailManager.getInstance().mOtherMailBoxs.size(); i++) {
                MailBox mMailBox = MailManager.getInstance().mMailBoxs.get(i);
                if (!mMailBox.isloacl) {
                    View mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                    item = new Mailboxobj();
                    item.state = 1;
                    item.type = 0;
                    item.name = getMailName(mMailBox.mAddress);
                    item.mailboxid = mMailBox.mRecordId;
                    mview.setTag(item);
                    mview.setOnClickListener(mMailActivity.mailBoxListener);
                    TextView name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                    name.setText(mMailBox.mAddress);
                    TextView textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                    mMailActivity.shoujianhits.put(mMailBox.mRecordId, textViewhit1);
                    getcount(mMailBox.count1, textViewhit1);
                    mMailActivity.mMoreMailBox1.addView(mview);
                    mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                    imageView = mview.findViewById(R.id.shoujianxiangxin_img);
                    imageView.setImageResource(R.drawable.yifa_img);
                    item = new Mailboxobj();
                    item.state = 2;
                    item.type = 2;
                    item.name = getMailName(mMailBox.mAddress);
                    item.mailboxid = mMailBox.mRecordId;
                    mview.setTag(item);
                    mview.setOnClickListener(mMailActivity.mailBoxListener);
                    name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                    name.setText(mMailBox.mAddress);
                    textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                    mMailActivity.yifahits.put(mMailBox.mRecordId, textViewhit1);
                    getcount(mMailBox.count2, textViewhit1);
                    mMailActivity.mMoreMailBox3.addView(mview);
                    mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                    imageView = mview.findViewById(R.id.shoujianxiangxin_img);
                    imageView.setImageResource(R.drawable.caogao_img);
                    item = new Mailboxobj();
                    item.state = 0;
                    item.type = 3;
                    item.name = getMailName(mMailBox.mAddress);
                    item.mailboxid = mMailBox.mRecordId;
                    mview.setTag(item);
                    mview.setOnClickListener(mMailActivity.mailBoxListener);
                    name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                    name.setText(mMailBox.mAddress);
                    textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                    mMailActivity.caogaohits.put(mMailBox.mRecordId, textViewhit1);
                    getcount(mMailBox.count4, textViewhit1);
                    mMailActivity.mMoreMailBox4.addView(mview);
                    mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                    imageView = mview.findViewById(R.id.shoujianxiangxin_img);
                    imageView.setImageResource(R.drawable.shanchu_img);
                    item = new Mailboxobj();
                    item.state = 3;
                    item.type = 4;
                    item.name = getMailName(mMailBox.mAddress);
                    item.mailboxid = mMailBox.mRecordId;
                    mview.setTag(item);
                    mview.setOnClickListener(mMailActivity.mailBoxListener);
                    name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                    name.setText(mMailBox.mAddress);
                    textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                    mMailActivity.huishouhits.put(mMailBox.mRecordId, textViewhit1);
                    getcount(mMailBox.count5, textViewhit1);
                    mMailActivity.mMoreMailBox5.addView(mview);
                    mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                    imageView = mview.findViewById(R.id.shoujianxiangxin_img);
                    imageView.setImageResource(R.drawable.laji_img);
                    item = new Mailboxobj();
                    item.state = 4;
                    item.type = 5;
                    item.name = getMailName(mMailBox.mAddress);
                    item.mailboxid = mMailBox.mRecordId;
                    mview.setTag(item);
                    mview.setOnClickListener(mMailActivity.mailBoxListener);
                    name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                    name.setText(mMailBox.mAddress);
                    textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                    mMailActivity.lajihits.put(mMailBox.mRecordId, textViewhit1);
                    getcount(mMailBox.count6, textViewhit1);
                    mMailActivity.mMoreMailBox6.addView(mview);

                }

            }
        }
        else
        {
            for (int i = 0; i < MailManager.getInstance().mMailBoxs.size(); i++) {
                MailBox mMailBox = MailManager.getInstance().mMailBoxs.get(i);
                if (!mMailBox.isloacl) {
                    View mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                    item = new Mailboxobj();
                    item.state = 1;
                    item.type = 0;
                    item.name = getMailName(mMailBox.mAddress);
                    item.mailboxid = mMailBox.mRecordId;
                    mview.setTag(item);
                    mview.setOnClickListener(mMailActivity.mailBoxListener);
                    TextView name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                    name.setText(mMailBox.mAddress);
                    TextView textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                    mMailActivity.shoujianhits.put(mMailBox.mRecordId, textViewhit1);
                    getcount(mMailBox.count1, textViewhit1);
                    mMailActivity.mMoreMailBox1.addView(mview);
                    mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                    imageView = mview.findViewById(R.id.shoujianxiangxin_img);
                    imageView.setImageResource(R.drawable.yifa_img);
                    item = new Mailboxobj();
                    item.state = 2;
                    item.type = 2;
                    item.name = getMailName(mMailBox.mAddress);
                    item.mailboxid = mMailBox.mRecordId;
                    mview.setTag(item);
                    mview.setOnClickListener(mMailActivity.mailBoxListener);
                    name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                    name.setText(mMailBox.mAddress);
                    textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                    mMailActivity.yifahits.put(mMailBox.mRecordId, textViewhit1);
                    getcount(mMailBox.count2, textViewhit1);
                    mMailActivity.mMoreMailBox3.addView(mview);
                    mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                    imageView = mview.findViewById(R.id.shoujianxiangxin_img);
                    imageView.setImageResource(R.drawable.caogao_img);
                    item = new Mailboxobj();
                    item.state = 0;
                    item.type = 3;
                    item.name = getMailName(mMailBox.mAddress);
                    item.mailboxid = mMailBox.mRecordId;
                    mview.setTag(item);
                    mview.setOnClickListener(mMailActivity.mailBoxListener);
                    name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                    name.setText(mMailBox.mAddress);
                    textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                    mMailActivity.caogaohits.put(mMailBox.mRecordId, textViewhit1);
                    getcount(mMailBox.count4, textViewhit1);
                    mMailActivity.mMoreMailBox4.addView(mview);
                    mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                    imageView = mview.findViewById(R.id.shoujianxiangxin_img);
                    imageView.setImageResource(R.drawable.shanchu_img);
                    item = new Mailboxobj();
                    item.state = 3;
                    item.type = 4;
                    item.name = getMailName(mMailBox.mAddress);
                    item.mailboxid = mMailBox.mRecordId;
                    mview.setTag(item);
                    mview.setOnClickListener(mMailActivity.mailBoxListener);
                    name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                    name.setText(mMailBox.mAddress);
                    textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                    mMailActivity.huishouhits.put(mMailBox.mRecordId, textViewhit1);
                    getcount(mMailBox.count5, textViewhit1);
                    mMailActivity.mMoreMailBox5.addView(mview);
                    mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                    imageView = mview1.findViewById(R.id.shoujianxiangxin_img);
                    imageView.setImageResource(R.drawable.laji_img);
                    item = new Mailboxobj();
                    item.state = 4;
                    item.type = 5;
                    item.name = getMailName(mMailBox.mAddress);
                    item.mailboxid = mMailBox.mRecordId;
                    mview.setTag(item);
                    mview.setOnClickListener(mMailActivity.mailBoxListener);
                    name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                    name.setText(mMailBox.mAddress);
                    textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                    mMailActivity.lajihits.put(mMailBox.mRecordId, textViewhit1);
                    getcount(mMailBox.count6, textViewhit1);
                    mMailActivity.mMoreMailBox6.addView(mview);

                }

            }
        }


    }

    public void checkMailBoxViewc1() {
        LayoutInflater mInflater = (LayoutInflater) mMailActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMailActivity.mMoreMailBox1.removeAllViews();
        mMailActivity.mMoreMailBox3.removeAllViews();
        mMailActivity.mMoreMailBox4.removeAllViews();
        mMailActivity.mMoreMailBox5.removeAllViews();
        mMailActivity.mMoreMailBox6.removeAllViews();
        View mview1 = mInflater.inflate(R.layout.mail_box_layer1, null);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        ImageView imageView = mview1.findViewById(R.id.shoujianxiangxin_img);
        imageView.setImageResource(R.drawable.shoujianxiangs);
        Mailboxobj item = new Mailboxobj();
        mMailActivity.select = mview1;
        item.state = 1;
        item.type = 0;
        mview1.setTag(item);
        startMailList(item);
        TextView name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText(mMailActivity.getString(R.string.mail_all));
        name1.setTextColor(Color.parseColor("#1EA1F3"));
        mview1.setBackgroundColor(Color.parseColor("#F5F5F5"));
        TextView textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        getcount(MailManager.getInstance().allcount1, textViewhit);
        mMailActivity.shoujianhits.put("0", textViewhit);
        mMailActivity.mMoreMailBox1.addView(mview1);
        mview1 = mInflater.inflate(R.layout.mail_box_layer1, null);
        imageView = mview1.findViewById(R.id.shoujianxiangxin_img);
        imageView.setImageResource(R.drawable.yifa_img);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        item = new Mailboxobj();
        item.state = 2;
        item.type = 2;
        mview1.setTag(item);
        name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText(mMailActivity.getString(R.string.mail_all));
        textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        mMailActivity.yifahits.put("0", textViewhit);
        getcount(MailManager.getInstance().allcount2, textViewhit);
        mMailActivity.mMoreMailBox3.addView(mview1);
        mview1 = mInflater.inflate(R.layout.mail_box_layer1, null);
        imageView = mview1.findViewById(R.id.shoujianxiangxin_img);
        imageView.setImageResource(R.drawable.caogao_img);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        item = new Mailboxobj();
        item.state = 0;
        item.type = 3;
        mview1.setTag(item);
        name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText(mMailActivity.getString(R.string.mail_all));
        textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        mMailActivity.caogaohits.put("0", textViewhit);
        getcount(MailManager.getInstance().allcount4, textViewhit);
        mMailActivity.mMoreMailBox4.addView(mview1);
        mview1 = mInflater.inflate(R.layout.mail_box_layer1, null);
        imageView = mview1.findViewById(R.id.shoujianxiangxin_img);
        imageView.setImageResource(R.drawable.shanchu_img);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        item = new Mailboxobj();
        item.state = 3;
        item.type = 4;
        mview1.setTag(item);
        name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText(mMailActivity.getString(R.string.mail_all));
        textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        mMailActivity.huishouhits.put("0", textViewhit);
        getcount(MailManager.getInstance().allcount5, textViewhit);
        mMailActivity.mMoreMailBox5.addView(mview1);
        mview1 = mInflater.inflate(R.layout.mail_box_layer1, null);
        imageView = mview1.findViewById(R.id.shoujianxiangxin_img);
        imageView.setImageResource(R.drawable.laji_img);
        mview1.setOnClickListener(mMailActivity.mailBoxListener);
        item = new Mailboxobj();
        item.state = 4;
        item.type = 5;
        mview1.setTag(item);
        name1 = (TextView) mview1.findViewById(R.id.shoujianxiang_text_xin);
        name1.setText(mMailActivity.getString(R.string.mail_all));
        textViewhit = mview1.findViewById(R.id.shoujianxiang_count_text_xin);
        mMailActivity.lajihits.put("0", textViewhit);
        getcount(MailManager.getInstance().allcount6, textViewhit);
        mMailActivity.mMoreMailBox6.addView(mview1);

        for (int i = 0; i < MailManager.getInstance().mMailBoxs.size(); i++) {
            MailBox mMailBox = MailManager.getInstance().mMailBoxs.get(i);
            if (!mMailBox.isloacl) {
                View mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                item = new Mailboxobj();
                item.state = 1;
                item.type = 0;
                item.mailboxid = mMailBox.mRecordId;
                mview.setTag(item);
                mview.setOnClickListener(mMailActivity.mailBoxListener);
                TextView name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                name.setText(mMailBox.mAddress);
                TextView textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                mMailActivity.shoujianhits.put(mMailBox.mRecordId, textViewhit1);
                getcount(mMailBox.count1, textViewhit1);
                mMailActivity.mMoreMailBox1.addView(mview);
                mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                imageView = mview.findViewById(R.id.shoujianxiangxin_img);
                imageView.setImageResource(R.drawable.yifa_img);
                item = new Mailboxobj();
                item.state = 2;
                item.type = 2;
                item.mailboxid = mMailBox.mRecordId;
                mview.setTag(item);
                mview.setOnClickListener(mMailActivity.mailBoxListener);
                name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                name.setText(mMailBox.mAddress);
                textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                mMailActivity.yifahits.put(mMailBox.mRecordId, textViewhit1);
                getcount(mMailBox.count2, textViewhit1);
                mMailActivity.mMoreMailBox3.addView(mview);
                mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                imageView = mview.findViewById(R.id.shoujianxiangxin_img);
                imageView.setImageResource(R.drawable.caogao_img);
                item = new Mailboxobj();
                item.state = 0;
                item.type = 3;
                item.mailboxid = mMailBox.mRecordId;
                mview.setTag(item);
                mview.setOnClickListener(mMailActivity.mailBoxListener);
                name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                name.setText(mMailBox.mAddress);
                textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                mMailActivity.caogaohits.put(mMailBox.mRecordId, textViewhit1);
                getcount(mMailBox.count4, textViewhit1);
                mMailActivity.mMoreMailBox4.addView(mview);
                mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                imageView = mview.findViewById(R.id.shoujianxiangxin_img);
                imageView.setImageResource(R.drawable.shanchu_img);
                item = new Mailboxobj();
                item.state = 3;
                item.type = 4;
                item.mailboxid = mMailBox.mRecordId;
                mview.setTag(item);
                mview.setOnClickListener(mMailActivity.mailBoxListener);
                name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                name.setText(mMailBox.mAddress);
                textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                mMailActivity.huishouhits.put(mMailBox.mRecordId, textViewhit1);
                getcount(mMailBox.count5, textViewhit1);
                mMailActivity.mMoreMailBox5.addView(mview);
                mview = mInflater.inflate(R.layout.mail_box_layer1, null);
                imageView = mview.findViewById(R.id.shoujianxiangxin_img);
                imageView.setImageResource(R.drawable.laji_img);
                item = new Mailboxobj();
                item.state = 4;
                item.type = 5;
                item.mailboxid = mMailBox.mRecordId;
                mview.setTag(item);
                mview.setOnClickListener(mMailActivity.mailBoxListener);
                name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
                name.setText(mMailBox.mAddress);
                textViewhit1 = mview.findViewById(R.id.shoujianxiang_count_text_xin);
                mMailActivity.lajihits.put(mMailBox.mRecordId, textViewhit1);
                getcount(mMailBox.count6, textViewhit1);
                mMailActivity.mMoreMailBox6.addView(mview);

            }

        }
        updataAllsendView();
        updataAllLableView();
        updataAlFileView();
    }


    public String getCount(Mailboxobj mailboxobj) {
        switch (mailboxobj.type) {
            case 0:
                if (mailboxobj.mailboxid.length() == 0) {
                    return String.valueOf(MailManager.getInstance().allcount1) + mMailActivity.getString(R.string.mail_unread2);
                } else {
                    MailBox mMailBox = MailManager.getInstance().hashMailBox.get(mailboxobj.mailboxid);
                    return String.valueOf(mMailBox.count1) + mMailActivity.getString(R.string.mail_unread2);
                }
            case 1:
                if (mailboxobj.mailboxid.length() == 0) {
                    return String.valueOf(MailManager.getInstance().me_approval + MailManager.getInstance().to_me_approval) + mMailActivity.getString(R.string.mail_unread);
                } else {
                    if (mailboxobj.id == 1) {
                        return String.valueOf(MailManager.getInstance().to_me_approval) + mMailActivity.getString(R.string.mail_unread2);
                    } else {
                        return String.valueOf(MailManager.getInstance().me_approval) + mMailActivity.getString(R.string.mail_unread2);
                    }
                }
            case 2:
                if (mailboxobj.mailboxid.length() == 0) {
                    return String.valueOf(MailManager.getInstance().allcount2) + mMailActivity.getString(R.string.mail_sunend);
                } else {
                    MailBox mMailBox = MailManager.getInstance().hashMailBox.get(mailboxobj.mailboxid);
                    return String.valueOf(mMailBox.count2) + mMailActivity.getString(R.string.mail_sunend);
                }
            case 3:
                if (mailboxobj.mailboxid.length() == 0) {
                    return String.valueOf(MailManager.getInstance().allcount4) + mMailActivity.getString(R.string.unit1);
                } else {
                    MailBox mMailBox = MailManager.getInstance().hashMailBox.get(mailboxobj.mailboxid);
                    return String.valueOf(mMailBox.count4) + mMailActivity.getString(R.string.unit1);
                }
            case 4:
                if (mailboxobj.mailboxid.length() == 0) {
                    return String.valueOf(MailManager.getInstance().allcount5) + mMailActivity.getString(R.string.unit1);
                } else {
                    MailBox mMailBox = MailManager.getInstance().hashMailBox.get(mailboxobj.mailboxid);
                    return String.valueOf(mMailBox.count5) + mMailActivity.getString(R.string.unit1);
                }
            case 5:
                if (mailboxobj.mailboxid.length() == 0) {
                    return String.valueOf(MailManager.getInstance().allcount6) + mMailActivity.getString(R.string.unit1);
                } else {
                    MailBox mMailBox = MailManager.getInstance().hashMailBox.get(mailboxobj.mailboxid);
                    return String.valueOf(mMailBox.count6) + mMailActivity.getString(R.string.unit1);
                }
        }
        return "";
    }

    private void getcount(int count, TextView mTextView) {
        if (count == 0) {
            mTextView.setVisibility(View.INVISIBLE);
        } else {
            mTextView.setVisibility(View.VISIBLE);
            if (count > 99) {
                mTextView.setText(String.valueOf(99) + "+");
            } else {
                mTextView.setText(String.valueOf(count));
            }
        }

    }


    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > mMailActivity.mBasePresenter.mScreenDefine.verticalMinDistance * mMailActivity.mBasePresenter.mScreenDefine.density && Math.abs(velocityX) > 0) {
            return false;
        } else if (e2.getX() - e1.getX() > mMailActivity.mBasePresenter.mScreenDefine.verticalMinDistance * mMailActivity.mBasePresenter.mScreenDefine.density && Math.abs(velocityX) > 0) {

            if (e2.getX() - e1.getX() > e2.getY() - e1.getY() && e2.getX() - e1.getX() > e1.getY() - e2.getY()) {
                showLeftView();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void showLeftView() {
        if (mMailActivity.popupWindow != null) {
            mMailActivity.popupWindow.dismiss();
        }
        mMailActivity.popupWindow = AppUtils.creatLeftView(mMailActivity, mMailActivity.shade, mMailActivity.findViewById(R.id.activity_mail), mMailActivity.leftView);
    }


    public void getMails(boolean isupdatall,Mailboxobj mMailBoxModel) {
        if (MailManager.getInstance().account.iscloud == false) {
            if (isupdatall) {
                mMailActivity.mails.clear();
                mMailActivity.isAll = false;
                mMailActivity.startPos = 0;
                mMailActivity.waitDialog.show();
                MailAsks.getMials(mMailActivity, mMailHandler, mMailActivity.getIntent().getIntExtra("id", 0),
                        mMailActivity.getIntent().getIntExtra("type", 0), mMailActivity.getIntent().getStringExtra("mailboxid"), mMailActivity.startPos,mMailBoxModel);
            } else {
                if (mMailActivity.isAll == false) {
                    mMailActivity.waitDialog.show();
                    MailAsks.getMials(mMailActivity, mMailHandler, mMailActivity.getIntent().getIntExtra("id", 0),
                            mMailActivity.getIntent().getIntExtra("type", 0), mMailActivity.getIntent().getStringExtra("mailboxid"), mMailActivity.startPos,mMailBoxModel);
                } else
                    AppUtils.showMessage(mMailActivity, mMailActivity.getString(R.string.message_addall));
            }
        } else {
            if (mMailActivity.type2 == -1) {

                if (mMailActivity.mViewPager.getCurrentItem() == 0) {
                    if (isupdatall) {
                        mMailActivity.mMailItems1.clear();
                        mMailActivity.isall1 = false;
                        mMailActivity.waitDialog.show();
                        MailAsks.getMials(mMailActivity, mMailHandler, mMailActivity.type1,
                                mMailActivity.type2, mMailActivity.mViewPager.getCurrentItem(), mMailActivity.mailboxid, 1, mMailActivity.mStartpos1 * 40,mMailBoxModel);
                        mMailActivity.mStartpos1 = 1;
                    } else {
                        if (mMailActivity.isall1 == false) {
                            mMailActivity.waitDialog.show();
                            MailAsks.getMials(mMailActivity, mMailHandler, mMailActivity.type1,
                                    mMailActivity.type2, mMailActivity.mViewPager.getCurrentItem(), mMailActivity.mailboxid, mMailActivity.mStartpos1, 40,mMailBoxModel);
                        } else
                            AppUtils.showMessage(mMailActivity, mMailActivity.getString(R.string.message_addall));
                    }
                } else if (mMailActivity.mViewPager.getCurrentItem() == 1) {
                    if (isupdatall) {
                        mMailActivity.mMailItems2.clear();
                        mMailActivity.isall2 = false;
                        mMailActivity.waitDialog.show();
                        MailAsks.getMials(mMailActivity, mMailHandler, mMailActivity.type1,
                                mMailActivity.type2, mMailActivity.mViewPager.getCurrentItem(), mMailActivity.mailboxid, 1, mMailActivity.mStartpos2 * 40,mMailBoxModel);
                        mMailActivity.mStartpos2 = 1;
                    } else {
                        if (mMailActivity.isall2 == false) {
                            mMailActivity.waitDialog.show();
                            MailAsks.getMials(mMailActivity, mMailHandler, mMailActivity.type1,
                                    mMailActivity.type2, mMailActivity.mViewPager.getCurrentItem(), mMailActivity.mailboxid, mMailActivity.mStartpos2, 40,mMailBoxModel);
                        } else
                            AppUtils.showMessage(mMailActivity, mMailActivity.getString(R.string.message_addall));
                    }
                } else if (mMailActivity.mViewPager.getCurrentItem() == 2) {
                    if (isupdatall) {
                        mMailActivity.mMailItems3.clear();
                        mMailActivity.isall3 = false;
                        mMailActivity.waitDialog.show();
                        MailAsks.getMials(mMailActivity, mMailHandler, mMailActivity.type1,
                                mMailActivity.type2, mMailActivity.mViewPager.getCurrentItem(), mMailActivity.mailboxid, 1, mMailActivity.mStartpos3 * 40,mMailBoxModel);
                        mMailActivity.mStartpos2 = 1;
                    } else {
                        if (mMailActivity.isall3 == false) {
                            mMailActivity.waitDialog.show();
                            MailAsks.getMials(mMailActivity, mMailHandler, mMailActivity.type1,
                                    mMailActivity.type2, mMailActivity.mViewPager.getCurrentItem(), mMailActivity.mailboxid, mMailActivity.mStartpos3, 40,mMailBoxModel);
                        } else
                            AppUtils.showMessage(mMailActivity, mMailActivity.getString(R.string.message_addall));
                    }
                } else if (mMailActivity.mViewPager.getCurrentItem() == 3) {
                    if (isupdatall) {
                        mMailActivity.mMailItems4.clear();
                        mMailActivity.isall4 = false;
                        mMailActivity.waitDialog.show();
                        MailAsks.getMials(mMailActivity, mMailHandler, mMailActivity.type1,
                                mMailActivity.type2, mMailActivity.mViewPager.getCurrentItem(), mMailActivity.mailboxid, 1, mMailActivity.mStartpos4 * 40,mMailBoxModel);
                        mMailActivity.mStartpos2 = 1;
                    } else {
                        if (mMailActivity.isall4 == false) {
                            mMailActivity.waitDialog.show();
                            MailAsks.getMials(mMailActivity, mMailHandler, mMailActivity.type1,
                                    mMailActivity.type2, mMailActivity.mViewPager.getCurrentItem(), mMailActivity.mailboxid, mMailActivity.mStartpos4, 40,mMailBoxModel);
                        }

                    }
                }
            } else {
                if (isupdatall) {
                    mMailActivity.mails.clear();
                    mMailActivity.isAll = false;
                    mMailActivity.waitDialog.show();
                    MailAsks.getMials(mMailActivity, mMailHandler, mMailActivity.type1,
                            mMailActivity.type2, 0, mMailActivity.mailboxid, 1, mMailActivity.startPos * 40,mMailBoxModel);
                } else {
                    if (mMailActivity.isAll == false) {
                        mMailActivity.waitDialog.show();
                        MailAsks.getMials(mMailActivity, mMailHandler, mMailActivity.type1,
                                mMailActivity.type2, 0, mMailActivity.mailboxid, mMailActivity.startPos, 40,mMailBoxModel);
                    } else
                        AppUtils.showMessage(mMailActivity, mMailActivity.getString(R.string.message_addall));
                }

            }
        }

    }

    public void onFoot() {
        getMails(false,mailboxobj);
    }

    public void onHead() {
        getMails(true,mailboxobj);
    }

    public void startMail(Mail mail) {
        if (mMailActivity.popupWindow != null) {
            if (mMailActivity.popupWindow.isShowing()) {
                return;
            }
        }
        if (mMailActivity.buttomLayer2.getVisibility() == View.VISIBLE) {
            if (mail.isSelect == false) {
                mail.isSelect = true;
            } else {
                mail.isSelect = false;
            }
            mMailActivity.mMailItemAdapter.notifyDataSetChanged();
        } else {
            Intent mIntent = new Intent();
            mIntent.setClass(mMailActivity, MailShowActivity.class);
            mIntent.putExtra("mail", mail);
            if (MailManager.getInstance().account.iscloud == false)
                mIntent.putExtra("type", mMailActivity.type1);
            else
                mIntent.putExtra("type", mMailActivity.type2);
            if(mailboxobj != null)
            {
                if(mailboxobj.type == 61 || mailboxobj.type == 62)
                {
                    mIntent.putExtra("push", true);
                }
            }

            mMailActivity.startActivity(mIntent);
        }
    }

//    public void startDelete(Mail mail)
//    {
//        if(mMailActivity.mDelete.getVisibility() == View.INVISIBLE)
//        {
//            mMailActivity.mDelete.setVisibility(View.VISIBLE);
//            mail.isSelect = true;
//
//        }
//        ImageView back = mMailActivity.findViewById(R.id.back);
//        back.setVisibility(View.INVISIBLE);
//        TextView cancle = mMailActivity.findViewById(R.id.cancle);
//        cancle.setVisibility(View.VISIBLE);
//        cancle.setOnClickListener(stopDelectListener);
//        if(mMailActivity.mMailItemAdapter != null)
//        mMailActivity.mMailItemAdapter.notifyDataSetChanged();
//        if(mMailActivity.mMailItemAdapter1 != null)
//            mMailActivity.mMailItemAdapter1.notifyDataSetChanged();
//        if(mMailActivity.mMailItemAdapter2 != null)
//            mMailActivity.mMailItemAdapter2.notifyDataSetChanged();
//        if(mMailActivity.mMailItemAdapter3 != null)
//            mMailActivity.mMailItemAdapter3.notifyDataSetChanged();
//        if(mMailActivity.mMailItemAdapter4 != null)
//            mMailActivity.mMailItemAdapter4.notifyDataSetChanged();
//    }

    public void startManager() {
        if(mailboxobj != null)
        {
            if(mailboxobj.type == 1)
            {
                if(mailboxobj.id == 1)
                {
                    if (mMailActivity.buttomLayer4.getVisibility() == View.INVISIBLE) {
                        mMailActivity.buttomLayer4.setVisibility(View.VISIBLE);
                        mMailActivity.manager.setText(mMailActivity.getString(R.string.button_word_finish));
                        if (mMailActivity.mMailItemAdapter != null)
                            mMailActivity.mMailItemAdapter.edit = true;
                        mMailActivity.mMailItemAdapter.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter1 != null)
                            mMailActivity.mMailItemAdapter1.edit = true;
                        mMailActivity.mMailItemAdapter1.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter2 != null)
                            mMailActivity.mMailItemAdapter2.edit = true;
                        mMailActivity.mMailItemAdapter2.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter3 != null)
                            mMailActivity.mMailItemAdapter3.edit = true;
                        mMailActivity.mMailItemAdapter3.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter4 != null)
                            mMailActivity.mMailItemAdapter4.edit = true;
                        mMailActivity.mMailItemAdapter4.notifyDataSetChanged();
                    } else {

                        mMailActivity.buttomLayer4.setVisibility(View.INVISIBLE);
                        mMailActivity.manager.setText(mMailActivity.getString(R.string.button_manager));

                        for (int i = 0; i < mMailActivity.mails.size(); i++) {
                            mMailActivity.mails.get(i).isSelect = false;
                        }
                        for (int i = 0; i < mMailActivity.mMailItems1.size(); i++) {
                            mMailActivity.mMailItems1.get(i).isSelect = false;
                        }
                        for (int i = 0; i < mMailActivity.mMailItems2.size(); i++) {
                            mMailActivity.mMailItems2.get(i).isSelect = false;
                        }
                        for (int i = 0; i < mMailActivity.mMailItems3.size(); i++) {
                            mMailActivity.mMailItems3.get(i).isSelect = false;
                        }
                        for (int i = 0; i < mMailActivity.mMailItems4.size(); i++) {
                            mMailActivity.mMailItems4.get(i).isSelect = false;
                        }

                        if (mMailActivity.mMailItemAdapter != null)
                            mMailActivity.mMailItemAdapter.edit = false;
                        mMailActivity.mMailItemAdapter.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter1 != null)
                            mMailActivity.mMailItemAdapter1.edit = false;
                        mMailActivity.mMailItemAdapter1.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter2 != null)
                            mMailActivity.mMailItemAdapter2.edit = false;
                        mMailActivity.mMailItemAdapter2.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter3 != null)
                            mMailActivity.mMailItemAdapter3.edit = false;
                        mMailActivity.mMailItemAdapter3.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter4 != null)
                            mMailActivity.mMailItemAdapter4.edit = false;
                        mMailActivity.mMailItemAdapter4.notifyDataSetChanged();
                    }
                }
                else
                {
                    if (mMailActivity.buttomLayer3.getVisibility() == View.INVISIBLE) {
                        mMailActivity.buttomLayer3.setVisibility(View.VISIBLE);
                        mMailActivity.manager.setText(mMailActivity.getString(R.string.button_word_finish));
                        if (mMailActivity.mMailItemAdapter != null)
                            mMailActivity.mMailItemAdapter.edit = true;
                        mMailActivity.mMailItemAdapter.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter1 != null)
                            mMailActivity.mMailItemAdapter1.edit = true;
                        mMailActivity.mMailItemAdapter1.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter2 != null)
                            mMailActivity.mMailItemAdapter2.edit = true;
                        mMailActivity.mMailItemAdapter2.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter3 != null)
                            mMailActivity.mMailItemAdapter3.edit = true;
                        mMailActivity.mMailItemAdapter3.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter4 != null)
                            mMailActivity.mMailItemAdapter4.edit = true;
                        mMailActivity.mMailItemAdapter4.notifyDataSetChanged();
                    } else {

                        mMailActivity.buttomLayer3.setVisibility(View.INVISIBLE);
                        mMailActivity.manager.setText(mMailActivity.getString(R.string.button_manager));

                        for (int i = 0; i < mMailActivity.mails.size(); i++) {
                            mMailActivity.mails.get(i).isSelect = false;
                        }
                        for (int i = 0; i < mMailActivity.mMailItems1.size(); i++) {
                            mMailActivity.mMailItems1.get(i).isSelect = false;
                        }
                        for (int i = 0; i < mMailActivity.mMailItems2.size(); i++) {
                            mMailActivity.mMailItems2.get(i).isSelect = false;
                        }
                        for (int i = 0; i < mMailActivity.mMailItems3.size(); i++) {
                            mMailActivity.mMailItems3.get(i).isSelect = false;
                        }
                        for (int i = 0; i < mMailActivity.mMailItems4.size(); i++) {
                            mMailActivity.mMailItems4.get(i).isSelect = false;
                        }

                        if (mMailActivity.mMailItemAdapter != null)
                            mMailActivity.mMailItemAdapter.edit = false;
                        mMailActivity.mMailItemAdapter.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter1 != null)
                            mMailActivity.mMailItemAdapter1.edit = false;
                        mMailActivity.mMailItemAdapter1.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter2 != null)
                            mMailActivity.mMailItemAdapter2.edit = false;
                        mMailActivity.mMailItemAdapter2.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter3 != null)
                            mMailActivity.mMailItemAdapter3.edit = false;
                        mMailActivity.mMailItemAdapter3.notifyDataSetChanged();
                        if (mMailActivity.mMailItemAdapter4 != null)
                            mMailActivity.mMailItemAdapter4.edit = false;
                        mMailActivity.mMailItemAdapter4.notifyDataSetChanged();
                    }
                }

                return;
            }

        }
        if (mMailActivity.buttomLayer2.getVisibility() == View.INVISIBLE) {
            mMailActivity.buttomLayer2.setVisibility(View.VISIBLE);
            mMailActivity.manager.setText(mMailActivity.getString(R.string.button_word_finish));
            if (mMailActivity.mMailItemAdapter != null)
                mMailActivity.mMailItemAdapter.edit = true;
                mMailActivity.mMailItemAdapter.notifyDataSetChanged();
            if (mMailActivity.mMailItemAdapter1 != null)
                mMailActivity.mMailItemAdapter1.edit = true;
                mMailActivity.mMailItemAdapter1.notifyDataSetChanged();
            if (mMailActivity.mMailItemAdapter2 != null)
                mMailActivity.mMailItemAdapter2.edit = true;
                mMailActivity.mMailItemAdapter2.notifyDataSetChanged();
            if (mMailActivity.mMailItemAdapter3 != null)
                mMailActivity.mMailItemAdapter3.edit = true;
                mMailActivity.mMailItemAdapter3.notifyDataSetChanged();
            if (mMailActivity.mMailItemAdapter4 != null)
                mMailActivity.mMailItemAdapter4.edit = true;
                mMailActivity.mMailItemAdapter4.notifyDataSetChanged();
        } else {

            mMailActivity.buttomLayer2.setVisibility(View.INVISIBLE);
            mMailActivity.manager.setText(mMailActivity.getString(R.string.button_manager));

            for (int i = 0; i < mMailActivity.mails.size(); i++) {
                mMailActivity.mails.get(i).isSelect = false;
            }
            for (int i = 0; i < mMailActivity.mMailItems1.size(); i++) {
                mMailActivity.mMailItems1.get(i).isSelect = false;
            }
            for (int i = 0; i < mMailActivity.mMailItems2.size(); i++) {
                mMailActivity.mMailItems2.get(i).isSelect = false;
            }
            for (int i = 0; i < mMailActivity.mMailItems3.size(); i++) {
                mMailActivity.mMailItems3.get(i).isSelect = false;
            }
            for (int i = 0; i < mMailActivity.mMailItems4.size(); i++) {
                mMailActivity.mMailItems4.get(i).isSelect = false;
            }

            if (mMailActivity.mMailItemAdapter != null)
                mMailActivity.mMailItemAdapter.edit = false;
                mMailActivity.mMailItemAdapter.notifyDataSetChanged();
            if (mMailActivity.mMailItemAdapter1 != null)
                mMailActivity.mMailItemAdapter1.edit = false;
                mMailActivity.mMailItemAdapter1.notifyDataSetChanged();
            if (mMailActivity.mMailItemAdapter2 != null)
                mMailActivity.mMailItemAdapter2.edit = false;
                mMailActivity.mMailItemAdapter2.notifyDataSetChanged();
            if (mMailActivity.mMailItemAdapter3 != null)
                mMailActivity.mMailItemAdapter3.edit = false;
                mMailActivity.mMailItemAdapter3.notifyDataSetChanged();
            if (mMailActivity.mMailItemAdapter4 != null)
                mMailActivity.mMailItemAdapter4.edit = false;
                mMailActivity.mMailItemAdapter4.notifyDataSetChanged();
        }
    }

    public void updataMails()
    {
        if (mMailActivity.mMailItemAdapter != null)
        mMailActivity.mMailItemAdapter.notifyDataSetChanged();
        if (mMailActivity.mMailItemAdapter1 != null)
        mMailActivity.mMailItemAdapter1.notifyDataSetChanged();
        if (mMailActivity.mMailItemAdapter2 != null)
        mMailActivity.mMailItemAdapter2.notifyDataSetChanged();
        if (mMailActivity.mMailItemAdapter3 != null)
        mMailActivity.mMailItemAdapter3.notifyDataSetChanged();
        if (mMailActivity.mMailItemAdapter4 != null)
        mMailActivity.mMailItemAdapter4.notifyDataSetChanged();
    }


    public View.OnClickListener stopDelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            stopDelete();
        }
    };

//    public void stopDelete() {
//
//        if (mMailActivity.mDelete.getVisibility() == View.VISIBLE) {
//            mMailActivity.mDelete.setVisibility(View.INVISIBLE);
//
//        }
//        ImageView back = mMailActivity.findViewById(R.id.back);
//        back.setVisibility(View.VISIBLE);
//        TextView cancle = mMailActivity.findViewById(R.id.cancle);
//        cancle.setVisibility(View.INVISIBLE);
//        for (int i = 0; i < mMailActivity.mails.size(); i++) {
//            mMailActivity.mails.get(i).isSelect = false;
//        }
//        for (int i = 0; i < mMailActivity.mMailItems1.size(); i++) {
//            mMailActivity.mMailItems1.get(i).isSelect = false;
//        }
//        for (int i = 0; i < mMailActivity.mMailItems2.size(); i++) {
//            mMailActivity.mMailItems2.get(i).isSelect = false;
//        }
//        for (int i = 0; i < mMailActivity.mMailItems3.size(); i++) {
//            mMailActivity.mMailItems3.get(i).isSelect = false;
//        }
//        for (int i = 0; i < mMailActivity.mMailItems4.size(); i++) {
//            mMailActivity.mMailItems4.get(i).isSelect = false;
//        }
//        if (mMailActivity.mMailItemAdapter != null)
//            mMailActivity.mMailItemAdapter.notifyDataSetChanged();
//        if (mMailActivity.mMailItemAdapter1 != null)
//            mMailActivity.mMailItemAdapter1.notifyDataSetChanged();
//        if (mMailActivity.mMailItemAdapter2 != null)
//            mMailActivity.mMailItemAdapter2.notifyDataSetChanged();
//        if (mMailActivity.mMailItemAdapter3 != null)
//            mMailActivity.mMailItemAdapter3.notifyDataSetChanged();
//        if (mMailActivity.mMailItemAdapter4 != null)
//            mMailActivity.mMailItemAdapter4.notifyDataSetChanged();
//
//    }

    public void deleteMail() {

        ArrayList<Mail> mails = new ArrayList<Mail>();
        for (int i = 0; i < mMailActivity.mails.size(); i++) {
            if (mMailActivity.mails.get(i).isSelect == true) {
                mails.add(mMailActivity.mails.get(i));
            }
        }
        if(mailboxobj != null)
        {
            if(mailboxobj.type == 61 || mailboxobj.type == 62)
            {
                mMailActivity.waitDialog.show();
                MailAsks.setdeletePushMails(mMailActivity, mMailHandler, mails);
                return;
            }
        }
        mMailActivity.waitDialog.show();
        MailAsks.setdeleteMails(mMailActivity, mMailHandler, mails,mailboxobj);
    }


    public void seletTabMail() {
        ArrayList<Mail> mails = new ArrayList<Mail>();
        for (int i = 0; i < mMailActivity.mails.size(); i++) {
            Mail mail = mMailActivity.mails.get(i);
            if (mail.isSelect == true) {
                mails.add(mail);
            }
        }
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        MenuItem menuItem = new MenuItem();
        menuItem.item =mails;
        menuItem.mListener = setreadListener;
        menuItem.btnName = mMailActivity.getString(R.string.mail_set_read);
        menuItems.add(menuItem);
        menuItem = new MenuItem();
        menuItem.item =mails;
        menuItem.mListener = setunreadListener;
        menuItem.btnName = mMailActivity.getString(R.string.mail_set_unread);
        menuItems.add(menuItem);
        menuItem = new MenuItem();
        menuItem.item =mails;
        menuItem.mListener = setnorepeatListener;
        menuItem.btnName = mMailActivity.getString(R.string.mail_set_repeeat);
        menuItems.add(menuItem);
        menuItem = new MenuItem();
        menuItem.item =mails;
        menuItem.mListener = setunnorepeatListener;
        menuItem.btnName = mMailActivity.getString(R.string.mail_set_unrepeeat);
        menuItems.add(menuItem);
        menuItem = new MenuItem();
        menuItem.item =mails;
        menuItem.mListener = setTabListener;
        menuItem.btnName = mMailActivity.getString(R.string.mail_set_tab);
        menuItems.add(menuItem);
        menuItem = new MenuItem();
        menuItem.item =mails;
        menuItem.mListener = setreadallListener;
        menuItem.btnName = mMailActivity.getString(R.string.mail_set_allread);
        menuItems.add(menuItem);
        if(popupWindowbutom != null)
        {
            popupWindowbutom.dismiss();
        }
        popupWindowbutom = AppUtils.creatButtomMenu(mMailActivity,mMailActivity.shade,menuItems,mMailActivity.findViewById(R.id.activity_mail));
    }

    public View.OnClickListener setTabListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            ArrayList<Mail> mails = (ArrayList<Mail>) v.getTag();
            Intent intent = new Intent(mMailActivity, MailLableActivity.class);
            intent.putExtra("mails",mails);
            mMailActivity.startActivity(intent);
            if(popupWindowbutom != null)
            {
                popupWindowbutom.dismiss();
            }
        }
    };


    public void guibinMail() {
        ArrayList<Mail> mails = new ArrayList<Mail>();
        for (int i = 0; i < mMailActivity.mails.size(); i++) {
            Mail mail = mMailActivity.mails.get(i);
            if (mail.isSelect == true) {
                mails.add(mail);
            }
        }
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        MenuItem menuItem = new MenuItem();
        menuItem.item =mails;
        menuItem.mListener = guibinSelectListener;
        menuItem.btnName = mMailActivity.getString(R.string.mail_guibin_select);
        menuItems.add(menuItem);
        menuItem = new MenuItem();
        menuItem.item =mails;
        menuItem.mListener = guibinAllListener;
        menuItem.btnName = mMailActivity.getString(R.string.mail_guibin_all);
        menuItems.add(menuItem);
        menuItem = new MenuItem();
        menuItem.item =mails;
        menuItem.mListener = guibinRepeattListener;
        menuItem.btnName = mMailActivity.getString(R.string.mail_guibin_repeat);
        menuItems.add(menuItem);
        menuItem = new MenuItem();
        menuItem.item =mails;
        menuItem.mListener = guibinReadListener;
        menuItem.btnName = mMailActivity.getString(R.string.mail_guibin_read);
        menuItems.add(menuItem);
        popupWindowbutom = AppUtils.creatButtomMenu(mMailActivity,mMailActivity.shade,menuItems,mMailActivity.findViewById(R.id.activity_mail));
    }


    public View.OnClickListener guibinSelectListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            ArrayList<Mail> mails = (ArrayList<Mail>) v.getTag();
            mMailActivity.waitDialog.show();
            managerMails(0,mails);
            if(popupWindowbutom != null)
            {
                popupWindowbutom.dismiss();
            }
        }
    };

    public View.OnClickListener guibinAllListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            ArrayList<Mail> mails = (ArrayList<Mail>) v.getTag();
            mMailActivity.waitDialog.show();
            managerMails(1,mails);
            if(popupWindowbutom != null)
            {
                popupWindowbutom.dismiss();
            }
        }
    };

    public View.OnClickListener guibinRepeattListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            ArrayList<Mail> mails = (ArrayList<Mail>) v.getTag();
            mMailActivity.waitDialog.show();
            managerMails(2,mails);
            if(popupWindowbutom != null)
            {
                popupWindowbutom.dismiss();
            }
        }
    };

    public View.OnClickListener guibinReadListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            ArrayList<Mail> mails = (ArrayList<Mail>) v.getTag();
            mMailActivity.waitDialog.show();
            managerMails(3,mails);
            if(popupWindowbutom != null)
            {
                popupWindowbutom.dismiss();
            }
        }
    };

    public View.OnClickListener setreadListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            ArrayList<Mail> mails = (ArrayList<Mail>) v.getTag();
            mMailActivity.waitDialog.show();
            MailAsks.setRead(mMailActivity,mMailHandler,mails);
            if(popupWindowbutom != null)
            {
                popupWindowbutom.dismiss();
            }
        }
    };

    public View.OnClickListener setunreadListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            ArrayList<Mail> mails = (ArrayList<Mail>) v.getTag();
            mMailActivity.waitDialog.show();
            MailAsks.setunRead(mMailActivity,mMailHandler,mails);
            if(popupWindowbutom != null)
            {
                popupWindowbutom.dismiss();
            }
        }
    };


    public View.OnClickListener setnorepeatListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            ArrayList<Mail> mails = (ArrayList<Mail>) v.getTag();
            mMailActivity.waitDialog.show();
            MailAsks.setnoRepeat(mMailActivity,mMailHandler,mails);
            if(popupWindowbutom != null)
            {
                popupWindowbutom.dismiss();
            }
        }
    };

    public View.OnClickListener setunnorepeatListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            ArrayList<Mail> mails = (ArrayList<Mail>) v.getTag();
            mMailActivity.waitDialog.show();
            MailAsks.setunnoRepeat(mMailActivity,mMailHandler,mails);
            if(popupWindowbutom != null)
            {
                popupWindowbutom.dismiss();
            }
        }
    };

    public View.OnClickListener setreadallListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            mMailActivity.waitDialog.show();
            MailAsks.setReadAll(mMailActivity,mMailHandler,mailboxobj);
            if(popupWindowbutom != null)
            {
                popupWindowbutom.dismiss();
            }
        }
    };

    public void managerMails(int mtyp,ArrayList<Mail> mails) {
        if (mailboxobj.state == -1) {

            if (mMailActivity.mViewPager.getCurrentItem() == 0) {
                MailAsks.managerMails(mMailActivity, mMailHandler, mMailActivity.mViewPager.getCurrentItem(),1, 40,mailboxobj,mtyp,mails);
            } else if (mMailActivity.mViewPager.getCurrentItem() == 1) {
                MailAsks.managerMails(mMailActivity, mMailHandler,  mMailActivity.mViewPager.getCurrentItem(), 1, 40,mailboxobj,mtyp,mails);
            } else if (mMailActivity.mViewPager.getCurrentItem() == 2) {
                MailAsks.managerMails(mMailActivity, mMailHandler, mMailActivity.mViewPager.getCurrentItem(), 1, 40,mailboxobj,mtyp,mails);
            } else if (mMailActivity.mViewPager.getCurrentItem() == 3) {
                MailAsks.managerMails(mMailActivity, mMailHandler,mMailActivity.mViewPager.getCurrentItem(),  1, 40,mailboxobj,mtyp,mails);
            }
        } else {
            mMailActivity.waitDialog.show();
            MailAsks.managerMails(mMailActivity, mMailHandler,0,  1, 40,mailboxobj,mtyp,mails);
        }
    }

    public void fengfaMail() {
        ArrayList<Mail> mails = new ArrayList<Mail>();
        for (int i = 0; i < mMailActivity.mails.size(); i++) {
            Mail mail = mMailActivity.mails.get(i);
            if (mail.isSelect == true) {
                mails.add(mail);
            }
        }
        if(mails.size() > 1)
        {
            AppUtils.showMessage(mMailActivity,mMailActivity.getString(R.string.mail_fengfa_only_one));
        }
        else{
            if(MailManager.getInstance().account.iscloud)
            {
                Intent intent = new Intent(mMailActivity, MailContactsActivity.class);
                intent.putExtra("mail",mails.get(0));
                mMailActivity.startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(mMailActivity, MailContactsActivity.class);
                intent.putExtra("maildata",mails.get(0));
                mMailActivity.startActivity(intent);
            }

        }


    }

    public void moveMail() {
        ArrayList<Mail> mails = new ArrayList<Mail>();
        for (int i = 0; i < mMailActivity.mails.size(); i++) {
            Mail mail = mMailActivity.mails.get(i);
            if (mail.isSelect == true) {
                mails.add(mail);
            }
        }
        Intent intent = new Intent(mMailActivity, MailFileActivity.class);
        intent.putExtra("mails",mails);
        if(mailboxobj != null)
        intent.putExtra("type",mailboxobj.type);
        else
            intent.putExtra("type",0);
        intent.putExtra("mailboxid",mailboxobj.mailboxid);
        mMailActivity.startActivity(intent);
    }

    public void access() {
        ArrayList<Mail> mails = new ArrayList<Mail>();
        for (int i = 0; i < mMailActivity.mails.size(); i++) {
            Mail mail = mMailActivity.mails.get(i);
            if (mail.isSelect == true) {
                mails.add(mail);
            }
        }
        mMailActivity.waitDialog.show();
        MailAsks.setApprove(mMailActivity,mMailHandler,mails);
    }


    public void vote() {
        ArrayList<Mail> mails = new ArrayList<Mail>();
        for (int i = 0; i < mMailActivity.mails.size(); i++) {
            Mail mail = mMailActivity.mails.get(i);
            if (mail.isSelect == true) {
                mails.add(mail);
            }
        }
        mMailActivity.waitDialog.show();
        MailAsks.setVote(mMailActivity,mMailHandler,mails);
    }

    public void selectAll() {
        for (int i = 0; i < mMailActivity.mails.size(); i++) {
            mMailActivity.mails.get(i).isSelect = true;
        }
        mMailActivity.mMailItemAdapter.notifyDataSetChanged();
    }

    public void slectNone() {
        for (int i = 0; i < mMailActivity.mails.size(); i++) {
            mMailActivity.mails.get(i).isSelect = false;
        }
        mMailActivity.mMailItemAdapter.notifyDataSetChanged();
    }

//    public void cancle() {
//        mMailActivity.mDelete.setVisibility(View.INVISIBLE);
//        slectNone();
//    }

    public void showLeft() {

        mMailActivity.mViewPager.setCurrentItem(0);
        mMailActivity.mRightImg.setTextColor(Color.parseColor("#8F9093"));
        mMailActivity.mMiddleImg.setTextColor(Color.parseColor("#8F9093"));
        mMailActivity.mRightImg2.setTextColor(Color.parseColor("#8F9093"));
        mMailActivity.mLefttImg.setTextColor(Color.parseColor("#FFFFFF"));
        mMailActivity.mRightTeb2.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mMailActivity.mRightTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mMailActivity.mMiddeleTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mMailActivity.mLefttTeb.setBackgroundResource(R.drawable.shape_bg_readbg);
        mMailActivity.mPullToRefreshView1.onFooterRefreshComplete();
    }

    public void showMiddle() {

        mMailActivity.mViewPager.setCurrentItem(1);
        mMailActivity.mRightImg.setTextColor(Color.parseColor("#8F9093"));
        mMailActivity.mRightImg2.setTextColor(Color.parseColor("#8F9093"));
        mMailActivity.mMiddleImg.setTextColor(Color.parseColor("#FFFFFF"));
        mMailActivity.mLefttImg.setTextColor(Color.parseColor("#8F9093"));
        mMailActivity.mRightTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mMailActivity.mRightTeb2.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mMailActivity.mMiddeleTeb.setBackgroundResource(R.drawable.shape_bg_readbg);
        mMailActivity.mLefttTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mMailActivity.mPullToRefreshView2.onFooterRefreshComplete();

    }

    public void showRight() {

        mMailActivity.mViewPager.setCurrentItem(2);
        mMailActivity.mRightImg.setTextColor(Color.parseColor("#FFFFFF"));
        mMailActivity.mRightImg2.setTextColor(Color.parseColor("#8F9093"));
        mMailActivity.mMiddleImg.setTextColor(Color.parseColor("#8F9093"));
        mMailActivity.mLefttImg.setTextColor(Color.parseColor("#8F9093"));
        mMailActivity.mRightTeb.setBackgroundResource(R.drawable.shape_bg_readbg);
        mMailActivity.mRightTeb2.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mMailActivity.mMiddeleTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mMailActivity.mLefttTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mMailActivity.mPullToRefreshView3.onFooterRefreshComplete();


    }

    public void showRight2() {
        mMailActivity.mViewPager.setCurrentItem(3);
        mMailActivity.mRightImg.setTextColor(Color.parseColor("#8F9093"));
        mMailActivity.mRightImg2.setTextColor(Color.parseColor("#FFFFFF"));
        mMailActivity.mMiddleImg.setTextColor(Color.parseColor("#8F9093"));
        mMailActivity.mLefttImg.setTextColor(Color.parseColor("#8F9093"));
        mMailActivity.mRightTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mMailActivity.mRightTeb2.setBackgroundResource(R.drawable.shape_bg_readbg);
        mMailActivity.mMiddeleTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mMailActivity.mLefttTeb.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mMailActivity.mPullToRefreshView4.onFooterRefreshComplete();


    }

    public void initData() {
        mMailActivity.mMailItemAdapter1 = new MailItemAdapter(mMailActivity, mMailActivity.mMailItems1);
        mMailActivity.mMailItemAdapter2 = new MailItemAdapter(mMailActivity, mMailActivity.mMailItems2);
        mMailActivity.mMailItemAdapter3 = new MailItemAdapter(mMailActivity, mMailActivity.mMailItems3);
        mMailActivity.mMailItemAdapter4 = new MailItemAdapter(mMailActivity, mMailActivity.mMailItems4);
        mMailActivity.mAllList.setAdapter(mMailActivity.mMailItemAdapter1);
        mMailActivity.mWaitList.setAdapter(mMailActivity.mMailItemAdapter2);
        mMailActivity.mAccessList.setAdapter(mMailActivity.mMailItemAdapter3);
        mMailActivity.mVoteList.setAdapter(mMailActivity.mMailItemAdapter4);
        showMiddle();
    }


    public String getTitlec(int id) {
        String title = "";
        switch (id) {
            case -1:
                title = mMailActivity.getString(R.string.keyword_mailtitleaplrove);
                break;
            case 1:
                title = mMailActivity.getString(R.string.mail_shoujian);
                break;
            case 2:
                title = mMailActivity.getString(R.string.keyword_mailtitleasend);
                break;
            case 0:
                title = mMailActivity.getString(R.string.keyword_mailtitledraft);
                break;
            case 3:
                title = mMailActivity.getString(R.string.keyword_mailtitldelete2);
                break;
            case 4:
                title = mMailActivity.getString(R.string.keyword_mailtitlercycle);
                break;
        }
        if (mMailActivity.mailboxid != null) {
            if (mMailActivity.mailname.length() > 0)
                title = mMailActivity.mailname;
        }

        return title;
    }


    public void updataListViewSet(Mailboxobj mMailBoxModel) {
        if (mMailActivity.type2 == -1 && MailManager.getInstance().account.iscloud == true) {
            mMailActivity.mPullToRefreshView.setVisibility(View.INVISIBLE);
            mMailActivity.mTab.setVisibility(View.VISIBLE);
            mMailActivity.mViewPager.setVisibility(View.VISIBLE);
        } else {
            mMailActivity.mPullToRefreshView.setVisibility(View.VISIBLE);
            mMailActivity.mTab.setVisibility(View.INVISIBLE);
            mMailActivity.mViewPager.setVisibility(View.INVISIBLE);
        }

        TextView textView = mMailActivity.findViewById(R.id.title);
        textView.setText(getTitlec(mMailActivity.type2));


        getMails(true,mMailBoxModel);
    }

    public void doReceiveMail() {
        mMailActivity.bReceive.setBackgroundResource(R.drawable.shape_bg_receive_gray);
        mMailActivity.bReceive.setEnabled(false);
        mMailActivity.iReceive.setVisibility(View.INVISIBLE);
        mMailActivity.receiveTime = MailActivity.RECEIVE_TIME;
        MailAsks.getReceive(mMailHandler, mMailActivity);
        onReceiveUpdata();
    }

    public void onReceiveUpdata() {
        mMailActivity.btnReceive.setText(String.valueOf(mMailActivity.receiveTime));
        if (mMailActivity.receiveTime > 0) {
            mMailActivity.receiveTime--;
            mMailHandler.sendEmptyMessageDelayed(MailHandler.RECEIVE_MAIL_COUNT, 1000);
        } else {
            mMailHandler.removeMessages(MailHandler.RECEIVE_MAIL_COUNT);
            onHead();
            MailManager.getInstance().getReadCount();
            mMailActivity.bReceive.setBackgroundResource(R.drawable.shape_bg_receive);
            mMailActivity.iReceive.setVisibility(View.VISIBLE);
            mMailActivity.bReceive.setEnabled(true);
            mMailActivity.btnReceive.setText("");

        }
    }

    public void updataAllsendView() {
        if(MailManager.getInstance().allSendMailBoxs.size() > 0)
        {
            mMailActivity.cloudall.setVisibility(View.VISIBLE);
        }
        else
        {
            mMailActivity.cloudall.setVisibility(View.GONE);
        }

//        mMailActivity.mMoreMailBox7.setVisibility(View.GONE);
    }

    public void updataAllLableView() {
        LayoutInflater mInflater = (LayoutInflater) mMailActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMailActivity.mMoreMailBox8.removeAllViews();
        for(int i = 0 ; i < MailManager.getInstance().mMyLabols.size() ; i++) {
            Select select = MailManager.getInstance().mMyLabols.get(i);
            View mview = mInflater.inflate(R.layout.mail_box_layer1_color, null);
            Mailboxobj item = new Mailboxobj();
            item.state = -3;
            item.type = 7;
            item.mailboxid = select.mId;
            select.view = mview;
            mview.setTag(select);
            RelativeLayout main = mview.findViewById(R.id.main);
            TextView delete = mview.findViewById(R.id.delete);
            TextView edit = mview.findViewById(R.id.edit);
            main.setTag(item);
            delete.setTag(mview);
            edit.setTag(mview);
            main.setOnClickListener(mMailActivity.mailBoxListener);
            edit.setOnClickListener(mMailActivity.editLableListener);
            delete.setOnClickListener(mMailActivity.deleteLableListener);
            TextView name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
            name.setText(select.mName);
            View image = mview.findViewById(R.id.shoujianxiangxin_img);
            image.setBackgroundColor(Color.parseColor(select.mColor));
            mMailActivity.mMoreMailBox8.addView(mview);
        }

    }

    public void updataAlFileView() {
        LayoutInflater mInflater = (LayoutInflater) mMailActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMailActivity.mMoreMailBox9.removeAllViews();
        for(int i = 0 ; i < MailManager.getInstance().mailFiles.size() ; i++) {
            MailFile select = MailManager.getInstance().mailFiles.get(i);
            View mview = mInflater.inflate(R.layout.mail_box_layer2, null);
            Mailboxobj item = new Mailboxobj();
            item.state = -4;
            item.type = 8;
            item.mailboxid = select.rid;
            select.view = mview;
            mview.setTag(select);
            RelativeLayout main = mview.findViewById(R.id.main);
            TextView delete = mview.findViewById(R.id.delete);
            TextView edit = mview.findViewById(R.id.edit);
            ImageView imageView = mview.findViewById(R.id.shoujianxiangxin_img);
            imageView.setImageResource(R.drawable.mailfile);
            main.setTag(item);
            delete.setTag(mview);
            edit.setTag(mview);
            main.setOnClickListener(mMailActivity.mailBoxListener);
            edit.setOnClickListener(mMailActivity.editFileListener);
            delete.setOnClickListener(mMailActivity.deleteFileListener);
            TextView name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
            name.setText(select.name);
            mMailActivity.mMoreMailBox9.addView(mview);
        }

    }

    public void newTab() {
        newLableView.show(mMailActivity.findViewById(R.id.activity_mail),lableOkListener);
    }

    public void newFile() {
        timeInput.title1.setText(mMailActivity.getString(R.string.mail_lable_file_creat));
        timeInput.value.setHint(mMailActivity.getString(R.string.mail_lable_file_input_hit));
        timeInput.creat(mMailActivity,mMailActivity.shade,mMailActivity.findViewById(R.id.activity_mail),"");
    }

    public void editLable(View view) {
        Select select = (Select) view.getTag();
        newLableView.show(mMailActivity.findViewById(R.id.activity_mail),lableOkListener,select);
    }

    public void deleteLable(View view) {
        final Select select = (Select) view.getTag();
        AppUtils.creatDialogTowButton(mMailActivity,mMailActivity.getString(R.string.mail_lable_lable_del_hit),"",
                mMailActivity.getString(R.string.cancle),mMailActivity.getString(R.string.button_word_ok),null,
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMailActivity.waitDialog.show();
                        MailAsks.lableDel(mMailActivity,mMailHandler,select);
                    }
                });
    }

    public void deleteFile(View view) {
        final MailFile select = (MailFile) view.getTag();
        AppUtils.creatDialogTowButton(mMailActivity,mMailActivity.getString(R.string.mail_lable_lable_del_hit),"",
                mMailActivity.getString(R.string.cancle),mMailActivity.getString(R.string.button_word_ok),null,
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMailActivity.waitDialog.show();
                        MailAsks.fileDel(mMailActivity,mMailHandler,select);
                    }
                });
    }

    public void editFile(View view) {
        MailFile select = (MailFile) view.getTag();
        timeInput.title1.setText(mMailActivity.getString(R.string.mail_lable_file_edit));
        timeInput.creat(mMailActivity,mMailActivity.shade,mMailActivity.findViewById(R.id.activity_mail),select.name,select);
    }

    private InputView.DoOkListener doFileOkListener = new InputView.DoOkListener() {
        @Override
        public void OkListener() {

        }

        @Override
        public void OkListener(Object item) {
            MailFile select = (MailFile) item;
            mMailActivity.waitDialog.show();
            MailAsks.setFile(mMailActivity,mMailHandler,select);
        }
    };

    public NewLableView.OkListener lableOkListener = new NewLableView.OkListener() {

        @Override
        public void OkListener(Select select) {
            mMailActivity.waitDialog.show();
            MailAsks.lableSet(mMailActivity,mMailHandler,select);
            newLableView.hide();
        }
    };

    public void updataLable(Select iselect) {
        Select select = iselect;
        for(int i = 0 ; i < MailManager.getInstance().mMyLabols.size() ; i++) {
            Select select1 = MailManager.getInstance().mMyLabols.get(i);
            if(select1.mId.equals(select.mId))
            {
                select1.mName = select.mName;
                select1.mColor = select.mColor;
                if(select1.view != null)
                {
                    View image = select1.view.findViewById(R.id.shoujianxiangxin_img);
                    image.setBackgroundColor(Color.parseColor(select.mColor));
                    TextView name = (TextView) select1.view.findViewById(R.id.shoujianxiang_text_xin);
                    name.setText(select.mName);
                }
                select = select1;
                return;
            }
        }
        MailManager.getInstance().mMyLabols.add(0,select);
        LayoutInflater mInflater = (LayoutInflater) mMailActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mview = mInflater.inflate(R.layout.mail_box_layer1_color, null);
        Mailboxobj item = new Mailboxobj();
        item.state = -3;
        item.type = 7;
        item.mailboxid = select.mId;
        select.view = mview;
        mview.setTag(select);
        RelativeLayout main = mview.findViewById(R.id.main);
        TextView delete = mview.findViewById(R.id.delete);
        TextView edit = mview.findViewById(R.id.edit);
        main.setTag(item);
        delete.setTag(mview);
        edit.setTag(mview);
        main.setOnClickListener(mMailActivity.mailBoxListener);
        edit.setOnClickListener(mMailActivity.editLableListener);
        delete.setOnClickListener(mMailActivity.deleteLableListener);
        TextView name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
        name.setText(select.mName);
        View image = mview.findViewById(R.id.shoujianxiangxin_img);
        image.setBackgroundColor(Color.parseColor(select.mColor));
        mMailActivity.mMoreMailBox8.addView(mview,0);
    }

    public void delLable(Select select) {
        for(int i = 0 ; i < MailManager.getInstance().mMyLabols.size() ; i++) {
            Select select1 = MailManager.getInstance().mMyLabols.get(i);
            if(select1.mId.equals(select.mId))
            {
                MailManager.getInstance().mMyLabols.remove(i);
                if(select1.view != null)
                mMailActivity.mMoreMailBox8.removeView(select1.view);
                return;
            }
        }
    }

    public void updataFile(MailFile iselect) {
        MailFile select = iselect;
        for(int i = 0 ; i < MailManager.getInstance().mailFiles.size() ; i++)
        {
            MailFile select1 = MailManager.getInstance().mailFiles.get(i);
            if(select1.rid.equals(select.rid))
            {
                select1.name = select.name;
                if(select1.view != null)
                {
                    TextView name = (TextView) select1.view.findViewById(R.id.shoujianxiang_text_xin);
                    name.setText(select.name);
                }
                select = select1;
                return;
            }
        }
        MailManager.getInstance().mailFiles.add(0,select);
        LayoutInflater mInflater = (LayoutInflater) mMailActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mview = mInflater.inflate(R.layout.mail_box_layer2, null);
        Mailboxobj item = new Mailboxobj();
        item.state = -4;
        item.type = 8;
        item.mailboxid = select.rid;
        select.view = mview;
        mview.setTag(select);
        RelativeLayout main = mview.findViewById(R.id.main);
        TextView delete = mview.findViewById(R.id.delete);
        TextView edit = mview.findViewById(R.id.edit);
        ImageView imageView = mview.findViewById(R.id.shoujianxiangxin_img);
        imageView.setImageResource(R.drawable.mailfile);
        main.setTag(item);
        delete.setTag(mview);
        edit.setTag(mview);
        main.setOnClickListener(mMailActivity.mailBoxListener);
        edit.setOnClickListener(mMailActivity.editFileListener);
        delete.setOnClickListener(mMailActivity.deleteFileListener);
        TextView name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
        name.setText(select.name);
        mMailActivity.mMoreMailBox9.addView(mview,0);
    }

    public void delFile(MailFile select) {
        for(int i = 0 ; i < MailManager.getInstance().mailFiles.size() ; i++)
        {
            MailFile select1 = MailManager.getInstance().mailFiles.get(i);
            if(select1.rid.equals(select.rid))
            {
                MailManager.getInstance().mailFiles.remove(i);
                if(select1.view != null)
                    mMailActivity.mMoreMailBox9.removeView(select1.view);
                return;
            }
        }
    }

    public void showMailTypeView(MailType mailType) {
        LayoutInflater mInflater = (LayoutInflater) mMailActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mailType.show = true;
        int n = -1;
        if(mailType.leave > 0)
        {
            n = mMailActivity.mMoreMailBox10.indexOfChild(mailType.view);
        }
        for(int i = 0 ; i < mailType.mailTypes.size() ; i++) {
            MailType select = mailType.mailTypes.get(i);
            View mview = mInflater.inflate(R.layout.mail_box_layer3, null);
            Mailboxobj item = new Mailboxobj();
            item.state = -5;
            item.type = 9;
            item.object = select;
            select.view = mview;
            mview.setTag(item);
            select.view.setOnClickListener(mMailActivity.mailBoxListener);
            TextView name = (TextView) mview.findViewById(R.id.shoujianxiang_text_xin);
            name.setText(select.typename);
            ImageView image = mview.findViewById(R.id.shoujianxiangxin_img);
            if(select.group.equals("item"))
            {
                image.setVisibility(View.INVISIBLE);
            }
            if(select.show)
            {
                image.setImageResource(R.drawable.array_down);
            }
            else
            {
                image.setImageResource(R.drawable.array_right);
            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) image.getLayoutParams();
            layoutParams.leftMargin = (int) (60*mMailActivity.mBasePresenter.mScreenDefine.density+select.leave*mMailActivity.mBasePresenter.mScreenDefine.density*8);
            image.setLayoutParams(layoutParams);
            if(n == -1)
            mMailActivity.mMoreMailBox10.addView(mview);
            else
                mMailActivity.mMoreMailBox10.addView(mview,n+i+1);
        }
        if(mailType.leave == 0)
        {
            mMailActivity.cloudtypeimg.setImageResource(R.drawable.mailtype);
            mMailActivity.cloudtype.setBackgroundColor(Color.parseColor("#F5F5F5"));
            mMailActivity.mMoreMailBox10.setVisibility(View.VISIBLE);
        }
        else
        {
            View view = mailType.view;
            if(view != null) {
                ImageView image = view.findViewById(R.id.shoujianxiangxin_img);
                image.setImageResource(R.drawable.array_down);
            }
        }
    }

    public void hidMailTypeView(MailType mailType) {

        if(mailType.show == true)
        {
            mailType.show = false;
            for(int i = 0 ; i < mailType.mailTypes.size() ; i++){
                hidMailTypeView(mailType.mailTypes.get(i));
            }
        }
        if(mailType.view != null)
        {
            mMailActivity.mMoreMailBox10.removeView(mailType.view);
        }
        mailType.mailTypes.clear();

    }

    public void doHid(MailType mailType) {
        if(mailType.show == true)
        {
            mailType.show = false;
            for(int i = 0 ; i < mailType.mailTypes.size() ; i++){
                hidMailTypeView(mailType.mailTypes.get(i));
            }
        }
        mailType.mailTypes.clear();
        if(mailType.leave == 0)
        {
            mMailActivity.cloudtypeimg.setImageResource(R.drawable.mailtype);
            mMailActivity.cloudtype.setBackgroundColor(Color.parseColor("#ffffff"));
            mMailActivity.mMoreMailBox10.setVisibility(View.INVISIBLE);
        }
        else
        {
            View view = mailType.view;
            if(view != null) {
                ImageView image = view.findViewById(R.id.shoujianxiangxin_img);
                image.setImageResource(R.drawable.array_right);
            }
        }
    }

    public void cleanListView(MailType mailType) {
        for(int i = 0 ; i < mailType.mailTypes.size() ; i++)
        {
            MailType item = mailType.mailTypes.get(i);
            if(item.view != null)
            mMailActivity.mMoreMailBox10.removeView(item.view);
        }
        mailType.mailTypes.clear();
    }

    public void updataMails(Intent intent) {
        ArrayList<Mail> mails = intent.getParcelableArrayListExtra("mails");
        for(int i = 0 ; i < mMailActivity.mails.size() ; i++)
        {
            if(mails.size() == 0)
            {
                break;
            }
            for(int j = 0 ; j < mails.size() ; j++)
            {
                if(mMailActivity.mails.get(i).mRecordId.equals(mails.get(j).mRecordId))
                {
                    Mail mail1 = mMailActivity.mails.get(i);
                    Mail mail2 = mails.get(j);
                    mail1.setMail(mail2);
                    mails.remove(j);
                }
            }
        }
        for (int i = 0; i < mMailActivity.mMailItems1.size(); i++) {
            if(mails.size() == 0)
            {
                break;
            }
            for(int j = 0 ; j < mails.size() ; j++)
            {
                if(mMailActivity.mMailItems1.get(i).mRecordId.equals(mails.get(j).mRecordId))
                {
                    Mail mail1 = mMailActivity.mMailItems1.get(i);
                    Mail mail2 = mails.get(j);
                    mail1.setMail(mail2);
                    mails.remove(j);
                }
            }
        }
        for (int i = 0; i < mMailActivity.mMailItems2.size(); i++) {
            if(mails.size() == 0)
            {
                break;
            }
            for(int j = 0 ; j < mails.size() ; j++)
            {
                if(mMailActivity.mMailItems2.get(i).mRecordId.equals(mails.get(j).mRecordId))
                {
                    Mail mail1 = mMailActivity.mMailItems2.get(i);
                    Mail mail2 = mails.get(j);
                    mail1.setMail(mail2);
                    mails.remove(j);
                }
            }
        }
        for (int i = 0; i < mMailActivity.mMailItems3.size(); i++) {
            if(mails.size() == 0)
            {
                break;
            }
            for(int j = 0 ; j < mails.size() ; j++)
            {
                if(mMailActivity.mMailItems3.get(i).mRecordId.equals(mails.get(j).mRecordId))
                {
                    Mail mail1 = mMailActivity.mMailItems3.get(i);
                    Mail mail2 = mails.get(j);
                    mail1.setMail(mail2);
                    mails.remove(j);
                }
            }
        }
        for (int i = 0; i < mMailActivity.mMailItems4.size(); i++) {
            if(mails.size() == 0)
            {
                break;
            }
            for(int j = 0 ; j < mails.size() ; j++)
            {
                if(mMailActivity.mMailItems4.get(i).mRecordId.equals(mails.get(j).mRecordId))
                {
                    Mail mail1 = mMailActivity.mMailItems4.get(i);
                    Mail mail2 = mails.get(j);
                    mail1.setMail(mail2);
                    mails.remove(j);
                }
            }
        }
        mMailActivity.mMailItemAdapter.notifyDataSetChanged();
        mMailActivity.mMailItemAdapter1.notifyDataSetChanged();
        mMailActivity.mMailItemAdapter2.notifyDataSetChanged();
        mMailActivity.mMailItemAdapter3.notifyDataSetChanged();
        mMailActivity.mMailItemAdapter4.notifyDataSetChanged();

    }

    public View.OnClickListener showUserListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mMailActivity.popupWindow2 != null) {
                mMailActivity.popupWindow2.dismiss();
            }
            mMailActivity.popupWindow2 = AppUtils.creatLeftView(mMailActivity, null, mMailActivity.findViewById(R.id.activity_mail), mMailActivity.userView);
        }
    };

    public SearchViewLayout.DoTextChange doTextChange = new SearchViewLayout.DoTextChange()
    {

        @Override
        public void doTextChange(boolean visiable) {
            doContactSearch(mMailActivity.searchView.getText());
        }
    };


    public void doContactSearch(String keyword) {
        if (keyword.length() == 0) {
            if (mMailActivity.isShowSearch == true) {
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
                            tempheads.add(new MailContact(s, ""));
                            typebooleans[pos] = true;
                        }
                    } else {
                        s = "#";
                        pos = CharacterParser.typeLetter.indexOf(s);
                        if (pos != -1) {
                            if (typebooleans[pos] == false) {
                                tempheads.add(new MailContact(s, ""));
                                typebooleans[pos] = true;
                            }
                        }
                    }
                }
            }
        }
//        if (temps.size() == 0) {
//            //AppUtils.showMessage(mMailActivity, mMailActivity.getString(R.string.searchview_search_none));
//        } else {
//
//            mMailActivity.isShowSearch = true;
//        }

        mMailActivity.mSearchItems.clear();
        mMailActivity.mSearchHeadItems.clear();
        mMailActivity.mSearchItems.addAll(temps);
        mMailActivity.mSearchHeadItems.addAll(tempheads);
        mMailActivity.mSearchItems.addAll(0, mMailActivity.mSearchHeadItems);
        Collections.sort(mMailActivity.mSearchItems, new SortMailContactComparator());
        Collections.sort(mMailActivity.mSearchHeadItems, new SortMailContactComparator());
        if(keyword.length() != 0){
            if (mMailActivity.isShowSearch == false) {
                mMailActivity.isShowSearch = true;
                mMailActivity.contactList.setAdapter(mMailActivity.mSearchContactAdapter);
            }
            else
            {
                mMailActivity.mSearchContactAdapter.notifyDataSetChanged();
            }
        }
    }
}
