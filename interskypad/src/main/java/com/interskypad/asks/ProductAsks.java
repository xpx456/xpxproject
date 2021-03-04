package com.interskypad.asks;

import android.content.Context;
import android.os.Handler;

import com.interskypad.manager.ProducterManager;
import com.interskypad.thread.GetCatalogThread;
import com.interskypad.thread.GetCategoryThread;
import com.interskypad.view.InterskyPadApplication;

import java.util.ArrayList;

import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;

public class ProductAsks {

    public static final int EVENT_GET_CATEGORY_SUCCESS = 1020000;
    public static final int EVENT_GET_CATEGORY_LOCAL_SUCCESS = 1020001;
    public static final int EVENT_GET_CATALOG_SUCCESS = 1020002;
    public static final int EVENT_GET_CATALOG_LOCAL_SUCCESS = 1020003;
    public static final String CATEGORY_PATH = "api/ProductCatalogue";
    public static final String CATALOG_PATH = "api/ProductItems";
    public static void getCateGory(Handler mHandler,Context mContext) {
        if(InterskyPadApplication.mApp.isLogin == false) {
            ProducterManager.getInstance().categories.clear();
            new GetCategoryThread(mHandler,mContext).start();
        }
        else
        {
            String urlString = NetUtils.getInstance().praseUrl(InterskyPadApplication.mApp.mService,CATEGORY_PATH);
            ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
            BasicNameValuePair item = new BasicNameValuePair("token", NetUtils.getInstance().token);
            items.add(item);
            PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_CATEGORY_SUCCESS, mContext, items);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
//            try {
//                String urlString = NetUtils.getInstance().createURLStringex(CATEGORY_PATH);
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("token",NetUtils.getInstance().token);
//                String postBody =  jsonObject.toString();
//                PostJsonNetTask mPostNetTask = new PostJsonNetTask(urlString, mHandler, EVENT_GET_CATEGORY_SUCCESS, mContext, postBody);
//                NetTaskManager.getInstance().addNetTask(mPostNetTask);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }

    public static void getCatalog(Handler mHandler , Context mContext,String keyword,String categoryid,int page)
    {
        if(InterskyPadApplication.mApp.isLogin == false) {
            new GetCatalogThread(mHandler,mContext,keyword,categoryid).start();
        }
        else
        {
            String urlString = NetUtils.getInstance().praseUrl(InterskyPadApplication.mApp.mService,CATALOG_PATH);
            ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
            BasicNameValuePair item = new BasicNameValuePair("token", NetUtils.getInstance().token);
            items.add(item);
            item = new BasicNameValuePair("CatalogueID", categoryid);
            items.add(item);
            item = new BasicNameValuePair("keyword", keyword);
            items.add(item);
            item = new BasicNameValuePair("page_no", String.valueOf(page));
            items.add(item);
            item = new BasicNameValuePair("page_size", String.valueOf(20));
            items.add(item);
            PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_CATALOG_SUCCESS, mContext, items);
            NetTaskManager.getInstance().addNetTask(mPostNetTask);
        }
    }

}
