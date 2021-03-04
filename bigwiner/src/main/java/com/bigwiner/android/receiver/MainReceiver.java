package com.bigwiner.android.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.handler.ChatHandler;
import com.bigwiner.android.handler.ContactsListHandler;
import com.bigwiner.android.handler.ConversationListHandler;
import com.bigwiner.android.handler.MainHandler;
import com.bigwiner.android.handler.SailHandler;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.SailActivity;

import intersky.appbase.BaseReceiver;
import intersky.appbase.entity.Conversation;
import intersky.chat.ChatUtils;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.NotifictionManager;
import intersky.conversation.handler.SendMessageHandler;
import intersky.scan.ScanUtils;

public class MainReceiver extends BaseReceiver {

	private Handler mHandler;

	public MainReceiver(Handler mHandler)
	{
		this.mHandler = mHandler;
		this.intentFilter = new IntentFilter();
		intentFilter.addAction(MainActivity.ACTION_UPDATE_MY);
		intentFilter.addAction(MainActivity.ACTION_UPDATE_FRIENDS_LIST);
		intentFilter.addAction(MainActivity.ACTION_SET_TYPE);
		intentFilter.addAction(MainActivity.ACTION_SET_AREA);
		intentFilter.addAction(MainActivity.ACTION_SET_CITY);
		intentFilter.addAction(DetialAsks.ACTION_MEETING_JOIN_SUCCESS);
		intentFilter.addAction(ChatUtils.ACTION_IM_FILE_DOWNLOAD_FAIL);
		intentFilter.addAction(ChatUtils.ACTION_IM_FILE_DOWNLOAD_UPDATA);
		intentFilter.addAction(ChatUtils.ACTION_IM_FILE_DOWNLOAD_FINISH);
		intentFilter.addAction(ChatUtils.ACTION_IM_AUDIO_DOWNLOAD_FINISH);
		intentFilter.addAction(ChatUtils.ACTION_IM_IMG_DOWNLOAD_FINISH);
		intentFilter.addAction(NotifictionManager.ACTION_UPDATA_CONVERSATION_CLOD);
		intentFilter.addAction(MainActivity.ACTION_SET_CLEANMESSAGE);
		intentFilter.addAction(MainActivity.ACTION_LOGIN_OUT);
		intentFilter.addAction(BigWinerConversationManager.ACTION_CONVERSATION_SET_READ);
		intentFilter.addAction(NotifictionOpenReceiver.ACTION_NOTIFICTION_OPEN_ACTIVITY);
		intentFilter.addAction(ContactsAsks.ACTION_FRIEND_CHANGE);
		intentFilter.addAction(MainActivity.ACTION_REMOVE_MESSAGE);
		intentFilter.addAction(ScanUtils.ACTION_SCAN_FINISH);
		intentFilter.addAction(BigWinerConversationManager.ACTION_SEND_CONVERSATION_MESSAGE);
		intentFilter.addAction(BigWinerConversationManager.ACTION_CODE_CONVERSATION_MESSAGE);
		intentFilter.addAction(SendMessageHandler.ACTION_SEND_MESSAGE_SUCCESS);
		intentFilter.addAction(SendMessageHandler.ACTION_SEND_MESSAGE_FAIL);
		intentFilter.addAction(SourceAsks.ACIONT_SOURCE_CREAT);
		intentFilter.addAction(SourceAsks.ACIONT_SOURCE_EDIT);
		intentFilter.addAction(SourceAsks.ACIONT_SOURCE_DELETE);
		intentFilter.addAction(MainActivity.ACTION_LEAVE_MESSAGE);
		intentFilter.addAction(MainActivity.ACTION_LEAVE_MESSAGE_NAME);
		intentFilter.addAction(MainActivity.ACTION_SET_READ_CONVERSATION);
		intentFilter.addAction(MainActivity.ACTION_UPDATA_MESSAGE);
		intentFilter.addAction(SailActivity.ACTION_SAIL_APPLY_SUCCESS);

		intentFilter.addAction(MainActivity.ACTION_CONVERSATION_ALL_UPDATA);
		intentFilter.addAction(MainActivity.ACTION_CONVERSATION_MEETING_UPDATA);
		intentFilter.addAction(MainActivity.ACTION_CONVERSATION_NOTICE_UPDATA);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(MainActivity.ACTION_UPDATE_FRIENDS_LIST)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_MAIN_FRIENDS;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MainActivity.ACTION_UPDATE_MY)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_MAIN_MY;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MainActivity.ACTION_SET_TYPE)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_TYPE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MainActivity.ACTION_SET_CITY)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_CITY;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MainActivity.ACTION_SET_AREA)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_AREA;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(NotifictionManager.ACTION_UPDATA_CONVERSATION_CLOD)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_CONVERSATION;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(DetialAsks.ACTION_MEETING_JOIN_SUCCESS)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_MEETING;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(BigWinerConversationManager.ACTION_SEND_CONVERSATION_MESSAGE)) {

			Message msg = new Message();
			msg.what = MainHandler.ADD_MESSAGE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if (intent.getAction().equals(MainActivity.ACTION_SET_CLEANMESSAGE)) {

			Message msg = new Message();
			msg.what = MainHandler.CLEAN_MESSAGE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		else if(intent.getAction().equals(ChatUtils.ACTION_IM_FILE_DOWNLOAD_FAIL))
		{
			Message smsg = new Message();
			smsg.what = ChatHandler.DOWNLOAD_FIAL;
			smsg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(ChatUtils.ACTION_IM_FILE_DOWNLOAD_UPDATA))
		{
			Message smsg = new Message();
			smsg.what = ChatHandler.DOWNLOAD_UPDATA;
			smsg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(ChatUtils.ACTION_IM_FILE_DOWNLOAD_FINISH))
		{
			Message smsg = new Message();
			smsg.what = ChatHandler.DOWNLOAD_FINISH;
			smsg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(ChatUtils.ACTION_IM_AUDIO_DOWNLOAD_FINISH))
		{
			Message smsg = new Message();
			smsg.what = ChatHandler.DOWNLOAD_AFINISH;
			smsg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(ChatUtils.ACTION_IM_IMG_DOWNLOAD_FINISH))
		{
			Message smsg = new Message();
			smsg.what = ChatHandler.DOWNLOAD_IMGFINISH;
			smsg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(BigWinerConversationManager.ACTION_CONVERSATION_SET_READ))
		{
			Message smsg = new Message();
			smsg.what = MainHandler.READ_MESSAGE;
			smsg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(smsg);
		}
		else if(intent.getAction().equals(NotifictionOpenReceiver.ACTION_NOTIFICTION_OPEN_ACTIVITY))
		{
			Message smsg = new Message();
			smsg.what = MainHandler.OPEN_ACTIVITY;
			smsg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(smsg);
		}
		if (intent.getAction().equals(ContactsAsks.ACTION_FRIEND_CHANGE)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_FRIENDS_CHANGE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		if (intent.getAction().equals(ScanUtils.ACTION_SCAN_FINISH)) {

			Message msg = new Message();
			msg.what = MainHandler.SCAN_FINISH;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		if (intent.getAction().equals(MainActivity.ACTION_REMOVE_MESSAGE)) {

			Message msg = new Message();
			msg.what = MainHandler.MESSAGE_REMOVE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		if (intent.getAction().equals(SendMessageHandler.ACTION_SEND_MESSAGE_SUCCESS)) {

			Message msg = new Message();
			msg.what = MainHandler.MESSAGE_SUCCESS;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		if (intent.getAction().equals(SendMessageHandler.ACTION_SEND_MESSAGE_FAIL)) {

			Message msg = new Message();
			msg.what = MainHandler.MESSAGE_FAIL;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		if (intent.getAction().equals(BigWinerConversationManager.ACTION_DODELETE_CONVERSATION_MESSAGE)) {

			Message msg = new Message();
			msg.what = MainHandler.MESSAGE_DEKETE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		if (intent.getAction().equals(SourceAsks.ACIONT_SOURCE_CREAT)
		||intent.getAction().equals(SourceAsks.ACIONT_SOURCE_DELETE)
				||intent.getAction().equals(SourceAsks.ACIONT_SOURCE_EDIT)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_SOURCELIST;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		if (intent.getAction().equals(BigWinerConversationManager.ACTION_CODE_CONVERSATION_MESSAGE)) {

			Message msg = new Message();
			msg.what = MainHandler.MESSAGE_CODE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		if (intent.getAction().equals(MainActivity.ACTION_LOGIN_OUT)) {

			Message msg = new Message();
			msg.what = MainHandler.LOING_OUT;
			msg.obj = intent;
			if(mHandler!=null)
			{
				mHandler.removeMessages(MainHandler.LOING_OUT);
				mHandler.sendMessage(msg);
			}

		}
		if (intent.getAction().equals(MainActivity.ACTION_LEAVE_MESSAGE)) {

			Message msg = new Message();
			msg.what = ConversationAsks.MESSAGE_RESULT;
			msg.obj = intent;
			if(mHandler!=null)
			{
				mHandler.sendMessage(msg);
			}

		}
		if (intent.getAction().equals(MainActivity.ACTION_LEAVE_MESSAGE_NAME)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_CONVERSATION_NAME;
			msg.obj = intent;
			if(mHandler!=null)
			{
				mHandler.sendMessage(msg);
			}

		}
		if (intent.getAction().equals(MainActivity.ACTION_SET_READ_CONVERSATION)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_CONVERSATION_READ;
			msg.obj = intent;
			if(mHandler!=null)
			{
				mHandler.sendMessage(msg);
			}

		}
		if (intent.getAction().equals(MainActivity.ACTION_UPDATA_MESSAGE)) {

			Message msg = new Message();
			msg.what = ConversationAsks.MESSAGE_RESULT;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		if (intent.getAction().equals(SailActivity.ACTION_SAIL_APPLY_SUCCESS)) {

			Message msg = new Message();
			msg.what = SailHandler.SAIL_SUCCESS;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		if (intent.getAction().equals(MainActivity.ACTION_CONVERSATION_ALL_UPDATA)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_CONVERSATION_ALL;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		if (intent.getAction().equals(MainActivity.ACTION_CONVERSATION_MEETING_UPDATA)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_CONVERSATION_MEETING;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
		if (intent.getAction().equals(MainActivity.ACTION_CONVERSATION_NOTICE_UPDATA)) {

			Message msg = new Message();
			msg.what = MainHandler.UPDATA_CONVERSATION_NOTICE;
			msg.obj = intent;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

}
