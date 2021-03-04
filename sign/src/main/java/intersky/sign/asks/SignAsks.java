package intersky.sign.asks;

import android.content.Context;
import android.os.Handler;

import com.amap.api.services.core.LatLonPoint;


import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.oa.OaUtils;
import intersky.sign.SignManager;
import intersky.sign.entity.Sign;
import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;


public class SignAsks {

    public static final String SIGN_PATH = "api/v1/SignIn.html";
    public static final String SIGN_LIST = "get.signin.list";
    public static final String SIGN_ADD = "add.signin.item";


    public static final int EVENT_SAVE_SIGN_SUCCESS = 1230001;
    public static final int EVENT_GET_LIST_SUCCESS = 1230002;

    public static void getSignList(Handler mHandler, Context mContext,String userid,int page,String day) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(SignManager.getInstance().oaUtils.service,SIGN_PATH);

        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", SIGN_LIST);
        items.add(item);
        item = new BasicNameValuePair("company_id", SignManager.mSignModul.oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", userid);
        items.add(item);
        item = new BasicNameValuePair("page_no", String.valueOf(page));
        items.add(item);
        item = new BasicNameValuePair("page_size", "20");
        items.add(item);
        item = new BasicNameValuePair("begin_time", day+" 00:00:00");
        items.add(item);
        item = new BasicNameValuePair("end_time", day+" 23:59:59");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_LIST_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getSignHit(Handler mHandler, Context mContext,String userid) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(SignManager.getInstance().oaUtils.service,SIGN_PATH);

        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", SIGN_LIST);
        items.add(item);
        item = new BasicNameValuePair("company_id", SignManager.mSignModul.oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", userid);
        items.add(item);
        item = new BasicNameValuePair("page_no", "1");
        items.add(item);
        item = new BasicNameValuePair("page_size", "20");
        items.add(item);
        item = new BasicNameValuePair("begin_time", TimeUtils.getDate() +" 00:00:00");
        items.add(item);
        item = new BasicNameValuePair("end_time", TimeUtils.getDate()+" 23:59:59");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_LIST_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void saveSign(Handler mHandler, Context mContext, Sign sign) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(SignManager.getInstance().oaUtils.service,SIGN_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", SIGN_ADD);
        items.add(item);
        item = new BasicNameValuePair("company_id", SignManager.mSignModul.oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", SignManager.mSignModul.oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("address", sign.mAddress);
        items.add(item);
        LatLonPoint point = AppUtils.toWGS84Point(sign.mAltitude,sign.mLongitude);
        item = new BasicNameValuePair("latitude", String.valueOf(point.getLongitude()) + "," + String.valueOf(point.getLatitude()));
        items.add(item);
        item = new BasicNameValuePair("image", sign.mImage);
        items.add(item);
        item = new BasicNameValuePair("reason", sign.mReason);
        items.add(item);
        item = new BasicNameValuePair("remark", sign.mRemark);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_SAVE_SIGN_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }


}
