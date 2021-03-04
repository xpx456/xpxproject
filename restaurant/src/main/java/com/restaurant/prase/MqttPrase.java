package com.restaurant.prase;

import android.content.Intent;

import com.restaurant.entity.GuestFinger;
import com.restaurant.view.RestaurantApplication;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;

public class MqttPrase {

    public static final String ACTION_SET_DELY = "ACTION_SET_DELY";
    public static final String ACTION_UPDATA_TIME = "ACTION_UPDATA_TIME";
    public static final String ACTION_SET_PASSWORD = "ACTION_SET_PASSWORD";
    public static final String ACTION_UPDATA_ALWAYS_OPEN = "ACTION_UPDATA_ALWAYS_OPEN";
    public static final String ACTION_UPDATA_ALWAYS_CLOSE = "ACTION_UPDATA_ALWAYS_CLOSE";
    public static final String ACTION_ADD_GUEST = "ACTION_SAVE_GUEST";
    public static final String ACTION_DELETE_GUEST = "ACTION_DELETE_GUEST";
    public static final String ACTION_UPDATA_GUEST = "ACTION_UPDATA_GUEST";
    public static final String ACTION_OPEN_DOOR = "ACTION_OPEN_DOOR";
    public static final String ACTION_CLEAN_GUEST = "ACTION_CLEAN_GUEST";
    public static String praseMessage(String json)
    {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            String cmd = jsonObject.getString("routeCmd");
            if(cmd.equals("syncAccessSetting"))
            {
                return praseAlwaysOpen(json);
            }
            else if(cmd.equals("syncConfigTime"))
            {
                return praseDelyTime(json);
            }
            else if(cmd.equals("syncEmpInfo"))
            {
                return praseGuest(json);
            }
            else if(cmd.equals("syncEqptType"))
            {
                return praseCheckOnline(json);
            }
            else if(cmd.equals("syncOpenCommand"))
            {
                praseOpen(json);
            }
            else if(cmd.equals("syncSystemTime"))
            {
                return praseTime(json);
            }
            else if(cmd.equals("syncEqptPassword"))
            {
                return setPassword(json);
            }
            else if(cmd.equals("clearEqptAuth"))
            {
                return cleanGuests(json);
            }
            else
            {
                praseCallback(json);
            }
            return json;

        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }
    }

    public static String praseAlwaysOpen(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            String code = jsonObject.getString("entFeatureCode");
            String start = jsonObject.getString("startTime");
            String end = jsonObject.getString("endTime");
            if(start.length() == 19 && end.length() == 19 && (code.equals("01") || code.equals("02")))
            {
                jsonObject.put("success",true);
                jsonObject.put("msg","");
                Intent intent = new Intent();
                if(code.equals("01") )
                {
                     intent = intent.setAction(ACTION_UPDATA_ALWAYS_OPEN);
                }
                else
                {
                    intent = intent.setAction(ACTION_UPDATA_ALWAYS_CLOSE);
                }
                intent.putExtra("code",code);
                intent.putExtra("start",start);
                intent.putExtra("end",end);
                RestaurantApplication.mApp.sendBroadcast(intent);
            }
            else
            {
                jsonObject.put("success",false);
                if(!(code.equals("01") || code.equals("02")))
                    jsonObject.put("msg","开关代码不对");
                if(start.length() != 19)
                    jsonObject.put("msg","开始时间未获取到时间或者时间格式不对");
                if(end.length() != 19)
                    jsonObject.put("msg","结束时间未获取到时间或者时间格式不对");
            }
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();


            return json;
        }
    }

    public static String praseDelyTime(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            String time = jsonObject.getString("delayTime");
            String screen = jsonObject.getString("screenTime");
            String connect = jsonObject.getString("uploadUnconnectTime");
            if(time.length() > 0)
            {
                jsonObject.put("success",true);
                jsonObject.put("msg","");
                Intent intent = new Intent(ACTION_SET_DELY);
                if(time.length() > 0)
                    intent.putExtra("dely",Integer.valueOf(time));
                if(screen.length() > 0)
                    intent.putExtra("screen",Integer.valueOf(screen));
                if(connect.length() > 0)
                    intent.putExtra("connect",Integer.valueOf(connect));
                RestaurantApplication.mApp.sendBroadcast(intent);
            }
            else
            {
                jsonObject.put("success",false);
                jsonObject.put("msg","未获取到时间或者时间格式不对");
            }
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();


            return json;
        }

    }

    public static String setPassword(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            String pass = jsonObject.getString("password");
            jsonObject.put("success",true);
            jsonObject.put("msg","");
            Intent intent = new Intent(ACTION_SET_PASSWORD);
            intent.putExtra("pass",pass);
            RestaurantApplication.mApp.sendBroadcast(intent);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();


            return json;
        }

    }

    public static String praseTime(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            String time = jsonObject.getString("systemTime");
            if(time.length() == 19)
            {
                jsonObject.put("success",true);
                jsonObject.put("msg","");
                Intent intent = new Intent(ACTION_UPDATA_TIME);
                intent.putExtra("time",time);
                RestaurantApplication.mApp.sendBroadcast(intent);
            }
            else
            {
                jsonObject.put("success",false);
                jsonObject.put("msg","未获取到时间或者时间格式不对");
            }
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();


            return json;
        }

    }

    public static String praseGuest(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            ArrayList<GuestFinger> fingers = new ArrayList<GuestFinger>();
            XpxJSONArray ja = jsonObject.getJSONArray("veinList");
            GuestFinger guest = new GuestFinger();
            guest.rid = jsonObject.getString("empAuthInfoId");
            guest.name = jsonObject.getString("empName");
            guest.licence = jsonObject.getString("icCardNo");
            guest.type = jsonObject.getString("isLock");
            guest.canCard = jsonObject.getString("isCardPass");
            guest.canFinger = jsonObject.getString("isVeinPass");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                GuestFinger guest1 = new GuestFinger();
                guest1.rid = jsonObject.getString("empAuthInfoId");
                guest1.name = jsonObject.getString("empName");
                guest1.licence = jsonObject.getString("icCardNo");
                guest1.type = jsonObject.getString("isLock");
                guest1.canCard = jsonObject.getString("isCardPass");
                guest1.canFinger = jsonObject.getString("isVeinPass");
                XpxJSONObject jo = ja.getJSONObject(i);
                guest1.finger = jo.getString("veinCode");
                fingers.add(guest1);

            }

            if(jsonObject.has("operateType"))
            {
                if(jsonObject.getString("operateType").equals("0"))
                {
                    if(checkData(jsonObject,guest))
                    {
                        doAdd(guest,fingers);
                    }
                }
                else if(jsonObject.getString("operateType").equals("1"))
                {
                    if(checkData(jsonObject,guest))
                    {
                        doUpdata(guest,fingers);
                    }
                }
                else if(jsonObject.getString("operateType").equals("2"))
                {
                    if(checkData(jsonObject,guest))
                    {
                        doDelete(guest);
                    }
                }
            }
            else
            {
                jsonObject.put("success",false);
                jsonObject.put("msg","操作方式为空");
            }
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();


            return json;
        }

    }

    public static String praseCheckOnline(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            jsonObject.put("msg","");
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }
    }

    public static String praseOpen(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            jsonObject.put("msg","");
            Intent intent = new Intent(ACTION_OPEN_DOOR);
            RestaurantApplication.mApp.sendBroadcast(intent);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }


    public static void praseCallback(String json)
    {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            String msgid = jsonObject.getString("messageId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static boolean checkData(XpxJSONObject xpxJSONObject,GuestFinger guest)
    {
        try {
            if(guest.rid.length() == 0)
            {
                xpxJSONObject.put("success",false);
                xpxJSONObject.put("msg","员工身份认证信息ID为空");
                return false;
            }
            else if(guest.name.length() == 0)
            {
                xpxJSONObject.put("success",false);
                xpxJSONObject.put("msg","姓名为空");
                return false;
            }
            else if(guest.type.length() == 0)
            {
                xpxJSONObject.put("success",false);
                xpxJSONObject.put("msg","是否锁定为空");
                return false;
            }
            else{
                xpxJSONObject.put("success",true);
                xpxJSONObject.put("msg","");
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String cleanGuests(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            String pass = jsonObject.getString("password");
            jsonObject.put("success",true);
            jsonObject.put("msg","");
            Intent intent = new Intent(ACTION_CLEAN_GUEST);
            RestaurantApplication.mApp.sendBroadcast(intent);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();


            return json;
        }

    }


    public static void doAdd(GuestFinger guest,ArrayList<GuestFinger> guests) {
        Intent intent = new Intent(ACTION_ADD_GUEST);
        intent.putExtra("finger",guest);
        intent.putParcelableArrayListExtra("fingers",guests);
        RestaurantApplication.mApp.sendBroadcast(intent);
    }

    public static void doUpdata(GuestFinger guest,ArrayList<GuestFinger> guests) {
        Intent intent = new Intent(ACTION_UPDATA_GUEST);
        intent.putExtra("finger",guest);
        intent.putParcelableArrayListExtra("fingers",guests);
        RestaurantApplication.mApp.sendBroadcast(intent);
    }

    public static void doDelete(GuestFinger guest) {
        Intent intent = new Intent(ACTION_DELETE_GUEST);
        intent.putExtra("finger",guest);
        RestaurantApplication.mApp.sendBroadcast(intent);
    }
}
