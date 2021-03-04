package com.bigwiner.android.prase;

import android.content.Context;

import com.bigwiner.android.entity.Company;
import com.bigwiner.android.entity.Meeting;
import com.bigwiner.android.entity.Notice;
import com.bigwiner.android.view.BigwinerApplication;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.apputils.AppUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.select.entity.Select;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class DetialPrase {

    public static boolean praseNoticeAndNew(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        Notice notice = (Notice) net.item;
        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            XpxJSONObject jo = jObject.getJSONObject("data");
            notice.classification = jo.getString("Classification");
            notice.tital = jo.getString("Tital");
            notice.content = jo.getString("Content");
            notice.time = jo.getString("ctime");
            notice.uid = jo.getString("uid");
            return  true;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
    }

    public static boolean praseMeeting(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        Meeting meeting = (Meeting) net.item;
        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            XpxJSONObject jo = jObject.getJSONObject("data");
            meeting.joinCompanys = jo.getString("JoinCompany");
            meeting.address = jo.getString("address");
            meeting.contractor = jo.getString("contractor");
            meeting.des = jo.getString("demo");
            meeting.prise2 = jo.getString("fee");
            meeting.prise1 = jo.getString("earlybirdfee");
            meeting.contactor = jo.getString("contactor");
            meeting.phone = jo.getString("contactphone");
            meeting.stute = jo.getString("status");
//            meeting.prise2 = jo.getString("fee");
            meeting.count = jo.getInt("participantscount",0);
            meeting.max = jo.getInt("limitednum",0);
            meeting.sponsor = jo.getString("sponsor");
            meeting.timebegin = jo.getString("startdate");
            meeting.timeend = jo.getString("enddate");
            meeting.isjionin = jo.getBoolean("isenroll",false);
            meeting.issign = jo.getBoolean("ischeckd",false);
            meeting.name = jo.getString("tital");
            meeting.photo = jo.getString("photo");
            meeting.isjionin = jo.getBoolean("isenroll",false);
            if(jo.has("list"))
            {
                XpxJSONArray ja = jo.getJSONArray("list");
                for(int i = 0 ; i < ja.length() ; i++) {
                    XpxJSONObject jo2 = ja.getJSONObject(i);
                    Company company = new Company();
                    company.id = jo2.getString("companyid");
                    company.name = jo2.getString("compnayname");
                    company.icon = jo2.getString("companylogo");
                    company.jointype = jo2.getString("participationtype");
                    meeting.companies.add(company);
                }
            }


            return  true;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
    }

    public static boolean praseDate(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        return true;
    }

    public static void praseCompanyList(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
        }

        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            XpxJSONArray ja = jObject.getJSONArray("data");
            BigwinerApplication.mApp.hashCompany.clear();
            BigwinerApplication.mApp.companies.clear();
            BigwinerApplication.mApp.companyslelct.clear();

            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                Company company = new Company();
                company.id = jo.getString("rid");
                company.city = jo.getString("city");
                company.province = jo.getString("province");
                company.address = jo.getString("address");
                company.name = jo.getString("name");
                company.web = jo.getString("website");
                company.statue = jo.getString("status");
                BigwinerApplication.mApp.hashCompany.put(company.id,company);
                BigwinerApplication.mApp.companies.add(company);
                Select select = new Select(company.id,company.name);
                BigwinerApplication.mApp.companyslelct.list.add(select);
                BigwinerApplication.mApp.companyslelct.hashMap.put(select.mId,select);
            }

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public static String praseData(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return "";
        }
        String count = "";
        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            count = jObject.getString("checkno");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return count;
    }
}
