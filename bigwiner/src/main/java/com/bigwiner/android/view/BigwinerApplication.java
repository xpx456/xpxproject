package com.bigwiner.android.view;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.FeedbackErrorCallback;
import com.alibaba.sdk.android.httpdns.HttpDns;
import com.alibaba.sdk.android.httpdns.HttpDnsService;
import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.GcmRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.alibaba.sdk.android.push.register.OppoRegister;
import com.alibaba.sdk.android.push.register.VivoRegister;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.bigwiner.R;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.asks.LoginAsks;
import com.bigwiner.android.entity.Company;
import com.bigwiner.android.entity.JoinData;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.entity.UserDefine;
import com.bigwiner.android.handler.AppHandler;
import com.bigwiner.android.handler.ContactsHeadHandler;
import com.bigwiner.android.prase.ContactsPrase;
import com.bigwiner.android.prase.ConversationPrase;
import com.bigwiner.android.view.activity.BigwinerScanActivity;
import com.bigwiner.android.view.activity.ContactsListActivity;
import com.bigwiner.android.view.activity.LoginActivity;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.SelectActivity;
import com.bigwiner.android.view.activity.SettingActivity;
import com.bumptech.glide.Glide;
import com.fm.openinstall.OpenInstall;
import com.interksy.autoupdate.UpDataManager;
import com.interksy.autoupdate.UpdataDownloadThread;
import com.interksy.autoupdate.UpdateHandler;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.debug.I;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.ut.mini.internal.UTTeamWork;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;


import intersky.appbase.AppActivityManager;
import intersky.appbase.GetProvideGetPath;
import intersky.appbase.bundle.BaseLibsInit;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.ShareItem;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideConfiguration;
import intersky.apputils.Onlocation;
import intersky.apputils.SystemUtil;
import intersky.apputils.TimeUtils;
import intersky.chat.ChatUtils;
import intersky.chat.ContactManager;
import intersky.chat.entity.ChatPager;
import intersky.chat.handler.LeaveMessageHandler;
import intersky.conversation.NotifictionManager;
import intersky.conversation.BigWinerConversationManager;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.conversation.entity.Channel;
import intersky.filetools.FileUtils;;
import intersky.guide.GuideUtils;
import intersky.guide.entity.GuidePic;
import intersky.json.XpxJSONObject;
import intersky.scan.ScanUtils;
import intersky.select.SelectManager;
import intersky.select.entity.MapSelect;
import intersky.select.entity.Select;
import intersky.talk.FaceConversionUtil;
import intersky.talk.TalkUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.NetTask;
import xpx.map.MapManager;

import static com.alibaba.mtl.appmonitor.AppMonitorDelegate.TAG;
import static com.tencent.bugly.Bugly.applicationContext;


/*
 * Author: pan Email:gdpancheng@gmail.com
 * Created Date:2013-7-23
 * Copyright @ 2013 BU
 * Description: 全局application
 *
 * History:
 */
public class BigwinerApplication extends Application {

    public static final int COMPANY_ICON_UPDATA_TIME = 120;
    public static final int COMPANY_BG_UPDATA_TIME = 120;
    public static final int CONTACTS_ICON_UPDATA_TIME = 120;
    public static final int CONTACTS_BG_UPDATA_TIME = 120;
//    public static final String BASE_NET_PATH = "http://192.168.100.224";
    public static final String CHECK_VERSION_URL = "http://www.intersky.com.cn/app/android.version/bigwiner.txt";
    public static final String UPDATA_APP_URL = "http://www.intersky.com.cn/app/android.version/bigwiner.apk";
    public static final String UPDATA_NAME = "bigwiner.apk";
    public static final String BASE_NET_PATH = "http://47.56.104.229:81";
    public static final String SERVICE_NET_PATH = "http://47.52.242.195";
    public static final String ACTION_LOCATION_CHANGE = "ACTION_LOCATION_CHANGE";
    public static final String UPDATA_NOTIFICATIONID = "bigwiner_updata_download";
    public static final int SHARE_PROMIESS_CODE = 12346;
    public static final boolean GUIDE_OPEN = false;
    public static final boolean istest = true;
    public static BigwinerApplication mApp;
    public static boolean hisupdata = false;
    public static boolean hisupdata2 = false;
    public static Account mAccount = new Account();
    public static Company company = new Company();
    public static String szImei = "asdf";
    public AppActivityManager appActivityManager;
    public BigWinerConversationManager conversationManager;
    public ContactManager contactManager;
    public NotifictionManager notifictionManager;
    public ChatUtils chatUtils;
    public FileUtils mFileUtils;
    public MsgDisplayListener msgDisplayListener = null;
    public String deviceId = "";
    public JoinData my = new JoinData();
    public JoinData want = new JoinData();
    public StringBuilder cacheMsg = new StringBuilder();
    public ContactsHeadHandler mContactsHeadHandler = new ContactsHeadHandler();
    public HashMap<String, MapSelect> allcity = new HashMap<String, MapSelect>();
    public MapSelect allprovience = new MapSelect();
    public MapSelect citySelect = new MapSelect();
    public MapSelect businessareaSelect = new MapSelect();
    public MapSelect businesstypeSelect = new MapSelect();
    public MapSelect sexSelect = new MapSelect();
    public MapSelect ports = new MapSelect();
    public MapSelect positions = new MapSelect();
    public MapSelect sourceSelect = new MapSelect();
    public MapSelect companyslelct = new MapSelect();
    public ArrayList<Company> companies = new ArrayList<Company>();
    public HashMap<String, Company> hashCompany = new HashMap<String, Company>();
    public Select city;
    public Select provience;
    public File bg;
    public File icon;
    public int second = 0;
    public boolean sharepromiess = false;
    public HashMap<String, String> headKey = new HashMap<String, String>();
    public ArrayList<GuidePic> guidePics = new ArrayList<GuidePic>();
    public HashMap<String, ChatPager> chatPagerHashMap = new HashMap<String, ChatPager>();
    public AppHandler mAppHandler = new AppHandler();
    public NotifictionManager.NotificationOper oper;
    public UpdataDownloadThread.UpMessage last;
    public UpDataManager mUpDataManager;
    public MapManager mMapManager;
    public void onCreate() {
        mApp = this;
        initModules();
        initApllacation();
        super.onCreate();
    }

