package com.bigwiner.android.prase;

import android.content.Context;

import com.bigwiner.android.entity.Complaint;
import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.view.BigwinerApplication;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;

public class SourcePrase {

    public static void praseSourceList(Context context, NetObject net, ModuleDetial moduleDetial, ArrayList<SourceData> sources) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return ;
        }
        String  type = (String) net.item;
        XpxJSONObject jObject = null;
        try {
            jObject = new XpxJSONObject(json);
            XpxJSONObject jsonObject = jObject.getJSONObject("data");
            XpxJSONArray ja;
            if(type.equals("0"))
             ja = jsonObject.getJSONArray("all");
            else if(type.equals("1"))
                ja = jsonObject.getJSONArray("demandresources");
            else
                ja = jsonObject.getJSONArray("provideresources");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                SourceData sourceData = new SourceData();
                sourceData.id = jo.getString("no");
                sourceData.viewcount = jo.getString("browsecount");
                sourceData.area = jo.getString("businessarea");
                sourceData.type = jo.getString("businesstype");
                sourceData.collectcount = jo.getString("collectcount");
                sourceData.iscollcet = jo.getInt("collectionstatus",0);
                sourceData.memo = jo.getString("description");
                sourceData.end = jo.getString("enddate")+" 00:00:00";
                sourceData.mobile = jo.getString("moblie");
                sourceData.port = jo.getString("port");
                sourceData.publicetime = jo.getString("postdate")+" 00:00:00";
                sourceData.username = jo.getString("postname");
                sourceData.sourcetype = jo.getString("resourcestype");
                sourceData.start = jo.getString("startdate")+" 00:00:00";
                sourceData.name = jo.getString("tital");
                sourceData.photo = jo.getString("photo");
                sources.add(sourceData);
            }
            moduleDetial.currentpage = jsonObject.getInt("pageNo", 1);
            moduleDetial.pagesize = jsonObject.getInt("page_size", 30);
            moduleDetial.totlepage = jsonObject.getInt("totalPage", 1);
            moduleDetial.totleszie = jsonObject.getInt("totalcount", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseSourceSearch(Context context, NetObject net, ArrayList<SourceData> sources, HashMap<String,SourceData> hashMap, ModuleDetial moduldetial) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return ;
        }
        String  type = (String) net.item;
        XpxJSONObject jObject = null;
        try {
            jObject = new XpxJSONObject(json);
            XpxJSONObject jsonObject = jObject.getJSONObject("data");
            XpxJSONArray ja;
            if(type.equals("0"))
                ja = jsonObject.getJSONArray("all");
            else if(type.equals("1"))
                ja = jsonObject.getJSONArray("demandresources");
            else
                ja = jsonObject.getJSONArray("provideresources");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                SourceData sourceData = new SourceData();
                sourceData.id = jo.getString("no");
                sourceData.viewcount = jo.getString("browsecount");
                sourceData.area = jo.getString("businessarea");
                sourceData.type = jo.getString("businesstype");
                sourceData.collectcount = jo.getString("collectcount");
                sourceData.iscollcet = jo.getInt("collectionstatus",0);
                sourceData.memo = jo.getString("description");
                sourceData.end = jo.getString("enddate")+" 00:00:00";
                sourceData.mobile = jo.getString("moblie");
                sourceData.port = jo.getString("port");
                sourceData.publicetime = jo.getString("postdate")+" 00:00:00";
                sourceData.username = jo.getString("postname");
                sourceData.sourcetype = jo.getString("resourcestype");
                sourceData.start = jo.getString("startdate")+" 00:00:00";
                sourceData.name = jo.getString("tital");
                sourceData.photo = jo.getString("photo");
                sources.add(sourceData);
                hashMap.put(sourceData.id,sourceData);
            }
            moduldetial.currentpage = jsonObject.getInt("pageNo", 1);
            moduldetial.pagesize = jsonObject.getInt("page_size", 30);
            moduldetial.totlepage = jsonObject.getInt("totalPage", 1);
            moduldetial.totleszie = jsonObject.getInt("totalcount", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseSourceSCollect(Context context, NetObject net, ModuleDetial moduleDetial, ArrayList<SourceData> sources,HashMap<String,SourceData> hashMap) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return ;
        }
        String  type = (String) net.item;
        XpxJSONObject jObject = null;
        try {
            jObject = new XpxJSONObject(json);
            XpxJSONObject jsonObject = jObject.getJSONObject("data");
            XpxJSONArray ja;
            ja = jsonObject.getJSONArray("list");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                SourceData sourceData = new SourceData();
                sourceData.id = jo.getString("no");
                sourceData.viewcount = jo.getString("browsecount");
                sourceData.area = jo.getString("businessarea");
                sourceData.type = jo.getString("businesstype");
                sourceData.collectcount = jo.getString("collectcount");
                sourceData.iscollcet = 1;
                sourceData.memo = jo.getString("description");
                sourceData.end = jo.getString("enddate")+" 00:00:00";
                sourceData.mobile = jo.getString("moblie");
                sourceData.port = jo.getString("port");
                sourceData.publicetime = jo.getString("postdate")+" 00:00:00";
                sourceData.username = jo.getString("name");
                sourceData.sourcetype = jo.getString("resourcestype");
                sourceData.start = jo.getString("startdate"+" 00:00:00");
                sourceData.name = jo.getString("tital");
                sourceData.photo = jo.getString("photo");
                hashMap.put(sourceData.id,sourceData);
                sources.add(sourceData);
            }
            moduleDetial.currentpage = jsonObject.getInt("pageNo", 1);
            moduleDetial.pagesize = jsonObject.getInt("page_size", 10);
            moduleDetial.totlepage = jsonObject.getInt("totalPage", 1);
            moduleDetial.totleszie = jsonObject.getInt("totalcount", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseSourceDetial(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return ;
        }
        SourceData sourceData = (SourceData) net.item;
        XpxJSONObject jObject = null;
        try {
            jObject = new XpxJSONObject(json);
            XpxJSONObject jo = jObject.getJSONObject("data");
            sourceData.viewcount = jo.getString("browsecount");
            sourceData.area = jo.getString("businessarea");
            sourceData.type = jo.getString("businesstype");
            sourceData.collectcount = jo.getString("collectcount");
//            sourceData.iscollcet = jo.getInt("collectionstatus",0);
            sourceData.memo = jo.getString("description");
            sourceData.end = jo.getString("enddate")+" 00:00:00";
            sourceData.mobile = jo.getString("moblie");
            sourceData.port = jo.getString("port");
            sourceData.publicetime = jo.getString("postdate")+" 00:00:00";
            sourceData.username = jo.getString("name");
            sourceData.sourcetype = jo.getString("resourcestype");
            sourceData.start = jo.getString("startdate")+" 00:00:00";
            sourceData.name = jo.getString("tital");
            sourceData.photo = jo.getString("photo");
            sourceData.userid = jo.getString("postid");
            sourceData.position = jo.getString("position");
            sourceData.company = jo.getString("companyname");
            sourceData.isfriend = jo.getBoolean("isfriend",false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseSourceMyList(Context context, NetObject net, ModuleDetial moduleDetial, ArrayList<SourceData> sources) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return ;
        }
        String  type = (String) net.item;
        XpxJSONObject jObject = null;
        try {
            jObject = new XpxJSONObject(json);
            XpxJSONObject jsonObject = jObject.getJSONObject("data");
            XpxJSONArray ja;
            if(type.equals("1"))
                ja = jsonObject.getJSONArray("demandresources");
            else
                ja = jsonObject.getJSONArray("provideresources");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                SourceData sourceData = new SourceData();
                sourceData.id = jo.getString("no");
                sourceData.viewcount = jo.getString("browsecount");
                sourceData.area = jo.getString("businessarea");
                sourceData.type = jo.getString("businesstype");
                sourceData.collectcount = jo.getString("collectcount");
                sourceData.iscollcet = jo.getInt("collectionstatus",0);
                sourceData.memo = jo.getString("description");
                sourceData.end = jo.getString("enddate")+" 00:00:00";
                sourceData.mobile = jo.getString("moblie");
                sourceData.port = jo.getString("port");
                sourceData.publicetime = jo.getString("postdate")+" 00:00:00";
                sourceData.username = jo.getString("postname");
                sourceData.sourcetype = jo.getString("resourcestype");
                sourceData.start = jo.getString("startdate")+" 00:00:00";
                sourceData.name = jo.getString("tital");
                sourceData.userid = BigwinerApplication.mApp.mAccount.mRecordId;
                sourceData.photo = jo.getString("photo");
                sourceData.state = jo.getString("status");
                sources.add(sources.size()-1,sourceData);
            }
            moduleDetial.currentpage = jsonObject.getInt("pageNo", 1);
            moduleDetial.pagesize = jsonObject.getInt("page_size", 10);
            moduleDetial.totlepage = jsonObject.getInt("totalPage", 1);
            moduleDetial.totleszie = jsonObject.getInt("totalcount", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean praseAddCheck(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        else
        {
//            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
        }
        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            XpxJSONObject jsonObject = jObject.getJSONObject("data");
            if(jsonObject.getInt("istransfinite",0) == 1)
            {
                return false;
            }
            else
            {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
