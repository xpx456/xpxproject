package com.accessmaster.prase;

import android.content.Context;


import com.accessmaster.entity.Device;
import com.accessmaster.entity.Location;
import com.accessmaster.view.AccessMasterApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.apputils.AppUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class HttpPrase {

    public static void praseLocation(NetObject net, Context context) {
        try {
            String json = net.result;
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("data");
            AccessMasterApplication.mApp.locationHashMap.clear();
            AccessMasterApplication.mApp.grops.clear();
            AccessMasterApplication.mApp.locations.clear();
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                Location location = new Location();
                location.addressid = jo.getString("eqptLocationId");
                location.address = jo.getString("eqptLocation");
                location.parentid = jo.getString("parentId");
                AccessMasterApplication.mApp.locationHashMap.put(location.addressid,location);
                location.locations = AccessMasterApplication.mApp.grops.get(location.addressid);
                if(location.locations == null) {
                    location.locations = new ArrayList<Location>();
                    AccessMasterApplication.mApp.grops.put(location.addressid,location.locations);
                }
                if(location.parentid.length() > 0 && !location.parentid.equals("0"))
                {
                    ArrayList<Location> locations =  AccessMasterApplication.mApp.grops.get(location.parentid);
                    if(locations == null) {
                        locations = new ArrayList<Location>();
                        AccessMasterApplication.mApp.grops.put(location.parentid,locations);
                    }
                    locations.add(location);
                }
                else
                {
                    AccessMasterApplication.mApp.locations.add(location);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Device praseDevice(NetObject net, Context context)
    {
        try {
            String json = net.result;
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONObject jo = jsonObject.getJSONObject("data");
            Device device = new Device();
            if(!jo.getString("eqptLocationId").equals("null"))
            device.addressid = jo.getString("eqptLocationId");
            if(jo.has("entEqptLocation"))
            {
                device.address = jo.getString("entEqptLocation");
            }
            else
            {
                if(!jo.getString("eqptLocationId").equals("null"))
                {
                    Location location = AccessMasterApplication.mApp.locationHashMap.get(device.addressid);
                    if(location != null)
                    {
                        device.address = location.address;
                    }
                }
            }
            device.cname = jo.getString("entEqptName");
            if(!jo.getString("isAttendanceEqpt").equals("null"))
            device.isattence = jo.getString("isAttendanceEqpt");
            if(!jo.getString("isEntEqpt").equals("null"))
            device.isaccess = jo.getString("isEntEqpt");
            return device;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void praseUpdate(Context context , NetObject net)
    {
        String json = net.result;
        XpxJSONObject jsonObject = null;
        try {
            jsonObject = new XpxJSONObject(json);
            XpxJSONObject jo = jsonObject.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
