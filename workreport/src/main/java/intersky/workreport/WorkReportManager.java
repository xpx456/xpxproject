package intersky.workreport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import intersky.appbase.AppActivityManager;
import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Register;
import intersky.oa.OaUtils;
import intersky.workreport.asks.WorkReportAsks;
import intersky.workreport.entity.Report;
import intersky.workreport.handler.HitHandler;
import intersky.workreport.view.activity.ReportDetialActivity;
import intersky.workreport.view.activity.WorkReportActivity;
import intersky.xpxnet.net.NetUtils;

public class WorkReportManager {

    public static final int TYPE_DAY = 1;
    public static final int TYPE_WEEK = 2;
    public static final int TYPE_MONTH = 3;
    public static final int MAX_PIC_COUNT = 9;
    public static final String REPORT_INFO = "report_info";
    public static final String REPORTSENDER = "reportsender";
    public static final String REPORTCOPYER = "reportcopyer";
    public static final String ACTION_REPORT_UPATE_SENDER = "ACTION_REPORT_UPATE_SENDER";
    public static final String ACTION_REPORT_UPATE_COPYER = "ACTION_REPORT_UPATE_COPYER";
    public static final String ACTION_REPORT_ADDPICTORE = "ACTION_REPORT_ADDPICTORE";
    public static final String ACTION_WORK_REPORT_ACTION_UPDATA_HIT = "ACTION_WORK_REPORT_ACTION_UPDATA_HIT";
    public static final String ACTION_WORK_REPORT_UPDATE = "ACTION_WORK_REPORT_UPDATE";
    public static final String ACTION_WORK_REPORT_DELETE = "ACTION_WORK_REPORT_DELETE";
    public static final String ACTION_WORK_REPORT_ADD = "ACTION_WORK_REPORT_ADD";
    public static final String ACTION_WORK_REPORT_CONVERSATION_UPDATE= "ACTION_WORK_REPORT_CONVERSATION_UPDATE";
    public static final String ACTION_SET_WORK_CONTENT5 = "ACTION_SET_WORK_CONTENT5";
    public static final String ACTION_SET_WORK_CONTENT1 = "ACTION_SET_WORK_CONTENT1";
    public static final String ACTION_SET_WORK_CONTENT2 = "ACTION_SET_WORK_CONTENT2";
    public static final String ACTION_SET_WORK_CONTENT3 = "ACTION_SET_WORK_CONTENT3";
    public static final String ACTION_SET_WORK_CONTENT4 = "ACTION_SET_WORK_CONTENT4";
    public static volatile WorkReportManager mWorkReportManager;
    public Context context;
    public int hit1 = 0;
    public int hit2 = 0;
    public int day1 = 0;
    public int week1 = 0;
    public int month1 = 0;
    public int day2 = 0;
    public int week2 = 0;
    public int month2 = 0;
    public HitHandler hitHandler;
    public Context workContext;
    public Register register;
    public OaUtils oaUtils;
    public static WorkReportManager init(OaUtils oaUtils,Context context) {

        if (mWorkReportManager == null) {
            synchronized (WorkReportManager.class) {
                if (mWorkReportManager == null) {
                    mWorkReportManager = new WorkReportManager(oaUtils,context);
                }
                else
                {
                    mWorkReportManager.context = context;
                    mWorkReportManager.oaUtils = oaUtils;
                    mWorkReportManager.hitHandler = new HitHandler(context);
                }
            }
        }
        return mWorkReportManager;
    }

    public static WorkReportManager getInstance() {
        return mWorkReportManager;
    }

    public WorkReportManager(OaUtils oaUtils,Context context) {
        this.oaUtils = oaUtils;
        this.context = context;
        hitHandler = new HitHandler(context);
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public void upDataHit(Context context) {

        if(context != null)
        WorkReportAsks.gethit(context,hitHandler);
    }

    public String getSenders()
    {
        SharedPreferences sharedPre = context.getSharedPreferences(WorkReportManager.REPORT_INFO, 0);
        return sharedPre.getString(WorkReportManager.REPORTSENDER+ oaUtils.mAccount.mRecordId
                + oaUtils.service.sAddress+oaUtils.service.sPort,"");
    }

    public String getCopyers()
    {
        SharedPreferences sharedPre =context.getSharedPreferences(WorkReportManager.REPORT_INFO, 0);
        return sharedPre.getString(WorkReportManager.REPORTCOPYER+oaUtils.mAccount.mRecordId
                +oaUtils.service.sAddress+oaUtils.service.sPort,"");
    }

    public void sendReportUpdate(String report) {
        Intent intent = new Intent(ACTION_WORK_REPORT_UPDATE);
        intent.putExtra("reportid",report);
        context.sendBroadcast(intent);
    }

    public void sendReportDelete(String report) {
        Intent intent = new Intent(ACTION_WORK_REPORT_DELETE);
        intent.putExtra("reportid",report);
        context.sendBroadcast(intent);
    }


    public void sendReportAdd() {
        Intent intent = new Intent(ACTION_WORK_REPORT_ADD);
        context.sendBroadcast(intent);
    }

    public void startDetial(Context context,String recordid) {
        BaseActivity baseActivity = (BaseActivity) AppActivityManager.getInstance().getCurrentActivity();
        baseActivity.waitDialog.show();
        Report report = new Report();
        report.mRecordid = recordid;
        workContext = context;
        WorkReportAsks.getReportDetial(context,hitHandler,report);
    }

    public void startDetial(Context context,Report report) {
        Intent intent = new Intent(context, ReportDetialActivity.class);
        intent.putExtra("report",report);
        context.startActivity(intent);
    }

    public void startReportMain(Context context) {
        Intent intent = new Intent(context, WorkReportActivity.class);
        context.startActivity(intent);
    }
}
