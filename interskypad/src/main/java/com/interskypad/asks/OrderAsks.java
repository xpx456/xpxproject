package com.interskypad.asks;

import android.content.Context;
import android.os.Handler;

import com.interskypad.entity.Catalog;
import com.interskypad.entity.Order;
import com.interskypad.view.InterskyPadApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;

public class OrderAsks {

    public static final int EVENT_SUBMIT_SUCCESS = 1030000;
    public static final int EVENT_DELETE_SUCCESS = 1030001;
    public static final String SUBMIT_ORDER_PATH = "api/ProductQuotation";
    public static final String DELETE_ORDER_PATH = "api/DeleteProductQuotation";

    public static void submitOrder(Handler mHandler, Context mContext, Order order) {

        String urlString = NetUtils.getInstance().praseUrl(InterskyPadApplication.mApp.mService,SUBMIT_ORDER_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token", NetUtils.getInstance().token);
        items.add(item);
        item = new BasicNameValuePair("Data", measureOrderData(order));
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_SUBMIT_SUCCESS, mContext, items,order);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void deleteOrder(Handler mHandler,Context mContext,Order order) {

        String urlString = NetUtils.getInstance().praseUrl(InterskyPadApplication.mApp.mService,DELETE_ORDER_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("token", NetUtils.getInstance().token);
        items.add(item);
        item = new BasicNameValuePair("RecordID", order.id);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_DELETE_SUCCESS, mContext, items,order);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static String measureOrderData(Order order) {


        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject maintablle = new JSONObject();
            JSONArray chiletable = new JSONArray();
            jsonObject.put("mainTable",maintablle);
            maintablle.put("UserName", InterskyPadApplication.mApp.mAccount.mAccountId);
            maintablle.put("DataSouce", "PAD");
            maintablle.put("RecordID", order.id);
            maintablle.put("Memo", "客户名称："+order.c_name+" 电话："+order.c_phone+" 手机："+order.c_mobil+" 地址："+order.c_address
                    +" 说明："+order.memo+" 报价时间: "+order.time);
            jsonObject.put("childTable",chiletable);
            for(Map.Entry<String, Catalog> entry: order.hashcatalogs.entrySet())
            {
                JSONObject jo = new JSONObject();
                Catalog catalog = entry.getValue();
                jo.put("RecordID",catalog.mSerialID);
                jo.put("ParentID",order.id);
                jo.put("SalesPrice",catalog.mSalesPrice);
                jo.put("Barcode",catalog.mBarcode);
                jo.put("ENGItemName",catalog.mENGItemName);
                jo.put("PurchasePrice",catalog.mPurchasePrice);
                jo.put("ExportRebatesRate",catalog.mRebate);
                jo.put("Unit",catalog.mUnit);
                jo.put("ENGSpecification",catalog.mENGSpecification);
                jo.put("Packaging",catalog.mPacking+"/" +catalog.mOuterCapacity+"/" +catalog.mMinimumQty+"/" +catalog.mCanBill);
                jo.put("Factory",catalog.mSupplierShortName+"/" +catalog.mSupplierItemNo);
                jo.put("Memo1",catalog.mOuterGrossWeight+"/" + catalog.mOuterNetWeight+"/" + catalog.mOuterVolume);
                chiletable.put(jo);
            }
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

}
