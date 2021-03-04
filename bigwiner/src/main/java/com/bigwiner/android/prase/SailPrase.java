package com.bigwiner.android.prase;

import android.content.Context;

import com.bigwiner.android.entity.Complaint;
import com.bigwiner.android.entity.SailMember;
import com.bigwiner.android.view.BigwinerApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;

public class SailPrase {

    public static void parasComplant(Context context, NetObject net, ArrayList<Complaint> complaints, ModuleDetial moduleDetial) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return ;
        }

        XpxJSONObject jObject = null;
        try {
            jObject = new XpxJSONObject(json);
            XpxJSONObject jsonObject = jObject.getJSONObject("data");
            XpxJSONArray ja = jsonObject.getJSONArray("list");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                Complaint complaint = new Complaint();
                complaint.id = jo.getString("id");
                complaint.cname = jo.getString("complaintcompany");
                complaint.cid = jo.getString("complaintcompanyid");
                complaint.cname1 = jo.getString("convercomplaintcompany");
                complaint.cid1 = jo.getString("convercomplaintcompanyid");
                complaint.result = jo.getString("operateresult");
                complaint.request = jo.getString("questionreflect");
                complaint.memo = jo.getString("memo");
                complaint.statue = jo.getString("status");
                complaints.add(complaint);
            }
            moduleDetial.currentpage = jsonObject.getInt("pageNo", 1);
            moduleDetial.pagesize = jsonObject.getInt("page_size", 20);
            moduleDetial.totlepage = jsonObject.getInt("totalPage", 1);
            moduleDetial.totleszie = jsonObject.getInt("totalcount", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parasSailMember(Context context, NetObject net, ArrayList<SailMember> complaints,ModuleDetial moduleDetial) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return ;
        }

        XpxJSONObject jObject = null;
        try {
            jObject = new XpxJSONObject(json);
            XpxJSONObject jsonObject = jObject.getJSONObject("data");
            XpxJSONArray ja = jsonObject.getJSONArray("sailmemberlist");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                SailMember complaint = new SailMember();
                complaint.cid = jo.getString("companyno");
                complaint.cname = jo.getString("companyname");
                complaint.addr = jo.getString("addr");
                complaint.address = jo.getString("address");
                complaint.money = jo.getString("compensation");
                complaint.jointime = jo.getString("joindate");
                complaint.joinyear = jo.getString("joinyear");
                complaint.logo = jo.getString("logo");
                complaint.leavel = jo.getInt("serviceStar",0);
                complaints.add(complaint);
            }
            moduleDetial.currentpage = jsonObject.getInt("page_no", 1);
            moduleDetial.pagesize = jsonObject.getInt("page_size", 20);
            moduleDetial.totlepage = jsonObject.getInt("totalPage", 1);
            moduleDetial.totleszie = jsonObject.getInt("totalcount", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
