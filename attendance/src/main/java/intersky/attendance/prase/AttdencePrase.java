package intersky.attendance.prase;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.attendance.AttendanceManager;
import intersky.attendance.entity.AttdanceSet;
import intersky.attendance.entity.Attendance;
import intersky.apputils.AppUtils;
import intersky.select.entity.Select;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class AttdencePrase {

    public static void praseList(NetObject net, Context context, ArrayList<Attendance> mWorkAttendances) {

        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }

        mWorkAttendances.clear();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = array.getJSONObject(i);
                String latitude = String.valueOf(jo.get("latitude"));
                if (latitude.equals("29.865664121.604691")) {
                    latitude = "29.865664,121.604691";
                }
                if (latitude == null) {
                    continue;
                }
                latitude = AppUtils.getNumber(latitude);
                int a = latitude.indexOf(",");
                String x = AppUtils.getNumber(latitude.substring(a + 1, latitude.length()));
                String y = AppUtils.getNumber(latitude.substring(0, a));
                String text = "";
                if (jo.has("text")) {
                    text = jo.getString("text");
                }
                LatLonPoint point = AppUtils.toGCJ02Piont(Double.valueOf(x), Double.valueOf(y));
                mWorkAttendances.add(new Attendance(Integer.valueOf(String.valueOf(jo.get("status")))
                        , jo.getString("attence_id"), point.getLatitude(), point.getLongitude()
                        , jo.getString("user_id"), jo.getString("company_id"), jo.getString("attence_group_id"), jo.getString("address")
                        , jo.getString("attence_time"), jo.getString("create_time"), text));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseSetList(NetObject net, Context context, ArrayList<AttdanceSet> mAttdanceSets) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return;
            }

            JSONObject jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            for(int i =0 ; i < ja.length() ; i++)
            {
                JSONObject jo = ja.getJSONObject(i);
                String a =jo.getString("day");
                String b = AttendanceManager.initDayString(a);
                if(b.length() == 0 && a.length() != 0)
                {
                    String[] strs = a.split(",");
                    for(int j = 0 ; j < strs.length ; j++)
                    {
                        if(j != 0)
                        {
                            b += ","+ AttendanceManager.getweek(Integer.valueOf(strs[j]));
                        }
                        else
                        {
                            b += AttendanceManager.getweek(Integer.valueOf(strs[j]));
                        }
                    }
                }
                String latitude = String.valueOf(jo.get("latitude"));
                if (latitude.equals("29.865664121.604691")) {
                    latitude = "29.865664,121.604691";
                }
                if (latitude == null) {
                    continue;
                }
                latitude = AppUtils.getNumber(latitude);
                int c = latitude.indexOf(",");
                String x = AppUtils.getNumber(latitude.substring(c + 1, latitude.length()));
                String y = AppUtils.getNumber(latitude.substring(0, c));
                AttdanceSet attdanceSet = new AttdanceSet(jo.getString("attence_config_id")
                        ,jo.getString("user_id"),jo.getString("company_id"),jo.getString("name"),
                        jo.getString("to_work"),jo.getString("off_work"),b,a);
                attdanceSet.x = Double.valueOf(x);
                attdanceSet.y = Double.valueOf(y);
                mAttdanceSets.add(attdanceSet);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseDetial(NetObject net,Context context,ArrayList<Select> mReminds) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return;
            }
            AttdanceSet attdanceSet = (AttdanceSet) net.item;
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jo = jsonObject.getJSONObject("data");
            attdanceSet.name = jo.getString("name");
            attdanceSet.start = jo.getString("to_work");
            attdanceSet.end = jo.getString("off_work");
            String a =jo.getString("day");
            attdanceSet.dayid = jo.getString("day");
            if(jo.has("personnel"))
            {
                JSONArray ja = jo.getJSONArray("personnel");
                for(int i = 0 ; i < ja.length() ; i ++)
                {
                    JSONObject jo2 = ja.getJSONObject(i);
                    if(i == 0)
                    {
                        attdanceSet.copyer += jo2.getString("user_id");
                    }
                    else
                    {
                        attdanceSet.copyer += ","+jo2.getString("user_id");
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean praseData(NetObject net, Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        return  true;
    }
}
