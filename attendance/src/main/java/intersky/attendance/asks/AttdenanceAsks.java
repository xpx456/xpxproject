package intersky.attendance.asks;

import android.content.Context;
import android.os.Handler;

import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;

import intersky.attendance.AttendanceManager;
import intersky.attendance.entity.AttdanceSet;
import intersky.attendance.entity.Attendance;
import intersky.apputils.AppUtils;
import intersky.oa.OaUtils;
import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;

public class AttdenanceAsks {

    public static final String WORK_ATTDENCE_PATH = "api/v1/Attence.html";
    public static final String WORK_ATTDENCE_CONFIG_PATH = "api/v1/AttenceConfig.html";
    public static final String WORK_ATTDENCE_LIST = "get.attence.list";
    public static final String WORK_ATTDENCE_ADD = "add.attence.item";
    public static final String WORK_ATTDENCE_SET = "set.attence.item";
    public static final String WORK_ATTDENCE_CONFIG_ADD = "add.attence.config.item";
    public static final String WORK_ATTDENCE_CONFIG_SET = "set.attence.config.item";
    public static final String WORK_ATTDENCE_CONFIG_DELETE = "del.attence.config.list";
    public static final String WORK_ATTDENCE_CONFIG_LIST = "get.attence.config.list";
    public static final String WORK_ATTDENCE_CONFIG_DETIAL = "get.attence.config.item";

    public static final int EVENT_GET_LIST_SUCCESS = 1160000;
    public static final int EVENT_SAVE_SUCCESS = 1160001;
    public static final int EVENT_UP_SUCCESS = 1160002;
    public static final int EVENT_GET_SET_LIST_SUCCESS = 1160003;
    public static final int EVENT_GET_SET_DETILA= 1160004;
    public static final int EVENT_SET_SAVE_SUCCESS= 1160005;
    public static final int EVENT_SET_DELETE_SUCCESS= 1160006;

    public static void getWorkAttdenaceList(Handler mHandler, Context mContext,String userid,String day) {

        String urlString = OaUtils.getOaUtils().createURLStringoa(AttendanceManager.getInstance().oaUtils.service,WORK_ATTDENCE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_ATTDENCE_LIST);
        items.add(item);
        item = new BasicNameValuePair("company_id", AttendanceManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", userid);
        items.add(item);
        item = new BasicNameValuePair("begin_time", day+" 00:00:00");
        items.add(item);
        item = new BasicNameValuePair("end_time", day+" 23:59:59");
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_LIST_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void doSign(Handler mHandler, Context mContext,String address,double x,double y) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(AttendanceManager.getInstance().oaUtils.service,WORK_ATTDENCE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_ATTDENCE_ADD);
        items.add(item);
        item = new BasicNameValuePair("company_id", AttendanceManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", AttendanceManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("address", address);
        items.add(item);
        LatLonPoint point = AppUtils.toWGS84Point(x,y);
        item = new BasicNameValuePair("latitude",  String.valueOf(point.getLongitude()) + "," + String.valueOf(point.getLatitude()));
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_SAVE_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void doUpdateSign(Handler mHandler, Context mContext, String address , Attendance mWorkAttendance, double x, double y) {


        String urlString = OaUtils.getOaUtils().createURLStringoa(AttendanceManager.getInstance().oaUtils.service,WORK_ATTDENCE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_ATTDENCE_SET);
        items.add(item);
        item = new BasicNameValuePair("company_id", AttendanceManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", AttendanceManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("attence_id", mWorkAttendance.getmAttenceId());
        items.add(item);
        item = new BasicNameValuePair("address", address);
        items.add(item);
        LatLonPoint point = AppUtils.toWGS84Point(x,y);
        item = new BasicNameValuePair("latitude",  String.valueOf(point.getLongitude()) + "," + String.valueOf(point.getLatitude()));
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_SAVE_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getSetList(Handler mHandler, Context mContext)
    {

        String urlString = OaUtils.getOaUtils().createURLStringoa(AttendanceManager.getInstance().oaUtils.service,WORK_ATTDENCE_CONFIG_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_ATTDENCE_CONFIG_LIST);
        items.add(item);
        item = new BasicNameValuePair("company_id", AttendanceManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", AttendanceManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_SET_LIST_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getDetial(Handler mHandler, Context mContext, AttdanceSet attdanceSet)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(AttendanceManager.getInstance().oaUtils.service,WORK_ATTDENCE_CONFIG_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_ATTDENCE_CONFIG_DETIAL);
        items.add(item);
        item = new BasicNameValuePair("company_id", AttendanceManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", AttendanceManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("attence_config_id", attdanceSet.aId);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_GET_SET_DETILA, mContext, items,attdanceSet);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void saveSet(Handler mHandler, Context mContext, AttdanceSet attdanceSet)
    {

        String urlString = OaUtils.getOaUtils().createURLStringoa(AttendanceManager.getInstance().oaUtils.service,WORK_ATTDENCE_CONFIG_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item;
        if(attdanceSet.aId.length() > 0)
        {
            item = new BasicNameValuePair("method", WORK_ATTDENCE_CONFIG_SET);
            items.add(item);
            item = new BasicNameValuePair("attence_config_id", attdanceSet.aId);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("method", WORK_ATTDENCE_CONFIG_ADD);
            items.add(item);

        }
        item = new BasicNameValuePair("company_id", AttendanceManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", AttendanceManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("name", attdanceSet.name);
        items.add(item);
        item = new BasicNameValuePair("to_work", attdanceSet.start);
        items.add(item);
        item = new BasicNameValuePair("off_work", attdanceSet.end);
        items.add(item);
        item = new BasicNameValuePair("day", attdanceSet.dayid);
        items.add(item);
        item = new BasicNameValuePair("personnel", attdanceSet.copyer);
        items.add(item);
        LatLonPoint point = AppUtils.toWGS84Point(attdanceSet.x,attdanceSet.y);
        item = new BasicNameValuePair("latitude",  String.valueOf(point.getLongitude()) + "," + String.valueOf(point.getLatitude()));
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_SET_SAVE_SUCCESS, mContext, items,attdanceSet);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);

    }

    public static void doDelete(Handler mHandler, Context mContext, AttdanceSet attdanceSet)
    {

        String urlString = OaUtils.getOaUtils().createURLStringoa(AttendanceManager.getInstance().oaUtils.service,WORK_ATTDENCE_CONFIG_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", WORK_ATTDENCE_CONFIG_DELETE);
        items.add(item);
        item = new BasicNameValuePair("company_id", AttendanceManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", AttendanceManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("attence_config_id", attdanceSet.aId);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_SET_DELETE_SUCCESS, mContext, items,attdanceSet);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);


    }
}
