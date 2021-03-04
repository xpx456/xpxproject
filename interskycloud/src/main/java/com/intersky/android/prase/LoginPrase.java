package com.intersky.android.prase;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.intersky.R;
import com.intersky.android.asks.LoginAsks;
import com.intersky.android.entity.IntersakyData;
import com.intersky.android.handler.LoginHandler;
import com.intersky.android.view.InterskyApplication;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;

import intersky.appbase.entity.Account;
import intersky.apputils.AppUtils;
import intersky.chat.ChatUtils;
import intersky.chat.ContactManager;
import intersky.conversation.ConversationManager;
import intersky.conversation.NotifictionManager;
import intersky.document.DocumentManager;
import intersky.filetools.FileUtils;
import intersky.filetools.handler.DownloadThreadHandler;
import intersky.function.FunctionUtils;
import intersky.json.XpxJSONObject;
import intersky.mail.MailManager;
import intersky.oa.OaUtils;
import intersky.schedule.ScheduleManager;
import intersky.schedule.view.activity.ScheduleActivity;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class LoginPrase {

    public static boolean praseLogin(Context context, NetObject net, Handler mHandler, Account account)
    {
        String json = net.result;
        try {
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return false;
            }

            XpxJSONObject jObject;
            jObject = new XpxJSONObject(json);
            account.isouter = jObject.getBoolean("isouter",false);
            account.iscloud = jObject.getBoolean("icloud",false);
            account.logininfo = json;
            jObject = (XpxJSONObject) jObject.getJSONObject("user");
            if(jObject.getString("name") != null)
                InterskyApplication.mApp.mAccount.mUserName = jObject.getString("name");
            else
                InterskyApplication.mApp.mAccount.mUserName = InterskyApplication.mApp.mAccount.mAccountId;
            InterskyApplication.mApp.mAccount.mRecordId = jObject.getString("recordid");

            if(jObject.getString("realname") != null)
            {
                InterskyApplication.mApp.mAccount.mRealName = jObject.getString("realname");
            }
            InterskyApplication.mApp.mAccount.serviceid = InterskyApplication.mApp.mService.sAddress;
            InterskyApplication.mApp.mAccount.mRoleId = jObject.getString("RoleID");
            InterskyApplication.mApp.mAccount.mOrgId = jObject.getString("organizationid");
            InterskyApplication.mApp.mAccount.mOrgName = jObject.getString("organizationname");

            if (jObject.getInt("sex",0) == 0)
                InterskyApplication.mApp.mAccount.mSex = InterskyApplication.mApp.getString(R.string.sex_male);
            else if (jObject.getInt("sex",0) == 1)
                InterskyApplication.mApp.mAccount.mSex = InterskyApplication.mApp.getString(R.string.sex_female);
            else
                InterskyApplication.mApp.mAccount.mSex = InterskyApplication.mApp.getString(R.string.sex_unknowfemale);
            InterskyApplication.mApp.mAccount.mPhone = jObject.getString("phone");
            InterskyApplication.mApp.mAccount.mMobile = jObject.getString("mobile");
            InterskyApplication.mApp.mAccount.mFax = jObject.getString("fax");
            InterskyApplication.mApp.mAccount.mEmail = jObject.getString("email");
            InterskyApplication.mApp.mAccount.isAdmin = jObject.getBoolean("isadmin",false);
            XpxJSONObject xpxJsonObject = new XpxJSONObject(json);
            XpxJSONObject project = xpxJsonObject.getJSONObject("project");
            XpxJSONObject jo = (XpxJSONObject) project.getJSONObject("orgs");
            XpxJSONObject jo2 = (XpxJSONObject) project.getJSONObject("cloud");
            XpxJSONObject jo3 = (XpxJSONObject) project.getJSONObject("app");
            XpxJSONObject jo4 = (XpxJSONObject) project.getJSONObject("user");
            XpxJSONObject jo5 = (XpxJSONObject) project.getJSONObject("cloud");
            InterskyApplication.mApp.mAccount.mCompanyId = jo2.getString("companyid");
            InterskyApplication.mApp.mAccount.mUCid = jObject.getString("companyid");
            if(InterskyApplication.mApp.mAccount.mUCid.length() == 0)
            {
                //InterskyApplication.mApp.mAccount.mUCid = jo2.getString("companyid");
            }

            InterskyApplication.mApp.mAccount.mCompanyName = jo3.getString("company");
            InterskyApplication.mApp.mAccount.mManagerId = jo4.getString("mgr");
            InterskyApplication.mApp.mAccount.mCloundAdminId = jo5.getString("cloudadminid");
            InterskyApplication.mApp.mAccount.mPosition = jo4.getString("position");
            InterskyApplication.mApp.mAccount.cloudServer = jo5.getString("cloudserver");
            if(InterskyApplication.istest)
            {
                InterskyApplication.mApp.mAccount.mCompanyId = "10C1EFA4-C4AE-53C9-6F76-3B4A85C50E1A";
                InterskyApplication.mApp.mAccount.mCompanyName = "宁波畅想软件股份有限公司";
                InterskyApplication.mApp.mAccount.cloudServer = "oa.intersky.com.cn";
                InterskyApplication.mApp.mAccount.mCloundAdminId = "E8B80597-5D32-4097-99B5-78A8BA4023E3";
            }
//            if(InterskyApplication.mApp.mAccount.project != null)
//            {
//                if(!InterskyApplication.mApp.mAccount.project.toString().equals(project.toString()))
//                {
//                    InterskyApplication.mApp.mAccount.project = project;
//                    InterskyApplication.mApp.setModules();
//                }
//                else
//                {
//                    InterskyApplication.mApp.mAccount.project = project;
//                    InterskyApplication.mApp.updataSample();
//                }
//            }
//            else
//            {
//                InterskyApplication.mApp.mAccount.project = project;
//                InterskyApplication.mApp.updataSample();
//            }
            InterskyApplication.mApp.mAccount.project = project;
            InterskyApplication.mApp.setModules();
            mHandler.sendEmptyMessage(LoginHandler.START_MAIN);
            return true;

        } catch (JSONException e) {
            e.printStackTrace();
            Message msg = new Message();
            msg.what = LoginAsks.LOGIN_FAIL;
            msg.obj = json;
            mHandler.sendMessage(msg);
            return false;
        }
    }

    public static boolean praseDate(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        return true;
    }

}
