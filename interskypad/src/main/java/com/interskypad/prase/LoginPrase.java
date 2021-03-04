package com.interskypad.prase;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.interskypad.R;
import com.interskypad.asks.LoginAsks;
import com.interskypad.handler.LoginHandler;
import com.interskypad.view.InterskyPadApplication;

import org.json.JSONException;

import intersky.apputils.AppUtils;
import intersky.filetools.FileUtils;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class LoginPrase {

    public static void praseLogin(Context context, NetObject netObject, Handler handler) {
        String json = netObject.result;
        try {
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return;
            }

            XpxJSONObject jObject;
            jObject = new XpxJSONObject(json);
            XpxJSONObject aproject = jObject.getJSONObject("project");
            boolean isouter = jObject.getBoolean("isouter",false);
            jObject = (XpxJSONObject) jObject.getJSONObject("user");
            if(jObject.getString("name") != null)
                InterskyPadApplication.mApp.mAccount.mUserName = jObject.getString("name");
            else
                InterskyPadApplication.mApp.mAccount.mUserName = InterskyPadApplication.mApp.mAccount.mAccountId;
            InterskyPadApplication.mApp.mAccount.mRecordId = jObject.getString("recordid");

            if(jObject.getString("realname") != null)
            {
                InterskyPadApplication.mApp.mAccount.mRealName = jObject.getString("realname");
            }
            InterskyPadApplication.mApp.mAccount.mRoleId = jObject.getString("RoleID");
            InterskyPadApplication.mApp.mAccount.mOrgId = jObject.getString("organizationid");
            InterskyPadApplication.mApp.mAccount.mOrgName = jObject.getString("organizationname");

            if (jObject.getInt("sex",0) == 0)
                InterskyPadApplication.mApp.mAccount.mSex = InterskyPadApplication.mApp.getString(R.string.sex_male);
            else if (jObject.getInt("sex",0) == 1)
                InterskyPadApplication.mApp.mAccount.mSex = InterskyPadApplication.mApp.getString(R.string.sex_female);
            else
                InterskyPadApplication.mApp.mAccount.mSex = InterskyPadApplication.mApp.getString(R.string.sex_unknowfemale);
            InterskyPadApplication.mApp.mAccount.mPhone = jObject.getString("phone");
            InterskyPadApplication.mApp.mAccount.mMobile = jObject.getString("mobile");
            InterskyPadApplication.mApp.mAccount.mFax = jObject.getString("fax");
            InterskyPadApplication.mApp.mAccount.mEmail = jObject.getString("email");
            InterskyPadApplication.mApp.mAccount.mUCid = jObject.getString("companyid");
            InterskyPadApplication.mApp.mAccount.isAdmin = jObject.getBoolean("isadmin",false);
            XpxJSONObject xpxJsonObject = new XpxJSONObject(json);
            XpxJSONObject project = xpxJsonObject.getJSONObject("project");
            XpxJSONObject jo = (XpxJSONObject) project.getJSONObject("orgs");
            XpxJSONObject jo2 = (XpxJSONObject) project.getJSONObject("cloud");
            XpxJSONObject jo3 = (XpxJSONObject) project.getJSONObject("app");
            XpxJSONObject jo4 = (XpxJSONObject) project.getJSONObject("user");
            XpxJSONObject jo5 = (XpxJSONObject) project.getJSONObject("cloud");
            InterskyPadApplication.mApp.mAccount.mCompanyId = jo2.getString("companyid");
            InterskyPadApplication.mApp.mAccount.mCompanyName = jo3.getString("company");
            InterskyPadApplication.mApp.mAccount.mManagerId = jo4.getString("mgr");
            InterskyPadApplication.mApp.mAccount.mCloundAdminId = jo5.getString("cloudadminid");
            InterskyPadApplication.mApp.mAccount.mPosition = jo4.getString("position");
            InterskyPadApplication.mApp.mAccount.cloudServer = jo5.getString("cloudserver");
            InterskyPadApplication.mApp.mFileUtils = FileUtils.init(InterskyPadApplication.mApp,InterskyPadApplication.mApp.mgetProvidePath,null,null);
            InterskyPadApplication.mApp.mFileUtils.pathUtils.setAppBase("/interskypad"+"/"+InterskyPadApplication.mApp.mService.sAddress);
            InterskyPadApplication.mApp.isLogin = true;
            handler.sendEmptyMessage(LoginHandler.START_MAIN);

        } catch (JSONException e) {
            e.printStackTrace();
            Message msg = new Message();
            msg.what = LoginAsks.LOGIN_FAIL;
            msg.obj = json;
            handler.sendMessage(msg);
        }
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
