package intersky.schedule.asks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

import intersky.oa.OaUtils;
import intersky.schedule.ScheduleManager;
import intersky.schedule.entity.Event;
import intersky.xpxnet.net.BasicNameValuePair;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetTaskManager;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.nettask.PostNetTask;

public class ScheduleAsks {

    public static final String SCHEDULE_PATH =  "api/v1/Schedule.html";
    public static final String SCHEDULE_EVENT_LIST =  "get.schedule.list";
    public static final String SCHEDULE_EVENT_REMIND_LIST =  "get.schedule.remind.list";
    public static final String SCHEDULE_EVENT_ADD =  "add.schedule.item";
    public static final String SCHEDULE_EVENT_EDIT =  "set.schedule.item";
    public static final String SCHEDULE_EVENT_GET =  "get.schedule.item";
    public static final String SCHEDULE_EVENT_DELETE =  "del.schedule.item";

    public static final int EVENT_LIST_SUCCESS = 1230000;
    public static final int EVENT_REMIND_SUCCESS = 1230001;
    public static final int EVENT_SAVE_SUCCESS = 1230002;
    public static final int EVENT_DELETE_SUCCESS = 1230003;

    public static void getEvents(Context mContext, String userid, Handler mHandler, String beginTime, String endTime)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(ScheduleManager.getInstance().oaUtils.service,SCHEDULE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", SCHEDULE_EVENT_LIST);
        items.add(item);
        item = new BasicNameValuePair("company_id", ScheduleManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", userid);
        items.add(item);
        if(beginTime.equals(endTime))
        {
            item = new BasicNameValuePair("begin_time", beginTime+" 00:00:00");
            items.add(item);
            item = new BasicNameValuePair("end_time", endTime+" 23:59:59");
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("begin_time", beginTime+" 00:00:00");
            items.add(item);
            item = new BasicNameValuePair("end_time", endTime+" 00:00:00");
            items.add(item);
        }

        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_LIST_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void getReminds(Context mContext, Handler mHandler)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(ScheduleManager.getInstance().oaUtils.service,SCHEDULE_PATH);

        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", SCHEDULE_EVENT_REMIND_LIST);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_REMIND_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void saveEvent(Context mContext, Handler mHandler, Event event) {
        String urlString = OaUtils.getOaUtils().createURLStringoa(ScheduleManager.getInstance().oaUtils.service,SCHEDULE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item;
        if(event.mScheduleid.length() == 0)
        {
            item = new BasicNameValuePair("method", SCHEDULE_EVENT_ADD);
            items.add(item);
        }
        else
        {
            item = new BasicNameValuePair("method", SCHEDULE_EVENT_EDIT);
            items.add(item);
            item = new BasicNameValuePair("schedule_id", event.mScheduleid);
            items.add(item);
        }
        item = new BasicNameValuePair("company_id", ScheduleManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", ScheduleManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("content", event.mContent);
        items.add(item);
        item = new BasicNameValuePair("schedule_time", event.mTime);
        items.add(item);
        item = new BasicNameValuePair("remind", event.mReminds);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_SAVE_SUCCESS, mContext, items);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

    public static void deleteEvent(Context mContext,Handler mHandler, Event event)
    {
        String urlString = OaUtils.getOaUtils().createURLStringoa(ScheduleManager.getInstance().oaUtils.service,SCHEDULE_PATH);
        ArrayList<NameValuePair> items = new ArrayList<NameValuePair>();
        BasicNameValuePair item = new BasicNameValuePair("method", SCHEDULE_EVENT_DELETE);
        items.add(item);
        item = new BasicNameValuePair("company_id", ScheduleManager.getInstance().oaUtils.mAccount.mCompanyId);
        items.add(item);
        item = new BasicNameValuePair("user_id", ScheduleManager.getInstance().oaUtils.mAccount.mRecordId);
        items.add(item);
        item = new BasicNameValuePair("schedule_id", event.mScheduleid);
        items.add(item);
        PostNetTask mPostNetTask = new PostNetTask(urlString, mHandler, EVENT_DELETE_SUCCESS, mContext, items,event);
        NetTaskManager.getInstance().addNetTask(mPostNetTask);
    }

}
