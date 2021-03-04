package com.intersky.android.view;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.multidex.MultiDex;


import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.alibaba.sdk.android.push.register.OppoRegister;
import com.alibaba.sdk.android.push.register.VivoRegister;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.interksy.autoupdate.UpDataManager;
import com.intersky.R;
import com.intersky.android.asks.ConversationAsks;
import com.intersky.android.asks.ImAsks;
import com.intersky.android.entity.IntersakyData;
import com.intersky.android.handler.ShareHandler;
import com.intersky.android.prase.ImPrase;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import intersky.appbase.GetProvideGetPath;
import intersky.attendance.AttendanceManager;
import intersky.appbase.Actions;
import intersky.appbase.AppActivityManager;
import intersky.appbase.BaseActivity;
import intersky.appbase.Local.LocalData;
import intersky.appbase.PermissionResult;
import intersky.appbase.bundle.BaseLibsInit;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.ShareItem;
import intersky.appbase.utils.XpxShare;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideConfiguration;
import intersky.chat.ChatUtils;
import intersky.chat.ContactManager;
import intersky.chat.handler.LeaveMessageHandler;
import intersky.chat.handler.SendMessageHandler;
import intersky.chat.view.activity.ContactsDetialActivity;
import intersky.chat.view.activity.ContactsListActivity;
import intersky.conversation.ConversationManager;
import intersky.conversation.NotifictionManager;
import intersky.conversation.entity.BrodcastData;
import intersky.conversation.entity.NotificationData;
import intersky.appbase.entity.Register;
import intersky.document.DocumentManager;
import intersky.filetools.FileUtils;
import intersky.filetools.handler.DownloadThreadHandler;
import intersky.function.FunctionUtils;
import intersky.function.view.activity.WebMessageActivity;
import intersky.json.XpxJSONObject;
import intersky.leave.LeaveManager;
import intersky.mail.MailManager;
import intersky.notice.NoticeManager;
import intersky.oa.OaUtils;
import intersky.scan.ScanUtils;
import intersky.schedule.ScheduleManager;
import intersky.sign.SignManager;
import intersky.task.TaskManager;
import intersky.vote.VoteManager;
import intersky.workreport.WorkReportManager;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.Service;
import xpx.map.MapManager;

import static com.alibaba.mtl.appmonitor.AppMonitorDelegate.TAG;

/*
 * Author: pan Email:gdpancheng@gmail.com
 * Created Date:2013-7-23
 * Copyright @ 2013 BU
 * Description: 全局application
 *
 * History:
 */
public class InterskyApplication extends Application {

    //http://www.intersky.com.cn/app/android.version/open.html
    public static final String CHECK_VERSION_URL = "http://www.intersky.com.cn/app/android.version/android_json.txt";
    public static final String UPDATA_APP_URL = "http://www.intersky.com.cn/app/android.version/android_app.apk";
    public static final String UPDATA_NAME = "intersky.apk";
    public static final String CONVERSATION_DB_NAME = "com.intersky.conversation";
    public static final int CONVERSATION_DB_VERSION = 10;
    public static final int SHARE_PERMISSION_REQUEST_CODE = 1001;
    public static final boolean istest = false;
    public static InterskyApplication mApp;
    public Account mAccount = new Account();
    public Account mLAccount = new Account();
    public Service mService = new Service();
    public Service mLService = new Service();
    public AppActivityManager appActivityManager;
//    public CommendManager commendManager;
    public MapManager mMapManager;
    public boolean first = false;
    public String szImei = "";
    public UpDataManager upDataManager;
    public Share share = new Share();

    public ConversationManager conversationManager;
    public ContactManager contactManager;
    public NotifictionManager notifictionManager;
    public ChatUtils chatUtils;
    public FileUtils mFileUtils;
    public FunctionUtils functionUtils;
    public MailManager mailManager;
    public String deviceId = "";
    public ArrayList<Register> registers = new ArrayList<Register>();
    public NotifictionManager.NotificationOper downloadOper;
    public NotifictionManager.NotificationOper updataOper;
    public DocumentManager documentManager;
    public Handler handler = new Handler();
    public OaUtils oaUtils;
    public SignManager signManager;
    public AttendanceManager attendanceManager;
    public TaskManager taskManager;
    public LeaveManager leaveManager;
    public ScheduleManager scheduleManager;
    public VoteManager voteManager;
    public NoticeManager noticeManager;
    public WorkReportManager workReportManager;
//    public boolean setmoduls;
    public interface MsgDisplayListener {
        void handle(String msg);
    }

    public void onCreate() {
        mApp = this;
        initdata();
        initmodules();
        initApplacation();
        super.onCreate();
    }
    //  "/intersky"+"/"+InterskyApplication.mApp.mService.sAddress
    //                +"/"+InterskyApplication.mApp.mAccount.mRecordId
    public void initmodules() {

        InterskyApplication.mApp.first = AppUtils.getAppFirst(mApp);
        InterskyApplication.mApp.szImei = AppUtils.getAppUnicode(mApp);
//        InterskyApplication.mApp.commendManager = CommendManager.init();
        CrashReport.initCrashReport(getApplicationContext(), "9d23881d23", false);
        InterskyApplication.mApp.appActivityManager = AppActivityManager.getAppActivityManager(mApp);
        InterskyApplication.mApp.mMapManager =  MapManager.init(functions,mApp);
        InterskyApplication.mApp.mMapManager.initLastlocation();
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5b0773d0");
        File file = new File(mApp.getExternalFilesDir(null).getPath() + "/intersky");
        if (file.exists() == false) {
            file.mkdirs();
        }
        try {
            PackageManager packageManager = mApp.getApplicationContext().getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    mApp.getPackageName(), 0);
            upDataManager = UpDataManager.init(mApp, CHECK_VERSION_URL, UPDATA_APP_URL, packInfo.versionName,
                    packInfo.versionCode, file.getPath() + "/" + UPDATA_NAME, updataOperation,getProvidePath);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        GlideConfiguration.init(file);


        initCloudChannel(this);
        initThridChannel();                //53e1c977fd98c5aa760050f7
        initUmShare();


        NetUtils.init(mApp);
        NetUtils.getInstance().addaskManagerThread("imchat", 1000, 1);
        ScanUtils.init(mApp);
        BaseLibsInit.init(mApp);


        InterskyApplication.mApp.mFileUtils = FileUtils.init(InterskyApplication.mApp,InterskyApplication.mApp.getProvidePath,InterskyApplication.mApp.fileOperation,InterskyApplication.mApp.share);
        InterskyApplication.mApp.contactManager = ContactManager.init(InterskyApplication.mApp);
        InterskyApplication.mApp.oaUtils = OaUtils.init(InterskyApplication.mApp,InterskyApplication.mApp.mMapManager,InterskyApplication.mApp.contactManager);
        initConversationRegister();
        InterskyApplication.mApp.conversationManager = ConversationManager.init(mApp,CONVERSATION_DB_NAME,CONVERSATION_DB_VERSION);
        InterskyApplication.mApp.chatUtils =  ChatUtils.init(InterskyApplication.mApp,InterskyApplication.mApp.contactManager.contactsHashMap,
                InterskyApplication.mApp.chatFunctions,InterskyApplication.mApp.mMapManager,""
        );
        InterskyApplication.mApp.chatUtils.enable = false;
        InterskyApplication.mApp.documentManager = DocumentManager.init(InterskyApplication.mApp,InterskyApplication.mApp.notificationOperation);
        InterskyApplication.mApp.mailManager = MailManager.init(InterskyApplication.mApp,InterskyApplication.mApp.share);
        InterskyApplication.mApp.functionUtils = FunctionUtils.init(InterskyApplication.mApp);

    }

