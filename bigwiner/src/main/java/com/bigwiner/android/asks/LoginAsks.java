package com.bigwiner.android.asks;

import android.content.Context;
import android.os.Handler;

import com.bigwiner.android.entity.Company;
import com.bigwiner.android.view.BigwinerApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.entity.Account;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.chat.ContactManager;
import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostJsonNetTask;
import intersky.xpxnet.net.nettask.PostNetTask;

public class LoginAsks {
    public static final String PUSH_PATH = "api/v1/Ios";
    public static final String PUSH_LOGIN_IN = "add.ios.landing";
    public static final String PUSH_LOGOUT = "close.ios.token";
    public static final String REGISTER_PATH = "/bigwinner/userinfo/register";
    public static final String LOGIN_PATH = "/bigwinner/verify/login";
    public static final String USERINFO_PATH = "/bigwinner/query/userinfo";
    public static final String USERINFO_EDIT_PATH = "/bigwinner/edit/user/info";
    public static final String COMPANY_EDIT_PATH = "/bigwinner/edit/company/info";
    public static final String UPLOAD_HEAD = "/bigwinner/user/upload/logo";
    public static final String UPLOAD_BG = "/bigwinner/user/upload/conver";
    public static final String UPLOAD_C_HEAD = "/bigwinner/company/upload/logo";
    public static final String UPLOAD_C_BG = "/bigwinner/company/upload/background";
    public static final String GET_CODE = "/bigwinner/aliyun/send/message";
    public static final String PASSWORD_EDIT_PATH = "/bigwinner/edit/password";
    public static final String PASSWORD_PATH = "/bigwinner/forget/password";
    public static final String LOGOUT_PATH = "/bigwinner/login/out";
    public static final String CONFIRM_PATH = "/bigwinner/user/upload/businesscard";
    public static final int LOGIN_RESULT = 1000000;
    public static final int USERINFO_RESULT = 1000001;
    public static final int TOKEN_RESULT = 1000002;
    public static final int PUSH_LOGININ_SUCCESS = 1000004;
    public static final int PUSH_LOGINOUT_SUCCESS = 1000003;
    public static final int PUSH_LOGIN_AGAIN = 1000005;
    public static final int UPLOAD_HEAD_RESULT = 1000006;
    public static final int UPLOAD_BG_RESULT = 1000007;
    public static final int EDIT_RESULT = 1000008;
    public static final int CODE_RESULT = 1000009;
    public static final int UPLOAD_HEAD_C_RESULT = 1000010;
    public static final int UPLOAD_BG_C_RESULT = 1000011;
    public static final int C_EDIT_RESULT = 1000012;
    public static final int PASS_WORD_EDIT_RUSULT = 1000013;
    public static final int REGISTER_RESULT = 1000014;
    public static final int CONFIRM_RESULT = 1000015;
    public static final int CHECK_TOKEN = 1000016;
    public static void doRegister(Context mContext, Handler mHandler, String username, String password, String code, Contacts contacts) {
        String urlString = BigwinerApplication.BASE_NET_PATH + REGISTER_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("smsverifycode", code);
            jsonObject.put("loginname", username);
            jsonObject.put("password", AppUtils.md5Decode(password));
            jsonObject.put("logo", contacts.icon);
            jsonObject.put("realname", contacts.mRName);
            jsonObject.put("sex", contacts.mSex);
            jsonObject.put("mobile", contacts.mMobile);
            jsonObject.put("province", contacts.province);
            jsonObject.put("city", contacts.city);
            jsonObject.put("businessType", contacts.typevalue);
            jsonObject.put("businessArea", contacts.typearea);
            jsonObject.put("status", contacts.staue);
            jsonObject.put("joinModel", "android");
            jsonObject.put("ccreditation", contacts.mPosition);
            jsonObject.put("phone", contacts.mPhone);
            jsonObject.put("fax", contacts.mFax);
            jsonObject.put("email", contacts.eMail);
            jsonObject.put("othercontact", contacts.mPhone2);
            jsonObject.put("demo", contacts.des);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, LOGIN_RESULT,
                    mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doPassword(Context mContext, Handler mHandler, String username, String password, String code,String area) {
        String urlString = BigwinerApplication.BASE_NET_PATH + PASSWORD_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("smsverifycode", code);
            jsonObject.put("loginname", username);
            jsonObject.put("areacode  ", area);
            jsonObject.put("newpassword", AppUtils.md5Decode(password));
            jsonObject.put("confirmpassword", AppUtils.md5Decode(password));
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, REGISTER_RESULT,
                    mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doRegisterq(Context mContext, Handler mHandler, String username, String password,String code,String city,String type,String area
    ,String areacode,String mail) {
        String urlString = BigwinerApplication.BASE_NET_PATH + REGISTER_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("smsverifycode", code);
            jsonObject.put("areacode", areacode);
            jsonObject.put("email", mail);
            jsonObject.put("loginname", username);
            jsonObject.put("password", AppUtils.md5Decode(password));
            jsonObject.put("city", city);
            jsonObject.put("businessarea", area);
            jsonObject.put("businesstype", type);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, REGISTER_RESULT,
                    mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doChangeRegister(Context mContext, Handler mHandler, String password, String old) {
        String urlString = BigwinerApplication.BASE_NET_PATH + PASSWORD_EDIT_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oldpassword", old);
            jsonObject.put("uid", BigwinerApplication.mApp.mAccount.mRecordId);
            jsonObject.put("newpassword", AppUtils.md5Decode(password));
            jsonObject.put("confirmpassword", AppUtils.md5Decode(password));
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, PASS_WORD_EDIT_RUSULT,
                    mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doLogin(Context mContext, Handler mHandler, String username, String password,String area) {
        String urlString = BigwinerApplication.BASE_NET_PATH + LOGIN_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("areacode", area);
            jsonObject.put("password", AppUtils.md5Decode(password));
            jsonObject.put("ipaddress", NetUtils.getLocalIpAddress());
            jsonObject.put("customer", "android");
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, LOGIN_RESULT,
                    mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doLogout() {
        String urlString = BigwinerApplication.BASE_NET_PATH + LOGOUT_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, null, LOGIN_RESULT,
                    null, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doEditUserinfo(Context mContext, Handler mHandler, Account account) {
        String urlString = BigwinerApplication.BASE_NET_PATH + USERINFO_EDIT_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("realname", account.mRealName);
//            jsonObject.put("logo",account.icon);
            jsonObject.put("email", account.mEmail);
            jsonObject.put("sex", account.mSex);
            jsonObject.put("mobile", account.mMobile);
            jsonObject.put("province", account.province);
            jsonObject.put("city", account.city);
            jsonObject.put("businesstype", account.typeBusiness);
            jsonObject.put("businessarea", account.typeArea);
            jsonObject.put("accreditation", account.mPosition);
            jsonObject.put("companyid", account.mUCid);
            jsonObject.put("personalprofile", account.des);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, EDIT_RESULT,
                    mContext, postBody, account,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doEditCompanyInfo(Context mContext, Handler mHandler, Company company) {
        String urlString = BigwinerApplication.BASE_NET_PATH + COMPANY_EDIT_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            jsonObject.put("companyid", company.id);
            jsonObject.put("businesslicense", company.sailno);
            jsonObject.put("taxcertificate", company.taxno);
            jsonObject.put("address", company.address);
            jsonObject.put("province", company.province);
            jsonObject.put("city", company.city);
            jsonObject.put("companyname", company.name);
            jsonObject.put("englishname", company.ename);
            jsonObject.put("fax", company.fax);
            jsonObject.put("phone", company.phone);
            jsonObject.put("email", company.mail);
            jsonObject.put("website", company.web);
            jsonObject.put("demo", company.characteristic);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, C_EDIT_RESULT,
                    mContext, postBody, company,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void checkToken(Context mContext, Handler mHandler) {
        String urlString = BigwinerApplication.BASE_NET_PATH + USERINFO_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CHECK_TOKEN,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getUserInfo(Context mContext, Handler mHandler) {
        String urlString = BigwinerApplication.BASE_NET_PATH + USERINFO_PATH;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", NetUtils.getInstance().token);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, USERINFO_RESULT,
                    mContext, postBody,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getCode(Context mContext, Handler mHandler, String phone, String type,String areacode) {
        String urlString = BigwinerApplication.BASE_NET_PATH + GET_CODE;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginname", phone);
            jsonObject.put("type", type);
            jsonObject.put("areacode", areacode);
            String postBody = jsonObject.toString();
            PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, CODE_RESULT,
                    mContext, postBody);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doPushLogout(Handler mHandler, Context mContext) {


        String urlString = BigwinerApplication.SERVICE_NET_PATH+"/" + PUSH_PATH;
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", PUSH_LOGOUT);
        items.add(item);
        item = new BasicNameValuePair("company_id", ContactManager.mContactManager.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", ContactManager.mContactManager.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("device_token", BigwinerApplication.mApp.szImei);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PUSH_LOGINOUT_SUCCESS, mContext, items,BigwinerApplication.mApp.checkToken);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void doPushlogin(Handler mHandler, Context mContext) {
        if (BigwinerApplication.mApp.deviceId.length() != 0) {


            String urlString = BigwinerApplication.SERVICE_NET_PATH+"/" + PUSH_PATH;
            ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
            BasicNameValuePair item = new BasicNameValuePair("method", PUSH_LOGIN_IN);
            items.add(item);
            item = new BasicNameValuePair("company_id", ContactManager.mContactManager.mAccount.mCompanyId);
            items.add(item);
            item = new BasicNameValuePair("user_id", ContactManager.mContactManager.mAccount.mRecordId);
            items.add(item);
            item = new BasicNameValuePair("source", "intersky");
            items.add(item);
            item = new BasicNameValuePair("customer", "android");
            items.add(item);
            item = new BasicNameValuePair("device_token", BigwinerApplication.mApp.szImei);
            items.add(item);
            item = new BasicNameValuePair("device_id", BigwinerApplication.mApp.deviceId);
            items.add(item);
            PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, PUSH_LOGININ_SUCCESS, mContext, items,BigwinerApplication.mApp.checkToken);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        } else {
            if(mHandler != null)
            mHandler.sendEmptyMessageDelayed(PUSH_LOGIN_AGAIN, 500);
        }
    }

    public static void setUploadHead(Context mContext, Handler mHandler, File file) {
        String urlString = BigwinerApplication.BASE_NET_PATH + UPLOAD_HEAD;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair;
        mNameValuePair = new NameValuePair("fileName", file.getName());
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("token", NetUtils.getInstance().token);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("file", file.getName());
        mNameValuePair.isFile = true;
        mNameValuePair.path = file.getPath();
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, UPLOAD_HEAD_RESULT, mContext, nameValuePairs, file,BigwinerApplication.mApp.checkToken);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setUploadBg(Context mContext, Handler mHandler, File file) {
        String urlString = BigwinerApplication.BASE_NET_PATH + UPLOAD_BG;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair;
        mNameValuePair = new NameValuePair("fileName", file.getName());
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("token", NetUtils.getInstance().token);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("file", file.getName());
        mNameValuePair.isFile = true;
        mNameValuePair.path = file.getPath();
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, UPLOAD_BG_RESULT, mContext, nameValuePairs, file,BigwinerApplication.mApp.checkToken);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setUploadCompanyHead(Context mContext, Handler mHandler, File file, Company company) {
        String urlString = BigwinerApplication.BASE_NET_PATH + UPLOAD_C_HEAD;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair;
        mNameValuePair = new NameValuePair("fileName", file.getName());
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("companyid", company.id);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("token", NetUtils.getInstance().token);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("file", file.getName());
        mNameValuePair.isFile = true;
        mNameValuePair.path = file.getPath();
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, UPLOAD_HEAD_C_RESULT, mContext, nameValuePairs, company,BigwinerApplication.mApp.checkToken);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setUploadCompanyBg(Context mContext, Handler mHandler, File file, Company company) {
        String urlString = BigwinerApplication.BASE_NET_PATH + UPLOAD_C_BG;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair;
        mNameValuePair = new NameValuePair("fileName", file.getName());
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("companyid", company.id);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("token", NetUtils.getInstance().token);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("file", file.getName());
        mNameValuePair.isFile = true;
        mNameValuePair.path = file.getPath();
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, UPLOAD_BG_C_RESULT, mContext, nameValuePairs, company,BigwinerApplication.mApp.checkToken);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void setUploadConfirm(Context mContext, Handler mHandler, File file) {
        String urlString = BigwinerApplication.BASE_NET_PATH + CONFIRM_PATH;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair mNameValuePair;
        mNameValuePair = new NameValuePair("fileName", file.getName());
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("token", NetUtils.getInstance().token);
        nameValuePairs.add(mNameValuePair);
        mNameValuePair = new NameValuePair("file", file.getName());
        mNameValuePair.isFile = true;
        mNameValuePair.path = file.getPath();
        nameValuePairs.add(mNameValuePair);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, CONFIRM_RESULT, mContext, nameValuePairs, file,BigwinerApplication.mApp.checkToken);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }
}
