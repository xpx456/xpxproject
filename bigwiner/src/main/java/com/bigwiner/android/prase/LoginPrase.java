package com.bigwiner.android.prase;

import android.content.Context;
import android.content.SharedPreferences;

import com.bigwiner.R;
import com.bigwiner.android.entity.Company;
import com.bigwiner.android.entity.UserDefine;
import com.bigwiner.android.view.BigwinerApplication;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;

import java.io.File;

import intersky.appbase.Local.LocalData;
import intersky.appbase.entity.Account;
import intersky.apputils.AppUtils;
import intersky.apputils.SystemUtil;
import intersky.apputils.TimeUtils;
import intersky.filetools.FileUtils;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.DownloadTask;
import intersky.xpxnet.net.nettask.NetTask;

public class LoginPrase {

    public static boolean praseLogin(Context context,NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            XpxJSONObject jo = jObject.getJSONObject("userinfo");
            BigwinerApplication.mApp.mAccount.islogin = true;
            BigwinerApplication.mApp.mAccount.mRecordId = jo.getString("rid");
            BigwinerApplication.mApp.mAccount.mPosition = jo.getString("accreditation");
            BigwinerApplication.mApp.mAccount.confrim = jo.getString("authentication");
            BigwinerApplication.mApp.mAccount.typeBusiness = jo.getString("businesstype");
            BigwinerApplication.mApp.mAccount.typeArea = jo.getString("businessarea");
            BigwinerApplication.mApp.mAccount.city = jo.getString("city");
            BigwinerApplication.mApp.mAccount.mEmail = jo.getString("email");
            BigwinerApplication.mApp.mAccount.mFax = jo.getString("fax");
            BigwinerApplication.mApp.mAccount.icon = jo.getString("logo");
            BigwinerApplication.mApp.mAccount.cover = jo.getString("cover");
            BigwinerApplication.mApp.mAccount.issail  = jo.getBoolean("issailmember",false);
            BigwinerApplication.mApp.mAccount.leavel = measureStarrating(jo.getString("starrating"));
            BigwinerApplication.mApp.mAccount.mCompanyId = jo.getString("cid");
            BigwinerApplication.mApp.mAccount.mCloundAdminId = jo.getString("areacode");
            BigwinerApplication.mApp.mAccount.mUCid = jo.getString("companyid");
            BigwinerApplication.mApp.mAccount.des = jo.getString("personalprofile",context.getString(R.string.des_temp));
            BigwinerApplication.mApp.mAccount.mMobile = jo.getString("mobile");
            BigwinerApplication.mApp.mAccount.mRealName = jo.getString("name");
            BigwinerApplication.mApp.mAccount.mPhone = jo.getString("phone");
            BigwinerApplication.mApp.mAccount.vip = jo.getString("membercategory");
            BigwinerApplication.mApp.mAccount.province = jo.getString("province");
            BigwinerApplication.mApp.mAccount.complaint = jo.getString("complaint");
            BigwinerApplication.mApp.mAccount.mHCurrent = jo.getInt("hcurrency",0);
            BigwinerApplication.mApp.mAccount.mSex = jo.getString("sex");
            BigwinerApplication.mApp.mAccount.modify = String.valueOf(System.currentTimeMillis());
            if(BigwinerApplication.mApp.mAccount.mSex.equals("男"))
                BigwinerApplication.mApp.mAccount.sex = 0;
            else if(BigwinerApplication.mApp.mAccount.mSex.equals("女"))
                BigwinerApplication.mApp.mAccount.sex = 1;
            else
                BigwinerApplication.mApp.mAccount.sex = 2;
            return  true;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
    }