    public void setModules() {
        CrashReport.setUserId(InterskyApplication.mApp.mAccount.mRecordId
                +"_"+InterskyApplication.mApp.mAccount.getName()
                +"/"+InterskyApplication.mApp.mService.sAddress);
        InterskyApplication.mApp.mFileUtils.pathUtils.setAppBase("/intersky"+"/"+InterskyApplication.mApp.mService.sAddress
                +"/"+InterskyApplication.mApp.mAccount.mRecordId);
        InterskyApplication.mApp.contactManager.setAccount(InterskyApplication.mApp.mAccount);
        InterskyApplication.mApp.oaUtils.setService(InterskyApplication.mApp.mService);
        InterskyApplication.mApp.oaUtils.setAccount(InterskyApplication.mApp.mAccount);
        InterskyApplication.mApp.conversationManager.setService(InterskyApplication.mApp.mService);
        InterskyApplication.mApp.conversationManager.setDataBaseId(InterskyApplication.mApp.mAccount.mRecordId
                +InterskyApplication.mApp.mService.sAddress
                +InterskyApplication.mApp.mAccount.mCompanyId);
        InterskyApplication.mApp.chatUtils.setmAccount(mAccount);
        InterskyApplication.mApp.chatUtils.setRegister(InterskyApplication.mApp.conversationManager.conversationAll.getRegister(IntersakyData.CONVERSATION_TYPE_MESSAGE));
        InterskyApplication.mApp.chatUtils.startChart();
        if(mAccount.isouter == false)
        {
            InterskyApplication.mApp.documentManager.setService(InterskyApplication.mApp.mService);
            InterskyApplication.mApp.documentManager.setmAccount(InterskyApplication.mApp.mAccount);
            InterskyApplication.mApp.documentManager.startDocument();
            InterskyApplication.mApp.mailManager.setAccount(InterskyApplication.mApp.mAccount);
            InterskyApplication.mApp.mailManager.setService(InterskyApplication.mApp.mService);
            InterskyApplication.mApp.mailManager.setRegister(InterskyApplication.mApp.conversationManager.conversationAll.getRegister(IntersakyData.CONVERSATION_TYPE_IWEB_MAIL));
            InterskyApplication.mApp.mailManager.cleanAll();
            InterskyApplication.mApp.mailManager.getAllData();
            InterskyApplication.mApp.mailManager.initLocal();
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_mail_main),InterskyApplication.mApp.mailManager.commendFunStart);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_mail_new),InterskyApplication.mApp.mailManager.commendFunNew);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_document),InterskyApplication.mApp.documentManager.commendFunMain);
        }
        InterskyApplication.mApp.functionUtils.setAccount(InterskyApplication.mApp.mAccount);
        InterskyApplication.mApp.functionUtils.setService(InterskyApplication.mApp.mService);
        InterskyApplication.mApp.functionUtils.starFunctionData();
