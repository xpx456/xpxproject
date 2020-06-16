package com.bigwiner.android.handler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.ConversationPrase;
import com.bigwiner.android.prase.DetialPrase;
import com.bigwiner.android.prase.ImPrase;
import com.bigwiner.android.prase.LoginPrase;
import com.bigwiner.android.prase.SourcePrase;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.LoginActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.io.File;
import java.util.Calendar;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideConfiguration;
import intersky.apputils.GlideUtils;
import intersky.apputils.SystemUtil;
import intersky.apputils.TimeUtils;
import intersky.budge.shortcutbadger.ShortcutBadger;
import intersky.chat.ChatUtils;
import intersky.chat.handler.LeaveMessageHandler;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.conversation.handler.SendMessageHandler;
import intersky.filetools.FileUtils;
import intersky.guide.GuideUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//04

public class MainHandler extends Handler {

    public MainActivity theActivity;
    public static final int BACK_TIME_UPDATE = 300400;
    public static final int UPDATA_MAIN_FRIENDS = 300401;
    public static final int UPDATA_MAIN_MY = 300402;
    public static final int UPDATA_CITY = 300403;
    public static final int UPDATA_AREA = 300404;
    public static final int UPDATA_TYPE = 300405;
    public static final int UPDATA_CONVERSATION = 300406;
    public static final int UPDATA_MEETING = 300407;
    public static final int ADD_MESSAGE = 300408;
    public static final int CLEAN_MESSAGE = 300409;
    public static final int READ_MESSAGE = 300410;
    public static final int OPEN_ACTIVITY = 300411;
    public static final int UPDATA_FRIENDS_CHANGE = 300412;
    public static final int SCAN_FINISH = 300415;
    public static final int MESSAGE_REMOVE = 300416;
    public static final int MESSAGE_SUCCESS = 300417;
    public static final int MESSAGE_FAIL = 300418;
    public static final int MESSAGE_UPLOAD_SUCCESS = 300419;
    public static final int MESSAGE_DEKETE = 300421;
    public static final int MESSAGE_CODE = 300422;
    public static final int UPDATA_SOURCELIST = 300420;
    public static final int LOING_OUT = 300423;
    public static final int UPDATA_CONVERSATION_NAME = 300424;
    public static final int UPDATA_CONVERSATION_READ = 300425;