    public static int checkToken(Context context,NetObject net)
    {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            //AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            if(AppUtils.tokenresult(json) == -1)
            {
                AppUtils.showMessage(context,context.getString(R.string.kick_word));
                return -1;
            }
            else
            {
                return -2;
            }
        }
        return 1;
    }

    public static int praseUserInfo(Context context,NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            if(AppUtils.tokenresult(json) == -1)
            {
                return -1;
            }
            else
            {
                return -2;
            }
        }
        if(AppUtils.measureToken(json) != null) {

        }
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            XpxJSONObject jo = jsonObject.getJSONObject("data");
            BigwinerApplication.mApp.mAccount.mPosition = jo.getString("accreditation");
            BigwinerApplication.mApp.mAccount.confrim = jo.getString("authentication");
            BigwinerApplication.mApp.mAccount.typeBusiness = jo.getString("businessType");
            BigwinerApplication.mApp.mAccount.typeArea = jo.getString("businessArea");
            BigwinerApplication.mApp.mAccount.city = jo.getString("city");
            BigwinerApplication.mApp.mAccount.mEmail = jo.getString("email");
            BigwinerApplication.mApp.mAccount.mFax = jo.getString("fax");
            BigwinerApplication.mApp.mAccount.icon = jo.getString("logo");
            BigwinerApplication.mApp.mAccount.cover = jo.getString("cover");
            BigwinerApplication.mApp.mAccount.issail  = jo.getBoolean("issailmember",false);
            BigwinerApplication.mApp.mAccount.leavel = measureStarrating(jo.getString("starrating"));
            BigwinerApplication.mApp.mAccount.mUCid = jo.getString("companyid");
            BigwinerApplication.mApp.mAccount.mCloundAdminId = jo.getString("areacode");
            BigwinerApplication.mApp.mAccount.complaint = jo.getString("complaint");
            BigwinerApplication.mApp.mAccount.des = jo.getString("personalProfile",context.getString(R.string.des_temp));
            BigwinerApplication.mApp.mAccount.mMobile = jo.getString("mobile");
            BigwinerApplication.mApp.mAccount.mRealName = jo.getString("name");
            BigwinerApplication.mApp.mAccount.vip = jo.getString("membercategory");
            BigwinerApplication.mApp.mAccount.mHCurrent = jo.getInt("hcurrency",0);
            BigwinerApplication.mApp.mAccount.mSex = jo.getString("sex");
            return 1;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -2;
        }
    }


    public static boolean praseToken(Context context,NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }

        SharedPreferences sharedPre = context.getSharedPreferences(UserDefine.LAST_USER, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        if(AppUtils.measureToken(json) != null) {

            if(AppUtils.measureToken(json).length() > 0)
            {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
                e.putString(UserDefine.USER_TOKEN, NetUtils.getInstance().token);

            }
        }
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            XpxJSONObject jo = jsonObject.getJSONObject("data");
            if(jo.getString("ExpiresDate").length() > 0)
            {
                e.putString(UserDefine.USER_TOKEN_DATA, jo.getString("expiresdate"));
                e.commit();
            }
            BigwinerApplication.mApp.mAccount.mRecordId = jo.getString("rid");
            BigwinerApplication.mApp.mAccount.mPosition = jo.getString("accreditation");
            BigwinerApplication.mApp.mAccount.confrim = jo.getString("authentication");
            BigwinerApplication.mApp.mAccount.typeBusiness = jo.getString("businesstyp");
            BigwinerApplication.mApp.mAccount.typeArea = jo.getString("businessarea");
            BigwinerApplication.mApp.mAccount.mUCid = jo.getString("companyid");
            BigwinerApplication.mApp.mAccount.city = jo.getString("city");
            BigwinerApplication.mApp.mAccount.mEmail = jo.getString("email");
            BigwinerApplication.mApp.mAccount.mUCid = jo.getString("companyid");
            BigwinerApplication.mApp.mAccount.mFax = jo.getString("fax");
            BigwinerApplication.mApp.mAccount.des = jo.getString("personalprofile",context.getString(R.string.des_temp));
            BigwinerApplication.mApp.mAccount.leavel = measureStarrating(jo.getString("starrating"));
            BigwinerApplication.mApp.mAccount.issail  = jo.getBoolean("issailmember",false);
            BigwinerApplication.mApp.mAccount.icon = jo.getString("logo");
            BigwinerApplication.mApp.mAccount.mCloundAdminId = jo.getString("areacode");
            BigwinerApplication.mApp.mAccount.cover = jo.getString("cover");
            BigwinerApplication.mApp.mAccount.mMobile = jo.getString("mobile");
            BigwinerApplication.mApp.mAccount.mRealName = jo.getString("name");
            BigwinerApplication.mApp.mAccount.mPhone = jo.getString("phone");
//            BigwinerApplication.mApp.mAccount.mPassword = jo.getString("passWord");
            BigwinerApplication.mApp.mAccount.mPhone = jo.getString("phone");
            BigwinerApplication.mApp.mAccount.province = jo.getString("province");
            BigwinerApplication.mApp.mAccount.mHCurrent = jo.getInt("hcurrency",0);
            BigwinerApplication.mApp.mAccount.mSex = jo.getString("sex");
            if(BigwinerApplication.mApp.mAccount.mSex.equals("男"))
                BigwinerApplication.mApp.mAccount.sex = 0;
            else if(BigwinerApplication.mApp.mAccount.mSex.equals("女"))
            BigwinerApplication.mApp.mAccount.sex = 1;
            else
            BigwinerApplication.mApp.mAccount.sex = 2;
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return true;
    }

    public static boolean praseHead (Context context,NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            BigwinerApplication.mApp.mAccount.icon = jsonObject.getString("fileid");
        } catch (JSONException e1) {
            e1.printStackTrace();
            return false;
        }
        return true;
    }

    public static String praseHeadex (Context context,NetObject net) {
        String json = net.result;
        String head = "";
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return "";
        }
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            head = jsonObject.getString("fileid");
        } catch (JSONException e1) {
            e1.printStackTrace();
            return "";
        }
        return head;
    }

    public static boolean praseBg (Context context,NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            BigwinerApplication.mApp.mAccount.cover = jsonObject.getString("fileid");
        } catch (JSONException e1) {
            e1.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean praseUseData(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        Account account = (Account) net.item;
        BigwinerApplication.mApp.mAccount.copy(account);
        BigwinerApplication.mApp.saveUseData(context);
        if(BigwinerApplication.mApp.mAccount.mUCid.length() > 0)
        BigwinerApplication.mApp.company = BigwinerApplication.mApp.hashCompany.get(BigwinerApplication.mApp.mAccount.mUCid);
        return true;
    }

    public static boolean praseUserCompany(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        Company company = (Company) net.item;
        BigwinerApplication.mApp.company.copy(company);
        return true;
    }

    public static String companyHUpload (Context context,NetObject net) {
        String json = net.result;
        String head = "";
        Company company = (Company) net.item;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return "";
        }
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            head = jsonObject.getString("fileid");
            company.icon = head;
            return head;
        } catch (JSONException e1) {
            e1.printStackTrace();
            return "";
        }
    }

    public static String companyBUpload (Context context,NetObject net) {
        String json = net.result;
        String head = "";
        Company company = (Company) net.item;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return "";
        }
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            head = jsonObject.getString("fileid");
            company.bg = head;
            return head;
        } catch (JSONException e1) {
            e1.printStackTrace();
            return "";
        }
    }

    public static int measureStarrating(String stare) {
        if(stare.equals("五星"))
        {
            return 5;
        }
        else if(stare.equals("四星"))
        {
            return 4;
        }
        else if(stare.equals("三星"))
        {
            return 3;
        }
        else if(stare.equals("二星"))
        {
            return 2;
        }
        else if(stare.equals("一星"))
        {
            return 1;
        }
        else{
            return 0;
        }
    }
}
