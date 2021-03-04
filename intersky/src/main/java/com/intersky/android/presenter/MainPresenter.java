package com.intersky.android.presenter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.intersky.R;
import com.intersky.android.asks.LoginAsks;
import com.intersky.android.entity.IntersakyData;
import com.intersky.android.handler.MainHandler;
import com.intersky.android.receiver.MainReceiver;
import com.intersky.android.view.InterskyApplication;
import com.intersky.android.view.activity.AboutActivity;
import com.intersky.android.view.activity.MainActivity;
import com.intersky.android.view.activity.ServiceListActivity;

import intersky.appbase.PermissionCode;
import intersky.appbase.entity.Register;
import com.intersky.android.tools.IatHelper;

import intersky.chat.RecordAudioPremissionResult;
import intersky.conversation.NotifictionManager;
import intersky.function.entity.Function;
import intersky.function.view.activity.WebMessageActivity;
import com.intersky.android.view.fragment.ContactsFragment;
import com.intersky.android.view.fragment.ConversationFragment;
import com.intersky.android.view.fragment.MyFragment;
import com.intersky.android.view.fragment.WorkFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import intersky.appbase.FragmentTabAdapter;
import intersky.appbase.Local.LocalData;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.apputils.UtilBitmap;
import intersky.apputils.UtilScreenCapture;
import intersky.chat.ChatUtils;
import intersky.chat.ContactManager;
import intersky.chat.view.adapter.ContactsAdapter;
import intersky.conversation.ConversationManager;
import intersky.conversation.view.adapter.ConversationAdapter;
import intersky.function.FunctionUtils;
import intersky.function.view.adapter.FunctionAdapter;
import intersky.mywidget.MyGridView;
import intersky.scan.ScanUtils;
import intersky.sign.SignManager;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class MainPresenter implements Presenter {

    public MainHandler mMainHandler;
    public MainActivity mMainActivity;
    public RelativeLayout mConversation;
    public RelativeLayout mWork;
    public RelativeLayout mMore;
    public RelativeLayout mContacts;
    public RelativeLayout mMy;
    public ImageView mConversationImg;
    public ImageView mImgWork;
    public ImageView mImgMore;
    public ImageView mImgContacts;
    public ImageView mImgMy;
    public TextView mTxtConversation;
    public TextView mTxtWork;
    public TextView mTxtContacts;
    public TextView mTxtMy;
    public TextView workHit;
    public TextView conversionHit;
    public ImageView mclose;
    public List<Fragment> mFragments = new ArrayList<Fragment>();
    public int nowPage;
    public int messagePage = 1;
    public int messageCount= 0;
    public ContactsAdapter mContactAdapter;
    public ContactsAdapter mSearchContactAdapter;
    public ConversationAdapter mConversationAdapter;
    public ConversationAdapter mConversationSearchAdapter;
    public ArrayList<Conversation> mSearchConversations = new ArrayList<Conversation>();
    public ArrayList<Contacts> mSearchItems = new ArrayList<Contacts>();
    public ArrayList<Contacts> mSearchHeadItems = new ArrayList<Contacts>();
    public WorkFragment mWorkFragment;
    public MainPresenter(MainActivity mMainActivity)
    {
        this.mMainActivity = mMainActivity;
        this.mMainHandler = new MainHandler(mMainActivity);
        mMainActivity.setBaseReceiver(new MainReceiver(mMainHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mMainActivity.setContentView(R.layout.activity_main);
        mMainActivity.iatHelper = new IatHelper(mMainActivity,InterskyApplication.mApp.commendManager);
        mMainActivity.iatHelper.initArs();
        InterskyApplication.mApp.conversationManager.registerConversations(InterskyApplication.mApp.registers, mMainActivity.baseReceiver, mMainActivity);
        InterskyApplication.mApp.notifictionManager = NotifictionManager.init(InterskyApplication.mApp,R.mipmap.small_icon,R.mipmap.icon,
                InterskyApplication.mApp.conversationManager.channels,
                "com.intersky.android.receiver.NotifictionOpenReceiver",InterskyApplication.mApp.dataMeasure);
        InterskyApplication.mApp.downloadOper = InterskyApplication.mApp.notifictionManager.
                getOper(ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_DOCUMENT));
        InterskyApplication.mApp.updataOper = InterskyApplication.mApp.notifictionManager.
                getOper(ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_UPDATA));
        if(mMainActivity.getIntent().getBooleanExtra("auto",false) == true)
        InterskyApplication.mApp.upDataManager.checkVersin();
        InterskyApplication.mApp.conversationManager.getDataBaseData();
        SignManager.getInstance().getSignHit(mMainActivity);
        mConversation = (RelativeLayout) mMainActivity.findViewById(R.id.conversation);
        mWork = (RelativeLayout) mMainActivity.findViewById(R.id.work);
        mMore = (RelativeLayout) mMainActivity.findViewById(R.id.more);
        mContacts = (RelativeLayout) mMainActivity.findViewById(R.id.contacts);
        mMy = (RelativeLayout) mMainActivity.findViewById(R.id.my);
        mConversationImg = (ImageView) mMainActivity.findViewById(R.id.conversation_image);
        mImgWork = (ImageView) mMainActivity.findViewById(R.id.work_image);
        mImgMore = (ImageView) mMainActivity.findViewById(R.id.more_image);
        mImgContacts = (ImageView) mMainActivity.findViewById(R.id.contacts_image);
        mImgMy = (ImageView) mMainActivity.findViewById(R.id.my_image);
        mTxtConversation = (TextView) mMainActivity.findViewById(R.id.conversation_text);
        mTxtWork = (TextView) mMainActivity.findViewById(R.id.work_text);
        mTxtContacts = (TextView) mMainActivity.findViewById(R.id.contacts_text);
        mTxtMy = (TextView) mMainActivity.findViewById(R.id.my_text);
        workHit = (TextView) mMainActivity.findViewById(R.id.work_hit);
        conversionHit = (TextView) mMainActivity.findViewById(R.id.conversation_hit);
        mContactAdapter = new ContactsAdapter(InterskyApplication.mApp.contactManager.mOrganization.allClassContacts, mMainActivity);
        mConversationAdapter = new ConversationAdapter(ConversationManager.getInstance().conversationAll.showConversations, mMainActivity,true);
        mConversationSearchAdapter = new ConversationAdapter(mSearchConversations, mMainActivity,true);
        mSearchContactAdapter = new ContactsAdapter(mSearchItems, mMainActivity);
        mWorkFragment = new WorkFragment();
        mFragments.add(new ConversationFragment());
        mFragments.add(mWorkFragment);
        mFragments.add(new ContactsFragment());
        mFragments.add(new MyFragment());
        mWork.setOnClickListener(mMainActivity.showWorkListener);
        mContacts.setOnClickListener(mMainActivity.showContactsListener);
        mConversation.setOnClickListener(mMainActivity.showCoversationListener);
        mMy.setOnClickListener(mMainActivity.showMyListener);
        mMore.setOnClickListener(mMainActivity.mMoreListener);
        mMainActivity.tabAdapter = new FragmentTabAdapter(mMainActivity, mFragments, R.id.tab_content);
        setContent(MainActivity.PAGE_CONVERSATION);
        if(mMainHandler != null)
        mMainHandler.sendEmptyMessageDelayed(MainHandler.INIT_DATA, 50);
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {
//        mMainActivity.isResum = true;
//        if (nowPage == MainActivity.PAGE_CONTACTS) {
//            ContactsFragment mContactsFragment = ((ContactsFragment) mFragments.get(MainActivity.PAGE_CONTACTS));
//            if (mContactsFragment.msbar != null) {
//                String[] ss = new String[InterskyApplication.mApp.contactManager.mOrganization.allContactsHead.size()];
//                for (int i = 0; i < InterskyApplication.mApp.contactManager.mOrganization.allContactsHead.size(); i++) {
//                    ss[i] = InterskyApplication.mApp.contactManager.mOrganization.allContactsHead.get(i).getName();
//                }
//                mContactsFragment.msbar.setAddletters(ss);
//                mContactsFragment.msbar.requestLayout();
//                mContactsFragment.msbar.setVisibility(View.VISIBLE);
//
//            }
//        }
    }

    @Override
    public void Pause() {
//        mMainActivity.isResum = false;
    }

    @Override
    public void Destroy() {

    }

    public void setContent(int id) {
        nowPage = id;
        mMainActivity.tabAdapter.onCheckedChanged(id);
        switch (nowPage) {
            case MainActivity.PAGE_CONTACTS:

                mConversationImg.setImageResource(R.drawable.conversation);
                mImgWork.setImageResource(R.drawable.work);
                mImgMore.setImageResource(R.drawable.more);
                mImgContacts.setImageResource(R.drawable.contactss);
                mImgMy.setImageResource(R.drawable.my);
                mTxtConversation.setTextColor(Color.rgb(77, 77, 77));
                mTxtWork.setTextColor(Color.rgb(77, 77, 77));
                mTxtContacts.setTextColor(Color.rgb(35, 145, 230));
                mTxtMy.setTextColor(Color.rgb(77, 77, 77));
                ToolBarHelper.setTitle(mMainActivity.mActionBar, mMainActivity.getString(R.string.main_contact));
                ToolBarHelper.hidBack(mMainActivity.mActionBar);
                ToolBarHelper.hidRight(mMainActivity.mActionBar);
                ToolBarHelper.setBgColor(mMainActivity, mMainActivity.mActionBar, Color.rgb(44, 167, 234));
                break;
            case MainActivity.PAGE_CONVERSATION:
                mConversationImg.setImageResource(R.drawable.conversations);
                mImgWork.setImageResource(R.drawable.work);
                mImgMore.setImageResource(R.drawable.more);
                mImgContacts.setImageResource(R.drawable.contacts);
                mImgMy.setImageResource(R.drawable.my);
                mTxtConversation.setTextColor(Color.rgb(35, 145, 230));
                mTxtWork.setTextColor(Color.rgb(77, 77, 77));
                mTxtContacts.setTextColor(Color.rgb(77, 77, 77));
                mTxtMy.setTextColor(Color.rgb(77, 77, 77));
                ToolBarHelper.setTitle(mMainActivity.mActionBar, mMainActivity.getString(R.string.main_conversation));
                ToolBarHelper.hidBack(mMainActivity.mActionBar);
                ToolBarHelper.setRightBtn(mMainActivity.mActionBar, mMainActivity.mScanListener,R.drawable.scansafe);
                ToolBarHelper.setBgColor(mMainActivity, mMainActivity.mActionBar, Color.rgb(44, 167, 234));
                break;
            case MainActivity.PAGE_MY:
                mConversationImg.setImageResource(R.drawable.conversation);
                mImgWork.setImageResource(R.drawable.work);
                mImgMore.setImageResource(R.drawable.more);
                mImgContacts.setImageResource(R.drawable.contacts);
                mImgMy.setImageResource(R.drawable.mys);
                mTxtConversation.setTextColor(Color.rgb(77, 77, 77));
                mTxtWork.setTextColor(Color.rgb(77, 77, 77));
                mTxtContacts.setTextColor(Color.rgb(77, 77, 77));
                mTxtMy.setTextColor(Color.rgb(35, 145, 230));
                ToolBarHelper.setTitle(mMainActivity.mActionBar, mMainActivity.getString(R.string.main_my));
                ToolBarHelper.hidBack(mMainActivity.mActionBar);
                ToolBarHelper.hidRight(mMainActivity.mActionBar);
                ToolBarHelper.setBgColor(mMainActivity, mMainActivity.mActionBar, Color.rgb(44, 167, 234));
                break;
            case MainActivity.PAGE_WORK:
                mConversationImg.setImageResource(R.drawable.conversation);
                mImgWork.setImageResource(R.drawable.works);
                mImgMore.setImageResource(R.drawable.more);
                mImgContacts.setImageResource(R.drawable.contacts);
                mImgMy.setImageResource(R.drawable.my);
                mTxtConversation.setTextColor(Color.rgb(77, 77, 77));
                mTxtWork.setTextColor(Color.rgb(35, 145, 230));
                mTxtContacts.setTextColor(Color.rgb(77, 77, 77));
                mTxtMy.setTextColor(Color.rgb(77, 77, 77));
                ToolBarHelper.setTitle(mMainActivity.mActionBar, mMainActivity.getString(R.string.main_word));
                ToolBarHelper.hidBack(mMainActivity.mActionBar);
                ToolBarHelper.hidRight(mMainActivity.mActionBar);
                ToolBarHelper.setBgColor(mMainActivity, mMainActivity.mActionBar, Color.rgb(56, 71, 83));
                if(mWorkFragment.taskHit != null)
                {
                    if(FunctionUtils.getInstance().account.isouter == false)
                    {
                        Bus.callData(mMainActivity,"mail/getMailHit");
                    }
                    Bus.callData(mMainActivity,"mail/getTaskHit");
                }
                break;
        }

    }

    public void showMore() {

        mImgMore.setImageResource(R.drawable.mores);
        RotateAnimation animation = new RotateAnimation(0f, 45f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        animation.setRepeatCount(0);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                View popupWindowView = LayoutInflater.from(mMainActivity).inflate(R.layout.buttom_window8, null);
                RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
                RelativeLayout layer2 = (RelativeLayout) popupWindowView.findViewById(R.id.layer2);
                layer2.setFocusable(true);
                lsyer.setFocusable(true);
                lsyer.setFocusableInTouchMode(true);
                if (AppUtils.obtainCutoutHeight(mMainActivity) > 0) {
                    //AppUtils.showMessage(mMainActivity,"screenh1 = "+String.valueOf(mMainActivity.mBasePresenter.mScreenDefine.ScreenHeight)+"lh="+String.valueOf(AppUtils.obtainCutoutHeight(mMainActivity)));
                    mMainActivity.popupWindow = new PopupWindow(popupWindowView, mMainActivity.mBasePresenter.mScreenDefine.ScreenWidth, mMainActivity.mBasePresenter.mScreenDefine.ScreenHeight+AppUtils.obtainCutoutHeight(mMainActivity));
                } else {
                    //AppUtils.showMessage(mMainActivity,"screenh2 = "+String.valueOf(mMainActivity.mBasePresenter.mScreenDefine.ScreenHeight));
                    mMainActivity.popupWindow = new PopupWindow(popupWindowView, mMainActivity.mBasePresenter.mScreenDefine.ScreenWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
                }

                popupWindowView.setFocusable(true);
                popupWindowView.setFocusableInTouchMode(true);
                mMainActivity.popupWindow.setAnimationStyle(R.style.PopupAnimation);
                mMainActivity.popupWindow.setClippingEnabled(false);
                Bitmap bitmap = UtilScreenCapture.getDrawing(mMainActivity);
                ImageView mImageView = (ImageView) popupWindowView.findViewById(R.id.iv_popup_window_back);
                if (bitmap != null) {
                    mImageView.setImageBitmap(bitmap);
                    UtilBitmap.blurImageView(mMainActivity, mImageView, 25, 0xaaffffff);
                } else
                    mImageView.setBackgroundColor(0xaaffffff);

                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doclose();

                    }
                });

                mMainActivity.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (mMainActivity.dismissflag) {
                            mMainActivity.dismissflag = true;
                            mImgMore.setImageResource(R.drawable.more);
                            RotateAnimation animation1 = new RotateAnimation(45f, 0f, Animation.RELATIVE_TO_SELF,
                                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            animation1.setDuration(200);
                            animation1.setFillAfter(true);
                            animation1.setRepeatCount(0);
                            mImgMore.startAnimation(animation1);
                        } else {
                            mMainActivity.dismissflag = true;
                        }
                        //backgroundAlpha(1f);
                    }
                });

                lsyer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doclose();

                    }
                });
                ColorDrawable dw = new ColorDrawable(0xffffff);
                mMainActivity.popupWindow.setBackgroundDrawable(dw);

                GridView mGrid = (GridView) popupWindowView.findViewById(R.id.more_grid);

                mGrid.setAdapter(new FunctionAdapter(mMainActivity, InterskyApplication.mApp.functionUtils.moreFunctions, true));
                mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        InterskyApplication.mApp.functionUtils.startFunction(mMainActivity,(Function)parent.getAdapter().getItem(position),InterskyApplication.mApp.mAccount.iscloud);
                        doclose();
                    }
                });
                mclose = (ImageView) popupWindowView.findViewById(R.id.close);
                mclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doclose();

                    }
                });
                mGrid.setFocusable(true);
                TextView mdata = (TextView) popupWindowView.findViewById(R.id.dete1_text);
                mdata.setText(TimeUtils.getDateDay());
                mdata = (TextView) popupWindowView.findViewById(R.id.dete2_text);
                mdata.setText(TimeUtils.getDateMYex());
                mdata = (TextView) popupWindowView.findViewById(R.id.week_text);
                mdata.setText(TimeUtils.getWeek(mMainActivity));
                mMainActivity.popupWindow.showAtLocation(popupWindowView, Gravity.NO_GRAVITY, 0, 0);