    public void initModules() {
        CrashReport.initCrashReport(getApplicationContext(), "824a5bb9e7", false);
        appActivityManager = AppActivityManager.getAppActivityManager(mApp);
        notifictionManager = NotifictionManager.init(mApp, R.mipmap.icon, R.mipmap.icon);
        szImei = AppUtils.getAppUnicode(mApp);
        mAccount.serviceid = "bigwiner";
        Channel channel = new  Channel();
        channel.leave = NotificationManager.IMPORTANCE_LOW;
        channel.id = UPDATA_NOTIFICATIONID;
        channel.name = UPDATA_NOTIFICATIONID;
        oper = notifictionManager.registerNotification(channel);
        try {
            PackageManager packageManager = mApp.getApplicationContext().getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    mApp.getPackageName(), 0);
            updataSharedVersion(packInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/bigwiner");
        if(file.exists() == false)
        {
            file.mkdirs();
        }
        try {
            PackageManager packageManager = mApp.getApplicationContext().getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    mApp.getPackageName(), 0);
            BigwinerApplication.mApp.mUpDataManager = UpDataManager.init(BigwinerApplication.mApp,BigwinerApplication.CHECK_VERSION_URL,BigwinerApplication.UPDATA_APP_URL,
                    packInfo.versionName,packInfo.versionCode,file.getPath()+"/"+BigwinerApplication.UPDATA_NAME,BigwinerApplication.mApp.updataOperation,BigwinerApplication.mApp.getProvidePath);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        GlideConfiguration.init(file);

        guidePics.add(new GuidePic(R.drawable.splash));
        guidePics.add(new GuidePic(R.drawable.splash));
        guidePics.add(new GuidePic(R.drawable.splash));
        guidePics.add(new GuidePic(R.drawable.splash));
        NetUtils.init(mApp);
        NetUtils.getInstance().addaskManagerThread("imchat",1000,1);
        getBaseData();

        ScanUtils.init(mApp);
        BaseLibsInit.init(mApp);
        GuideUtils.init(mApp);
        contactManager = ContactManager.init(mApp);
        TalkUtils.init(mApp);
        BigWinerDBHelper.getInstance(mApp);

        UMConfigure.init(this, "5d71ee140cafb2f9ea000de4"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
        PlatformConfig.setWeixin("wxe47d3f04e094c679", "5414ba372433ae075f1527a5a95dff67");
        PlatformConfig.setQQZone("101873645", "eDSIH5IvE3jWkUim");
        PlatformConfig.setSinaWeibo("2570171723", "10d98bac68dd76710a34b9922797a8cd", "http://sns.whalecloud.com");


        initManService();
        initFeedbackService();
        initHttpDnsService();
        initHotfix();
        initPushService(this);

        initSex();
        mMapManager =  MapManager.init(functions,mApp);
        initChannel();

        BigwinerApplication.mApp.mFileUtils = FileUtils.init(BigwinerApplication.mApp, BigwinerApplication.mApp.getProvidePath,null,null);
        BigwinerApplication.mApp.chatUtils = ChatUtils.sampleinit(BigwinerApplication.mApp
                ,BigwinerApplication.mApp.sampleChatFunctions,BigwinerApplication.mApp.mMapManager
                ,BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("/im/head")+"/");
        BigwinerApplication.mApp.conversationManager = BigWinerConversationManager.init(BigwinerApplication.mApp);
    }

    public void getBaseData() {
        if(BigwinerApplication.mApp.ports.list.size() == 0 &&
                BigwinerApplication.mApp.businesstypeSelect.list.size() == 0 &&
                BigwinerApplication.mApp.businessareaSelect.list.size() == 0 &&
                BigwinerApplication.mApp.positions.list.size() == 0)
        {
            ConversationAsks.getBaseData(mApp,mAppHandler,"all");
        }

    }

    public void initApllacation() {
        BigwinerApplication.mApp.praseUseData();
        BigwinerApplication.mApp.praseUseData2();
        setModules();
        if(mAccount.islogin == true)
        {
            ConversationAsks.getNewsAndNotices(BigwinerApplication.mApp,mAppHandler,ConversationAsks.TYPE_NOTICE, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).pagesize
                    , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).currentpage);
            ConversationAsks.getNewsAndNotices(BigwinerApplication.mApp,mAppHandler,ConversationAsks.TYPE_NEWS, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).pagesize
                    , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).currentpage);
            ConversationAsks.getMettings(BigwinerApplication.mApp,mAppHandler, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).pagesize
                    , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).currentpage);
            if(ChatUtils.getChatUtils().mLeaveMessageHandler != null )
            {
                ConversationAsks.getMessageUnread(BigwinerApplication.mApp,ChatUtils.getChatUtils().mLeaveMessageHandler);
            }
        }
    }

    public void setModules() {
        BigwinerApplication.mApp.appplyUseData();
        CrashReport.setUserId(BigwinerApplication.mApp.mAccount.mRecordId+"_"+BigwinerApplication.mApp.mAccount.getName());
        BigwinerApplication.mApp.mFileUtils.pathUtils.setBase("/bigwiner" + "/" + BigwinerApplication.mApp.mAccount.mRecordId);
        BigwinerApplication.mApp.contactManager.setAccount(mAccount);
        BigwinerApplication.mApp.conversationManager.setUserid(BigwinerApplication.mAccount.mRecordId);
        BigwinerApplication.mApp.conversationManager.initData();
        BigwinerApplication.mApp.conversationManager.mConversations.addAll(BigWinerDBHelper.getInstance(BigwinerApplication.mApp).scanConversation(BigwinerApplication.mApp.mAccount.mRecordId));
        BigwinerApplication.mApp.chatUtils.setmAccount(BigwinerApplication.mAccount);
        BigwinerApplication.mApp.chatUtils.startSampleChart();

        BigwinerApplication.mApp.contactManager.cleanAll();
        BigWinerDBHelper.getInstance(BigwinerApplication.mApp).scanContacts(BigwinerApplication.mApp.contactManager.friendHashMap,
                BigwinerApplication.mApp.contactManager.mContactsfs,BigwinerApplication.mApp.contactManager.mContactsFHashHead,
                BigwinerApplication.mApp.contactManager.mContactsFHead,
                BigwinerApplication.mApp.contactManager.mContactsFall,BigwinerApplication.mApp.contactManager.typeboolfriend,
                BigwinerApplication.mApp.contactManager.friendHeadHashMap, BigwinerApplication.mApp.mAccount.mRecordId);

    }

    public void setNewModules() {
        BigwinerApplication.mApp.saveUseData(BigwinerApplication.mApp);
        CrashReport.setUserId(BigwinerApplication.mApp.mAccount.mRecordId+"_"+BigwinerApplication.mApp.mAccount.getName());
        BigwinerApplication.mApp.mFileUtils.pathUtils.setBase("/bigwiner" + "/" + BigwinerApplication.mApp.mAccount.mRecordId);
        BigwinerApplication.mApp.contactManager.setAccount(mAccount);
        BigwinerApplication.mApp.conversationManager.setUserid(BigwinerApplication.mAccount.mRecordId);
        BigwinerApplication.mApp.conversationManager.initData();
        BigwinerApplication.mApp.conversationManager.mConversations.addAll(BigWinerDBHelper.getInstance(BigwinerApplication.mApp).scanConversation(BigwinerApplication.mApp.mAccount.mRecordId));

        BigwinerApplication.mApp.chatUtils.setmAccount(BigwinerApplication.mAccount);
        BigwinerApplication.mApp.chatUtils.startSampleChart();

        BigwinerApplication.mApp.contactManager.friendPage.pagesize = 1000;
        BigwinerApplication.mApp.contactManager.friendPage.reset();
        BigwinerApplication.mApp.contactManager.friendPage.pagesize = 1000;
        BigwinerApplication.mApp.contactManager.cleanAll();
        BigWinerDBHelper.getInstance(BigwinerApplication.mApp).cleanContacts();

        ConversationAsks.getNewsAndNotices(BigwinerApplication.mApp,mAppHandler,ConversationAsks.TYPE_NOTICE, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).pagesize
                , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NOTICE).currentpage);
        ConversationAsks.getNewsAndNotices(BigwinerApplication.mApp,mAppHandler,ConversationAsks.TYPE_NEWS, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).pagesize
                , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_NEWS).currentpage);
        ConversationAsks.getMettings(BigwinerApplication.mApp,mAppHandler, BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).pagesize
                , BigWinerConversationManager.getInstance().cPage.get(Conversation.CONVERSATION_TYPE_MEETING).currentpage);
        if(ChatUtils.getChatUtils().mLeaveMessageHandler != null )
        {
            ConversationAsks.getMessageUnread(BigwinerApplication.mApp,ChatUtils.getChatUtils().mLeaveMessageHandler);
        }

    }

    public void initChannel() {
        MiPushRegister.register(mApp, "2882303761518201584", "5811820183584");
        HuaWeiRegister.register(mApp);
        OppoRegister.register(mApp, "62cb30ad05124332ad9ee5fffe85ea48", "a950712f48524ee4ba29d44c433b5ceb"); // appKey/appSecret在OPPO开发者平台获取
        VivoRegister.register(mApp);
    }

    public void updataSharedVersion(String name) {
        if(name.equals("7.1.7"))
        {
            SharedPreferences sharedPre3 = mApp.getSharedPreferences(UserDefine.LAST_USER, 0);
            SharedPreferences.Editor e3 = sharedPre3.edit();
            e3.putLong(UserDefine.USER_LOGIN_TIME,0);
            e3.putLong(UserDefine.DATA_TIME,0);
            e3.putLong(UserDefine.COMPANY_TIME,0);
            e3.commit();
            SharedPreferences sharedPre = mApp.getSharedPreferences("717", 0);
            boolean up = sharedPre.getBoolean("dataupdata", false);
            if(up == false)
            {
                SharedPreferences sharedPre2 = mApp.getSharedPreferences(UserDefine.LAST_USER, 0);
                SharedPreferences.Editor e2 = sharedPre2.edit();
                e2.putLong(UserDefine.USER_LOGIN_TIME,0);
                e2.putLong(UserDefine.DATA_TIME,0);
                e2.putLong(UserDefine.COMPANY_TIME,0);
                e2.commit();
                SharedPreferences.Editor e1 = sharedPre.edit();
                e1.putBoolean("dataupdata",true);
                e1.commit();
            }

        }
    }

    public void praseUseData2()
    {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.LAST_USER, 0);
        if (sharedPre != null) {
            mAccount.mUserName = sharedPre.getString(UserDefine.USER_NAME, "");
            SharedPreferences info = mApp.getSharedPreferences(mAccount.mUserName, 0);
            if(info != null)
            {
                SharedPreferences.Editor e2 = info.edit();
                boolean up = info.getBoolean("dataupdata717", false);
                if(up == false)
                {
                    e2.remove(UserDefine.USER_LOGIN_TIME);
                    e2.remove(UserDefine.DATA_TIME);
                    e2.remove(UserDefine.COMPANY_TIME);
                    e2.putBoolean("dataupdata717",true);
                    e2.commit();
                }
                mAccount.mPassword = info.getString(UserDefine.USER_PASSWORD, "");
            }
        }
    }


    public void praseUseData() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.LAST_USER, 0);
        if (sharedPre != null) {
            mAccount.islogin = sharedPre.getBoolean(UserDefine.USER_ISLOGIN, false);
            mAccount.mUserName = sharedPre.getString(UserDefine.USER_NAME, "");
            SharedPreferences info = mApp.getSharedPreferences(mAccount.mUserName, 0);
            if(info != null)
            {
                mAccount.cover = info.getString(UserDefine.BG_PATH, "");
                mAccount.icon = info.getString(UserDefine.HEAD_PATH, "");
                mAccount.mPassword = info.getString(UserDefine.USER_PASSWORD, "");
                mAccount.city = info.getString(UserDefine.USER_CITY, "");
                mAccount.mAddress = info.getString(UserDefine.USER_ADDRESS, "");
                mAccount.mCompanyName = info.getString(UserDefine.USER_COMPANYNAME, "");
                mAccount.mUCid = info.getString(UserDefine.USER_COMPANYID, "");
                mAccount.mRecordId = info.getString(UserDefine.USER_RECORDID, "");
                mAccount.issail = info.getBoolean(UserDefine.USER_ISSAIL, false);
                mAccount.mRealName = info.getString(UserDefine.USER_RNAME, "");
                mAccount.mPosition = info.getString(UserDefine.USER_POSITION, "");
                mAccount.confrim = info.getString(UserDefine.USER_CONFIRM, "");
                mAccount.mEmail = info.getString(UserDefine.USER_EMAIL, "");
                mAccount.mFax = info.getString(UserDefine.USER_FAX, "");
                mAccount.mCompanyId = info.getString(UserDefine.USER_CID, "");
                mAccount.des = info.getString(UserDefine.USER_DES, "");
                mAccount.mMobile = info.getString(UserDefine.USER_MOBIL, "");
                mAccount.mPhone = info.getString(UserDefine.USER_PHONE, "");
                mAccount.mSex = info.getString(UserDefine.USER_SEX, "");
                mAccount.typeBusiness = info.getString(UserDefine.USER_TYPEBUSINESS, "");
                mAccount.typeArea = info.getString(UserDefine.USER_TYPEAREA, "");
                mAccount.leavel = info.getInt(UserDefine.USER_LEAVEL, 0);
                mAccount.vip = info.getString(UserDefine.USER_VIP, "");
                mAccount.modify = info.getString(UserDefine.USER_MODIFY, String.valueOf(System.currentTimeMillis()));
                mAccount.province = info.getString(UserDefine.USER_PROVIENCE, "");
                mAccount.complaint = info.getString(UserDefine.USER_COMPLAINT, "");
                mAccount.mHCurrent = info.getInt(UserDefine.USER_HCURRENT, 0);
                NetUtils.getInstance().token = info.getString(UserDefine.USER_TOKEN, "");
                if(BigwinerApplication.mApp.mAccount.mSex.equals("男"))
                    BigwinerApplication.mApp.mAccount.sex = 0;
                else if(BigwinerApplication.mApp.mAccount.mSex.equals("女"))
                    BigwinerApplication.mApp.mAccount.sex = 1;
                else
                    BigwinerApplication.mApp.mAccount.sex = 2;
                //BigwinerApplication.mApp.mFileUtils = FileUtils.init("/bigwiner" + "/" + BigwinerApplication.mApp.mAccount.mRecordId, BigwinerApplication.mApp, BigwinerApplication.mApp.mgetProvidePath,null);
            }

        }
    }

    public void appplyUseData() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.LAST_USER, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putBoolean(UserDefine.USER_ISLOGIN,mAccount.islogin);
        e1.putString(UserDefine.USER_NAME, mAccount.mUserName);
        e1.commit();
        SharedPreferences info = mApp.getSharedPreferences(mAccount.mUserName, 0);
        SharedPreferences.Editor e = info.edit();
        e.putString(UserDefine.BG_PATH, mAccount.cover);
        e.putString(UserDefine.USER_PASSWORD, mAccount.mPassword);
        e.putString(UserDefine.HEAD_PATH, mAccount.icon);
        e.putString(UserDefine.USER_CITY, mAccount.city);
        e.putString(UserDefine.USER_ADDRESS, mAccount.mAddress);
        e.putString(UserDefine.USER_COMPANYNAME, mAccount.mCompanyName);
        e.putString(UserDefine.USER_COMPANYID, mAccount.mUCid);
        e.putBoolean(UserDefine.USER_ISSAIL, mAccount.issail);
        e.putString(UserDefine.USER_RECORDID, mAccount.mRecordId);
        e.putString(UserDefine.USER_RNAME, mAccount.mRealName);
        e.putString(UserDefine.USER_POSITION, mAccount.mPosition);
        e.putString(UserDefine.USER_CONFIRM, mAccount.confrim);
        e.putString(UserDefine.USER_EMAIL, mAccount.mEmail);
        e.putString(UserDefine.USER_FAX, mAccount.mFax);
        e.putString(UserDefine.USER_CID, mAccount.mCompanyId);
        e.putString(UserDefine.USER_DES, mAccount.des);
        e.putString(UserDefine.USER_MOBIL, mAccount.mMobile);
        e.putString(UserDefine.USER_PHONE, mAccount.mPhone);
        e.putString(UserDefine.USER_COMPLAINT, mAccount.complaint);
        e.putString(UserDefine.USER_TYPEBUSINESS, mAccount.typeBusiness);
        e.putString(UserDefine.USER_TYPEAREA, mAccount.typeArea);
        e.putInt(UserDefine.USER_LEAVEL, mAccount.leavel);
        e.putString(UserDefine.USER_VIP, mAccount.vip);
        e.putString(UserDefine.USER_PROVIENCE, mAccount.province);
        e.putInt(UserDefine.USER_HCURRENT, mAccount.mHCurrent);
        e.putString(UserDefine.USER_MODIFY, mAccount.modify);
        e.putString(UserDefine.USER_TOKEN, NetUtils.getInstance().token);
        e.apply();
    }

    public void saveUseData(Context context) {
        SharedPreferences sharedPre = mApp.getSharedPreferences(UserDefine.LAST_USER, 0);
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putBoolean(UserDefine.USER_ISLOGIN,mAccount.islogin);
        e1.putString(UserDefine.USER_NAME, mAccount.mUserName);
        e1.commit();
        SharedPreferences info = mApp.getSharedPreferences(mAccount.mUserName, 0);
        SharedPreferences.Editor e = info.edit();
        e.putString(UserDefine.BG_PATH, mAccount.cover);
        e.putString(UserDefine.USER_PASSWORD, mAccount.mPassword);
        e.putString(UserDefine.HEAD_PATH, mAccount.icon);
        e.putString(UserDefine.USER_CITY, mAccount.city);
        e.putString(UserDefine.USER_ADDRESS, mAccount.mAddress);
        e.putString(UserDefine.USER_COMPANYNAME, mAccount.mCompanyName);
        e.putString(UserDefine.USER_COMPANYID, mAccount.mUCid);
        e.putBoolean(UserDefine.USER_ISSAIL, mAccount.issail);
        e.putString(UserDefine.USER_RECORDID, mAccount.mRecordId);
        e.putString(UserDefine.USER_RNAME, mAccount.mRealName);
        e.putString(UserDefine.USER_POSITION, mAccount.mPosition);
        e.putString(UserDefine.USER_CONFIRM, mAccount.confrim);
        e.putString(UserDefine.USER_EMAIL, mAccount.mEmail);
        e.putString(UserDefine.USER_FAX, mAccount.mFax);
        e.putString(UserDefine.USER_CID, mAccount.mCompanyId);
        e.putString(UserDefine.USER_DES, mAccount.des);
        e.putString(UserDefine.USER_MOBIL, mAccount.mMobile);
        e.putString(UserDefine.USER_PHONE, mAccount.mPhone);
        e.putString(UserDefine.USER_COMPLAINT, mAccount.complaint);
        e.putString(UserDefine.USER_TYPEBUSINESS, mAccount.typeBusiness);
        e.putString(UserDefine.USER_TYPEAREA, mAccount.typeArea);
        e.putInt(UserDefine.USER_LEAVEL, mAccount.leavel);
        e.putString(UserDefine.USER_VIP, mAccount.vip);
        e.putString(UserDefine.USER_PROVIENCE, mAccount.province);
        e.putString(UserDefine.USER_MODIFY, mAccount.modify);
        e.putInt(UserDefine.USER_HCURRENT, mAccount.mHCurrent);
        e.putString(UserDefine.USER_TOKEN, NetUtils.getInstance().token);
        e.commit();
    }

    public void initSex() {
        Select select = new Select("0", mApp.getString(R.string.sex_male));
        sexSelect.list.add(select);
        sexSelect.hashMap.put(select.mId, select);
        select = new Select("1", mApp.getString(R.string.sex_female));
        sexSelect.list.add(select);
        sexSelect.hashMap.put(select.mId, select);
        select = new Select("2", mApp.getString(R.string.sex_secrate));
        sexSelect.list.add(select);
        sexSelect.hashMap.put(select.mId, select);
    }


    private void initManService() {
        /**
         * 初始化Mobile Analytics服务
         */
        // 获取MAN服务
        MANService manService = MANServiceProvider.getService();
        // 打开调试日志
        manService.getMANAnalytics().turnOnDebug();
        manService.getMANAnalytics().setAppVersion("3.0");
        // MAN初始化方法之一，通过插件接入后直接在下发json中获取appKey和appSecret初始化
        manService.getMANAnalytics().init(this, getApplicationContext());
        // MAN另一初始化方法，手动指定appKey和appSecret
        // String appKey = "******";
        // String appSecret = "******";
        // manService.getMANAnalytics().init(this, getApplicationContext(), appKey, appSecret);
        // 若需要关闭 SDK 的自动异常捕获功能可进行如下操作,详见文档5.4
        //manService.getMANAnalytics().turnOffCrashReporter();
        // 通过此接口关闭页面自动打点功能，详见文档4.2
        manService.getMANAnalytics().turnOffAutoPageTrack();
        // 设置渠道（用以标记该app的分发渠道名称），如果不关心可以不设置即不调用该接口，渠道设置将影响控制台【渠道分析】栏目的报表展现。如果文档3.3章节更能满足您渠道配置的需求，就不要调用此方法，按照3.3进行配置即可
        manService.getMANAnalytics().setChannel("某渠道");
        // 若AndroidManifest.xml 中的 android:versionName 不能满足需求，可在此指定；
        // 若既没有设置AndroidManifest.xml 中的 android:versionName，也没有调用setAppVersion，appVersion则为null
        //manService.getMANAnalytics().setAppVersion("2.0");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("debug_api_url", "https://service-usertrack.alibaba-inc.com/upload_records_from_client");
        map.put("debug_key", "aliyun_sdk_utDetection");
        map.put("debug_sampling_option", "true");
        UTTeamWork.getInstance().turnOnRealTimeDebug(map);
    }

    private void initFeedbackService() {
        /**
         * 添加自定义的error handler
         */
        FeedbackAPI.addErrorCallback(new FeedbackErrorCallback() {
            @Override
            public void onError(Context context, String errorMessage, com.alibaba.sdk.android.feedback.util.ErrorCode errorCode) {
                Toast.makeText(context, "ErrMsg is: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        FeedbackAPI.addLeaveCallback(new Callable() {
            @Override
            public Object call() throws Exception {
                Log.d("DemoApplication", "custom leave callback");
                return null;
            }
        });
        /**
         * 建议放在此处做初始化
         */
        //默认初始化
        FeedbackAPI.init(this);
        //FeedbackAPI.init(this, "DEFAULT_APPKEY", "DEFAULT_APPSECRET");
        /**
         * 在Activity的onCreate中执行的代码
         * 可以设置状态栏背景颜色和图标颜色，这里使用com.githang:status-bar-compat来实现
         */
        //FeedbackAPI.setActivityCallback(new IActivityCallback() {
        //    @Override
        //    public void onCreate(Activity activity) {
        //        StatusBarCompat.setStatusBarColor(activity,getResources().getColor(R.color.aliwx_setting_bg_nor),true);
        //    }
        //});
        /**
         * 自定义参数演示
         */
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginTime", "登录时间");
            jsonObject.put("visitPath", "登陆，关于，反馈");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FeedbackAPI.setAppExtInfo(jsonObject);
        /**
         * 以下是设置UI
         */
        //设置默认联系方式
        FeedbackAPI.setDefaultUserContactInfo("13800000000");
        //沉浸式任务栏，控制台设置为true之后此方法才能生效
        FeedbackAPI.setTranslucent(true);
        //设置返回按钮图标
        //FeedbackAPI.setBackIcon(R.drawable.ali_feedback_common_back_btn_bg);
        //设置标题栏"历史反馈"的字号，需要将控制台中此字号设置为0
        FeedbackAPI.setHistoryTextSize(20);
        //设置标题栏高度，单位为像素
        FeedbackAPI.setTitleBarHeight(100);
    }

    private void initHttpDnsService() {
        // 初始化httpdns
        //HttpDnsService httpdns = HttpDns.getService(getApplicationContext(), accountID);
        HttpDnsService httpdns = HttpDns.getService(getApplicationContext());
        //this.setPreResoveHosts();
        // 允许过期IP以实现懒加载策略
        //httpdns.setExpiredIPEnabled(true);
    }

    private void initHotfix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                //.setAesKey("0123456789123456")
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        String msg = new StringBuilder("").append("Mode:").append(mode)
                                .append(" Code:").append(code)
                                .append(" Info:").append(info)
                                .append(" HandlePatchVersion:").append(handlePatchVersion).toString();
                        if (msgDisplayListener != null) {
                            msgDisplayListener.handle(msg);
                        } else {
                            cacheMsg.append("\n").append(msg);
                        }
                    }
                }).initialize();
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initPushService(final Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "init cloudchannel success");
                //setConsoleText("init cloudchannel success");
                deviceId = pushService.getDeviceId();
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.e(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
                //setConsoleText("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    public interface MsgDisplayListener {
        void handle(String msg);
    }

    public static GetProvideGetPath getProvidePath = new GetProvideGetPath() {
        @Override
        public Uri getProvideGetPath(File file) {
            return FileProvider.getUriForFile(BigwinerApplication.mApp, "com.bigwiner.fileprovider", file);
        }
    };

