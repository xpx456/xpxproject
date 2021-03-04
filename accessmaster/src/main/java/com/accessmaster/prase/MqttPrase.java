package com.accessmaster.prase;

import android.content.Intent;

import com.accessmaster.asks.MqttAsks;
import com.accessmaster.entity.GuestFinger;
import com.accessmaster.view.AccessMasterApplication;

import org.json.JSONException;

import java.util.HashMap;

import intersky.json.XpxJSONObject;

public class MqttPrase {

    public static final String ACTION_SET_DELY = "ACTION_SET_DELY";
    public static final String ACTION_UPDATA_TIME = "ACTION_UPDATA_TIME";
    public static final String ACTION_UPDATA_ALWAYS_OPEN = "ACTION_UPDATA_ALWAYS_OPEN";
    public static final String ACTION_UPDATA_ALWAYS_CLOSE = "ACTION_UPDATA_ALWAYS_CLOSE";
    public static final String ACTION_ADD_GUEST = "ACTION_SAVE_GUEST";
    public static final String ACTION_DELETE_GUEST = "ACTION_DELETE_GUEST";
    public static final String ACTION_UPDATA_GUEST = "ACTION_UPDATA_GUEST";
    public static final String ACTION_OPEN_DOOR = "ACTION_OPEN_DOOR";
    public static final String ACTION_OPEN_CONTACT = "ACTION_OPEN_CONTACT";
    public static final String ACTION_ADD_DEVICE = "ACTION_ADD_DEVICE";
    public static final String ACTION_OTHER_CONNECT = "ACTION_OTHER_CONNECT";
    public static final String ACTION_SENT_MATER = "ACTION_SENT_MATER";
    public static final String ACTION_STOP_CONNECT = "ACTION_STOP_CONNECT";
    public static final String ACTION_SET_PASSWORD = "ACTION_SET_PASSWORD";
    public static final String ACTION_DEVICE_LEAVE = "ACTION_DEVICE_LEAVE";
    public static final String ACTION_ACCEPT_ICE = "ACTION_ACCEPT_ICE";

    public static final String ACTION_ANSWER_RECEIVER = "ACTION_ANSWER_RECEIVER";
    public static final String ACTION_RECEIVER_ICE = "ACTION_RECEIVER_ICE";
    public static final String ACTION_EXIST = "ACTION_EXIST";
    public static final String ACTION_BUSY = "ACTION_BUSY";
    public static final String ACTION_ASK_SEND_ICE = "ACTION_ASK_SEND_ICE";
    public static final String ACTION_LIVE_BACK = "ACTION_LIVE_BACK";

