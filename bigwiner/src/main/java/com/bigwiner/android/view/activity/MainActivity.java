package com.bigwiner.android.view.activity;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.handler.MainHandler;
import com.bigwiner.android.prase.ImPrase;
import com.bigwiner.android.presenter.MainPresenter;
import com.bigwiner.android.view.adapter.ContactsAdapter;
import com.bigwiner.android.view.adapter.ConversationAdapter;
import com.bigwiner.android.view.adapter.SourceListAdapter;
import com.bigwiner.android.view.fragment.ContactsFragment;
import com.bigwiner.android.view.fragment.ConversationFragment;
import com.bigwiner.android.view.fragment.MyFragment;
import com.bigwiner.android.view.fragment.SailFragment;
import com.bigwiner.android.view.fragment.SourceFragment;
import com.bigwiner.android.view.fragment.ToolsFragment;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import intersky.appbase.Actions;
import intersky.appbase.BaseActivity;
import intersky.appbase.FragmentTabAdapter;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.handler.SendMessageHandler;
import intersky.mywidget.CircleImageView;
import intersky.scan.ScanUtils;
import intersky.xpxnet.net.FinishItem;
import intersky.xpxnet.net.NetObject;


public class MainActivity extends BaseActivity
{

	public static final String ACTION_UPDATE_FRIENDS_LIST = "ACTION_UPDATE_FRIENDS_LIST";
    public static final String ACTION_SET_READ_CONVERSATION = "ACTION_SET_READ_CONVERSATION";
	public static final String ACTION_UPDATE_MY = "ACTION_UPDATE_MY";
	public static final String ACTION_SET_CITY = "ACTION_SET_CITY";
	public static final String ACTION_SET_TYPE = "ACTION_SET_TYPE";
	public static final String ACTION_SET_AREA = "ACTION_SET_AREA";
	public static final String ACTION_SET_CLEANMESSAGE = "ACTION_SET_CLEANMESSAGE";
	public static final String ACTION_REMOVE_MESSAGE = "ACTION_REMOVE_MESSAGE";
	public static final String ACTION_UPDATA_MESSAGE = "ACTION_UPDATA_MESSAGE";
	public static final String ACTION_LOGIN_OUT = "ACTION_LOGIN_OUT";
	public static final String ACTION_LEAVE_MESSAGE = "ACTION_LEAVE_MESSAGE";
	public static final String ACTION_LEAVE_MESSAGE_NAME = "ACTION_LEAVE_MESSAGE_NAME";

	public static final String ACTION_CONVERSATION_MEETING_UPDATA = "ACTION_CONVERSATION_MEETING_UPDATA";
	public static final String ACTION_CONVERSATION_NOTICE_UPDATA = "ACTION_CONVERSATION_NOTICE_UPDATA";
	public static final String ACTION_CONVERSATION_ALL_UPDATA = "ACTION_CONVERSATION_ALL_UPDATA";
	public static final int CONVERSATION_PAGE = 0;
	public static final int CONTACTS_PAGE = 1;
	public static final int SOURCE_PAGE = 2;
//	public static final int TOOLS_PAGE = 3;
	public static final int SAIL_PAGE = 3;
	public static final int MY_PAGE = 4;
	public static final int TAKE_PHOTO_HEAD = 0x4;
	public static final int TAKE_PHOTO_BG = 0x5;
	public static final int CHOSE_PICTURE_HEAD = 0x6;
	public static final int CHOSE_PICTURE_BG = 0x7;
	public static final int CROP_HEAD = 0x8;
	public static final int CROP_BG = 0x9;
	public static final int CHECK_TOKEN_EVENT = 9000001;
	public RelativeLayout mConversation;
	public TextView mConversationTxt;
	public TextView mConversationHit;
	public ImageView mConversationImg;
	public RelativeLayout mContacts;
	public TextView mContactsTxt;
	public ImageView mContactsImg;
	public RelativeLayout mSource;
	public TextView mSourceTxt;
	public ImageView mSourceImg;
	public RelativeLayout mTools;
	public TextView mToolsTxt;
	public ImageView mToolsImg;
	public RelativeLayout mMy;
	public TextView mMyTxt;
	public ImageView mMyImg;
	public FragmentTabAdapter tabAdapter;
	public ConversationFragment mConversationFragment;
	public ContactsFragment mContactsFragment;
	public SourceFragment mSourceFragment;
//	public ToolsFragment mToolsFragment;
	public SailFragment mSailFragment;
	public MyFragment mMyFragment;
	public List<Fragment> mFragments = new ArrayList<Fragment>();
	public MainPresenter mMainPresenter = new MainPresenter(this);
	public ConversationAdapter allAdapter;
	public ConversationAdapter meessageAdapter;
	public ConversationAdapter noticeAdapter;
	public ConversationAdapter newsAdapter;
	public ConversationAdapter meetingAdapter;
	public ContactsAdapter contactsAdapter;
	public ContactsAdapter searchAdapter;
	public int lastpage = 0;
	public RelativeLayout shade;
	public PopupWindow popupWindow;
//	public CircleImageView headImg;
//	public ImageView bgImg;
	public boolean backflag = false;