    public static final int UPDATA_CONVERSATION_NOTICE = 300426;
    public static final int UPDATA_CONVERSATION_MEETING = 300427;
    public static final int UPDATA_CONVERSATION_ALL = 300428;
    public MainHandler(MainActivity mMainActivity) {
        theActivity = mMainActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case UPDATA_CONVERSATION_NOTICE:
                theActivity.noticeAdapter.notifyDataSetChanged();
                theActivity.newsAdapter.notifyDataSetChanged();
                break;
            case UPDATA_CONVERSATION_MEETING:
                theActivity.meetingAdapter.notifyDataSetChanged();
                break;
            case UPDATA_CONVERSATION_ALL:
                theActivity.waitDialog.hide();
                theActivity.allAdapter.notifyDataSetChanged();
                break;
            case ConversationAsks.NEW_NOTICE_RESULT:
                theActivity.waitDialog.hide();
                if(BigwinerApplication.mApp.conversationManager.messageback.size() > 0)
                {
                    BigwinerApplication.mApp.conversationManager.mConversations.addAll(BigwinerApplication.mApp.conversationManager.messageback);
                    BigwinerApplication.mApp.conversationManager.messageback.clear();
                }
                ConversationPrase.praseConversationNoticeAndNews(theActivity, (NetObject) msg.obj);
                if(((NetObject) msg.obj).item.equals(ConversationAsks.TYPE_NOTICE))
                {
                    BigwinerApplication.mApp.conversationManager.noticefinish = true;
                }
                else
                {
                    BigwinerApplication.mApp.conversationManager.newfinish = true;
                }
                if(BigwinerApplication.mApp.conversationManager.newfinish && BigwinerApplication.mApp.conversationManager.noticefinish && BigwinerApplication.mApp.conversationManager.meetingfinish)
                theActivity.allAdapter.notifyDataSetChanged();
                theActivity.noticeAdapter.notifyDataSetChanged();
                theActivity.newsAdapter.notifyDataSetChanged();
                break;
            case ConversationAsks.MEETING_RESULT:
                theActivity.waitDialog.hide();
                if(BigwinerApplication.mApp.conversationManager.messageback.size() > 0)
                {
                    BigwinerApplication.mApp.conversationManager.mConversations.addAll(BigwinerApplication.mApp.conversationManager.messageback);
                    BigwinerApplication.mApp.conversationManager.messageback.clear();
                }
                ConversationPrase.praseConversationMeeting(theActivity, (NetObject) msg.obj);
                theActivity.meetingAdapter.notifyDataSetChanged();
                BigwinerApplication.mApp.conversationManager.meetingfinish = true;
                if(BigwinerApplication.mApp.conversationManager.newfinish && BigwinerApplication.mApp.conversationManager.noticefinish && BigwinerApplication.mApp.conversationManager.meetingfinish)
                    theActivity.allAdapter.notifyDataSetChanged();
                break;
            case ConversationAsks.MESSAGE_RESULT:
                theActivity.waitDialog.hide();
                theActivity.mMainPresenter.updataConversationHit();
                if(BigwinerApplication.mApp.conversationManager.newfinish && BigwinerApplication.mApp.conversationManager.noticefinish && BigwinerApplication.mApp.conversationManager.meetingfinish)
                theActivity.allAdapter.notifyDataSetChanged();
                theActivity.meessageAdapter.notifyDataSetChanged();
                theActivity.mConversationFragment.updataReadHit();
                break;
            case UPDATA_CONVERSATION_NAME:
                Intent intent1 = (Intent) msg.obj;
                Conversation mConversation = intent1.getParcelableExtra("msg");
                theActivity.mMainPresenter.updataMessageName(mConversation);
                break;
            case LoginAsks.TOKEN_RESULT:
                theActivity.waitDialog.hide();
                LoginPrase.praseToken(theActivity, (NetObject) msg.obj);
                break;
            case LoginAsks.USERINFO_RESULT:
                theActivity.waitDialog.hide();
                if(LoginPrase.praseUserInfo(theActivity, (NetObject) msg.obj) == 1)
                {
                    BigwinerApplication.mApp.appplyUseData();
                    if(theActivity.mMyFragment != null)
                    theActivity.mMyFragment.updataView();
                }
                if(theActivity.mMyFragment != null)
                {
                    if(theActivity.mMyFragment != null)
                    if(theActivity.mMyFragment.swipeRefreshLayout != null)
                        theActivity.mMyFragment.swipeRefreshLayout.setRefreshing(false);
                }

                break;
            case ContactsAsks.CONTACTS_LIST_RESULT:
                theActivity.waitDialog.hide();
                theActivity.isupdatacontact = false;
                boolean updata = ContactsPrase.praseContactList(theActivity, (NetObject) msg.obj);
                theActivity.contactsAdapter.notifyDataSetChanged();
                if(updata)
                {
                    theActivity.allAdapter.notifyDataSetChanged();
                    theActivity.meessageAdapter.notifyDataSetChanged();
                }
                break;
            case ContactsAsks.CONTACTS_LIST_SEARCH_RESULT:
                theActivity.waitDialog.hide();
                boolean updata2 = ContactsPrase.praseContactListSearch(theActivity, (NetObject) msg.obj);
                theActivity.contactsAdapter.notifyDataSetChanged();
                theActivity.searchAdapter.notifyDataSetChanged();
                if(updata2)
                {
                    theActivity.allAdapter.notifyDataSetChanged();
                    theActivity.meessageAdapter.notifyDataSetChanged();
                }
                break;
            case ContactsAsks.FRIEND_LIST_RESULT:
                theActivity.waitDialog.hide();
                boolean updata1 = ContactsPrase.praseFriendList(theActivity, (NetObject) msg.obj);
                Intent intent2 = new Intent(ContactsAsks.ACTION_UPDATA_CONTACTS);
                intent2.setPackage(theActivity.getPackageName());
                theActivity.sendBroadcast(intent2);
                if(updata1)
                {
                    theActivity.allAdapter.notifyDataSetChanged();
                    theActivity.meessageAdapter.notifyDataSetChanged();
                }
                break;
            case ConversationAsks.BASE_DATA_RESULT:
                NetObject netObject = (NetObject) msg.obj;
                ConversationPrase.praseBaseData(theActivity, (NetObject) msg.obj);
                break;
            case ConversationAsks.BASE_DATA_FAIL:
                ConversationAsks.getBaseData(theActivity,theActivity.mMainPresenter.mMainHandler,"all");
                break;
            case UPDATA_MAIN_FRIENDS:

                break;
            case LoginAsks.UPLOAD_BG_RESULT:
                if(LoginPrase.praseBg(theActivity, (NetObject) msg.obj))
                {

                    RequestOptions options1 = new RequestOptions()
                            .placeholder(R.drawable.meetingtemp);
                    File file = (File) ((NetObject)msg.obj).item;
                    BigwinerApplication.mApp.mAccount.modify = String.valueOf(System.currentTimeMillis());
                    BigwinerApplication.mApp.saveUseData(BigwinerApplication.mApp);
                    if(theActivity.mMyFragment != null)
                    {
                        if(theActivity.mMyFragment.bgImg != null)
                        {
                            Glide.with(theActivity).load(ContactsAsks.getContactBgUrl(BigwinerApplication.mApp.mAccount.mRecordId,BigwinerApplication.mApp.mAccount.modify)).apply(options1).into(new MySimpleTarget(theActivity.mMyFragment.bgImg));
                        }
                    }
                }
                theActivity.waitDialog.hide();
                break;
            case ConversationAsks.SOURCE_RESULT:
                theActivity.waitDialog.hide();
//                ConversationPrase.praseSourceData(theActivity, (NetObject) msg.obj);
                break;
//            case WebMessageActivity.GET_PIC_PATH:
//                theActivity.mToolsFragment.doupload((String) msg.obj);
//                break;
            case UPDATA_MAIN_MY:
                theActivity.mMyFragment.updataView();
                break;
            case UPDATA_CITY:
                theActivity.mContactsFragment.setCity((Intent) msg.obj);
                break;
            case UPDATA_AREA:
                theActivity.mContactsFragment.setArea((Intent) msg.obj);
                break;
            case UPDATA_TYPE:
                theActivity.mContactsFragment.setType((Intent) msg.obj);
                break;
            case DetialAsks.COMPANY_LIST_RESULT:
                DetialPrase.praseCompanyList(theActivity, (NetObject) msg.obj);
                if(BigwinerApplication.mApp.mAccount.mUCid.length() > 0)
                {
                    BigwinerApplication.mApp.setMyCompany();
                }
                if(theActivity.mMyFragment != null)
                theActivity.mMyFragment.updataView();
                break;
            case UPDATA_CONVERSATION:
                theActivity.mMainPresenter.updateConversation((Intent) msg.obj);
                break;
            case UPDATA_MEETING:
                theActivity.mMainPresenter.updataMeeting((Intent) msg.obj);
                break;
            case NetUtils.NO_NET_WORK:
            case NetUtils.NO_INTERFACE:
                theActivity.waitDialog.hide();
                NetObject netObjecturl = (NetObject) msg.obj;
                //AppUtils.showMessage(theActivity,theActivity.getString(R.string.error_net_network));
                if(netObjecturl.result.contains(LoginAsks.USERINFO_PATH))
                {
                    if(theActivity.mMyFragment != null)
                    {
                        if(theActivity.mMyFragment.swipeRefreshLayout != null)
                            theActivity.mMyFragment.swipeRefreshLayout.setRefreshing(false);
                    }
                    theActivity.checkeFinish = true;
                }
                else if(netObjecturl.result.contains(ContactsAsks.CONTACTS_LIST_PATH))
                {
                    theActivity.isupdatacontact = false;
                }
                break;
            case BACK_TIME_UPDATE:
                theActivity.backflag = false;
                break;
            case ADD_MESSAGE:
                BigwinerApplication.mApp.conversationManager.addMessage((Intent) msg.obj,theActivity);
                theActivity.meessageAdapter.notifyDataSetChanged();
                theActivity.allAdapter.notifyDataSetChanged();
                break;
            case CLEAN_MESSAGE:
                BigWinerDBHelper.getInstance(theActivity).conversationMessaegClean();
                BigwinerApplication.mApp.conversationManager.cleanMessage();
                if(ChatUtils.getChatUtils().mLeaveMessageHandler != null) {
                    Message message = new Message();
                    message.what = LeaveMessageHandler.DELETE_CHAT_SOURCE_ALL;
                    ChatUtils.getChatUtils().mLeaveMessageHandler.sendMessage(message);
                }
                File file = new File(BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("/im"));
                FileUtils.delFile(file.getPath());
                BigwinerApplication.mApp.chatPagerHashMap.clear();
//                BigwinerApplication.mApp.mFileUtils.pathUtils.recursionDeleteFile(new File(BigwinerApplication.mApp.mFileUtils.pathUtils.getBasePath()));
                GlideConfiguration.mGlideConfiguration.clearImageAllCache(theActivity);
                theActivity.meessageAdapter.notifyDataSetChanged();
                theActivity.allAdapter.notifyDataSetChanged();
                theActivity.mConversationFragment.updataReadHit();
                break;
            case READ_MESSAGE:
                theActivity.mMainPresenter.setMessageRead((Intent) msg.obj);
                theActivity.mConversationFragment.updataReadHit();
                break;
            case ChatHandler.DOWNLOAD_UPDATA:
                theActivity.mMainPresenter.finishUpdata((Intent) msg.obj);
                break;
            case ChatHandler.DOWNLOAD_FINISH:
                theActivity.mMainPresenter.finishUpdata((Intent) msg.obj);
                break;
            case ChatHandler.DOWNLOAD_AFINISH:
                theActivity.mMainPresenter.doaudio((Intent) msg.obj);
                break;
            case ChatHandler.DOWNLOAD_IMGFINISH:
                theActivity.mMainPresenter.doimg((Intent) msg.obj);
                break;
            case OPEN_ACTIVITY:
                theActivity.mMainPresenter.openShareActivity((Intent) msg.obj);
                break;
            case ContactsAsks.CONTACTS_DETIAL_RESULT:
                theActivity.waitDialog.hide();
                ContactsPrase.praseContactDetial(theActivity, (NetObject) msg.obj);
                theActivity.mMainPresenter.satartChat((Contacts) ((NetObject) msg.obj).item);
                break;
            case UPDATA_FRIENDS_CHANGE:
                theActivity.waitDialog.hide();
                theActivity.mMainPresenter.updataContactState((Intent) msg.obj);
                break;
            case LoginAsks.CONFIRM_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj)) {

                }
                break;
            case ContactsAsks.CONTACTS_SCAN_RESULT:
                theActivity.waitDialog.hide();
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj)) {
                    NetObject netObject1 = (NetObject) msg.obj;
                    String url = (String) netObject1.item;
                    String id = url.replace(ContactsAsks.CONTACT_CODE_SCAN+"/","");
                    if(!BigwinerApplication.mApp.contactManager.friendHashMap.containsKey(id))
                    {
                        Contacts contacts = new Contacts();
                        contacts.mRecordid = id;
                        theActivity.mMainPresenter.addFriend(contacts);
                    }
                }
                break;
            case SCAN_FINISH:
                theActivity.waitDialog.hide();
                Intent data = (Intent) msg.obj;
                Bundle bundle = data.getExtras();
                theActivity.mMainPresenter.addFriend(bundle.getString("result"));
                break;
            case ContactsAsks.UPDATA_CONVERSATION_NAME:
                theActivity.waitDialog.hide();
                ContactsPrase.praseContactDetial(theActivity, (NetObject) msg.obj);
                NetObject netObject1 = (NetObject) msg.obj;
                Contacts contacts = (Contacts) netObject1.item;
                theActivity.detialids.remove(contacts.mRecordid);
                if(ContactsPrase.updataConversationTitle(contacts,theActivity))
                {
                    theActivity.allAdapter.notifyDataSetChanged();
                    theActivity.meessageAdapter.notifyDataSetChanged();
                }
                break;
            case MESSAGE_REMOVE:
                theActivity.mMainPresenter.removeMessage((Intent) msg.obj);
                break;
            case MESSAGE_SUCCESS:
                Intent intent = (Intent) msg.obj;
                Conversation conversation1 = intent.getParcelableExtra("msg");
                conversation1.issend = Conversation.MESSAGE_STATAUE_SUCCESS;
                BigWinerConversationManager.getInstance().updataMessage(theActivity,conversation1);
                break;
            case MESSAGE_FAIL:
                Intent intent4 = (Intent) msg.obj;
                Conversation conversation2 = intent4.getParcelableExtra("msg");
                conversation2.issend = Conversation.MESSAGE_STATAUE_FAIL;
                BigWinerConversationManager.getInstance().updataMessage(theActivity,conversation2);
                break;
            case MESSAGE_UPLOAD_SUCCESS:
                ImPrase.praseUploadFile((NetObject) msg.obj);
                Conversation conversation = (Conversation) ((NetObject)msg.obj).item;
                BigWinerConversationManager.getInstance().updataMessage(theActivity,conversation);
                ConversationAsks.sendMsg(BigwinerApplication.mApp, BigWinerConversationManager.getInstance().mSendMessageHandler, conversation, SendMessageHandler.SEND_MESSAGE_SUCCESS
                        , Calendar.getInstance().getTimeInMillis(),0);
                break;
            case MESSAGE_DEKETE:
                BigWinerConversationManager.getInstance().deleteMessage(theActivity, (Intent) msg.obj);
                theActivity.allAdapter.notifyDataSetChanged();
                theActivity.meessageAdapter.notifyDataSetChanged();
                break;
            case SourceAsks.SOURCE_LIST_ALL_RESULT:
                NetObject netObject2 = (NetObject) msg.obj;
                String type = (String) netObject2.item;
                theActivity.waitDialog.hide();
                if(type.equals("0"))
                {
                    SourcePrase.praseSourceList(theActivity, (NetObject) msg.obj,theActivity.sorceAllPage,theActivity.sourceAllData);
                    theActivity.allSourceAdapter.notifyDataSetChanged();
                    theActivity.sorceAllPageF.isfinish = true;

                }
                else if(type.equals("1"))
                {
                    SourcePrase.praseSourceList(theActivity, (NetObject) msg.obj,theActivity.sorceWantPage,theActivity.sourceWantData);
                    theActivity.wantSourceAdapter.notifyDataSetChanged();
                    theActivity.sorceWantPageF.isfinish = true;

                }
                else if(type.equals("2"))
                {
                    SourcePrase.praseSourceList(theActivity, (NetObject) msg.obj,theActivity.sorceGavePage,theActivity.sourceGaveData);
                    theActivity.gaveSourceAdapter.notifyDataSetChanged();
                    theActivity.sorceGavePageF.isfinish = true;
                }
                break;
            case SourceAsks.SOURCE_LIST_SEARCH_RESULT:
                theActivity.waitDialog.hide();
                SourcePrase.praseSourceSearch(theActivity, (NetObject) msg.obj,theActivity.sourceSearchData,theActivity.sourceDataHashMap,theActivity.sorceSearchPage);
                theActivity.searchSourceAdapter.notifyDataSetChanged();
                theActivity.sorceSearchPageF.isfinish = true;
                break;
            case SourceAsks.SOURCE_COLLECT_RESULT:
                if(ContactsPrase.praseData(theActivity, (NetObject) msg.obj))
                {
                    SourceData sourceData = (SourceData) ((NetObject) msg.obj).item;
                    if(sourceData.iscollcet == 0)
                    {
                        sourceData.collectcount = String.valueOf(Integer.valueOf(sourceData.collectcount)+1);
                        sourceData.iscollcet = 1;
                    }
                    else
                    {
                        sourceData.collectcount = String.valueOf(Integer.valueOf(sourceData.collectcount)-1);
                        sourceData.iscollcet = 0;
                    }
                }
                theActivity.searchSourceAdapter.notifyDataSetChanged();
                theActivity.allSourceAdapter.notifyDataSetChanged();
                theActivity.wantSourceAdapter.notifyDataSetChanged();
                theActivity.gaveSourceAdapter.notifyDataSetChanged();
                break;
            case UPDATA_SOURCELIST:
                theActivity.mMainPresenter.updataSourceList((Intent) msg.obj);
                break;
            case MESSAGE_CODE:
                break;
            case ConversationAsks.LINK_RESULT:
                theActivity.waitDialog.hide();
                intent =  ConversationPrase.praselink(theActivity, (NetObject) msg.obj);
                theActivity.startActivity(intent);
                break;
            case ContactsAsks.CONTACTS_DETIAL_RESULT2:
                ContactsPrase.praseContactDetial(theActivity, (NetObject) msg.obj);
                Contacts contacts1 = (Contacts) ((NetObject) msg.obj).item;
                BigwinerApplication.mApp.contactManager.addFriend(contacts1);
                BigWinerDBHelper.getInstance(theActivity).saveContacts(contacts1);
                Intent intent10 = new Intent(ContactsAsks.ACTION_UPDATA_CONTACTS);
                intent10.setPackage(theActivity.getPackageName());
                theActivity.sendBroadcast(intent10);
                break;
            case LoginAsks.CHECK_TOKEN:
                if(BigwinerApplication.mApp.mAccount.islogin)
                {
                    if(LoginPrase.checkToken(theActivity, (NetObject) msg.obj) == -1)
                    {
                        theActivity.mMainPresenter.mMainHandler.removeMessages(MainActivity.CHECK_TOKEN_EVENT);
                        BigwinerApplication.mApp.logout(theActivity.mMainPresenter.mMainHandler,theActivity);
                        NetUtils.getInstance().cleanTasks();

                    }
                }
                else
                {
                    theActivity.mMainPresenter.mMainHandler.removeMessages(MainActivity.CHECK_TOKEN_EVENT);
                    BigwinerApplication.mApp.logout(theActivity.mMainPresenter.mMainHandler,theActivity);
                    NetUtils.getInstance().cleanTasks();
                }
                theActivity.checkeFinish = true;
                break;
            case MainActivity.CHECK_TOKEN_EVENT:
                theActivity.mMainPresenter.checkToken();
                break;
            case LOING_OUT:
                theActivity.mMainPresenter.mMainHandler.removeMessages(MainActivity.CHECK_TOKEN_EVENT);
                BigwinerApplication.mApp.logout(this,theActivity);
                NetUtils.getInstance().cleanTasks();
                break;
            case UPDATA_CONVERSATION_READ:
                theActivity.mMainPresenter.setRead((Intent) msg.obj);
                break;
            case SailHandler.SAIL_SUCCESS:
                if(BigwinerApplication.mApp.mAccount.issail)
                {
                    if(theActivity.mSailFragment != null)
                    theActivity.mSailFragment.btnApply.setVisibility(View.INVISIBLE);
                }
                break;
            case NetUtils.TOKEN_ERROR:
                if(BigwinerApplication.mApp.mAccount.islogin == true) {
                    BigwinerApplication.mApp.logout(BigwinerApplication.mApp.mAppHandler,BigwinerApplication.mApp.appActivityManager.getCurrentActivity());
                    NetUtils.getInstance().cleanTasks();
                }
                break;
        }

    }
}