    public static String[] praseGuest2(String json) {
        String [] back = new String[2];
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            String cmd = jsonObject.getString("action");
            back[1] = jsonObject.getString("cid");
            if(cmd.equals(MqttAsks.ACTION_CONTACT))
            {
                doContact(json);
                back[0] = "";
            }
            else if(cmd.equals(MqttAsks.ACTION_IMF))
            {
                addDevice(json);
                back[0] = "";

            }
            else if(cmd.equals(MqttAsks.ACTION_GET_MASTER))
            {
                getMaster(json);
                back[0] = "";

            }
            else if(cmd.equals(MqttAsks.ACTION_ACCEPT_BACK))
            {
                connectBack(json);
                back[0] = "";
            }
            else if(cmd.equals(MqttAsks.ACTION_STOP_CONNECT))
            {
                stopConnect(json);
                back[0] = "";

            }
            else if(cmd.equals(MqttAsks.ACTION_ACCEPT_ICE))
            {
                acceptIce(json);
                back[0] = "";

            }
            else if(cmd.equals(MqttAsks.SEND_ANSWWER))
            {
                receiveAnswer(json);
                back[0] = "";

            }
            else if(cmd.equals(MqttAsks.SEND_ICE))
            {
                receiveIce(json);
                back[0] = "";

            }
            else if(cmd.equals(MqttAsks.EXIST))
            {
                exist(json);
                back[0] = "";

            }
            else if(cmd.equals(MqttAsks.ASK_SEND_ICE))
            {
                askSendIce(json);
                back[0] = "";

            }
            else if(cmd.equals(MqttAsks.ACTION_BUESY))
            {
                busy(json);
                back[0] = "";

            }
            else if(cmd.equals(MqttAsks.LIVE_BACK))
            {
                liveBack(json);
                back[0] = "";

            }
            else
            {
                praseCallback(json);
                back[0] = "";
            }
            return back;
        } catch (JSONException e) {
            e.printStackTrace();
            return back;
        }
    }

    public static void praseSys(String json)
    {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            if(jsonObject.has("reason"))
            {
                Intent intent = new Intent(ACTION_DEVICE_LEAVE);
                jsonObject.has("clientid");
                {
                    intent.putExtra("cid",jsonObject.getString("clientid"));
                    AccessMasterApplication.mApp.sendBroadcast(intent);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String praseMessage(String json,HashMap<String,String> send) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            String cmd = jsonObject.getString("routeCmd");
            if(cmd.equals("syncAccessSetting"))
            {
                return praseAlwaysOpen(json);
            }
            else if(cmd.equals("syncDelayTime"))
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
                return praseOpen(json);
            }
            else if(cmd.equals("syncSystemTime"))
            {
                return praseTime(json);
            }
            else if(cmd.equals("syncEqptPassword"))
            {
                return setPassword(json);
            }
            else
            {
                praseCallback(json);
            }
            return "";

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
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
                AccessMasterApplication.mApp.sendBroadcast(intent);
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
                AccessMasterApplication.mApp.sendBroadcast(intent);
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
            AccessMasterApplication.mApp.sendBroadcast(intent);
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
                AccessMasterApplication.mApp.sendBroadcast(intent);
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
            GuestFinger guest = new GuestFinger();
            guest.rid = jsonObject.getString("empAuthInfoId");
            guest.name = jsonObject.getString("empName");
            guest.licence = jsonObject.getString("icCardNo");
            guest.type = jsonObject.getString("isLock");
            guest.finger = jsonObject.getString("fingerCode");
            if(jsonObject.has("operateType"))
            {
                if(jsonObject.getString("operateType").equals("0"))
                {
                    if(checkData(jsonObject,guest))
                    {
                        doAdd(guest);
                    }
                }
                else if(jsonObject.getString("operateType").equals("1"))
                {
                    if(checkData(jsonObject,guest))
                    {
                        doUpdata(guest);
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
            AccessMasterApplication.mApp.sendBroadcast(intent);
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


    public static String doContact(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            Intent intent = new Intent(ACTION_OPEN_CONTACT);
            intent.putExtra("cid",jsonObject.getString("cid"));
            intent.putExtra("cname",jsonObject.getString("cname"));
            if(jsonObject.has("mid"))
            {
                intent.putExtra("mid",jsonObject.getString("mid"));
            }
            intent.putExtra("caddress",jsonObject.getString("caddress"));
            AccessMasterApplication.mApp.sendBroadcast(intent);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }

    public static String addDevice(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            Intent intent = new Intent(ACTION_ADD_DEVICE);
            intent.putExtra("cid",jsonObject.getString("cid"));
            intent.putExtra("cname",jsonObject.getString("cname"));
            intent.putExtra("caddress",jsonObject.getString("caddress"));
            AccessMasterApplication.mApp.sendBroadcast(intent);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }

    public static String getMaster(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);

            Intent intent = new Intent(ACTION_SENT_MATER);
            intent.putExtra("cid",jsonObject.getString("cid"));
            AccessMasterApplication.mApp.sendBroadcast(intent);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }

    public static String connectBack(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            Intent intent = new Intent(ACTION_OTHER_CONNECT);
            intent.putExtra("mid",jsonObject.getString("mid"));
            intent.putExtra("mname",jsonObject.getString("mname"));
            AccessMasterApplication.mApp.sendBroadcast(intent);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }

    public static String stopConnect(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            Intent intent = new Intent(ACTION_STOP_CONNECT);
            intent.putExtra("cid",jsonObject.getString("cid"));
            AccessMasterApplication.mApp.sendBroadcast(intent);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }

    public static String acceptIce(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            Intent intent = new Intent(ACTION_ACCEPT_ICE);
            intent.putExtra("cid",jsonObject.getString("cid"));
            AccessMasterApplication.mApp.sendBroadcast(intent);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }

    public static String receiveAnswer(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            Intent intent = new Intent(ACTION_ANSWER_RECEIVER);
            intent.putExtra("cid",jsonObject.getString("cid"));
            intent.putExtra("spd",jsonObject.getString("spd"));
            AccessMasterApplication.mApp.sendBroadcast(intent);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }


    public static String receiveIce(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            Intent intent = new Intent(ACTION_RECEIVER_ICE);
            intent.putExtra("cid",jsonObject.getString("cid"));
            intent.putExtra("label",jsonObject.getInt("label",0));
            intent.putExtra("id",jsonObject.getString("id"));
            intent.putExtra("candidate",jsonObject.getString("candidate"));
            AccessMasterApplication.mApp.sendBroadcast(intent);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }

    public static String askSendIce(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            Intent intent = new Intent(ACTION_ASK_SEND_ICE);
            intent.putExtra("cid",jsonObject.getString("cid"));
            AccessMasterApplication.mApp.sendBroadcast(intent);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }


    public static String exist(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            Intent intent = new Intent(ACTION_EXIST);
            intent.putExtra("cid",jsonObject.getString("cid"));
            intent.putExtra("msg",jsonObject.getString("msg"));
            AccessMasterApplication.mApp.sendBroadcast(intent);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }

    public static String busy(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            Intent intent = new Intent(ACTION_BUSY);
            intent.putExtra("cid",jsonObject.getString("cid"));
            intent.putExtra("msg",jsonObject.getString("msg"));
            AccessMasterApplication.mApp.sendBroadcast(intent);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }

    public static String liveBack(String json) {
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            jsonObject.put("success",true);
            Intent intent = new Intent(ACTION_LIVE_BACK);
            intent.putExtra("cid",jsonObject.getString("cid"));
            AccessMasterApplication.mApp.sendBroadcast(intent);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return json;
        }

    }


    public static void doAdd(GuestFinger guest) {
        Intent intent = new Intent(ACTION_ADD_GUEST);
        intent.putExtra("finger",guest);
        AccessMasterApplication.mApp.sendBroadcast(intent);
    }

    public static void doUpdata(GuestFinger guest) {
        Intent intent = new Intent(ACTION_UPDATA_GUEST);
        intent.putExtra("finger",guest);
        AccessMasterApplication.mApp.sendBroadcast(intent);
    }

    public static void doDelete(GuestFinger guest) {
        Intent intent = new Intent(ACTION_DELETE_GUEST);
        intent.putExtra("finger",guest);
        AccessMasterApplication.mApp.sendBroadcast(intent);
    }
}