	public HashMap<String,String> detialids = new HashMap<String,String>();
	public ModuleDetial sorceAllPage = new ModuleDetial();
	public ModuleDetial sorceWantPage = new ModuleDetial();
	public ModuleDetial sorceGavePage = new ModuleDetial();
	public ModuleDetial sorceSearchPage = new ModuleDetial();
	public FinishItem sorceAllPageF = new FinishItem();
	public FinishItem sorceWantPageF = new FinishItem();
	public FinishItem sorceGavePageF = new FinishItem();
	public FinishItem sorceSearchPageF = new FinishItem();
	public ArrayList<SourceData> sourceAllData = new ArrayList<SourceData>();
	public ArrayList<SourceData> sourceWantData = new ArrayList<SourceData>();
	public ArrayList<SourceData> sourceGaveData = new ArrayList<SourceData>();
	public ArrayList<SourceData> sourceSearchData = new ArrayList<SourceData>();
	public HashMap<String,SourceData> sourceDataHashMap = new HashMap<String,SourceData>();
	public SourceListAdapter allSourceAdapter;
	public SourceListAdapter wantSourceAdapter;
	public SourceListAdapter gaveSourceAdapter;
	public SourceListAdapter searchSourceAdapter;
	public String sourceType = "0";
	public long time = System.currentTimeMillis();
	public PopupMenu popup;
	public boolean isupdatacontact = false;
	public boolean issourceDataSearch = false;
	public boolean checkeFinish = true;
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mMainPresenter.Create();
		mMainPresenter.praseIntent(getIntent());
	}
	
	@Override
	protected void onDestroy()
	{
		mMainPresenter.Destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		mMainPresenter.Start();
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	@Override
	protected void onPause()
	{
		mMainPresenter.Pause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		mMainPresenter.Resume();
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mMainPresenter.onKeyDown(keyCode, event)) {
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	public View.OnClickListener showConverstionListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mMainPresenter.setContent(MainActivity.CONVERSATION_PAGE);
		}
	};

	public View.OnClickListener showContactsListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mMainPresenter.setContent(MainActivity.CONTACTS_PAGE);
		}
	};

	public View.OnClickListener showSourceListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mMainPresenter.setContent(MainActivity.SOURCE_PAGE);
		}
	};

	public View.OnClickListener showToolsListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mMainPresenter.setContent(MainActivity.SAIL_PAGE);
		}
	};

	public View.OnClickListener showMyListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mMainPresenter.setContent(MainActivity.MY_PAGE);
		}
	};



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ScanUtils.SCAN_FINISH) {
			Bundle bundle = data.getExtras();
			mMainPresenter.addFriend(bundle.getString("result"));
		}
//		else if (requestCode == WebMessageActivity.Controller.FILE_SELECTED) {
//			// Chose a file from the file picker.
//			if (mToolsFragment.mUploadHandler != null) {
//				mToolsFragment.mUploadHandler.onResult(resultCode, data);
//			}
//		}
//		else if(requestCode == Actions.TAKE_PHOTO)
//		{
//			mToolsFragment.takePhotoResult(requestCode, resultCode, data);
//		}
//		else if(requestCode == Actions.CHOSE_PICTURE)
//		{
//			mToolsFragment.takePhotoResult(requestCode, resultCode, data);
//		}
		else {
			mMainPresenter.takePhotoResult(requestCode, resultCode, data);
		}

	}


	public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mMainPresenter.takePhoto();
		}
	};

	public View.OnClickListener mAddPicListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mMainPresenter.pickPhoto();
		}
	};



	@JavascriptInterface
	public void RegsailAction(){
		Intent intent = new Intent(this,SailActivity.class);
		startActivity(intent);
	}

	@JavascriptInterface
	public void openImage(String img) {
		if(!img.contains("earth.png")) {
			AppUtils.showMessage(mMainPresenter.mMainActivity,"该功能开发中,尽情期待");
		}
	}

	@Override
	public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		return false;
	}

	public SendMessageHandler.UploadFile uploadFile = new SendMessageHandler.UploadFile() {
		@Override
		public boolean praseFile(NetObject netObject) {

			return ImPrase.checksuccessUploadFile(netObject);
		}

		@Override
		public void sendmessage(NetObject netObject) {
			Message message = new Message();
			message.obj = netObject;
			message.what = MainHandler.MESSAGE_UPLOAD_SUCCESS;
			mMainPresenter.mMainHandler.sendMessage(message);

		}

		@Override
		public void praseFilesize(Context context, NetObject netObject) {
			Conversation conversation = (Conversation) netObject.item;
			conversation.sourceSize = Long.valueOf(netObject.result);
			BigWinerConversationManager.getInstance().updataMessage(mMainPresenter.mMainActivity,conversation);

		}
	};

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mMainPresenter.praseIntent(intent);

	}



}
