package intersky.sign.prase;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import intersky.apputils.AppUtils;
import intersky.sign.SignManager;
import intersky.sign.entity.Sign;
import intersky.sign.view.adapter.SignAdapter;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class SignPrase {

    public static void praseSignHit(NetObject net, Context context) {
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
            JSONObject jo = ja.getJSONObject(ja.length()-1);
            SignManager.getInstance().signHit = jo.getInt("total_results");
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


    public static void praseSign(NetObject net, Context context, SignAdapter adapter) {
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
            JSONObject mjson = new JSONObject(json);
            JSONArray array = mjson.getJSONArray("data");

            for (int i = 0; i < array.length() - 1; i++) {
                JSONObject item = array.getJSONObject(i);

                String latitude = String.valueOf(item.get("latitude"));
                if(latitude == null || latitude.contains("{"))
                {
                    continue;
                }
                String[] strs = latitude.split(",");
                LatLonPoint point = AppUtils.toGCJ02Piont(Double.valueOf(strs[1]), Double.valueOf(strs[0]));
                String detetime = item.getString("create_time");
                String dete = detetime.substring(0, 10);
                String time = detetime.substring(11, 19);
                adapter.mAttendanceModels.add(new Sign(item.getString("signin_id"), item.getString("user_id"), point.getLatitude(), point.getLongitude()
                        , item.getString("address"), dete, time, item.getString("address"), item.getString("reason"), item.getString("remark"),item.getString("image")));

            }
            adapter.notifyDataSetChanged();
            JSONObject jo = array.getJSONObject(array.length() - 1);
            if(jo.getInt("total_results") > adapter.mAttendanceModels.size())
            {
                adapter.page++;
            }
            else
            {
                adapter.isall = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