//        InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_card),InterskyApplication.mApp.functionUtils.commendCard);
//        InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_warn),InterskyApplication.mApp.functionUtils.commendWarn);
//        InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_workflow),InterskyApplication.mApp.functionUtils.commendWorkFlow);
//        if (InterskyApplication.mApp.mAccount.mCloundAdminId.length() == 0 || InterskyApplication.mApp.mAccount.mCompanyId.length() == 0 || InterskyApplication.mApp.mAccount.cloudServer.length() == 0)
//        {
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_report));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_date));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_date_new));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_leave));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_leave_new));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_task));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_task_new));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_project_new));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_vote));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_vote_new));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_notice));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_notice_new));
//        }
//        else
//        {
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_report),InterskyApplication.mApp.functionUtils.commendWorkReport);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_date),InterskyApplication.mApp.functionUtils.commendDate);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_date_new),InterskyApplication.mApp.functionUtils.commendDateNew);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_leave),InterskyApplication.mApp.functionUtils.commendLeave);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_leave_new),InterskyApplication.mApp.functionUtils.commendLeaveNew);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_task),InterskyApplication.mApp.functionUtils.commendTaskManager);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_task_new),InterskyApplication.mApp.functionUtils.commendTaskNew);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_project_new),InterskyApplication.mApp.functionUtils.commendProjectNew);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_vote),InterskyApplication.mApp.functionUtils.commendVote);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_vote_new),InterskyApplication.mApp.functionUtils.commendVoteNew);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_notice),InterskyApplication.mApp.functionUtils.commendNotice);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_notice_new),InterskyApplication.mApp.functionUtils.commendNoticeNew);
//        }
        FileUtils.mFileUtils.mDownloadThreadHandler = new DownloadThreadHandler(InterskyApplication.mApp.mFileUtils);
        FileUtils.mFileUtils.mDownloadThreadHandler.sendEmptyMessageDelayed(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD,300);
        ContactManager.mContactManager.initContacts();
        InterskyApplication.mApp.functionUtils.getBaseHit();
        InterskyApplication.mApp.functionUtils.getOaHit();
        InterskyApplication.mApp.chatUtils.setmAccount(mAccount);

        InterskyApplication.mApp.leaveManager.getLeaveType();
    }


    public void updataSample() {
        InterskyApplication.mApp.contactManager.setAccount(InterskyApplication.mApp.mAccount);
        InterskyApplication.mApp.oaUtils.setAccount(InterskyApplication.mApp.mAccount);
        InterskyApplication.mApp.chatUtils.setmAccount(mAccount);
        InterskyApplication.mApp.chatUtils.setRegister(InterskyApplication.mApp.conversationManager.conversationAll.getRegister(IntersakyData.CONVERSATION_TYPE_MESSAGE));
        InterskyApplication.mApp.chatUtils.startChart();
        if(mAccount.isouter == false)
        {
            InterskyApplication.mApp.documentManager.setmAccount(InterskyApplication.mApp.mAccount);
            InterskyApplication.mApp.mailManager.setAccount(InterskyApplication.mApp.mAccount);
            InterskyApplication.mApp.mailManager.cleanAll();
            InterskyApplication.mApp.mailManager.getAllData();
            InterskyApplication.mApp.mailManager.initLocal();
        }
        InterskyApplication.mApp.functionUtils.setAccount(InterskyApplication.mApp.mAccount);
        InterskyApplication.mApp.functionUtils.starFunctionData();
//        InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_card),InterskyApplication.mApp.functionUtils.commendCard);
//        InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_warn),InterskyApplication.mApp.functionUtils.commendWarn);
//        InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_workflow),InterskyApplication.mApp.functionUtils.commendWorkFlow);
//        if (InterskyApplication.mApp.mAccount.mCloundAdminId.length() == 0 || InterskyApplication.mApp.mAccount.mCompanyId.length() == 0 || InterskyApplication.mApp.mAccount.cloudServer.length() == 0)
//        {
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_report));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_date));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_date_new));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_leave));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_leave_new));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_task));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_task_new));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_project_new));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_vote));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_vote_new));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_notice));
//            InterskyApplication.mApp.commendManager.commends.remove(InterskyApplication.mApp.getString(R.string.commend_fun_notice_new));
//        }
//        else
//        {
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_report),InterskyApplication.mApp.functionUtils.commendWorkReport);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_date),InterskyApplication.mApp.functionUtils.commendDate);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_date_new),InterskyApplication.mApp.functionUtils.commendDateNew);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_leave),InterskyApplication.mApp.functionUtils.commendLeave);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_leave_new),InterskyApplication.mApp.functionUtils.commendLeaveNew);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_task),InterskyApplication.mApp.functionUtils.commendTaskManager);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_task_new),InterskyApplication.mApp.functionUtils.commendTaskNew);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_project_new),InterskyApplication.mApp.functionUtils.commendProjectNew);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_vote),InterskyApplication.mApp.functionUtils.commendVote);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_vote_new),InterskyApplication.mApp.functionUtils.commendVoteNew);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_notice),InterskyApplication.mApp.functionUtils.commendNotice);
//            InterskyApplication.mApp.commendManager.registerComment(InterskyApplication.mApp.getString(R.string.commend_fun_notice_new),InterskyApplication.mApp.functionUtils.commendNoticeNew);
//        }
        ContactManager.mContactManager.initContacts();
        InterskyApplication.mApp.chatUtils.setmAccount(mAccount);
    }

    public void initConversationRegister() {
        signManager = SignManager.init(oaUtils,mMapManager);
        attendanceManager = AttendanceManager.init(oaUtils,mApp,mMapManager);
        leaveManager = LeaveManager.init(oaUtils,mApp);
        taskManager = TaskManager.init(oaUtils,mApp);
        scheduleManager = ScheduleManager.init(oaUtils,mApp);
        workReportManager = WorkReportManager.init(oaUtils,mApp);
        voteManager = VoteManager.init(oaUtils,mApp);
        noticeManager = NoticeManager.init(oaUtils,mApp);
        Register register = new Register(IntersakyData.CONVERSATION_TYPE_MESSAGE,Register.CONVERSATION_COLLECT_TYPE_BY_DETIAL,2);
        register.typeRealName = this.getString(R.string.conversation_msg);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "iCloud[im],iweb[im]";
        register.mPic = R.drawable.gropmessageh;
        registers.add(register);
        register = new Register(IntersakyData.CONVERSATION_TYPE_NOTICE,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,1);
        register.typeRealName = this.getString(R.string.conversation_notice);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "notice";
        register.mPic = R.drawable.noticeh;
        mApp.noticeManager.setRegister(register);
        registers.add(register);
        register = new Register(IntersakyData.CONVERSATION_TYPE_TASK,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,1);
        register.typeRealName = this.getString(R.string.conversation_task);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "task";
        register.mPic = R.drawable.taskh;
        mApp.taskManager.setRegister(register);
        registers.add(register);
        register = new Register(IntersakyData.CONVERSATION_TYPE_LEAVE,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,1);
        register.typeRealName = this.getString(R.string.conversation_leave);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "leave";
        register.mPic = R.drawable.leaveh;
        mApp.leaveManager.setRegister(register);
        registers.add(register);
        register = new Register(IntersakyData.CONVERSATION_TYPE_REPORT,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,1);
        register.typeRealName = this.getString(R.string.conversation_report);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "report";
        register.mPic = R.drawable.workreporth;
        mApp.workReportManager.setRegister(register);
        registers.add(register);
        register = new Register(IntersakyData.CONVERSATION_TYPE_VOTE,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,1);
        register.typeRealName = this.getString(R.string.conversation_vote);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "vote";
        register.mPic = R.drawable.voteh;
        mApp.voteManager.setRegister(register);
        registers.add(register);
        register = new Register(IntersakyData.CONVERSATION_TYPE_GROP_MESSAGE,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,1);
        register.typeRealName = this.getString(R.string.conversation_gropmess);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "oa";
        register.mPic = R.drawable.gropmessageh;
        registers.add(register);
        register = new Register(IntersakyData.CONVERSATION_TYPE_IWEB_MAIL,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,1);
        register.typeRealName = this.getString(R.string.conversation_mail);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "iCloud[mail],iweb[mail]";
        register.mPic = R.drawable.mailh;
        registers.add(register);
        register = new Register(IntersakyData.CONVERSATION_TYPE_IWEB_APPROVE,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,1);
        register.typeRealName = this.getString(R.string.conversation_approve);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "iCloud[workflow],iweb[workflow]";
        register.mPic = R.drawable.apporveh;
        registers.add(register);

        register = new Register(IntersakyData.CONVERSATION_TYPE_IWEB_REMIND,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,1);
        register.typeRealName = this.getString(R.string.conversation_reminder);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "iCloud[reminder]";
        register.mPic = R.drawable.remindh;
        registers.add(register);

        register = new Register(IntersakyData.CONVERSATION_TYPE_IWEB_APP,Register.CONVERSATION_COLLECT_TYPE_BY_TYPE,1);
        register.typeRealName = this.getString(R.string.conversation_iwebapp);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "iweb[api]";
        register.mPic = R.drawable.gropmessageh;
        registers.add(register);
        register = new Register(IntersakyData.CONVERSATION_TYPE_IWEB_SYSTEM,Register.CONVERSATION_COLLECT_TYPE_BY_NULL,1);
        register.typeRealName = this.getString(R.string.conversation_iwebsystem);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "iweb[system]";
        register.mPic = R.drawable.gropmessageh;
        registers.add(register);
        register = new Register(IntersakyData.CONVERSATION_TYPE_DOCUMENT,Register.CONVERSATION_COLLECT_TYPE_BY_NULL,1);
        register.typeRealName = this.getString(R.string.conversation_document);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "iweb[document]";
        register.mNotivicationleavel = NotificationManager.IMPORTANCE_LOW;
        register.mPic = R.drawable.document;
        registers.add(register);
        register = new Register(IntersakyData.CONVERSATION_TYPE_UPDATA,Register.CONVERSATION_COLLECT_TYPE_BY_NULL,1);
        register.typeRealName = this.getString(R.string.conversation_updata);
        register.conversationFunctions = conversationFunctions;
        register.moduleId = "iweb[updata]";
        register.mNotivicationleavel = NotificationManager.IMPORTANCE_LOW;
        register.mPic = R.drawable.gropmessageh;
        registers.add(register);

    }

    public void initApplacation() {
        SharedPreferences sharedPre1 = mApp.getSharedPreferences(LocalData.LOGIN_INFO, 0);
        boolean islogin = sharedPre1.getBoolean(LocalData.LOGIN_INFO_STATU,false);
        SharedPreferences sharedPre = mApp.getSharedPreferences("Applacation", 0);
        if(sharedPre.getBoolean("exist",true) == false && islogin == true)
        {
            mAccount.mRecordId = sharedPre.getString("userid","");
            mAccount.mAccountId = sharedPre.getString("accountid","");
            mAccount.mPassword = sharedPre.getString("password","");
            mAccount.mUserName = sharedPre.getString("uesrname","");
            mAccount.mRealName = sharedPre.getString("realname","");
            mAccount.mRoleId = sharedPre.getString("roleid","");
            mAccount.mOrgId = sharedPre.getString("organizationid","");
            mAccount.mOrgName = sharedPre.getString("organizationname","");
            mAccount.mSex = sharedPre.getString("sex","");
            mAccount.mPhone = sharedPre.getString("phone","");
            mAccount.mMobile = sharedPre.getString("mobile","");
            mAccount.mFax = sharedPre.getString("fax","");
            mAccount.mEmail = sharedPre.getString("emile","");
            mAccount.isAdmin = sharedPre.getBoolean("isadmin",false);
            mAccount.mCompanyId = sharedPre.getString("companyid","");
            mAccount.mUCid = sharedPre.getString("ucid","");
            mAccount.mCompanyName = sharedPre.getString("companyname","");
            mAccount.mManagerId = sharedPre.getString("managerid","");
            mAccount.mCloundAdminId = sharedPre.getString("cloundadminid","");
            mAccount.mPosition = sharedPre.getString("position","");
            mAccount.cloudServer = sharedPre.getString("cloudServer","");
            if(NetUtils.getInstance().token.length() > 0)
            NetUtils.getInstance().token =sharedPre.getString("token","");
            if(sharedPre.contains("project")) {
                try {
                    mAccount.project = new XpxJSONObject(sharedPre.getString("project",""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mAccount.logininfo = sharedPre.getString("logininfo","");
            mLAccount.copy(mAccount);
            mService.sRecordId = sharedPre.getString("serviceid","");
            mService.sName = sharedPre.getString("servicename","");
            mService.sAddress = sharedPre.getString("serviceaddress","");
            mService.sCode = sharedPre.getString("servicecode","");
            mService.sPort = sharedPre.getString("serviceport","");
            mService.sType = sharedPre.getBoolean("servicetype",false);
            mLService.copy(mService);
            if(mService.sAddress.length() > 0)
            setModules();
        }
        SharedPreferences.Editor e = sharedPre.edit();
        e.putBoolean("exist", false);
        e.commit();
    }

    public void existApplacation() {
        SharedPreferences sharedPre = mApp.getSharedPreferences("Applacation", 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putBoolean("exist", true);
        e.commit();
    }

    public void saveApplacation() {
        SharedPreferences sharedPre = mApp.getSharedPreferences("Applacation", 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString("userid", mAccount.mRecordId);
        e.putString("accountid", mAccount.mAccountId);
        e.putString("password", mAccount.mPassword);
        e.putString("uesrname", mAccount.mUserName);
        e.putString("realname", mAccount.mRealName);
        e.putString("roleid", mAccount.mRoleId);
        e.putString("organizationid", mAccount.mOrgId);
        e.putString("organizationname", mAccount.mOrgName);
        e.putString("sex", mAccount.mSex);
        e.putString("phone", mAccount.mPhone);
        e.putString("mobile", mAccount.mMobile);
        e.putString("fax", mAccount.mFax);
        e.putString("emile", mAccount.mEmail);
        e.putBoolean("isadmin", mAccount.isAdmin);
        e.putString("companyid", mAccount.mCompanyId);
        e.putString("ucid", mAccount.mUCid);
        e.putString("companyname", mAccount.mCompanyName);
        e.putString("managerid", mAccount.mManagerId);
        e.putString("cloundadminid", mAccount.mCloundAdminId);
        e.putString("position", mAccount.mPosition);
        e.putString("cloudServer", mAccount.cloudServer);
        e.putString("project", mAccount.project.toString());
        e.putString("logininfo", mAccount.logininfo);
        e.putString("serviceid", mService.sRecordId);
        e.putString("servicename", mService.sName);
        e.putString("serviceaddress", mService.sAddress);
        e.putString("servicecode", mService.sCode);
        e.putString("serviceport", mService.sPort);
        e.putBoolean("servicetype", mService.sType);
        e.putString("token", NetUtils.getInstance().token);
        e.commit();


    }

    public void initUmShare()
    {
        UMConfigure.init(this, "5e5b16854ca35749c2000052", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
        PlatformConfig.setWeixin("wxa5ebdefe4a3485cf", "ca84fcfad71172530a5959ffabd41440");
        PlatformConfig.setQQZone("101857284", "a1bbe8166f9d412dc3eeeaefa193c844");
        //3921700954 /04b48b094faeb16683c32669824ebdad
        PlatformConfig.setSinaWeibo("4038552739", "6963c833fa7b6aad859eed528e121239", "http://sns.whalecloud.com");
        PlatformConfig.setDing("dingoa0k7ufuwsklq4ix4p");
        //        PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
    }


    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success");
                deviceId = pushService.getDeviceId();
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    public GetProvideGetPath getProvidePath = new GetProvideGetPath() {
        @Override
        public Uri getProvideGetPath(File file) {
            return FileProvider.getUriForFile(InterskyApplication.mApp, "com.intersky.fileprovider", file);
        }
    };



    public class Share implements XpxShare {

        public boolean isPremission = false;

        public void sharePremission(BaseActivity context,ShareItem shareItem) {
            if (Build.VERSION.SDK_INT >= 23 && isPremission == false) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.GET_ACCOUNTS};
                AppUtils.getPermission(mPermissionList, context, ShareHandler.DOSHARE, new ShareHandler(context,shareItem));
            } else {
                isPremission = true;
                InterskyApplication.mApp.share.doShare(context,shareItem);
            }
        }

        @Override
        public void doShare(BaseActivity context,String des, String keyword) {
            ShareItem shareItem = new ShareItem();
            if(keyword.length() != 0)
                shareItem.title = keyword;
            else
                shareItem.title = mAccount.getName()+mApp.getString(R.string.button_word_share);
            shareItem.des = des;
            shareItem.type = 0;
            if (isPremission == false) {
                sharePremission(context,shareItem);
                return;
            }
            new ShareAction(context).withText(shareItem.title).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA
                    , SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.DINGTALK)
                    .setCallback(new XpxUMShareListener(context)).open();
        }

        @Override
        public void doShare(BaseActivity context,String des, String keyword, String url) {
            ShareItem shareItem = new ShareItem();
            shareItem.picurl = url;
            if(keyword.length() != 0)
                shareItem.title = keyword;
            else
                shareItem.title = mAccount.getName()+mApp.getString(R.string.button_word_share);
            shareItem.type = 1;
            shareItem.des = des;
            if (isPremission == false) {
                sharePremission(context,shareItem);
                return;
            }
            UMImage image = new UMImage(context, url);//网络图片
            UMImage thumb =  new UMImage(context, R.drawable.icon);
//            image.setThumb(thumb);
            new ShareAction(context).withText(shareItem.title).withMedia(image).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA
                    , SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.DINGTALK)
                    .setCallback(new XpxUMShareListener(context)).open();
        }

        @Override
        public void doShare(BaseActivity context,String des, String keyword, File file) {
            ShareItem shareItem = new ShareItem();
            shareItem.file = file;
            if(keyword.length() != 0)
                shareItem.title = keyword;
            else
                shareItem.title = mAccount.getName()+mApp.getString(R.string.button_word_share);
            shareItem.type = 2;
            shareItem.des = des;
            if (isPremission == false) {
                sharePremission(context,shareItem);
                return;
            }
            UMImage image = new UMImage(context, file);//本地文件
            new ShareAction(context).withText(shareItem.title).withMedia(image).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA
                    , SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.DINGTALK)
                    .setCallback(new XpxUMShareListener(context)).open();
        }

        @Override
        public void doShare(BaseActivity context,String des, String keyword, int id) {
            ShareItem shareItem = new ShareItem();
            shareItem.rid = id;
            if(keyword.length() != 0)
                shareItem.title = keyword;
            else
                shareItem.title = mAccount.getName()+mApp.getString(R.string.button_word_share);
            shareItem.type = 3;
            shareItem.des = des;
            if (isPremission == false) {
                sharePremission(context,shareItem);
                return;
            }
            UMImage image = new UMImage(context, id);//资源文件
            new ShareAction(context).withText(shareItem.title).withMedia(image).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA
                    , SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.DINGTALK)
                    .setCallback(new XpxUMShareListener(context)).open();
        }

        @Override
        public void doShare(BaseActivity context,String des, String keyword, Bitmap bitmap) {

            ShareItem shareItem = new ShareItem();
            shareItem.bitmap = bitmap;
            if(keyword.length() != 0)
            shareItem.title = keyword;
            else
                shareItem.title = mAccount.getName()+mApp.getString(R.string.button_word_share);
            shareItem.type = 4;
            shareItem.des = des;
            if (isPremission == false) {
                sharePremission(context,shareItem);
                return;
            }
            UMImage image = new UMImage(context, bitmap);//bitmap文件
            new ShareAction(context).withText(shareItem.title).withMedia(image).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA
                    , SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.DINGTALK)
                    .setCallback(new XpxUMShareListener(context)).open();
        }

        @Override
        public void doShare(BaseActivity context,String des, String keyword, byte[] bitmapbyte) {

            ShareItem shareItem = new ShareItem();
            shareItem.bytes = bitmapbyte;
            if(keyword.length() != 0)
                shareItem.title = keyword;
            else
                shareItem.title = mAccount.getName()+mApp.getString(R.string.button_word_share);
            shareItem.type = 5;
            shareItem.des = des;
            if (isPremission == false) {
                sharePremission(context,shareItem);
                return;
            }
            UMImage image = new UMImage(context, bitmapbyte);//字节流
            new ShareAction(context).withText(shareItem.title).withMedia(image).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA
                    , SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.DINGTALK)
                    .setCallback(new XpxUMShareListener(context)).open();
        }

        @Override
        public void onActivityResult(Context context,int requestCode, int resultCode, Intent data) {
            UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
        }

        public void doShare(BaseActivity context, ShareItem shareItem) {
            switch (shareItem.type)
            {
                case 0:
                    share.doShare(context,shareItem.des,shareItem.title);
                    break;
                case 1:
                    share.doShare(context,shareItem.des,shareItem.title,shareItem.picurl);
                    break;
                case 2:
                    share.doShare(context,shareItem.des,shareItem.title,shareItem.file);
                    break;
                case 3:
                    share.doShare(context,shareItem.des,shareItem.title,shareItem.rid);
                    break;
                case 4:
                    share.doShare(context,shareItem.des,shareItem.title,shareItem.bitmap);
                    break;
                case 5:
                    share.doShare(context,shareItem.des,shareItem.title,shareItem.bytes);
                    break;
            }

        }
    };

    public class XpxUMShareListener implements UMShareListener {

        public Context context;

        public XpxUMShareListener(Context context) {
            this.context = context;
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            AppUtils.showMessage(context, context.getString(R.string.share_success));
        }

        /**
         * @param platform 平台类型
         * @param t        错误原因
         * @descrption 分享失败的回调
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            AppUtils.showMessage(context, t.getMessage());
        }

        /**
         * @param platform 平台类型
         * @descrption 分享取消的回调
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            AppUtils.showMessage(context, context.getString(R.string.share_cancle));
        }
    }

    public class SharePremisstionResult implements PermissionResult {

        public Share share;
        public ShareItem shareItem;
        public BaseActivity baseActivity;

        public SharePremisstionResult(BaseActivity baseActivity,Share share,ShareItem shareItem) {
            this.share = share;
            this.shareItem = shareItem;
            this.baseActivity = baseActivity;
        }

        @Override
        public void doResult(int code, int[] grantResults) {
            boolean access = true;
            if(grantResults.length > 0)
            {
                if (code == SHARE_PERMISSION_REQUEST_CODE) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            access = false;
                            break;
                        }
                    }
                }
                share.isPremission = access;
                if(share.isPremission)
                {
                    share.doShare(baseActivity,shareItem);
                }
                else
                {
                    AppUtils.showMessage(InterskyApplication.mApp,InterskyApplication.mApp.getString(R.string.permission_fail));
                }
            }
            else
            {
                AppUtils.showMessage(InterskyApplication.mApp,InterskyApplication.mApp.getString(R.string.permission_fail));
            }
        }
    }

    public NotifictionManager.DataMeasure dataMeasure = new NotifictionManager.DataMeasure() {

        @Override
        public NotificationData praseNoticicationData(String data, String title) {
            return measureMessage(data, title);
        }

        @Override
        public BrodcastData parseBrodcastData(NotificationData ndata, String data, String title) {
            return measureMessageIntent(ndata, data, title);
        }

        @Override
        public boolean checkShowChat(NotificationData ndata) {
            if (ndata.channel.registername.equals(IntersakyData.CONVERSATION_TYPE_MESSAGE)) {
                if (chatUtils != null) {
                    if (chatUtils.showContacts != null) {
                        if (chatUtils.showContacts.mRecordid.equals(ndata.detialid)) ;
                        {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

    };

    public void initThridChannel()
    {
        MiPushRegister.register(mApp, "2882303761518224508", "5221822434508");
        HuaWeiRegister.register(mApp);
        OppoRegister.register(mApp, "9a6fecc4c68f47c18cb4c59274398364", "0974d5aa66c040ac8f1107f29fc926f9"); // appKey/appSecret在OPPO开发者平台获取
        VivoRegister.register(mApp);
    }



    public Register.ConversationFunctions conversationFunctions = new Register.ConversationFunctions() {

		@Override
		public void Open(Intent intent) {

            String title = "";
            String extend = "";
            String json = "";
			String type = intent.getStringExtra("type");
			String id = intent.getStringExtra("detialid");
			if(intent.hasExtra("json"))
			json = intent.getStringExtra("json");
			if(json.length() > 0)
            {
                try {
                    XpxJSONObject jsonObject = new XpxJSONObject(json);
                    XpxJSONObject msg = jsonObject.getJSONObject("message");
                    title = msg.getString("title");
                    type = msg.getString("extend");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
			if(id.length() > 0)
			{
				if(type.equals(IntersakyData.CONVERSATION_TYPE_REPORT)) {
					WorkReportManager.getInstance().startDetial(AppActivityManager.getInstance().getCurrentActivity(),id);
				}
				else if(type.equals(IntersakyData.CONVERSATION_TYPE_LEAVE)) {
					LeaveManager.getInstance().startDetial(AppActivityManager.getInstance().getCurrentActivity(),id);
				}
				else if(type.equals(IntersakyData.CONVERSATION_TYPE_NOTICE)) {
					NoticeManager.getInstance().startDetial(AppActivityManager.getInstance().getCurrentActivity(),id);
				}
				else if(type.equals(IntersakyData.CONVERSATION_TYPE_VOTE)) {
					VoteManager.getInstance().startDetial(AppActivityManager.getInstance().getCurrentActivity(),id);
				}
				else if(type.equals(IntersakyData.CONVERSATION_TYPE_MESSAGE)) {
				    if(intent.hasExtra("newstart"))
				    chatUtils.mChatFunctions.startLeaveMessage(AppActivityManager.getInstance().getCurrentActivity(),id);
				    else
                    {
                        Contacts contacts = contactManager.mOrganization.getContact(id,mApp);
                        chatUtils.mChatFunctions.startChart(contacts);
                    }
				}
                else if(type.equals(IntersakyData.CONVERSATION_TYPE_TASK)) {
                    TaskManager.getInstance().startTaskDetial(AppActivityManager.getInstance().getCurrentActivity(),id);
                }
				else if(type.equals(IntersakyData.CONVERSATION_TYPE_IWEB_MAIL)) {
					MailManager.startMailMain(AppActivityManager.getInstance().getCurrentActivity());
				}
                else if(type.equals(IntersakyData.CONVERSATION_TYPE_IWEB_REMIND)) {
                    FunctionUtils.getInstance().showWebMessageRemind(AppActivityManager.getInstance().getCurrentActivity(),id,title,extend,mAccount.iscloud);
                }
				else if(type.equals(IntersakyData.CONVERSATION_TYPE_GROP_MESSAGE)) {
					JSONObject jsonObject = null;
					try {
						jsonObject = new JSONObject(intent.getStringExtra("json"));
						JSONObject msg = jsonObject.getJSONObject("message");
						Conversation conversation = conversationManager.getConversationByMessageid(Conversation.CONVERSATION_TYPE_GROP_MESSAGE,msg.getString("message_id"));
						if(conversation == null)
						{
							ConversationAsks.getFuncGropMessage(mApp,conversationManager.mOaMessageHandler,mApp.szImei,msg.getString("message_id"));
						}
						else
						{
							FunctionUtils.getInstance().showWebMessage(AppActivityManager.getInstance().getCurrentActivity(),conversation);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
                else if(intent.getStringExtra("type").equals(IntersakyData.CONVERSATION_TYPE_DOCUMENT))
                {
                    if(id.equals("download"))
                    {
                        DocumentManager.startDocumentMainDownload(AppActivityManager.getInstance().getCurrentActivity(),true);
                    }
                    else
                    {
                        DocumentManager.startDocumentMainDownload(AppActivityManager.getInstance().getCurrentActivity(),false);
                    }
                }

			}
		}

		@Override
		public void Open(Conversation conversation) {
			String type = conversation.mType;
			String id = conversation.detialId;
			if(id.length() > 0)
			{
				if(type.equals(IntersakyData.CONVERSATION_TYPE_REPORT)) {
					WorkReportManager.getInstance().startDetial(AppActivityManager.getInstance().getCurrentActivity(),id);
				}
				else if(type.equals(IntersakyData.CONVERSATION_TYPE_LEAVE)) {
					LeaveManager.getInstance().startDetial(AppActivityManager.getInstance().getCurrentActivity(),id);
				}
				else if(type.equals(IntersakyData.CONVERSATION_TYPE_NOTICE)) {
					NoticeManager.getInstance().startDetial(AppActivityManager.getInstance().getCurrentActivity(),id);
				}
				else if(type.equals(IntersakyData.CONVERSATION_TYPE_VOTE)) {
					VoteManager.getInstance().startDetial(AppActivityManager.getInstance().getCurrentActivity(),id);
				}
				else if(type.equals(IntersakyData.CONVERSATION_TYPE_MESSAGE)) {
                    chatUtils.startChat(AppActivityManager.getInstance().getCurrentActivity(),conversation);
				}
				else if(type.equals(IntersakyData.CONVERSATION_TYPE_GROP_MESSAGE)) {
					FunctionUtils.getInstance().showWebMessage(AppActivityManager.getInstance().getCurrentActivity(),conversation);
				}
                else if(type.equals(IntersakyData.CONVERSATION_TYPE_TASK)) {
                    TaskManager.getInstance().startTaskDetial(AppActivityManager.getInstance().getCurrentActivity(),id);
                }
                else if(type.equals(IntersakyData.CONVERSATION_TYPE_IWEB_APPROVE)) {
                    mApp.conversationManager.read(conversation);
                    mApp.functionUtils.commendWorkFlow.doCommend(AppActivityManager.getInstance().getCurrentActivity());
                }
                else if(type.equals(IntersakyData.CONVERSATION_TYPE_IWEB_MAIL)) {
                    mApp.conversationManager.read(conversation);
                    mApp.functionUtils.commendWorkFlow.doCommend(AppActivityManager.getInstance().getCurrentActivity());
                }
                else if(type.equals(IntersakyData.CONVERSATION_TYPE_IWEB_REMIND)) {
                    mApp.conversationManager.read(conversation);
                    FunctionUtils.getInstance().showWebMessageRemind(AppActivityManager.getInstance().getCurrentActivity(),conversation,mAccount.iscloud);
                }
			}
		}

		@Override
		public void Read(String type,String id) {
			Conversation conversation = new Conversation();
			conversation.mType = type;
			conversation.detialId = id;
            conversation.isRead = true;
			ConversationManager.getInstance().doRead(conversation);
		}

		@Override
		public void Asks(Handler handler, Intent intent) {
			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(intent.getStringExtra("data"));
				JSONObject msg = jsonObject.getJSONObject("message");
				if(intent.getStringExtra("type").equals(IntersakyData.CONVERSATION_TYPE_NOTICE)
						||intent.getStringExtra("type").equals(IntersakyData.CONVERSATION_TYPE_REPORT)||
						intent.getStringExtra("type").equals(IntersakyData.CONVERSATION_TYPE_VOTE)||
						intent.getStringExtra("type").equals(IntersakyData.CONVERSATION_TYPE_TASK)||
						intent.getStringExtra("type").equals(IntersakyData.CONVERSATION_TYPE_LEAVE))
				{
					if(handler != null)
						ConversationAsks.getFuncMessagesOne(mApp,handler,msg.getString("message_id")
								,mAccount.mRecordId,mAccount.mCompanyId);
				}
				else if(intent.getStringExtra("type").equals(IntersakyData.CONVERSATION_TYPE_GROP_MESSAGE))
				{
					if(handler != null)
						ConversationAsks.getFuncGropMessage(mApp,ConversationManager.getInstance().mOaMessageHandler,mApp.szImei);
				}
				else if(intent.getStringExtra("type").equals(IntersakyData.CONVERSATION_TYPE_IWEB_MAIL))
				{
					ConversationAsks.getFuncMessagesOne(mApp,handler,msg.getString("message_id")
							,mAccount.mRecordId,mAccount.mCompanyId,msg.getString("source_type"));
				}
				else if(intent.getStringExtra("type").equals(IntersakyData.CONVERSATION_TYPE_IWEB_APPROVE))
				{
					ConversationAsks.getFuncMessagesOne(mApp,handler,msg.getString("message_id")
							,mAccount.mRecordId,mAccount.mCompanyId,msg.getString("source_type"));
				}
                else if(intent.getStringExtra("type").equals(IntersakyData.CONVERSATION_TYPE_IWEB_REMIND))
                {
                    ConversationAsks.getFuncMessagesOne(mApp,handler,msg.getString("message_id")
                            ,mAccount.mRecordId,mAccount.mCompanyId,msg.getString("source_type"));
                }
				else if(intent.getStringExtra("type").equals(IntersakyData.CONVERSATION_TYPE_MESSAGE))
				{
					InterskyApplication.mApp.chatUtils.getLeaveMessage();
				}
				else if(intent.getStringExtra("type").equals(IntersakyData.CONVERSATION_TYPE_IWEB_SYSTEM))
				{

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};
//{"module_id":"F8704EA6-0BFD-4A0E-96EC-20428DA0498C","company_id":"","create_time":1598842734,"user_id":"","module":"iCloud","source_type":"iCloud[reminder]","source":"iCloud","type":"admin","message":{"extend":"客户公海","module_id":"F8704EA6-0BFD-4A0E-96EC-20428DA0498C","module":"iCloud","source_type":"iCloud[reminder]","message_id":"568736","source_id":"BEE3556C-60DC-4740-A585-6957C6F4F091","title":"测试2app","content":"测试2app"}}
    public NotificationData measureMessage(String data, String title) {
        try {
            SharedPreferences sharedPre = mApp.getSharedPreferences(LocalData.LOGIN_INFO, 0);
            boolean flag = sharedPre.getBoolean(LocalData.LOGIN_INFO_STATU, false);
            if (flag == false) {
                return null;
            }
            JSONObject jsonObject = new JSONObject(data);
            JSONObject msg = jsonObject.getJSONObject("message");
            if (msg.has("source_type")) {
                if (msg.getString("source_type").toLowerCase().contains("iweb[api]")) {
                    Intent intent = new Intent();
                    intent.putExtra("json", jsonObject.toString());
                    NotificationData notificationData = new NotificationData(data,title,title,
                            ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_IWEB_APP));
                    return notificationData;
                }
                else if (msg.getString("source_type").toLowerCase().contains("iweb")||msg.getString("source_type").toLowerCase().contains("icloud")) {
                    String token = "";
                    if (msg.getString("source_type").toLowerCase().contains("iweb[system]") && msg.getInt("module") == 100) {
                        token = msg.getString("module_id");
                        if (token.equals(NetUtils.getInstance().token) && NetUtils.getInstance().token.length() > 0) {
                            NotificationData notificationData = new NotificationData(data,title,title,
                                    ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_IWEB_SYSTEM));
                            return notificationData;
                        }
                        else {
                            return null;
                        }
                    }
                    else if (msg.getString("source_type").toLowerCase().equals("iweb[im]") || msg.getString("source_type").toLowerCase().equals("iweb[mail]")
                            || msg.getString("source_type").toLowerCase().equals("iweb[workflow]") || msg.getString("source_type").toLowerCase().equals("icloud[workflow]")
							|| msg.getString("source_type").toLowerCase().equals("icloud[mail]")|| msg.getString("source_type").toLowerCase().equals("icloud[im]")
                    ||msg.getString("source_type").toLowerCase().equals("icloud[reminder]")) {
                        NotificationData notificationData = null;
                        if(msg.getString("source_type").toLowerCase().equals("iweb[im]") || msg.getString("source_type").toLowerCase().equals("icloud[im]")) {

                            String name = "";
                            if(contactManager != null)
                            {
                                if(contactManager.mOrganization != null)
                                {
                                    if(!contactManager.mOrganization.getContact(msg.getString("source_id"),mApp).getName().equals("未知"))
                                    {
                                        name = contactManager.mOrganization.getContact(msg.getString("source_id"),mApp).getName();
                                    }
                                }
                            }

                            if(name.length() > 0)
                            {
                                notificationData=     new NotificationData(data,title,name
                                        +":"+msg.getString("content"),
                                        ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_MESSAGE));
                            }
                            else
                            {
                                notificationData=     new NotificationData(data,title,msg.getString("content"),
                                        ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_MESSAGE));
                            }
                            if(chatUtils.showContacts != null)
                            {
                                if(chatUtils.showContacts.mRecordid.equals(msg.getString("source_id")))
                                {
                                    notificationData.show = false;
                                }
                            }
                            notificationData.detialid = msg.getString("source_id");
                        }
                        else if(msg.getString("source_type").toLowerCase().equals("iweb[workflow]") || msg.getString("source_type").toLowerCase().equals("icloud[workflow]"))
                        {
                            notificationData=     new NotificationData(data,title, msg.getString("content"),
                                    ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_IWEB_APPROVE));
                        }
                        else if(msg.getString("source_type").toLowerCase().equals("iweb[mail]")|| msg.getString("source_type").toLowerCase().equals("icloud[mail]"))
                        {
                            notificationData=     new NotificationData(data,title,msg.getString("content"),
                                    ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_IWEB_MAIL));
                        }
                        else if(msg.getString("source_type").toLowerCase().equals("icloud[reminder]"))
                        {
                            notificationData = new NotificationData(data,title,msg.getString("content"),
                                    ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_IWEB_REMIND));
                            notificationData.detialid = msg.getString("module_id");
                        }

                        return notificationData;
                    }
                }
                else if (msg.getString("source_type").equals("oa")) {
                    NotificationData notificationData = null;
                    String type = msg.getString("module");
                    if(type.toLowerCase().contains("report"))
                        notificationData = new NotificationData(data,title,msg.getString("content"),
                                ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_REPORT));
                    else if(type.toLowerCase().contains("leave"))
                        notificationData = new NotificationData(data,title,msg.getString("content"),
                                ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_LEAVE));
                    else if(type.toLowerCase().contains("vote"))
                        notificationData = new NotificationData(data,title,msg.getString("content"),
                                ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_VOTE));
                    else if(type.toLowerCase().contains("notice"))
                        notificationData = new NotificationData(data,title,msg.getString("content"),
                                ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_NOTICE));
                    else if(type.toLowerCase().contains("task"))
                        notificationData = new NotificationData(data,title,msg.getString("content"),
                                ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_TASK));
                    else
                        notificationData = new NotificationData(data,title,msg.getString("content"),
                                ConversationManager.getInstance().getChannel(IntersakyData.CONVERSATION_TYPE_GROP_MESSAGE));
                    return notificationData;
                }
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public BrodcastData measureMessageIntent(NotificationData ndata,String data, String title) {
        Intent intent = new Intent(ndata.channel.id);
        intent.putExtra("data",data);
        intent.putExtra("type",ndata.channel.registername);
        BrodcastData brodcastData = new BrodcastData(ndata.channel.id,intent);
        return brodcastData;
    }

    public ChatUtils.ChatFunctions chatFunctions = new ChatUtils.ChatFunctions() {
		@Override
		public ArrayList<Conversation> getMessages(String detialid) {
			return conversationManager.getDetialList(IntersakyData.CONVERSATION_TYPE_MESSAGE,detialid);
		}

		@Override
		public Conversation getMessages(String detialid, String recordid) {
			return conversationManager.getConversation(IntersakyData.CONVERSATION_TYPE_MESSAGE,detialid,recordid);
		}

		@Override
		public String getFileUrl(String id) {
			return ImAsks.imFileDownloadUrl(id);
		}

		@Override
		public void sendFile(Context context, File file) {
            contactManager.mOrganization.showContacts = contactManager.mOrganization.organizationContacts;
			Intent intent = new Intent(context, ContactsListActivity.class);
			intent.putExtra("path",file.getPath());
			context.startActivity(intent);
		}

		@Override
		public void sendText(Context context, String content) {
            contactManager.mOrganization.showContacts = contactManager.mOrganization.organizationContacts;
			Intent intent = new Intent(context, ContactsListActivity.class);
			intent.putExtra("text",content);
			context.startActivity(intent);
		}

		@Override
		public void scanCode(Context context,String url) {
			Intent intent = new Intent(context, WebMessageActivity.class);
			intent.putExtra("url", url);
			intent.putExtra("showshare", false);
			context.startActivity(intent);
		}

		@Override
		public String measureCode(Bitmap bitmap) {
			return QRCodeDecoder.syncDecodeQRCode(bitmap);
		}

		@Override
		public void sendMessage(Context context, Conversation msg) {
			if(InterskyApplication.mApp.conversationManager.getConversation(msg) == null)
			{
				InterskyApplication.mApp.conversationManager.doAdd(msg);
				ImAsks.sendMsg(context,chatUtils.messageHandler, SendMessageHandler.SEND_MESSAGE_SUCCESS,msg, Calendar.getInstance().getTimeInMillis(),0);
			}
			else
			{
				InterskyApplication.mApp.conversationManager.updata(msg);
				ImAsks.sendMsg(context,chatUtils.messageHandler, SendMessageHandler.SEND_MESSAGE_SUCCESS,msg,Calendar.getInstance().getTimeInMillis(),0);
			}
		}

		@Override
		public void sendMessage(Context context, Conversation msg, File file) {
			if(InterskyApplication.mApp.conversationManager.getConversation(msg) == null)
			{
				InterskyApplication.mApp.conversationManager.doAdd(msg);
				ImAsks.uploadFile(context,chatUtils.messageHandler, SendMessageHandler.SEND_UPLOADFILE_SUCCESS,msg);
			}
			else
			{
				InterskyApplication.mApp.conversationManager.updata(msg);
				ImAsks.uploadFile(context,chatUtils.messageHandler, SendMessageHandler.SEND_UPLOADFILE_SUCCESS,msg);
			}
		}

		@Override
		public void sendMessageSuccess(Context context, Conversation msg) {
			msg.issend = Conversation.MESSAGE_STATAUE_SUCCESS;
			InterskyApplication.mApp.conversationManager.updata(msg);
			InterskyApplication.mApp.chatUtils.updataMessage(msg);
		}

		@Override
		public void sendMessageFail(Context context, Conversation msg) {
			msg.issend = Conversation.MESSAGE_STATAUE_FAIL;
			InterskyApplication.mApp.conversationManager.updata(msg);
			InterskyApplication.mApp.chatUtils.updataMessage(msg);
		}

		@Override
		public void delete(Context context, Conversation msg, int index) {
			ConversationManager.getInstance().doDelete(msg);
		}

		@Override
		public PermissionResult takePhoto(Activity context, Contacts contacts) {
			return mFileUtils.checkPermissionTakePhoto(context, mFileUtils.pathUtils.getfilePath("/im" + "/" + contacts.mRecordid));
		}

		@Override
		public void slectPhoto(Context context) {
			mFileUtils.getPhotos(context,false,9, "intersky.chat.view.activity.ChatActivity",ChatUtils.ACTION_CHAT_PHOTO_SELECT);
		}

		@Override
		public void showContactHead(Context context, Contacts contacts) {
			Intent intent = new Intent(context, ContactsDetialActivity.class);
            Contacts contacts1 = contactManager.mOrganization.getContact(contacts.mRecordid,mApp);
            if(contacts1 != null)
            {
                intent.putExtra("contacts",contacts1);
            }
            else
            {
                intent.putExtra("contacts",contacts);
            }
			context.startActivity(intent);
		}

		@Override
		public void readMessages(String id) {
			Conversation conversation = new Conversation();
			conversation.mType = IntersakyData.CONVERSATION_TYPE_MESSAGE;
			conversation.detialId = id;
			conversationManager.doRead(conversation);
		}

		@Override
		public String photoResult(int requestCode, int resultCode, Intent data) {
			if(requestCode == Actions.TAKE_PHOTO)
			{
				File mFile = new File(mFileUtils.takePhotoPath);
				return  mFile.getPath();
			}
			return "";
		}

		@Override
		public Intent openFile(File file) {
			return FileUtils.mFileUtils.openfile(file);
		}

		@Override
		public boolean praseFile(NetObject netObject) {
			 return ImPrase.checksuccessUploadFile(netObject);
		}

		@Override
		public void sendMessage(Context context,NetObject netObject) {
			InterskyApplication.mApp.conversationManager.doUpdata((Conversation) netObject.item);
			ImAsks.sendMsg(context,chatUtils.messageHandler, SendMessageHandler.SEND_MESSAGE_SUCCESS, (Conversation) netObject.item,Calendar.getInstance().getTimeInMillis(),0);
		}

		@Override
		public void getLeaveMessage(Context context, Handler handler) {
			ImAsks.getImMessage(context,handler, LeaveMessageHandler.GET_LEAVE_MSG_SUCCESS);
		}

		@Override
		public void praseLeaveMessage(NetObject netObject) {
			ImPrase.praseImMsg(mApp,netObject);
		}

		@Override
		public void setNotificationShow() {

		}

        @Override
        public void startLeaveMessage(Context context, String id) {
		    Contacts contacts = contactManager.mOrganization.getContact(id,mApp);
            BaseActivity baseActivity = (BaseActivity) context;
            baseActivity.waitDialog.show();
            ImAsks.getImMessage(context,chatUtils.mLeaveMessageHandler, LeaveMessageHandler.GET_LEAVE_MSG_START_SUCCESS,contacts);
        }

        @Override
        public void openFile(Context context, Conversation msg) {
		    Attachment attachment = new Attachment();
            attachment.mRecordid = AppUtils.getguid();
            attachment.mName = msg.sourceName;
            attachment.mSize = msg.sourceSize;
            if(msg.sourcePath.length() == 0)
            {
                msg.sourcePath = InterskyApplication.mApp.mFileUtils.pathUtils.getfilePath("/im/file/"
                        +msg.detialId+"/"+msg.sourceName);
            }
            attachment.mPath = msg.sourcePath;
            attachment.mUrl = ImAsks.imFileDownloadUrl(msg.sourceId);
            mFileUtils.startAttachment(context,attachment);
        }

        @Override
        public void openLocation(Context context, Conversation msg) {
            try {
                JSONObject jsonObject = new JSONObject(msg.sourceName);
                jsonObject.put("path",msg.sourcePath);
                mMapManager.startBigMap(context,jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void praseFilesize(Context context, NetObject netObject) {
            Conversation conversation = (Conversation) netObject.item;
            conversation.sourceSize = Long.valueOf(netObject.result);
            conversationManager.updata(conversation);
        }

        @Override
        public void getFilesize(Context context, Conversation conversation) {
            ImAsks.getImFileInfo(context,chatUtils.messageHandler,conversation, SendMessageHandler.SEND_UPDATA_SIZE_SUCCESS);
        }

        @Override
        public void startChart(Contacts contacts) {
            BaseActivity baseActivity = (BaseActivity) AppActivityManager.getInstance().getCurrentActivity();
            baseActivity.waitDialog.hide();
            ChatUtils.getChatUtils().startChat(AppActivityManager.getInstance().getCurrentActivity(),contacts);
        }

        @Override
        public String getGlideUrl(String id) {
            return ImAsks.imFileDownloadUrlg(id);
        }

        @Override
        public void updataChatView(Conversation conversation) {
		    Intent intent = new Intent(ChatUtils.ACTION_UPDATA_CHAT_MESSAGE);
            intent.putExtra("msg",conversation);
            mApp.sendBroadcast(intent);
        }

        @Override
        public void checkHead(Attachment attachment) {

        }

        @Override
        public String checkHeadResult(NetObject netObject) {
            return null;
        }

        @Override
        public String getHeadIcom(String id) {
            return null;
        }

    };

    public DocumentManager.NotificationOperation notificationOperation = new DocumentManager.NotificationOperation()
    {

        @Override
        public void showProgress(String title, String content, int max, int min,String id) {

            try {
                XpxJSONObject jsonObject = new XpxJSONObject();
                XpxJSONObject jo = new XpxJSONObject();
                jsonObject.put("message",jo);
                jo.put("module",IntersakyData.CONVERSATION_TYPE_DOCUMENT);
                jo.put("module_id",id);
                downloadOper.creatNotification(title,content,max,min,jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void showMesage(String title, String content,String id) {
            try {
                XpxJSONObject jsonObject = new XpxJSONObject();
                XpxJSONObject jo = new XpxJSONObject();
                jsonObject.put("message",jo);
                jo.put("module",IntersakyData.CONVERSATION_TYPE_DOCUMENT);
                jo.put("module_id",id);
                downloadOper.creatNotification(title,content,jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void showCancle() {
            downloadOper.cancleNotification();
        }
    };

    public UpDataManager.NotificationOperation updataOperation = new UpDataManager.NotificationOperation() {
        @Override
        public void showProgress(String title, String content, long max, long min) {
            int imax = 1000;
            int ifinish = 0;
            if(max > 0)
            {
                ifinish = (int) (imax*min/max);
            }
            updataOper.creatNotificationNopen(title,content,imax,ifinish);
        }

        @Override
        public void showMesage(String title, String content) {
            updataOper.creatNotificationNopen(title,content);
        }

        @Override
        public void showCancle() {
            updataOper.cancleNotification();
        }
    };

    public FileUtils.FileOperation fileOperation = new FileUtils.FileOperation() {
        @Override
        public void share(BaseActivity context,String title,String path) {
            share.doShare(context,"",title,new File(path));
        }

        @Override
        public void sendDocument(Context context,String path) {
            File file = new File(path);
            ArrayList<Attachment> attachments = new ArrayList<Attachment>();
            Attachment attachment = new Attachment();
            attachment.mRecordid = AppUtils.getguid();
            attachment.mSize = file.length();
            attachment.mName = file.getName();
            attachment.mPath = file.getPath();
            attachments.add(attachment);
            DocumentManager.getInstance().setPositionUpload(context,attachments);
        }

        @Override
        public void sendChat(Context context,String path) {
            chatUtils.mChatFunctions.sendFile(context,new File(path));
        }

        @Override
        public void sendMail(Context context,String path) {
            mailManager.sendMail(context,new File(path));
        }
    };

    public MapManager.MapFunctions functions = new MapManager.MapFunctions() {

        @Override
        public void sendContact(Context context, Intent intent) {
            contactManager.mOrganization.showContacts = contactManager.mOrganization.organizationContacts;
            Intent intent1 = new Intent(context, ContactsListActivity.class);
            intent1.putExtra("json", intent.getStringExtra("json"));
            context.startActivity(intent1);
        }
    };

    @Override

    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);

        MultiDex.install(this);

    }


    public boolean initdata()
    {
        SharedPreferences sharedPre = mApp.getSharedPreferences(LocalData.SETTING_INFO, 0);
        boolean dark = sharedPre.getBoolean(LocalData.SETTING_DARK,false);
//        if(dark == false)
//        {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }
//        else
//        {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
//        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        return dark;
    }

    public boolean getDarkType() {
        SharedPreferences sharedPre = mApp.getSharedPreferences(LocalData.SETTING_INFO, 0);
        boolean dark = sharedPre.getBoolean(LocalData.SETTING_DARK,false);
        return dark;
    }

    public void reCreatAll() {
        //appActivityManager.reCreatAll();
    }

}