//	public void startLogin(Context context,String action) {
//		Intent intent = new Intent(context, LoginActivity.class);
//		intent.putExtra("action",action);
//		context.startActivity(intent);
//	}

    public void logout(Handler mHandler, Activity context) {
        if(BigwinerApplication.mApp.mAccount.islogin == true)
        {
            BigwinerApplication.mApp.mAccount.islogin = false;
            SharedPreferences sharedPre = BigwinerApplication.mApp.getSharedPreferences(UserDefine.LAST_USER, 0);
            SharedPreferences.Editor e = sharedPre.edit();
            e.putBoolean(UserDefine.USER_ISLOGIN, BigwinerApplication.mApp.mAccount.islogin);
            e.commit();
            LoginAsks.doPushLogout(mHandler, BigwinerApplication.mApp);
            BigwinerApplication.mApp.mAccount.mUCid = "";
            LoginAsks.doLogout();
//            ChatUtils.getChatUtils().stopHeadList();
            ChatUtils.getChatUtils().stopSourceList();
            ports.clear();
            positions.clear();
            businessareaSelect.clear();
            businesstypeSelect.clear();
            citySelect.clear();
            companyslelct.clear();
            companies.clear();
            hashCompany.clear();

            if(context != null)
            {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        }

    }
    public void exist() {
        AppActivityManager.getAppActivityManager(mApp).AppExit(mApp);
    }
    public String measureImg(String path) {
        String url = BASE_NET_PATH + path;
        return url;
    }

    public void sendFileContacts(Context context, File file) {
        Intent intent = new Intent(context, ContactsListActivity.class);
        intent.putExtra("path", file.getPath());
        intent.putExtra("select", true);
        context.startActivity(intent);
    }

    public void sendContacts(Context context, String subject) {
        Intent intent = new Intent(context, ContactsListActivity.class);
        intent.putExtra("subject", subject);
        intent.putExtra("select", true);
        context.startActivity(intent);
    }

    public void sendCard(Context context, Contacts contacts) {
        Intent intent = new Intent(context, ContactsListActivity.class);
        intent.putExtra("contacts", contacts);
        intent.putExtra("select", true);
        context.startActivity(intent);
    }

    public void setContactHead(Context context, File icon, ImageView imageView) {
        if (imageView != null) {
            if (icon.exists()) {
                Glide.with(context).load(icon).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.contact_detial_head);
            }
        }
    }

    public void setContactBg(Context context, File icon, ImageView imageView) {
        if (imageView != null) {
            if (icon.exists()) {
                Glide.with(context).load(icon).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.contact_detial_head);
            }
        }
    }


    public void startScan(Activity context, String className) {

        Intent intent = new Intent(context, BigwinerScanActivity.class);
        intent.putExtra("class", className);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (className.length() == 0) {
            context.startActivityForResult(intent, ScanUtils.SCAN_FINISH);
        } else {
            context.startActivity(intent);
        }

    }

    public void startScan(Activity context, String className, Meeting meeting,String title) {

        Intent intent = new Intent(context, BigwinerScanActivity.class);
        intent.putExtra("class", className);
        if (meeting != null)
            intent.putExtra("meeting", meeting);
        intent.putExtra("title", title);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public void startScan(Activity context) {
        Intent intent = new Intent(context, BigwinerScanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivityForResult(intent, ScanUtils.SCAN_FINISH);
    }
    public void startSelectView(Context mContext, MapSelect selects, String title, String action, boolean signal, boolean showSearch,int maxcount) {
        SelectManager.getInstance().mSelects.clear();
        SelectManager.getInstance().mSignal = null;
        SelectManager.getInstance().mList.clear();
        SelectManager.getInstance().mSelects.addAll(selects.list);
        if(signal == false)
        SelectManager.getInstance().mList.addAll(selects.selectlist);
        else
        {
            if(selects.selectlist.size() > 0)
            SelectManager.getInstance().mSignal = selects.selectlist.get(0);
        }

        Intent intent = new Intent(mContext, SelectActivity.class);
        intent.putExtra("signal", signal);
        intent.putExtra("title", title);
        intent.putExtra("showSearch", showSearch);
        intent.putExtra("max", maxcount);
        intent.setAction(action);
        mContext.startActivity(intent);
    }

    public void startSelectView(Context mContext, ArrayList<Select> selects, String title, String action, boolean signal, boolean showSearch) {
        SelectManager.getInstance().mSelects.clear();
        SelectManager.getInstance().mSignal = null;
        SelectManager.getInstance().mList.clear();
        SelectManager.getInstance().mSelects.addAll(selects);
        for (int i = 0; i < selects.size(); i++) {
            if (selects.get(i).iselect == true) {
                if (signal) {
                    SelectManager.getInstance().mSignal = selects.get(i);
                    break;
                } else {
                    SelectManager.getInstance().mList.add(selects.get(i));
                }
            }
        }
        if (signal && SelectManager.getInstance().mSignal == null) {
            SelectManager.getInstance().mSignal = selects.get(0);
        }
        Intent intent = new Intent(mContext, SelectActivity.class);
        intent.putExtra("signal", signal);
        intent.putExtra("title", title);
        intent.putExtra("showSearch", showSearch);
        intent.setAction(action);
        mContext.startActivity(intent);
    }

    public void startSelectView(Context mContext, ArrayList<Select> selects, String title, String action, boolean signal, boolean showSearch,boolean cancle) {
        SelectManager.getInstance().mSelects.clear();
        SelectManager.getInstance().mSignal = null;
        SelectManager.getInstance().mList.clear();
        SelectManager.getInstance().mSelects.addAll(selects);
        for (int i = 0; i < selects.size(); i++) {
            if (selects.get(i).iselect == true) {
                if (signal) {
                    SelectManager.getInstance().mSignal = selects.get(i);
                    break;
                } else {
                    SelectManager.getInstance().mList.add(selects.get(i));
                }
            }
        }
        if (signal && SelectManager.getInstance().mSignal == null) {
            if(selects.size() > 0)
            SelectManager.getInstance().mSignal = selects.get(0);
        }
        Intent intent = new Intent(mContext, SelectActivity.class);
        intent.putExtra("signal", signal);
        if(signal == true)
        {
            intent.putExtra("cancleable", cancle);
        }
        intent.putExtra("title", title);
        intent.putExtra("showSearch", showSearch);
        intent.setAction(action);
        mContext.startActivity(intent);
    }

    public void startSelectViewCity(Context mContext, ArrayList<Select> selects, String title, String action, boolean signal, boolean showSearch) {
        SelectManager.getInstance().mSelects.clear();
        SelectManager.getInstance().mSignal = null;
        SelectManager.getInstance().mList.clear();
        SelectManager.getInstance().mSelects.addAll(selects);
        for (int i = 0; i < selects.size(); i++) {
            if (selects.get(i).iselect == true) {
                if (signal) {
                    SelectManager.getInstance().mSignal = selects.get(i);
                    break;
                } else {
                    SelectManager.getInstance().mList.add(selects.get(i));
                }
            }
        }
        if (signal && SelectManager.getInstance().mSignal == null) {
            SelectManager.getInstance().mSignal = selects.get(0);
        }
        Intent intent = new Intent(mContext, SelectActivity.class);
        intent.putExtra("signal", signal);
        intent.putExtra("title", title);
        intent.putExtra("city", true);
        intent.putExtra("showSearch", showSearch);
        intent.setAction(action);
        mContext.startActivity(intent);
    }

    public void cleanSelect(ArrayList<Select> selects) {
        for (int i = 0; i < selects.size(); i++) {
            selects.get(i).iselect = false;
        }

    }

    public void getProSet() {
        //打开数据库
        DistrictSearch search = new DistrictSearch(mApp);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("省");//传入关键字
        query.setPageSize(40);
        query.setShowBoundary(false);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(OnDistrictSearchListener);//绑定监听器
        search.searchDistrictAsyn();
        //开始搜索
    }

    public void setBeijing() {
        DistrictSearch search = new DistrictSearch(mApp);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("北京市");//传入关键字
        query.setPageSize(40);
        query.setShowBoundary(false);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(OnDistrictSearchListener);//绑定监听器
        search.searchDistrictAsyn();
    }

    public void setTianjin() {
        DistrictSearch search = new DistrictSearch(mApp);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("上海市");//传入关键字
        query.setPageSize(40);
        query.setShowBoundary(false);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(OnDistrictSearchListener);//绑定监听器
        search.searchDistrictAsyn();
    }

    public void setSanghai() {
        DistrictSearch search = new DistrictSearch(mApp);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("重庆市");//传入关键字
        query.setPageSize(40);
        query.setShowBoundary(false);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(OnDistrictSearchListener);//绑定监听器
        search.searchDistrictAsyn();
    }

    public void setChongqin() {
        DistrictSearch search = new DistrictSearch(mApp);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("重庆市");//传入关键字
        query.setPageSize(40);
        query.setShowBoundary(false);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(OnDistrictSearchListener);//绑定监听器
        search.searchDistrictAsyn();
    }

    public void setZiZhi() {
        DistrictSearch search = new DistrictSearch(mApp);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("自治区");//传入关键字
        query.setPageSize(40);
        query.setShowBoundary(false);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(OnDistrictSearchListener);//绑定监听器
        search.searchDistrictAsyn();
    }

    public void setTebie() {
        DistrictSearch search = new DistrictSearch(mApp);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("特别行政区");//传入关键字
        query.setPageSize(40);
        query.setShowBoundary(false);//是否返回边界值
        search.setQuery(query);
        search.setOnDistrictSearchListener(OnDistrictSearchListener);//绑定监听器
        search.searchDistrictAsyn();
    }

    public DistrictSearch.OnDistrictSearchListener OnDistrictSearchListener = new DistrictSearch.OnDistrictSearchListener() {


        @Override
        public void onDistrictSearched(DistrictResult districtResult) {
            for (int i = 0; i < districtResult.getDistrict().size(); i++) {
                DistrictItem districtItem = districtResult.getDistrict().get(i);
                Select select = new Select(districtItem.getName(), districtItem.getName());
                allprovience.hashMap.put(select.mId, select);
                allprovience.list.add(select);
                MapSelect citys = allcity.get(select.mName);
                if (provience == null) {
                    provience = select;
                    provience.iselect = true;
                }
                if (citys == null) {
                    citys = new MapSelect();
                    allcity.put(select.mName, citys);
                }
                for (int j = 0; j < districtItem.getSubDistrict().size(); j++) {
                    DistrictItem districtItem1 = districtItem.getSubDistrict().get(j);
                    Select select1 = new Select(districtItem1.getName(), districtItem1.getName());
                    citys.list.add(select1);
                    citys.hashMap.put(select1.mId, select1);
                    if (city == null) {
                        city = select1;
                        city.iselect = true;
                    }
                }

            }
        }
    };

    public void selectLocation(AMapLocation amapLocation) {
        boolean citychange = false;
        if (city != null) {
            if (!city.mId.equals(amapLocation.getCity())) {
                citychange = true;
            }
            city.iselect = false;
        }
        if (provience != null) {
            provience.iselect = false;
        }
        provience = allprovience.hashMap.get(amapLocation.getProvince());
        citySelect = allcity.get(amapLocation.getProvince());
        city = citySelect.hashMap.get(amapLocation.getCity());
        if (provience != null) {
            provience.iselect = true;
        }
        if (city != null) {
            city.iselect = true;
        }
        if (citychange) {
            Intent intent = new Intent(BigwinerApplication.ACTION_LOCATION_CHANGE);
            intent.setPackage(BigwinerApplication.mApp.getPackageName());
            mApp.sendBroadcast(intent);
        }
    }

    public String getLocalHead(String name) {
        return mFileUtils.pathUtils.getfilePath(BigwinerApplication.mApp.mAccount.mRecordId + "/" + "head" + "/" + name);
    }


    public void setMyCompany() {
        Company company = hashCompany.get(BigwinerApplication.mApp.mAccount.mUCid);
        if (company != null) {
            BigwinerApplication.mApp.company.copy(company);
        }
    }

//    public void initChannel() {
//        ArrayList<Channel> channels = new ArrayList<Channel>();
//        Channel channel = new Channel();
//        channel.id = CHANNEL_ID_CONVERSATION;
//        channel.name = CHANNEL_NAME_CONVERSATION;
//        channel.leave = NotificationManager.IMPORTANCE_HIGH;
//        channels.add(channel);
//        NotifictionManager.init(mApp, R.drawable.icon, R.drawable.icon, channels);
//    }

    public void askSharePromiess(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            sharepromiess = true;
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(context, mPermissionList, SHARE_PROMIESS_CODE);
        }
        else
        {
            sharepromiess = true;
        }
    }

    public static class DoshareListener implements View.OnClickListener {

        public Activity activity;
        public ShareItem shareItem;

        public DoshareListener(Activity activity,ShareItem shareItem){
            this.activity = activity;
            this.shareItem = shareItem;
        }

        @Override
        public void onClick(View v) {
            BigwinerApplication.mApp.doShare(activity,shareItem);
        }
    };

    public void doShare(Activity context,ShareItem shareItem) {
        askSharePromiess(context);
        if(sharepromiess)
        {

            UMWeb web = new UMWeb(shareItem.weburl);
            web.setTitle(shareItem.title);//标题
            if(shareItem.picurl.length() > 0)
            {
                UMImage image = new UMImage(context, shareItem.picurl);//网络图片
                web.setThumb(image);  //缩略图
            }
            web.setDescription(shareItem.des);//描述
            new ShareAction(context).withMedia(web).setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA, SHARE_MEDIA.QQ)
                    .setCallback(shareListener).open();

        }
        else
        {
            AppUtils.showMessage(context,"");
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            AppUtils.showMessage(mApp,mApp.getString(R.string.share_success));
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            AppUtils.showMessage(mApp,"");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            AppUtils.showMessage(mApp,"");
        }
    };


    public boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }

    public UpDataManager.NotificationOperation updataOperation = new UpDataManager.NotificationOperation() {
        @Override
        public void showProgress(String title, String content, long max, long min) {
            int imax = 1000;
            int ifinish = 0;
            if(max > 0)
            {
                ifinish = (int) (imax*min/max);
            }
            if(last  == null)
            {
                last = new UpdataDownloadThread.UpMessage();
                last.size = max;
                last.finishsize = min;
            }
            if(last.finishsize < min)
            {
                last.finishsize = min;
                oper.creatNotificationNopen(title,content,imax,ifinish);
            }

        }

        @Override
        public void showMesage(String title, String content) {
            oper.creatNotificationNopen(title,content);
        }

        @Override
        public void showCancle() {
            oper.cancleNotification();
        }
    };

    public MapManager.MapFunctions functions = new MapManager.MapFunctions() {

        @Override
        public void sendContact(Context context, Intent intent) {
            Intent intent1 = new Intent(context, ContactsListActivity.class);
            intent1.putExtra("json", intent.getStringExtra("json"));
            context.startActivity(intent1);
        }
    };

    public ChatUtils.SampleChatFunctions sampleChatFunctions = new ChatUtils.SampleChatFunctions()
    {

        @Override
        public void praseLeaveMessage(NetObject netObject) {
            ConversationPrase.praseMessage(mApp, netObject);
            Intent intent = new Intent(MainActivity.ACTION_LEAVE_MESSAGE);
            intent.setPackage(BigwinerApplication.mApp.getPackageName());
            mApp.sendBroadcast(intent);
        }

        @Override
        public String getFileUrl(String id) {
            return measureImg(id);
        }

        @Override
        public void updataChatView(Conversation conversation) {
            Intent intent = new Intent(BigWinerConversationManager.ACTION_UPDATA_CONVERSATION_MESSAGE);
            intent.putExtra("msg",conversation);
            intent.setPackage(BigwinerApplication.mApp.getPackageName());
            mApp.sendBroadcast(intent);
        }

        @Override
        public void checkHead(Attachment attachment) {
            ContactsAsks.checkHead(mApp,ChatUtils.getChatUtils().mLeaveMessageHandler,attachment, LeaveMessageHandler.CHEACK_HTED_RESULT);
        }

        @Override
        public void checkHeadResult(NetObject netObject) {
            ContactsPrase.praseContactHead(mApp,netObject);
        }

        @Override
        public String getHeadIcom(String path) {
            return ContactsAsks.getContactIconUrlPath(path);
        }
    };

    public boolean checkConfirm(Context context,String title) {
        if(mAccount.confrim.equals(context.getString(R.string.contacts_un_confrim)))
        {
            AppUtils.creatDialogTowButton(context,context.getString(R.string.confirm_setting),title,context.getString(R.string.button_word_cancle),context.getString(R.string.button_word_ok),
                    null,new SettingListener(context));
            return false;
        }
        else
        {
            return true;
        }
    }

    public class SettingListener implements DialogInterface.OnClickListener {

        public Context context;

        public SettingListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(context, SettingActivity.class);
            context.startActivity(intent);
        }
    }

    public NetTask.checkToken checkToken = new NetTask.checkToken(){

        @Override
        public boolean checkToken(String result,String rul) {
            if(result != null)
            {
                if(result.length() > 0)
                {
                    try {
                        XpxJSONObject xpxJSONObject = new XpxJSONObject(result);
                        if(xpxJSONObject.getInt("code",0) == -1)
                        {

                            return false;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        return true;
                    }
                }
            }
            return true;
        }
    };
}
