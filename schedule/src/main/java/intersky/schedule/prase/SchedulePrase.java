package intersky.schedule.prase;

import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.mywidget.CalendarEventHelper;
import intersky.schedule.R;
import intersky.schedule.entity.Event;
import intersky.select.entity.Select;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class SchedulePrase {

    public static void praseEvent(NetObject net, CalendarEventHelper calendarEventHelper) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;

            JSONObject jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            if (ja.length() > 0) {
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    Event temp = new Event(jo.getString("schedule_time")
                            , String.valueOf(jo.get("content")), Integer.valueOf(String.valueOf(jo.get("status"))), String.valueOf(jo.get("schedule_id"))
                            , jo.getString("user_id"), jo.getString("company_id"), jo.getString("remind"));
                    calendarEventHelper.addEvent(temp);
                    if(!temp.keyDay.equals(TimeUtils.getDate()) && !temp.keyDay.equals(String.format("%04d-%02d-%02d", calendarEventHelper.choise.getSolar()[0],
                            calendarEventHelper.choise.getSolar()[1],calendarEventHelper.choise.getSolar()[2])))
                    {
                        View view1 = calendarEventHelper.lasthashView.get(temp.keyDay);
                        if(view1 != null)
                        {
                            view1.setBackgroundResource(R.drawable.shape_bg_cycle_blue);
                        }
                        View view2 = calendarEventHelper.hashView.get(temp.keyDay);
                        if(view2 != null)
                        {
                            view2.setBackgroundResource(R.drawable.shape_bg_cycle_blue);
                        }
                        View view3 = calendarEventHelper.nexthashView.get(temp.keyDay);
                        if(view3 != null)
                        {
                            view3.setBackgroundResource(R.drawable.shape_bg_cycle_blue);
                        }
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseRemind(NetObject net, ArrayList<Select> mSelects) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;

            JSONObject object = new JSONObject(json);
            JSONArray ja = object.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Select mSelectMoreModel = new Select(jo.getString("remind_id"), jo.getString("remind_status"));
                if(i == 0)
                    mSelectMoreModel.iselect = true;
                mSelects.add(mSelectMoreModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseRemind(NetObject net, ArrayList<Select> mSelects, Event mEvent) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;

            JSONObject object = new JSONObject(json);
            JSONArray ja = object.getJSONArray("data");
            String[] strs = mEvent.mReminds.split(",");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Select mSelectMoreModel = new Select(jo.getString("remind_id"), jo.getString("remind_status"));
                if(mEvent.mReminds.length() == 0)
                {
                    if(i == 0)
                    {
                        mSelectMoreModel.iselect = true;
                    }
                }
                else
                {
                    for (int j = 0; j < strs.length; j++) {
                        if (strs[j].equals(mSelectMoreModel.mId)) {
                            mSelectMoreModel.iselect = true;
                        }
                    }
                }

                mSelects.add(mSelectMoreModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