//                mMainActivity.popupWindow.showAtLocation(mMainActivity.findViewById(R.id.main),
//                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImgMore.startAnimation(animation);


    }

    public void doclose() {
        mMainActivity.dismissflag = false;
        RotateAnimation animation = new RotateAnimation(0f, -45f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        animation.setRepeatCount(0);
        mclose.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mImgMore.setImageResource(R.drawable.more);
                RotateAnimation animation1 = new RotateAnimation(45f, 0f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation1.setDuration(200);
                animation1.setFillAfter(true);
                animation1.setRepeatCount(0);
                mImgMore.startAnimation(animation1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mMainActivity.popupWindow.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void initData() {
        ContactManager.mContactManager.getPhoneContacts(mMainActivity);
        mMainHandler.sendEmptyMessage(MainHandler.UPDATE_CONVERSATION);
        Bus.callData(mMainActivity,"task/getTaskHit");
//        InterskyApplication.mApp.conversationManager.getLeaveOaMessage(mMainHandler);
    }

    //work
    public void setWorkhit() {
        if(FunctionUtils.getInstance().workhit > 0)
        {
            if(workHit != null)
            workHit.setVisibility(View.VISIBLE);
        }
        else
        {
            if(workHit != null)
            workHit.setVisibility(View.INVISIBLE);
        }
    }

    public void upDateWorkView() {
        if(mFragments.size() > 0)
        {
            WorkFragment fragment = ((WorkFragment) mFragments.get(MainActivity.PAGE_WORK));
            if (fragment.mFungrids != null) {
                if (fragment.mFungrids.getChildCount() == 0) {
                    LayoutInflater mInflater = (LayoutInflater) mMainActivity
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    for (int i = 0; i < FunctionUtils.getInstance().mFunctionAdapters.size(); i++) {
                        if (FunctionUtils.getInstance().mFunctionAdapters.get(i).mFunctions.size() % 4 != 0) {
                            switch (FunctionUtils.getInstance().mFunctionAdapters.get(i).mFunctions.size() % 4) {
                                case 1:
                                    FunctionUtils.getInstance().mFunctionAdapters.get(i).mFunctions.add(new Function(Function.BLACK));
                                    FunctionUtils.getInstance().mFunctionAdapters.get(i).mFunctions.add(new Function(Function.BLACK));
                                    FunctionUtils.getInstance().mFunctionAdapters.get(i).mFunctions.add(new Function(Function.BLACK));
                                    break;
                                case 2:
                                    FunctionUtils.getInstance().mFunctionAdapters.get(i).mFunctions.add(new Function(Function.BLACK));
                                    FunctionUtils.getInstance().mFunctionAdapters.get(i).mFunctions.add(new Function(Function.BLACK));
                                    break;
                                case 3:
                                    FunctionUtils.getInstance().mFunctionAdapters.get(i).mFunctions.add(new Function(Function.BLACK));
                                    break;
                            }
                        }
                        View mview = mInflater.inflate(R.layout.workgrid, null);
                        View mview1 = mInflater.inflate(R.layout.black_layer, null);
                        MyGridView mgrid = (MyGridView) mview.findViewById(R.id.function_grid);
                        mgrid.setAdapter(FunctionUtils.getInstance().mFunctionAdapters.get(i));
                        mgrid.setOnItemClickListener(fragment.clickFunctionListener);
                        fragment.mGridViews.add(mgrid);
                        fragment.mFungrids.addView(mview);
                        fragment.mFungrids.addView(mview1);
                    }
                    View mview1 = mInflater.inflate(R.layout.workspeach, null);
                    mview1.setOnClickListener(mMainActivity.speachListener);
                    fragment.mFungrids.addView(mview1);
                } else {
                    for (int i = 0; i < FunctionUtils.getInstance().mFunctionAdapters.size(); i++) {
                        FunctionUtils.getInstance().mFunctionAdapters.get(i).notifyDataSetChanged();
                    }
                }

            }
        }
    }

    public void LetterChange(int s) {
        Contacts model = InterskyApplication.mApp.contactManager.mOrganization.allContactsHead.get(s);
        int a =InterskyApplication.mApp.contactManager.mOrganization.allContacts.indexOf(model);
        ListView list = ((ContactsFragment) mFragments.get(MainActivity.PAGE_CONTACTS)).contactList;
        list.setSelectionFromTop(a, 0);

    }

    public void updataContactView() {
        ContactsFragment mContactsFragment = ((ContactsFragment) mFragments.get(MainActivity.PAGE_CONTACTS));

        if (mContactsFragment != null) {

            if (mContactAdapter != null) {
                mContactAdapter.notifyDataSetChanged();
                mSearchContactAdapter.notifyDataSetChanged();
            }
            if (mContactsFragment.msbar != null) {
                if (mContactsFragment.isShowSearch == false) {
                    String[] ss = new String[InterskyApplication.mApp.contactManager.mOrganization.allContactsHead.size()];
                    for (int i = 0; i < InterskyApplication.mApp.contactManager.mOrganization.allContactsHead.size(); i++) {
                        ss[i] = InterskyApplication.mApp.contactManager.mOrganization.allContactsHead.get(i).getName();
                    }
                    mContactsFragment.msbar.setAddletters(ss);
                    mContactsFragment.msbar.requestLayout();
                    mContactsFragment.msbar.setVisibility(View.VISIBLE);
                } else {

                    String[] ss = new String[mSearchHeadItems.size()];
                    for (int i = 0; i < mSearchHeadItems.size(); i++) {
                        ss[i] = mSearchHeadItems.get(i).getName();
                    }
                    mContactsFragment.msbar.setAddletters(ss);
                    mContactsFragment.msbar.requestLayout();
                    mContactsFragment.msbar.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    public void contactsClick(Contacts contacts) {
        if(contacts.mType == Contacts.TYPE_PERSON)
        {
            InterskyApplication.mApp.contactManager.startContactDetial(mMainActivity,contacts);
        }
        else if(contacts.mType == Contacts.TYPE_CLASS)
        {
            InterskyApplication.mApp.contactManager.startContactList(mMainActivity,contacts);
        }
    }


    //work
    public void startFunction(Function mFunction)
    {
        FunctionUtils.getInstance().startFunction(mMainActivity,mFunction,InterskyApplication.mApp.mAccount.iscloud);
    }

    public void openConversationActivity(Intent intent) {
        String type = intent.getStringExtra("type");
        Register register = InterskyApplication.mApp.conversationManager.conversationAll.getRegister(type);
        if(register != null) {
            register.conversationFunctions.Open(intent);
        }
        if(type.equals(Conversation.CONVERSATION_TYPE_DOCUMENT))
        {
            register.conversationFunctions.Open(intent);
        }
    }

    public void showWebMessage(boolean isurl,String url) {
        Intent intent = new Intent(mMainActivity, WebMessageActivity.class);
        if(isurl == false)
        {
            intent.putExtra("isurl",false);
            intent.putExtra("data",url);
        }
        else
        {
            intent.putExtra("isurl",true);
            intent.putExtra("url",url);
        }
//        intent.putExtra("conversation", mConversationModel);
        mMainActivity.startActivity(intent);
    }

    public void selectService() {
        Intent intent = new Intent(mMainActivity, ServiceListActivity.class);
        mMainActivity.startActivity(intent);
    }

    public void showAbout() {
        Intent intent = new Intent(mMainActivity, AboutActivity.class);
        mMainActivity.startActivity(intent);
    }

    public void askClean() {
        AppUtils.creatDialogTowButton(mMainActivity, mMainActivity.getString(R.string.message_claen),"", mMainActivity.getString(R.string.button_word_cancle), mMainActivity.getString(R.string.button_word_ok),
                null,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        doClean();
                    }
                });
    }

    public void doClean() {
        File mfile = new File(InterskyApplication.mApp.mFileUtils.pathUtils.getfilePath("/mail"));
        if (mfile.exists()) {
            InterskyApplication.mApp.mFileUtils.pathUtils.recursionDeleteFile(mfile);
            mfile.delete();
        }
        mfile.mkdirs();
        mfile = new File(InterskyApplication.mApp.mFileUtils.pathUtils.getfilePath("/task"));
        if (mfile.exists()) {
            InterskyApplication.mApp.mFileUtils.pathUtils.recursionDeleteFile(mfile);
            mfile.delete();
        }
        mfile.mkdirs();
        mfile = new File(InterskyApplication.mApp.mFileUtils.pathUtils.getfilePath("/grid"));
        if (mfile.exists()) {
            InterskyApplication.mApp.mFileUtils.pathUtils.recursionDeleteFile(mfile);
            mfile.delete();
        }
        mfile.mkdirs();
        mfile = new File(InterskyApplication.mApp.mFileUtils.pathUtils.getfilePath("/download"));
        if (mfile.exists()) {
            InterskyApplication.mApp.mFileUtils.pathUtils.recursionDeleteFile(mfile);
            mfile.delete();
        }
        mfile.mkdirs();
        mfile = new File(InterskyApplication.mApp.mFileUtils.pathUtils.getfilePath("/warm"));
        if (mfile.exists()) {
            InterskyApplication.mApp.mFileUtils.pathUtils.recursionDeleteFile(mfile);
            mfile.delete();
        }
        mfile.mkdirs();
        mfile = new File(InterskyApplication.mApp.mFileUtils.pathUtils.getfilePath("/attdence"));
        if (mfile.exists()) {
            InterskyApplication.mApp.mFileUtils.pathUtils.recursionDeleteFile(mfile);
            mfile.delete();
        }
        mfile.mkdirs();
        mfile = new File(InterskyApplication.mApp.mFileUtils.pathUtils.getfilePath("/cicle"));
        if (mfile.exists()) {
            InterskyApplication.mApp.mFileUtils.pathUtils.recursionDeleteFile(mfile);
            mfile.delete();
        }
        mfile.mkdirs();
        mfile = new File(InterskyApplication.mApp.mFileUtils.pathUtils.getfilePath("/report"));
        if (mfile.exists()) {
            InterskyApplication.mApp.mFileUtils.pathUtils.recursionDeleteFile(mfile);
            mfile.delete();
        }
        mfile.mkdirs();
        mfile = new File(InterskyApplication.mApp.mFileUtils.pathUtils.getfilePath("/photo"));
        if (mfile.exists()) {
            InterskyApplication.mApp.mFileUtils.pathUtils.recursionDeleteFile(mfile);
            mfile.delete();
        }
        mfile.mkdirs();
        mfile = new File(InterskyApplication.mApp.mFileUtils.pathUtils.getfilePath("/warm") );
        if (mfile.exists()) {
            InterskyApplication.mApp.mFileUtils.pathUtils.recursionDeleteFile(mfile);
            mfile.delete();
        }
        mfile.mkdirs();

        AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.keyword_cleansuccess));
    }

    public void doLogout() {
        AppUtils.creatDialogTowButton(mMainActivity, mMainActivity.getString(R.string.message_exit),"", mMainActivity.getString(R.string.button_word_cancle), mMainActivity.getString(R.string.button_word_ok),
                null,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        logout();
                    }
                });
    }

    public void logout() {

        mMainActivity.waitDialog.show();
        SharedPreferences sharedPre = mMainActivity.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putBoolean(LocalData.LOGIN_INFO_STATU, false);
        e.commit();
        LoginAsks.doLogout(mMainHandler, mMainActivity);
        mMainHandler.sendEmptyMessage(MainHandler.EVENT_EXIST);
    }

    public void doSafeLogin() {
        mMainActivity.permissionRepuest = ScanUtils.getInstance().checkStartScan(mMainActivity, "com.intersky.android.view.activity.ScanLoginActivity");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(System.currentTimeMillis()- mMainActivity.clickTime < 2000 )
            {
                InterskyApplication.mApp.existApplacation();
                InterskyApplication.mApp.appActivityManager.AppExit(mMainActivity);
            }
            else
            {
                mMainActivity.clickTime = System.currentTimeMillis();
            }
            return true;
        }
        else {
            return false;
        }
    }

    public void getReceive() {
        String data = "{\"module_id\":\"29922\",\"company_id\":\"\",\"create_time\":1587344478,\"user_id\":\"\",\"module\":\"icloud[workflow]\",\"source_type\":\"icloud[workflow]\",\"source\":\"iCloud\",\"type\":\"admin\",\"message\":{\"module_id\":29922,\"module\":\"icloud[workflow]\",\"source_type\":\"icloud[workflow]\",\"message_id\":\"340855\",\"source_id\":\"EBEBA793-29F2-4D4D-B181-043AF51FF786\",\"title\":\"创建新的日报\",\"content\":\"已完成工作：格式化手机\"}}";
        NotifictionManager.mNotifictionManager.doCreatNotification(data,"测试消息");
    }
}
