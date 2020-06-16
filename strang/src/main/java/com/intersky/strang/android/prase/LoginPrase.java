package com.intersky.strang.android.prase;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.intersky.strang.R;
import com.intersky.strang.android.asks.LoginAsks;
import com.intersky.strang.android.handler.LoginHandler;
import com.intersky.strang.android.view.StrangApplication;

import org.json.JSONException;

import intersky.appbase.entity.Account;
import intersky.apputils.AppUtils;
import intersky.json.XpxJSONObject;
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
                StrangApplication.mApp.mAccount.mUserName = jObject.getString("name");
            else
                StrangApplication.mApp.mAccount.mUserName = StrangApplication.mApp.mAccount.mAccountId;
            StrangApplication.mApp.mAccount.mRecordId = jObject.getString("recordid");

            if(jObject.getString("realname") != null)
            {
                StrangApplication.mApp.mAccount.mRealName = jObject.getString("realname");
            }
            StrangApplication.mApp.mAccount.serviceid = StrangApplication.mApp.mService.sAddress;
            StrangApplication.mApp.mAccount.mRoleId = jObject.getString("RoleID");
            StrangApplication.mApp.mAccount.mOrgId = jObject.getString("organizationid");
            StrangApplication.mApp.mAccount.mOrgName = jObject.getString("organizationname");

            if (jObject.getInt("sex",0) == 0)
                StrangApplication.mApp.mAccount.mSex = StrangApplication.mApp.getString(R.string.sex_male);
            else if (jObject.getInt("sex",0) == 1)
                StrangApplication.mApp.mAccount.mSex = StrangApplication.mApp.getString(R.string.sex_female);
            else
                StrangApplication.mApp.mAccount.mSex = StrangApplication.mApp.getString(R.string.sex_unknowfemale);
            StrangApplication.mApp.mAccount.mPhone = jObject.getString("phone");
            StrangApplication.mApp.mAccount.mMobile = jObject.getString("mobile");
            StrangApplication.mApp.mAccount.mFax = jObject.getString("fax");
            StrangApplication.mApp.mAccount.mEmail = jObject.getString("email");
            StrangApplication.mApp.mAccount.isAdmin = jObject.getBoolean("isadmin",false);
            XpxJSONObject xpxJsonObject = new XpxJSONObject(json);
            XpxJSONObject project = xpxJsonObject.getJSONObject("project");
            XpxJSONObject jo = (XpxJSONObject) project.getJSONObject("orgs");
            XpxJSONObject jo2 = (XpxJSONObject) project.getJSONObject("cloud");
            XpxJSONObject jo3 = (XpxJSONObject) project.getJSONObject("app");
            XpxJSONObject jo4 = (XpxJSONObject) project.getJSONObject("user");
            XpxJSONObject jo5 = (XpxJSONObject) project.getJSONObject("cloud");
            StrangApplication.mApp.mAccount.mCompanyId = jo2.getString("companyid");
            StrangApplication.mApp.mAccount.mUCid = jObject.getString("companyid");
            if(StrangApplication.mApp.mAccount.mUCid.length() == 0)
            {
                //StrangApplication.mApp.mAccount.mUCid = jo2.getString("companyid");
            }

            StrangApplication.mApp.mAccount.mCompanyName = jo3.getString("company");
            StrangApplication.mApp.mAccount.mManagerId = jo4.getString("mgr");
            StrangApplication.mApp.mAccount.mCloundAdminId = jo5.getString("cloudadminid");
            StrangApplication.mApp.mAccount.mPosition = jo4.getString("position");
            StrangApplication.mApp.mAccount.cloudServer = jo5.getString("cloudserver");
            if(StrangApplication.istest)
            {
                StrangApplication.mApp.mAccount.mCompanyId = "10C1EFA4-C4AE-53C9-6F76-3B4A85C50E1A";
                StrangApplication.mApp.mAccount.mCompanyName = "宁波畅想软件股份有限公司";
                StrangApplication.mApp.mAccount.cloudServer = "oa.intersky.com.cn";
                StrangApplication.mApp.mAccount.mCloundAdminId = "E8B80597-5D32-4097-99B5-78A8BA4023E3";
            }
//            if(StrangApplication.mApp.mAccount.project != null)
//            {
//                if(!StrangApplication.mApp.mAccount.project.toString().equals(project.toString()))
//                {
//                    StrangApplication.mApp.mAccount.project = project;
//                    StrangApplication.mApp.setModules();
//                }
//                else
//                {
//                    StrangApplication.mApp.mAccount.project = project;
//                    StrangApplication.mApp.updataSample();
//                }
//            }
//            else
//            {
//                StrangApplication.mApp.mAccount.project = project;
//                StrangApplication.mApp.updataSample();
//            }
            StrangApplication.mApp.mAccount.project = project;
            StrangApplication.mApp.setModules();
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

    public static String getFailMessage(String json) {
        String message = "";
        try {
            XpxJSONObject jsonobj = new XpxJSONObject(json);
            message = jsonobj.getString("msg");
            if (message.length() == 0) {
                message = "连接服务器失败";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }
}
